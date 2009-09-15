/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.view;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.engine.analytics.AnalyticValueDefinition;
import com.opengamma.util.TerminatableJob;

/**
 * The primary job which will recalculate a {@link View}'s output data.
 *
 * @author kirk
 */
public class ViewRecalculationJob extends TerminatableJob {
  private static final Logger s_logger = LoggerFactory.getLogger(ViewRecalculationJob.class);
  private final ViewImpl _view;
  private ViewComputationResultModelImpl _previousResult;
  
  public ViewRecalculationJob(ViewImpl view) {
    if(view == null) {
      throw new NullPointerException("Must provide a backing view.");
    }
    _view = view;
  }

  /**
   * @return the previousResult
   */
  public ViewComputationResultModelImpl getPreviousResult() {
    return _previousResult;
  }

  /**
   * @param previousResult the previousResult to set
   */
  public void setPreviousResult(ViewComputationResultModelImpl previousResult) {
    _previousResult = previousResult;
  }

  /**
   * @return the view
   */
  public ViewImpl getView() {
    return _view;
  }

  @Override
  protected void preStart() {
    super.preStart();
    Set<AnalyticValueDefinition> requiredLiveData = getView().getLogicalDependencyGraphModel().getAllRequiredLiveData();
    s_logger.info("Informing snapshot provider of {} subscriptions to input data", requiredLiveData.size());
    for(AnalyticValueDefinition liveDataDefinition : requiredLiveData) {
      getView().getLiveDataSnapshotProvider().addSubscription(liveDataDefinition);
    }
  }

  @Override
  protected void runOneCycle() {
    ViewComputationCache cache = getView().getComputationCacheFactory().generateCache();
    FullyPopulatedPortfolioNode positionRoot = getView().getPopulatedPositionRoot();
    assert cache != null;
    ViewComputationResultModelImpl result = new ViewComputationResultModelImpl();
    
    SingleComputationCycle cycle = new SingleComputationCycle(cache, positionRoot, getView().getLiveDataSnapshotProvider(), getView().getLogicalDependencyGraphModel(), result, getView().getDefinition());
    cycle.prepareInputs();
    // Flatten, just because we're not going to handle trees yet.
    cycle.loadPositions();
    // REVIEW kirk 2009-09-14 -- This is completely unnecessary to do in the cycle stage.
    // Once we have incremental maintenance of position data we can move it to the beginning
    // of the cycle when we process inbound portfolio changes.
    cycle.buildExecutionPlans();
    cycle.populateResultModel();
    
    long endTime = System.currentTimeMillis();
    result.setResultTimestamp(endTime);
    long delta = endTime - cycle.getStartTime();
    s_logger.info("Completed one recalculation pass in {}ms", delta);
    getView().recalculationPerformed(result);
  }

}
