/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.conversion;

import com.opengamma.engine.ComputationTargetType;

/**
 * Converts a value from one currency to another, acting on a position
 */
public class PortfolioNodeCurrencyConversionFunction extends CurrencyConversionFunction {

  public PortfolioNodeCurrencyConversionFunction(final String valueName) {
    super(ComputationTargetType.PORTFOLIO_NODE, valueName);
  }

  public PortfolioNodeCurrencyConversionFunction(final String... valueNames) {
    super(ComputationTargetType.PORTFOLIO_NODE, valueNames);
  }

}
