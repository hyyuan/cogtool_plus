package cogtoolplus_interpreter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
public class ImportMixedModelXML {
	private static final String MIXED_MODEL_ELT = "cogtoolplus_mixed";
	private static final String LEVEL_ELT = "level";
	//private static final String LEVEL_ID_ELT = "level_id";
	private static final String PROPERTY_ELT = "property";
	private static final String ID_ELT= "id";
	private static final String MODEL_LIST_ELT = "model_list";
	private static final String MODEL_ELT = "model";
	private static final String SIM_MODEL_ELT = "simulated_level_model";
	private static final String SIMULATION_ELT = "simulation_name";
	private static final String MODEL_NAME_ELT = "model_name";
	private static final String WEIGHT_ELT = "weight";
	private static final String NAME_ELT = "name";
	private static final String TRUE = "true";
	private static final String FALSE = "false";
	protected HashMap<String, Object> mixedModel = new HashMap<String, Object>();
	protected String projectName = "project"; //default name
	protected ArrayList<LevelElement> levelList = new ArrayList<LevelElement>();
	protected Boolean weightError = false;
	
	public static class ImportFailedException extends RuntimeException {
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
	
	private void parseFile(Node node) throws IOException {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(MIXED_MODEL_ELT)){
					NodeList nlist = child.getChildNodes();
					if (nlist != null) {
						for (int j = 0; j < nlist.getLength(); j++) {
							Node sub_child = nlist.item(j);
							String subNodeName = sub_child.getNodeName();
							if (subNodeName.equalsIgnoreCase(NAME_ELT)){
								projectName = sub_child.getTextContent();
							}
							else if (subNodeName.equalsIgnoreCase(LEVEL_ELT)) {
								levelList.add(parseLevel(sub_child));
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
	private LevelElement parseLevel(Node node) {
		String property = null;
		String levelID = null;
		NodeList list = node.getChildNodes();
		LevelElement level = new LevelElement();
		if (list != null) {
			for (int m = 0; m < list.getLength(); m++) {
				Node child = list.item(m);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(PROPERTY_ELT)){
					property = child.getTextContent();
					level.setProperty(property);
				}
				else if(nodeName.equalsIgnoreCase(ID_ELT)){	
					//levelID = parseID(child);
					levelID = child.getTextContent();
					level.setID(levelID);
				}
				else if (nodeName.equalsIgnoreCase(MODEL_LIST_ELT)) {								
					level.setModelList(parseModelList(child));								
				}
			}
		}	
		return level;
	}
/*
	private String parseID(Node node) {
		String id = null; 
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(ID_ELT)) {
					id = child.getTextContent();
				}
			}
		}
		return id;
	}
	*/
	private ArrayList<ModelElement> parseModelList(Node node) {
		String levelID = null; 
		NodeList children = node.getChildNodes();
		ArrayList<ModelElement> modelList= new ArrayList<ModelElement>();
		float weightSum = 0;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(MODEL_ELT)) {
					ModelElement tmpModel = parseModel(child);
					modelList.add(tmpModel);
					weightSum += tmpModel.getWeight();
				}
				if (nodeName.equalsIgnoreCase(SIM_MODEL_ELT)){
					ModelElement tmpModel = parseSimulatedLevelModel(child);
					modelList.add(tmpModel);	
					weightSum += tmpModel.getWeight();
				}
			}
		}
		if (weightSum!=1){
			weightError = true;
		}
		return modelList;
	}

	private SimulatedLevelElement parseSimulatedLevelModel(Node node) {		
		NodeList children = node.getChildNodes();		
		ModelElement model = new ModelElement();
		if (children != null) {	
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				String level_id = null;
				Double weight = null;
				if (nodeName.equalsIgnoreCase(ID_ELT)) {								
					level_id = child.getTextContent();
					model.setID(level_id);
				}
				else if (nodeName.equalsIgnoreCase(WEIGHT_ELT)) {
					weight = Double.valueOf(child.getTextContent());
					model.setWeight(weight);
				}
			}
		}
		SimulatedLevelElement sModel = new SimulatedLevelElement(model);
		return sModel;
	}

	private ModelElement parseModel(Node node) {
		NodeList children = node.getChildNodes();
		ModelElement model = new ModelElement();
		if (children != null) {	
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				String modelId = null;
				String modelName = null;
				String simulationFile = null;
				Double weight = null;
				if(nodeName.equalsIgnoreCase(ID_ELT)){
					//modelId = parseID(child);
					modelId = child.getTextContent();
					model.setID(modelId);
				}
				else if (nodeName.equalsIgnoreCase(MODEL_NAME_ELT)) {
					modelName = child.getTextContent();
					model.setModelName(modelName);
				}
				else if(nodeName.equalsIgnoreCase(SIMULATION_ELT)){
					simulationFile = child.getTextContent();
					model.setSimulationName(simulationFile);
				}
				else if (nodeName.equalsIgnoreCase(WEIGHT_ELT)) {
					weight = Double.valueOf(child.getTextContent());
					model.setWeight(weight);
				}
			}
		}
		return model;
	}
	public String getProjectName(){
		return projectName;
	}
	public ArrayList<LevelElement> getLevelModels(){
		return levelList;
	}
	public Boolean getErrorMsgWeight(){
		return weightError;
	}
}
