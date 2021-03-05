package cogtoolplus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
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
import edu.cmu.cs.hcii.cogtool.util.CSVSupport;

/**
 * Read and parse CSV files
 * @author Haiyue Yuan
 *
 */
public class CogToolPlusCSVParser {

	public CogToolPlusCSVParser() {
	}

	public void readTasksPerFrame(String csvFile) {
	}

	/**
	 * Read a CSV file, and parse all data to ArrayList &ltDouble[]&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public ArrayList<Double[]> DoubleArrayReadCSV(String csvFile) {
		final String DELIMITER = ",";
		BufferedReader fileReader = null;
		ArrayList<Double[]> data = new ArrayList<Double[]>();
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				Double[] tmp = new Double[tokens.length];
				for (int i = 0; i < tokens.length; i++) {
					tmp[i] = Double.parseDouble(tokens[i]);
				}
				data.add(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	/**
	 * Read a CSV file, and parse all data to ArrayList&ltString[]&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public ArrayList<String[]> StringArrayReadCSV(String csvFile) {
		final String DELIMITER = ",";
		BufferedReader fileReader = null;
		ArrayList<String[]> data = new ArrayList<String[]>();
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				String[] tmp = new String[tokens.length];
				for (int i = 0; i < tokens.length; i++) {
					tmp[i] = tokens[i];
				}
				data.add(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	/**
	 * Read a CSV file, and parse all data to ArrayList&ltString&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public ArrayList<String> StringListReadCSV(String csvFile) {
		final String DELIMITER = ",";
		BufferedReader fileReader = null;
		ArrayList<String> data = new ArrayList<String>();
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				//String[] tmp = new String[tokens.length];
				for (int i = 0; i < tokens.length; i++) {
					data.add(tokens[i]);
				}
				//data.add(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	/**
	 * Read a CSV file, and parse all data to ArrayList&ltInteger[]&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public ArrayList<Integer[]> IntegerArrayReadCSV(String csvFile) {
		final String DELIMITER = ",";
		BufferedReader fileReader = null;
		ArrayList<Integer[]> data = new ArrayList<Integer[]>();
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				Integer[] tmp = new Integer[tokens.length];
				for (int i = 0; i < tokens.length; i++) {
					tmp[i] = Integer.parseInt(tokens[i]);
				}
				data.add(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return data;
	}
	/**
	 * Read a CSV file, and parse all data to HashMap&ltString, ArrayList&ltString&gt&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public HashMap<String, ArrayList<String>> readAllTaskPerFrame(String csvFile) {
		HashMap<String, ArrayList<String>> output = new HashMap<String, ArrayList<String>>();
		BufferedReader fileReader = null;
		final String DELIMITER = ",";
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				String value = tokens[0];

				for (int i = 1; i < tokens.length; i++) {
					String tmpKey = tokens[i];
					// System.out.println("task key is " + tmpKey);
					// System.out.println("task value is " + value);
					// data[i-1] = tokens[i];
					if (!output.containsKey(tmpKey)) {
						ArrayList<String> list = new ArrayList<String>();
						list.add(value);
						output.put(tmpKey, list);
					} else if (output.containsKey(tmpKey)) {
						ArrayList<String> list = output.get(tmpKey);
						list.add(value);
						output.put(tmpKey, list);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	public HashMap<String, ArrayList<Double[]>> readAllTaskPerFramev1(String csvFile,
			HashMap<String, ArrayList<Double[]>> time, HashMap<String, ArrayList<Double[]>> output) {
		// HashMap<String, Double[]> output = new HashMap<String, Double[]>();
		BufferedReader fileReader = null;
		// Delimiter used in CSV file
		final String DELIMITER = ",";
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				String key = tokens[0];
				// String[] data = new String[tokens.length-1];
				ArrayList<Double[]> timeList = time.get(key);
				for (int i = 1; i < tokens.length; i++) {
					String tmpKey = tokens[i];
					// data[i-1] = tokens[i];
					if (!output.containsKey(tmpKey)) {
						// ArrayList<Double[]> list = new ArrayList<String[]>();
						// list.add(timeList);
						output.put(tmpKey, timeList);
					} else {
						ArrayList<Double[]> list = output.get(tmpKey);
						list.addAll(timeList);
						output.put(tmpKey, list);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	/**
	 * Read a CSV file, and parse all data to HashMap&ltString, ArrayList&ltDouble[]&gt&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public HashMap<String, ArrayList<Double[]>> readAllTimePerFrame(String csvFile) {
		HashMap<String, ArrayList<Double[]>> output = new HashMap<String, ArrayList<Double[]>>();
		BufferedReader fileReader = null;
		// Delimiter used in CSV file
		final String DELIMITER = ",";
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				String key = tokens[0];
				Double[] data = new Double[tokens.length - 1];
				for (int i = 1; i < tokens.length; i++) {
					data[i - 1] = Double.parseDouble(tokens[i]);
				}
				if (!output.containsKey(key)) {
					ArrayList<Double[]> list = new ArrayList<Double[]>();
					list.add(data);
					output.put(key, list);
				} else if (output.containsKey(key)) {
					ArrayList<Double[]> list = output.get(key);
					list.add(data);
					output.put(key, list);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// output = transformData(output);
		return output;
	}

	/**
	 * Convert data type from HashMap&ltString, ArrayList&ltDouble[]&gt&gt to HashMap&ltString, ArrayList&ltDouble[]&gt&gt
	 * @param input
	 * @return
	 */
	public HashMap<String, ArrayList<Double[]>> transformData(HashMap<String, ArrayList<Double[]>> input) {
		HashMap<String, ArrayList<Double[]>> output = new HashMap<String, ArrayList<Double[]>>();
		Set<String> keySet = input.keySet();
		Iterator<String> itr = keySet.iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			ArrayList<Double[]> timelist = input.get(key);
			Double[] rightHand = new Double[timelist.size()];
			Double[] leftHand = new Double[timelist.size()];
			Double[] vision_enc = new Double[timelist.size()];
			Double[] eye_exec = new Double[timelist.size()];
			Double[] eye_prep = new Double[timelist.size()];
			Double[] cognition = new Double[timelist.size()];
			Double[] overall = new Double[timelist.size()];
			for (int i = 0; i < timelist.size(); i++) {
				Double[] data = timelist.get(i);
				rightHand[i] = data[0];
				leftHand[i] = data[1];
				vision_enc[i] = data[2];
				eye_exec[i] = data[3];
				eye_prep[i] = data[4];
				cognition[i] = data[5];
				overall[i] = data[6];
			}
			ArrayList<Double[]> newList = new ArrayList<Double[]>();
			newList.add(rightHand);
			newList.add(leftHand);
			newList.add(vision_enc);
			newList.add(eye_exec);
			newList.add(eye_prep);
			newList.add(cognition);
			newList.add(overall);
			output.put(key, newList);
		}
		return output;
	}

	/**
	 * Read a CSV file, and parse all data to HashMap&ltString, Double&gt 
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public HashMap<String, Double> readTimePerFrame(String csvFile) {
		HashMap<String, Double> output = new HashMap<String, Double>();
		BufferedReader fileReader = null;
		// Delimiter used in CSV file
		final String DELIMITER = ",";
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				String key = tokens[0];
				// System.out.println(key);
				Double time = Double.parseDouble(tokens[7]);
				// System.out.println(time);
				output.put(key, time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	/**
	 * Read a CSV file given a position, and parse all data to HashMap&ltString, Double&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @param position
	 * @return
	 */
	public HashMap<String, Double> readTimePerFramePerOperation(String csvFile, int position) {
		HashMap<String, Double> output = new HashMap<String, Double>();
		BufferedReader fileReader = null;
		// Delimiter used in CSV file
		final String DELIMITER = ",";
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				String key = tokens[0];
				// System.out.println(key);
				Double time = Double.parseDouble(tokens[position]);
				// System.out.println(time);
				output.put(key, time);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	/**
	 * Read a CSV file, and parse all data to HashMap&ltString, ArrayList&ltString&gt&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public HashMap<String, ArrayList<String>> readCSVStringArray(String csvFile) {
		HashMap<String, ArrayList<String>> output = new HashMap<String, ArrayList<String>>();
		BufferedReader fileReader = null;
		// Delimiter used in CSV file
		final String DELIMITER = ",";
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				int size = tokens.length;
				String key = tokens[0];
				ArrayList<String> list = new ArrayList<String>(size - 1);
				for (int i = 1; i < size; i++) {
					list.add(tokens[i]);
				}
				output.put(key, list);
				list = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	/**
	 * Read a CSV file, and parse all data to HashMap&ltString, ArrayList&ltDouble&gt&gt
	 * @param csvFile: the absolute path of the CSV file
	 * @return
	 */
	public HashMap<String, ArrayList<Double>> readCSVDoubleArray(String csvFile) {
		HashMap<String, ArrayList<Double>> output = new HashMap<String, ArrayList<Double>>();
		BufferedReader fileReader = null;
		// Delimiter used in CSV file
		final String DELIMITER = ",";
		try {
			String line = "";
			// Create the file reader
			fileReader = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line
			while ((line = fileReader.readLine()) != null) {
				// Get all tokens available in line
				String[] tokens = line.split(DELIMITER);
				int size = tokens.length;
				String key = tokens[0];
				ArrayList<Double> listOfTime = new ArrayList<Double>(size - 1);
				for (int i = 1; i < size; i++) {
					listOfTime.add(Double.parseDouble(tokens[i]));
				}
				output.put(key, listOfTime);
				listOfTime = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return output;
	}

	/**
	 * Export simulation results by frame
	 * @param frameNameList
	 * @param destName
	 * @param output
	 * @param input
	 * @param widgetList
	 * @return
	 */
	public boolean export2CSVFrameInformation(ArrayList<String> frameNameList, String destName, String output,
			HashMap<String, ArrayList<TaskElement>> input, HashMap<String, ArrayList<String>> widgetList) {
		
		Date now = new Date();
		String exportFile = destName + '/' + output + ".csv";
		//File dest = new File(exportFile);
		StringBuilder buffer = write2CSVFrameInformation(frameNameList, CSV_SEPARATOR, now, input, widgetList);
		write2csv(exportFile, buffer);
		
		/*FileWriter fw = null;
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
		}*/
		return true;
	}

	private StringBuilder write2CSVFrameInformation(ArrayList<String> frameNameList, String csvSeparator, Date now,
			HashMap<String, ArrayList<TaskElement>> input, HashMap<String, ArrayList<String>> widgetList) {
		StringBuilder buffer = new StringBuilder();
		// Set<String> temp = input.keySet();
		// Iterator<String> keySet = temp.iterator();
		for (int i = 0; i < frameNameList.size(); i++) {
			String frame = frameNameList.get(i);
			// String frame = keySet.next();
			buffer.append(frame);
			buffer.append(csvSeparator);
			ArrayList<TaskElement> tasks = input.get(frame);
			for (int j = 0; j < tasks.size(); j++) {
				String groupName = tasks.get(j).getGroupName();
				//System.out.println("here group name " + groupName + " " + frame);
				//System.out.println("index " + j);
				String widgetName = "";
				if (tasks.get(j).getTargetIdx()!=null){
					widgetName = widgetList.get(groupName + "#" + frame).get(tasks.get(j).getTargetIdx());
				}
				//System.out.println("here widget name " + widgetName);
				buffer.append(groupName);
				buffer.append(csvSeparator);
				buffer.append(widgetName);
				buffer.append(csvSeparator);
			}
			CSVSupport.addLineEnding(buffer);
		}
		return buffer;
	}

	public boolean export2CSVDouble(String destName, String output, HashMap<String, Double> input) {
		new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		String exportFile = destName + '/' + output + ".csv";
		//File dest = new File(exportFile);
		StringBuilder buffer = write2CSVDouble(CSV_SEPARATOR, now, input);
		
		write2csv(exportFile, buffer);
		/*
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
		}*/
		return true;
	}

	public boolean exportArray2CSVDoubleOrdered(ArrayList<String> frameNameList, String destName, String output,
			HashMap<String, ArrayList<Double>> resultsList) {
		new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		String exportFile = destName + '/' + output + ".csv";
		//File dest = new File(exportFile);
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            // ignore
        }
		StringBuilder buffer = writeArray2CSVDoubleOrdered(frameNameList, CSV_SEPARATOR, now, resultsList);		
		write2csv(exportFile, buffer);
		/*
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
		}*/
		return true;
	}

	public boolean exportArray2CSVDouble(String destName, String output, HashMap<String, ArrayList<Double>> input) {
		new SimpleDateFormat("yyyyMMdd_HHmmss");
		Date now = new Date();
		String exportFile = destName + '/' + output + ".csv";
		//File dest = new File(exportFile);
		StringBuilder buffer = writeArray2CSVDouble(CSV_SEPARATOR, now, input);
		
		write2csv(exportFile, buffer);
		
		/*FileWriter fw = null;
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
		}*/
		return true;
	}

	private StringBuilder write2CSVDouble(String separator, Date now, HashMap<String, Double> input) {
		StringBuilder buffer = new StringBuilder();
		Set<String> temp = input.keySet();
		Iterator<String> keySet = temp.iterator();
		while (keySet.hasNext()) {
			String key = keySet.next();
			Double time = input.get(key);
			buffer.append(key);
			buffer.append(separator);
			buffer.append(time);
			buffer.append(separator);
			CSVSupport.addLineEnding(buffer);
		}
		return buffer;
	}

	private StringBuilder writeArray2CSVDouble(String separator, Date now, HashMap<String, ArrayList<Double>> input) {
		StringBuilder buffer = new StringBuilder();
		Set<String> temp = input.keySet();
		Iterator<String> keySet = temp.iterator();
		while (keySet.hasNext()) {
			String key = keySet.next();
			List<Double> listOfTime = input.get(key);
			buffer.append(key);
			buffer.append(separator);
			for (int j = 0; j < listOfTime.size(); j++) {
				buffer.append(listOfTime.get(j));
				buffer.append(separator);
			}
			CSVSupport.addLineEnding(buffer);
		}
		return buffer;
	}

	private StringBuilder writeArray2CSVDoubleOrdered(ArrayList<String> orderList, String separator, Date now,
			HashMap<String, ArrayList<Double>> resultsList) {
		StringBuilder buffer = new StringBuilder();
		// Set<String> temp = input.keySet();
		// Iterator<String> keySet = temp.iterator();
		for (int i = 0; i < orderList.size(); i++) {
			String key = orderList.get(i);
			//System.out.println("frame is " + key);
			
			// while (keySet.hasNext()) {
			// String key = keySet.next();
			ArrayList<Double> listOfTime = new ArrayList<Double>(); 
			listOfTime = resultsList.get(key);
			//System.out.println("result is " + listOfTime);
			buffer.append(key);
			buffer.append(separator);
			
			for (int j = 0; j < listOfTime.size(); j++) {
				buffer.append(listOfTime.get(j));
				buffer.append(separator);
			}
			CSVSupport.addLineEnding(buffer);
		}
		return buffer;
	}

	// This is for the inter-stroke timing attack
	public StringBuilder readTimePerWidget(HashMap<String, ArrayList<String>> widgetGroupList, String csvTime, String csvWidget, int position, StringBuilder buffer) {
		//HashMap<String, Double> output = new HashMap<String, Double>();
		BufferedReader fileReaderTime = null;
		BufferedReader fileReaderWidget = null;
		// Delimiter used in CSV file
		final String DELIMITER = ",";
		try {
			String lineTime = "";
			String lineWidget = "";
			// Create the file reader
			fileReaderTime = new BufferedReader(new FileReader(csvTime));
			fileReaderWidget = new BufferedReader(new FileReader(csvWidget));
			ArrayList<ArrayList<String>> listWidget = new ArrayList<ArrayList<String>>();
			ArrayList<Double> timeList = new ArrayList<Double>();
			// Read the file line by line
			while ((lineTime = fileReaderTime.readLine()) != null
					&& (lineWidget = fileReaderWidget.readLine()) != null) {
				// Get all tokens available in line
				String[] tokensTime = lineTime.split(DELIMITER);
				String frameName = tokensTime[0];

				String[] tokensWidget = lineWidget.split(DELIMITER);

				Double time = Double.parseDouble(tokensTime[position]);
				timeList.add(time);
				ArrayList<String> list = new ArrayList<String>();
				//String[] list = new String[tokensWidget.length];
				for (int i = 0; i < tokensWidget.length; i++) {
					if (!widgetGroupList.containsKey(tokensWidget[i]+ "#" +frameName)){
						//System.out.println(tokensWidget[i]);
						list.add(tokensWidget[i]);
					}
				}
				listWidget.add(list);
			}			
			ArrayList<String> tmp1 = listWidget.get(0);//String[] tmp1 = listWidget.get(0);
			for (int l = 1; l < tmp1.size(); l++) {
				for (int m = 0; m < listWidget.size(); m++) {
					ArrayList<String> tmp = listWidget.get(m);
					buffer.append(tmp.get(l));
					buffer.append(",");
				}
				CSVSupport.addLineEnding(buffer);
			}
			for (int i = 0; i < timeList.size(); i++) {
				Double time = timeList.get(i);
				buffer.append(time);
				buffer.append(",");
			}
			CSVSupport.addLineEnding(buffer);		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileReaderTime.close();
				fileReaderWidget.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return buffer;
	}

	public void write2csv(String destFile, StringBuilder buffer){
		//String exportFile = "C:/Users/hy0006/Project/COMMANDO-HUMANS/software/CogTool+/inter-keystroke study/test.csv";
		File dest = new File(destFile);
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(dest);
			writer = new BufferedWriter(fw);
			writer.write(buffer.toString());
		} catch (IOException e) {
			e.printStackTrace();

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
			}
		}
	}
	protected static final String CLIPBOARD_SEPARATOR = "\t";
	protected static final String CSV_SEPARATOR = Character.toString(CSVSupport.CELL_SEPARATOR);

	protected static final String FORMAT_VERSION = "1.0";
}
