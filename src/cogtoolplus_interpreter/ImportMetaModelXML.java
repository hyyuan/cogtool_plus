/*******************************************************************************
 * CogTool Copyright Notice and Distribution Terms
 * CogTool 1.3, Copyright (c) 2005-2013 Carnegie Mellon University
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License (see LGPL.txt). 
 * 
 * CogTool is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * CogTool is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CogTool; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * CogTool makes use of several third-party components, with the 
 * following notices:
 * 
 * Eclipse SWT version 3.448
 * Eclipse GEF Draw2D version 3.2.1
 * 
 * Unless otherwise indicated, all Content made available by the Eclipse 
 * Foundation is provided to you under the terms and conditions of the Eclipse 
 * Public License Version 1.0 ("EPL"). A copy of the EPL is provided with this 
 * Content and is also available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * CLISP version 2.38
 * 
 * Copyright (c) Sam Steingold, Bruno Haible 2001-2006
 * This software is distributed under the terms of the FSF Gnu Public License.
 * See COPYRIGHT file in clisp installation folder for more information.
 * 
 * ACT-R 6.0
 * 
 * Copyright (c) 1998-2007 Dan Bothell, Mike Byrne, Christian Lebiere & 
 *                         John R Anderson. 
 * This software is distributed under the terms of the FSF Lesser
 * Gnu Public License (see LGPL.txt).
 * 
 * Apache Jakarta Commons-Lang 2.1
 * 
 * This product contains software developed by the Apache Software Foundation
 * (http://www.apache.org/)
 * 
 * jopt-simple version 1.0
 * 
 * Copyright (c) 2004-2013 Paul R. Holser, Jr.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 * Mozilla XULRunner 1.9.0.5
 * 
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/.
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 * 
 * The J2SE(TM) Java Runtime Environment version 5.0
 * 
 * Copyright 2009 Sun Microsystems, Inc., 4150
 * Network Circle, Santa Clara, California 95054, U.S.A.  All
 * rights reserved. U.S.  
 * See the LICENSE file in the jre folder for more information.
 ******************************************************************************/

package cogtoolplus_interpreter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xerces.parsers.DOMParser;
import org.eclipse.ecf.core.util.Base64;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import cogtoolplus.CogToolPlusCSVParser;
import edu.cmu.cs.hcii.cogtool.model.AAction;
import edu.cmu.cs.hcii.cogtool.model.AParentWidget;
import edu.cmu.cs.hcii.cogtool.model.AShape;
import edu.cmu.cs.hcii.cogtool.model.AUndertaking;
import edu.cmu.cs.hcii.cogtool.model.ButtonAction;
import edu.cmu.cs.hcii.cogtool.model.CheckBox;
import edu.cmu.cs.hcii.cogtool.model.ChildWidget;
import edu.cmu.cs.hcii.cogtool.model.CognitiveModelGenerator;
import edu.cmu.cs.hcii.cogtool.model.ContextMenu;
import edu.cmu.cs.hcii.cogtool.model.DefaultModelGeneratorState;
import edu.cmu.cs.hcii.cogtool.model.Demonstration;
import edu.cmu.cs.hcii.cogtool.model.Design;
import edu.cmu.cs.hcii.cogtool.model.DeviceType;
import edu.cmu.cs.hcii.cogtool.model.DoublePoint;
import edu.cmu.cs.hcii.cogtool.model.DoubleRectangle;
import edu.cmu.cs.hcii.cogtool.model.Frame;
import edu.cmu.cs.hcii.cogtool.model.FrameElement;
import edu.cmu.cs.hcii.cogtool.model.FrameElementGroup;
import edu.cmu.cs.hcii.cogtool.model.GraffitiAction;
import edu.cmu.cs.hcii.cogtool.model.GridButton;
import edu.cmu.cs.hcii.cogtool.model.GridButtonGroup;
import edu.cmu.cs.hcii.cogtool.model.GroupNature;
import edu.cmu.cs.hcii.cogtool.model.IWidget;
import edu.cmu.cs.hcii.cogtool.model.KeyAction;
import edu.cmu.cs.hcii.cogtool.model.KeyPressType;
import edu.cmu.cs.hcii.cogtool.model.ListItem;
import edu.cmu.cs.hcii.cogtool.model.MenuHeader;
import edu.cmu.cs.hcii.cogtool.model.MenuItem;
import edu.cmu.cs.hcii.cogtool.model.MouseButtonState;
import edu.cmu.cs.hcii.cogtool.model.MousePressType;
import edu.cmu.cs.hcii.cogtool.model.PullDownHeader;
import edu.cmu.cs.hcii.cogtool.model.PullDownItem;
import edu.cmu.cs.hcii.cogtool.model.RadioButton;
import edu.cmu.cs.hcii.cogtool.model.RadioButtonGroup;
import edu.cmu.cs.hcii.cogtool.model.ShapeOval;
import edu.cmu.cs.hcii.cogtool.model.ShapeRectangle;
import edu.cmu.cs.hcii.cogtool.model.ShapeRoundedRectangle;
import edu.cmu.cs.hcii.cogtool.model.SimpleWidgetGroup;
import edu.cmu.cs.hcii.cogtool.model.TapAction;
import edu.cmu.cs.hcii.cogtool.model.TapPressType;
import edu.cmu.cs.hcii.cogtool.model.Task;
import edu.cmu.cs.hcii.cogtool.model.TaskGroup;
import edu.cmu.cs.hcii.cogtool.model.TaskParent;
import edu.cmu.cs.hcii.cogtool.model.Transition;
import edu.cmu.cs.hcii.cogtool.model.TransitionSource;
import edu.cmu.cs.hcii.cogtool.model.VoiceAction;
import edu.cmu.cs.hcii.cogtool.model.Widget;
import edu.cmu.cs.hcii.cogtool.model.WidgetAttributes;
import edu.cmu.cs.hcii.cogtool.model.WidgetType;
import edu.cmu.cs.hcii.cogtool.util.GraphicsUtil;
import edu.cmu.cs.hcii.cogtool.util.IAttributed;
import edu.cmu.cs.hcii.cogtool.util.L10N;
import edu.cmu.cs.hcii.cogtool.util.NamedObjectUtil;
import edu.cmu.cs.hcii.cogtool.util.NullSafe;
import edu.cmu.cs.hcii.cogtool.util.ObjectLoader;

/**
 * Imports meta model from an XML File, based on work of cogtool from @author
 * AlexF Edited by Brett Harris with Importing from an BMML File The added new
 * features are from @author Haiyue Yuan
 */
public class ImportMetaModelXML {

	//
	//protected ScriptEngineManager manager = new ScriptEngineManager();
	//protected ScriptEngine engine = manager.getEngineByName("JavaScript");
	// ---------The below global variables are defined for cogtool+-----------//
	protected static final String META_MODEL_IMPORT_ELT = "cogtoolplus_metamodel";
	protected static final String VARIABLE = "var";
	// preference setting
	protected static final String RESEARCH_ELT = "research";
	protected static final String PREF_ELT = "pref-setting";
	protected static final String FITTS_A_ELT = "fitts_cof";
	protected static final String FITTS_B_ELT = "fitts_min";
	protected static final String CUSTOMISE_VALUE = "customise";
	protected static final String DEFAULT_VALUE = "default";
	// protected static final String TRUE_VALUE = "true";
	// protected static final String FALSE_VALUE = "false";

	// common elements and attributes of cogtool+ meta model
	protected static final String FRAME_SETTING_ELT = "frame_setting";
	protected static final String FRAME_LIST_ELT = "frame_list";
	protected static final String FRAME_REF_ATTR = "frame_ref";
	protected static final String TARGET_INDEX_ELT = "targetIndex";
	protected static final String TARGET_ELT = "target";
	protected static final String TYPE_ELT = "type";
	protected static final String STYLE_ATTR = "style";
	protected static final String HAND_ELT = "hand";
	protected static final String VALUE_ELT = "value";
	protected static final String ID_ELT = "id";
	protected static final String PAR_ATTR = "par";
	// gloabl variable
	protected static final String GLOBAL_VAR_ELT = "global_variable";
	protected static final String VAR_ELT = "variable";
	protected static final String GLOBAL_VAR_LINK_ELT = "global_var_link";
	protected static final String REF_ATTR = "ref";
	// callback
	protected static final String CALLBACK_ELT = "callback";
	protected static final String CB_FILE_ELT = "file";
	protected static final String FUNCTION_ELT = "function";
	protected static final String CB_ARG_LIST_ELT = "argument_list";
	protected static final String CB_DATA_ELT = "data_structure";
	protected static final String CB_ARG_ELT = "argument";
	protected static final String CB_LINK_ELT = "callback_link";

	// simulation part of cogtool+ meta model
	protected static final String SIMULATION_ELT = "simulation";
	protected static final String TRIAL_ELT = "trial";
	protected static final String EXTERNAL_ELT = "external";

	// system part of cogtool+ meta model
	protected static final String SYSTEM_ELT = "system";
	protected static final String GB_CALLBACK_ELT = "global_callback";

	// widget group part of cogtool+ meta model
	protected static final String WDT_GROUP_LIST_ELT = "widget_group_list";
	protected static final String WDT_GROUP_ELT = "widget_group";

	// task part of cogtool+ meta model
	protected static final String DEMO_TASK_ELT = "demonstration_task";
	protected static final String TASK_SETTING_ELT = "task_setting";
	protected static final String SESSION_ELT = "session";
	protected static final String TASK_VARIABLE_LIST_ELT = "variable_list";
	protected static final String TASK_LIST_ELT = "task_list";
	protected static final String TASK__ELT = "task";
	protected static final String START_FR_ATTR = "start_frame";
	protected static final String DYNAMIC_ATTR = "dynamic";
	protected static final String STATIC_ATTR = "static";

	// visual search part of cogtool+ meta model
	protected static final String VISUAL_SEARCH_ELT = "visual_search";
	protected static final String VISUAL_STRATEGY_ELT = "strategy";
	protected static final String VISUAL_SERACH_COL_ELT = "number_column";
	protected static final String DOUBLE_SCAN_ELT = "double_scan";
	protected static final String STARTING_TARGET_INDEX_ELT = "starting_target";
	protected static final String VISUAL_SCAN_ELT = "visual_scan";

	// non interactive part of cogtool+ meta model
	protected static final String NONINTER_ELT = "noninteractive";

	// button press and touch screen part of cogtool+ meta model
	protected static final String BT_PRESS_ELT = "button_press";
	protected static final String BT_STATE_ELT = "state";
	protected static final String BT_MODIFIER_ELT = "mod";
	protected static final String DELAY_ELT = "delay";
	protected static final String DELAY_LABEL_ELT = "label";
	protected static final String TOUCH_ELT = "touch_screen";

	// think of cogtool+ meta model
	protected static final String THINK_ELT = "think_process";
	protected static final String THINK_TIME_ELT = "duration";
	protected static final String THINK_LABEL_ELT = "label";

	// global hash map to store results generated by gloabal call back function
	protected HashMap<String, CallBackElement> globalCallBackMap = new HashMap<String, CallBackElement>();
	// local hash map to store results generated by call back function
	protected HashMap<String, CallBackElement> localCallBackMap = new HashMap<String, CallBackElement>();

	/////////////////////////////////////////////////////////
	protected static final String TASK_VISUAL = "visual_task";
	protected static final String TASK_BUTTONPRESS = "buttonpress_task";
	protected static final String TASK_NONINTER = "noninter_task";
	protected static final String TASK_TOUCH = "touch_task";
	protected static final String TASK_THINK = "think_task";

	protected static final String FROM_ELT = "from";
	protected static final String TO_ELT = "to";
	protected static final String ROW = "row";
	protected static final String COLUMN = "column";
	protected static final String T_ROW = "rowTarget";
	protected static final String T_COLUMN = "columnTarget";
	protected static final String INPUT_LINK = "input";
	protected static final String OUTPUT_LINK = "output";
	protected static final String NUM_OUTPUT = "numOutput";
	protected static final String BUTTONPRESS = "buttonPress";
	protected static final String DYNAMIC_ELT = "dynamic";
	protected static final String RANDOM_ELT = "random";
	protected static final String MIN_NUM = "minNum";
	protected static final String MAX_NUM = "maxNum";
	protected static final String INTERACTIVE = "interactive";
	protected static final String IO_ELT = "IO";
	protected static final String WIDGET_CLUSTER = "widgetcluster";
	protected static final String TASK_CLUSTER = "taskcluster";
	protected static final String WIDGET_LIST = "widgetlist";
	protected static final String DESCRIPTION = "description";
	protected static final String PROPERTY_ATTR = "property";
	protected static final String VAR_ATTR = "var";
	protected static final String INTEGER_ATTR = "Integer";
	protected static final String SEMICOLON = ";";// semi-colon

	protected static final String SINGLE_ATTR = "single";
	protected HashMap<String, IWidget> dynamicWidgetList = new HashMap<String, IWidget>();
	protected HashMap<String, ArrayList<String>> widgetGroup = new HashMap<String, ArrayList<String>>();
	protected HashMap<String, TaskElement> taskMap = new HashMap<String, TaskElement>();
	protected ArrayList<String> taskOrderList = new ArrayList<String>();
	protected String startingFrame = null;
	// protected Integer trial = 1;
	protected HashMap<String, Object> taskLinkMap = new HashMap<String, Object>();
	protected String userHand = "right"; // default value;
	protected String demonstrationName = "demo"; // default value;
	protected HashMap<String, GlobalVariable> globalVarMap = new HashMap<String, GlobalVariable>();
	protected ArrayList<String> frameNameList = new ArrayList<String>();
	protected HashMap<String, ArrayList<TaskElement>> frameParameterMap = new HashMap<String, ArrayList<TaskElement>>();

	protected HashMap<String, Variable> externalVariableMap = new HashMap<String, Variable>();
	protected Integer currentTrial = null;

	// protected HashMap<String, Object> prefSetting = new HashMap<String,
	// Object>();
	// -----------------------------------------------------------------------//

	// ---------The below global variables are inherited from cogtool---------//
	public static final String CURRENT_VERSION = "1";

	public static final String DESIGN_ELT = "design";
	public static final String DEVICE_ELT = "device";
	public static final String FRAME_ELT = "frame";
	public static final String CONTROLS_ELT = "controls";
	public static final String CONTROL_ELT = "control";
	public static final String HREF_ELT = "href";
	public static final String BALSAMIQ_TEXT_ELT = "text";

	public static final String MOCKUP_ELT = "mockup";
	public static final String DEMONSTRATION_ELT = "demonstration";
	public static final String BKG_IMAGE_PATH_ELT = "backgroundImagePath";
	public static final String BKG_IMAGE_DATA_ELT = "backgroundImageData";
	public static final String ORIGIN_ELT = "topLeftOrigin";
	public static final String SPEAKER_TEXT_ELT = "speakerText";
	public static final String LISTEN_TIME_SECS_ELT = "listenTimeSecs";
	public static final String WIDGET_ELT = "widget";
	public static final String ELTGROUP_ELT = "eltGroup";
	public static final String ELTNAME_ELT = "elementName";
	public static final String KEYBOARD_TRANSITIONS_ELT = "keyboardTransitions";
	public static final String VOICE_TRANSITIONS_ELT = "voiceTransitions";
	public static final String DISPLAY_LABEL_ELT = "displayLabel";
	public static final String AUX_TEXT_ELT = "auxText";
	public static final String EXTENT_ELT = "extent";
	public static final String TRANSITION_ELT = "transition";
	public static final String ACTION_ELT = "action";
	public static final String MOUSE_ACTION_ELT = "mouseAction";
	public static final String TOUCHSCREEN_ACTION_ELT = "touchscreenAction";
	public static final String GRAFFITI_ACTION_ELT = "graffitiAction";
	public static final String KEYBOARD_ACTION_ELT = "keyboardAction";
	public static final String VOICE_ACTION_ELT = "voiceAction";
	public static final String KBD_MODIFIER_ELT = "modifier";
	public static final String GESTURES_ELT = "gestures";
	public static final String TEXT_ELT = "text";
	public static final String DEMO_STEP_ELT = "demonstrationStep";
	public static final String ACTION_STEP_ELT = "actionStep";
	public static final String KEYBOARD_STEP_ELT = "keyboardActionStep";
	public static final String VOICE_STEP_ELT = "voiceActionStep";
	public static final String THINK_STEP_ELT = "thinkStep";
	public static final String SYS_DELAY_STEP_ELT = "systemDelayStep";
	public static final String LOOK_AT_STEP_ELT = "lookAtWidgetStep";
	public static final String START_EYE_LOC_ELT = "startingEyePosition";
	public static final String START_MOUSE_LOC_ELT = "startingMousePosition";
	public static final String START_LEFT_POS_ELT = "startingLeftHandPosition";
	public static final String START_RIGHT_POS_ELT = "startingRightHandPosition";
	public static final String TASK_ELT = "task";
	public static final String TASK_GROUP_ELT = "taskGroup";

