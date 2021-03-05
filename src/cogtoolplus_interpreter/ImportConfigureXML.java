package cogtoolplus_interpreter;

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

import edu.cmu.cs.hcii.cogtool.model.TaskParent;

public class ImportConfigureXML {
	protected static final String COGTOOL_CONF_ELT = "cogtoolplus_configure";
	protected static final String IO_ELT = "io";
	protected static final String INPUT_PATH_ELT = "input_path";
	protected static final String INTER_PATH_ELT = "intermediate_path";
	protected static final String OUTPUT_PATH_ELT = "output_path";
	protected static final String JS_PATH_ELT = "js_path";
	
	// simulation part of cogtool+ meta model
	protected static final String SIMULATION_ELT = "simulation";
	protected static final String TRIAL_ELT = "trial";
	protected static final String EXTERNAL_ELT = "external";
	
	protected static final String RESEARCH_ELT = "research";
	protected static final String PREF_ELT = "pref-setting";
	protected static final String FITTS_A_ELT = "fitts_cof";
	protected static final String FITTS_B_ELT = "fitts_min";
	protected static final String TYPE_ATTR = "type";

	protected static final String CUSTOMISE_VALUE = "customise";
	protected static final String DEFAULT_VALUE = "default";
	protected static final String STATIC_VALUE = "static";
	protected static final String DYNAMIC_VALUE = "dynamic";
	protected static final String TRUE_VALUE = "true";
	protected static final String FALSE_VALUE = "false";

	// callback
	public static final String CALLBACK_ELT = "callback";
	public static final String CB_FILE_ELT = "file";
	public static final String FUNCTION_ELT = "function";
	public static final String CB_ARG_LIST_ELT = "argumeent_list";
	public static final String CB_DATA_ELT = "data_structure";
	public static final String CB_ARG_ELT = "argument";
	public static final String CB_ID_ELT = "id";
	public static final String CB_LINK_ELT = "callback_link";
	
	protected String jsPath = null;
	protected String inputPath = null;
	protected String interPath = null;
	protected String outputPath = null;
	protected String prefType = null;
	protected String research = "false";
	protected String actr_par_type = "false";
	protected Integer fitts_cof;
	protected Integer fitts_min;
	protected ArrayList<HashMap<String, Object>> preferenceList = new ArrayList<HashMap<String, Object>>();
	protected HashMap<String, Object> prefSetting = new HashMap<String, Object>();
	protected Integer trial = 1;

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

