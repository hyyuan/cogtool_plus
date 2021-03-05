package cogtoolplus_analyser;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import cogtoolplus.CogToolPlusFileSystem;
import cogtoolplus_interpreter.ImportMixedModelXML;
import cogtoolplus_interpreter.LevelElement;
import cogtoolplus_interpreter.ModelElement;
import processing.core.PApplet;
import processing.event.MouseEvent;

public class VisualiserStartPage extends PApplet {
	protected ImportMixedModelXML mixedModel = new ImportMixedModelXML();
	protected ArrayList<String> keySet = new ArrayList<String>(); // the number
																	// of level
																	// in mixed
																	// model
	protected ArrayList<GraphElement> allGraphs;
	protected ArrayList<LevelElement> levelList;
	protected CogToolPlusFileSystem fileSystem;
	protected Integer fps;
	protected float levelWidth = 130;
	protected float levelHeight = 65;
	protected float modelWidth = 120;
	protected float modelHeight = 60;
	protected float resultWidth = 100;
	protected float resultHeight = 50;
	protected float gapWidth = 75;
	protected float gapHeight = 45;

	protected float marginHorizontal = 50;
	protected float marginVertical = 50;
	protected float graphIntervalV = 10;
	protected float graphIntervalH = 25;
	protected float informationBoardWidth = 500;
	protected float informationBoardHeight = 160;
	protected float buttonWidth = 120;
	protected float buttonHeight = 40;

	protected int windowHeight = 800;
	protected int windowWidth = 850;
	protected int currentHeight = 0;
	protected int currentWidth = 0;
	protected int smallText = 10;
	protected int mediumText = 15;
	protected int largeText = 30;
	protected int thinLine = 1;
	protected int line = 2;
	protected int thickLine = 3;
	protected int thickerLine = 4;
	protected int roundCornerDegree = 15;
	protected HashMap<String, ArrayList<LevelElement>> levelMap = new HashMap<String, ArrayList<LevelElement>>();
	protected HashMap<String, ConnectionXY> connectionMapLevel = new HashMap<String, ConnectionXY>();
	protected HashMap<String, ConnectionXY> connectionMapModel = new HashMap<String, ConnectionXY>();
	protected HashMap<String, ConnectionXY> connectionMapLevelGraph = new HashMap<String, ConnectionXY>();
	protected HashMap<String, ConnectionXY> connectionMapModelGraph = new HashMap<String, ConnectionXY>();
	protected HashMap<String, HashMap<String, ArrayList<Double>>> modelStatsMap = new HashMap<String, HashMap<String, ArrayList<Double>>>();
	protected ArrayList<Rectangle> interactiveEntities;// = new ArrayList<Rectangle>();
	protected HashMap<String, HashMap<String, Double>> statsMap;
	protected HashMap<String, ArrayList<String>> interactiveStructure;// = new HashMap<String, ArrayList<String>>();
	protected HashMap<String, Integer> colorMap = new HashMap<String, Integer>();
	protected HashMap<String, Integer> currentColorMap = new HashMap<String, Integer>();
	// https://www.w3schools.com/colors/colors_picker.asp
	protected int transparent = color(0,0,0,1);
	protected int buttonColor = color(0, 51, 102);
	protected int infoMenu = color(204, 230, 255);
	protected int colorMenuBG = color(230, 243, 255);
	protected int blueLevel = color(51, 153, 255);
	protected int blueModel = color(153, 204, 255);
	protected int greenLevel = color(128, 255, 170);
	protected int greenModel = color(204, 255, 221);
	protected int highlightColor = color(255, 255, 102);
	protected int black = color(0, 0, 0);
	protected int white = color(255, 255, 255);
	protected int grey = color(126, 126, 126);
	//protected int transparentWhite = color(255, 255, 255,63);
	protected int transparentWhite = color(255, 255, 255, 175);
	//protected int noColor = color(0,0,0);
	
	protected Boolean staticMenuIsClicked = false;
	float centerX = 0;
	float centerY = 0, offsetX = 0, offsetY = 0;
	double zoom = 1.5;
	float translateX;
	float translateY;
	float scaleFactor;

	public VisualiserStartPage(Integer _fps, ImportMixedModelXML _mixedModel,
			HashMap<String, HashMap<String, Double>> _statsMap, ArrayList<GraphElement> _allGraphs,
			HashMap<String, HashMap<String, ArrayList<Double>>> _modelStatsMap, CogToolPlusFileSystem _fileSystem) {		
		fps = _fps;
		mixedModel = _mixedModel;
		statsMap = _statsMap;
		allGraphs = _allGraphs;
		modelStatsMap = _modelStatsMap;
		fileSystem = _fileSystem;
	}

	public void setup() {
		surface.setLocation(0,0); 
		surface.setTitle("CogTool+ Visualisation"); 
		surface.setResizable(true); 
		frameRate(fps);	
	}

