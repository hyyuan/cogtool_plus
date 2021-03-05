
package cogtoolplus_simulator;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.xml.sax.SAXException;

import edu.cmu.cs.hcii.cogtool.CogToolLID;
import edu.cmu.cs.hcii.cogtool.controller.DemoStateManager;
import edu.cmu.cs.hcii.cogtool.controller.ProjectCmd;
import edu.cmu.cs.hcii.cogtool.controller.UndoManagerRecovery;
import edu.cmu.cs.hcii.cogtool.controller.ControllerCP;
import edu.cmu.cs.hcii.cogtool.model.AUndertaking;
import edu.cmu.cs.hcii.cogtool.model.CognitiveModelGenerator;
import edu.cmu.cs.hcii.cogtool.model.Demonstration;
import edu.cmu.cs.hcii.cogtool.model.Design;
import edu.cmu.cs.hcii.cogtool.model.ImportCogToolXML;
import edu.cmu.cs.hcii.cogtool.model.KLMCognitiveGenerator;
import edu.cmu.cs.hcii.cogtool.model.Project;
import edu.cmu.cs.hcii.cogtool.model.Project.ITaskDesign;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;
import edu.cmu.cs.hcii.cogtool.model.TaskParent;
import edu.cmu.cs.hcii.cogtool.ui.DesignSelectionState;
import edu.cmu.cs.hcii.cogtool.ui.ProjectContextSelectionState;
import edu.cmu.cs.hcii.cogtool.ui.ProjectLID;
import edu.cmu.cs.hcii.cogtool.util.AUndoableEdit;
import edu.cmu.cs.hcii.cogtool.util.CompoundUndoableEdit;
import edu.cmu.cs.hcii.cogtool.util.IUndoableEdit;
import edu.cmu.cs.hcii.cogtool.util.IUndoableEditSequence;
import edu.cmu.cs.hcii.cogtool.util.L10N;
import edu.cmu.cs.hcii.cogtool.util.ListenerIdentifierMap;
import edu.cmu.cs.hcii.cogtool.util.NamedObjectUtil;
import edu.cmu.cs.hcii.cogtool.util.RcvrIllegalStateException;
import edu.cmu.cs.hcii.cogtool.util.UndoManager;

public class CogToolXMLImporter {
	protected static CognitiveModelGenerator MODELGEN_ALG = KLMCognitiveGenerator.ONLY;
	protected Project project = new Project();
	protected static final String importXMLPresentation = L10N.get("UNDO.PC.ImportXML", "Import XML");
	//protected Map<Design, Collection<Demonstration>> parsedDesigns = null;
	protected Set<AUndertaking> newUndertakings = null;
	protected File importFile = null;
	//protected ListenerIdentifierMap lIDMap = new ListenerIdentifierMap();
	//protected UndoManager undoMgr = null;
    public static final String SET_ATTRIBUTE =
            L10N.get("UNDO.DC.SetAttribute", "Set Attribute");
    protected static final String DEFAULT_PROJECT_PREFIX =
            L10N.get("PM.NewProjectName", "Untitled Project");
    protected static int nextNewProjectSuffix = 1;
	public CogToolXMLImporter() {			
	}
	
	public Project getModel() {
		return project;
	}
	
	public String getName(){
		String tmp = importFile.getName();
		String name = (String) tmp.subSequence(0, tmp.lastIndexOf("."));
		return name;
	}
	   
