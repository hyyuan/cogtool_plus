package edu.cmu.cs.hcii.cogtool.controller;

import java.io.IOException;
import edu.cmu.cs.hcii.cogtool.model.Project;
import edu.cmu.cs.hcii.cogtool.ui.UI;
import edu.cmu.cs.hcii.cogtool.util.RcvrCannotUndoRedoException;
import edu.cmu.cs.hcii.cogtool.util.RcvrIOTempException;
import edu.cmu.cs.hcii.cogtool.util.UndoManager;

public class ControllerCP extends DefaultController
{
    public ControllerCP()
    {
        this(new Project("newProject"),
                         true, true);
    }

    public ControllerCP(Project proj,
                             boolean unregistered,
                             boolean unmodified)
    {
        // All CogTool model controllers record the associated Project
        super(proj);

        // Fetch the undo manager for this instance, creating one if one
        // does not already exist for this instance
        undoMgr = UndoManager.getUndoManager(project);

        if (unmodified) {
            // This is by definition a save point
            try {
                // Do this only to the Project's undo manager because
                // we should not ever get into this code when other windows
                // are open unless the project is at a save point; thus,
                // this statement will have no effect.  If no other windows
                // for this project are currently open, then this will be
                // (clearly) the first, and only this manager must be set!
                undoMgr.markSavePoint();
            }
            catch (IllegalStateException ex) {
                throw new RcvrCannotUndoRedoException("Marking save point", ex);
            }
        }

        // Check if registration with the persistence manager is required
        if (unregistered) {
            try {
                persist.registerForPersistence(project);
            }
            catch (IOException e) {
                throw new RcvrIOTempException("Error persisting new project: "
                                                        + e.getMessage(),
                                              e);
            }
        }
    }

    public static ControllerCP newProjectController()
    {
    	ControllerCP controller = new ControllerCP();

        ControllerRegistry.ONLY.addOpenController(controller);

        return controller;
    }

	@Override
	protected Object getModelObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UI getUI() {
		// TODO Auto-generated method stub
		return null;
	}
}