	public void settings() {

		levelList = mixedModel.getLevelModels();
		for (int i=0; i<levelList.size(); i++){
			LevelElement level = levelList.get(i);
			String property = level.getProperty();
			if (!levelMap.containsKey(property)){
				ArrayList<LevelElement> tmpList = new ArrayList<LevelElement>();
				tmpList.add(level);
				levelMap.put(property, tmpList);
			}else{
				ArrayList<LevelElement> tmpList = levelMap.get(property);
				tmpList.add(level);
				levelMap.put(property, tmpList);
			}
		}

		Iterator<String> temp = statsMap.keySet().iterator();
		while (temp.hasNext()) {
			keySet.add(temp.next());
		}
				
		if (surface != null) {
			surface.setResizable(true);
		}
		Set<String> tmp = levelMap.keySet();
		int numberOfLevels = tmp.size();
		//windowWidth = (int) ((marginHorizontal+resultWidth+modelWidth)*2*numberOfLevels + marginHorizontal);			
		int tempWidth = (int) ((marginHorizontal+resultWidth+modelWidth)*2*numberOfLevels + marginHorizontal);	
		windowWidth = tempWidth > windowWidth ? tempWidth : windowWidth;
		size(windowWidth, windowHeight);

		centerX = 0;
		centerY = 0;
		scaleFactor = 1;
		smooth();
	}

	/*
	 * private void calculateWindowHeight() { windowHeight = ((allGraphs.size()
	 * + 1) * graphIntervalV + allGraphs.size() * modelHeight) * keySet.size();
	 * }
	 */

	private void drawMenu() {
		
		int textGapV = 10;
		int textGapH = 10;
		
		fill(infoMenu);
		strokeWeight(thinLine);
		rect(0, 0, windowWidth, (float) (informationBoardHeight+graphIntervalV*1.5));
		
		// Draw information board
		fill(transparentWhite);
		strokeWeight(thinLine);
		rect(graphIntervalH + textGapH, graphIntervalV, informationBoardWidth, informationBoardHeight);
		
		
		stroke(buttonColor);
		strokeWeight(thinLine);
		line(0,(float)(informationBoardHeight+graphIntervalV*1.5), windowWidth, (float)(informationBoardHeight+graphIntervalV*1.5));
		
		float x_start = graphIntervalH + textGapH;
		float y_start = graphIntervalV + textGapV;

		textSize(mediumText);
		fill(black);
		text("-Current scale is: ", x_start, y_start);
		x_start += textWidth("Current scale is: ");
		text(scaleFactor, x_start, y_start);

		x_start = graphIntervalH + textGapV;
		//y_start += textAscent() + textGapH;
		//text("-Click the round rectangle to view simulation results", x_start, y_start);

		//y_start += textAscent() + textGapH;
		//text("-Click 'reset color' to go back to original color scheme", x_start, y_start);
		
		//y_start += textAscent() + textGapH;
		//text("-Click 'reset scale' to go back to original scale", x_start, y_start);
		
		y_start += textAscent() + textGapH;
		text("-Please use mouse wheel to zoom in/out", x_start, y_start);

		y_start += textAscent() + textGapH;
		text("-Please click and drag to move the visulisation graph", x_start, y_start);

		y_start += textAscent() + textGapH;
		text("-All results are saved in CSV format, can be found in 'output dir'", x_start, y_start);

		y_start += textAscent() + textGapH;
		text("-Mixed model and meta models can be found in 'input dir'", x_start, y_start);

		y_start += textAscent() + textGapH;
		text("-Javascript files can be found in 'javascript dir'", x_start, y_start);

		// Draw button to reset color
		float x = graphIntervalH + textGapH + informationBoardWidth + graphIntervalH / 2;
		float y = graphIntervalV;
		fill(buttonColor);
		strokeWeight(thinLine);
		rect(x, y, buttonWidth, buttonHeight, roundCornerDegree);
		interactiveEntities.add(new Rectangle(x, y, buttonWidth, buttonHeight, "resetcolor", false, null, null));
		x_start = x + textGapH;
		y_start = y + textGapV;
		textSize(mediumText);
		fill(white);
		textAlign(LEFT, CENTER);
		text("reset color", x_start, y_start);

		// Draw button to reset the scale
		y += buttonHeight + graphIntervalV;
		fill(buttonColor);
		rect(x, y, buttonWidth, buttonHeight, roundCornerDegree);
		interactiveEntities.add(new Rectangle(x, y, buttonWidth, buttonHeight, "resetscale", false, null, null));
		x_start = x + textGapH;
		y_start = y + textGapV;
		textSize(mediumText);
		fill(white);
		textAlign(LEFT, CENTER);
		text("reset scale", x_start, y_start);

		// Draw button to go to the project directory
		x += buttonWidth + graphIntervalH / 2;
		y = graphIntervalV;

		fill(buttonColor);
		rect(x, y, buttonWidth, buttonHeight, roundCornerDegree);
		interactiveEntities.add(new Rectangle(x, y, buttonWidth, buttonHeight, "outputdir", false, null, null));
		x_start = x + textGapH;
		y_start = y + textGapV;
		textSize(mediumText);
		fill(white);
		textAlign(LEFT, CENTER);
		text("output dir", x_start, y_start);

		// Draw button to go to the input directory
		y += buttonHeight + graphIntervalV;
		fill(buttonColor);
		rect(x, y, buttonWidth, buttonHeight, roundCornerDegree);
		interactiveEntities.add(new Rectangle(x, y, buttonWidth, buttonHeight, "inputdir", false, null, null));
		x_start = x + textGapH;
		y_start = y + textGapV;
		textSize(mediumText);
		fill(white);
		textAlign(LEFT, CENTER);
		text("input dir", x_start, y_start);

		// Draw button to go to the directory storing the javascript files
		y += buttonHeight + graphIntervalV;
		fill(buttonColor);
		rect(x, y, buttonWidth, buttonHeight, roundCornerDegree);
		interactiveEntities.add(new Rectangle(x, y, buttonWidth, buttonHeight, "jsdir", false, null, null));
		x_start = x + textGapH;
		y_start = y + textGapV;
		textSize(mediumText);
		fill(white);
		textAlign(LEFT, CENTER);
		text("javascript dir", x_start, y_start);
	}

