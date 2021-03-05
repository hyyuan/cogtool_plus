package cogtoolplus_analyser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import cogtoolplus_interpreter.TaskElement;
import edu.cmu.cs.hcii.cogtool.model.APredictionResult;
import edu.cmu.cs.hcii.cogtool.model.AUndertaking;
import edu.cmu.cs.hcii.cogtool.model.CognitiveModelGenerator;
import edu.cmu.cs.hcii.cogtool.model.Design;
import edu.cmu.cs.hcii.cogtool.model.IPredictionAlgo;
import edu.cmu.cs.hcii.cogtool.model.Project;
import edu.cmu.cs.hcii.cogtool.model.ResultStep;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;
import edu.cmu.cs.hcii.cogtool.util.CSVSupport; 
/**
 * This class is for offline analyser, which is used to load and process
 * simulation results
 * 
 * @author Haiyue Yuan
 *
 */
public class OfflineAnalyser {

	// This is used to store general stats of the simulation
	// including 'Frame, Vision, Eye Movement, Hand Movement and Cognition'
	/*
	/* public HashMap<String, Double> overallStatsMap = new HashMap<String, Double>();
	*/
	
	// This is used to store stats for each frame
	//public HashMap<String, Double> frameStatsMap = new HashMap<String, Double>();

	// This is used to store detailed stats of the simulation for each operation
	/*
	 * public HashMap<String, Double> detailedStatsMap = new HashMap<String, Double>();
	 */

	//public HashMap<String, ArrayList<TaskElement>> frameParameterMap;
	//private static String[] segmentList = {"Right Hand", "Left Hand", "Vision - Enc", "Eye Move - Exec", "Eye Move - Prep", "Cognition"};
	
	// Get results from simulation results
	private APredictionResult getResult(Project prj, AUndertaking task, Design design) {
		TaskApplication ta = prj.getTaskApplication(task, design);
		CognitiveModelGenerator gen = ta.getFirstModelGenerator();
		IPredictionAlgo activeAlgo = ta.determineActiveAlgorithm(prj);
		// This APredictionResult contains all computed ACT-R trace information
		APredictionResult result = ta.getResult(gen, activeAlgo);
		return result;
	}

	private Iterator<ResultStep> getResultSteps(APredictionResult result) {
		Iterator<ResultStep> sIt = result.getModelSteps().iterator();
		return sIt;
	}

	
	// Obtain general stats of the simulation
	public HashMap<String, Double> exportResultsFrameOnly(Project prj, AUndertaking task, Design design) {		
				
		Iterator<ResultStep> sIt = getResultSteps(getResult(prj, task, design)) ;		
		HashMap<String, Double> frameStatsMap = new HashMap<String, Double>();
		while (sIt.hasNext()) {
			ResultStep step = sIt.next();
			String res = step.resource;
			Double dur = step.duration;
			if (res.equalsIgnoreCase("Frame")) {
				String opr = step.operation;
				frameStatsMap.put(opr, dur);
			}
		}
		return frameStatsMap;
	}
	
