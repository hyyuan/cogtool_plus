package cogtoolplus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.xml.sax.SAXException;

import cogtoolplus_analyser.GraphElement;
import cogtoolplus_analyser.ImportVisualiserXML;
import cogtoolplus_analyser.OfflineAnalyser;
import cogtoolplus_analyser.VisualiserStartPage;
import cogtoolplus_generator.DemonstrationPlus;
import cogtoolplus_generator.TaskElementToDemo;
import cogtoolplus_generator.ScriptGenerator;
import cogtoolplus_interpreter.ImportIOXML;
import cogtoolplus_interpreter.ImportMixedModelXML;
import cogtoolplus_interpreter.LevelElement;
import cogtoolplus_interpreter.MetaModelInterpreter;
import cogtoolplus_interpreter.ModelElement;
import cogtoolplus_interpreter.TaskElement;
import cogtoolplus_interpreter.Variable;
import cogtoolplus_simulator.CogToolXMLImporter;
import cogtoolplus_simulator.ImportSimulationXML;
import cogtoolplus_simulator.ActrCalculator;
import cogtoolplus_utility.DirectoryUtility;
import edu.cmu.cs.hcii.cogtool.model.AUndertaking;
import edu.cmu.cs.hcii.cogtool.model.Demonstration;
import edu.cmu.cs.hcii.cogtool.model.Design;
import edu.cmu.cs.hcii.cogtool.model.Frame;
import edu.cmu.cs.hcii.cogtool.model.Project;
import edu.cmu.cs.hcii.cogtool.model.Task;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;
import processing.core.PApplet;
import edu.cmu.cs.hcii.cogtool.model.Project.ITaskDesign;
import edu.cmu.cs.hcii.cogtool.model.Project.TaskDesign;

public class LaunchCogToolPlus {
	protected String configurationFile;
	protected String projectFile;
	protected String visualiseFile;
	protected DirectoryUtility dir = new DirectoryUtility();
	protected HashMap<String, HashMap<String, Double>> levelStatsMap = new HashMap<String, HashMap<String, Double>>();
	protected HashMap<String, HashMap<String, ArrayList<Double>>> modelStatsMap = new HashMap<String, HashMap<String, ArrayList<Double>>>();
	protected ArrayList<GraphElement> allGraphs;
	protected static String FRAME_INFO_OUTPUT = "model_frame_";
	protected static String TRIAL_OUTPUT = "trial_";
	protected static String NAME_OUTPUT = "model_";
	//protected MetaModelInterpreter interpreterTemplate = new MetaModelInterpreter();
	private static String[] segmentList = {"Right Hand", "Left Hand", "Vision - Enc", "Eye Move - Exec", "Eye Move - Prep", "Cognition", "Overall"};
	protected CogToolPlusFileSystem fileSystem = new CogToolPlusFileSystem();
	protected ImportMixedModelXML mixedModel = new ImportMixedModelXML();
	protected ImportSimulationXML simulationXML;// = new ImportSimulationXML(jsPath);
	protected ArrayList<ArrayList<String>> allFrameNameList = new ArrayList<ArrayList<String>>();
	protected ArrayList<HashMap<String, ArrayList<String>>> allWidgetGroupList = new ArrayList<HashMap<String, ArrayList<String>>>();
	protected Boolean traceComputed = false;
	protected Boolean metaModelCreated = false;
	protected ImportIOXML io = new ImportIOXML();
	String inputPath;
	String jsPath;
	String outputPath;
	String projectName;
	public LaunchCogToolPlus(String confiFile, String prjFile, String visFile) throws SecurityException, IOException, SAXException{
		this.configurationFile = confiFile;
		this.projectFile = prjFile;
		this.visualiseFile = visFile;
		initilize();
	}
	private void initilize() throws SecurityException, IOException, SAXException{
		File cFile = new File(configurationFile);
		io.importXML(cFile);
		inputPath = io.getDirPath();
		jsPath = io.getJSPath();
		outputPath = io.getOutputPath();
		File pFile = new File(inputPath + "/" + projectFile);
		mixedModel.importXML(pFile);
		projectName = mixedModel.getProjectName();	
		simulationXML = new ImportSimulationXML(jsPath);
	}	