	public static final String VERSION_ATTR = "version";
	public static final String NAME_ATTR = "name";
	public static final String TYPE_ATTR = "type";
	public static final String PARENT_ATTR = "parent";
	public static final String GROUP_ATTR = "group";
	public static final String REMOTE_LABEL_ATTR = "remoteLabel";
	public static final String CONTROL_TYPE_ATTR = "controlTypeID";
	public static final String CONTROL_ID_ATTR = "controlID";
	public static final String TASK_GROUP_ID_ATTR = "taskGroupID";

	public static final String X_ATTR = "x";
	public static final String Y_ATTR = "y";
	public static final String WIDTH_ATTR = "width";
	public static final String HEIGHT_ATTR = "height";
	public static final String MEASURED_WIDTH_ATTR = "measuredW";
	public static final String MEASURED_HEIGHT_ATTR = "measuredH";
	public static final String SHAPE_ATTR = "shape";
	public static final String DEST_FRAME_NAME_ATTR = "destinationFrameName";
	public static final String BUTTON_ATTR = "button";
	public static final String ACTION_ATTR = "action";
	public static final String IS_CMD_ATTR = "is-command";
	public static final String TASK_NAME_ATTR = "taskName";
	public static final String START_FRAME_NAME_ATTR = "startFrameName";
	public static final String HANDEDNESS_ATTR = "handedness";
	public static final String TARGET_WIDGET_NAME_ATTR = "targetWidgetName";
	public static final String LOOKAT_WIDGET_NAME_ATTR = "lookAtWidgetName";
	public static final String DURATION_ATTR = "durationInSecs";
	public static final String THINK_LABEL_ATTR = "thinkLabel";
	public static final String DELAY_LABEL_ATTR = "delayLabel";
	public static final String GROUP_NATURE_ATTR = "displayedGroupSummary";

	// General WidgetAttributes attribute names and values
	// REMEMBER TO ENTER INTO THE REGISTRIES BELOW!!!!
	public static final String TRUE_VALUE = "true";
	public static final String FALSE_VALUE = "false";
	public static final String IS_SELECTED_ATTR = "w-is-selected";
	public static final String IS_SELECTED_VALUE = TRUE_VALUE;
	public static final String NOT_SELECTED_VALUE = FALSE_VALUE;
	public static final String TOGGLE_VALUE = "toggle";
	public static final String IS_TOGGLEABLE_ATTR = "w-is-toggleable";
	public static final String IS_STANDARD_ATTR = "w-is-standard";
	public static final String SELECTION_ATTR = "w-selected-name";
	public static final String IS_RENDERED_ATTR = "w-is-rendered";
	public static final String IS_SEPARATOR_ATTR = "w-is-separator";
	public static final String APPENDED_TEXT_ATTR = "w-appended-text";
	public static final String SUBMENU_ACTION_ATTR = "w-submenu-action";
	public static final String TAP_VALUE = "tap";
	public static final String CLICK_VALUE = "click";
	public static final String HOVER_VALUE = "hover";
	public static final String SUBMENU_DELAY_ATTR = "w-submenu-delay";
	public static final String PC_DELAY_VALUE = "pc-delay";
	public static final String NO_DELAY_VALUE = "no-delay";
	public static final String CONTEXT_MENU_ACTION_ATTR = "w-menu-action";
	public static final String CTRL_LEFT_VALUE = "ctrl-left-click";
	public static final String TAP_HOLD_VALUE = "tap-hold";
	public static final String MENU_KEY_VALUE = "menu-key";
	public static final String RIGHT_CLICK_VALUE = "right-click";

	public static final String KEYBOARD_DEVICE = "keyboard";
	public static final String MOUSE_DEVICE = "mouse";
	public static final String TOUCHSCREEN_DEVICE = "touchscreen";
	public static final String MICROPHONE_DEVICE = "microphone";
	public static final String DISPLAY_DEVICE = "display";
	public static final String SPEAKER_DEVICE = "speaker";

	public static final String BUTTON_WIDGETTYPE = "button";
	public static final String LINK_WIDGETTYPE = "link";
	public static final String CHECKBOX_WIDGETTYPE = "check box";
	public static final String RADIO_WIDGETTYPE = "radio button";
	public static final String TEXTBOX_WIDGETTYPE = "text box";
	public static final String TEXT_WIDGETTYPE = "text";
	public static final String PULLDOWNLIST_WIDGETTYPE = "pull-down list";
	public static final String PULLDOWNITEM_WIDGETTYPE = "pull-down item";
	public static final String LISTBOXITEM_WIDGETTYPE = "list box item";
	public static final String CONTEXTMENU_WIDGETTYPE = "context menu";
	public static final String MENUHEADER_WIDGETTYPE = "menu";
	public static final String SUBMENU_WIDGETTYPE = "submenu";
	public static final String MENUITEM_WIDGETTYPE = "menu item";
	public static final String GRAFFITI_WIDGETTYPE = "graffiti";
	public static final String NONINTERACTIVE_WIDGETTYPE = "non-interactive";

	public static final String RECTANGLE_SHAPE = "rectangle";
	public static final String ELLIPSE_SHAPE = "ellipse";
	public static final String ROUND_RECT_SHAPE = "rounded rectangle";

	public static final String LEFT_BUTTON = "left";
	public static final String MIDDLE_BUTTON = "middle";
	public static final String RIGHT_BUTTON = "right";

	public static final String DOWNUP_ACTION = "downUp";
	public static final String TAP_ACTION = "tap";
	public static final String PRESS_ACTION = "press";
	public static final String DOUBLE_ACTION = "double";
	public static final String TRIPLE_ACTION = "triple";
	public static final String DOWN_ACTION = "down";
	public static final String UP_ACTION = "up";
	public static final String HOVER_ACTION = "hover";

	public static final String SHIFT_MODIFIER = "SHIFT";
	public static final String CTRL_MODIFIER = "CTRL";
	public static final String ALT_MODIFIER = "ALT";
	public static final String COMMAND_MODIFIER = "COMMAND";
	public static final String FUNCTION_MODIFIER = "FUNCTION";

	public static final String RIGHT_HAND = "right";
	public static final String LEFT_HAND = "left";

	public static final String FINAL_FRAME_NAME = "FINAL FRAME";

	protected static final String CB_COLUMN_ELT = "column";
	protected static final String CB_ROW_ELT = "row";
	// -----------------------------------------------------------------------//

	/**
	 * Thrown when the import cannot complete.
	 */
	@SuppressWarnings("serial")
	public static class ImportFailedException extends RuntimeException {
		public ImportFailedException(String msg, Throwable ex) {
			super(msg, ex);
		}

		public ImportFailedException(String msg) {
			super(msg);
		}
	}

	private static final Map<String, IAttributed.AttributeDefinition<?>> ATTRIBUTE_REGISTRY = new HashMap<String, IAttributed.AttributeDefinition<?>>();

	private static final Map<String, Object> VALUE_REGISTRY = new HashMap<String, Object>();

	private static IAttributed.AttributeDefinition<?> getAttrDefn(String attr) {
		return IAttributed.AttributeRegistry.ONLY.getAttributeDefn(attr);
	}

	private static void registerAttributes() {
		ATTRIBUTE_REGISTRY.put(IS_SELECTED_ATTR, getAttrDefn(WidgetAttributes.IS_SELECTED_ATTR));
		ATTRIBUTE_REGISTRY.put(IS_TOGGLEABLE_ATTR, getAttrDefn(WidgetAttributes.IS_TOGGLEABLE_ATTR));
		ATTRIBUTE_REGISTRY.put(IS_STANDARD_ATTR, getAttrDefn(WidgetAttributes.IS_STANDARD_ATTR));
		ATTRIBUTE_REGISTRY.put(SELECTION_ATTR, getAttrDefn(WidgetAttributes.SELECTION_ATTR));
		ATTRIBUTE_REGISTRY.put(IS_RENDERED_ATTR, getAttrDefn(WidgetAttributes.IS_RENDERED_ATTR));
		ATTRIBUTE_REGISTRY.put(IS_SEPARATOR_ATTR, getAttrDefn(WidgetAttributes.IS_SEPARATOR_ATTR));
		ATTRIBUTE_REGISTRY.put(APPENDED_TEXT_ATTR, getAttrDefn(WidgetAttributes.APPENDED_TEXT_ATTR));
		ATTRIBUTE_REGISTRY.put(SUBMENU_ACTION_ATTR, getAttrDefn(WidgetAttributes.SUBMENU_ACTION_ATTR));
		ATTRIBUTE_REGISTRY.put(SUBMENU_DELAY_ATTR, getAttrDefn(WidgetAttributes.SUBMENU_DELAY_ATTR));
		ATTRIBUTE_REGISTRY.put(CONTEXT_MENU_ACTION_ATTR, getAttrDefn(WidgetAttributes.CONTEXT_MENU_ACTION_ATTR));

		VALUE_REGISTRY.put(TRUE_VALUE, Boolean.TRUE);
		VALUE_REGISTRY.put(FALSE_VALUE, Boolean.FALSE);
		VALUE_REGISTRY.put(TOGGLE_VALUE, WidgetAttributes.TOGGLE_SELECTION);

		VALUE_REGISTRY.put(TAP_VALUE, WidgetAttributes.TAP_SUBMENU_ACTION);
		VALUE_REGISTRY.put(CLICK_VALUE, WidgetAttributes.CLICK_SUBMENU_ACTION);
		VALUE_REGISTRY.put(HOVER_VALUE, WidgetAttributes.HOVER_SUBMENU_ACTION);

		VALUE_REGISTRY.put(PC_DELAY_VALUE, WidgetAttributes.PC_SUBMENU_DELAY);
		VALUE_REGISTRY.put(NO_DELAY_VALUE, WidgetAttributes.NO_SUBMENU_DELAY);

		VALUE_REGISTRY.put(CTRL_LEFT_VALUE, WidgetAttributes.CTRL_LEFT_CLICK);
		VALUE_REGISTRY.put(TAP_HOLD_VALUE, WidgetAttributes.TAP_HOLD);
		VALUE_REGISTRY.put(MENU_KEY_VALUE, WidgetAttributes.MENU_KEY_PRESS);
		VALUE_REGISTRY.put(RIGHT_CLICK_VALUE, WidgetAttributes.RIGHT_CLICK);
	}

	private static boolean notInitialized = true;

	private static void addAttributes(IAttributed attributed, Node node) {
		addAttributes(attributed, node, null);
	}

	private static void addAttributes(IAttributed attributed, Node node, Map<IAttributed, String> pendingAttrSets) {
		NamedNodeMap attributes = node.getAttributes();

		if (attributes != null) {
			int numAttributes = attributes.getLength();

			if (numAttributes > 0) {
				if (notInitialized) {
					registerAttributes();
				}

				for (int i = 0; i < numAttributes; i++) {
					Node attributeNode = attributes.item(i);

					// Should never be null; sanity check
					if (attributeNode != null) {
						String attribute = attributeNode.getNodeName();
						IAttributed.AttributeDefinition<?> attrDefn = ATTRIBUTE_REGISTRY.get(attribute);

						if (attrDefn != null) {
							String attrName = attrDefn.attrName;
							String attrNodeValue = attributeNode.getNodeValue();

							if (WidgetAttributes.SELECTION_ATTR.equals(attrName)) {
								// attrNodeValue is name of the selected widget,
								// but the widget may not exist yet.
								pendingAttrSets.put(attributed, attrNodeValue);
							} else if (VALUE_REGISTRY.containsKey(attrNodeValue)) {
								Object attrValue = VALUE_REGISTRY.get(attrNodeValue);

								if (!NullSafe.equals(attrValue, attrDefn.defaultValue)) {
									attributed.setAttribute(attrName, attrValue);
								}
							} else {
								// Assume string value (eg, APPENDED_TEXT_ATTR)
								attributed.setAttribute(attrName, attrNodeValue);
							}
						}
					}
				}
			}
		}
	}

	private String path = ""; // absolute path read from configure file

	private String directoryPath = "";
	private ObjectLoader objLoader = new ObjectLoader();

	// Maps Design to List of Demonstration
	private Map<Design, Collection<Demonstration>> designs = new LinkedHashMap<Design, Collection<Demonstration>>();

	private Set<AUndertaking> newUndertakings = new LinkedHashSet<AUndertaking>();

	private Map<String, TaskGroup> taskGroups = new HashMap<String, TaskGroup>();

	private List<String> failedObjectErrors = new ArrayList<String>();
	//private List<String> failedImages = new ArrayList<String>();
	private List<String> klmWarnings = new ArrayList<String>();

	private String dtdVersion = "0";
	// Maps name to newly created Task
	private Map<String, List<Task>> createdTaskRegistry = new HashMap<String, List<Task>>();

	// Maps file name to image data (byte[])
	private Map<String, byte[]> imageRegistry = new HashMap<String, byte[]>();

	private Map<String, SimpleWidgetGroup> groupRegistry = new HashMap<String, SimpleWidgetGroup>();

	// Image data cache; maps image name to data (byte[])
	private Map<String, byte[]> cachedImages = new HashMap<String, byte[]>();

	private static ObjectLoader.IObjectLoader<Design> designLoader = Design.getImportLoader();
	private static ObjectLoader.IObjectLoader<Frame> frameLoader = Frame.getImportLoader();
	private static ObjectLoader.IObjectLoader<Widget> widgetLoader = Widget.getImportLoader();
	private static ObjectLoader.IObjectLoader<FrameElementGroup> groupLoader = FrameElementGroup.getImportLoader();

	public void setAbsolutePath(String input) {
		path = input;
	}

	public String getAbsolutePath() {
		return path;
	}

	public boolean importXML(File inputFile, TaskParent parent, CognitiveModelGenerator modelGen,
			HashMap<String, Variable> _variableMap, int _currentTrial)
			throws IOException, SAXException, SecurityException {
		InputStream fis = null;
		fis = new FileInputStream(inputFile);
		externalVariableMap = _variableMap;
		currentTrial = _currentTrial;
		boolean result = false;

		try {
			Reader input = new InputStreamReader(fis, "UTF-8");
			DOMParser parser = new DOMParser(); // Create a Xerces DOM Parser
			parser.parse(new InputSource(input)); // Parse the Document and
													// traverse the DOM
			Document document = parser.getDocument();
			parseFile(document, parent);
			/*
			 * try { result = importXML(input, inputFile.getParent() +
			 * File.separator, parent, modelGen); } finally { input.close(); }
			 */
		} finally {
			fis.close();
		}
		return result;
	}