	public HashMap<String, Double> exportSegmentResults(Project prj, AUndertaking task, Design design, String Segment){
		Iterator<ResultStep> sIt = getResultSteps(getResult(prj, task, design)) ;				
		HashMap<String, Double[]> TempMap = new HashMap<String, Double[]>();
		HashMap<String, ArrayList<Double[]>> oprMap = new HashMap<String, ArrayList<Double[]>>();
		HashMap<String, Double[]> FrameMap = new HashMap<String, Double[]>();		
		//TODO: rewrite this part if some operations are repeatedly used such as Tap
		//
		while (sIt.hasNext()) {
			ResultStep step = sIt.next();
			String res = step.resource;
			//String opr = step.operation;
			//Double dur = step.duration;	
			if (res.equalsIgnoreCase(Segment)) {
				String opr = step.operation;
				Double[] data = new Double[3];
				data[0] = step.startTime;
				data[1] = step.duration;
				data[2] = data[0]+data[1];
				TempMap.put(opr, data);
				if (!oprMap.containsKey(opr)){
					ArrayList<Double[]> dlist = new ArrayList<Double[]>();
					dlist.add(data);
					oprMap.put(opr, dlist);
				}else{
					ArrayList<Double[]> dlist = oprMap.get(opr);
					dlist.add(data);
					oprMap.put(opr, dlist);
				}
				
			}
			else if(res.equalsIgnoreCase("Frame")){
				String opr = step.operation;			
				Double[] data = new Double[2];				
				data[0] = step.startTime;
				data[1] = step.duration;
				FrameMap.put(opr, data);	
			}
		}
		Set<String> temp = FrameMap.keySet();
		Iterator<String> keySet = temp.iterator();
		HashMap<String, Double> output = new HashMap<String, Double>();
		while (keySet.hasNext()){	
			String frame = keySet.next();
			Double[] data = FrameMap.get(frame);
			double startTime = data[0];
			double endTime = data[0]+data[1];
			Set<String> tp = TempMap.keySet();
			Iterator<String> kS = tp.iterator();
			while(kS.hasNext()){
				String element = kS.next();
				Double[] d = TempMap.get(element);
				/*if (d[0]<=endTime && d[0]>=startTime){
					if (!output.containsKey(frame)){
						output.put(frame, d[1]);
					}else{
						double duration = output.get(frame);
						output.put(frame, duration+d[1]);
					}
				}
				if (element.equalsIgnoreCase("Tap")){
					System.out.println("--");
					System.out.println(startTime);
					System.out.println(endTime);
					System.out.println(d[0]);
				}*/
				ArrayList<Double[]> dlist = oprMap.get(element);
				for (int i=0; i<dlist.size(); i++){
					Double[] dt = dlist.get(i);
					if (dt[0]<=endTime && dt[0]>=startTime){
						if (!output.containsKey(frame)){
							output.put(frame, dt[1]);
						}else{
							double duration = output.get(frame);
							output.put(frame, duration+dt[1]);
						}
					}
					/*if (element.equalsIgnoreCase("Tap")){
						System.out.println("--");
						System.out.println(startTime);
						System.out.println(endTime);
					}*/
				}
			}
		}
		
		
		
		return output;
	}
	/*
	public HashMap<String, Double> exportDetailedStats(Project prj, AUndertaking task, Design design) {
		Iterator<ResultStep> sIt = getResultSteps(getResult(prj, task, design)) ;		
		HashMap<String, Double[]> HandMap = new HashMap<String, Double[]>();
		HashMap<String, Double[]> FrameMap = new HashMap<String, Double[]>();		
		while (sIt.hasNext()) {
			ResultStep step = sIt.next();
			String res = step.resource;
			//String opr = step.operation;
			//Double dur = step.duration;	
			if (res.equalsIgnoreCase("Right Hand")|| res.equalsIgnoreCase("Left Hand")) {
				String opr = step.operation;			
				Double[] data = new Double[2];
				data[0] = step.startTime;
				data[1] = step.duration;
				HandMap.put(opr, data);									
			}
			else if(res.equalsIgnoreCase("Frame")){
				String opr = step.operation;			
				Double[] data = new Double[2];				
				data[0] = step.startTime;
				data[1] = step.duration;
				FrameMap.put(opr, data);	
			}
		}
		Set<String> temp = FrameMap.keySet();
		Iterator<String> keySet = temp.iterator();
		HashMap<String, Double> output = new HashMap<String, Double>();
		while (keySet.hasNext()){	
			String frame = keySet.next();
			Double[] data = FrameMap.get(frame);
			double startTime = data[0];
			double endTime = data[0]+data[1];
			Set<String> tp = HandMap.keySet();
			Iterator<String> kS = tp.iterator();
			while(kS.hasNext()){
				String motor = kS.next();
				Double[] d = HandMap.get(motor);
				if (d[0]<endTime && d[0]>startTime){
					if (!output.containsKey(frame)){
						output.put(frame, d[1]);
					}else{
						double duration = output.get(frame);
						output.put(frame, duration+d[1]);
					}
				}
			}
		}
		return output;
	}

	public HashMap<String, Double> exportOverallStats(Project prj, AUndertaking task, Design design) {
		Iterator<ResultStep> sIt = getResultSteps(getResult(prj, task, design)) ;	
		// APredictionResult result = getResult(prj, task, design);
		// Iterator<ResultStep> sIt = getResultSteps(result);
		// scan through all results, and sum the duration of the same operation
		// (such as cognition, hand movement etc)
		while (sIt.hasNext()) {
			ResultStep step = sIt.next();
			String res = step.resource;
			Double dur = step.duration;
			if (!overallStatsMap.containsKey(res)) {
				overallStatsMap.put(res, dur);
			} else {
				Double tmp = overallStatsMap.get(res);
				tmp = tmp + dur;
				// System.out.println(res);
				// System.out.println(tmp);
				overallStatsMap.put(res, tmp);
			}
		}
		return overallStatsMap;
	}

	//public HashMap<String, Double> getFrameStats() {
	//	return frameStatsMap;
	//}

	public HashMap<String, Double> getOverallStats() {
		return overallStatsMap;
	}
	*/
	public boolean exportResultsToCSV_MixedModel(String destName, String output, HashMap<String, Double> statsMap) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		//String fileName = prj;
		String exportFile = destName + '/' + output + ".csv";
		//System.out.println(fileName);
		File dest = new File(exportFile);							
		StringBuilder buffer = exportResults_v4(CSV_SEPARATOR, now, statsMap);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(dest);
			writer = new BufferedWriter(fw);
			writer.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	public boolean exportResultsToCSV(String destName, String output, HashMap<String, ArrayList<Double>> statsMap) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		//String fileName = prj;
		String exportFile = destName + '/' + output + ".csv";
		//System.out.println(fileName);
		File dest = new File(exportFile);							
		StringBuilder buffer = exportResults_v3(CSV_SEPARATOR, now, statsMap);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(dest);
			writer = new BufferedWriter(fw);
			writer.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	/*
	public boolean exportResultsToCSV(String prj, String destName, HashMap<String, Double> statsMap, HashMap<String, ArrayList<TaskElement>> parMap) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		String fileName = prj;
		String exportFile = destName + '/' + fileName + ".csv";
		File dest = new File(exportFile);							
		StringBuilder buffer = exportResults(CSV_SEPARATOR, now, statsMap, parMap);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(dest);
			writer = new BufferedWriter(fw);
			writer.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	// Output results in CSV format
	public boolean exportResultsToCSV(String prj, String destName, HashMap<String, Double> map) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		String fileName = prj;
		String exportFile = destName + '/' + fileName + ".csv";
		File dest = new File(exportFile);							
		StringBuilder buffer = exportResults(CSV_SEPARATOR, now, map);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(dest);
			writer = new BufferedWriter(fw);
			writer.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {

				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	*/
	
