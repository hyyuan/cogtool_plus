package cogtoolplus_interpreter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ImportIOXML {
	protected static final String COGTOOL_CONF_ELT = "cogtoolplus_configure";
	protected static final String IO_ELT = "io";
	protected static final String DIR_PATH_ELT = "dir";
	protected static final String DIRJS_PATH_ELT = "dir_js";
	protected static final String DIROUT_PATH_ELT = "dir_output";
	protected String dirPath = null;
	protected String dirJSPath = null;
	protected String dirOutPath = null;
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
				//System.out.println(nodeName);
				if (nodeName.equalsIgnoreCase(COGTOOL_CONF_ELT)) {
					NodeList nlist = child.getChildNodes();
					if (nlist != null) {
						for (int j = 0; j < nlist.getLength(); j++) {
							Node sub_child = nlist.item(j);
							String subNodeName = sub_child.getNodeName();
							if (subNodeName.equalsIgnoreCase(IO_ELT)) {
								parseIO(sub_child);
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
	private void parseIO(Node node){
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(DIR_PATH_ELT)) {
					dirPath = child.getTextContent();
				}
				else if(nodeName.equalsIgnoreCase(DIRJS_PATH_ELT)){
					dirJSPath = child.getTextContent();
				}
				else if(nodeName.equalsIgnoreCase(DIROUT_PATH_ELT)){
					dirOutPath = child.getTextContent();
				}
			}
		}		
	}
	public String getDirPath() {
		return dirPath;
	}
	public String getJSPath(){
		return dirJSPath;
	}
	public String getOutputPath(){
		return dirOutPath;
	}
}