	/*
	 * This is temporarily made inactive, as there is no need to add images to
	 * cogtool+
	 * 
	 * public boolean importXML(Reader input, String imageDirPath, TaskParent
	 * taskParent, CognitiveModelGenerator modelGen) throws IOException,
	 * SAXException { modelGenerator = modelGen; // Create a Xerces DOM Parser
	 * DOMParser parser = new DOMParser(); // Set the path for loading images
	 * directoryPath = imageDirPath; // Parse the Document and traverse the DOM
	 * parser.parse(new InputSource(input)); Document document =
	 * parser.getDocument(); parseFile(document, taskParent); if
	 * (failedImages.size() > 0) { String failedImageString =
	 * "Failed to load the following images:"; Iterator<String> fIter =
	 * failedImages.iterator(); while (fIter.hasNext()) { failedImageString +=
	 * System.getProperty("file.separator") + fIter.next(); } throw new
	 * GraphicsUtil.ImageException(failedImageString); } return true; }
	 */
	public Map<Design, Collection<Demonstration>> getDesigns() {
		return designs;
	}

	public Set<AUndertaking> getNewUndertakings() {
		return newUndertakings;
	}

	public List<String> getObjectFailures() {
		return failedObjectErrors;
	}

	public List<String> getGenerationWarnings() {
		return klmWarnings;
	}

	private String getAttributeValue(Node node, String attr) {
		NamedNodeMap attributes = node.getAttributes();

		if (attributes != null) {
			Node attributeNode = attributes.getNamedItem(attr);

			if (attributeNode != null) {
				return attributeNode.getNodeValue();
			}
		}

		return null;
	}

	private String getElementText(Node node) {
		if (node.getFirstChild() != null) {
			return node.getFirstChild().getNodeValue().trim();
		}

		return "";
	}