	protected static final String CLIPBOARD_SEPARATOR = "\t";
	protected static final String CSV_SEPARATOR = Character.toString(CSVSupport.CELL_SEPARATOR);

	protected static final String FORMAT_VERSION = "1.0";

	protected StringBuilder exportResults_v3(String separator, Date now, HashMap<String, ArrayList<Double>> hashmap) {
		StringBuilder buffer = new StringBuilder();
		    Set<String> temp = hashmap.keySet();
			Iterator<String> keySet = temp.iterator();
			while (keySet.hasNext()) {				
				String key = keySet.next();
				//System.out.println(key);
				ArrayList<Double> durList = hashmap.get(key);
				Double durTotal = 0.0;
				for (int i=0; i<durList.size(); i++)
				{
					durTotal += durList.get(i);		
				}			
				Double mean = durTotal/durList.size();
				Double sum_difference = 0.0;
				for (int i=0; i<durList.size(); i++)
				{
					sum_difference += Math.pow((durList.get(i)-mean),2);
					//durTotal += durList.get(i);		
				}
				Double std = Math.sqrt(sum_difference/durList.size());
				//Double dur = hashmap.get(key);
				buffer.append(key);
				buffer.append(separator);
				buffer.append(mean);
				buffer.append(separator);
				buffer.append(std);
				buffer.append(separator);
				buffer.append(durList.size());
				buffer.append(separator);
				CSVSupport.addLineEnding(buffer);
			}
			CSVSupport.addLineEnding(buffer);
		//}
		return buffer;
	}
	
	protected StringBuilder exportResults_v4(String separator, Date now, HashMap<String, Double> hashmap) {
		StringBuilder buffer = new StringBuilder();
		    Set<String> temp = hashmap.keySet();
			Iterator<String> keySet = temp.iterator();
			while (keySet.hasNext()) {				
				String key = keySet.next();
				//System.out.println(key);
				Double mean = hashmap.get(key);				
				//Double std = Math.sqrt(sum_difference/durList.size());
				//Double dur = hashmap.get(key);
				buffer.append(key);
				buffer.append(separator);
				buffer.append(mean);
				buffer.append(separator);
				//buffer.append(std);
				//buffer.append(separator);
				//buffer.append(durList.size());
				//buffer.append(separator);
				CSVSupport.addLineEnding(buffer);
			}
			CSVSupport.addLineEnding(buffer);
		//}
		return buffer;
	}
	
	protected StringBuilder exportResults(String separator, Date now, HashMap<String, Double> hashmap) {
		StringBuilder buffer = new StringBuilder();
		//for (int i = 0; i < list.size(); i++) {
			//HashMap<String, Double> hashmap = list.get(i);
			
		    Set<String> temp = hashmap.keySet();
			Iterator<String> keySet = temp.iterator();
			while (keySet.hasNext()) {
				String key = keySet.next();
				Double dur = hashmap.get(key);
				buffer.append(key);
				buffer.append(separator);
				buffer.append(dur);
				buffer.append(separator);
				CSVSupport.addLineEnding(buffer);
			}
			CSVSupport.addLineEnding(buffer);
		//}
		return buffer;
	}

