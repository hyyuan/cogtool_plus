package cogtoolplus_interpreter;

import java.util.ArrayList;

public class VisualScanElement extends TaskElement{
	public VisualScanElement(){}
	public VisualScanElement(TaskElement task){
		super(task);
	}		
	protected Integer target_idx = null;
	protected ArrayList<Integer> scanPath = null;
	
	public void setTargetIdx(Integer _target_idx){
		target_idx = _target_idx;
	}
	public void setScanPath(ArrayList<Integer> _scanPath) {
		scanPath = _scanPath;
	}
	public Integer getTargetIdx(){
		return target_idx;
	}
	public ArrayList<Integer> getScanPath(){
		return scanPath;
	}
}
