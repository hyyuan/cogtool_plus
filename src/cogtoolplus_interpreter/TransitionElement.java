package cogtoolplus_interpreter;

public class TransitionElement {
	public String destinationName;
	public Double durationInSeconds;
	public ActionElement actions = null;
	public TransitionElement(String desName, double duration, ActionElement act) {
		destinationName = desName;
		durationInSeconds = duration;
		actions = act;
	}
	public String getDestination(){
		return destinationName;
	}
	public Double getDuration(){
		return durationInSeconds;
	}
	public ActionElement getAction(){
		return actions;
	}
	public boolean hasAction(){
		if(actions==null){
			return false;
		}else
			return true;
	}
}