	public void createCogToolModels() throws SecurityException, IOException, SAXException {	
		if (mixedModel.getErrorMsgWeight()) {
			System.out.println("There is/are errors about the sum of the weight in the mixed model. The sum of weight is either smaller than 1 or larger than 1");
			System.exit(0);
		} else {
			fileSystem.setInputPath(inputPath);
			fileSystem.setJavaScriptPath(jsPath);
			fileSystem.setOutputPath(outputPath);
			fileSystem.setProjectPath(projectName, outputPath);

			ArrayList<LevelElement> levelList = mixedModel.getLevelModels();
			String prjPath = fileSystem.getProjectPath();
			File file = new File(prjPath);
			if (file.list().length == 0) {
				System.out.println("Processing mixed model for " + file);
				// Process models in each level
				for (int idx = 0; idx < levelList.size(); idx++) {
					LevelElement level = levelList.get(idx);
					// String levelId = level.getID();
					ArrayList<ModelElement> modelList = level.getModelList();				
					for (int j = 0; j < modelList.size(); j++) {
						ModelElement model = modelList.get(j);
						
						// Process meta model
						boolean isModel = model.isModelEntity();
						if (isModel) {
							// Get model file and simulation file
							String modelId = model.getID();
							String modelName = model.getModelName();
							String simuName = model.getSimulationName();
							String metaFile = modelName + ".xml";
							String simuFile = simuName + ".xml";

							// Create separate directory for different model
							if (!dir.isExisted(fileSystem.getProjectPath() + "/" + modelId))
								dir.createDir(fileSystem.getProjectPath() + "/" + modelId);

							// Create intermediate directory to store converted CogTool
							// compatible model
							String intermediatePath = fileSystem.getProjectPath() + "/" + modelId + "/inter";
							if (!dir.isExisted(intermediatePath))
								dir.createDir(intermediatePath);

							// Create simulation directory to store simulation results
							String simulationPath = fileSystem.getProjectPath() + "/" + modelId + "/output_perTrial";
							String allTrialPath = fileSystem.getProjectPath() + "/" + modelId + "/output_allTrial";
							if (!dir.isExisted(simulationPath))
								dir.createDir(simulationPath);
							if (!dir.isExisted(allTrialPath))
								dir.createDir(allTrialPath);
							
							// Parse simulation file
							//ImportSimulationXML simulationXML = new ImportSimulationXML(jsPath);
							String fileName = inputPath + "/" + simuFile;
							File importFile = new File(fileName);
							simulationXML.importXML(importFile);
							
							// Interpret meta model and convert to a number of CogTool model based on the number of trial							
							int trial = simulationXML.getTrialNumber();	

							HashMap<String, Variable> variableMap = simulationXML.getVariableMap();												
							System.out.println("Processing meta models for " + metaFile);
							CogToolPlusCSVParser parser = new CogToolPlusCSVParser();
							ScriptGenerator scriptGen = new ScriptGenerator();							
							//ArrayList<String> frameNameList = new ArrayList<String>();
							HashMap<String, Frame> frameMap = new HashMap<String, Frame>();
							ArrayList<String> taskOrderList = new ArrayList<String>();
							HashMap<String, TaskElement> taskMap = new HashMap<String, TaskElement>();
							HashMap<String, ArrayList<TaskElement>> frameParameterMap = new HashMap<String, ArrayList<TaskElement>>();
							HashMap<String, ArrayList<Integer>> TaskIOMap = new HashMap<String, ArrayList<Integer>>();
							TaskApplication taskapp = new TaskApplication();
							Demonstration demo = new Demonstration();
							Map<ITaskDesign, TaskApplication> designTAs = new HashMap<ITaskDesign, TaskApplication>();
							// Interpret meta model for each trial
							
							
							for (int i = 0; i < trial; i++) {
								MetaModelInterpreter interpreter = new MetaModelInterpreter();
								// Start to convert meta model to cogtool model and save it
								interpreter.loadXML(inputPath, jsPath, metaFile, variableMap, i);								
								Design design = interpreter.getDesign();								
								frameMap = interpreter.getFrameMap();
								ArrayList<String> frameNameList = new ArrayList<String>();
								frameNameList = interpreter.getFrameNameList();					
								allFrameNameList.add(frameNameList);								
								HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
								list = interpreter.getWidgetGroup();
								allWidgetGroupList.add(list);
								taskOrderList = interpreter.getTaskOrderList();
								taskMap = interpreter.getTaskMap();
								frameParameterMap = interpreter.getParameterMap();
								String outputName = FRAME_INFO_OUTPUT + String.valueOf(i);
								parser.export2CSVFrameInformation(frameNameList, simulationPath, outputName, frameParameterMap, list);           
								
					            String userHand = interpreter.getUserHand();
								String demonstrationName = interpreter.getDemonstrationName();
								
								taskapp = new TaskApplication(new Task(demonstrationName), design);
								Frame stFrame = frameMap.get(interpreter.getStartingFrame());
																
								demo = new DemonstrationPlus(taskapp, userHand, stFrame).getDemo();
								TaskIOMap = new HashMap<String, ArrayList<Integer>>();
								for (int m = 0; m < taskOrderList.size(); m++) {
									String key = taskOrderList.get(m);
									TaskElement task = taskMap.get(key);
									TaskElementToDemo linker = new TaskElementToDemo(task, key, taskapp, list, frameMap, demo);
									linker.setTaskIOMap(TaskIOMap);
									demo = linker.getDemonstration();
									taskapp.addDemonstration(demo);
									TaskIOMap = linker.getTaskIOMap();
								}
								TaskDesign td = new TaskDesign(taskapp.getTask(), design);
								designTAs.put(td, taskapp);
								String output = intermediatePath + "/" + TRIAL_OUTPUT + String.valueOf(i) + ".xml";
								try {
									scriptGen.setXML("UTF-8", output, design, designTAs);
								} catch (IOException e) {
									e.printStackTrace();
								}																					
								frameMap.clear();
								taskOrderList.clear();
								taskMap.clear();
								frameParameterMap.clear();
								TaskIOMap.clear();		
								designTAs.clear();	
							}
						}
					}
					System.out.println("<><><><><><><><><><><><><><><><><><><><><><><><><><><><><>");
				}
				metaModelCreated = true;
			} else {
				System.out.println("This project has been created, loading results for visualisation...");
			}			
		}		
	}
	
