package cogtoolplus_interpreter;
import java.util.LinkedHashSet;
import edu.cmu.cs.hcii.cogtool.model.IWidget;
import edu.cmu.cs.hcii.cogtool.model.DoubleRectangle;

public class WidgetElement {
	// set default values
	protected String widgetName = "default";
	protected String widgetType = "button";
	protected String widgetShape = "rectangle";
	protected Boolean widgetStandard = true;	
	protected WidgetTypeList typeList = new WidgetTypeList();
	protected WidgetShapeList shapeList = new WidgetShapeList();
	protected DoubleRectangle bounds = null;// = new DoubleRectangle(15,14,100,100);
	protected String imageSource = null;
	protected LinkedHashSet<TransitionElement> tranList = new LinkedHashSet<TransitionElement>();
	protected IWidget widget = null;
	
	public WidgetElement(){}
	public WidgetElement(String name, String type, String shape, DoubleRectangle extent){
		//These three attributes are compulsory. 
		//This is corresponding to attribute 'name'
		widgetName = (name == null)? widgetName:name;
		//This is corresponding to attribute 'type'
		widgetType = (type == null)? widgetType:type;
		//This is corresponding to attribute 'shape'
		widgetShape = (shape == null)? widgetShape:shape;
		//
		bounds = extent;
		widget.setName(widgetName);
	}
	
	public void editName(String name)
	{		
        if (name == null) {
            throw new IllegalArgumentException("Cannot set name with null identifier!");
        }else{
        	widgetName = name;
        }       
	}
	public void editType(String type) {			
		if (type == null) {
			throw new IllegalArgumentException("Cannot set type with null identifier!");
		}
		if (!typeList.checkType(type)){
			throw new IllegalArgumentException("This widget type is not recognised");
		}
		else {
			widgetType = type;
		}
	}	
	public void editShape(String shape){
		if (shape == null) {
			throw new IllegalArgumentException("Cannot set shape with null identifier!");
		}
		if (!shapeList.checkShape(shape)){
			throw new IllegalArgumentException("This widget shape is not recognised");
		}
		else {
			widgetShape = shape;
		}
	}	
	//This is corresponding to attribute 'w-is-standard'
	public void addStandard(Boolean standard){
		if (standard == null) {
			//throw new IllegalArgumentException("Cannot set standard with null identifier!");
		} else {
			widgetStandard = standard;
		}
	}	
	public String getName(){
		return widgetName;
	}	
	public String getShape(){
		return widgetShape;
	}
	public String getType(){
		return widgetType;
	}
	public boolean isStandard(){
		return widgetStandard;
	}
	public void addExtent(Double x, Double y, Double width, Double hieght){
		bounds = new DoubleRectangle(x,y,width,hieght);
	}
	public DoubleRectangle getExtent(){
		return bounds;
	}
	public void addBackgroundImage(String img_source){
		imageSource = img_source;
	}
	public String getBackgroundImage(){
		return imageSource;
	}
	public void addTransition(TransitionElement transition){
		tranList.add(transition);
	}
	public LinkedHashSet<TransitionElement> getTransition(){
		return tranList;
	}
	public boolean hasExtent(){
		if(bounds==null){
			return false;
		}
		else{
			return true;
		}
	}
	public boolean hasTransition(){
		if(tranList.isEmpty()){
			return false;
		}else{
			return true;
		}
	}
}
