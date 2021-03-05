package cogtoolplus_interpreter;

import java.util.LinkedHashSet;
import java.util.Set;


public class WidgetTypeList{
	protected Set<String> typeList = new LinkedHashSet<String>();
    protected static final String BUTTON = "button";
    protected static final String LINK = "link";
    protected static final String CHECKBOX = "check box";
    protected static final String RADIO = "radio button";
    protected static final String TEXTBOX = "text box";
    protected static final String TEXT = "text";
    protected static final String PULLDOWNLIST = "pull-down list";
    protected static final String PULLDOWNITEM = "pull-down item";
    protected static final String LISTBOXITEM = "list box item";
    protected static final String CONTEXTMENUE = "context menu";
    protected static final String MENUHEADER = "menu";
    protected static final String SUBMENU = "submenu";
    protected static final String MENUITEM = "menu item";
    protected static final String GRAFFITI = "graffiti";
    protected static final String NONINTERACTIVE = "non-interactive";
    
    public WidgetTypeList(){   
    	typeList.add(BUTTON);
    	typeList.add(LINK);
    	typeList.add(CHECKBOX);
    	typeList.add(RADIO);
    	typeList.add(TEXTBOX);
    	typeList.add(TEXT);
    	typeList.add(PULLDOWNLIST);
    	typeList.add(PULLDOWNITEM);
    	typeList.add(LISTBOXITEM);
    	typeList.add(CONTEXTMENUE);
    	typeList.add(MENUHEADER);
    	typeList.add(SUBMENU);
    	typeList.add(MENUITEM);
    	typeList.add(GRAFFITI);
    	typeList.add(NONINTERACTIVE);
    }
    public boolean checkType(String type){
    	return typeList.contains(type);
    }
}
