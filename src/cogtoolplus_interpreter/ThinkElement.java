package cogtoolplus_interpreter;

public class ThinkElement extends TaskElement{
	public ThinkElement(){}
	public ThinkElement(TaskElement task){
		super(task);
	}
	private String label = null;
	private Double time = 1.2;// default value
	private String frame = null;
	public void setDuration(Double input){
		time = input;
	}
	public void setLabel(String input){
		label = input;
	}
	public void setFrameName(String input){
		frame = input;
	}
	public Double getDuration(){
		return time;
	}
	public String getLabel(){
		return label;
	}
	public String getFrameName(){
		return frame;
	}
}
