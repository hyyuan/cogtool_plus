package cogtoolplus_simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cogtoolplus.CogToolPlusCSVParser;
import cogtoolplus_interpreter.CallBackElement;
import cogtoolplus_interpreter.Variable;
import cogtoolplus_utility.sampleGenerator;

public class ImportSimulationXML {
	protected static final String COGTOOL_SIMU_ELT = "cogtoolplus_simulation";
	protected static final String TRIAL_ELT = "trial";
	protected static final String EXTERNAL_ELT = "external";
	protected static final String PREF_ELT = "pref-setting";
	protected static final String TO_META_ELT = "to-metamodel";
	protected static final String VAR_LIST_ELT = "variable-list";
	protected static final String VAR_ELT = "variable";
	protected static final String ID_ELT = "id";
	protected static final String VALUE_ELT = "value";
	protected static final String TYPE_ELT = "type";
	protected static final String IMPLY_THINK_ELT = "imply_think";
	protected static final String FITTS_COF = "fitts_cof";
	protected static final String FITTS_MIN = "fitts_min";
	protected static final String TYPE_ATTR = "type";
	protected static final String CUSTOMISE_VALUE = "customise";
	protected static final String DEFAULT_VALUE = "default";
	protected static final String STATIC_VALUE = "static";
	protected static final String DYNAMIC_VALUE = "dynamic";
	protected static final String TRUE_VALUE = "true";
	protected static final String FALSE_VALUE = "false";
	protected static final String RESEARCH_ELT = "research";
	protected static final String DISTRIBUTION_ELT = "distribution";
	protected static final String NAME_ATTR = "name";
	protected static final String SIZE_ELT = "size";
	protected static final String MEAN_ELT = "mean";
	protected static final String STD_ELT = "std";
	protected static final String LAMDA_ELT = "lamda";
	protected static final String GAUSSIAN = "gaussian";
	protected static final String EXPONENTIAL = "exponential";
	// callback xml
	protected static final String CALLBACK_ELT = "callback";
	protected static final String CB_FILE_ELT = "file";
	protected static final String FUNCTION_ELT = "function";
	protected static final String CB_ARG_LIST_ELT = "argumeent_list";
	protected static final String CB_DATA_ELT = "data_structure";
	protected static final String CB_ARG_ELT = "argument";
	protected static final String CB_ID_ELT = "id";
	protected static final String CB_LINK_ELT = "callback_link";
	
	// call back csv
	protected static final String CB_COLUMN_ELT = "column";
	protected static final String CB_ROW_ELT = "row";
	
	protected String prefType = null;
	protected String research = "false";
	protected String actr_par_type = "false";
	protected Integer fitts_cof;
	protected Integer fitts_min;
	protected ArrayList<HashMap<String, Object>> preferenceList = new ArrayList<HashMap<String, Object>>();
	protected HashMap<String, Object> prefSetting = new HashMap<String, Object>();
	
	protected String variableType = null;
	protected ArrayList<HashMap<String, Variable>> variableList = new ArrayList<HashMap<String, Variable>>();
    protected HashMap<String, Variable> variableMap = new HashMap<String, Variable>();
	protected Integer trial = 1;
	protected String path; // this is the path where stores javascript file, or
							// csv file

	public ImportSimulationXML(String _path) {
		path = _path;
	}

	public static class ImportFailedException extends RuntimeException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ImportFailedException(String msg, Throwable ex) {
			super(msg, ex);
		}

