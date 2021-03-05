package cogtoolplus;

import cogtoolplus_utility.DirectoryUtility;

public class CogToolPlusFileSystem {
	private String inputPath;
	private String jsPath;
	private String outputPath;
	private String projectPath;
	private String projectName;
	protected DirectoryUtility dir = new DirectoryUtility();
	public CogToolPlusFileSystem(){}
	public void setInputPath(String _inputPath){
		inputPath = _inputPath;
	}
	public void setJavaScriptPath(String _jsPath){
		jsPath = _jsPath;
	}
	public boolean setOutputPath(String _outputPath){
		outputPath = _outputPath;
		if(!dir.isExisted(outputPath)){
			dir.createDir(outputPath);
			return true;
		}else{
			return false;
		}
	}
	public boolean setProjectPath(String _projectName, String _outputPath){		
		projectName = _projectName;
		projectPath = _outputPath + "/" + projectName;
		if(!dir.isExisted(projectPath)){
			dir.createDir(projectPath);
			return true;
		}
		else{
			return false;
		}
	}

	public String getInputPath(){
		return inputPath;
	}
	public String getJavaScriptPath(){
		return jsPath;
	}
	public String getProjectPath(){
		return projectPath;
	}
	public String getProjectName(){
		return projectName;
	}
}
