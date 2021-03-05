
package cogtoolplus_interpreter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.xml.sax.SAXException;

import cogtoolplus_simulator.CogToolPrefCP;
import edu.cmu.cs.hcii.cogtool.controller.ProjectCmd;
import edu.cmu.cs.hcii.cogtool.controller.ControllerCP;
import edu.cmu.cs.hcii.cogtool.model.CognitiveModelGenerator;
import edu.cmu.cs.hcii.cogtool.model.Demonstration;
import edu.cmu.cs.hcii.cogtool.model.Design;
import edu.cmu.cs.hcii.cogtool.model.Frame;
import edu.cmu.cs.hcii.cogtool.model.IWidget;
import edu.cmu.cs.hcii.cogtool.model.KLMCognitiveGenerator;
import edu.cmu.cs.hcii.cogtool.model.Project;
import edu.cmu.cs.hcii.cogtool.model.TaskParent;
import edu.cmu.cs.hcii.cogtool.ui.DesignSelectionState;
import edu.cmu.cs.hcii.cogtool.ui.ProjectContextSelectionState;
import edu.cmu.cs.hcii.cogtool.ui.ProjectLID;
import edu.cmu.cs.hcii.cogtool.util.CompoundUndoableEdit;
import edu.cmu.cs.hcii.cogtool.util.IUndoableEditSequence;
import edu.cmu.cs.hcii.cogtool.util.L10N;
import edu.cmu.cs.hcii.cogtool.util.NamedObjectUtil;
import edu.cmu.cs.hcii.cogtool.util.UndoManager;

public class MetaModelInterpreter {
	protected static CognitiveModelGenerator MODELGEN_ALG = KLMCognitiveGenerator.ONLY;
	protected Project project = new Project();
	protected static final String importXMLPresentation = L10N.get("UNDO.PC.ImportXML", "Import XML");
	protected Map<Design, Collection<Demonstration>> parsedDesigns = null;
	protected File importFile = null;
	protected UndoManager undoMgr = null;
	protected HashMap<String, ArrayList<String>> widgetGroup;
	protected HashMap<String, IWidget> dynamicWidgets;
	protected ArrayList<String> taskOrderList;
	protected HashMap<String, TaskElement> taskMap;
	protected String startingFrame; 
	protected String userHand;
	protected String demonstrationName;
	protected HashMap<String, CallBackElement> localCallBackMap = new HashMap<String, CallBackElement>();
	protected HashMap<String, CallBackElement> globalCallBackMap = new HashMap<String, CallBackElement>();
	protected HashMap<String, ArrayList<TaskElement>> frameParameterMap = new HashMap<String, ArrayList<TaskElement>>();
	protected ArrayList<String> frameNameList = new ArrayList<String>();
//	protected HashMap<String, Object> prefSetting = new HashMap<String, Object>();
	public MetaModelInterpreter() {			
	}	
	public Project getModel() {
		return project;
	}	
	public String getName(){
		String tmp = importFile.getName();
		String name = (String) tmp.subSequence(0, tmp.lastIndexOf("."));
		return name;
	}	
	
