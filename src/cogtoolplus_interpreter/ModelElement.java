package cogtoolplus_interpreter;

public class ModelElement {
	protected String id;
	protected String model_name;
	protected String simulation_name;
	protected Double weight;
	public ModelElement(){
	}
	public ModelElement(ModelElement model) {
		this.setID(model.getID()); 
		this.setWeight(model.getWeight());
	}
	public void setID(String input){
		id = input;
	}
	public void setWeight(Double input){
		weight = input;
	}
	public void setModelName(String input){
		model_name = input;
	}
	public void setSimulationName(String input){
		simulation_name = input;
	}
	public String getID(){
		return id;
	}
	public Double getWeight(){
		return weight;
	}
	public String getModelName(){
		return model_name;
	}
	public String getSimulationName(){
		return simulation_name;
	}
	public boolean isModelEntity(){
		return true;
	}
}
