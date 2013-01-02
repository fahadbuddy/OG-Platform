/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.equity.indexoption;

import java.util.Collections;
import java.util.Set;

import com.opengamma.analytics.financial.equity.EqyOptBaroneAdesiWhaleyPresentValueCalculator;
import com.opengamma.analytics.financial.equity.StaticReplicationDataBundle;
import com.opengamma.analytics.financial.equity.option.EquityIndexOption;
import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.function.FunctionInputs;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.engine.value.ValueSpecification;

/**
 * Calculates the present value of an equity index option using the Black formula.
 */
public class EquityIndexOptionBAWPresentValueFunction extends EquityIndexOptionBAWFunction {
  /** The Barone-Adesi Whaley present value calculator */
  private static final EqyOptBaroneAdesiWhaleyPresentValueCalculator s_calculator = EqyOptBaroneAdesiWhaleyPresentValueCalculator.getInstance();

  /**
   * Default constructor
   */
  public EquityIndexOptionBAWPresentValueFunction() {
    super(ValueRequirementNames.PRESENT_VALUE);
  }

  @Override
  protected Set<ComputedValue> computeValues(final EquityIndexOption derivative, final StaticReplicationDataBundle market, final FunctionInputs inputs,
      final Set<ValueRequirement> desiredValues, final ComputationTargetSpecification targetSpec, final ValueProperties resultProperties) {
    final ValueSpecification resultSpec = new ValueSpecification(getValueRequirementNames()[0], targetSpec, resultProperties);
    final double pv = s_calculator.visitEquityIndexOption(derivative, market);
    return Collections.singleton(new ComputedValue(resultSpec, pv));
  }

}