	private void settingPref(HashMap<String, Object> prefSetting){
		if(prefSetting.size()!=0){
			Set<String> keys = prefSetting.keySet();
			Iterator<String> itr = keys.iterator();
			while(itr.hasNext()){
				String setting = itr.next();
				switch(setting){
				case "PECK_FITTS_COFF":
					CogToolPrefCP.PECK_FITTS_COEFF.setInt((int) prefSetting.get(setting));
					break;
				case "MIN_FITTS":
					CogToolPrefCP.MIN_FITTS.setInt((int) prefSetting.get(setting));
					break;
				case "RESEARCH":
					CogToolPrefCP.RESEARCH.setBoolean((boolean) prefSetting.get(setting));
					break;
				case "ACTR_ALTERNATIVE_PARAMETERS":
					CogToolPrefCP.ACTR_ALTERNATIVE_PARAMETERS.setBoolean((boolean) prefSetting.get(setting));
					break;
				case "ACTRDAT":
					CogToolPrefCP.ACTR_DAT.setInt((int) prefSetting.get(setting));
					break;
				case "VISUAL_ATTENTION":
					CogToolPrefCP.VISUAL_ATTENTION.setInt((int) prefSetting.get(setting));
					break;
				case "IMPLIED_THINK":
					CogToolPrefCP.GENERATE_THINKS_ON_IMPORT.setBoolean((boolean)prefSetting.get(setting));
				}				
			}			
		}
	}
	public Project loadXML(String fileName, HashMap<String, Object> prefSetting) {
		//ControllerCP.newProjectController();	
		if (prefSetting != null)
			settingPref(prefSetting);		
		//UndoManager undoMgr = UndoManager.getUndoManager(project);
		CompoundUndoableEdit editSeq = new CompoundUndoableEdit(importXMLPresentation, ProjectLID.ImportXML);
		importFile = new File(fileName);
		TaskParent parent = project;
		ImportCogToolXML importer = new ImportCogToolXML();
		try {
			importer.importXML(importFile, parent, MODELGEN_ALG);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}		
		Map<Design, Collection<Demonstration>> parsedDesigns = importer.getDesigns();
		newUndertakings = importer.getNewUndertakings();
		for (AUndertaking u : newUndertakings) {
			parent.addUndertaking(u);
			final AUndertaking uu = u;
			final TaskParent pp = parent;
			editSeq.addEdit(new AUndoableEdit(ProjectLID.ImportXML) {
				@Override
				public void redo() {
					super.redo();
					pp.addUndertaking(uu);
				}

				@Override
				public void undo() {
					super.undo();
					pp.removeUndertaking(uu);
				}
			});
		}
		ProjectContextSelectionState sel = new ProjectContextSelectionState(project);
		Iterator<Map.Entry<Design, Collection<Demonstration>>> designDemos = parsedDesigns.entrySet().iterator();

		while (designDemos.hasNext()) {
			Map.Entry<Design, Collection<Demonstration>> entry = designDemos.next();

			importDesign(parent, sel, 
					entry.getKey(), entry.getValue(), newUndertakings, editSeq);
		}
		editSeq.end();
		//undoMgr.addEdit(editSeq);
		return project;
	}

	protected void makeDesignNameUnique(Design design) {
		design.setName(NamedObjectUtil.makeNameUnique(design.getName(), project.getDesigns()));
	}

	protected void importDesign(TaskParent parent, DesignSelectionState prms, Design newDesign,
			Collection<Demonstration> demonstrations, Set<AUndertaking> newUndertakings,
			IUndoableEditSequence editSeq) {
		makeDesignNameUnique(newDesign);

		ProjectCmd.addNewDesign(project, newDesign, prms.getSelectedDesign(), importXMLPresentation, editSeq);

		// Add taskapplications/tasks as well for demonstrations
		//System.out.println("demonstration steps " + demonstrations.size());
		if ((demonstrations != null) && (demonstrations.size() > 0)) {
			DemoStateManager demoMgr = DemoStateManager.getStateManager(project, newDesign);

			Iterator<Demonstration> demoIt = demonstrations.iterator();

			while (demoIt.hasNext()) {
				Demonstration demo = demoIt.next();
				//System.out.println("demo step is " + demo.getStepCount());
				TaskApplication taskApp = demo.getTaskApplication();
				AUndertaking demoTask = taskApp.getTask();
				//System.out.println("demo task is " + demoTask.getFullName());
				if (demoTask.getTaskGroup() == null && !newUndertakings.contains(demoTask)) {
					// If the taskApp's task is not already part of the
					// project, add it. Regardless, any project root task
					// with the same name should be the same at this point!
					AUndertaking rootTask = parent.getUndertaking(demoTask.getName());
					if (rootTask == null) {
						parent.addUndertaking(demoTask);
						editSeq.addEdit(createNewTaskUndo(parent, Project.AT_END, demoTask, ProjectLID.ImportXML,
								importXMLPresentation));
					} else if (rootTask != demoTask) {
						throw new RcvrIllegalStateException("Unexpected root task difference");
					}
				}
				project.setTaskApplication(taskApp);
				demoMgr.trackEdits(demo);
			}
		}
	}

	protected IUndoableEdit createNewTaskUndo(final TaskParent parent, final int atIndex, final AUndertaking newTask,
			CogToolLID lid, final String presentationName) {
		return new AUndoableEdit(lid) {
			protected Map<ITaskDesign, TaskApplication> associatedTAs = null;
			protected boolean recoverMgrs = false;

			@Override
			public String getPresentationName() {
				return presentationName;
			}

			@Override
			public void redo() {
				super.redo();

				recoverMgrs = false;

				parent.addUndertaking(atIndex, newTask);

				if (associatedTAs != null) {
					project.restoreRemovedTaskApplications(associatedTAs);
				}
			}

			@Override
			public void undo() {
				super.undo();

				recoverMgrs = true;

				associatedTAs = project.taskApplicationsForRemovedTask(newTask);

				parent.removeUndertaking(newTask);
			}

			@Override
			public void die() {
				super.die();

				if (recoverMgrs) {
					recoverManagers(newTask, associatedTAs);
				}
			}
		};
	} // createNewTaskUndo

	protected void recoverManagers(AUndertaking undertaking, Map<ITaskDesign, TaskApplication> associatedTAs) {
		UndoManagerRecovery.recoverScriptManagers(project, associatedTAs, true);
	}
}