	public void computeMetaModel(Boolean visualOn) throws SecurityException, IOException, SAXException {
		ArrayList<LevelElement> levelList = mixedModel.getLevelModels();
		String prjPath = fileSystem.getProjectPath();
		File file = new File(prjPath);
		traceComputed = false;
		File[] listFiles = file.listFiles();
		int count = 0;
		for (File doc: listFiles){
			if(!doc.isDirectory())
				count++;
		}
		if (count == 1 || count == 0) {
			traceComputed = false;			
		}
		else
			traceComputed = true;
		
		if (!traceComputed) {
			// Process models in each level
			for (int idx = 0; idx < levelList.size(); idx++) {
				LevelElement level = levelList.get(idx);				
				ArrayList<ModelElement> modelList = level.getModelList();
				for (int j = 0; j < modelList.size(); j++) {
					ModelElement model = modelList.get(j);
					// Process meta model
					boolean isModel = model.isModelEntity();
					if (isModel) {
						// Get model file and simulation file
						String modelId = model.getID();
						String modelName = model.getModelName();
						String metaFile = modelName + ".xml";
						String simuName = model.getSimulationName();
						String simuFile = simuName + ".xml";
						String fileName = inputPath + "/" + simuFile;
						File importFile = new File(fileName);
						simulationXML.importXML(importFile);
						// Create intermediate directory to store converted
						// CogTool
						// compatible model
						String intermediatePath = fileSystem.getProjectPath() + "/" + modelId + "/inter";

						// Create simulation directory to store simulation
						// results
						String simulationPath = fileSystem.getProjectPath() + "/" + modelId + "/output_perTrial";
						String allTrialPath = fileSystem.getProjectPath() + "/" + modelId + "/output_allTrial";

						// Interpret meta model and convert to a number of
						// CogTool model based on the number of trial
						int trial = simulationXML.getTrialNumber();
						// Get preferences including Fitt's law parameters...
						String prefType = simulationXML.getPrefType();

						CogToolPlusCSVParser parser = new CogToolPlusCSVParser();
						// Interpret meta model for each trial
						for (int i = 0; i < trial; i++) {
							ArrayList<String> frameNameList =  new ArrayList<String>();
							if (metaModelCreated)
								frameNameList = allFrameNameList.get(i);// interpreter.getFrameNameList();
							else if (!metaModelCreated){
								MetaModelInterpreter interpreter = new MetaModelInterpreter();
								interpreter.loadXML(inputPath, jsPath, metaFile, simulationXML.getVariableMap(), i);
								frameNameList = interpreter.getFrameNameList();	
							}
							String output = intermediatePath + "/" + TRIAL_OUTPUT + String.valueOf(i) + ".xml";
							if (prefType != null) {
								if (prefType.equalsIgnoreCase("static")) {
									HashMap<String, Object> prefSetting = simulationXML.getPreSetting();
									HashMap<String, ArrayList<Double>> resultsList = organiseTraces(
											computeTraces(output, simulationPath, prefSetting));
									String tmpName = NAME_OUTPUT + String.valueOf(i);
									parser.exportArray2CSVDoubleOrdered(frameNameList, simulationPath, tmpName,
											resultsList);
								} else if (prefType.equalsIgnoreCase("dynamic")) {
									ArrayList<HashMap<String, Object>> preferenceList = simulationXML
											.getPreferenceList();
									if (preferenceList != null) {
										HashMap<String, Object> prefSetting = preferenceList.get(i);
										HashMap<String, ArrayList<Double>> resultsList = organiseTraces(
												computeTraces(output, simulationPath, prefSetting));
										String tmpName = NAME_OUTPUT + String.valueOf(i);
										parser.exportArray2CSVDoubleOrdered(frameNameList, simulationPath, tmpName,
												resultsList);
									}
								}
							} else {
								HashMap<String, ArrayList<Double>> resultsList = organiseTraces(
										computeTraces(output, simulationPath, null));
								String tmpName = NAME_OUTPUT + String.valueOf(i);
								parser.exportArray2CSVDoubleOrdered(frameNameList, simulationPath, tmpName,
										resultsList);
							}							
						}

						if (visualOn) {
							// Start analysing meta model
							for (int jd = 0; jd < segmentList.length; jd++) {
								HashMap<String, ArrayList<Double>> output = new HashMap<String, ArrayList<Double>>();
								StringBuilder buffer = new StringBuilder();
								for (int i = 0; i < trial; i++) {
									String file_model = simulationPath + "/" + NAME_OUTPUT + String.valueOf(i) + ".csv";
									String file_frame = simulationPath + "/" + FRAME_INFO_OUTPUT + String.valueOf(i)
											+ ".csv";
									if (metaModelCreated)
										buffer = parser.readTimePerWidget(allWidgetGroupList.get(i), file_model, file_frame,
												jd + 1, buffer);
									else if (!metaModelCreated){
										MetaModelInterpreter interpreter = new MetaModelInterpreter();
										interpreter.loadXML(inputPath, jsPath, metaFile, simulationXML.getVariableMap(), i);
										HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
										list = interpreter.getWidgetGroup();
										interpreter.loadXML(inputPath, jsPath, metaFile, simulationXML.getVariableMap(), i);
										buffer = parser.readTimePerWidget(list, file_model, file_frame,
												jd + 1, buffer);
									}
									HashMap<String, Double> timePerFrame = parser
											.readTimePerFramePerOperation(file_model, jd + 1);
									HashMap<String, ArrayList<String>> tasksPerFrame = parser
											.readCSVStringArray(file_frame);
									HashMap<String, ArrayList<Double>> timePerTask = analyseTimePerTask(timePerFrame,
											tasksPerFrame);
									output = mergeHashmap(output, timePerTask);
								}
								String destFile = allTrialPath + "/" + "widget_" + segmentList[jd] + ".csv";
								parser.write2csv(destFile, buffer);
								parser.exportArray2CSVDouble(fileSystem.getProjectPath(),
										modelId + "_" + segmentList[jd], output);
								traceComputed = true;
							}
						}
					}
				}
			}
		}
		if (traceComputed) {
			if (visualOn) {
				analyseMixedModelPerOperation(fileSystem.getProjectPath(), levelList);
				ImportVisualiserXML analyserXML = new ImportVisualiserXML();
				String fileName = inputPath + "/" + visualiseFile;
				System.out.println(fileName);
				File importFile = new File(fileName);
				analyserXML.importXML(importFile);
				// Get all graph information that needed to be visualised
				allGraphs = analyserXML.getAllGraphs();
				String[] processingArgs1 = { "MySketch" };
				VisualiserStartPage mySketch = new VisualiserStartPage(60, mixedModel, levelStatsMap, allGraphs,
						modelStatsMap, fileSystem);
				PApplet.runSketch(processingArgs1, mySketch);
			}
		}
	}
	private HashMap<String, Double> updateResultsModel(Double weight, HashMap<String, ArrayList<Double>> statsMap) {
		HashMap<String, Double> hashmap = new HashMap<String, Double>();
		Set<String> temp = statsMap.keySet();
		Iterator<String> keySet = temp.iterator();
		while (keySet.hasNext()) {
			String key = keySet.next();
			ArrayList<Double> durList = statsMap.get(key);
			Double durTotal = 0.0;
			Double mean = 0.0;
			if (!durList.isEmpty()) {
				for (Double time : durList) {
					durTotal += time;
				}
				mean = (durTotal / durList.size()) * weight;
				hashmap.put(key, mean);
			}
		}
		return hashmap;
	}
	
