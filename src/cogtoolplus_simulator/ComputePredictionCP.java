package cogtoolplus_simulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.cmu.cs.hcii.cogtool.CogToolPref;
import edu.cmu.cs.hcii.cogtool.CogToolWorkThread;
import edu.cmu.cs.hcii.cogtool.controller.SNIFACTCmd;
import edu.cmu.cs.hcii.cogtool.model.ACTRPredictionAlgo;
import edu.cmu.cs.hcii.cogtool.model.CognitiveModelGenerator;
import edu.cmu.cs.hcii.cogtool.model.IPredictionAlgo;
import edu.cmu.cs.hcii.cogtool.model.APredictionResult;
import edu.cmu.cs.hcii.cogtool.model.Script;
import edu.cmu.cs.hcii.cogtool.model.TaskApplication;
import edu.cmu.cs.hcii.cogtool.model.PredictionResultProxy;
import edu.cmu.cs.hcii.cogtool.model.Project;
import edu.cmu.cs.hcii.cogtool.model.TraceParser;
import edu.cmu.cs.hcii.cogtool.ui.Interaction;
import edu.cmu.cs.hcii.cogtool.ui.ProjectLID;
import edu.cmu.cs.hcii.cogtool.ui.RcvrExceptionHandler;
import edu.cmu.cs.hcii.cogtool.ui.Interaction.ITraceWindow;
import edu.cmu.cs.hcii.cogtool.util.AUndoableEdit;
import edu.cmu.cs.hcii.cogtool.util.IUndoableEdit;
import edu.cmu.cs.hcii.cogtool.util.L10N;
import edu.cmu.cs.hcii.cogtool.util.ProcessTraceCallback;
import edu.cmu.cs.hcii.cogtool.util.RcvrComputationException;
import edu.cmu.cs.hcii.cogtool.util.RcvrIllegalStateException;
import edu.cmu.cs.hcii.cogtool.util.RcvrParsingException;
import edu.cmu.cs.hcii.cogtool.util.RcvrUnimplementedFnException;
import edu.cmu.cs.hcii.cogtool.util.ThreadManager;
import edu.cmu.cs.hcii.cogtool.util.UndoManager;

public class ComputePredictionCP {
	public ComputePredictionCP() {
	}

	/**
	 * Adds the edit for each of the scripts in the demonstration.
	 * 
	 * @param seq
	 * @param demo
	 * @param project
	 */
	protected static void addUndoableEditToScripts(IUndoableEdit edit, TaskApplication taskApp, Project project) {
		Iterator<CognitiveModelGenerator> modelGens = taskApp.getModelGenerators();

		while (modelGens.hasNext()) {
			CognitiveModelGenerator modelGen = modelGens.next();

			Script script = taskApp.getScript(modelGen);

			if (script != null) {
				UndoManager undoMgr = UndoManager.getUndoManager(script, project);

				if (undoMgr != null) {
					undoMgr.addEdit(edit);
				}
			}
		}
	}

	/**
	 * Perform computation totally within the main thread.
	 *
	 * @param alg
	 *            the algorithm to use in computing the analysis
	 * @param script
	 *            the script to execute to compute the prediction
	 * @return a result object with implementation-specific type and contents
	 */
	/*public static APredictionResult computePrediction(IPredictionAlgo alg, Script script, double minFitts,
			double cofFitts) {
		return computePrediction(alg, script, null);// , minFitts, cofFitts);
	}*/

	/**
	 * Perform computation with progress callback in the main thread.
	 *
	 * @param alg
	 *            the algorithm to use in computing the analysis
	 * @param script
	 *            the script to execute to compute the prediction
	 * @param pcb
	 *            the progress callback
	 * @return a result object with implementation-specific type and contents
	 */
	public static APredictionResult computePrediction(IPredictionAlgo alg, Script script, ProcessTraceCallback pcb) {
		try {
			return alg.prepareComputation(script).compute(pcb).completeWork();
		} catch (IPredictionAlgo.ComputationException ex) {
			//System.out.println("!1");
			throw new RcvrComputationException(ex);
		} catch (TraceParser.ParseException ex) {
			//System.out.println("!2");
			throw new RcvrParsingException(ex);
		} catch (IllegalStateException ex) {
			//System.out.println("!3");
			throw new RcvrIllegalStateException(ex);
		} catch (UnsupportedOperationException ex) {
			//System.out.println("!4");
			throw new RcvrUnimplementedFnException(ex);
		} catch (Exception ex) {
			//System.out.println("!5");
			throw new RcvrComputationException(ex);
		}
	}

