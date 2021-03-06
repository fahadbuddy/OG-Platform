/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.language.snapshot;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.time.Instant;

import com.google.common.collect.ImmutableList;
import com.opengamma.DataNotFoundException;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.core.marketdatasnapshot.StructuredMarketDataSnapshot;
import com.opengamma.engine.marketdata.snapshot.MarketDataSnapshotter;
import com.opengamma.engine.view.ViewComputationResultModel;
import com.opengamma.engine.view.ViewDeltaResultModel;
import com.opengamma.engine.view.calc.EngineResourceReference;
import com.opengamma.engine.view.calc.ViewCycle;
import com.opengamma.engine.view.client.ViewClient;
import com.opengamma.engine.view.execution.ViewCycleExecutionOptions;
import com.opengamma.engine.view.listener.AbstractViewResultListener;
import com.opengamma.financial.analytics.volatility.cube.VolatilityCubeDefinitionSource;
import com.opengamma.financial.marketdatasnapshot.MarketDataSnapshotterImpl;
import com.opengamma.id.UniqueId;
import com.opengamma.language.context.SessionContext;
import com.opengamma.language.definition.Categories;
import com.opengamma.language.definition.DefinitionAnnotater;
import com.opengamma.language.definition.JavaTypeInfo;
import com.opengamma.language.definition.MetaParameter;
import com.opengamma.language.function.AbstractFunctionInvoker;
import com.opengamma.language.function.MetaFunction;
import com.opengamma.language.function.PublishedFunction;
import com.opengamma.language.view.DetachedViewClientHandle;
import com.opengamma.language.view.UserViewClient;
import com.opengamma.livedata.UserPrincipal;
import com.opengamma.util.async.AsynchronousExecution;

/**
 * Takes a snapshot from the next cycle of a view client, automatically triggering a cycle.
 */
public class TakeSnapshotNowFunction extends AbstractFunctionInvoker implements PublishedFunction {

  // REVIEW 2011-12-01 andrew -- This is not a good function. It partially duplicates the SnapshotViewResultFunction
  // but more importantly should be a procedure as it has a side-effect (triggering a cycle on the view client).

  private static final int DEFAULT_TIMEOUT_MILLIS = 30000;
  
  /**
   * Default instance.
   */
  public static final TakeSnapshotNowFunction INSTANCE = new TakeSnapshotNowFunction();
  
  private final MetaFunction _meta;
  
  private static List<MetaParameter> parameters() {
    return ImmutableList.of(new MetaParameter("view_client_id", JavaTypeInfo.builder(UniqueId.class).get()));
  }
  
  protected TakeSnapshotNowFunction() {
    this(new DefinitionAnnotater(TakeSnapshotNowFunction.class));
  }
  
  private TakeSnapshotNowFunction(final DefinitionAnnotater info) {
    super(info.annotate(parameters()));
    _meta = info.annotate(new MetaFunction(Categories.MARKET_DATA, "TakeSnapshotNow", getParameters(), this));
  }

  private StructuredMarketDataSnapshot invoke(final UserViewClient viewClient, final UserPrincipal liveDataUser, final VolatilityCubeDefinitionSource volatilityCubeDefinitionSource) {
    final NextCycleReferenceListener resultListener = new NextCycleReferenceListener(viewClient.getViewClient(), liveDataUser);
    try {
      viewClient.getViewClient().setViewCycleAccessSupported(true);
      viewClient.addResultListener(resultListener);
      viewClient.getViewClient().triggerCycle();
      resultListener.awaitResult();

      if (resultListener.getCycleReference() != null) {      
        MarketDataSnapshotter snapshotter = new MarketDataSnapshotterImpl(volatilityCubeDefinitionSource);
        return snapshotter.createSnapshot(viewClient.getViewClient(), resultListener.getCycleReference());
      } else {
        throw new OpenGammaRuntimeException("Unable to obtain cycle from view client " + viewClient.getViewClient().getUniqueId(), resultListener.getException());
      }
    } catch (InterruptedException e) {
      Thread.interrupted();
      throw new OpenGammaRuntimeException("Interrupted while waiting for cycle to complete");
    } finally {
      viewClient.removeResultListener(resultListener);
      viewClient.getViewClient().setViewCycleAccessSupported(false);
      resultListener.releaseCycleReference();
    }
  }
  
  @Override
  protected Object invokeImpl(SessionContext sessionContext, Object[] parameters) throws AsynchronousExecution {
    UniqueId viewClientId = (UniqueId) parameters[0];
    DetachedViewClientHandle viewClientHandle = sessionContext.getViewClients().lockViewClient(viewClientId);
    if (viewClientHandle == null) {
      throw new DataNotFoundException("Invalid view client " + viewClientId);
    }
    try {
      return invoke(viewClientHandle.get(), sessionContext.getUserContext().getLiveDataUser(),
          sessionContext.getGlobalContext().getVolatilityCubeDefinitionSource());
    } finally {
      viewClientHandle.unlock();
    }
  }
  
  @Override
  public MetaFunction getMetaFunction() {
    return _meta;
  }
  
  //-------------------------------------------------------------------------
  private class NextCycleReferenceListener extends AbstractViewResultListener {
    
    private final ViewClient _viewClient;
    private final UserPrincipal _liveDataUser;
    private final CountDownLatch _latch = new CountDownLatch(1);
    
    private volatile EngineResourceReference<? extends ViewCycle> _cycle;
    private volatile Exception _exception;
    
    public NextCycleReferenceListener(ViewClient viewClient, UserPrincipal liveDataUser) {
      _viewClient = viewClient;
      _liveDataUser = liveDataUser;
    }
    
    public void awaitResult() throws InterruptedException {
      _latch.await(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
    }
    
    public ViewCycle getCycleReference() {
      return _cycle != null ? _cycle.get() : null;
    }
    
    public void releaseCycleReference() {
      if (_cycle != null) {
        _cycle.release();
      }
    }
    
    public Exception getException() {
      return _exception;
    }
    
    @Override
    public UserPrincipal getUser() {
      return _liveDataUser;
    }

    @Override
    public void cycleCompleted(ViewComputationResultModel fullResult, ViewDeltaResultModel deltaResult) {
      if (_cycle != null || _exception != null) {
        return;
      }
      _cycle = _viewClient.createLatestCycleReference();
      _latch.countDown();
    }

    @Override
    public void viewDefinitionCompilationFailed(Instant valuationTime, Exception exception) {
      if (_cycle != null || _exception != null) {
        return;
      }
      _exception = exception;
      _latch.countDown();
    }

    @Override
    public void cycleExecutionFailed(ViewCycleExecutionOptions executionOptions, Exception exception) {
      if (_cycle != null || _exception != null) {
        return;
      }
      _exception = exception;
      _latch.countDown();
    }
    
  }

}
