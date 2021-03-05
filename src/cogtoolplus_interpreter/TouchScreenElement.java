package cogtoolplus_interpreter;

public class TouchScreenElement extends TaskElement {
	public TouchScreenElement(){}
	public TouchScreenElement(TaskElement task){
		super(task);
	}
	private String type = null;
	private Double delay = null;
	private String delayLabel = null; // default value

	public void setTouchType(String input){
		type = input;		
	}
	public void setTouchDelay(Double input){
		delay = input;
	}
	public void setDelayLabel(String input){
		delayLabel = input;
	}
	public String getTouchType(){
		return type;
	}
	public Double getTouchDelay(){
		return delay;
	}
	public String getTouchDelayLabel(){
		return delayLabel;
	}
}