	/**
	 * Utility to recompute in the main thread all the results for all the
	 * scripts of a TaskApplication, using the given callback.
	 */
	public IUndoableEdit computeAllPredictions(Project project, final TaskApplication ta,
			final IPredictionAlgo compute)
	// Interaction interaction)

	{
		// The list of old results that were replaced.
		final Map<CognitiveModelGenerator, APredictionResult> oldResults = new HashMap<CognitiveModelGenerator, APredictionResult>();
		//System.out.println("Step 1..............");
		// It is possible the set of old results does not include one for the
		// specified computation algorithm; after enumerating, track new
		// results for that algorithm (needed so that we can unset results
		// in the undo action!).
		final List<APredictionResult> ensuredResults = new ArrayList<APredictionResult>();
		//System.out.println("here 1" + ta.toString());
		//if (ta!=null){
			//System.out.println("here 2" + ta.toString());
		Iterator<CognitiveModelGenerator> modelGens = ta.getModelGenerators();	
		//System.out.println("Step 3..............");		
		int obsoleteWaitContainingResults = 0;

		while (modelGens.hasNext()) {
			CognitiveModelGenerator modelGen = modelGens.next();
			APredictionResult oldResult = ta.getResult(modelGen, compute);

			// oldResult may be null if not set
			oldResults.put(modelGen, oldResult);

			Script script = ta.getScript(modelGen);
			APredictionResult ensureResult;
			//System.out.println("script step state count" + script.getStepStateCount());
			//System.out.println("demonstration step count" + script.getDemonstration().getStepCount());
            /*if (inBackground) {
                ensureResult =
                    computeInBackground(compute, script, interaction);
            }*/
            //else {
                ensureResult = computePrediction(compute, script, null);
            //}

            if (ACTRPredictionAlgo.usesObsoleteWaits(ensureResult)) {
                ++obsoleteWaitContainingResults;
            }
			//System.out.println("result is " + ensureResult.getName() + " " + ensureResult.getResultState() + " " + ensureResult.toString());
			ensuredResults.add(ensureResult);
			ta.setResult(modelGen, compute, ensureResult);
		}
		//}

		if (ensuredResults.size() > 0) {
			IUndoableEdit edit = new AUndoableEdit(ProjectLID.RecomputeScript) {
				@Override
				public String getPresentationName() {
					return L10N.get("UNDO.PM.RecomputeScript(s)", "Recompute Script(s)");
				}

				protected void setResults(List<APredictionResult> results) {
					int numResults = results.size();
					
					for (int i = 0; i < numResults; i++) {
						APredictionResult result = results.get(i);

						result = PredictionResultProxy.getLatestResult(result);
						results.set(i, result);

						ta.setResult(result.getScript().getModelGenerator(), result.getPredictionAlgorithm(), result);
					}
				}

				@Override
				public void redo() {
					super.redo();

					setResults(ensuredResults);
				}
                /*
				@Override
				public void undo() {
					super.undo();

					Iterator<Map.Entry<CognitiveModelGenerator, APredictionResult>> resetResults = oldResults.entrySet()
							.iterator();

					while (resetResults.hasNext()) {
						Map.Entry<CognitiveModelGenerator, APredictionResult> entry = resetResults.next();

						// key is modelGen, value part is the old result
						CognitiveModelGenerator modelGen = entry.getKey();
						APredictionResult oldResult = entry.getValue();
						if (oldResult == null) {
							ta.unsetResult(modelGen, compute);
						} else {
							ta.setResult(modelGen, compute, oldResult);
						}
					}
				}*/
			};

			// addUndoableEditToScripts(edit, ta, project);

			return edit;
		}

		return null;
	}