	/*private void settingPref(HashMap<String, Object> PrefSetting){
		if(PrefSetting.size()!=0){
			Set<String> keys = PrefSetting.keySet();
			Iterator<String> itr = keys.iterator();
			while(itr.hasNext()){
				String setting = itr.next();
				switch(setting){
				case "PECK_FITTS_COFF":
					CogToolPrefCP.PECK_FITTS_COEFF.setInt((int) PrefSetting.get(setting));
					break;
				case "MIN_FITTS":
					CogToolPrefCP.MIN_FITTS.setInt((int) PrefSetting.get(setting));
					break;
				case "RESEARCH":
					CogToolPrefCP.RESEARCH.setBoolean((boolean) PrefSetting.get(setting));
					break;
				case "ACTR_ALTERNATIVE_PARAMETERS":
					CogToolPrefCP.ACTR_ALTERNATIVE_PARAMETERS.setBoolean((boolean) PrefSetting.get(setting));
					break;
				case "ACTRDAT":
					CogToolPrefCP.ACTR_DAT.setInt((int) PrefSetting.get(setting));
					break;
				case "VISUAL_ATTENTION":
					CogToolPrefCP.VISUAL_ATTENTION.setInt((int) PrefSetting.get(setting));
					break;
				}
			}
		}
	}*/
	public void loadXML(String inputPath, String jsPath,  String file, HashMap<String, Variable> variableMap, Integer currentTrial) {		
						
		ControllerCP.newProjectController(); // inherit from cogtool
		UndoManager undoMgr = UndoManager.getUndoManager(project); // inherit from cogtool 
		CompoundUndoableEdit editSeq = new CompoundUndoableEdit(importXMLPresentation, ProjectLID.ImportXML); // inherit from cogtool
		String fileName = inputPath + "/" + file; // construct path for meta model file
		importFile = new File(fileName);
		TaskParent parent = project;
		ImportMetaModelXML importer = new ImportMetaModelXML(); 
		importer.setAbsolutePath(jsPath); // set and pass path to importer
		try {
			importer.importXML(importFile, parent, MODELGEN_ALG, variableMap, currentTrial);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parsedDesigns = importer.getDesigns();

		ProjectContextSelectionState sel = new ProjectContextSelectionState(project);
		Iterator<Map.Entry<Design, Collection<Demonstration>>> designDemos = parsedDesigns.entrySet().iterator();

		while (designDemos.hasNext()) {
			Map.Entry<Design, Collection<Demonstration>> entry = designDemos.next();			
			importDesign(parent, sel, 
					entry.getKey(), editSeq);
			//System.out.println(entry.getKey().getName());
		}
		editSeq.end();
		undoMgr.addEdit(editSeq);
		widgetGroup = importer.getWidgetGroup();
		dynamicWidgets = importer.getDynamicWidgetList();
		taskOrderList = importer.getTaskOrderList();
		taskMap = importer.getTaskMap();
		startingFrame = importer.getStartingFrame();
		//trial = importer.getTrialNumber();
		userHand = importer.getUserHand();
		demonstrationName = importer.getDemonstrationName();
		localCallBackMap = importer.getLocalCallBack();
		globalCallBackMap = importer.getGlobalCallBack();
		frameParameterMap = importer.getParameterMap();
		frameNameList = importer.getFrameNameList();
		//prefSetting = importer.getPreference();
		//settingPref(prefSetting);
		//System.out.println(demonstrationName);
		//return project;
		importer = null;
	}
	public Design getDesign(){
		return project.getDesigns().get(0);
	}
	public HashMap<String, Frame> getFrameMap(){
		HashMap<String, Frame> frameMap = new HashMap<String, Frame>();		
		Design design = project.getDesigns().get(0);		
		Set<Frame> frameList = design.getFrames();				
		Iterator<Frame> itr = frameList.iterator();				
		while(itr.hasNext()){
			Frame temp = itr.next();
			frameMap.put(temp.getName(), temp);
		}	
		return frameMap;
	}
	public Project getProject(){
		return project;
	}
	/*public HashMap<String, Object> getPreference(){
		return prefSetting;
	}*/
	public HashMap<String, ArrayList<String>> getWidgetGroup(){
		return widgetGroup;
	}
    public HashMap<String, IWidget> getDynamicWidget(){
    	return dynamicWidgets;
    }
    public ArrayList<String> getTaskOrderList(){
    	return taskOrderList;
    }
	public HashMap<String, TaskElement> getTaskMap() {
		return taskMap;
	}
	public String getStartingFrame() {
		return startingFrame;
	}
	public String getUserHand() {
		return userHand;
	}
	public String getDemonstrationName() {
		return demonstrationName;
	}
	public HashMap<String, CallBackElement> getLocalCallBack() {
		return localCallBackMap;
	}
	public HashMap<String, CallBackElement> getGlobalCallBack() {
		return globalCallBackMap;
	}
	public HashMap<String, ArrayList<TaskElement>> getParameterMap() {
		return frameParameterMap;
	}
	public ArrayList<String> getFrameNameList(){
		return frameNameList;
	}
	protected void makeDesignNameUnique(Design design) {
		design.setName(NamedObjectUtil.makeNameUnique(design.getName(), project.getDesigns()));
	}
	protected void importDesign(TaskParent parent, DesignSelectionState prms, Design newDesign, IUndoableEditSequence editSeq) {
		makeDesignNameUnique(newDesign);
		ProjectCmd.addNewDesign(project, newDesign, prms.getSelectedDesign(), importXMLPresentation, editSeq);
	}
}