	protected StringBuilder exportResults(String separator, Date now, HashMap<String, Double> statsMap, HashMap<String, ArrayList<TaskElement>> parMap) {
		StringBuilder buffer = new StringBuilder();
		//for (int i = 0; i < list.size(); i++) {
			//HashMap<String, Double> hashmap = list.get(i);
			
		    Set<String> temp = statsMap.keySet();
			Iterator<String> keySet = temp.iterator();
			while (keySet.hasNext()) {
				String key = keySet.next();
				ArrayList<TaskElement> tasks = parMap.get(key);
				for (int i=0;i<tasks.size();i++){
					buffer.append(tasks.get(i).getGroupName());
					buffer.append(separator);
					//System.out.println(tasks.get(i).getGroupName());
				}
				Double dur = statsMap.get(key);
				buffer.append(key);
				buffer.append(separator);
				buffer.append(dur);
				buffer.append(separator);
				CSVSupport.addLineEnding(buffer);
			}
			CSVSupport.addLineEnding(buffer);
		//}
		return buffer;
	}
	
	protected StringBuilder exportResults_v2(String separator, Date now, HashMap<String, List<Double>> hashmap_results,
			double[] listOfFrameDuration, double[] listOfEyemovementDistance) {
		StringBuilder buffer = new StringBuilder();
		// System.out.print(hashmap_results.size() + "\n");
		// for(int i=0;i<hashmap_results.size();i++){
		Set<String> keySet = hashmap_results.keySet();// toArray();
		Iterator<String> keyStrings = keySet.iterator();
		while (keyStrings.hasNext()) {
			String opr = keyStrings.next();
			// System.out.print("~~" + opr + "~~" + "\n");
			List<Double> dur = hashmap_results.get(opr);
			buffer.append(opr);
			CSVSupport.addLineEnding(buffer);
			for (int j = 0; j < dur.size(); j++) {
				// CSVSupport.addLineEnding(buffer);
				if (j != 0 && j % 5 == 0) {
					CSVSupport.addLineEnding(buffer);
				}
				// System.out.print(dur[j] + "\n");
				buffer.append(dur.get(j));
				buffer.append(separator);
			}
			CSVSupport.addLineEnding(buffer);
			CSVSupport.addLineEnding(buffer);
		}

		CSVSupport.addLineEnding(buffer);
		buffer.append("Duration");
		CSVSupport.addLineEnding(buffer);
		for (int j = 0; j < listOfFrameDuration.length; j++) {
			if (j != 0 && j % 5 == 0) {
				CSVSupport.addLineEnding(buffer);
			}
			// System.out.print(dur[j] + "\n");
			buffer.append(listOfFrameDuration[j]);
			buffer.append(separator);
		}
		CSVSupport.addLineEnding(buffer);

		CSVSupport.addLineEnding(buffer);
		buffer.append("Distance_Eye");
		CSVSupport.addLineEnding(buffer);
		for (int j = 0; j < listOfEyemovementDistance.length; j++) {
			if (j != 0 && j % 5 == 0) {
				CSVSupport.addLineEnding(buffer);
			}
			// System.out.print(dur[j] + "\n");
			buffer.append(listOfEyemovementDistance[j]);
			buffer.append(separator);
		}
		CSVSupport.addLineEnding(buffer);
		// }
		/*
		 * for (int i = 0; i < list.length; i++) { buffer.append("Frame " + (i +
		 * 1)); buffer.append(separator); buffer.append(list[i]);
		 * CSVSupport.addLineEnding(buffer); String fra = "Frame" + i;
		 * HashMap<String, Double> hmap = hashmap.get(fra); Set<String> keySet =
		 * hmap.keySet();// toArray(); Iterator<String> keyStrings =
		 * keySet.iterator(); for (int j = 0; j < hmap.size(); j++) { String opr
		 * = keyStrings.next(); Double dur = hmap.get(opr); buffer.append(opr);
		 * buffer.append(separator); buffer.append(dur);
		 * buffer.append(separator); CSVSupport.addLineEnding(buffer); } Point[]
		 * disList = hashmap_distance.get(fra); double distance =
		 * calculateDistance(disList); buffer.append("Eyemovement Distance");
		 * buffer.append(separator); buffer.append(distance);
		 * CSVSupport.addLineEnding(buffer); }
		 */
		return buffer;
	}
}