	public void draw() {

		if (mousePressed == true && staticMenuIsClicked == false) {
			centerX = mouseX - offsetX;
			centerY = mouseY - offsetY;
		}

		pushMatrix();

		translate(centerX, centerY);
		scale(scaleFactor);

		background(colorMenuBG);

		//
		interactiveEntities = new ArrayList<Rectangle>();
		interactiveStructure = new HashMap<String, ArrayList<String>>();
		
		Iterator<String> temp = levelMap.keySet().iterator();
		while (temp.hasNext()) {
			String property = temp.next();
			if (property.equalsIgnoreCase("1")){
				drawIndividualLevel(levelMap.get(property), marginHorizontal+resultWidth);
			}else if (property.equalsIgnoreCase("2")){
				drawIndividualLevel(levelMap.get(property),(marginHorizontal+resultWidth+modelWidth)*3);
			}else if (property.equalsIgnoreCase("3")){
				drawIndividualLevel(levelMap.get(property),(marginHorizontal+resultWidth+modelWidth)*5);
			}else if (property.equalsIgnoreCase("4")){
				drawIndividualLevel(levelMap.get(property),(marginHorizontal+resultWidth+modelWidth)*7);
			}else if (property.equalsIgnoreCase("5")){
				drawIndividualLevel(levelMap.get(property),(marginHorizontal+resultWidth+modelWidth)*9);
			}						
		}
		
		//drawLevel();
		/*
		 * randomSeed(100); for (int i = 0; i < 100; i++) { float r =
		 * random(100); fill(random(255),random(255),random(255));
		 * ellipse(random(width),random(height),r,r); }
		 */
		popMatrix();
		drawMenu();
	}

