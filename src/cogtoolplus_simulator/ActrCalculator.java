package cogtoolplus_simulator;

import java.util.List;

import edu.cmu.cs.hcii.cogtool.model.ACTR6PredictionAlgo;
import edu.cmu.cs.hcii.cogtool.model.AUndertaking;
import edu.cmu.cs.hcii.cogtool.model.Design;
import edu.cmu.cs.hcii.cogtool.model.IPredictionAlgo;
import edu.cmu.cs.hcii.cogtool.model.Project;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;

public class ActrCalculator {
	
	//protected ProjectInteraction Project_Interaction;
	/*public actrCalculator(ProjectInteraction _Project_Interaction){
		Project_Interaction = _Project_Interaction;
	}*/
	public ActrCalculator(){}
	protected Design design;// = null;
	protected AUndertaking task;// = null;
	public Project computeACTR(Project prj){
		List<Design> listDesign = prj.getDesigns();       
        List<AUndertaking> tasks = prj.getUndertakings();        
        if (listDesign.size()>0 && tasks.size() > 0) {
			design = listDesign.get(0);
			task = tasks.get(0);			
			//System.out.println(design.getName());
			//System.out.println(task.getName());
			//System.out.println(tasks.size());
			//System.out.println("This trial has " + listDesign.size() + "design of models with " + tasks.size() + " tasks");
			
			//System.out.println(".............");
			//System.out.println(".............");
			//System.out.println(".............");
			//System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
//			System.out.println(".............");
			TaskApplication ta = prj.getTaskApplication(task, design);
			IPredictionAlgo activeAlg = ACTR6PredictionAlgo.ONLY;
			ComputePredictionCP computer = new ComputePredictionCP();
			computer.computeAllPredictions(prj, ta, activeAlg);

			//ta.determineComputeInBackground(prj);
			// Project_Interaction);
		}
        return prj;      
	}
	public Design getDesign(){
		return design;
	}
	public AUndertaking getTask(){
		return task;
	}
}
