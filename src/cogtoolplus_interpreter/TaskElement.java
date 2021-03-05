package cogtoolplus_interpreter;

public class TaskElement {
	public String taskName = null;
	public String groupName = null;	
	public String type;
	public Integer targetIdx;
	public String target = null;
	public Boolean singleWidget = null;
	
	public TaskElement(){}
	public TaskElement(TaskElement task){
		this.setGroupName(task.getGroupName());
		this.setTaskName(task.getTaskName());
		this.setType(task.getType());	
		this.setSingleWidget(task.isSingleWidget());
	}
	public void setSingleWidget(Boolean _singleWidget){
		singleWidget = _singleWidget;
	}
	public boolean isSingleWidget(){
		return singleWidget;
	}
	public void setTaskName(String _taskName){
		taskName = _taskName;
	}
	public void setGroupName(String _groupName){
		groupName = _groupName;
	}
	public void setType(String _type){
		type = _type;
	}
	public void setTargetIdx(Integer _targetIdx){
		targetIdx = _targetIdx;
	}
	public String getTaskName() {
		return taskName;
	}
	public String getGroupName() {
		return groupName;
	}
	public String getType() {
		return type;
	}
	public Integer getTargetIdx(){
		return targetIdx;
	}
	public void setTarget(String _target){
		target = _target;
	}
	public String getTarget(){
		return target;
	}
	public Boolean containsWidgets(){
		if (groupName == null)
			return false;
		else
			return true;
	}
}