		public ImportFailedException(String msg) {
			super(msg);
		}
	}

	private String getAttributeValue(Node node, String attr) {
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			Node attributeNode = attributes.getNamedItem(attr);
			if (attributeNode != null) {
				return attributeNode.getNodeValue();
			}
		}
		return null;
	}

	public boolean importXML(File inputFile) throws IOException, SAXException, SecurityException {
		InputStream fis = null;
		fis = new FileInputStream(inputFile);
		boolean result = false;

		try {
			Reader input = new InputStreamReader(fis, "UTF-8");
			try {
				result = importXML(input, inputFile.getParent() + File.separator);
			} finally {
				input.close();
			}
		} finally {
			fis.close();
		}

		return result;
	}

	public boolean importXML(Reader input, String imageDirPath) throws IOException, SAXException {
		// Create a Xerces DOM Parser
		DOMParser parser = new DOMParser();
		// Parse the Document and traverse the DOM
		parser.parse(new InputSource(input));
		Document document = parser.getDocument();
		parseFile(document);
		return true;
	}

	@SuppressWarnings("null")
	private void parseFile(Node node) throws IOException {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(COGTOOL_SIMU_ELT)) {
					NodeList nlist = child.getChildNodes();
					if (nlist != null) {
						for (int j = 0; j < nlist.getLength(); j++) {
							Node sub_child = nlist.item(j);
							String subNodeName = sub_child.getNodeName();
							if (subNodeName.equalsIgnoreCase(TRIAL_ELT)) {
								trial = Integer.valueOf(sub_child.getTextContent());
							} else if (subNodeName.equalsIgnoreCase(PREF_ELT)) {
								if (getAttributeValue(sub_child, TYPE_ATTR).equalsIgnoreCase(DEFAULT_VALUE)) {
									prefType = "default";
									// TO DO
								} else if (getAttributeValue(sub_child, TYPE_ATTR).equalsIgnoreCase("static")) {
									prefSetting = parsePrefSettingStatic(sub_child);
									prefType = "static";
								} else if (getAttributeValue(sub_child, TYPE_ATTR).equalsIgnoreCase("dynamic")) {
									preferenceList = parsePrefSettingDynamic(sub_child);
									prefType = "dynamic";
								}
							} else if (subNodeName.equalsIgnoreCase(VAR_ELT)) {								
								variableMap = parseVariable(sub_child, variableMap);
							}
						}
					}
				}
			}
		} else {
			for (int i = 0; i < children.getLength(); i++) {
				parseFile(children.item(i));
			}
		}
	}

	private HashMap<String, Variable> parseVariable(Node node, HashMap<String, Variable> map) {
		//Variable vb = new Variable();
		String id = null;
		String type = null;
		Object value = null;
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(ID_ELT)){
					id = child.getTextContent();
				}
				else if(nodeName.equalsIgnoreCase(TYPE_ELT)){
					type = child.getTextContent();
				}
				else if(nodeName.equalsIgnoreCase(VALUE_ELT)){
					value = parseValue(child);
					//value = child.getTextContent();
				}
			}
			if (!map.containsKey(id)){
				map.put(id, new Variable(id, type, value));
			}
		}		
		return map;//new Variable(id, type, value);
	}

	private Object parseValue(Node node) {
		Object value = null;
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++){
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(CALLBACK_ELT)) {
					CallBackElement cb = parseCallBack(child);
					if (cb.getFileType().equalsIgnoreCase("xml")){
						//value = covert2DoubleArray((ArrayList<Object>)cb.getResult());
					}
					else if (cb.getFileType().equalsIgnoreCase("csv")){
						String dataType = cb.getDataType();
						switch(dataType){
						case "Integer":
							value = (Integer[]) cb.getResult(); 
							//Integer[] tlist = (Integer[]) cb.getResult(); 
							//System.out.println(tlist[0]);
							//System.out.println(tlist[1]);
							//System.out.println(tlist[2]);
							break;
						case "Double":
							value = (Double[]) cb.getResult(); 
							break;
						case "String":
							value = (String[]) cb.getResult(); 
							break;
						}						
					}
				}
			}
		}else{
			value = node.getTextContent();
		}
		return value;
	}

	private HashMap<String, Object> parsePrefSettingStatic(Node node) {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				prefSetting.put("RESEARCH", true);
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				if (nodeName.equalsIgnoreCase(FITTS_COF)) {
					prefSetting.put("PECK_FITTS_COEFF", Integer.valueOf(child.getTextContent()));
					prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				}
				else if (nodeName.equalsIgnoreCase(FITTS_MIN)) {
					prefSetting.put("MIN_FITTS", Integer.valueOf(child.getTextContent()));
					prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				}
				else if (nodeName.equalsIgnoreCase(IMPLY_THINK_ELT)){
					prefSetting.put("IMPLIED_THINK", Boolean.valueOf(child.getTextContent()));
				}
			}
		}
		return prefSetting;
	}

	private ArrayList<HashMap<String, Object>> parsePrefSettingDynamic(Node node) {
		NodeList children = node.getChildNodes();
		Double[] distributionFittsCof = null;
		Double[] distributionFittsMin = null;
		Boolean impliedThinkList = null;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(FITTS_COF)) {
					distributionFittsCof = parseFittsParameters(child);
				}
				else if (nodeName.equalsIgnoreCase(FITTS_MIN)) {
					distributionFittsMin = parseFittsParameters(child);
				}
				else if (nodeName.equalsIgnoreCase(IMPLY_THINK_ELT)){					
					impliedThinkList = Boolean.valueOf(child.getTextContent());
				}
			}
		}
		for (int i = 0; i < distributionFittsCof.length; i++) {
			HashMap<String, Object> preference = new HashMap<String, Object>();
			preference.put("PECK_FITTS_COEFF", distributionFittsCof[i].intValue());			
			preference.put("ACTR_ALTERNATIVE_PARAMETERS", true);
			preference.put("MIN_FITTS", distributionFittsMin[i].intValue());			
			preference.put("ACTR_ALTERNATIVE_PARAMETERS", true);			
			preference.put("IMPLIED_THINK", impliedThinkList);			
			preferenceList.add(preference);
		}
		return preferenceList;
	}

	// Parse fitts' law parameters as distribution
	@SuppressWarnings("unchecked")
	private Double[] parseFittsParameters(Node node) {
		Double[] distribution = null;
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(DISTRIBUTION_ELT)) {
					if (getAttributeValue(child, NAME_ATTR).equalsIgnoreCase(GAUSSIAN)) {
						distribution = parseGaussianDistribution(child);
					} else if (getAttributeValue(child, NAME_ATTR).equalsIgnoreCase(EXPONENTIAL)) {
						distribution = parseExponentialDistribution(child);
					}
				} else if (nodeName.equalsIgnoreCase(CALLBACK_ELT)) {
					CallBackElement cb = parseCallBack(child);
					if (cb.getFileType().equalsIgnoreCase("xml"))
						distribution = covert2DoubleArray((ArrayList<Object>)cb.getResult());
					else if (cb.getFileType().equalsIgnoreCase("csv")){
						String dataType = cb.getDataType();
						switch(dataType){
						case "Integer":
							//distribution = (Integer[]) cb.getResult(); 
							break;
						case "Double":
							distribution = (Double[]) cb.getResult(); 
							break;
						case "String":
							//distribution = (String[]) cb.getResult(); 
							break;
						}
					}
				}
			}
		}
		return distribution;
	}

	private Double[] covert2DoubleArray(ArrayList<Object> input){
		Double[] output = new Double[input.size()];
		for (int i=0; i<input.size(); i++){
			output[i] = (Double)input.get(i);
		}
		return output;
	}
	private CallBackElement parseCallBack(Node node) {
		String fileType = getAttributeValue(node, TYPE_ATTR);
		NodeList children = node.getChildNodes();
		CallBackElement cb = new CallBackElement();		
		cb.setFileType(fileType);
		String id = null;
		String file = null;
		String function = null;
		String dataStructure = null;
		Integer row = 0;
		Object output = null;
		List<Object> inputArguments = new ArrayList<Object>();
		if (fileType.equalsIgnoreCase("csv")) {
			if (children != null) {
				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();
					if (nodeName.equalsIgnoreCase(CB_FILE_ELT)) {
						String fileName = String.valueOf(child.getTextContent());
						file = path + "/" + fileName; // Get the absolute path
														// for java script file
						cb.setFile(file);
					}
					else if (nodeName.equalsIgnoreCase(CB_ROW_ELT)) {
						row = Integer.parseInt(child.getTextContent());
					}
					else if (nodeName.equalsIgnoreCase(CB_DATA_ELT)) {
						dataStructure = String.valueOf(child.getTextContent());
						cb.setDataType(dataStructure);
					}
				}
				// get a csv parser and save results
				CogToolPlusCSVParser parser = new CogToolPlusCSVParser();
				switch (dataStructure) {
				case "Double":
					cb.setResult(parser.DoubleArrayReadCSV(file).get(row));
					break;
				case "Integer":
					cb.setResult(parser.IntegerArrayReadCSV(file).get(row));
					break;
				case "String":
					cb.setResult(parser.StringArrayReadCSV(file).get(row));
					break;
				}								
			}
		} else if (fileType.equalsIgnoreCase("xml")) {
			if (children != null) {
				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();
					if (nodeName.equalsIgnoreCase(CB_ID_ELT)) {
						id = String.valueOf(child.getTextContent());
						cb.setID(id);
					}
					else if (nodeName.equalsIgnoreCase(CB_FILE_ELT)) {
						String fileName = String.valueOf(child.getTextContent());
						file = path + "/" + fileName; // Get the absolute path
														// for java script file
						cb.setFile(file);
					}
					else if (nodeName.equalsIgnoreCase(FUNCTION_ELT)) {
						function = String.valueOf(child.getTextContent()); // Get
																			// the
																			// function
						cb.setFunction(function);
					}
					else if (nodeName.equalsIgnoreCase(CB_ARG_LIST_ELT)) {
						inputArguments = parseArgument(child);
						cb.setInputArguments(inputArguments);
					}
					else if (nodeName.equalsIgnoreCase(CB_DATA_ELT)) {
						dataStructure = String.valueOf(child.getTextContent());
						cb.setDataType(dataStructure);
					}
				}
				ScriptEngineManager manager = new ScriptEngineManager();
				ScriptEngine engine = manager.getEngineByName("JavaScript");
				try {
					engine.eval(new FileReader(file));
					Invocable inv = (Invocable) engine;
					output = dynamicInvokeFunction(inv, function, inputArguments);
					cb.setResult(output);
				} catch (ScriptException | FileNotFoundException | NoSuchMethodException ex) {
				}
			}
		}
		return cb;
	}

	private Object dynamicInvokeFunction(Invocable inv, String function, List<Object> input)
			throws NoSuchMethodException, ScriptException {
		Object obj = null;
		int size = input.size();
		switch (size) {
		case 0:
			obj = inv.invokeFunction(function);
			break;
		case 1:
			obj = inv.invokeFunction(function, input.get(0));
			break;
		case 2:
			obj = inv.invokeFunction(function, input.get(0), input.get(1));
			break;
		case 3:
			obj = inv.invokeFunction(function, input.get(0), input.get(1), input.get(2));
			break;
		case 4:
			obj = inv.invokeFunction(function, input.get(0), input.get(1), input.get(2), input.get(3));
			break;
		case 5:
			obj = inv.invokeFunction(function, input.get(0), input.get(1), input.get(2), input.get(3), input.get(4));
			break;
		}
		return obj;
	}

	private List<Object> parseArgument(Node node) {
		NodeList children = node.getChildNodes();
		List<Object> inputArguments = new ArrayList<Object>();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(CB_ARG_ELT)) {
					String type = getAttributeValue(child, TYPE_ATTR);
					switch (type) {
					case "Integer":
						// if (child.getChildNodes().getLength() > 1)
						// inputArguments.add(((Double) parseCallBackLink(child,
						// idx)).intValue());
						// else
						inputArguments.add(Integer.valueOf(child.getTextContent()));
						break;
					case "String":
						// if (child.getChildNodes().getLength() > 1)
						// inputArguments.add((String) parseCallBackLink(child,
						// idx));
						// else
						inputArguments.add(String.valueOf(child.getTextContent()));
						// ;
						break;
					// case "ArrayList":
					// if (child.getChildNodes().getLength() > 1) {
					// inputArguments.add(parseCallBackLink(child, idx));
					// } else {
					// System.out.println("the argument for array list type is
					// wrong");
					// System.exit(0);
					// }
					// break;
					}
				}
			}
		}
		return inputArguments;
	}

	// Generate exponential distribution
	private Double[] parseExponentialDistribution(Node node) {
		NodeList children = node.getChildNodes();
		Integer size = 0;
		Double mean = 0.0;
		Double lamda = 0.0;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(SIZE_ELT)) {
					size = Integer.valueOf(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(MEAN_ELT)) {
					mean = Double.valueOf(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(LAMDA_ELT)) {
					lamda = Double.valueOf(child.getTextContent());
				}
			}
		}
		size = trial;
		sampleGenerator gen = new sampleGenerator();
		Double[] distribution = gen.exponentialDistribution(size, mean, lamda);
		return distribution;
	}

	// Generate guassian distribution
	private Double[] parseGaussianDistribution(Node node) {
		NodeList children = node.getChildNodes();
		Integer size = 0;
		Double mean = 0.0;
		Double std = 0.0;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(SIZE_ELT)) {
					size = Integer.valueOf(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(MEAN_ELT)) {
					mean = Double.valueOf(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(STD_ELT)) {
					std = Double.valueOf(child.getTextContent());
				}
			}
		}
		size = trial;
		sampleGenerator gen = new sampleGenerator();
		Double[] distribution = gen.normalDistribution(size, mean, std);
		return distribution;
	}

	public String getPrefType() {
		return prefType;
	}

	public ArrayList<HashMap<String, Object>> getPreferenceList() {
		return preferenceList;
	}

	public Integer getTrialNumber() {
		return trial;
	}

	public HashMap<String, Object> getPreSetting() {
		return prefSetting;
	}
	public HashMap<String, Variable> getVariableMap(){
		return variableMap;
	
	}
}
