package cogtoolplus_interpreter;

public class Variable {
	private String var_id = null;
	private String var_type = null;
	private Object var_value = null;
	public Variable(){}
	public Variable(Variable var){
		this.var_id = var.getId();
		this.var_type = var.getType();
		this.var_value = var.getValue();
	}
	public Variable(String id, String type, Object value){
		var_id = id;
		var_type = type;
		var_value = value;
	}
	public String getId(){
		return var_id;
	}
	public String getType(){
		return var_type;
	}
	public Object getValue(){
		return var_value;
	}
}