	// -----------------------------------------------------------------------//
	/**
	 * Imports an meta model (XML file) containing descriptive meta model as
	 * well as the association with algorithm meta model.
	 * 
	 * @param node
	 * @param taskParent
	 * @throws IOException
	 */
	public void parseFile(Node node, TaskParent taskParent) throws IOException {
		NodeList children = node.getChildNodes();

		if (children != null) {
			if (node.getNodeName().equalsIgnoreCase(META_MODEL_IMPORT_ELT)) {
				dtdVersion = getAttributeValue(node, VERSION_ATTR);
				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();
					if (nodeName.equalsIgnoreCase(SYSTEM_ELT)) {
						parseSystem(child, taskParent);
					} /*
						 * else if (nodeName.equalsIgnoreCase(PREF_ELT)){
						 * parsePrefSetting(child); }
						 */
				}
			} else {
				for (int i = 0; i < children.getLength(); i++) {
					parseFile(children.item(i), taskParent);
				}
			}
		}
	}

	private void parseSystem(Node node, TaskParent taskParent) throws IOException {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(GB_CALLBACK_ELT)) {
					parseGlobalCallBack(child, taskParent);
				} else if (nodeName.equalsIgnoreCase(DESIGN_ELT)) {
					parseDesign(child, taskParent);
				} else if (nodeName.equalsIgnoreCase(WDT_GROUP_LIST_ELT)) {
					parseWidgetGroupList(child, taskParent);
				} else if (nodeName.equalsIgnoreCase(DEMO_TASK_ELT)) {
					String starting_frame = getAttributeValue(child, START_FR_ATTR);
					demonstrationName = getAttributeValue(child, NAME_ATTR);
					parseDemonstrationTasks(child, starting_frame, taskParent);
				} else if (nodeName.equalsIgnoreCase(GLOBAL_VAR_ELT)) {
					parseGlobalVariable(child, taskParent);
				}
			}
		}
	}

	private Variable parseVariable(Node node, TaskParent taskParent) {
		String id = null;
		String type = null;
		Object value = null;
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(ID_ELT)) {
					id = child.getTextContent();
				} else if (nodeName.equalsIgnoreCase(TYPE_ELT)) {
					type = child.getTextContent();
				} else if (nodeName.equalsIgnoreCase(VALUE_ELT)) {
					value = parseValue(child, type);
				}
			}
		}

		Variable var = new Variable(id, type, value);
		return var;
	}

	private Object parseValue(Node node, String type) {
		Object value = null;
		String ref = getAttributeValue(node, REF_ATTR);
		if (ref.equalsIgnoreCase("true")) {
			NodeList children = node.getChildNodes();
			if (children != null) {
				String id = null;
				String dataType = null;
				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();
					if (nodeName.equalsIgnoreCase(ID_ELT)) {
						id = child.getTextContent();	
					}
					else if (nodeName.equalsIgnoreCase(TYPE_ELT)){
						dataType = child.getTextContent();
						switch (dataType){
						case "String":
							if (getAttributeValue(node, TYPE_ATTR).equalsIgnoreCase(DYNAMIC_ELT)) {
								String[] tempString = (String[]) externalVariableMap.get(id).getValue();
								String tmp = tempString[currentTrial];
								String parts[] = tmp.split(SEMICOLON);
								ArrayList<String> list = new ArrayList<String>();
								for (int j = 0; j < parts.length; j++) {
									list.add(parts[j]);
								}
								value = list;
							} else {
								String tmp = (String) externalVariableMap.get(id).getValue();
								String parts[] = tmp.split(SEMICOLON);
								ArrayList<String> list = new ArrayList<String>();
								for (int j = 0; j < parts.length; j++) {
									list.add(parts[j]);
								}
								value = list;
							}
							break;
						case "Integer":
							if (getAttributeValue(node, TYPE_ATTR).equalsIgnoreCase(DYNAMIC_ELT)) {								
								Integer[] tempInt = (Integer[]) externalVariableMap.get(id).getValue();
								value = tempInt[currentTrial];								
							} else {
								Integer tmpInt = (Integer) externalVariableMap.get(id).getValue();
								value = tmpInt;
							}
							break;
						case "Double":
							if (getAttributeValue(node, TYPE_ATTR).equalsIgnoreCase(DYNAMIC_ELT)) {
								Integer[] tempDouble = (Integer[]) externalVariableMap.get(id).getValue();
								value = tempDouble[currentTrial];								
							} else {
								Integer tempDouble = (Integer) externalVariableMap.get(id).getValue();
								value = tempDouble;
							}
							break;
						}
					}
						
						// dataType = child.getTextContent();
						/*
						 * switch(dataType){ case "Integer": Integer[] tempInt =
						 * (Integer[]) externalVariableMap.get(id).getValue();
						 * if (getAttributeValue(node,
						 * TYPE_ATTR).equalsIgnoreCase(DYNAMIC_ELT)) value =
						 * tempInt[currentTrial]; else value = tempInt; break;
						 * case "Double": Double[] tempDouble = (Double[])
						 * externalVariableMap.get(id).getValue(); if
						 * (getAttributeValue(node,
						 * TYPE_ATTR).equalsIgnoreCase(DYNAMIC_ELT)) value =
						 * tempDouble[currentTrial]; else value = tempDouble;
						 * break; case "String": String[] tempString =
						 * (String[]) externalVariableMap.get(id).getValue(); if
						 * (getAttributeValue(node,
						 * TYPE_ATTR).equalsIgnoreCase(DYNAMIC_ELT)) value =
						 * tempString[currentTrial]; else value = tempString;
						 * break; }
						 */
						// }
					}
				
			}
		} else {
			switch (type) {
			case "Integer":
				value = node.getTextContent();
				break;
			case "String":
				value = node.getTextContent();
				break;
			case "Double":
				value = node.getTextContent();
				break;
			case "ArrayList":
				String tmp = node.getTextContent().toString();
				String parts[] = tmp.split(SEMICOLON);
				ArrayList<String> list = new ArrayList<String>();
				for (int j = 0; j < parts.length; j++) {
					list.add(parts[j]);
				}
				value = list;
				break;
			}
		}
		return value;
	}

	private void parseGlobalVariable(Node node, TaskParent taskParent) {
		NodeList children = node.getChildNodes();
		//Variable var = null;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(VAR_ELT)) {
					Variable tmp = parseVariable(child, taskParent);
					GlobalVariable var = new GlobalVariable(tmp);
					if (!globalVarMap.containsKey(var.getId())) {
						globalVarMap.put(var.getId(), var);
					} else {
						System.out.println(
								"The id for this variable has been used, please double check your meta model !");
						System.exit(0);
					}
				}
			}
		}
	}

	private void parseWidgetGroupList(Node node, TaskParent taskParent) {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(WDT_GROUP_ELT)) {
					String name = getAttributeValue(child, NAME_ATTR);
					String frameName = getAttributeValue(child, FRAME_REF_ATTR);
					if (frameName == null) {
						for (int j = 0; j < frameNameList.size(); j++) {
							frameName = frameNameList.get(j);
							parseWidgetGroup(child, name, frameName, taskParent);
						}
					} else {
						parseWidgetGroup(child, name, frameName, taskParent);
					}
				}
			}
		}
	}

	private void parseDemonstrationTasks(Node node, String name, TaskParent taskParent) {
		startingFrame = name;
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equals(TASK_SETTING_ELT)) {
					String type = getAttributeValue(child, TYPE_ATTR);
					if (type.equalsIgnoreCase(DYNAMIC_ATTR)) {
						parseDynamicTasks(child, taskParent);
					}
					if (type.equalsIgnoreCase(STATIC_ATTR)) {
						parseStaticTasks(child, taskParent);
					}
				}
			}
		}
	}

	private void parseDynamicTasks(Node node, TaskParent taskParent) {
		int session = 0;
		Integer idx = null;
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(SESSION_ELT)) {
					session = parseSession(child, taskParent);
					// session = Integer.valueOf(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(TASK_LIST_ELT)) {
					if (session > 0) {
						for (idx = 0; idx < session; idx++) {
							parseTaskList(child, taskParent, idx);
						}
					} else {
						System.out.println("Error: the session number should be bigger than 0");
						System.exit(0);
					}
				}
			}
		}
	}

	private void parseStaticTasks(Node node, TaskParent taskParent) {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(TASK_LIST_ELT)) {
					parseTaskList(child, taskParent, null);
				}
			}
		}
	}

	private void parseTaskList(Node node, TaskParent taskParent, Integer idx) {

		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(TASK__ELT)) {
					// parseTask(child, taskParent, idx);
					taskOrderList.add(parseTask(child, taskParent, idx));
				}
			}
		}
	}

	private String parseTask(Node node, TaskParent taskParent, Integer idx) {
		TaskElement task = new TaskElement();
		String widgetGroupName = null;
		String taskName = getAttributeValue(node, NAME_ATTR);
		String frameName = getAttributeValue(node, FRAME_REF_ATTR);
		String taskType = getAttributeValue(node, TYPE_ATTR);
		if (taskType.equalsIgnoreCase(GROUP_ATTR)) {
			task.setSingleWidget(false);
		} else if (taskType.equalsIgnoreCase(SINGLE_ATTR)) {
			task.setSingleWidget(true);
		}
		if (frameName == null) {
			frameName = frameNameList.get(idx);
		}
		task.setTaskName(taskName);
		String type = null;
		String key = null;
		key = taskName + "#" + frameName;
		/*
		 * if (idx != null){ key = idx + "#" + taskName + "#" + frameName; }
		 * else{ key = taskName + "#" + frameName; }
		 */
		if (!taskMap.containsKey(key)) {
			NodeList children = node.getChildNodes();
			if (children != null) {
				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();
					if (nodeName.equalsIgnoreCase(WDT_GROUP_ELT)) {
						widgetGroupName = parseWidgetGroupName(child, taskParent, idx);
						// widgetGroupName = child.getTextContent();
						task.setGroupName(widgetGroupName);
					} else if (nodeName.equalsIgnoreCase(NONINTER_ELT)) {
						task = parseNoninter(child, taskParent, task, idx);
						type = TASK_NONINTER;
						task.setType(type);
					} else if (nodeName.equalsIgnoreCase(VISUAL_SCAN_ELT)) {
						task = parseVisualScan(child, taskParent, task, idx, key);
						type = TASK_VISUAL;
						task.setType(type);
					} else if (nodeName.equalsIgnoreCase(BT_PRESS_ELT)) {
						task = parseButtonPressAction(child, taskParent, task, idx);
						type = TASK_BUTTONPRESS;
						task.setType(type);
					} else if (nodeName.equalsIgnoreCase(TOUCH_ELT)) {
						task = parseTouchAction(child, taskParent, task, idx);
						type = TASK_TOUCH;
						task.setType(type);
					} else if (nodeName.equalsIgnoreCase(THINK_ELT)) {
						task = parseThinkAction(child, taskParent, task, idx);
						type = TASK_THINK;
						task.setType(type);
					}
				}
			}
			taskMap.put(key, task);
			if (task.containsWidgets()) {
				if (frameParameterMap.containsKey(frameName)) {
					ArrayList<TaskElement> tempList = frameParameterMap.get(frameName);
					tempList.add(task);
					frameParameterMap.put(frameName, tempList);
				} else if (!frameParameterMap.containsKey(frameName)) {
					ArrayList<TaskElement> tempList = new ArrayList<TaskElement>();
					tempList.add(task);
					frameParameterMap.put(frameName, tempList);
				}
			}
		} else {
			System.exit(0);
		}
		return key;
	}

	private NoninteractiveElement parseNoninter(Node node, TaskParent taskParent, TaskElement task, Integer idx) {
		NoninteractiveElement nTask = new NoninteractiveElement(task);
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				// target element for non interactive task, target is an integer
				// representing the index of an widget in the widget group
				if (nodeName.equalsIgnoreCase(TARGET_INDEX_ELT)) {
					nTask = (NoninteractiveElement) parseTargetIndex(child, taskParent, nTask, idx);
				} else if (nodeName.equalsIgnoreCase(TARGET_ELT)) {
					nTask.setTarget(child.getTextContent());
				}
			}
		}
		return nTask;
	}

	private TaskElement parseThinkAction(Node node, TaskParent taskParent, TaskElement task, Integer idx) {
		
		ThinkElement tkTask = new ThinkElement(task);
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				// target element for non interactive task, target is an integer
				// representing the index of an widget in the widget group
				if (nodeName.equalsIgnoreCase(THINK_TIME_ELT)) {
					if (child.getChildNodes().getLength() > 1) {
						NodeList subchildren = child.getChildNodes();
						for (int m = 0; m < subchildren.getLength(); m++) {
							String nName = subchildren.item(m).getNodeName();
							if (nName.equalsIgnoreCase(CALLBACK_ELT)) {
								CallBackElement tmp = parseCallBack(subchildren.item(m), taskParent, idx);
								if (!tmp.getDataType().equalsIgnoreCase("Double")) {
									System.out.println(
											"The data type for this call back must be an Double, please check your meta model!!");
									System.exit(0);
								} else {
									tkTask.setDuration((Double) tmp.getResult());
								}
							}
						}
					} else {
						tkTask.setDuration(Double.valueOf(child.getTextContent()));
					}
				}
				if (nodeName.equalsIgnoreCase(THINK_LABEL_ELT)) {
					tkTask.setLabel(child.getTextContent());
				}
			}
		}
		return tkTask;
	}

	private String parseWidgetGroupName(Node node, TaskParent taskParent, Integer idx) {
		String widgetGroupName = null;
		NodeList children = node.getChildNodes();
		if (children.getLength() > 1) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(CALLBACK_ELT)) {
					widgetGroupName = (String) parseCallBack(child, taskParent, idx).getResult();
				}else if(nodeName.equalsIgnoreCase(CB_LINK_ELT)){
					widgetGroupName = (String) parseCallBackLink(child, idx);
				}
			}
		} else {
			widgetGroupName = node.getTextContent();
		}

		return widgetGroupName;
	}

	private ButtonPressElement parseButtonPressAction(Node node, TaskParent taskParent, TaskElement task, Integer idx) {
		ButtonPressElement bTask = new ButtonPressElement(task);
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(TARGET_INDEX_ELT)) {
					bTask = (ButtonPressElement) parseTargetIndex(child, taskParent, bTask, idx);
					// bTask.setTargetIdx(Integer.valueOf(child.getTextContent()));
				} else if (nodeName.equalsIgnoreCase(TARGET_ELT)) {
					bTask.setTarget(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(BT_STATE_ELT)) {
					bTask.setButtonState(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(TYPE_ELT)) {
					bTask.setButtonEventType(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(BT_MODIFIER_ELT)) {
					bTask.setButtonModifier((Integer.valueOf(child.getTextContent())));
				} else if (nodeName.equalsIgnoreCase(DELAY_ELT)) {
					if (child.getChildNodes().getLength() > 1) {
						NodeList subchildren = child.getChildNodes();
						for (int m = 0; m < subchildren.getLength(); m++) {
							String nName = subchildren.item(m).getNodeName();
							if (nName.equalsIgnoreCase(CALLBACK_ELT)) {
								CallBackElement tmp = parseCallBack(subchildren.item(m), taskParent, idx);
								if (!tmp.getDataType().equalsIgnoreCase("Double")) {
									System.out.println(
											"The data type for this call back must be an Double, please check your meta model!!");
									System.exit(0);
								} else {
									bTask.setButtonDelay((Double) tmp.getResult());
								}
							}
						}
					} else {
						bTask.setButtonDelay(Double.valueOf(child.getTextContent()));
					}
				} else if (nodeName.equalsIgnoreCase(DELAY_LABEL_ELT)) {
					bTask.setButtonDelayLabel(child.getTextContent());
				}
			}
		}
		return bTask;
	}

	private TaskElement parseTouchAction(Node node, TaskParent taskParent, TaskElement task, Integer idx) {
		TouchScreenElement tTask = new TouchScreenElement(task);
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(TARGET_INDEX_ELT)) {
					tTask = (TouchScreenElement) parseTargetIndex(child, taskParent, tTask, idx);
					// bTask.setTargetIdx(Integer.valueOf(child.getTextContent()));
				} else if (nodeName.equalsIgnoreCase(TARGET_ELT)) {
					tTask.setTarget(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(TYPE_ELT)) {
					tTask.setTouchType(child.getTextContent());
				} else if (nodeName.equalsIgnoreCase(DELAY_ELT)) {
					tTask.setTouchDelay((Double.valueOf(child.getTextContent())));
				} else if (nodeName.equalsIgnoreCase(DELAY_LABEL_ELT)) {
					tTask.setDelayLabel(child.getTextContent());
				}
			}
		}
		return tTask;
	}

	@SuppressWarnings("unchecked")
	private TaskElement parseTargetIndex(Node node, TaskParent taskParent, TaskElement task, Integer idx) {
		Integer target = null;
		if (node.getChildNodes().getLength() > 1) {
			NodeList children = node.getChildNodes();
			for (int m = 0; m < children.getLength(); m++) {
				Node child = children.item(m);
				String nName = child.getNodeName();
				if (nName.equalsIgnoreCase(CALLBACK_ELT)) {
					CallBackElement tmp = parseCallBack(child, taskParent, idx);
					if (!tmp.getDataType().equalsIgnoreCase("Integer")) {
						System.out.println(
								"The data type for this call back must be an Integer, please check your meta model!!");
						System.exit(0);
					} else {
						target = ((Double) tmp.getResult()).intValue();
					}
				} else if (nName.equalsIgnoreCase(CB_LINK_ELT)) {
					target = ((Double) parseCallBackLink(child, idx)).intValue();
				} else if (nName.equalsIgnoreCase(GLOBAL_VAR_LINK_ELT)) {
					GlobalVariable gvar = parseVarLink(child);
					String type = getAttributeValue(child, TYPE_ATTR);					
					if (idx != null) {
						if (type.equalsIgnoreCase("dynamic")) {
							ArrayList<String> tmp = (ArrayList<String>) gvar.getValue();
							target = Integer.valueOf(tmp.get(idx).toString());
						} else if (type.equalsIgnoreCase("static")) {
							target = Integer.valueOf(gvar.getValue().toString());
						}
					} else {
						target = Integer.valueOf(gvar.getValue().toString());
					}
				}
			}
		}
		// get the target from meta model straight forward
		else {
			if (getAttributeValue(node, REF_ATTR).equalsIgnoreCase(TRUE_VALUE)) {
				String key = null;
				if (idx != null)
					key = node.getTextContent() + frameNameList.get(idx);
				else
					key = node.getTextContent();
				target = taskMap.get(key).getTargetIdx();
			} else {
				target = Integer.valueOf(node.getTextContent());
			}
		}
		if (target != null) {
			task.setTargetIdx(target);
		}
		return task;
	}

	private CallBackElement parseCallBack(Node node, TaskParent taskParent, Integer idx) {
		String fileType = getAttributeValue(node, TYPE_ATTR);
		NodeList children = node.getChildNodes();
		CallBackElement cb = new CallBackElement();
		cb.setFileType(fileType);
		String id = null;
		String file = null;
		String function = null;
		String dataStructure = null;
		Object output = null;
		List<Object> inputArguments = new ArrayList<Object>();
		Integer row = 0;
		if (fileType.equalsIgnoreCase("js")) {
			if (children != null) {
				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();
					if (nodeName.equalsIgnoreCase(ID_ELT)) {
						id = String.valueOf(child.getTextContent());
						cb.setID(id);
					}
					if (nodeName.equalsIgnoreCase(CB_FILE_ELT)) {
						String fileName = String.valueOf(child.getTextContent()); // Get
																					// the
																					// java
																					// script
																					// file
																					// name
						if (path.equalsIgnoreCase("")) {
							path = getAbsolutePath();
						}
						file = path + "/" + fileName; // Get the absolute path
														// for
														// the java script file
						cb.setFile(file);
					}
					if (nodeName.equalsIgnoreCase(FUNCTION_ELT)) {
						function = String.valueOf(child.getTextContent()); // Get
																			// the
																			// function
						cb.setFunction(function);
					}
					if (nodeName.equalsIgnoreCase(CB_ARG_LIST_ELT)) {
						inputArguments = parseArgument(child, taskParent, idx);
						cb.setInputArguments(inputArguments);
					}
					if (nodeName.equalsIgnoreCase(CB_DATA_ELT)) {
						dataStructure = String.valueOf(child.getTextContent());
						cb.setDataType(dataStructure);
					}
				}
				try {
					ScriptEngineManager manager = new ScriptEngineManager();
					ScriptEngine engine = manager.getEngineByName("JavaScript");
					engine.eval(new FileReader(file));
					Invocable inv = (Invocable) engine;
					output = dynamicInvokeFunction(inv, function, inputArguments);
					cb.setResult(output);
				} catch (ScriptException | FileNotFoundException | NoSuchMethodException ex) {
				}
			}
		} else if (fileType.equalsIgnoreCase("csv")) {
			if (children != null) {
				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();
					if (nodeName.equalsIgnoreCase(CB_FILE_ELT)) {
						String fileName = String.valueOf(child.getTextContent());
						file = path + "/" + fileName; // Get the absolute path
														// for java script file
						cb.setFile(file);
					} else if (nodeName.equalsIgnoreCase(CB_ROW_ELT)) {
						row = Integer.parseInt(child.getTextContent());
					} else if (nodeName.equalsIgnoreCase(CB_DATA_ELT)) {
						dataStructure = String.valueOf(child.getTextContent());
						cb.setDataType(dataStructure);
					}
				}
				// get a csv parser and save results
				CogToolPlusCSVParser parser = new CogToolPlusCSVParser();
				switch (dataStructure) {
				case "Double":
					cb.setResult(parser.DoubleArrayReadCSV(file).get(row));
					break;
				case "Integer":
					cb.setResult(parser.IntegerArrayReadCSV(file).get(row));
					break;
				case "String":
					cb.setResult(parser.StringArrayReadCSV(file).get(row));
					break;
				}
			}
		}
		if (cb.containsID())
			localCallBackMap.put(id, cb);
		return cb;
	}

	private Object parseCallBackLink(Node child, Integer idx) {
		Object results = null;
		CallBackElement callback = null;
		// NodeList children = node.getChildNodes();
		// if (children != null) {
		// for (int i = 0; i < children.getLength(); i++) {
		// Node child = children.item(i);
		// String nodeName = child.getNodeName();
		// if (nodeName.equalsIgnoreCase(CB_LINK_ELT)) {
		String type = getAttributeValue(child, TYPE_ATTR);
		String style = getAttributeValue(child, STYLE_ATTR);
		String id = String.valueOf(child.getTextContent());
		if (style.equalsIgnoreCase("global"))
			if (globalCallBackMap.containsKey(id)) {
				callback = globalCallBackMap.get(id);
			} else {
				System.out.println("The key for the hashmap is incorrect");
				System.exit(0);
			}
		if (style.equalsIgnoreCase("local"))
			if (localCallBackMap.containsKey(id)) {
				callback = localCallBackMap.get(id);
			} else {
				System.out.println("The key for the hashmap is incorrect");
				System.exit(0);
			}
		if (idx != null) {
			if (type.equalsIgnoreCase("dynamic")) {
				ArrayList<?> tmp = (ArrayList<?>) callback.getResult();
				results = tmp.get(idx);
			} else if (type.equalsIgnoreCase("static")) {
				results = callback.getResult();
			}
		} else {
			results = callback.getResult();
		}
		return results;
	}

	// To convert a string into an array list
	private ArrayList<Object> string2ArrayList(String input, String separator) {
		ArrayList<Object> output = new ArrayList<Object>();
		String[] parts = input.split(separator);
		for (int i = 0; i < parts.length; i++)
			output.add(parts[i]);
		return output;
	}

	private List<Object> parseArgument(Node node, TaskParent taskParent, Integer idx) {
		NodeList children = node.getChildNodes();
		List<Object> inputArguments = new ArrayList<Object>();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(CB_ARG_ELT)) {
					String type = getAttributeValue(child, TYPE_ATTR);
					String ref = getAttributeValue(child, REF_ATTR);
					if (!ref.equalsIgnoreCase(TRUE_VALUE)) {
						switch (type) {
						case "Integer":
							inputArguments.add(Integer.valueOf(child.getTextContent()));
							break;
						case "String":
							inputArguments.add(String.valueOf(child.getTextContent()));
							break;
						case "ArrayList":
							inputArguments.add(string2ArrayList(String.valueOf(child.getTextContent()), ","));
							break;
						}
					} else {
						if (child.getChildNodes().getLength() > 1) {
							NodeList subchildren = child.getChildNodes();
							if (subchildren != null) {
								for (int j = 0; j < subchildren.getLength(); j++) {
									Node subchild = subchildren.item(j);
									String subnodeName = subchild.getNodeName();
									if (subnodeName.equalsIgnoreCase(CB_LINK_ELT)) {
										inputArguments.add((parseCallBackLink(subchild, idx)));
									} else if (subnodeName.equalsIgnoreCase(GLOBAL_VAR_LINK_ELT)) {
										GlobalVariable gvar = parseVarLink(subchild);
										if (gvar != null) {
											inputArguments.add(gvar.getValue());
										} else {
											System.out.println(
													"parse argument: there is a problem when parsing global variable, please check meta model !");
											System.exit(0);
										}
									} else if (subnodeName.equalsIgnoreCase(WDT_GROUP_ELT)) {
										String widgetGroupName = parseWidgetGroupName(subchild, taskParent, idx);
										String wigetGroupRef = widgetGroupName + "#" + frameNameList.get(idx);
										inputArguments.add(widgetGroup.get(wigetGroupRef));
									}
								}
							}
						}
					}
				}
			}
		}
		return inputArguments;
	}

	private GlobalVariable parseVarLink(Node node) {
		// private GlobalVariable parseVarLink(Node node, Integer idx) {
		// private GlobalVariable parseVarLink(Node node, String dataType,
		// Integer idx) {
		GlobalVariable gvar = null;
		// String type = getAttributeValue(node, TYPE_ATTR);
		String ref = getAttributeValue(node, STYLE_ATTR);
		String id = node.getTextContent();
		if (ref.equalsIgnoreCase("local"))
			if (globalVarMap.containsKey(id))
				gvar = globalVarMap.get(id);
			else{
				//TODO:
			}

		return gvar;
	}

	private VisualScanElement parseVisualScan(Node node, TaskParent taskParent, TaskElement task, Integer idx,
			String currentKey) {

		VisualScanElement vTask = new VisualScanElement(task);
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(TARGET_INDEX_ELT)) {
					vTask = (VisualScanElement) parseTargetIndex(child, taskParent, vTask, idx);
				} else if (nodeName.equalsIgnoreCase("scan_path")) {
					vTask.setScanPath(parseScanPath(child, taskParent, idx));
				}
			}
		}
		return vTask;
	}

	// scan path is a array of integer, so here we have to cast the data type
	@SuppressWarnings("unchecked")
	private ArrayList<Integer> parseScanPath(Node node, TaskParent taskParent, Integer idx) {
		NodeList children = node.getChildNodes();
		ArrayList<Integer> scanPath = new ArrayList<Integer>();
		ArrayList<Double> tmp = null;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(CALLBACK_ELT)) {
					CallBackElement cb = parseCallBack(child, taskParent, idx);

					// The outcome generated by java-script is always double,
					// here we need to
					// cast the results to be array-list of double
					tmp = (ArrayList<Double>) cb.getResult();
				}
			}
		}
		// here we convert the data type to be array-list of integer
		if (tmp != null) {
			for (int i = 0; i < tmp.size(); i++) {
				scanPath.add(tmp.get(i).intValue());
			}
		}
		return scanPath;
	}

	private void parseGlobalCallBack(Node node, TaskParent taskParent) {
		NodeList children = node.getChildNodes();
		CallBackElement cb = null;
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(CALLBACK_ELT)) {
					// String type = getAttributeValue(subNode, TYPE_ATTR);
					cb = parseCallBack(child, taskParent, null);
					String id = cb.getID();
					if (cb.containsID()){
						globalCallBackMap.put(id, cb);
						// added 07-01-2020
						Variable var = new Variable(id, cb.getDataType(), cb.getResult());
						GlobalVariable gvar = new GlobalVariable(var);
						globalVarMap.put(id, gvar);
					}
				}
			}
		}
	}

	private Integer parseSession(Node child, TaskParent taskParent) {
		Integer session = null;
		if (child.getChildNodes().getLength() > 1) {
			NodeList subchildren = child.getChildNodes();
			if (subchildren != null) {
				for (int j = 0; j < subchildren.getLength(); j++) {
					Node subchild = subchildren.item(j);
					String subnodeName = subchild.getNodeName();
					if (subnodeName.equalsIgnoreCase(GLOBAL_VAR_LINK_ELT)) {
						// The number of session must to be a positive integers
						GlobalVariable gvar = parseVarLink(subchild);
						// GlobalVariable gvar = parseVarLink(subchild,
						// INTEGER_ATTR, null);
						if (gvar != null) {
							// TODO:
							String type = gvar.getValue().getClass().getSimpleName();
							if (type.equalsIgnoreCase("String")){
								session = Integer.valueOf(gvar.getValue().toString());
							}
							if (type.equalsIgnoreCase("Integer")){
								Integer tmpDouble = (Integer) gvar.getValue();
								Integer tempInt = tmpDouble.intValue();
								session = Integer.valueOf(tempInt.toString());
							}
							if (type.equalsIgnoreCase("Double")){
								Double tmpDouble = (Double) gvar.getValue();
								Integer tempInt = tmpDouble.intValue();
								session = Integer.valueOf(tempInt.toString());
							}
									//Integer tempInt = (Integer) gvar.getValue();

							// session = (Integer)gvar.getValue();
						} else {
							System.out.println(
									"parse session 1: there is a problem when parsing global variable, please check meta model !");
							System.exit(0);
						}
					}
				}
			}
		} else {
			session = Integer.parseInt(child.getTextContent());
		}
		return session;
	}

	// @Haiyue
	private void parseWidgetGroup(Node node, String name, String fname, TaskParent taskParent) {
		String key = name + "#" + fname;
		ArrayList<String> widgetNameList = new ArrayList<String>();
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(WIDGET_ELT)) {
					widgetNameList.add(child.getTextContent());
				}
			}
			widgetGroup.put(key, widgetNameList);
		}
	}

	private Object dynamicInvokeFunction(Invocable inv, String function, List<Object> input)
			throws NoSuchMethodException, ScriptException {
		Object obj = null;
		int size = input.size();
		switch (size) {
		case 0:
			obj = inv.invokeFunction(function);
			break;
		case 1:
			obj = inv.invokeFunction(function, input.get(0));
			break;
		case 2:
			obj = inv.invokeFunction(function, input.get(0), input.get(1));
			break;
		case 3:
			obj = inv.invokeFunction(function, input.get(0), input.get(1), input.get(2));
			break;
		case 4:
			obj = inv.invokeFunction(function, input.get(0), input.get(1), input.get(2), input.get(3));
			break;
		case 5:
			obj = inv.invokeFunction(function, input.get(0), input.get(1), input.get(2), input.get(3), input.get(4));
			break;
		}
		return obj;
	}

	// -----------------------------------------------------------------------//
	// @Haiyue
	public HashMap<String, IWidget> getDynamicWidgetList() {
		return dynamicWidgetList;
	}

	// -----------------------------------------------------------------------//
	// @Haiyue
	public HashMap<String, ArrayList<String>> getWidgetGroup() {
		return widgetGroup;
	}
	// -----------------------------------------------------------------------//

	// @Haiyue
	public ArrayList<String> getTaskOrderList() {
		return taskOrderList;
	}

	// -----------------------------------------------------------------------//
	// @Haiyue
	public HashMap<String, TaskElement> getTaskMap() {
		return taskMap;
	}

	// -----------------------------------------------------------------------//
	// @Haiyue
	public String getStartingFrame() {
		return startingFrame;
	}

	// -----------------------------------------------------------------------//
	// @Haiyue
	public String getUserHand() {
		return userHand;
	}

	// -----------------------------------------------------------------------//
	// -----------------------------------------------------------------------//
	// @Haiyue
	public String getDemonstrationName() {
		return demonstrationName;
	}

	// -----------------------------------------------------------------------//
	// @Haiyue
	public HashMap<String, CallBackElement> getLocalCallBack() {
		return localCallBackMap;
	}

	// -----------------------------------------------------------------------//
	// @Haiyue
	public HashMap<String, CallBackElement> getGlobalCallBack() {
		return globalCallBackMap;
	}

	// -----------------------------------------------------------------------//
	// @Haiyue
	public HashMap<String, ArrayList<TaskElement>> getParameterMap() {
		return frameParameterMap;
	}
	// -----------------------------------------------------------------------//

	public ArrayList<String> getFrameNameList() {
		return frameNameList;
	}

	/**
	 * Imports a design
	 * 
	 * @param node
	 */
	private void parseDesign(Node node, TaskParent taskParent) throws IOException {
		NodeList children = node.getChildNodes();

		if (children != null) {
			Design design = designLoader.createObject();
			List<Demonstration> demonstrations = new ArrayList<Demonstration>();

			designLoader.set(design, Design.nameVAR, getAttributeValue(node, NAME_ATTR));
			addAttributes(design, node);

			@SuppressWarnings("unchecked")
			Collection<DeviceType> deviceTypes = (Collection<DeviceType>) designLoader.createCollection(design,
					Design.deviceTypesVAR, 1);
			Collection<?> frames = designLoader.createCollection(design, Design.framesVAR, 1);

			ObjectLoader.IAggregateLoader deviceTypesLoader = designLoader.getLoader(Design.deviceTypesVAR);

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();

				if (nodeName.equalsIgnoreCase(DEVICE_ELT)) {
					String device = getElementText(child);

					if (device.equalsIgnoreCase(KEYBOARD_DEVICE)) {
						deviceTypesLoader.addToCollection(objLoader, deviceTypes, DeviceType.Keyboard);
					} else if (device.equalsIgnoreCase(MOUSE_DEVICE)) {
						deviceTypesLoader.addToCollection(objLoader, deviceTypes, DeviceType.Mouse);
					} else if (device.equalsIgnoreCase(TOUCHSCREEN_DEVICE)) {
						deviceTypesLoader.addToCollection(objLoader, deviceTypes, DeviceType.Touchscreen);
					} else if (device.equalsIgnoreCase(MICROPHONE_DEVICE)) {
						deviceTypesLoader.addToCollection(objLoader, deviceTypes, DeviceType.Voice);
					} else if (device.equalsIgnoreCase(DISPLAY_DEVICE)) {
						deviceTypesLoader.addToCollection(objLoader, deviceTypes, DeviceType.Display);
					} else if (device.equalsIgnoreCase(SPEAKER_DEVICE)) {
						deviceTypesLoader.addToCollection(objLoader, deviceTypes, DeviceType.Speaker);
					} else {
						// "unknown device"
						failedObjectErrors.add("Unknown Design device: " + device);
					}
				} else if (nodeName.equalsIgnoreCase(FRAME_SETTING_ELT)) {
					parseFrameSetting(design, taskParent, child);
				} else if (nodeName.equalsIgnoreCase(FRAME_ELT)) {
					parseFrame(design, child);
					// No need to add frame to the design here; done already!
				} else if (nodeName.equalsIgnoreCase(HAND_ELT)) {
					userHand = String.valueOf(child.getTextContent());
				}
			}
			if ((design.getName() == null) || (deviceTypes.size() == 0) || (frames.size() == 0)) {
				throw new ImportFailedException("No design found");
			}

			designs.put(design, demonstrations);
		}
	}

	private void parseFrameSetting(Design design, TaskParent taskParent, Node node) throws IOException {
		String type = getAttributeValue(node, TYPE_ATTR);
		if (type.equalsIgnoreCase(DYNAMIC_ATTR)) {
			parseDynamicFrames(design, taskParent, node);
		} else if (type.equalsIgnoreCase(STATIC_ATTR)) {
			parseStaticFrames(design, taskParent, node);
		}
	}

	private void parseStaticFrames(Design design, TaskParent taskParent, Node node) throws IOException {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(FRAME_LIST_ELT)) {
					parseFrameList(design, child);
				}
			}
		}
	}

	private void parseDynamicFrames(Design design, TaskParent taskParent, Node node) throws IOException {
		int session = 0;
		int idx = 0;
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(SESSION_ELT)) {
					session = parseSession(child, taskParent);
				} else if (nodeName.equalsIgnoreCase(FRAME_LIST_ELT)) {
					if (session > 0) {
						for (idx = 0; idx < session; idx++) {
							parseFrameList(design, child, idx, session);
						}
					}
				}
			}
		}
	}

	private void parseFrameList(Design design, Node node) throws IOException {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(FRAME_ELT)) {
					parseFrame(design, child);
				}
			}
		}
	}

	private void parseFrameList(Design design, Node node, int idx, int session) throws IOException {
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(FRAME_ELT)) {
					parseFrame_dynamic(design, child, idx, session);
					// parseTask(child, taskParent, idx);
					// taskOrderList.add(parseTask(child, taskParent, idx));
				}
			}
		}
	}

	private Frame parseFrame_dynamic(Design design, Node node, int idx, int session) throws IOException {
		NodeList children = node.getChildNodes();

		if (children != null) {
			String frameName = getAttributeValue(node, NAME_ATTR);
			frameName = frameName + idx;
			frameNameList.add(frameName);
			if ((frameName == null) || frameName.equals("")) {
				failedObjectErrors.add("Cannot create a frame with an empty name.");
				return null;
			}

			// This adds the created frame to the design
			Frame frame = getFrame(design, frameName);
			addAttributes(frame, node);

			Frame.setFrameDevices(frame, design.getDeviceTypes());

			frame.getInputDevice(DeviceType.Keyboard);
			frame.getInputDevice(DeviceType.Voice);

			// Some widgets have parents; so as not to require that
			// all widgets of a frame occur in a particular order, we must
			// resolve the parent names after all widgets have been parsed.
			// Maps the child widget to the name of its parent
			Map<ChildWidget, String> pendingParentSets = new LinkedHashMap<ChildWidget, String>();

			// Some attributes refer to widget names; must resolve these
			// after all widgets have been created.
			// Currently, the only such attribute that applies to widgets
			// is WidgetAttributes.SELECTION_ATTR
			// Maps the attributed object to the widget name that is
			// the value of the WidgetAttributes.SELECTION_ATTR attribute
			Map<IAttributed, String> pendingAttrSets = new HashMap<IAttributed, String>();

			// Some element groups may be referenced as members of other
			// groups before being defined; this map will hold them
			Map<String, FrameElementGroup> pendingGrps = new HashMap<String, FrameElementGroup>();

			// Some remote labels may not be defined before they're referenced
			// so keep track of those cases. Maps the owner object to
			// the name of the remote label
			Map<FrameElement, String> pendingRemoteLabels = new HashMap<FrameElement, String>();

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();

				if (nodeName.equalsIgnoreCase(BKG_IMAGE_PATH_ELT)) {
					String backgroundImagePath = getElementText(child);
					byte[] image = loadImage(backgroundImagePath);

					if (image != null) {
						frameLoader.set(frame, Frame.backgroundVAR, image);
						frame.setAttribute(WidgetAttributes.IMAGE_PATH_ATTR, backgroundImagePath);
					}
				} else if (nodeName.equalsIgnoreCase(BKG_IMAGE_DATA_ELT)) {
					String backgroundImageData = getElementText(child);
					String imageName = getAttributeValue(child, NAME_ATTR);
					byte[] image = null;

					if (backgroundImageData != "") {
						image = Base64.decode(backgroundImageData);

						if ((imageName != null) && !imageName.equals("")) {
							cachedImages.put(imageName, image);
						}
					} else if ((imageName != null) && !imageName.equals("")) {
						// If imageName specified but there is no data, trust
						// and
						// try to find the last image data associated with that
						// name in the cache.
						image = cachedImages.get(imageName);
					}

					if (image != null) {
						frameLoader.set(frame, Frame.backgroundVAR, image);

						if ((imageName != null) && !imageName.equals("")) {
							frame.setAttribute(WidgetAttributes.IMAGE_PATH_ATTR, imageName);
						}
					}
				} else if (nodeName.equalsIgnoreCase(ORIGIN_ELT)) {
					double x = Double.parseDouble(getAttributeValue(child, X_ATTR));
					double y = Double.parseDouble(getAttributeValue(child, Y_ATTR));
					DoublePoint origin = new DoublePoint(x, y);

					frameLoader.set(frame, Frame.originVAR, origin);
				} else if (nodeName.equalsIgnoreCase(SPEAKER_TEXT_ELT)) {
					frameLoader.set(frame, Frame.speakerTextVAR, getElementText(child));
				} else if (nodeName.equalsIgnoreCase(LISTEN_TIME_SECS_ELT)) {
					frameLoader.set(frame, Frame.listenTimeVAR, Double.parseDouble(getElementText(child)));
				} else if (nodeName.equalsIgnoreCase(WIDGET_ELT)) {
					IWidget w = parseWidget_dynamic(design, frame, pendingParentSets, pendingAttrSets,
							pendingRemoteLabels, child, idx, session);

					if (w != null) {
						frame.addWidget(w);
					} else {
						w = new Widget(null, WidgetType.Noninteractive);
						GraphicsUtil
								.getImageFromResource("edu/cmu/cs/hcii/cogtool/resources/warning.jpg");
						// w.setImage(wImage.getBytes());
						frame.addWidget(w);
					}
				} else if (nodeName.equalsIgnoreCase(ELTGROUP_ELT)) {
					FrameElementGroup g = parseEltGroup(design, frame, pendingGrps, child);

					if (g != null) {
						String eltGrpName = g.getName();

						pendingGrps.remove(eltGrpName);

						eltGrpName = NamedObjectUtil.makeNameUnique(eltGrpName, frame.getEltGroups());
						g.setName(eltGrpName);
						frame.addEltGroup(g);
					}
				}
				// HERE IS TEMPORARILY DISABLED
				/*
				 * else if (nodeName.equalsIgnoreCase(KEYBOARD_TRANSITIONS_ELT))
				 * { if (keyboardDevice != null && idx<session) {
				 * parseTransitions_dynamic(design, keyboardDevice, child, idx);
				 * } else { failedObjectErrors.add(
				 * "Keyboard transitions require that Design have a Keyboard device"
				 * ); } } else if
				 * (nodeName.equalsIgnoreCase(VOICE_TRANSITIONS_ELT)) { if
				 * (voiceDevice != null && idx<session) {
				 * parseTransitions_dynamic(design, voiceDevice, child, idx); }
				 * else { failedObjectErrors.add(
				 * "Voice transitions require that Design have a Voice device");
				 * } }
				 */
			}

			if (frame.getName() != null) {
				// Handle any forward references for remote labels
				Iterator<Map.Entry<FrameElement, String>> labelRefs = pendingRemoteLabels.entrySet().iterator();

				while (labelRefs.hasNext()) {
					Map.Entry<FrameElement, String> labelRef = labelRefs.next();

					setRemoteLabel(frame, labelRef.getValue(), labelRef.getKey(), null);
				}

				// If any "pending" element groups still exist, then there
				// is an error -- an element group that didn't exist!
				Iterator<FrameElementGroup> missingGrps = pendingGrps.values().iterator();
				StringBuilder errorMsg = new StringBuilder();

				while (missingGrps.hasNext()) {
					FrameElementGroup missingGrp = missingGrps.next();

					errorMsg.append("Missing widget or group, named: ");
					errorMsg.append(missingGrp.getName());
					errorMsg.append(" as member of the following groups: ");

					Iterator<FrameElementGroup> inGrps = missingGrp.getEltGroups().iterator();
					String separator = "";

					while (inGrps.hasNext()) {
						errorMsg.append(separator + inGrps.next().getName());
						separator = ", ";
					}

					failedObjectErrors.add(errorMsg.toString());
					errorMsg.delete(0, errorMsg.length());
				}

				Iterator<Map.Entry<ChildWidget, String>> childToParentSet = pendingParentSets.entrySet().iterator();

				// Now that all widgets have been created, set the parent-child
				// relationships
				while (childToParentSet.hasNext()) {
					Map.Entry<ChildWidget, String> childToParent = childToParentSet.next();
					String parentName = childToParent.getValue();

					if (!"".equals(parentName)) {
						ChildWidget child = childToParent.getKey();
						AParentWidget parent = (AParentWidget) frame.getWidget(parentName);

						parent.addItem(child);
						child.setParent(parent);
					}
				}

				Iterator<Map.Entry<IAttributed, String>> selnAttrToSet = pendingAttrSets.entrySet().iterator();

				// Now that all widgets have been created, set the attributes
				// that used widget names as values.
				while (selnAttrToSet.hasNext()) {
					Map.Entry<IAttributed, String> selnAttr = selnAttrToSet.next();
					String widgetName = selnAttr.getValue();
					IWidget attrValue = "".equals(widgetName) ? null : frame.getWidget(widgetName);

					// At the moment, all occurrences that use pendingAttrSets
					// are instances of PullDownHeader for
					// WidgetAttributes.SELECTION_ATTR
					selnAttr.getKey().setAttribute(WidgetAttributes.SELECTION_ATTR, attrValue);
				}

				return frame;
			}
		}

		return null;
	} // parseFrame

	private byte[] loadImage(String relativePath) throws IOException {
		byte[] loadedImageData = imageRegistry.get(relativePath);

		if (loadedImageData == null) {
			loadedImageData = GraphicsUtil.loadImageFromFile(directoryPath + relativePath);

			if (loadedImageData != null) {
				imageRegistry.put(relativePath, loadedImageData);
			}
		}

		return loadedImageData;
	}

	/**
	 * Imports a frame
	 * 
	 * @param node
	 */
	private Frame parseFrame(Design design, Node node) throws IOException {
		NodeList children = node.getChildNodes();

		if (children != null) {
			String frameName = getAttributeValue(node, NAME_ATTR);

			if ((frameName == null) || frameName.equals("")) {
				failedObjectErrors.add("Cannot create a frame with an empty name.");
				return null;
			}

			frameNameList.add(frameName);
			// This adds the created frame to the design
			Frame frame = getFrame(design, frameName);
			addAttributes(frame, node);

			Frame.setFrameDevices(frame, design.getDeviceTypes());

			TransitionSource keyboardDevice = frame.getInputDevice(DeviceType.Keyboard);
			TransitionSource voiceDevice = frame.getInputDevice(DeviceType.Voice);

			// Some widgets have parents; so as not to require that
			// all widgets of a frame occur in a particular order, we must
			// resolve the parent names after all widgets have been parsed.
			// Maps the child widget to the name of its parent
			Map<ChildWidget, String> pendingParentSets = new LinkedHashMap<ChildWidget, String>();

			// Some attributes refer to widget names; must resolve these
			// after all widgets have been created.
			// Currently, the only such attribute that applies to widgets
			// is WidgetAttributes.SELECTION_ATTR
			// Maps the attributed object to the widget name that is
			// the value of the WidgetAttributes.SELECTION_ATTR attribute
			Map<IAttributed, String> pendingAttrSets = new HashMap<IAttributed, String>();

			// Some element groups may be referenced as members of other
			// groups before being defined; this map will hold them
			Map<String, FrameElementGroup> pendingGrps = new HashMap<String, FrameElementGroup>();

			// Some remote labels may not be defined before they're referenced
			// so keep track of those cases. Maps the owner object to
			// the name of the remote label
			Map<FrameElement, String> pendingRemoteLabels = new HashMap<FrameElement, String>();

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();

				if (nodeName.equalsIgnoreCase(BKG_IMAGE_PATH_ELT)) {
					String backgroundImagePath = getElementText(child);
					byte[] image = loadImage(backgroundImagePath);

					if (image != null) {
						frameLoader.set(frame, Frame.backgroundVAR, image);
						frame.setAttribute(WidgetAttributes.IMAGE_PATH_ATTR, backgroundImagePath);
					}
				} else if (nodeName.equalsIgnoreCase(BKG_IMAGE_DATA_ELT)) {
					String backgroundImageData = getElementText(child);
					String imageName = getAttributeValue(child, NAME_ATTR);
					byte[] image = null;

					if (backgroundImageData != "") {
						image = Base64.decode(backgroundImageData);

						if ((imageName != null) && !imageName.equals("")) {
							cachedImages.put(imageName, image);
						}
					} else if ((imageName != null) && !imageName.equals("")) {
						// If imageName specified but there is no data, trust
						// and
						// try to find the last image data associated with that
						// name in the cache.
						image = cachedImages.get(imageName);
					}

					if (image != null) {
						frameLoader.set(frame, Frame.backgroundVAR, image);

						if ((imageName != null) && !imageName.equals("")) {
							frame.setAttribute(WidgetAttributes.IMAGE_PATH_ATTR, imageName);
						}
					}
				} else if (nodeName.equalsIgnoreCase(ORIGIN_ELT)) {
					double x = Double.parseDouble(getAttributeValue(child, X_ATTR));
					double y = Double.parseDouble(getAttributeValue(child, Y_ATTR));
					DoublePoint origin = new DoublePoint(x, y);

					frameLoader.set(frame, Frame.originVAR, origin);
				} else if (nodeName.equalsIgnoreCase(SPEAKER_TEXT_ELT)) {
					frameLoader.set(frame, Frame.speakerTextVAR, getElementText(child));
				} else if (nodeName.equalsIgnoreCase(LISTEN_TIME_SECS_ELT)) {
					frameLoader.set(frame, Frame.listenTimeVAR, Double.parseDouble(getElementText(child)));
				} else if (nodeName.equalsIgnoreCase(WIDGET_ELT)) {
					IWidget w = parseWidget(design, frame, pendingParentSets, pendingAttrSets, pendingRemoteLabels,
							child);

					if (w != null) {
						frame.addWidget(w);
					} else {
						w = new Widget(null, WidgetType.Noninteractive);
						GraphicsUtil
								.getImageFromResource("edu/cmu/cs/hcii/cogtool/resources/warning.jpg");
						// w.setImage(wImage.getBytes());
						frame.addWidget(w);
					}
				} else if (nodeName.equalsIgnoreCase(ELTGROUP_ELT)) {
					FrameElementGroup g = parseEltGroup(design, frame, pendingGrps, child);

					if (g != null) {
						String eltGrpName = g.getName();

						pendingGrps.remove(eltGrpName);

						eltGrpName = NamedObjectUtil.makeNameUnique(eltGrpName, frame.getEltGroups());
						g.setName(eltGrpName);
						frame.addEltGroup(g);
					}
				} else if (nodeName.equalsIgnoreCase(KEYBOARD_TRANSITIONS_ELT)) {
					if (keyboardDevice != null) {
						parseTransitions(design, keyboardDevice, child);
					} else {
						failedObjectErrors.add("Keyboard transitions require that Design have a Keyboard device");
					}
				} else if (nodeName.equalsIgnoreCase(VOICE_TRANSITIONS_ELT)) {
					if (voiceDevice != null) {
						parseTransitions(design, voiceDevice, child);
					} else {
						failedObjectErrors.add("Voice transitions require that Design have a Voice device");
					}
				}
			}

			if (frame.getName() != null) {
				// Handle any forward references for remote labels
				Iterator<Map.Entry<FrameElement, String>> labelRefs = pendingRemoteLabels.entrySet().iterator();

				while (labelRefs.hasNext()) {
					Map.Entry<FrameElement, String> labelRef = labelRefs.next();

					setRemoteLabel(frame, labelRef.getValue(), labelRef.getKey(), null);
				}

				// If any "pending" element groups still exist, then there
				// is an error -- an element group that didn't exist!
				Iterator<FrameElementGroup> missingGrps = pendingGrps.values().iterator();
				StringBuilder errorMsg = new StringBuilder();

				while (missingGrps.hasNext()) {
					FrameElementGroup missingGrp = missingGrps.next();

					errorMsg.append("Missing widget or group, named: ");
					errorMsg.append(missingGrp.getName());
					errorMsg.append(" as member of the following groups: ");

					Iterator<FrameElementGroup> inGrps = missingGrp.getEltGroups().iterator();
					String separator = "";

					while (inGrps.hasNext()) {
						errorMsg.append(separator + inGrps.next().getName());
						separator = ", ";
					}

					failedObjectErrors.add(errorMsg.toString());
					errorMsg.delete(0, errorMsg.length());
				}

				Iterator<Map.Entry<ChildWidget, String>> childToParentSet = pendingParentSets.entrySet().iterator();

				// Now that all widgets have been created, set the parent-child
				// relationships
				while (childToParentSet.hasNext()) {
					Map.Entry<ChildWidget, String> childToParent = childToParentSet.next();
					String parentName = childToParent.getValue();

					if (!"".equals(parentName)) {
						ChildWidget child = childToParent.getKey();
						AParentWidget parent = (AParentWidget) frame.getWidget(parentName);

						parent.addItem(child);
						child.setParent(parent);
					}
				}

				Iterator<Map.Entry<IAttributed, String>> selnAttrToSet = pendingAttrSets.entrySet().iterator();

				// Now that all widgets have been created, set the attributes
				// that used widget names as values.
				while (selnAttrToSet.hasNext()) {
					Map.Entry<IAttributed, String> selnAttr = selnAttrToSet.next();
					String widgetName = selnAttr.getValue();
					IWidget attrValue = "".equals(widgetName) ? null : frame.getWidget(widgetName);

					// At the moment, all occurrences that use pendingAttrSets
					// are instances of PullDownHeader for
					// WidgetAttributes.SELECTION_ATTR
					selnAttr.getKey().setAttribute(WidgetAttributes.SELECTION_ATTR, attrValue);
				}

				return frame;
			}
		}

		return null;
	} // parseFrame

	/**
	 * Helper function used by parseFrame. This method is used to return the
	 * corresponding Frame object for a given frame name
	 *
	 * @ param frameName the specified frame name
	 */
	private Frame getFrame(Design design, String frameName) {
		if ((frameName != null) && !frameName.equals("") && !frameName.equals(FINAL_FRAME_NAME)) {
			// Fail-fast -- right now, we have only one implementation of
			// Frame, so this cannot fail.
			Frame frame = design.getFrame(frameName);

			if (frame == null) {
				frame = frameLoader.createObject();
				frame.setName(frameName);
				design.addFrame(frame);
			}

			return frame;
		}

		return null;
	}

	private WidgetType getWidgetType(String widgetType) {
		if ((widgetType == null) || // TODO: note error, return null on this
									// case?
				widgetType.equalsIgnoreCase(BUTTON_WIDGETTYPE)) {
			return WidgetType.Button;
		}
		if (widgetType.equalsIgnoreCase(LINK_WIDGETTYPE)) {
			return WidgetType.Link;
		}
		if (widgetType.equalsIgnoreCase(CHECKBOX_WIDGETTYPE)) {
			return WidgetType.Check;
		}
		if (widgetType.equalsIgnoreCase(RADIO_WIDGETTYPE)) {
			return WidgetType.Radio;
		}
		if (widgetType.equalsIgnoreCase(TEXTBOX_WIDGETTYPE)) {
			return WidgetType.TextBox;
		}
		if (widgetType.equalsIgnoreCase(TEXT_WIDGETTYPE)) {
			return WidgetType.Text;
		}
		if (widgetType.equalsIgnoreCase(PULLDOWNLIST_WIDGETTYPE)) {
			return WidgetType.PullDownList;
		}
		if (widgetType.equalsIgnoreCase(PULLDOWNITEM_WIDGETTYPE)) {
			return WidgetType.PullDownItem;
		}
		if (widgetType.equalsIgnoreCase(LISTBOXITEM_WIDGETTYPE)) {
			return WidgetType.ListBoxItem;
		}
		if (widgetType.equalsIgnoreCase(CONTEXTMENU_WIDGETTYPE)) {
			return WidgetType.ContextMenu;
		}
		if (widgetType.equalsIgnoreCase(MENUHEADER_WIDGETTYPE)) {
			return WidgetType.Menu;
		}
		if (widgetType.equalsIgnoreCase(SUBMENU_WIDGETTYPE)) {
			return WidgetType.Submenu;
		}
		if (widgetType.equalsIgnoreCase(MENUITEM_WIDGETTYPE)) {
			return WidgetType.MenuItem;
		}
		if (widgetType.equalsIgnoreCase(GRAFFITI_WIDGETTYPE)) {
			return WidgetType.Graffiti;
		}
		if (widgetType.equalsIgnoreCase(NONINTERACTIVE_WIDGETTYPE)) {
			return WidgetType.Noninteractive;
		}

		failedObjectErrors.add("Unknown widget type: " + widgetType);
		return null;
	}

	/**
	 * Imports a widget
	 * 
	 * @param node
	 */
	private IWidget parseWidget(Design design, Frame frame, Map<ChildWidget, String> pendingParentSets,
			Map<IAttributed, String> pendingAttrSets, Map<FrameElement, String> pendingRemoteLabels, Node node)
			throws IOException {
		NodeList children = node.getChildNodes();

		if (children != null) {
			String isStandardValue = getAttributeValue(node, IS_STANDARD_ATTR);
			boolean isStandard;

			if (isStandardValue != null) {
				Boolean isStandardAttrValue = (Boolean) VALUE_REGISTRY.get(isStandardValue);

				if (isStandardAttrValue != null) {
					isStandard = isStandardAttrValue.booleanValue();
				} else {
					isStandard = false;
				}
			} else if (CURRENT_VERSION.equals(dtdVersion)) {
				isStandard = false;
			} else {
				isStandard = true;
			}

			WidgetType widgetType = getWidgetType(getAttributeValue(node, TYPE_ATTR));

			Widget widget = null;

			if (widgetType != null) {
				if (isStandard) {
					if (widgetType == WidgetType.ContextMenu) {
						// Using this constructor sets up childItems and
						// childLocation to the only value currently in use
						widget = new ContextMenu(ContextMenu.FOR_DUPLICATION);
					} else if (widgetType == WidgetType.Menu) {
						// Using this constructor sets up childItems and
						// childLocation to the only value currently in use
						widget = new MenuHeader(MenuHeader.FOR_DUPLICATION);

						// Must also find its "anonymous" parentGroup
						String grpName = getAttributeValue(node, GROUP_ATTR);

						if ((grpName != null) && !"".equals(grpName)) {
							SimpleWidgetGroup menuHdrGroup = groupRegistry.get(grpName);

							if (menuHdrGroup == null) {
								menuHdrGroup = new SimpleWidgetGroup(SimpleWidgetGroup.HORIZONTAL);
								menuHdrGroup.setName(grpName);
								groupRegistry.put(grpName, menuHdrGroup);
							}

							menuHdrGroup.add(widget);
						}
					} else if ((widgetType == WidgetType.Submenu) || (widgetType == WidgetType.MenuItem)) {
						// Must also assign its parent, which must come earlier
						String parentName = getAttributeValue(node, PARENT_ATTR);

						if (parentName != null) {
							MenuItem menuItem = new MenuItem();

							// If a submenu, set up childItems and childLocation
							menuItem.setSubmenu(widgetType == WidgetType.Submenu);

							pendingParentSets.put(menuItem, parentName);

							widget = menuItem;
						} else {
							widget = widgetLoader.createObject();
						}
					} else if (widgetType == WidgetType.PullDownList) {
						// Using this constructor sets up childItems and
						// childLocation to the only value currently in use
						widget = new PullDownHeader(PullDownHeader.FOR_DUPLICATION);
					} else if (widgetType == WidgetType.PullDownItem) {
						// Must assign its parent, which must come earlier
						String parentName = getAttributeValue(node, PARENT_ATTR);

						if (parentName != null) {
							PullDownItem pullDownItem = new PullDownItem();

							pendingParentSets.put(pullDownItem, parentName);

							widget = pullDownItem;
						} else {
							widget = widgetLoader.createObject();
						}
					} else if (widgetType == WidgetType.ListBoxItem) {
						widget = new ListItem();

						// Must also find its "anonymous" parentGroup
						String grpName = getAttributeValue(node, GROUP_ATTR);

						if ((grpName != null) && !"".equals(grpName)) {
							SimpleWidgetGroup listItemGroup = groupRegistry.get(grpName);

							if (listItemGroup == null) {
								listItemGroup = new SimpleWidgetGroup(SimpleWidgetGroup.VERTICAL);
								listItemGroup.setName(grpName);
								groupRegistry.put(grpName, listItemGroup);
							}

							listItemGroup.add(widget);
						}
					} else if ((widgetType == WidgetType.Radio) || (widgetType == WidgetType.Check)) {
						GridButton gridWidget;

						if (widgetType == WidgetType.Radio) {
							gridWidget = new RadioButton();
						} else {
							gridWidget = new CheckBox();
						}

						// Fetch the horizontal/vertical spacing attributes
						double horizontal = Double.parseDouble(getAttributeValue(node, X_ATTR));
						double vertical = Double.parseDouble(getAttributeValue(node, Y_ATTR));
						gridWidget.setHorizSpace(horizontal);
						gridWidget.setVertSpace(vertical);

						// Must also find its "anonymous" parentGroup
						String grpName = getAttributeValue(node, GROUP_ATTR);

						if ((grpName != null) && !"".equals(grpName)) {
							SimpleWidgetGroup gridWidgetGrp = groupRegistry.get(grpName);

							if (gridWidgetGrp == null) {
								if (widgetType == WidgetType.Radio) {
									gridWidgetGrp = new RadioButtonGroup();
								} else {
									gridWidgetGrp = new GridButtonGroup();
								}

								gridWidgetGrp.setName(grpName);
								groupRegistry.put(grpName, gridWidgetGrp);
							}

							gridWidgetGrp.add(gridWidget);
						}

						widget = gridWidget;
					} else {
						widget = widgetLoader.createObject();
					}
				} else {
					widget = widgetLoader.createObject();
				}

				widgetLoader.set(widget, Widget.widgetTypeVAR, widgetType);
			} else {
				widget = widgetLoader.createObject();
			}

			String widgetName = getAttributeValue(node, NAME_ATTR);

			widget.setName(widgetName);

			String remoteLabelAttr = getAttributeValue(node, REMOTE_LABEL_ATTR);

			if ((remoteLabelAttr != null) && !remoteLabelAttr.equals("")) {
				FrameElement remoteLabelOwner = widget.getRemoteLabelOwner();

				if (remoteLabelOwner == null) {
					failedObjectErrors
							.add("Attempting to set a remote label on the wrong widget type for: " + widgetName);
				} else {
					IWidget existingLabel = (IWidget) remoteLabelOwner.getAttribute(WidgetAttributes.REMOTE_LABEL_ATTR);

					if (existingLabel == null) {
						setRemoteLabel(frame, remoteLabelAttr, remoteLabelOwner, pendingRemoteLabels);
					} else if (!remoteLabelAttr.equals(existingLabel.getName())) {
						failedObjectErrors.add("Attempting to set a second remote label for the widget: " + widgetName);
					}
				}
			}

			// For the case that the widget is a RadioButton that
			// has an is-selected attribute, the setting of that
			// attribute in this call will, per the override of setAttribute()
			// in RadioButton, set the proper selected-widget attribute
			// on the containing widget group.
			addAttributes(widget, node, pendingAttrSets);

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();

				if (nodeName.equalsIgnoreCase(DISPLAY_LABEL_ELT)) {
					widgetLoader.set(widget, Widget.titleVAR, getElementText(child));
				} else if (nodeName.equalsIgnoreCase(AUX_TEXT_ELT)) {
					widgetLoader.set(widget, Widget.auxTextVAR, getElementText(child));
				} else if (nodeName.equalsIgnoreCase(EXTENT_ELT)) {
					double x = Double.parseDouble(getAttributeValue(child, X_ATTR));
					double y = Double.parseDouble(getAttributeValue(child, Y_ATTR));
					double width = Double.parseDouble(getAttributeValue(child, WIDTH_ATTR));
					double height = Double.parseDouble(getAttributeValue(child, HEIGHT_ATTR));
					DoubleRectangle extent = new DoubleRectangle(x, y, width, height);

					AShape widgetShape = null;

					String shapeType = getAttributeValue(node, SHAPE_ATTR);

					if ((shapeType == null) || shapeType.equalsIgnoreCase(RECTANGLE_SHAPE)) {
						widgetShape = new ShapeRectangle(extent);
					} else if (shapeType.equalsIgnoreCase(ELLIPSE_SHAPE)) {
						widgetShape = new ShapeOval(extent);
					} else if (shapeType.equalsIgnoreCase(ROUND_RECT_SHAPE)) {
						widgetShape = new ShapeRoundedRectangle(extent);
					} else {
						failedObjectErrors.add("Unknown shape type: " + shapeType);
						widgetShape = new ShapeRectangle(extent);
					}

					if (widgetShape != null) {
						widgetLoader.set(widget, Widget.shapeVAR, widgetShape);
					}
				} else if (nodeName.equalsIgnoreCase(BKG_IMAGE_PATH_ELT)) {
					String backgroundImagePath = getElementText(child);
					byte[] image = loadImage(backgroundImagePath);

					if (image != null) {
						widgetLoader.set(widget, Widget.widgetImageVAR, image);
						widget.setAttribute(WidgetAttributes.IMAGE_PATH_ATTR, backgroundImagePath);
					}
				} else if (nodeName.equalsIgnoreCase(BKG_IMAGE_DATA_ELT)) {
					String backgroundImageData = getElementText(child);

					if (backgroundImageData != "") {
						byte[] image = Base64.decode(backgroundImageData);

						widgetLoader.set(widget, Widget.widgetImageVAR, image);

						String imageName = getAttributeValue(child, NAME_ATTR);

						if ((imageName != null) && !imageName.equals("")) {
							widget.setAttribute(WidgetAttributes.IMAGE_PATH_ATTR, imageName);
						}
					}
				} else if (nodeName.equalsIgnoreCase(TRANSITION_ELT)) {
					Transition t = parseTransition(design, widget, child);

					if (t != null) {
						// TODO Because of the setting of parents is deferred,
						// this is executed when there may not be any
						// parents. This results in the curves not being
						// assigned to the transitions, and is a bug,
						// ticket #775. The fix is probably to also defer
						// adding transitions until later?
						widget.addTransition(t);
					}
				}
			}
			String property = getAttributeValue(node, PROPERTY_ATTR);
			if ((widget.getName() != null) && (widget.getWidgetType() != null) && (widget.getShape() != null)) {
				if (property != null) {
					if (property.equals(DYNAMIC_ELT)) {
						// GroupElement g = new GroupElement(widget.getName(),
						// frame.getName());
						String key = widget.getName() + "#" + frame.getName();
						dynamicWidgetList.put(key, widget);
					}
				}
				return widget;
			}
		}

		return null;
	} // parseWidget

	private IWidget parseWidget_dynamic(Design design, Frame frame, Map<ChildWidget, String> pendingParentSets,
			Map<IAttributed, String> pendingAttrSets, Map<FrameElement, String> pendingRemoteLabels, Node node, int idx,
			int session) throws IOException {
		NodeList children = node.getChildNodes();

		if (children != null) {
			String isStandardValue = getAttributeValue(node, IS_STANDARD_ATTR);
			boolean isStandard;

			if (isStandardValue != null) {
				Boolean isStandardAttrValue = (Boolean) VALUE_REGISTRY.get(isStandardValue);

				if (isStandardAttrValue != null) {
					isStandard = isStandardAttrValue.booleanValue();
				} else {
					isStandard = false;
				}
			} else if (CURRENT_VERSION.equals(dtdVersion)) {
				isStandard = false;
			} else {
				isStandard = true;
			}

			WidgetType widgetType = getWidgetType(getAttributeValue(node, TYPE_ATTR));

			Widget widget = null;

			if (widgetType != null) {
				if (isStandard) {
					if (widgetType == WidgetType.ContextMenu) {
						// Using this constructor sets up childItems and
						// childLocation to the only value currently in use
						widget = new ContextMenu(ContextMenu.FOR_DUPLICATION);
					} else if (widgetType == WidgetType.Menu) {
						// Using this constructor sets up childItems and
						// childLocation to the only value currently in use
						widget = new MenuHeader(MenuHeader.FOR_DUPLICATION);

						// Must also find its "anonymous" parentGroup
						String grpName = getAttributeValue(node, GROUP_ATTR);

						if ((grpName != null) && !"".equals(grpName)) {
							SimpleWidgetGroup menuHdrGroup = groupRegistry.get(grpName);

							if (menuHdrGroup == null) {
								menuHdrGroup = new SimpleWidgetGroup(SimpleWidgetGroup.HORIZONTAL);
								menuHdrGroup.setName(grpName);
								groupRegistry.put(grpName, menuHdrGroup);
							}

							menuHdrGroup.add(widget);
						}
					} else if ((widgetType == WidgetType.Submenu) || (widgetType == WidgetType.MenuItem)) {
						// Must also assign its parent, which must come earlier
						String parentName = getAttributeValue(node, PARENT_ATTR);

						if (parentName != null) {
							MenuItem menuItem = new MenuItem();

							// If a submenu, set up childItems and childLocation
							menuItem.setSubmenu(widgetType == WidgetType.Submenu);

							pendingParentSets.put(menuItem, parentName);

							widget = menuItem;
						} else {
							widget = widgetLoader.createObject();
						}
					} else if (widgetType == WidgetType.PullDownList) {
						// Using this constructor sets up childItems and
						// childLocation to the only value currently in use
						widget = new PullDownHeader(PullDownHeader.FOR_DUPLICATION);
					} else if (widgetType == WidgetType.PullDownItem) {
						// Must assign its parent, which must come earlier
						String parentName = getAttributeValue(node, PARENT_ATTR);

						if (parentName != null) {
							PullDownItem pullDownItem = new PullDownItem();

							pendingParentSets.put(pullDownItem, parentName);

							widget = pullDownItem;
						} else {
							widget = widgetLoader.createObject();
						}
					} else if (widgetType == WidgetType.ListBoxItem) {
						widget = new ListItem();

						// Must also find its "anonymous" parentGroup
						String grpName = getAttributeValue(node, GROUP_ATTR);

						if ((grpName != null) && !"".equals(grpName)) {
							SimpleWidgetGroup listItemGroup = groupRegistry.get(grpName);

							if (listItemGroup == null) {
								listItemGroup = new SimpleWidgetGroup(SimpleWidgetGroup.VERTICAL);
								listItemGroup.setName(grpName);
								groupRegistry.put(grpName, listItemGroup);
							}

							listItemGroup.add(widget);
						}
					} else if ((widgetType == WidgetType.Radio) || (widgetType == WidgetType.Check)) {
						GridButton gridWidget;

						if (widgetType == WidgetType.Radio) {
							gridWidget = new RadioButton();
						} else {
							gridWidget = new CheckBox();
						}

						// Fetch the horizontal/vertical spacing attributes
						double horizontal = Double.parseDouble(getAttributeValue(node, X_ATTR));
						double vertical = Double.parseDouble(getAttributeValue(node, Y_ATTR));
						gridWidget.setHorizSpace(horizontal);
						gridWidget.setVertSpace(vertical);

						// Must also find its "anonymous" parentGroup
						String grpName = getAttributeValue(node, GROUP_ATTR);

						if ((grpName != null) && !"".equals(grpName)) {
							SimpleWidgetGroup gridWidgetGrp = groupRegistry.get(grpName);

							if (gridWidgetGrp == null) {
								if (widgetType == WidgetType.Radio) {
									gridWidgetGrp = new RadioButtonGroup();
								} else {
									gridWidgetGrp = new GridButtonGroup();
								}

								gridWidgetGrp.setName(grpName);
								groupRegistry.put(grpName, gridWidgetGrp);
							}

							gridWidgetGrp.add(gridWidget);
						}

						widget = gridWidget;
					} else {
						widget = widgetLoader.createObject();
					}
				} else {
					widget = widgetLoader.createObject();
				}

				widgetLoader.set(widget, Widget.widgetTypeVAR, widgetType);
			} else {
				widget = widgetLoader.createObject();
			}

			String widgetName = getAttributeValue(node, NAME_ATTR);

			widget.setName(widgetName);

			String remoteLabelAttr = getAttributeValue(node, REMOTE_LABEL_ATTR);

			if ((remoteLabelAttr != null) && !remoteLabelAttr.equals("")) {
				FrameElement remoteLabelOwner = widget.getRemoteLabelOwner();

				if (remoteLabelOwner == null) {
					failedObjectErrors
							.add("Attempting to set a remote label on the wrong widget type for: " + widgetName);
				} else {
					IWidget existingLabel = (IWidget) remoteLabelOwner.getAttribute(WidgetAttributes.REMOTE_LABEL_ATTR);

					if (existingLabel == null) {
						setRemoteLabel(frame, remoteLabelAttr, remoteLabelOwner, pendingRemoteLabels);
					} else if (!remoteLabelAttr.equals(existingLabel.getName())) {
						failedObjectErrors.add("Attempting to set a second remote label for the widget: " + widgetName);
					}
				}
			}

			// For the case that the widget is a RadioButton that
			// has an is-selected attribute, the setting of that
			// attribute in this call will, per the override of setAttribute()
			// in RadioButton, set the proper selected-widget attribute
			// on the containing widget group.
			addAttributes(widget, node, pendingAttrSets);

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();

				if (nodeName.equalsIgnoreCase(DISPLAY_LABEL_ELT)) {
					widgetLoader.set(widget, Widget.titleVAR, getElementText(child));
				} else if (nodeName.equalsIgnoreCase(AUX_TEXT_ELT)) {
					widgetLoader.set(widget, Widget.auxTextVAR, getElementText(child));
				} else if (nodeName.equalsIgnoreCase(EXTENT_ELT)) {
					double x = Double.parseDouble(getAttributeValue(child, X_ATTR));
					double y = Double.parseDouble(getAttributeValue(child, Y_ATTR));
					double width = Double.parseDouble(getAttributeValue(child, WIDTH_ATTR));
					double height = Double.parseDouble(getAttributeValue(child, HEIGHT_ATTR));
					DoubleRectangle extent = new DoubleRectangle(x, y, width, height);

					AShape widgetShape = null;

					String shapeType = getAttributeValue(node, SHAPE_ATTR);

					if ((shapeType == null) || shapeType.equalsIgnoreCase(RECTANGLE_SHAPE)) {
						widgetShape = new ShapeRectangle(extent);
					} else if (shapeType.equalsIgnoreCase(ELLIPSE_SHAPE)) {
						widgetShape = new ShapeOval(extent);
					} else if (shapeType.equalsIgnoreCase(ROUND_RECT_SHAPE)) {
						widgetShape = new ShapeRoundedRectangle(extent);
					} else {
						failedObjectErrors.add("Unknown shape type: " + shapeType);
						widgetShape = new ShapeRectangle(extent);
					}

					if (widgetShape != null) {
						widgetLoader.set(widget, Widget.shapeVAR, widgetShape);
					}
				} else if (nodeName.equalsIgnoreCase(BKG_IMAGE_PATH_ELT)) {
					String backgroundImagePath = getElementText(child);
					byte[] image = loadImage(backgroundImagePath);

					if (image != null) {
						widgetLoader.set(widget, Widget.widgetImageVAR, image);
						widget.setAttribute(WidgetAttributes.IMAGE_PATH_ATTR, backgroundImagePath);
					}
				} else if (nodeName.equalsIgnoreCase(BKG_IMAGE_DATA_ELT)) {
					String backgroundImageData = getElementText(child);

					if (backgroundImageData != "") {
						byte[] image = Base64.decode(backgroundImageData);

						widgetLoader.set(widget, Widget.widgetImageVAR, image);

						String imageName = getAttributeValue(child, NAME_ATTR);

						if ((imageName != null) && !imageName.equals("")) {
							widget.setAttribute(WidgetAttributes.IMAGE_PATH_ATTR, imageName);
						}
					}
				} else if (nodeName.equalsIgnoreCase(TRANSITION_ELT)) {
					if (idx < session - 1) {
						Transition t = parseTransition_dynamic(design, widget, child, idx);

						if (t != null) {
							// TODO Because of the setting of parents is
							// deferred,
							// this is executed when there may not be any
							// parents. This results in the curves not being
							// assigned to the transitions, and is a bug,
							// ticket #775. The fix is probably to also defer
							// adding transitions until later?
							widget.addTransition(t);
						}
					}
				}
			}
			String property = getAttributeValue(node, PROPERTY_ATTR);
			if ((widget.getName() != null) && (widget.getWidgetType() != null) && (widget.getShape() != null)) {
				if (property != null) {
					if (property.equals(DYNAMIC_ELT)) {
						String key = widget.getName() + "#" + frame.getName();
						dynamicWidgetList.put(key, widget);
					}
				}
				return widget;
			}
		}

		return null;
	} // parseWidget

	private void setRemoteLabel(Frame frame, String remoteLabelName, FrameElement labelOwner,
			Map<FrameElement, String> pendingRemoteLabels) {
		IWidget remoteLabel = frame.getWidget(remoteLabelName);

		if (remoteLabel == null) {
			if (pendingRemoteLabels == null) {
				failedObjectErrors.add("Missing remote label widget (" + remoteLabelName + ") for element group: "
						+ labelOwner.getName());
			} else {
				pendingRemoteLabels.put(labelOwner, remoteLabelName);
			}
		} else if (!remoteLabel.getWidgetType().canBeARemoteLabel()) {
			failedObjectErrors.add("Disallowed type for remote label: " + remoteLabelName);
		} else if (remoteLabel instanceof GridButton) {
			failedObjectErrors.add("Disallowed class for remote label: " + remoteLabelName);
		} else {
			labelOwner.setAttribute(WidgetAttributes.REMOTE_LABEL_ATTR, remoteLabel);
			remoteLabel.setAttribute(WidgetAttributes.REMOTE_LABEL_OWNER_ATTR, labelOwner);
			remoteLabel.setAttribute(WidgetAttributes.IS_STANDARD_ATTR, WidgetAttributes.IS_CUSTOM);
		}
	}

	private static final String DEFAULT_GROUP_PREFIX = L10N.get("ICT.GroupNamePrefix", "Group");

	private FrameElementGroup parseEltGroup(Design design, Frame frame, Map<String, FrameElementGroup> pendingGrps,
			Node node) {
		String nameAttr = getAttributeValue(node, NAME_ATTR);
		if (nameAttr == null) {
			nameAttr = DEFAULT_GROUP_PREFIX + " [1]";
		}
		FrameElementGroup eltGroup = pendingGrps.get(nameAttr);
		if (eltGroup == null) {
			eltGroup = new FrameElementGroup();
		}
		eltGroup.setName(nameAttr);
		String remoteLabelAttr = getAttributeValue(node, REMOTE_LABEL_ATTR);
		if ((remoteLabelAttr != null) && !remoteLabelAttr.equals("")) {
			setRemoteLabel(frame, remoteLabelAttr, eltGroup, null);
		}
		addAttributes(eltGroup, node);
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();
				if (nodeName.equalsIgnoreCase(ELTNAME_ELT)) {
					String eltName = getElementText(child);
					IWidget widget = frame.getWidget(eltName);
					FrameElement elt = null;
					if (widget != null) {
						elt = widget.getRootElement();
					} else {
						SimpleWidgetGroup anonymousParentGroup = groupRegistry.get(eltName);
						if (anonymousParentGroup != null) {
							elt = anonymousParentGroup;
						} else {
							FrameElementGroup grp = frame.getEltGroup(eltName);

							// If not found, assume the group will show up
							// later!
							if (grp == null) {
								grp = pendingGrps.get(eltName);
								if (grp == null) {
									grp = new FrameElementGroup();
									grp.setName(eltName);
									pendingGrps.put(eltName, grp);
								}
							}
							elt = grp;
						}
					}
					eltGroup.add(elt);
				} else if (nodeName.equalsIgnoreCase(AUX_TEXT_ELT)) {
					groupLoader.set(eltGroup, Widget.auxTextVAR, getElementText(child));
				}

			}
		}

		return eltGroup;
	}

	/**
	 * Imports a transition
	 * 
	 * @param node
	 */
	private void parseTransitions(Design design, TransitionSource source, Node node) {
		NodeList children = node.getChildNodes();

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);

				if (child.getNodeName().equalsIgnoreCase(TRANSITION_ELT)) {
					Transition t = parseTransition(design, source, child);

					if (t != null) {
						source.addTransition(t);
					}
				}
			}
		}
	}

	private Transition parseTransition_dynamic(Design design, TransitionSource source, Node node, int idx) {
		NodeList children = node.getChildNodes();

		if (children != null) {
			int idx_c = idx + 1;
			String frameName;
			frameName = "frame" + idx_c;
			Frame destination = (frameName != null) ? getFrame(design, frameName) : null;

			if (destination != null) {
				AAction action = null;

				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();

					if (nodeName.equalsIgnoreCase(ACTION_ELT)) {
						action = parseAction(child);
					}
				}

				if (action != null) {
					Transition t = new Transition(source, destination, action);

					String delayDurationAttr = getAttributeValue(node, DURATION_ATTR);

					if (delayDurationAttr != null) {
						t.setDelayInfo(Double.parseDouble(delayDurationAttr),
								getAttributeValue(node, DELAY_LABEL_ATTR));
					}

					addAttributes(t, node);

					return t;
				}
			}
		}

		return null;
	}

	private Transition parseTransition(Design design, TransitionSource source, Node node) {
		NodeList children = node.getChildNodes();

		if (children != null) {
			String frameName = getAttributeValue(node, DEST_FRAME_NAME_ATTR);
			Frame destination = (frameName != null) ? getFrame(design, frameName) : null;

			if (destination != null) {
				AAction action = null;

				for (int i = 0; i < children.getLength(); i++) {
					Node child = children.item(i);
					String nodeName = child.getNodeName();

					if (nodeName.equalsIgnoreCase(ACTION_ELT)) {
						action = parseAction(child);
					}
				}

				if (action != null) {
					Transition t = new Transition(source, destination, action);

					String delayDurationAttr = getAttributeValue(node, DURATION_ATTR);

					if (delayDurationAttr != null) {
						t.setDelayInfo(Double.parseDouble(delayDurationAttr),
								getAttributeValue(node, DELAY_LABEL_ATTR));
					}

					addAttributes(t, node);

					return t;
				}
			}
		}

		return null;
	} // parseTransition

	/**
	 * Imports an action
	 * 
	 * @param node
	 */
	private AAction parseAction(Node node) {
		NodeList children = node.getChildNodes();

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();

				if (nodeName.equalsIgnoreCase(MOUSE_ACTION_ELT)) {
					return parseMouseAction(child);
				}
				if (nodeName.equalsIgnoreCase(TOUCHSCREEN_ACTION_ELT)) {
					return parseTouchscreenAction(child);
				}
				if (nodeName.equalsIgnoreCase(GRAFFITI_ACTION_ELT)) {
					return parseGraffitiAction(child);
				}
				if (nodeName.equalsIgnoreCase(KEYBOARD_ACTION_ELT)) {
					return parseKeyboardAction(child);
				}
				if (nodeName.equalsIgnoreCase(VOICE_ACTION_ELT)) {
					return parseVoiceAction(child);
				}
			}
		}

		return null;
	}

	private AAction parseMouseAction(Node node) {
		int modifiers = AAction.NONE;

		NodeList children = node.getChildNodes();

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);

				if (child.getNodeName().equalsIgnoreCase(KBD_MODIFIER_ELT)) {
					modifiers = addKeyboardModifier(modifiers, getElementText(child));
				}
			}
		}

		String buttonAttr = getAttributeValue(node, BUTTON_ATTR);
		String actionAttr = getAttributeValue(node, ACTION_ATTR);

		MouseButtonState button = null;
		MousePressType mouseAction = null;

		if ((actionAttr == null) || actionAttr.equalsIgnoreCase(DOWNUP_ACTION)) {
			mouseAction = MousePressType.Click;
		} else if (actionAttr.equalsIgnoreCase(DOUBLE_ACTION)) {
			mouseAction = MousePressType.Double;
		} else if (actionAttr.equalsIgnoreCase(TRIPLE_ACTION)) {
			mouseAction = MousePressType.Triple;
		} else if (actionAttr.equalsIgnoreCase(DOWN_ACTION)) {
			mouseAction = MousePressType.Down;
		} else if (actionAttr.equalsIgnoreCase(UP_ACTION)) {
			mouseAction = MousePressType.Up;
		} else if (actionAttr.equalsIgnoreCase(HOVER_ACTION)) {
			mouseAction = MousePressType.Hover;
		} else {
			failedObjectErrors.add("Unknown button action: " + actionAttr);
			mouseAction = MousePressType.Click;
		}

		if (buttonAttr == null) {
			if (mouseAction != MousePressType.Hover) {
				button = MouseButtonState.Left;
			}
		} else if (buttonAttr.equalsIgnoreCase(LEFT_BUTTON)) {
			button = MouseButtonState.Left;
		} else if (buttonAttr.equalsIgnoreCase(MIDDLE_BUTTON)) {
			button = MouseButtonState.Middle;
		} else if (buttonAttr.equalsIgnoreCase(RIGHT_BUTTON)) {
			button = MouseButtonState.Right;
		} else {
			failedObjectErrors.add("Unknown button: " + buttonAttr);
			button = MouseButtonState.Left;
		}

		AAction action = new ButtonAction(button, mouseAction, modifiers);

		addAttributes(action, node);

		return action;
	}

	private AAction parseTouchscreenAction(Node node) {
		int modifiers = AAction.NONE;

		NodeList children = node.getChildNodes();

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);

				if (child.getNodeName().equalsIgnoreCase(KBD_MODIFIER_ELT)) {
					modifiers = addKeyboardModifier(modifiers, getElementText(child));
				}
			}
		}
		// TODO: currently, we have no place to put the modifiers; fix
		// TapAction!

		String actionAttr = getAttributeValue(node, ACTION_ATTR);

		TapPressType tapAction = null;

		if ((actionAttr == null) || actionAttr.equalsIgnoreCase(TAP_ACTION)
				|| actionAttr.equalsIgnoreCase(DOWNUP_ACTION)) {
			tapAction = TapPressType.Tap;
		} else if (actionAttr.equalsIgnoreCase(DOUBLE_ACTION)) {
			tapAction = TapPressType.DoubleTap;
		} else if (actionAttr.equalsIgnoreCase(TRIPLE_ACTION)) {
			tapAction = TapPressType.TripleTap;
		} else if (actionAttr.equalsIgnoreCase(DOWN_ACTION)) {
			tapAction = TapPressType.Down;
		} else if (actionAttr.equalsIgnoreCase(UP_ACTION)) {
			tapAction = TapPressType.Up;
		} else if (actionAttr.equalsIgnoreCase(HOVER_ACTION)) {
			tapAction = TapPressType.Hover;
		} else {
			failedObjectErrors.add("Unknown tap action: " + actionAttr);
			tapAction = TapPressType.Tap;
		}

		AAction action = new TapAction(tapAction);

		addAttributes(action, node);

		return action;
	}

	private boolean isAttributeTRUE(String isAttr, boolean defaultsTRUE) {
		if (isAttr == null) {
			return defaultsTRUE;
		}

		return isAttr.equalsIgnoreCase(TRUE_VALUE) || isAttr.equalsIgnoreCase("t") || isAttr.equals("1");
	}

	private AAction parseGraffitiAction(Node node) {
		NodeList children = node.getChildNodes();

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);

				if (child.getNodeName().equalsIgnoreCase(GESTURES_ELT)) {
					boolean isCommand = isAttributeTRUE(getAttributeValue(node, IS_CMD_ATTR), true);

					AAction action = new GraffitiAction(getElementText(child), isCommand);

					addAttributes(action, node);

					return action;
				}
			}
		}

		return null;
	}

	private KeyPressType getPressType(String type) {
		if (type != null) {
			if (type.equalsIgnoreCase(PRESS_ACTION)) {
				return KeyPressType.Stroke;
			}
			if (type.equalsIgnoreCase(UP_ACTION)) {
				return KeyPressType.Up;
			}
			if (type.equalsIgnoreCase(DOWN_ACTION)) {
				return KeyPressType.Down;
			}

			failedObjectErrors.add("Unknown key press type: " + type);
		}

		return KeyPressType.Stroke;
	}

	private AAction parseKeyboardAction(Node node) {
		NodeList children = node.getChildNodes();

		if (children != null) {
			String text = null;
			int modifiers = AAction.NONE;
			KeyPressType pressType = getPressType(getAttributeValue(node, TYPE_ATTR));

			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);
				String nodeName = child.getNodeName();

				if (nodeName.equalsIgnoreCase(TEXT_ELT)) {
					text = getElementText(child);
				}
				// No need to handle modifiers any more;
				// SPECIAL CHARACTERS ARE USED INSTEAD
			}

			if (text != null) {
				boolean isCommand = isAttributeTRUE(getAttributeValue(node, IS_CMD_ATTR), true);

				if ((text.length() == 1) && isCommand) {
					return new KeyAction(text.charAt(0), pressType, modifiers);
				}

				AAction action = new KeyAction(text, isCommand, modifiers);

				addAttributes(action, node);

				return action;
			}
		}

		return null;
	}

	private int addKeyboardModifier(int modifiers, String addModifier) {
		if (addModifier != null) {
			if (addModifier.equalsIgnoreCase(SHIFT_MODIFIER)) {
				return modifiers | AAction.SHIFT;
			}
			if (addModifier.equalsIgnoreCase(CTRL_MODIFIER)) {
				return modifiers | AAction.CTRL;
			}
			if (addModifier.equalsIgnoreCase(ALT_MODIFIER)) {
				return modifiers | AAction.ALT;
			}
			if (addModifier.equalsIgnoreCase(COMMAND_MODIFIER)) {
				return modifiers | AAction.COMMAND;
			}
			if (addModifier.equalsIgnoreCase(FUNCTION_MODIFIER)) {
				return modifiers | AAction.FUNCTION;
			}

			failedObjectErrors.add("Unknown keyboard modifier: " + addModifier);
		}
		return modifiers;
	}

	private AAction parseVoiceAction(Node node) {
		NodeList children = node.getChildNodes();

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node child = children.item(i);

				if (child.getNodeName().equalsIgnoreCase(TEXT_ELT)) {
					boolean isCommand = isAttributeTRUE(getAttributeValue(node, IS_CMD_ATTR), true);

					AAction action = new VoiceAction(getElementText(child), isCommand);

					addAttributes(action, node);

					return action;
				}
			}
		}

		return null;
	}

	private Task getTask(String taskName, String taskGroupID, TaskParent parent) {
		if (taskName != null) {
			AUndertaking undertaking = null;
			if (taskGroupID == null) {
				undertaking = parent.getUndertaking(taskName);
			}
			if (undertaking == null) {
				List<Task> createdTasks = createdTaskRegistry.get(taskName);
				if (taskGroupID != null) {
					parent = taskGroups.get(taskGroupID);
				}
				if (createdTasks != null) {
					for (Task t : createdTasks) {
						TaskGroup tg = t.getTaskGroup();
						if (tg == parent || (tg == null && taskGroupID == null)) {
							return t;
						}
					}
				} else {
					createdTasks = new ArrayList<Task>(1);
					createdTaskRegistry.put(taskName, createdTasks);
				}
				Task ct = new Task(taskName);
				if (parent instanceof TaskGroup) {
					parent.addUndertaking(ct);
				} else {
					newUndertakings.add(ct);
				}
				createdTasks.add(ct);
				return ct;
			}
			if (undertaking instanceof Task) {
				return (Task) undertaking;
			}
		}
		return null;
	}
}
