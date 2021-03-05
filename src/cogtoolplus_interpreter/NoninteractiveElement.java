package cogtoolplus_interpreter;
public class NoninteractiveElement extends TaskElement{	
	public CallBackElement cb = null;	
	public NoninteractiveElement(){}	
	public NoninteractiveElement(TaskElement task){
		super(task);
	}
/*	public void setCallBack(CallBackElement input) {
		cb = input;
	}
	public Boolean containsCallback(){
		Boolean flag = null;
		if (cb == null){
			flag = false;
		}
		else if (cb != null){
			flag = true;
		}
		return flag;
	}
	public CallBackElement getCallBack(){
		return cb;
	}*/
}
	