	/**
	 * Support for performing the analysis work in a background thread.
	 */
	/*
	protected static class AnalysisWorkThread extends CogToolWorkThread {
		protected IPredictionAlgo computeAlg;

		protected IPredictionAlgo.IAnalysisInput threadInput;
		protected IPredictionAlgo.IAnalysisOutput threadOutput = null;

		protected Interaction interaction;

		public AnalysisWorkThread(IPredictionAlgo predictionAlg, ITraceWindow traceWindow, Interaction uiInteraction) {
			// This initializes this.progressCallback to traceWindow
			super((traceWindow != null) ? traceWindow.getDisabler() : null, traceWindow, false);

			computeAlg = predictionAlg;

			interaction = uiInteraction;
		}

		public void setTraceWindow(ITraceWindow traceWindow) {
			setDisabler((traceWindow != null) ? traceWindow.getDisabler() : null);
			setProgressCallback(traceWindow, false);
		}

		public void doWork() {
			// Performed in the child thread; because the progressCallback
			// is initialized to the traceWindow, the cast should succeed!
			threadOutput = threadInput.compute((ITraceWindow) progressCallback, this);
			SNIFACTCmd.isComputing = false;
		}
	}
	*/
	
	/**
	 * Support for performing the analysis work in a background thread.
	 */
	/*
	protected static class DefaultAnalysisWorkThread extends AnalysisWorkThread {
		protected Script script;

		protected PredictionResultProxy resultProxy;

		public DefaultAnalysisWorkThread(IPredictionAlgo predictionAlg, Script s, ITraceWindow traceWindow,
				Interaction uiInteraction) {
			// This initializes this.progressCallback to traceWindow
			super(predictionAlg, traceWindow, uiInteraction);

			script = s;

			threadInput = computeAlg.prepareComputation(script);

			// TODO: replace "Proxy" with actual name
			resultProxy = new PredictionResultProxy("Proxy", s, computeAlg);
		}

		public APredictionResult getResultProxy() {
			return resultProxy;
		}

		@Override
		public void doneCallback() {
			// Performed in the main UI thread after doWork() has completed
			super.doneCallback();

			TaskApplication taskApp = script.getDemonstration().getTaskApplication();
			CognitiveModelGenerator modelGen = script.getModelGenerator();
			APredictionResult taResult = taskApp.getResult(modelGen, computeAlg);

			// Note that threadOutput will be null if an exception is thrown
			// by the child work thread!
			if ((!isCanceled()) && (threadOutput != null)) {
				APredictionResult result = threadOutput.completeWork();

				resultProxy.setActualResult(result);

				// If the computation hasn't been "undone", then it's ok to
				// reset the result in the task application.
				if (taResult == resultProxy) {
					taskApp.setResult(modelGen, computeAlg, result);
				}
			} else {
				// If the computation has been assigned, then we must
				// unset the result in the task application.
				if (taResult == resultProxy) {
					taskApp.setResult(modelGen, computeAlg, null);
				}

				RcvrExceptionHandler.recoverWorkThread(this, interaction);
			}
		}
	}
	*/
	
	/**
	 * Perform the analysis in the background. Set the result when done.
	 */
	/*
	public static APredictionResult computeInBackground(IPredictionAlgo computeAlg, Script s, Interaction interact) {
		try {
			DefaultAnalysisWorkThread workThread = new DefaultAnalysisWorkThread(computeAlg, s, null, interact);

			ITraceWindow traceWin = interact.createTraceWindow("Computation trace", workThread,
					"Trace output: stdout (top) and stderr (bottom)");

			workThread.setTraceWindow(traceWin);

			ThreadManager.startNewThread(workThread);

			return workThread.getResultProxy();
		} catch (IPredictionAlgo.ComputationException ex) {
			throw new RcvrComputationException(ex);
		} catch (IllegalStateException ex) {
			throw new RcvrIllegalStateException(ex);
		} catch (UnsupportedOperationException ex) {
			throw new RcvrUnimplementedFnException(ex);
		} catch (Exception ex) {
			throw new RcvrComputationException(ex);
		}
	}*/
}