	private HashMap<String, Double> addResults(HashMap<String, Double> tmpMap_source,
			HashMap<String, Double> tmpMap_target) {
		Set<String> temp = tmpMap_source.keySet();
		Iterator<String> keySet = temp.iterator();
		while (keySet.hasNext()) {
			String key = keySet.next();
			Double meanTime = 0.0;
			Double source = tmpMap_source.get(key);
			if (tmpMap_target.containsKey(key)) {
				Double target = tmpMap_target.get(key);
				meanTime = source + target;
			} else if (!tmpMap_target.containsKey(key)) {
				meanTime = source;
			}
			tmpMap_target.put(key, meanTime);
		}
		return tmpMap_target;
	}
	
	private HashMap<String, Double> updateLevelResults(ArrayList<HashMap<String, Double>> modelStatsList) {
		HashMap<String, Double> resultsMap = new HashMap<String, Double>();
		for (int i = 0; i < modelStatsList.size(); i++) {
			HashMap<String, Double> tmpMap_source = modelStatsList.get(i);
			resultsMap = addResults(tmpMap_source, resultsMap);
		}
		return resultsMap;
	}

	private HashMap<String, Double> updateSimulatedModelResults(Double weight, HashMap<String, Double> inputMap){
		HashMap<String, Double> hashmap = new HashMap<String, Double>();
		Set<String> temp = inputMap.keySet();
		Iterator<String> keySet = temp.iterator();
		while (keySet.hasNext()) {
			String key = keySet.next();
			Double mean = inputMap.get(key)*weight;
			hashmap.put(key, mean);			
		}
		return hashmap;
	}
	
