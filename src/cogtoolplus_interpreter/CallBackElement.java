package cogtoolplus_interpreter;

import java.util.List;

public class CallBackElement {
	private String fileName = null;
	private String fileType = null;
	private String functionName = null;
	private List<Object> inputArguments = null;
	private String dataType = null;
	private Object result = null;
	private String id = null;
	private Boolean containsID = false;
	public CallBackElement(){
	}	
	public void setID(String input){
		id = input;
		containsID = true;
	}
	public void setFile(String _fileName){
		fileName = _fileName;
	}
	public void setFunction(String _functionName){
		functionName = _functionName;
	}
	public void setInputArguments(List<Object> _inputArguments){
		inputArguments = _inputArguments;
	}
	public void setDataType(String _dataType){
		dataType = _dataType;
	}
	public void setResult(Object _result){
		result = _result;
	}
	public void setFileType(String _fileType){
		fileType = _fileType;
	}
	public String getFileType(){
		return fileType;
	}
	public String getID(){
		return id;
	}
	public String getFile(){
		return fileName;
	}
	public String getFunction(){
		return functionName;
	}
	public List<Object> getInputArguments(){
		return inputArguments;
	}
	public String getDataType(){
		return dataType;
	}
	public Object getResult(){
		return result;
	}
	public Boolean containsID(){
		return containsID;
	}
}
