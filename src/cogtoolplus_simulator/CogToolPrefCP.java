package cogtoolplus_simulator;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import edu.cmu.cs.hcii.cogtool.CogTool;
import edu.cmu.cs.hcii.cogtool.model.CachedGoogleSimilarity;
import edu.cmu.cs.hcii.cogtool.model.SNIFACTPredictionAlgo;
import edu.cmu.cs.hcii.cogtool.ui.PreferencesDialog;
import edu.cmu.cs.hcii.cogtool.util.Alerter;
import edu.cmu.cs.hcii.cogtool.util.L10N;
import edu.cmu.cs.hcii.cogtool.view.MenuFactory;

public enum CogToolPrefCP {
    
    RESEARCH("CogTool.research", Kind.BOOLEAN, false),
    RECENT_PREFIX("CogTool.recent.", Kind.STRING, null),
    HCIPA("CogTool.HCIPA", Kind.BOOLEAN, false),
    KLM_RESULT_RANGE("CogTool.klmResultRange", Kind.BOOLEAN, false),
    SYSWVO("CogTool.SystemWaitVisionOnly", Kind.BOOLEAN, false),
    COMPSCR("CogTool.enableComputeScripts", Kind.BOOLEAN, true),
    MIN_FRAME_WIDTH("CogTool.minFrameWidth", Kind.INT, PreferencesDialog.DEFAULT_MIN_FRAME_WIDTH),
    FRAMES_PER_ROW("CogTool.framesPerRow", Kind.INT, 3),
    ACTR_TIMEOUT("CogTool.actrTimeout", Kind.INT, 600000),
    CONVERTER_DIRECTORY("CogTool.converterDirectory", Kind.STRING, null),
    USE_KEYPAD("CogTool.useKeypad", Kind.BOOLEAN, false),
    IS_LOGGING("CogTool.isLogging", Kind.BOOLEAN, false),
    LOG_DIRECTORY("CogTool.logDirectory", Kind.STRING, System.getProperty("user.home")), 
    IS_TRACING("CogTool.isTracing", Kind.BOOLEAN, true),
    ACTR_DEBUG_LEVEL("CogTool.actrDebugLevel", Kind.INT, 0),
    PMI_G_SIZE("CogTool.pmiGSize", Kind.DOUBLE, CachedGoogleSimilarity.PMI_G_SIZE_AUTOMATIC),
    CTE_SUPPRESS_NOISE("CogTool.CTESuppressNoise", Kind.BOOLEAN, false),
    CTE_DEFAULT_BACK_LABEL("CogTool.CTEDefaultBackLabel", Kind.STRING, L10N.get("PREFS.defaultBackLabel", "Back")),
    GENERATE_THINKS_ON_IMPORT("CogTool.KLMGenerateOnImport", Kind.BOOLEAN, true),
    ACTR_ALTERNATIVE_PARAMETERS("CogTool.AlternativeParameters", Kind.BOOLEAN, false),
    VISUAL_ATTENTION("CogTool.VisualAttention", Kind.INT, 85),
    MOTOR_INITIATION("CogTool.MotorInitiation", Kind.INT, 50),
    PECK_FITTS_COEFF("CogTool.PeckFittsCoeff", Kind.INT, 75),    
    MIN_FITTS("CogTool.MinFittsTime", Kind.INT, 100), 
    
    // A new parameter is added to manipulate the fitts' law intercept value/mini value
    // Corresponding change is made in ACTRPredictionAlgoCP.java, where user is able to
    // change the relevant ACT-R parameter 
    
    ACTR_DAT("CogTool.ACTRDAT", Kind.INT, 50),
    USE_EMMA("CogTool.UseEMMA", Kind.BOOLEAN, true),
    REGENERATE_AUTOMATICALLY("CogTool.RegenerateAutomatically", Kind.BOOLEAN, true), // No UI yet for modifying this
    NESTED_GROUPS_SHOWN_AT_TOP_LEVEL("CogTool.NestedGroupsShownAtTopLevel", Kind.BOOLEAN, false), // No UI yet for modifying this
    DISPLAY_DIGITS("CogTool.DisplayDigits", Kind.INT, 1),
    CTE_BACK_BUTTON_SEMANTICS("CogTool.CTEBackButtonSemantics", Kind.INT, SNIFACTPredictionAlgo.EXPLICT_BACK),
    CTE_SUPPRESS_NONINTERACTIVE("CogTool.CTESuppressNoninteractive", Kind.BOOLEAN, false);
	
	
    
    public static class PreferencesChange extends EventObject {
        private final Set<CogToolPrefCP> prefsChanged;
        public PreferencesChange(Set<CogToolPrefCP> pc) {
            super(CogToolPrefCP.class);
            prefsChanged = pc;
        }
        public Set<CogToolPrefCP> getPrefs() {
            return prefsChanged;
        }
    }
    
    private enum Kind { STRING, BOOLEAN, INT, DOUBLE };
    
    public static final int NUM_RECENT_FILES = 10;
    public static final Alerter ALERTER = new Alerter();
    