	private void analyseMixedModelPerOperation(String prjPath, ArrayList<LevelElement> levelModel) {
		CogToolPlusCSVParser parser = new CogToolPlusCSVParser();
		for (int m = 0; m < segmentList.length; m++) {
			String operation = segmentList[m];
			for (int i = 0; i < levelModel.size(); i++) {
				LevelElement level = levelModel.get(i);
				String levelId = level.getID();
				ArrayList<ModelElement> modelList = level.getModelList();
				ArrayList<HashMap<String, Double>> currentModelResults = new ArrayList<HashMap<String, Double>>();
				for (int j = 0; j < modelList.size(); j++) {
					ModelElement model = modelList.get(j);
					boolean isModel = model.isModelEntity();
					String modelId = model.getID();
					Double weight = model.getWeight();
					String fileTimePerTask = prjPath + "/" + modelId+"_"+operation + ".csv";
					if (isModel) {
						HashMap<String, ArrayList<Double>> temp = parser.readCSVDoubleArray(fileTimePerTask);
						modelStatsMap.put(levelId + modelId+"_"+operation, temp);
						HashMap<String, Double> output = updateResultsModel(weight, temp);
						currentModelResults.add(output);
					} else if (!isModel) {
						if (levelStatsMap.containsKey(modelId+"_"+operation)) {
							currentModelResults.add(updateSimulatedModelResults(weight, levelStatsMap.get(modelId+"_"+operation)));
						}
					}
				}
				HashMap<String, Double> levelResults = updateLevelResults(currentModelResults);
				parser.export2CSVDouble(prjPath, levelId+"_"+operation, levelResults);
				levelStatsMap.put(levelId+"_"+operation, levelResults);
			}
		}
	}
	
