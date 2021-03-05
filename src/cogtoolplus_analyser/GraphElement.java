package cogtoolplus_analyser;

import java.util.ArrayList;

public class GraphElement {
	public String type = null;
	public String title = null;
	public String x_title = null;
	public String y_title = null;
	public String z_title = null;
	public String operation = null;

	public ArrayList<String> dataSet = null;
	public ArrayList<ArrayList<String>> dataSetList = null;
	
	public boolean isBarchart = false;
	public boolean isHistogram = false;
	public boolean isSeparateGraph = false;
	public String property = null;
	
	public GraphElement(){}
	public void setType(String input){
		type = input;
		if (type.equalsIgnoreCase("barchart")){
			isBarchart = true;
		}
		else if (type.equalsIgnoreCase("histogram")){
			isHistogram = true;
		}
	}
	public void setOperation(String input){
		operation = input;
	}
	public void setTitle(String input){
		title = input;
	}
	public void setXTitle(String input){
		x_title = input;
	}
	public void setYTitle(String input){
		y_title = input;
	}
	public void setZTitle(String input){
		z_title = input;
	}
	public void setDataSetList(ArrayList<ArrayList<String>> input){
		dataSetList = input;
	}
	public void setDataSet( ArrayList<String> input){
		dataSet = input;
	}
	public void setMultipleGraphs(String input){
		if (input.equalsIgnoreCase("true"))
			isSeparateGraph = true;
		else if(input.equalsIgnoreCase("false"))
			isSeparateGraph = false;
	}
	public void setProperty(String input){
		property = input;
	}
	public Boolean isThreeDGraph(){
		if (x_title != null && y_title != null && z_title != null)
			return true;
		else
			return false;
	}
}
