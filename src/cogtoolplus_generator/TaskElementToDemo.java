package cogtoolplus_generator;

import java.util.ArrayList;
import java.util.HashMap;
import cogtoolplus_interpreter.ButtonPressElement;
import cogtoolplus_interpreter.NoninteractiveElement;
import cogtoolplus_interpreter.ScriptStepPlus;
import cogtoolplus_interpreter.TaskElement;
import cogtoolplus_interpreter.ThinkElement;
import cogtoolplus_interpreter.TouchScreenElement;
import cogtoolplus_interpreter.VisualScanElement;
import edu.cmu.cs.hcii.cogtool.model.Demonstration;
import edu.cmu.cs.hcii.cogtool.model.Frame;
import edu.cmu.cs.hcii.cogtool.model.IWidget;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;

public class TaskElementToDemo {
	protected static final String TASK_VISUAL = "visual_task";
	protected static final String TASK_BUTTONPRESS = "buttonpress_task";
	protected static final String TASK_NONINTER = "noninter_task";
	protected static final String TASK_TOUCH = "touch_task";
	protected static final String TASK_THINK = "think_task";
	protected TaskElement task;
	protected String key;
	protected TaskApplication taskapp;
	protected HashMap<String, ArrayList<String>> list;
	protected HashMap<String, Frame> frameMap;
	protected Demonstration demo;
	protected HashMap<String, ArrayList<Integer>> TaskIOMap;

	public TaskElementToDemo(TaskElement nTask, String nKey, TaskApplication tApp,
			HashMap<String, ArrayList<String>> nList, HashMap<String, Frame> fMap, Demonstration nDemo) {
		// HashMap<String, ArrayList<Integer>> nTaskIOMap) {
		task = nTask;
		key = nKey;
		taskapp = tApp;
		list = nList;
		frameMap = fMap;
		demo = nDemo;
	}

	public void setTaskIOMap(HashMap<String, ArrayList<Integer>> input) {
		TaskIOMap = input;
	}

	public HashMap<String, ArrayList<Integer>> getTaskIOMap() {
		return TaskIOMap;
	}

	public Demonstration getDemonstration() {		
		String[] parts = key.split("#");
		String frame_name = null;
		if (parts.length > 2)
			frame_name = parts[2];
		else
			frame_name = parts[1];
		String task_name = task.getGroupName();
		String tmp = task_name + "#" + frame_name;

		String type = task.getType();
		// list only contain tasks that has a widget group
		if (list.containsKey(tmp)) {
			ArrayList<String> widget_group = list.get(tmp);
			ArrayList<IWidget> widgetList = new ArrayList<IWidget>(widget_group.size());
			for (int m = 0; m < widget_group.size(); m++) {
				//System.out.println("size is" + widget_group.size());
				//System.out.println("index is" + m);
				//System.out.println("frame name is " + frame_name);
				//System.out.println(frameMap.containsKey(frame_name));
				widgetList.add(frameMap.get(frame_name).getWidget(widget_group.get(m)));
			}
			if (type.equalsIgnoreCase(TASK_VISUAL)) {
				VisualScanElement vElement = (VisualScanElement) task;
				ArrayList<Integer> scanPath = vElement.getScanPath();
				for (int idx = 0; idx < scanPath.size(); idx++) {
					demo.appendStep(new ScriptStepPlus("LookAt", widgetList.get(scanPath.get(idx))).getStep());
				}
			} else if (type.equalsIgnoreCase(TASK_NONINTER)) {
				boolean isSingleWidget = task.isSingleWidget();
				if(!isSingleWidget){
					demo.appendStep(new ScriptStepPlus("LookAt", widgetList.get(task.getTargetIdx())).getStep());
				}
				else{
					demo.appendStep(new ScriptStepPlus("LookAt", frameMap.get(frame_name).getWidget(task.getTarget())).getStep());//frameMap.get(frame_name).getWidget();
				}
				
			} else if (type.equalsIgnoreCase(TASK_BUTTONPRESS)) {
				ButtonPressElement bTask = (ButtonPressElement) task;
				demo.appendStep(new ScriptStepPlus("ButtonPress", widgetList.get(bTask.getTargetIdx()),
						bTask.getButtonState(), bTask.getButtonEventType(), bTask.getButtonModifier(),
						bTask.getButtonDelay(), bTask.getButtonDelayLabel()).getStep());
			} else if (type.equalsIgnoreCase(TASK_TOUCH)) {
				TouchScreenElement tTask = (TouchScreenElement) task;
				demo.appendStep(new ScriptStepPlus("Tap", widgetList.get(tTask.getTargetIdx()), tTask.getTouchType(),
						tTask.getTouchDelay(), tTask.getTouchDelayLabel()).getStep());
			} else if (type.equalsIgnoreCase(TASK_THINK)){
				ThinkElement tkTask = (ThinkElement) task;
				demo.appendStep(new ScriptStepPlus("Think", frameMap.get(tkTask.getFrameName()), tkTask.getDuration(),
						tkTask.getLabel()).getStep());
			}
		}
		// for those tasks contains single widget, or no widget
		else {
			if (type.equalsIgnoreCase(TASK_THINK)) {
				ThinkElement tkTask = (ThinkElement) task;
				demo.appendStep(new ScriptStepPlus("Think", frameMap.get(tkTask.getFrameName()), tkTask.getDuration(),
						tkTask.getLabel()).getStep());
			}else if (type.equalsIgnoreCase(TASK_NONINTER)) {
				boolean isSingleWidget = task.isSingleWidget();
				if(isSingleWidget){
					demo.appendStep(new ScriptStepPlus("LookAt", frameMap.get(frame_name).getWidget(task.getTarget())).getStep());
				}				
			}else if (type.equalsIgnoreCase(TASK_BUTTONPRESS)) {
				ButtonPressElement bTask = (ButtonPressElement) task;
				boolean isSingleWidget = bTask.isSingleWidget();
				if(isSingleWidget){
					demo.appendStep(new ScriptStepPlus("ButtonPress", frameMap.get(frame_name).getWidget(task.getTarget()),
						bTask.getButtonState(), bTask.getButtonEventType(), bTask.getButtonModifier(),
						bTask.getButtonDelay(), bTask.getButtonDelayLabel()).getStep());
				}
			}
			else if (type.equalsIgnoreCase(TASK_TOUCH)) {
				TouchScreenElement tTask = (TouchScreenElement) task;
				boolean isSingleWidget = tTask.isSingleWidget();
				if(isSingleWidget){
					demo.appendStep(new ScriptStepPlus("Tap", frameMap.get(frame_name).getWidget(task.getTarget()), tTask.getTouchType(),
						tTask.getTouchDelay(), tTask.getTouchDelayLabel()).getStep());
				}
			}
		}
		return demo;
	}
}
