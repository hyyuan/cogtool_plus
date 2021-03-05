package cogtoolplus_interpreter;

import java.util.LinkedHashSet;
import java.util.Set;

public class WidgetShapeList {
	protected Set<String> shapeList = new LinkedHashSet<String>();
	protected static final String RECTANGLE= "rectangle";
	protected static final String ELLIPSE = "ellipse";
	protected static final String ROUND_RECT = "rounded rectangle";
    public WidgetShapeList(){   
    	shapeList.add(RECTANGLE);
    	shapeList.add(ELLIPSE);
    	shapeList.add(ROUND_RECT);
    }
    public boolean checkShape(String shape){
    	return shapeList.contains(shape);
    }
}
