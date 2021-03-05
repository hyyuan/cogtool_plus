package cogtoolplus_simulator;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import edu.cmu.cs.hcii.cogtool.model.APredictionResult;
import edu.cmu.cs.hcii.cogtool.model.AUndertaking;
import edu.cmu.cs.hcii.cogtool.model.CognitiveModelGenerator;
import edu.cmu.cs.hcii.cogtool.model.Design;
import edu.cmu.cs.hcii.cogtool.model.IPredictionAlgo;
import edu.cmu.cs.hcii.cogtool.model.Project;
import edu.cmu.cs.hcii.cogtool.model.ResultStep;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;
import edu.cmu.cs.hcii.cogtool.util.CSVSupport;

public class TraceExporter {
	public HashMap<String, HashMap<String, Double>> hashmap;// = new
															// HashMap<String,
															// HashMap<String,
															// Double>>();
	public HashMap<String, Point[]> hashmap_distance;
	public double[] list;
    public Point[] disList;// = new Point[10];
    
    public APredictionResult getResult(Project prj, AUndertaking task, Design design) {
		TaskApplication ta = prj.getTaskApplication(task, design);
		CognitiveModelGenerator gen = ta.getFirstModelGenerator();
		IPredictionAlgo activeAlgo = ta.determineActiveAlgorithm(prj);
		APredictionResult result = ta.getResult(gen, activeAlgo);
		return result;
	}

	public Iterator<ResultStep> getResultSteps(APredictionResult result) {
		Iterator<ResultStep> sIt = result.getModelSteps().iterator();	
		Iterator<String> traceIt = result.getTraceLines().iterator();
        //int lineCount = 0;
        /*while ((traceIt.hasNext()) && (lineCount < 10000)) {
            lineCount++;
            System.out.println(traceIt.next());
            //tt.append(traceIt.next() + "\n");
        }*/
		/*
		while (sIt.hasNext()) {
            ResultStep step = sIt.next();
            System.out.println(step.toString());
		}
		*/
		return sIt;
	}
/*
	public void exportResults_v2(Project prj, AUndertaking task, Design design){
		APredictionResult result = getResult(prj, task, design);
		Iterator<ResultStep> sIt = getResultSteps(result);
		hashmap = new HashMap<String, HashMap<String, Double>>();
		
	}
*/
	public void exportResults(Iterator<ResultStep> sIt, int size, int opsize) {
		hashmap_distance = new HashMap<String, Point[]>();
		hashmap = new HashMap<String, HashMap<String, Double>>();
		list = new double[size];
		disList = new Point[opsize];
		int dis_count = 0;
		int count = 0;		
		while (sIt.hasNext()) {
			ResultStep step = sIt.next();
			if (step.resource == "Frame") {
				hashmap_distance.put("Frame" + count, disList);
				list[count] = step.duration;
				count += 1;
				disList = new Point[opsize];
				dis_count = 0;
			}
			if (step.resource != "Frame") {
				String opString = step.operation;
				Point point = extractPoint(opString);
				if (point.x!=0 && point.y!=0){
					//System.out.print(point.x + " " +point.y + " ");
					disList[dis_count] = point;
					dis_count +=1;
				}
				//System.out.print(step.operation + "\n");
				String fra = "Frame" + count;
				String opr = step.resource;
				Double dur = step.duration;
				if (!hashmap.containsKey(fra)) {
					HashMap<String, Double> hmap = new HashMap<String, Double>();
					hmap.put(opr, dur);
					hashmap.put(fra, hmap);
				} else {
					HashMap<String, Double> hmaptmp = hashmap.get(fra);
					if (!hmaptmp.containsKey(opr)) {
						hmaptmp.put(opr, dur);
					} else {
						Double tmp = hmaptmp.get(opr);
						tmp = tmp + dur;
						hmaptmp.put(opr, tmp);
					}
					hashmap.put(fra, hmaptmp);
				}
			}
		}
	}

	protected Point extractPoint(String operation){
		Point point = new Point();// = null;
		if (operation.contains("Eye Movement to")) {
			Pattern p = Pattern.compile("-?\\d+");
			java.util.regex.Matcher m = p.matcher(operation);
			int[] tmp = new int[2];
			int ct = 0;
			while (m.find()) {
				tmp[ct] = Integer.parseInt(m.group());
				ct += 1;
			}
			point.x = tmp[0];
			point.y = tmp[1];
		}
		return point;
	}
	
