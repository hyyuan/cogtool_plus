package cogtoolplus_analyser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ImportVisualiserXML {
	private static final String ANALYSE_ELT = "cogtoolplus_visual";
	private static final String BAR_ELT = "bar_chart";
	private static final String HISTOGRAM_ELT = "histogram";
    private static final String SEPARATE_ATTR = "separate";
    private static final String PROPERTY_ATTR = "property";
    private static final String TITLE_ELT = "title";
	private static final String X_TITLE_ELT = "x_title";
	private static final String Y_TITLE_ELT = "y_title";
	private static final String DATA_ELT = "data";
	private static final String DATASET_ELT = "dataset";
	private static final String NAME_ATTR = "name";
	private static final String OPERATION_ELT = "operation";
	private ArrayList<GraphElement> graphList = new ArrayList<GraphElement>(); 	
	
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

	public ArrayList<GraphElement> getAllGraphs(){
		return graphList;
	}
	@SuppressWarnings("null")
	private void parseFile(Node node) throws IOException {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(ANALYSE_ELT)) {
					NodeList nlist = child.getChildNodes();
					if (nlist != null) {
						for (int j = 0; j < nlist.getLength(); j++) {
							Node sub_child = nlist.item(j);
							String subNodeName = sub_child.getNodeName();
							if (subNodeName.equalsIgnoreCase(BAR_ELT)) {
								graphList.add(parseBarChart(sub_child));
							} else if (subNodeName.equalsIgnoreCase(HISTOGRAM_ELT)) {
								graphList.add(parseHistogram(sub_child));
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

	private GraphElement parseHistogram(Node node) {
		GraphElement gElement = new GraphElement();
		ArrayList<ArrayList<String>> dataSetList = new ArrayList<ArrayList<String>>();
		gElement.setType("histogram");
		//String graphAttr = getAttributeValue(node, SEPARATE_ATTR);
		String propAttr = getAttributeValue(node, PROPERTY_ATTR);
		gElement.setProperty(propAttr);
		//gElement.setMultipleGraphs(graphAttr);
		NodeList children = node.getChildNodes();
		if (children != null) {			
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();		
				if (nodeName.equalsIgnoreCase(TITLE_ELT)) {
					gElement.setTitle(child.getTextContent());
				}
				else if (nodeName.equalsIgnoreCase(X_TITLE_ELT)){	
					gElement.setXTitle(child.getTextContent());
				}
				else if (nodeName.equalsIgnoreCase(Y_TITLE_ELT)){
					gElement.setYTitle(child.getTextContent());
				}
				else if (nodeName.equalsIgnoreCase(DATASET_ELT)){	
					dataSetList.add(parseDataset(child));
					gElement.setDataSet(parseDataset(child));
				}
				else if(nodeName.equalsIgnoreCase(OPERATION_ELT)){
					gElement.setOperation(child.getTextContent());
				}
			}
		}
		gElement.setDataSetList(dataSetList);
		return gElement;
	}

	private ArrayList<String> parseDataset(Node node) {
		NodeList children = node.getChildNodes();
		String name = getAttributeValue(node, NAME_ATTR);
		ArrayList<String> dataset = new ArrayList<String>();
		dataset.add(name);
		if (children != null) {			
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();	
				if (nodeName.equalsIgnoreCase(DATA_ELT)) {
					dataset.add(child.getTextContent());
				}
			}
		}
		return dataset;
	}

	private GraphElement parseBarChart(Node node) {
		NodeList children = node.getChildNodes();
		ArrayList<ArrayList<String>> dataSetList = new ArrayList<ArrayList<String>>();
		GraphElement gElement = new GraphElement();
		gElement.setType("barchart");
		//String graphAttr = getAttributeValue(node, SEPARATE_ATTR);
		String propAttr = getAttributeValue(node, PROPERTY_ATTR);
		gElement.setProperty(propAttr);
		//gElement.setMultipleGraphs(graphAttr);
		if (children != null) {			
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();		
				if (nodeName.equalsIgnoreCase(TITLE_ELT)) {
					gElement.setTitle(child.getTextContent());
				}
				else if (nodeName.equalsIgnoreCase(X_TITLE_ELT)){	
					gElement.setXTitle(child.getTextContent());
				}
				else if (nodeName.equalsIgnoreCase(Y_TITLE_ELT)){
					gElement.setYTitle(child.getTextContent());
				}
				else if (nodeName.equalsIgnoreCase(DATASET_ELT)){	
					dataSetList.add(parseDataset(child));
					gElement.setDataSet(parseDataset(child));
				}else if(nodeName.equalsIgnoreCase(OPERATION_ELT)){
					gElement.setOperation(child.getTextContent());
				}
			}
		}
		gElement.setDataSetList(dataSetList);
		return gElement;
	}
}