	protected void drawIndividualLevel(ArrayList<LevelElement> levelList, float levelItemX) {
		// This is the main menu of the visualisation part
		// The structure of the graphs are based on the mixed model
		//levelList = mixedModel.getLevelModels();
		//float levelItemX = (windowWidth - levelWidth) / 3;
		float levelItemY = marginVertical + informationBoardHeight;
		Coordinates pointLevelResults = new Coordinates(levelItemX - graphIntervalH - resultWidth, graphIntervalV);
		Coordinates pointModel = new Coordinates(levelItemX + levelWidth + graphIntervalH, graphIntervalV);
		for (int i = 0; i < levelList.size(); i++) {
			LevelElement level = levelList.get(i);
			String levelId = level.getID();
			textSize(mediumText);
			if (textWidth(levelId) > levelWidth)
				levelWidth = (int) textWidth(level.getID());

			if (!colorMap.containsKey(levelId)) {
				colorMap.put(levelId, blueLevel);
			}
			if (!currentColorMap.containsKey(levelId)) {
				currentColorMap.put(levelId, blueLevel);
				//System.out.println("level ID is " + levelId);
			}
			if (!interactiveStructure.containsKey(levelId)) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(levelId);
				interactiveStructure.put(levelId, list);
			}
			fill(currentColorMap.get(levelId));
			strokeWeight(thinLine);
			rect((levelItemX), (levelItemY), levelWidth, levelHeight);

			fill(black);
			text(levelId, (levelItemX), (levelItemY + levelHeight / 2));
			textAlign(LEFT, CENTER);
			ArrayList<ModelElement> modelList = level.getModelList();

			float xlt = levelItemX;
			float ylt = levelItemY;
			float xl = xlt;
			float yl = levelItemY + levelHeight/2;
			float xll = xlt;
			float yll = levelItemY + levelHeight;

			float xrt = levelItemX + levelWidth;
			float yrt = levelItemY;
			float xr = xrt;
			float yr = levelItemY + levelHeight/2;
			float xrl = xrt;
			float yrl = levelItemY + levelHeight;
			
			float xtm = levelItemX + levelWidth/2;
			float ytm = ylt;
			float xbm = levelItemX + levelWidth/2;
			float ybm = levelItemY + levelHeight;

			if (!connectionMapLevel.containsKey(level.getID())) {
				connectionMapLevel.put(level.getID(), new ConnectionXY(xlt, ylt, xl, yl, xll, yll,
						xrt, yrt, xr, yr, xrl, yrl,
						xtm, ytm, xbm, ybm));
			}
			pointLevelResults.y = levelItemY;
			pointLevelResults = drawLevelResults(allGraphs, level.getID(), pointLevelResults);

			pointModel.y = levelItemY;
			pointModel = drawModel(modelList, level.getID(), pointModel);
			
		    float tmpY = Math.max(pointModel.y, pointLevelResults.y);
		    levelItemY = tmpY;
		    
			/*if (pointLevelResults.y > pointModel.y - resultHeight)
				levelItemY = pointLevelResults.y;
			else if (pointLevelResults.y <= pointModel.y - resultHeight) {
				pointLevelResults.y = pointModel.y - resultHeight;
				levelItemY = pointModel.y - resultHeight;
			}*/
		}
		if (currentHeight > windowHeight) {
			surface.setSize(windowWidth, currentHeight);
		}
	}
	
	protected void drawLevel() {
		// This is the main menu of the visualisation part
		// The structure of the graphs are based on the mixed model
		levelList = mixedModel.getLevelModels();
		float levelItemX = (windowWidth - levelWidth) / 3;
		float levelItemY = graphIntervalV + informationBoardHeight + graphIntervalV;
		Coordinates pointLevelResults = new Coordinates(levelItemX - graphIntervalH - resultWidth, graphIntervalV);
		Coordinates pointModel = new Coordinates(levelItemX + levelWidth + graphIntervalH, graphIntervalV);
		for (int i = 0; i < levelList.size(); i++) {
			LevelElement level = levelList.get(i);
			String levelId = level.getID();
			textSize(mediumText);
			if (textWidth(levelId) > levelWidth)
				levelWidth = (int) textWidth(level.getID());

			if (!colorMap.containsKey(levelId)) {
				colorMap.put(levelId, blueLevel);
			}
			if (!currentColorMap.containsKey(levelId)) {
				currentColorMap.put(levelId, blueLevel);
				//System.out.println("level ID is " + levelId);
			}
			if (!interactiveStructure.containsKey(levelId)) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(levelId);
				interactiveStructure.put(levelId, list);
			}
			fill(currentColorMap.get(levelId));
			strokeWeight(thinLine);
			rect((levelItemX), (levelItemY), levelWidth, levelHeight);

			fill(black);
			text(levelId, (levelItemX + levelWidth / 4), (levelItemY + levelHeight / 2));
			ArrayList<ModelElement> modelList = level.getModelList();

			float xlt = levelItemX;
			float ylt = levelItemY;
			float xl = xlt;
			float yl = levelItemY + levelHeight/2;
			float xll = xlt;
			float yll = levelItemY + levelHeight;

			float xrt = levelItemX + levelWidth;
			float yrt = levelItemY;
			float xr = xrt;
			float yr = levelItemY + levelHeight/2;
			float xrl = xrt;
			float yrl = levelItemY + levelHeight;
			
			float xtm = levelItemX + levelWidth/2;
			float ytm = ylt;
			float xbm = levelItemX + levelWidth/2;
			float ybm = levelItemY + levelHeight;

			if (!connectionMapLevel.containsKey(level.getID())) {
				connectionMapLevel.put(level.getID(), new ConnectionXY(xlt, ylt, xl, yl, xll, yll,
						xrt, yrt, xr, yr, xrl, yrl,
						xtm, ytm, xbm, ybm));
			}
			pointLevelResults.y = levelItemY;
			pointLevelResults = drawLevelResults(allGraphs, level.getID(), pointLevelResults);

			pointModel.y = levelItemY;
			pointModel = drawModel(modelList, level.getID(), pointModel);
			
		    float tmpY = Math.max(pointModel.y, pointLevelResults.y);
		    levelItemY = tmpY;
		    
			/*if (pointLevelResults.y > pointModel.y - resultHeight)
				levelItemY = pointLevelResults.y;
			else if (pointLevelResults.y <= pointModel.y - resultHeight) {
				pointLevelResults.y = pointModel.y - resultHeight;
				levelItemY = pointModel.y - resultHeight;
			}*/
		}
		if (currentHeight > windowHeight) {
			surface.setSize(windowWidth, currentHeight);
		}
	}

	protected Coordinates drawModelResults(ArrayList<GraphElement> allGraphs, String levelID, String modelID,
			Coordinates point) {
		float x = point.x;
		float y = point.y;
		for (int j = 0; j < allGraphs.size(); j++) {
			GraphElement graph = allGraphs.get(j);
			if (graph.property.equalsIgnoreCase("model")) {
				float xlt, ylt;
				float xl, yl;
				float xll, yll;
				float xrt, yrt;
				float xr, yr;
				float xrl, yrl;
				float xtm, ytm;
				float xbm, ybm;
				textSize(smallText);
				if (textWidth(graph.x_title) > resultWidth) {
					// modelWidth = (int) textWidth(graph.x_title);
					fill(greenModel);
					strokeWeight(thinLine);
					rect(x, (y), (int) textWidth(graph.x_title), resultHeight, roundCornerDegree);
					fill(black);
					text(graph.title, x, (y + modelHeight / 4));
					text(graph.x_title, x, (y + modelHeight / 2));
					textAlign(LEFT, CENTER);
					interactiveEntities.add(
							new Rectangle(x, (y), resultWidth, resultHeight, levelID + modelID, false, modelID, graph));
					/*xl = x;
					yl = y + resultHeight / 2;
					xr = x + textWidth(graph.x_title);
					yr = y + resultHeight / 2;
					*/
					
					xlt = x;
					ylt = y;
					xl = xlt;
					yl = y + resultHeight/2;
					xll = xlt;
					yll = y + resultHeight;

					xrt = x + textWidth(graph.x_title);
					yrt = y;
					xr = xrt;
					yr = y + resultHeight/2;
					xrl = xrt;
					yrl = y + resultHeight;	
					
					xtm = x+textWidth(graph.x_title)/2;
					ytm = y;
					xbm = xtm;
					ybm = y + resultHeight;	
				} else {
					fill(greenModel);
					strokeWeight(thinLine);
					rect(x, (y), resultWidth, resultHeight, roundCornerDegree);
					fill(black);
					text(graph.title, x, (y + resultHeight / 4));
					textAlign(LEFT, CENTER);
					text(graph.x_title, x, (y + resultHeight / 2));
					textAlign(LEFT, CENTER);
					interactiveEntities.add(
							new Rectangle(x, (y), resultWidth, resultHeight, levelID + modelID, false, modelID, graph));
					/*xl = x;
					yl = y + resultHeight / 2;
					xr = x + resultWidth;
					yr = y + resultHeight / 2;
					*/
					xlt = x;
					ylt = y;
					xl = xlt;
					yl = y + resultHeight/2;
					xll = xlt;
					yll = y + resultHeight;

					xrt = x + resultWidth;
					yrt = y;
					xr = xrt;
					yr = y + resultHeight/2;
					xrl = xrt;
					yrl = y + resultHeight;	
					
					xtm = x + resultWidth/2;
					ytm = y;
					xbm = x + resultWidth/2;
					ybm = y + resultHeight;
				}
				// textSize(largeText);
				// text(graph.x_title, x, (y + modelHeight / 2));
				// System.out.println(textWidth(graph.title));
				// add these interactive items for further usage at mousepress
				// event

				if (!connectionMapModelGraph.containsKey(levelID + modelID + graph.title)) {
					connectionMapModelGraph.put(levelID + modelID + graph.title, new ConnectionXY(xlt, ylt, xl, yl, xll, yll,
							xrt, yrt, xr, yr, xrl, yrl,
							xtm, ytm, xbm, ybm));
				}
				strokeWeight(line);
				line(xl, yl, connectionMapModel.get(levelID + modelID).xr,
						connectionMapModel.get(levelID + modelID).yr);
				y += modelHeight + graphIntervalV;
			}
		}
		currentHeight = (int) y;
		return new Coordinates(x, y);
	}

	protected Coordinates drawModel(ArrayList<ModelElement> modelList, String levelID, Coordinates point) {
		float x = point.x;
		float y = point.y;
		Coordinates modelResults = new Coordinates(x + graphIntervalH + modelWidth, y);
		for (int j = 0; j < modelList.size(); j++) {
			ModelElement model = modelList.get(j);

			textSize(mediumText);
			if (textWidth(model.getID()) > modelWidth)
				modelWidth = (int) textWidth(model.getID());

			if (!colorMap.containsKey(levelID + model.getID()))
				colorMap.put(levelID + model.getID(), blueModel);
			if (!currentColorMap.containsKey(levelID + model.getID()))
				currentColorMap.put(levelID + model.getID(), blueModel);

			if (!interactiveStructure.containsKey(levelID)) {
				ArrayList<String> list = new ArrayList<String>();
				list.add(levelID);
				interactiveStructure.put(levelID, list);
			} else {
				ArrayList<String> list = interactiveStructure.get(levelID);
				list.add(levelID + model.getID());
				interactiveStructure.put(levelID, list);
			}

			fill(currentColorMap.get(levelID + model.getID()));
			strokeWeight(thinLine);
			rect(x, y, modelWidth, modelHeight);

			fill(color(black));
			text(model.getID(), x, (y + modelHeight / 4));
			textSize(smallText);
			text("weight: " + model.getWeight(), x, (y + modelHeight * 2 / 3));
			textAlign(LEFT, CENTER);

			/*
			float xl = x;
			float yl = y + modelHeight / 2;
			float xr = x + modelWidth;
			float yr = y + modelHeight / 2;
			*/
			
			float xlt = x;
			float ylt = y;
			float xl = xlt;
			float yl = y + modelHeight/2;
			float xll = xlt;
			float yll = y + modelHeight;
			float xtm = x + modelWidth/2;
			float ytm = y;

			float xrt = x + modelWidth;
			float yrt = y;
			float xr = xrt;
			float yr = y + modelHeight/2;
			float xrl = xrt;
			float yrl = y + modelHeight;
			float xbm = x + modelWidth/2;
			float ybm = y + modelHeight;
			
			if (!connectionMapModel.containsKey(levelID + model.getID())) {
				connectionMapModel.put(levelID + model.getID(), new ConnectionXY(xlt, ylt, xl, yl, xll, yll,
						xrt, yrt, xr, yr, xrl, yrl,
						xtm, ytm, xbm, ybm));
			}

			// If the model is equivalent to a level model, then a line with arrow is
			// drawn to connect it to its level model, the line is invisible unless the user
			// click the model, the line and arrow will be highlighted
			if (connectionMapLevel.containsKey(model.getID())) {
				
				if (!colorMap.containsKey(model.getID() + "arrow"))
					colorMap.put(model.getID() + "arrow", transparent);
				if (!currentColorMap.containsKey(model.getID() + "arrow"))
					currentColorMap.put(model.getID() + "arrow", transparent);
				if (interactiveStructure.containsKey(model.getID())) {

					ArrayList<String> list = interactiveStructure.get(model.getID());
					list.add(model.getID() + "arrow");
					interactiveStructure.put(model.getID(), list);
					
				}
				stroke(grey);
				strokeWeight(line);
				stroke(currentColorMap.get(model.getID() + "arrow"));
				drawArrow(currentColorMap.get(model.getID() + "arrow"), connectionMapLevel.get(model.getID()).xbm,
						connectionMapLevel.get(model.getID()).ybm, xlt, ylt);				 
			}			
			stroke(grey);
			strokeWeight(line);
			line(connectionMapLevel.get(levelID).xr, connectionMapLevel.get(levelID).yr, xl, yl);
			stroke(grey);
			if (model.isModelEntity()) {
				modelResults = drawModelResults(allGraphs, levelID, model.getID(), modelResults);
				interactiveEntities
						.add(new Rectangle(x, (y), modelWidth, modelHeight, levelID, false, model.getID(), null));
			}
			interactiveEntities.add(new Rectangle(x, (y), modelWidth, modelHeight, levelID, true, model.getID(), null));
			y = modelResults.y;
			y += modelHeight + graphIntervalV;
		}
		currentHeight = (int) y;
		return new Coordinates(x, y);
	}

	// This method is from https://gist.github.com/ketakahashi/81b7f22b4ecee1fa5d84393ab670ef99
	private void drawArrow(int color, float x1, float y1, float x2, float y2) {
		  //float a = dist(x1, y1, x2, y2)/50;
		  float a = 8;
		  //System.out.println("arrow size " + a);
		  fill(color);  
		  pushMatrix();
		  translate(x2, y2);
		  rotate(atan2(y2 - y1, x2 - x1));
		  triangle(- a * 2 , - a, 0, 0, - a * 2, a);
		  popMatrix();
		  line(x1, y1, x2, y2);  
		}
	protected Coordinates drawLevelResults(ArrayList<GraphElement> allGraphs, String levelID, Coordinates point) {
		float x = point.x;
		float y = point.y;
		float xlt, ylt = 0;
		float xl, yl = 0;
		float xll, yll = 0;
		float xrt, yrt = 0;
		float xr, yr = 0;
		float xrl, yrl = 0;
		float xtm, ytm, xbm, ybm = 0;
		for (int j = 0; j < allGraphs.size(); j++) {
			GraphElement graph = allGraphs.get(j);
			if (graph.property.equalsIgnoreCase("level")) {
				// System.out.println("before: " + modelWidth);
				textSize(smallText);
				if (textWidth(graph.x_title) > resultWidth) {					
					fill(greenLevel);
					stroke(grey);
					strokeWeight(thinLine);
					rect(x - (int) textWidth(graph.x_title) + resultWidth, (y), (int) textWidth(graph.x_title),
							resultHeight, roundCornerDegree);
					interactiveEntities
							.add(new Rectangle(x, (y), resultWidth, modelHeight, levelID, false, null, graph));
					/*xl = x - textWidth(graph.x_title) + resultWidth;
					yl = y + resultHeight / 2;
					xr = x + resultWidth;
					yr = y + resultHeight / 2;
					*/
					xlt = x - textWidth(graph.x_title) + resultWidth;
					ylt = y;
					xl = xlt;
					yl = y + resultHeight/2;
					xll = xlt;
					yll = y + resultHeight;

					xrt = x + resultWidth;
					yrt = y;
					xr = xrt;
					yr = y + resultHeight/2;
					xrl = xrt;
					yrl = y + resultHeight;	
					
					xtm = x + resultWidth/2;
					ytm = y;
					xbm = x + resultWidth/2;
					ybm = y + resultHeight;
					fill(color(0, 0, 0));
					text(graph.title, x - (int) textWidth(graph.x_title) + resultWidth, (y + resultHeight / 4));
					text(graph.x_title, x - (int) textWidth(graph.x_title) + resultWidth, (y + resultHeight / 2));
					textAlign(LEFT, CENTER);
				} else {
					fill(greenLevel);
					stroke(grey);
					strokeWeight(thinLine);
					rect(x, (y), resultWidth, resultHeight, roundCornerDegree);
					interactiveEntities
							.add(new Rectangle(x, (y), resultWidth, resultHeight, levelID, false, null, graph));
					/*xl = x;
					yl = y + resultHeight / 2;
					xr = x + resultWidth;
					yr = y + resultHeight / 2;
					*/
					xlt = x;
					ylt = y;
					xl = xlt;
					yl = y + resultHeight/2;
					xll = xlt;
					yll = y + resultHeight;

					xrt = x + resultWidth;
					yrt = y;
					xr = xrt;
					yr = y + resultHeight/2;
					xrl = xrt;
					yrl = y + resultHeight;	
					
					xtm = x + resultWidth/2;
					ytm = y;
					xbm = x + resultWidth/2;
					ybm = y + resultHeight;
					
					fill(color(0, 0, 0));
					text(graph.title, x, (y + resultHeight / 4));
					text(graph.x_title, x, (y + resultHeight / 2));
					textAlign(LEFT, CENTER);
				}

				// add these interactive items for further usage at mouse press
				// event
				if (!connectionMapLevelGraph.containsKey(levelID + graph.title)) {
					connectionMapLevelGraph.put(levelID + graph.title, new ConnectionXY(xlt, ylt, xl, yl, xll, yll,
							xrt, yrt, xr, yr, xrl, yrl,
							xtm, ytm, xbm, ybm));
				}
				stroke(grey);
				strokeWeight(line);
				line(xr, yr, connectionMapLevel.get(levelID).xl, connectionMapLevel.get(levelID).yl);
				y += modelHeight + graphIntervalV;
			}
		}
		currentHeight = (int) y;
		return new Coordinates(x, y);
	}

	public void keyPressed() {
		/*
		 * if (key == 'r') { scaleFactor = 1; centerX = 0; centerY = 0;
		 * Set<String> tmp = colorMap.keySet(); Iterator<String> keys =
		 * tmp.iterator(); while (keys.hasNext()){ String key = keys.next(); int
		 * tmpColor = colorMap.get(key); currentColorMap.put(key, tmpColor); } }
		 */
	}

	/*
	 * public void mouseDragged(MouseEvent e) { centerX += mouseX - pmouseX;
	 * centerY += mouseY - pmouseY; }
	 */

	public void mouseWheel(MouseEvent e) {
		centerX -= mouseX;
		centerY -= mouseY;
		float delta = (float) (e.getCount() > 0 ? 1.05 : e.getCount() < 0 ? 1.0 / 1.05 : 1.0);
		scaleFactor *= delta;
		centerX *= delta;
		centerY *= delta;
		centerX += mouseX;
		centerY += mouseY;
	}

	public void mousePressed() {
		if (mouseY < informationBoardHeight) {
			//System.out.println(interactiveEntities.size());
			for (int i = 0; i < interactiveEntities.size(); i++) {
				Rectangle rect = interactiveEntities.get(i);
				String key = rect.key;
				if (mouseX >= rect.x && mouseY >= rect.y && mouseX <= rect.x + rect.w && mouseY <= rect.y + rect.h) {
					if (key.equalsIgnoreCase("resetcolor")) {
						Set<String> tmp = colorMap.keySet();
						Iterator<String> keys = tmp.iterator();
						while (keys.hasNext()) {
							String tmpkey = keys.next();
							int tmpColor = colorMap.get(tmpkey);
							currentColorMap.put(tmpkey, tmpColor);
						}
						staticMenuIsClicked = true;
					} else if (key.equalsIgnoreCase("resetscale")) {
						scaleFactor = 1;
						centerX = 0;
						centerY = 0;
						staticMenuIsClicked = true;
					} else if (key.equalsIgnoreCase("outputdir")) {
						try {
							Desktop.getDesktop().open(new File(fileSystem.getProjectPath()));
							//System.out.println(fileSystem.getProjectPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						staticMenuIsClicked = true;
					} else if (key.equalsIgnoreCase("inputdir")) {
						try {
							Desktop.getDesktop().open(new File(fileSystem.getInputPath()));
							//System.out.println(i + " " + fileSystem.getInputPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						staticMenuIsClicked = true;
					} else if (key.equalsIgnoreCase("jsdir")) {
						System.out.println(fileSystem.getJavaScriptPath());
						try {
							Desktop.getDesktop().open(new File(fileSystem.getJavaScriptPath()));
							//System.out.println(fileSystem.getJavaScriptPath());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						staticMenuIsClicked = true;
					}
				}
			}
		} else if (mouseY > informationBoardHeight) {
			offsetX = mouseX - centerX;
			offsetY = mouseY - centerY;
			staticMenuIsClicked = false;
			for (int i = 0; i < interactiveEntities.size(); i++) {
				Rectangle rect = interactiveEntities.get(i);
				String key = rect.key;
				if (offsetX >= rect.x * scaleFactor && offsetY >= rect.y * scaleFactor
						&& offsetX <= rect.x * scaleFactor + rect.w * scaleFactor
						&& offsetY <= rect.y * scaleFactor + rect.h * scaleFactor) {
					//
					if (rect.graph != null) {
						GraphElement graph = rect.graph;
						// draw graph for level
						if (graph.property.equalsIgnoreCase("level")) {
							HashMap<String, Double> data = statsMap.get(key+"_"+graph.operation);
							String[] args = { "test" + i };
							if (graph.isBarchart) {
								VisualiserFreeChart der = new VisualiserFreeChart("dd");
								der.draw2DBarChartColorV1(graph, data);
							}
						}
						// draw graph for model
						else if (graph.property.equalsIgnoreCase("model")) {
							HashMap<String, ArrayList<Double>> data = modelStatsMap.get(key+"_"+graph.operation);
							String[] args = { "test" + i };
							if (graph.isBarchart) {
								VisualiserFreeChart der = new VisualiserFreeChart("dd");
								der.draw2DBarChartColorV2(graph, data);
							} else if (graph.isHistogram) {
								VisualiserFreeChart ser = new VisualiserFreeChart("test");
								ser.draw2DHistogram(graph, data, 20);
							}
						}
					} else {
						String id = rect.modelId;
						if (interactiveStructure.containsKey(id)) {
							Set<String> tmp = colorMap.keySet();
							Iterator<String> keys = tmp.iterator();
							while (keys.hasNext()) {
								String tmpkey = keys.next();
								int tmpColor = colorMap.get(tmpkey);
								currentColorMap.put(tmpkey, tmpColor);
							}
							ArrayList<String> list = interactiveStructure.get(id);
							for (int m = 0; m < list.size(); m++) {
								//String tmp = list.get(m);
								if (currentColorMap.containsKey(list.get(m))) {
									currentColorMap.put(list.get(m), highlightColor);
								}
							}
							currentColorMap.put(key + id, highlightColor);
						}
					}
					break;
				}
			}
		}
	}
	/*
	 * private void drawHightGraph(String key, int highlightColor){
	 * ArrayList<RectangleText> list = interactiveStructure.get(key); for (int
	 * i=0; i<list.size(); i++){ RectangleText recTxt = list.get(i);
	 * fill(highlightColor); strokeWeight(thinLine); rect(recTxt.x, recTxt.y,
	 * recTxt.w, recTxt.h);
	 * 
	 * textSize(recTxt.txtSize); fill(recTxt.txtColor); text(recTxt.text,
	 * recTxt.tx, recTxt.ty); textAlign(LEFT, CENTER); } }
	 */
}

class RectangleText {
	float x, y, w, h;
	int recColor;
	float tx, ty;
	String text;
	int txtColor;
	int txtSize;

	RectangleText(float _x, float _y, float _w, float _h, int _recColor, float _tx, float _ty, String _text,
			int _txtColor, int _txtSize) {
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		recColor = _recColor;
		tx = _tx;
		ty = _ty;
		text = _text;
		txtColor = _txtColor;
		txtSize = _txtSize;
	}
}

class ConnectionXY {
	float xlt, ylt; // top left x and y
	float xl, yl;   // left x and y (middle)
	float xll, yll; // lower left x and y
	float xrt, yrt; // top right x and y
	float xr, yr;   // right x and y (middle)
	float xrl, yrl; // lower right x and y
	float xtm, ytm; // top middle x and y
	float xbm, ybm; // lower middle x and y
	
	ConnectionXY(float _xlt, float _ylt,			
			float _xl, float _yl, 
			float _xll, float _yll,
			float _xrt, float _yrt,
			float _xr, float _yr,
			float _xrl, float _yrl,
			float _xtm, float _ytm,
			float _xbm, float _ybm) {
		xlt = _xlt;
		ylt = _ylt;
		xl = _xl;
		yl = _yl;
		xll = _xll;
		yll = _yll;
		xrt = _xrt;
		yrt = _yrt;
		xr = _xr;
		yr = _yr;
		xrl = _xrl;
		yrl = _yrl;	
		xtm = _xtm;
		ytm = _ytm;
		xbm = _xbm;
		ybm = _ybm;
	}
}

class ColorSwitcher {
	int colorOriginal;
	int colorHighlight;

	ColorSwitcher(int _color1, int _color2) {
		colorOriginal = _color1;
		colorHighlight = _color2;
	}
}

class Rectangle {
	float x, y, w, h;
	String key;
	Boolean isModel;
	String modelId;
	GraphElement graph;

	Rectangle(float _x, float _y, float _w, float _h, String _key, Boolean _isModel, String _modelId,
			GraphElement _graph) {
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		key = _key;
		isModel = _isModel;
		modelId = _modelId;
		graph = _graph;
	}
}

class Coordinates {
	float x, y;

	Coordinates(float _x, float _y) {
		x = _x;
		y = _y;
	}
}
