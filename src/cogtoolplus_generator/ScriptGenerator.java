package cogtoolplus_generator;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

import edu.cmu.cs.hcii.cogtool.model.Design;
import edu.cmu.cs.hcii.cogtool.model.ImportCogToolXML;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;
import edu.cmu.cs.hcii.cogtool.model.Project.ITaskDesign;
import edu.cmu.cs.hcii.cogtool.util.XMLOutput;

public class ScriptGenerator {
	public ScriptGenerator() {
	}
	protected static String prjName;
	
	private ArrayList<int[]> hiddenIndex = null;
	private int[] passIndex = null;
	private int[] buttonIndex = null;
	private XMLOutput xmlWriter = null;

	public void setProjectName(String name){
		prjName = name;
	}
	public String getProjectName(){
		return prjName;
	}
	
	public int[] getButtonIndex() {
		return buttonIndex;
	}

	public int[] getPassIndex() {
		return passIndex;
	}

	public void setHiddenIndex() {
		int[] tmp1 = { 3, 4, 5, 1, 2 };
		hiddenIndex.add(0, tmp1);
		int[] tmp2 = { 1, 2, 3, 4, 5 };
		hiddenIndex.add(0, tmp2);
		int[] tmp3 = { 4, 5, 1, 2, 3 };
		hiddenIndex.add(0, tmp3);
		int[] tmp4 = { 5, 1, 2, 3, 4 };
		hiddenIndex.add(0, tmp4);
		int[] tmp5 = { 2, 3, 4, 5, 1 };
		hiddenIndex.add(0, tmp5);
	}

	public void setPassIndex() {
		passIndex = new int[] { 1, 2, 3, 4, 5 };
	}

	public void setButtonIndex() {
		buttonIndex = new int[] { 1, 2, 3, 4, 5 };
	}


	public void setXML(String characterEncoding, String fileName, Design design, Map<ITaskDesign, TaskApplication> designTAs) throws IOException {
		OutputStream oStream;
		try {
			oStream = new FileOutputStream(fileName);
			Writer sink = new BufferedWriter(new OutputStreamWriter(oStream, characterEncoding));
			XMLOutput xmlWriter = new XMLOutput(sink, characterEncoding);
			// write header
			xmlWriter.outputXMLHeader();
			// write version
			xmlWriter.startElement(ImportCogToolXML.COGTOOL_IMPORT_ELT, ImportCogToolXML.VERSION_ATTR,
					ImportCogToolXML.CURRENT_VERSION);
			xmlWriter.noMoreAttributes(XMLOutput.HAS_NESTED_DATA);
						
			// write design
			ExportCogToolXMLPlus cogtoolXML = new ExportCogToolXMLPlus();
		    //Map<ITaskDesign, TaskApplication> designTAs = null;
		    cogtoolXML.outputDesign(design,
                    designTAs,
                    xmlWriter);
			//cogtoolXML.outputDesign(xmlWriter, design);
			// ADD DESIGN AND DEMONSTRATION HERE
			
			xmlWriter.endElement();			
			sink.flush();
			oStream.flush();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
	}
}