	public double calculateDistance(Point[] disList){
		double distance = 0;
		for(int i=0;i<disList.length-1;i++){
			Point point_o = disList[i];
			Point point_n = disList[i+1];
			if (point_o!=null && point_n!=null){
				double tmpDistance = point_o.distance(point_n);
				distance += tmpDistance;
			}
		}
		return distance;
	}
	public double[] getFrameDuration() {
		return list;
	}

	public HashMap<String, HashMap<String, Double>> getOperationDuration() {
		return hashmap;
	}

	public HashMap<String, Point[]> getEyemovementDistance(){
		return hashmap_distance;
	}
	public boolean exportResultsToCSV_v2(String prj, String destName, HashMap<String, List<Double>> hashmap_results, double[] listOfFrameDuration, double[] listOfEyemovementDistance){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		String fileName = prj;
		//String fileName = prj + '_' + fmt.format(now);
		String exportFile = destName + '/' + fileName + ".csv";
		File dest = new File(exportFile);
		StringBuilder buffer = exportResults_v2(CSV_SEPARATOR, now, hashmap_results, listOfFrameDuration, listOfEyemovementDistance);

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
	public boolean exportResultsToCSV(String prj, String destName, HashMap<String, HashMap<String, Double>> hashmap,
			double[] list, HashMap<String, Point[]> hashmap_distance) {
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		String fileName = prj;
		//String fileName = prj + '_' + fmt.format(now);
		String exportFile = destName + '/' + fileName + ".csv";
		File dest = new File(exportFile);
		System.out.println("i am here...");
		StringBuilder buffer = exportResults(CSV_SEPARATOR, now, hashmap, list, hashmap_distance);

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

	protected static final String CLIPBOARD_SEPARATOR = "\t";
	protected static final String CSV_SEPARATOR = Character.toString(CSVSupport.CELL_SEPARATOR);

	protected static final String FORMAT_VERSION = "1.0";

	protected StringBuilder exportResults(String separator, Date now, HashMap<String, HashMap<String, Double>> hashmap,
			double[] list, HashMap<String, Point[]> hashmap_distance) {
		StringBuilder buffer = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			buffer.append("Frame " + (i + 1));
			buffer.append(separator);
			buffer.append(list[i]);
			CSVSupport.addLineEnding(buffer);
			String fra = "Frame" + i;
			HashMap<String, Double> hmap = hashmap.get(fra);
			Set<String> keySet = hmap.keySet();// toArray();
			Iterator<String> keyStrings = keySet.iterator();
			for (int j = 0; j < hmap.size(); j++) {
				String opr = keyStrings.next();
				Double dur = hmap.get(opr);
				buffer.append(opr);
				buffer.append(separator);
				buffer.append(dur);
				buffer.append(separator);
				CSVSupport.addLineEnding(buffer);
			}
			Point[] disList = hashmap_distance.get(fra);
			double distance = calculateDistance(disList);
			buffer.append("Eyemovement Distance");
			buffer.append(separator);
			buffer.append(distance);
			CSVSupport.addLineEnding(buffer);
		}
		return buffer;
	}
	
	protected StringBuilder exportResults_v2(String separator, Date now, HashMap<String, List<Double>> hashmap_results, double[] listOfFrameDuration, double[] listOfEyemovementDistance) {
		StringBuilder buffer = new StringBuilder();
		// System.out.print(hashmap_results.size() + "\n");
		// for(int i=0;i<hashmap_results.size();i++){
		Set<String> keySet = hashmap_results.keySet();// toArray();
		Iterator<String> keyStrings = keySet.iterator();
		while (keyStrings.hasNext()) {
			String opr = keyStrings.next();
			//System.out.print("~~" + opr + "~~" + "\n");
			List<Double> dur = hashmap_results.get(opr);
			buffer.append(opr);
			CSVSupport.addLineEnding(buffer);
			for (int j = 0; j < dur.size(); j++) {
				//CSVSupport.addLineEnding(buffer);
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