	/*
	 * public boolean importXML(File inputFile) throws IOException,
	 * SAXException, SecurityException { InputStream fis = null; fis = new
	 * FileInputStream(inputFile); boolean result = false;
	 * 
	 * try { Reader input = new InputStreamReader(fis, "UTF-8");
	 * 
	 * try { result = importXML(input, inputFile.getParent() + File.separator);
	 * 
	 * } finally { input.close(); } } finally { fis.close(); }
	 * 
	 * return result; }
	 */
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
				//System.out.println(nodeName);
				if (nodeName.equalsIgnoreCase(COGTOOL_CONF_ELT)) {
					NodeList nlist = child.getChildNodes();
					if (nlist != null) {
						for (int j = 0; j < nlist.getLength(); j++) {
							Node sub_child = nlist.item(j);
							String subNodeName = sub_child.getNodeName();
							if (subNodeName.equalsIgnoreCase(IO_ELT)) {
								parseIO(sub_child);
							}/*
							else if (subNodeName.equalsIgnoreCase(PREF_ELT)) {
								// String tmp = getAttributeValue(child,
								// TYPE_ATTR);
								if (getAttributeValue(sub_child, TYPE_ATTR).equalsIgnoreCase(DEFAULT_VALUE)) {
									prefType = "default";
								} else if (getAttributeValue(sub_child, TYPE_ATTR).equalsIgnoreCase("static")) {
									prefSetting = parsePrefSetting(sub_child);
									preferenceList.add(parsePrefSetting(sub_child));
									prefType = "static";
								}
							}*/
							else if (subNodeName.equalsIgnoreCase(SIMULATION_ELT)){
								parseSimulation(sub_child);
							}
						}
					}
				}
			}
			// }
		} else {
			for (int i = 0; i < children.getLength(); i++) {
				parseFile(children.item(i));
			}
		}
	}

	private void parseSimulation(Node node) throws IOException {
		NodeList children = node.getChildNodes();
		if (children != null) {			
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(TRIAL_ELT)) {
					trial = Integer.valueOf(child.getTextContent());
				}
				else if (nodeName.equalsIgnoreCase(EXTERNAL_ELT)){					
				}
				else if (nodeName.equalsIgnoreCase(PREF_ELT)) {
					if (getAttributeValue(child, TYPE_ATTR).equalsIgnoreCase(DEFAULT_VALUE)) {

					} if (getAttributeValue(child, TYPE_ATTR).equalsIgnoreCase(DEFAULT_VALUE)) {
						prefType = "default";
					} else if (getAttributeValue(child, TYPE_ATTR).equalsIgnoreCase("static")) {
						prefSetting = parsePrefSettingStatic(child);
						//preferenceList.add(parsePrefSettingStatic(child));
						prefType = "static";
					} else if (getAttributeValue(child, TYPE_ATTR).equalsIgnoreCase("dynamic")){
						preferenceList = parsePrefSettingDynamic(child);
						prefType = "dynamic";
					}
				}
			}
		}	
	}
	
	private void parseIO(Node node){
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(INPUT_PATH_ELT)) {
					inputPath = child.getTextContent();
				}
				else if (nodeName.equalsIgnoreCase(INTER_PATH_ELT)) {
					interPath = child.getTextContent();
				}
				else if (nodeName.equalsIgnoreCase(OUTPUT_PATH_ELT)) {
					outputPath = child.getTextContent();
				}
				else if(nodeName.equalsIgnoreCase(JS_PATH_ELT)){
					jsPath = child.getTextContent();
				}
			}
		}		
	}
	private HashMap<String, Object> parsePrefSettingStatic(Node node) {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(RESEARCH_ELT)) {
					if (child.getTextContent().equalsIgnoreCase(TRUE_VALUE)) {
						prefSetting.put("RESEARCH", true);
						prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
					} else if (child.getTextContent().equalsIgnoreCase(FALSE_VALUE)) {
						prefSetting.put("RESEARCH", false);
						prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", false);
					}
				}
				if (nodeName.equalsIgnoreCase(FITTS_A_ELT)) {
					prefSetting.put("PECK_FITTS_COEFF", Integer.valueOf(child.getTextContent()));
					prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				}
				if (nodeName.equalsIgnoreCase(FITTS_B_ELT)) {
					prefSetting.put("MIN_FITTS", Integer.valueOf(child.getTextContent()));
					prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				}
				// TO BE CONTINUED

			}
		}
		return prefSetting;// return null;
	}
	
	private ArrayList<HashMap<String, Object>> parsePrefSettingDynamic(Node node) {
		NodeList children = node.getChildNodes();		
		CallBackElement cb_fittsA = null;
		CallBackElement cb_fittsB = null;
		Integer fittsA = null;
		Integer fittsB = null;
		Boolean research = false;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(RESEARCH_ELT)) {
					if (child.getTextContent().equalsIgnoreCase(TRUE_VALUE)) {
						research = true;
						//prefSetting.put("RESEARCH", true);
						//prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
					} else if (child.getTextContent().equalsIgnoreCase(FALSE_VALUE)) {
						research = false;
						//prefSetting.put("RESEARCH", false);
						//prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", false);
					}
				}
				if (nodeName.equalsIgnoreCase(FITTS_A_ELT)) {
					if (child.getChildNodes().getLength()>1){
						NodeList subchildren = child.getChildNodes();
						for(int m = 0; m<subchildren.getLength();m++){
							String nName = subchildren.item(m).getNodeName();
							if (nName.equalsIgnoreCase(CALLBACK_ELT)) {
								cb_fittsA = parseCallBack(subchildren.item(m));								
							}
						}
					}else{
						fittsA = Integer.valueOf(child.getTextContent());
						//prefSetting.put("PECK_FITTS_COEFF", Integer.valueOf(child.getTextContent()));
						//prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
					}
				}
				if (nodeName.equalsIgnoreCase(FITTS_B_ELT)) {
					if (child.getChildNodes().getLength()>1){
						NodeList subchildren = child.getChildNodes();
						for(int m = 0; m<subchildren.getLength();m++){
							String nName = subchildren.item(m).getNodeName();
							if (nName.equalsIgnoreCase(CALLBACK_ELT)) {
								cb_fittsB = parseCallBack(subchildren.item(m));
							}
						}
					}else{
						fittsB = Integer.valueOf(child.getTextContent());
						//prefSetting.put("MIN_FITTS", Integer.valueOf(child.getTextContent()));
						//prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
					}
				}
				// TO BE CONTINUED
			}
		}
		// TO BE NOTICED THAT: the size of the call back results must be the same as the trial number
		for(int i=0;i<trial;i++){
			if(cb_fittsA!=null && cb_fittsB!=null){
				HashMap<String, Object> prefSetting = new HashMap<String, Object>();
				prefSetting.put("RESEARCH", true);
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				// TO DO HERE ABOUT TYPE
				ArrayList<Integer> tmp1 = (ArrayList<Integer>) cb_fittsA.getResult();				
				prefSetting.put("PECK_FITTS_COEFF", tmp1.get(i));			
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				ArrayList<Integer> tmp2 = (ArrayList<Integer>) cb_fittsB.getResult();
				prefSetting.put("MIN_FITTS", tmp2.get(i));
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);	
				preferenceList.add(prefSetting);
			}else if (cb_fittsA!=null && cb_fittsB==null){
				HashMap<String, Object> prefSetting = new HashMap<String, Object>();
				prefSetting.put("RESEARCH", true);
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				// TO DO HERE ABOUT TYPE
				ArrayList<Integer> tmp1 = (ArrayList<Integer>) cb_fittsA.getResult();				
				prefSetting.put("PECK_FITTS_COEFF", tmp1.get(i));
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				prefSetting.put("MIN_FITTS", fittsB);
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);	
				preferenceList.add(prefSetting);
			}else if(cb_fittsB!=null && cb_fittsA==null){
				HashMap<String, Object> prefSetting = new HashMap<String, Object>();
				prefSetting.put("RESEARCH", true);
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				// TO DO HERE ABOUT TYPE
				prefSetting.put("PECK_FITTS_COEFF", fittsA);
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);
				ArrayList<Integer> tmp2 = (ArrayList<Integer>) cb_fittsB.getResult();
				prefSetting.put("MIN_FITTS", tmp2.get(i));
				prefSetting.put("ACTR_ALTERNATIVE_PARAMETERS", true);	
				preferenceList.add(prefSetting);
			}			
		}
		return preferenceList;// return null;
	}

	private CallBackElement parseCallBack(Node node) {
		NodeList children = node.getChildNodes();
		CallBackElement cb = new CallBackElement();
		String id = null;
		String file = null;
		String function = null;
		String dataStructure = null;
		Object output = null;
		List<Object> inputArguments = new ArrayList<Object>();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(CB_ID_ELT)) {
					id = String.valueOf(child.getTextContent());
					cb.setID(id);
				}
				if (nodeName.equalsIgnoreCase(CB_FILE_ELT)) {
					String fileName = String.valueOf(child.getTextContent());
					file = jsPath + "/" + fileName; // Get the absolute path for java script file					
					cb.setFile(file);
				}
				if (nodeName.equalsIgnoreCase(FUNCTION_ELT)) {
					function = String.valueOf(child.getTextContent()); // Get
																		// the
																		// function
					cb.setFunction(function);
				}
				if (nodeName.equalsIgnoreCase(CB_ARG_LIST_ELT)) {
					inputArguments = parseArgument(child);
					cb.setInputArguments(inputArguments);
				}
				if (nodeName.equalsIgnoreCase(CB_DATA_ELT)) {
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
		//if (cb.containsID())
		//	localCallBackMap.put(id, cb);
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
						//if (child.getChildNodes().getLength() > 1)
						//	inputArguments.add(((Double) parseCallBackLink(child, idx)).intValue());
						//else
							inputArguments.add(Integer.valueOf(child.getTextContent()));
						break;
					case "String":
						//if (child.getChildNodes().getLength() > 1)
						//	inputArguments.add((String) parseCallBackLink(child, idx));
						//else
							inputArguments.add(String.valueOf(child.getTextContent()));
						//;
						break;
					//case "ArrayList":
						//if (child.getChildNodes().getLength() > 1) {
						//	inputArguments.add(parseCallBackLink(child, idx));
						//} else {
						//	System.out.println("the argument for array list type is wrong");
						//	System.exit(0);
						//}
						//break;
					}
				}
			}
		}
		return inputArguments;
	}
	public String getPrefType(){
		return prefType;
	}
	public ArrayList<HashMap<String, Object>> getPreferenceList(){
		return preferenceList;
	}
	public Integer getTrialNumber(){
		return trial;
	}
	public HashMap<String, Object> getPreSetting() {
		return prefSetting;
	}
	public String getInputPath() {
		return inputPath;
	}
	public String getInterPath() {
		return interPath;
	}
	public String getOutputPath() {
		return outputPath;
	}
	public String getJsPath(){
		return jsPath;
	}
}
