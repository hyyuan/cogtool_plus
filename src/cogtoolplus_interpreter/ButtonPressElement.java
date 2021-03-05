package cogtoolplus_interpreter;

public class ButtonPressElement extends TaskElement {
	public ButtonPressElement(){}
	public ButtonPressElement(TaskElement task){
		super(task);
	}
	private String state = null;
	private String type = null;
	private Integer modifier = 0; //default value
	private Double delay = null;
	private String delayLabel = null;//default value
	public void setButtonState(String input){
		state = input;	
	}
	public void setButtonEventType(String input){
		type = input;
	}
	public void setButtonModifier(Integer input){
		modifier = input;
	}
	public void setButtonDelay(Double input){
		delay = input;
	}
	public void setButtonDelayLabel(String input){
		delayLabel = input;
	}
	public String getButtonState(){
		return state;
	}
	public String getButtonEventType(){
		return type;
	}
	public Integer getButtonModifier(){
		return modifier;
	}
	public Double getButtonDelay(){
		return delay;
	}
	public String getButtonDelayLabel(){
		return delayLabel;
	}
}
