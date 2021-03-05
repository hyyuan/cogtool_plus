package cogtoolplus_interpreter;

import edu.cmu.cs.hcii.cogtool.model.AScriptStep;
import edu.cmu.cs.hcii.cogtool.model.ActionScriptStep;
import edu.cmu.cs.hcii.cogtool.model.ButtonAction;
import edu.cmu.cs.hcii.cogtool.model.Frame;
import edu.cmu.cs.hcii.cogtool.model.IWidget;
import edu.cmu.cs.hcii.cogtool.model.LookAtScriptStep;
import edu.cmu.cs.hcii.cogtool.model.MouseButtonState;
import edu.cmu.cs.hcii.cogtool.model.MousePressType;
import edu.cmu.cs.hcii.cogtool.model.TapAction;
import edu.cmu.cs.hcii.cogtool.model.TapPressType;
import edu.cmu.cs.hcii.cogtool.model.ThinkScriptStep;
// reference from cogtool: ImportCogToolXML.java and ActionScriptStep.java

public class ScriptStepPlus {
    public static final String DOWNUP_ACTION = "downUp";
    public static final String TAP_ACTION = "tap";
    public static final String PRESS_ACTION = "press";
    public static final String DOUBLE_ACTION = "double";
    public static final String TRIPLE_ACTION = "triple";
    public static final String DOWN_ACTION = "down";
    public static final String UP_ACTION = "up";
    public static final String HOVER_ACTION = "hover";
    
	protected String scriptType;
	protected AScriptStep demoStep;

	public ScriptStepPlus(String type, IWidget widget) {
		scriptType = type;
		demoStep = new LookAtScriptStep(widget);				
	}

	public ScriptStepPlus(String type, Frame frame, Double duration, String text) {
		scriptType = type;
		demoStep = new ThinkScriptStep(frame, duration, text);		
	}

	public ScriptStepPlus(String type, IWidget widget, String MouseState, String MouseType, Integer Mod, Double delay, String delayMsg) {
		scriptType = type;
		ActionScriptStep temp = new ActionScriptStep(ButtonPressAction(MouseState, MouseType, Mod), widget);
		temp.setDelay(delay, delayMsg);			
		demoStep = temp;
	}
	public ScriptStepPlus(String type, IWidget widget, String MouseState, String MouseType, Integer Mod) {
		scriptType = type;
		ActionScriptStep temp = new ActionScriptStep(ButtonPressAction(MouseState, MouseType, Mod), widget);		
		demoStep = temp;	}

	public ScriptStepPlus(String type, IWidget widget, String TapType, Double delay, String delayMsg){
		scriptType = type;
		ActionScriptStep temp = new ActionScriptStep(TouchScreenAction(TapType), widget);			
		temp.setDelay(delay, delayMsg);		
		demoStep = temp;		
	}
	
	private TapAction TouchScreenAction(String type){
		TapPressType tapAction = null;
		switch(type)
		{
		case DOWNUP_ACTION:
			tapAction = TapPressType.Tap;
			break;
		case TAP_ACTION:
			tapAction = TapPressType.Tap;
			break;
		case TRIPLE_ACTION:
			tapAction = TapPressType.TripleTap;
			break;
		case DOUBLE_ACTION:
			tapAction = TapPressType.DoubleTap;
			break;
		case UP_ACTION:
			tapAction = TapPressType.Up;
			break;
		case HOVER_ACTION:
			tapAction = TapPressType.Hover;
			break;
		}
		return new TapAction(tapAction);
	}
	
	private ButtonAction ButtonPressAction(String state, String type, Integer mod) {
		MouseButtonState btn = null;
		MousePressType clk = null;
		switch(state)
		{
		case "left":
			btn = MouseButtonState.Left;
			break;
		case "right":
			btn = MouseButtonState.Right;
			break;
		case "middle":
			btn = MouseButtonState.Middle;
			break;
		}	
		switch(type){
		case "click":
			clk = MousePressType.Click;
			break;
		case "up":
			clk = MousePressType.Up;
			break;
		case "double":
			clk = MousePressType.Double;
			break;
		case "down":
			clk = MousePressType.Down;
			break;
		case "hover":
			clk = MousePressType.Hover;
			break;
		case "triple":
			clk = MousePressType.Triple;
			break;			
		}
		// TO DO: figure out the AACTION.NONE as a parameter of kbmodifier.
		return new ButtonAction(btn,clk,mod);
	}

	public AScriptStep getStep() {
		switch (scriptType) {
		case "Think":
			return (ThinkScriptStep)demoStep;
		case "LookAt":
			return (LookAtScriptStep)demoStep;
		case "Voice":
			return (ActionScriptStep)demoStep;
		case "ButtonPress":
			return (ActionScriptStep)demoStep;
		case "KeyPress":
			return (ActionScriptStep) demoStep;
		case "MouseOver":
			return (ActionScriptStep) demoStep;
		case "GraffitiStroke":
			return (ActionScriptStep) demoStep;
		case "MoveMouse":
			return (ActionScriptStep) demoStep;
		case "Tap":
			return (ActionScriptStep) demoStep;
		case "HomeKeyboard":
			return (ActionScriptStep) demoStep;
		case "HomeMouse":
			return (ActionScriptStep) demoStep;
		default:
			return demoStep;
		}
		// return demoStep;
	}

	public void setActionStep(String scriptType) {
		switch (scriptType) {
		case "LookAt":
			break;
		case "Voice":
			break;
		case "ButtonPress":
			break;
		case "KeyPress":
			break;
		case "MouseOver":
			break;
		case "GraffitiStroke":
			break;
		case "MoveMouse":
			break;
		case "Tap":
			break;
		case "HomeKeyboard":
			break;
		case "HomeMouse":
			break;
		}
	}

	private LookAtScriptStep setScript(IWidget widget) {
		return new LookAtScriptStep(widget);
	}
}