    private static Preferences prefs =
            Preferences.userNodeForPackage(CogTool.class);
    static {
        for (CogToolPrefCP p : CogToolPrefCP.values()) {
            switch (p.kind) {
                case STRING:
                    p.value = prefs.get(p.key, (String)p.defaultValue);
                    break;
                case BOOLEAN:
                    p.value = prefs.getBoolean(p.key, (Boolean)p.defaultValue);
                    break;
                case INT:
                    p.value = prefs.getInt(p.key, (Integer)p.defaultValue);
                    break;
                case DOUBLE:
                    p.value = prefs.getDouble(p.key, (Double)p.defaultValue);
            }
        }
    };
    
    private static List<String> recentFiles = new ArrayList<String>();
    static {
        for (int i = 0; i < NUM_RECENT_FILES; i++) {
            recentFiles.add(prefs.get((RECENT_PREFIX.key) + i, MenuFactory.UNSET_FILE));
        }
    }

    private final String key;
    private final Kind kind;
    private Object value = null;
    private final Object defaultValue;
    
    private CogToolPrefCP(String ky, Kind kd, Object dv) {
        key = ky;
        kind = kd;
        defaultValue = dv;
    }
    
    private void testKind(Kind k) {
        if (kind != k) {
            throw new IllegalArgumentException(String.format(
               "%s does not name a %s-valued preference", this, k));
        }
    }
    
    public String getString() {
        testKind(Kind.STRING);
        return (String)value;
    }
    
    public boolean getBoolean() {
        testKind(Kind.BOOLEAN);
        return (Boolean)value;
    }
    
    public int getInt() {
        testKind(Kind.INT);
        return (Integer)value;
    }
    
    public double getDouble() {
        testKind(Kind.DOUBLE);
        return (Double)value;
    }
    
    public boolean setString(String val) {
        testKind(Kind.STRING);
        if (val == value) {
            return false;
        }
        value = val;
        prefs.put(key, val);
        return true;
    }
    
    public boolean setBoolean(boolean val) {
        testKind(Kind.BOOLEAN);
        if (val == (Boolean)value) {
            return false;
        }
        value = val;
        prefs.putBoolean(key, val);
        return true;
    }
    
    public boolean setInt(int val) {
        testKind(Kind.INT);
        if (val == (Integer)value) {
            return false;
        }
        value = val;
        prefs.putInt(key, val);
        return true;
    }
    
    public boolean setDouble(double val) {
        testKind(Kind.DOUBLE);
        if (val == (Double)value) {
            return false;
        }
        value = val;
        prefs.putDouble(key, val);
        return true;
    }
    
    public String getStringDefault() {
        testKind(Kind.STRING);
        return (String)defaultValue;
    }
    
    public boolean getBooleanDefault() {
        testKind(Kind.BOOLEAN);
        return (Boolean)defaultValue;
    }
    
    public int getIntDefault() {
        testKind(Kind.INT);
        return (Integer)defaultValue;
    }
    
    public double getDoubleDefault() {
        testKind(Kind.DOUBLE);
        return (Double)defaultValue;
    }
        
    // Force the preferences stuff to be written to whatever mechanism is used
    // to persist it. Otherwise it is not guaranteed to be written until the
    // JVM terminates, though it may be written sooner if the JVM so chooses.
    // And, of course, if the JVM crashes or otherwise terminates abnormally
    // all bets are off, unless an explicit flush() has been performed
    // successfully.
    public static boolean flush() {
        try {
            prefs.flush();
            return true;
        } catch (BackingStoreException e) {
            // mostly ignore it -- the preferences changes will apply only
            // to the current invocation of CogTool and not be saved for the
            // future; however, scribble some info to stderr to help a
            // determined debugger to learn something
            System.err.println("Couldn't flush CogTool Preferences node");
            e.printStackTrace();
            return false;
        }
    }
    
    public static List<String> getRecent()
    {
        return new ArrayList<String>(recentFiles); // defensive copy
    }

    public static boolean setRecent(String newFile)
    {
        if ((newFile == null) || MenuFactory.UNSET_FILE.equals(newFile)) {
            return false;
        }
        if (recentFiles.size() > 0) {
            String def = recentFiles.get(0);

            if (newFile.equals(def)) {
                return false;
            }
        }
        int index = recentFiles.indexOf(newFile);
        if (index != -1) {
            recentFiles.remove(index);
        }
        else if (recentFiles.size() == NUM_RECENT_FILES) {
            recentFiles.remove(NUM_RECENT_FILES - 1);
        }
        recentFiles.add(0, newFile);
        for (int i = 0; i < recentFiles.size(); i++) {
            prefs.put((RECENT_PREFIX.key + i), recentFiles.get(i));
        }
        flush();
        return true;
    }

    public static void clearRecent()
    {
        for (int i = 0; i < NUM_RECENT_FILES; i++) {
            prefs.put((RECENT_PREFIX.key + i), MenuFactory.UNSET_FILE);
        }
        recentFiles.clear();
        flush();
    }

    public static boolean hasRecent()
    {
        return (recentFiles.size() > 0) &&
                (! (recentFiles.get(0).equals(MenuFactory.UNSET_FILE)));
    }
    
    public static Boolean isTracingOverride = null;
    
    public static boolean isTraceEmitted() {
        if (isTracingOverride != null) {
            return isTracingOverride.booleanValue();
        } else {
            return !RESEARCH.getBoolean() || IS_TRACING.getBoolean();
        }
    }
     
}

 

