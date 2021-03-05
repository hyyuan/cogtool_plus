package cogtoolplus_interpreter;

import java.util.LinkedHashSet;
import java.util.Set;

// five separate actions: mouse action, touchscreen action, graffiti action, keyboard action, voiceaction 
// here we complete the mouse action first
public class ActionElement{
	protected static final String BUTTON_LEFT = "left";
	protected static final String BUTTON_RIGHT = "right";
	protected static final String BUTTON_MIDDLE = "middle";

	protected static final String BUTTON_DOWNUP = "downup";
	protected static final String BUTTON_DOUBLE = "double";
	protected static final String BUTTON_TRIPLE = "triple";
	protected static final String BUTTON_DOWN = "down";
	protected static final String BUTTON_UP = "up";
	protected static final String BUTTON_HOVER = "hover";
	
	protected static final String SHIFT_MODIFIER = "SHIFT";
	protected static final String CTRL_MODIFIER = "CTRL";
	protected static final String ALT_MODIFIER = "ALT";
	protected static final String COMMAND_MODIFIER = "COMMAND";
	protected static final String FUNCTION_MODIFIER = "FUNCTION";

	protected Set<String> buttonTypeList = new LinkedHashSet<String>();
	protected Set<String> buttonActionList = new LinkedHashSet<String>();
	protected Set<String> keyboardStateList = new LinkedHashSet<String>();
	
	protected String ButtonType;
	protected String ButtonAction;
	protected String KeyboardAction;
	protected WidgetElement widget;
	protected Double duration;
	public ActionElement(WidgetElement wdt, Double time) {		
		widget = wdt;
		duration = time;
		// add button type
		buttonTypeList.add(BUTTON_LEFT);
		buttonTypeList.add(BUTTON_RIGHT);
		buttonTypeList.add(BUTTON_MIDDLE);
        // add button action
		buttonActionList.add(BUTTON_DOWNUP);
		buttonActionList.add(BUTTON_DOUBLE);
		buttonActionList.add(BUTTON_TRIPLE);
		buttonActionList.add(BUTTON_DOWN);
		buttonActionList.add(BUTTON_UP);
		buttonActionList.add(BUTTON_HOVER);		
		// add keyboard state 
		keyboardStateList.add(SHIFT_MODIFIER);
		keyboardStateList.add(CTRL_MODIFIER);
		keyboardStateList.add(ALT_MODIFIER);
		keyboardStateList.add(COMMAND_MODIFIER);
		keyboardStateList.add(FUNCTION_MODIFIER);		
	}
	public ActionElement(WidgetElement wdt, String buttonType, String buttonAction){
		widget = wdt;
		duration = 0.0;
		// add button type
		buttonTypeList.add(BUTTON_LEFT);
		buttonTypeList.add(BUTTON_RIGHT);
		buttonTypeList.add(BUTTON_MIDDLE);
        // add button action
		buttonActionList.add(BUTTON_DOWNUP);
		buttonActionList.add(BUTTON_DOUBLE);
		buttonActionList.add(BUTTON_TRIPLE);
		buttonActionList.add(BUTTON_DOWN);
		buttonActionList.add(BUTTON_UP);
		buttonActionList.add(BUTTON_HOVER);		
		// add keyboard state 
		keyboardStateList.add(SHIFT_MODIFIER);
		keyboardStateList.add(CTRL_MODIFIER);
		keyboardStateList.add(ALT_MODIFIER);
		keyboardStateList.add(COMMAND_MODIFIER);
		keyboardStateList.add(FUNCTION_MODIFIER);	
		
		if (buttonType == null) {
			throw new IllegalArgumentException("Cannot set type with null identifier!");
		}
		if (!buttonTypeList.contains(buttonType)){
			throw new IllegalArgumentException("This button type is not recognised");
		}
		else {
			ButtonType = buttonType;
		}
		
		if (buttonAction == null) {
			throw new IllegalArgumentException("Cannot set type with null identifier!");
		}
		if (!buttonActionList.contains(buttonAction)){
			throw new IllegalArgumentException("This button action is not recognised");
		}
		else {
			ButtonAction = buttonAction;
		}
		
	}
	public WidgetElement getWidget(){
		return widget;
	}
	public Double getDuration(){
		return duration;
	}
	/*
	public void SetMouseButtonType(String buttonType){
		if (buttonType == null) {
			throw new IllegalArgumentException("Cannot set type with null identifier!");
		}
		if (!buttonTypeList.contains(buttonType)){
			throw new IllegalArgumentException("This button type is not recognised");
		}
		else {
			ButtonType = buttonType;
		}
	}
	public String getMouseButtonType(){
		return ButtonType;
	}
	*/
	/*
	public void SetMouseButtonAction(String buttonAction){
		if (buttonAction == null) {
			throw new IllegalArgumentException("Cannot set type with null identifier!");
		}
		if (!buttonActionList.contains(buttonAction)){
			throw new IllegalArgumentException("This button action is not recognised");
		}
		else {
			ButtonAction = buttonAction;
		}
	}*/
	public String getMouseButtonAction(){
		return ButtonAction;
	}
	public void SetKeyboardAction(String keyboardAction){
		if (keyboardAction == null) {
			throw new IllegalArgumentException("Cannot set type with null identifier!");
		}
		if (!keyboardStateList.contains(keyboardAction)){
			throw new IllegalArgumentException("This keyboard state is not recognised");
		}
		else {
			KeyboardAction = keyboardAction;
		}
	}
	public String getKeyboardAction(){
		return KeyboardAction;
	}
}
