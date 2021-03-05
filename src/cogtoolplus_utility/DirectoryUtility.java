package cogtoolplus_utility;

import java.io.File;

public class DirectoryUtility {
	public DirectoryUtility(){}
	
	public void createDir(String path){
		File theDir = new File(path);		
		theDir.mkdir();
	}
	
	public boolean isExisted(String path){		
		File theDir = new File(path);	
		return theDir.exists();
	}
}
