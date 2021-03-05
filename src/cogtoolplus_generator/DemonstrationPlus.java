package cogtoolplus_generator;

import edu.cmu.cs.hcii.cogtool.model.Demonstration;
import edu.cmu.cs.hcii.cogtool.model.Frame;
import edu.cmu.cs.hcii.cogtool.model.HandLocation;
import edu.cmu.cs.hcii.cogtool.model.Task;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;

public class DemonstrationPlus {
	//protected String taskName = "task";
	protected TaskApplication taskapp;
	protected String handNess = "right";
	protected Task task;	
	protected Frame frame;
	protected Demonstration demo;
	public DemonstrationPlus(TaskApplication task, String hand, Frame fr){
		demo = new Demonstration(task);
		demo.setStartFrame(fr);		
		demo.setMouseHand((hand.equalsIgnoreCase(handNess)) 
				           ? HandLocation.RIGHT_HAND
				           : HandLocation.LEFT_HAND);
	}
	public Demonstration getDemo(){
		return demo;
	}
}
