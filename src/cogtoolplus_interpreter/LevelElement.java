package cogtoolplus_interpreter;

import java.util.ArrayList;

public class LevelElement {
	protected String property;
	protected String id;
	protected ArrayList<ModelElement> modelList;
	public LevelElement(){		
	}
	public void setProperty(String input){
		property = input;
	}
	public void setID(String input){
		id = input;
	}
	public void setModelList(ArrayList<ModelElement> input){
		modelList = input;
	}
	public String getProperty(){
		return property;
	}
	public String getID(){
		return id;
	}
	public ArrayList<ModelElement> getModelList(){
		return modelList;
	}
	
}