	private HashMap<String, ArrayList<Double>> organiseTraces(ArrayList<HashMap<String, Double>> list){
		HashMap<String, ArrayList<Double>> output = new HashMap<String, ArrayList<Double>>();
		
		HashMap<String, Double> frameMap = list.get(list.size()-1);
		Set<String> frameSet = frameMap.keySet();
		Iterator<String> itr = frameSet.iterator();
		while(itr.hasNext()){
			String frameName = itr.next();
			for (int i=0;i<list.size(); i++){
				HashMap<String, Double> tempMap = list.get(i);
				if (tempMap.containsKey(frameName)){
					Double data = tempMap.get(frameName);
					if(!output.containsKey(frameName)){
						ArrayList<Double> dlist = new ArrayList<Double>();
						dlist.add(data);
						output.put(frameName, dlist);
					}else{
						ArrayList<Double> dlist = output.get(frameName);
						dlist.add(data);
						output.put(frameName, dlist);
					}
				}else{
					Double data = 0.0;
					if(!output.containsKey(frameName)){
						ArrayList<Double> dlist = new ArrayList<Double>();
						dlist.add(data);
						output.put(frameName, dlist);
					}else{
						ArrayList<Double> dlist = output.get(frameName);
						dlist.add(data);
						output.put(frameName, dlist);
					}
				}
			}
		}
		return output;
	}
	
	private ArrayList<HashMap<String, Double>> computeTraces(String file, String outputPath, HashMap<String, Object> prefSetting) {			
		CogToolXMLImporter importer = new CogToolXMLImporter();
		ArrayList<HashMap<String, Double>> output = new ArrayList<HashMap<String, Double>>();
		Project prj = importer.loadXML(file, prefSetting);		
		ActrCalculator calculator = new ActrCalculator();
		prj = calculator.computeACTR(prj);		
		Design design = calculator.getDesign();
		AUndertaking task = calculator.getTask();
		System.out.println("Processing " + file);
		
		OfflineAnalyser analyser = new OfflineAnalyser();
		for (int i=0; i<segmentList.length-1; i++){
			output.add(analyser.exportSegmentResults(prj, task, design, segmentList[i]));
		}		
		output.add(analyser.exportResultsFrameOnly(prj, task, design));		
		return output;
	}
	

		
	private HashMap<String, ArrayList<Double>> analyseTimePerTask(HashMap<String, Double> timePerFrame,
			HashMap<String, ArrayList<String>> tasksPerFrame) {
		HashMap<String, ArrayList<Double>> output = new HashMap<String, ArrayList<Double>>();
		Set<String> temp = timePerFrame.keySet();
		Iterator<String> keys = temp.iterator();
		while (keys.hasNext()) {
			String frameName = keys.next();
			Double time = timePerFrame.get(frameName);
			ArrayList<String> taskList = tasksPerFrame.get(frameName);
			for (int i = 0; i < taskList.size(); i++) {
				String task = taskList.get(i);
				if (!output.containsKey(task)) {
					ArrayList<Double> timeList = new ArrayList<Double>();
					timeList.add(time);
					output.put(task, timeList);
				} else if (output.containsKey(task)) {
					ArrayList<Double> timeList = output.get(task);
					timeList.add(time);
					output.put(task, timeList);
				}
			}
		}
		return output;
	}

	private HashMap<String, ArrayList<Double>> mergeHashmap(HashMap<String, ArrayList<Double>> output,
			HashMap<String, ArrayList<Double>> input) {
		Set<String> temp = input.keySet();
		Iterator<String> keys = temp.iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			if (!output.containsKey(key)) {
				ArrayList<Double> times = input.get(key);
				output.put(key, times);
			} else if (output.containsKey(key)) {
				ArrayList<Double> times = output.get(key);
				times.addAll(input.get(key));
				output.put(key, times);
			}
		}
		return output;
	}
		
}
