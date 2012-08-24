/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.creditdefaultswap.definition;

import javax.time.calendar.ZonedDateTime;

import com.opengamma.analytics.financial.model.interestrate.curve.YieldCurve;
import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.money.Currency;

/**
 *  Definition of a vanilla legacy Credit Default Swap contract (i.e. transacted prior to Big Bang 8th April 2009)
 */
public class CreditDefaultSwapDefinition {
  
  //----------------------------------------------------------------------------------------------------------------------------------------
  
  // Member variables of the CDS contract (defines what a CDS is)

  // From the users perspective, are we buying or selling protection
  private final String _buySellProtection;

  // Identifiers for the (three) counterparties in the trade
  private final String _protectionBuyer;
  private final String _protectionSeller;
  private final String _referenceEntity;
  
  // The currency the trade is executed in e.g. USD
  private final Currency _currency;
  
  // The seniority of the debt of the reference entity the CDS is written on (e.g. senior or subordinated)
  private final String _debtSeniority;
  
  // The restructuring type in the event of a credit event deemed to be a restructuring of the reference entities debt (e.g. OR, MR, MMR or NR)
  private final String _restructuringClause;
  
  // Holiday calendar for the determination of adjusted business days in the cashflow schedule
  private final Calendar _calendar;
  
  // The date of the contract inception
  private final ZonedDateTime _startDate;
  
  // The effective date for protection to begin (usually T + 1 for legacy CDS)
  private final ZonedDateTime _effectiveDate;
  
  // The maturity date of the contract (when premium and protection coverage ceases)
  private final ZonedDateTime _maturityDate;
  
  // The date on which we want to calculate the CDS MtM
  private final ZonedDateTime _valuationDate;
  
  // The method for generating the schedule of premium payments
  private final String _scheduleGenerationMethod;
  
  // The frequency of coupon payments (usually quarterly)
  private final String _couponFrequency;
  
  // Day-count convention (usually Act/360)
  private final String _daycountFractionConvention;
  
  // Business day adjustment convention (usually following)
  private final String _businessdayAdjustmentConvention;
  
  // Flag to determine if we business day adjust the final maturity date
  private final boolean _adjustMaturityDate;
  
  // The trade notional (in the trade currency)
  private final double _notional;
  
  // The quoted par spread of the trade (as this is a legacy CDS, this is the coupon applied to the premium leg 
  // to give the trade a value of zero at the contract start date (there is no exchange of payments upfront) 
  private final double _parSpread;
  
  // The recovery rate to be used in the calculation of the CDS MtM (can be different to the rate used to calibrate the survival curve)
  private final double _valuationRecoveryRate;
  
  // The recovery rate to be used when calibrating the hazard rate term structure to the market par CDS spread quotes
  private final double _curveRecoveryRate;
  
  // Flag to determine whether the accrued coupons should be included in the CDS premium lag calculation
  private final boolean _includeAccruedPremium;
  
  // The number of integration steps (per year) - for the computation of the integral in the contingent leg
  private final int _numberOfIntegrationSteps;
  
  // The yield curve object for discount factors
  private final YieldCurve _yieldCurve;
  
  // TODO : Add the survival curve (assuming that we want the survival probabilities as an input to the pricer - not the par CDS spread term struct)
  
  // TODO : Should the yield and survival curves be part of the CDS object? Should they be passed in to the pricer seperately?
  
  // ----------------------------------------------------------------------------------------------------------------------------------------
  
  // Constructor for a CDS definition object (all fields are user specified)
 
  // Probably not the best way of handling schedule generation, business day conventions etc - need to look at another product to see how it is done
  
  // How can we reduce the number of parameters?
  
  public CreditDefaultSwapDefinition(String buySellProtection,
                                      String protectionBuyer, 
                                      String protectionSeller, 
                                      String referenceEntity, 
                                      Currency currency, 
                                      String debtSeniority, 
                                      String restructuringClause, 
                                      Calendar calendar,
                                      ZonedDateTime startDate,
                                      ZonedDateTime effectiveDate,
                                      ZonedDateTime maturityDate,
                                      ZonedDateTime valuationDate,
                                      String scheduleGenerationMethod,
                                      String couponFrequency,
                                      String daycountFractionConvention,
                                      String businessdayAdjustmentConvention,
                                      double notional, 
                                      double parSpread, 
                                      double valuationRecoveryRate, 
                                      double curveRecoveryRate, 
                                      boolean includeAccruedPremium,
                                      boolean adjustMaturityDate,
                                      int numberOfIntegrationSteps,
                                      YieldCurve yieldCurve) {
    
    //ArgumentChecker.isTrue(buySellProtection.isEmpty(), "Buy/Sell protection flag is empty");
    
    /*
    ArgumentChecker.isTrue(protectionSeller.isEmpty(), "Protection seller field is empty");
    ArgumentChecker.isTrue(protectionBuyer.isEmpty(), "Protection buyer field is empty");
    ArgumentChecker.isTrue(referenceEntity.isEmpty(), "Reference entity field is empty");
    
    ArgumentChecker.isTrue(debtSeniority.isEmpty(), "Debt seniority field is empty");
    ArgumentChecker.isTrue(restructuringClause.isEmpty(), "Restructuring clause field is empty");
    
    ArgumentChecker.isTrue(scheduleGenerationMethod.isEmpty(), "Schedule generation method field is empty");
    ArgumentChecker.isTrue(couponFrequency.isEmpty(), "Coupon frequency field is empty");
    ArgumentChecker.isTrue(daycountFractionConvention.isEmpty(), "Daycount fraction convention field is empty");
    ArgumentChecker.isTrue(businessdayAdjustmentConvention.isEmpty(), "Business day adjustment convention field is empty");
    
    
    //ArgumentChecker.isTrue(startdate.isBefore(valuationdate), "Start date {} must be before valuation date {}", startdate, valuationdate);
    
    */
    
    // TODO : Add all the argument checkers
    
    ArgumentChecker.isTrue(notional >= 0.0,  "Notional amount should be greater than or equal to zero");
    ArgumentChecker.isTrue(parSpread >= 0.0,  "CDS par spread should be greater than or equal to zero");
    
    ArgumentChecker.isInRangeInclusive(valuationRecoveryRate, 0.0, 1.0);
    ArgumentChecker.isInRangeInclusive(curveRecoveryRate, 0.0, 1.0);
    
    ArgumentChecker.isTrue(numberOfIntegrationSteps > 0,  "Number of integration steps (for contingent leg valuation) should be greater than zero");
    
    _buySellProtection = buySellProtection;
    _protectionBuyer = protectionBuyer;
    _protectionSeller = protectionSeller;
    _referenceEntity = referenceEntity;
    
    _currency = currency;
    _debtSeniority = debtSeniority;
    _restructuringClause = restructuringClause;
    
    _calendar = calendar;
    
    _startDate = startDate;
    _effectiveDate = effectiveDate;
    _maturityDate = maturityDate;
    _valuationDate = valuationDate;
    
    _scheduleGenerationMethod = scheduleGenerationMethod;
    _couponFrequency = couponFrequency;
    _daycountFractionConvention = daycountFractionConvention;
    _businessdayAdjustmentConvention = businessdayAdjustmentConvention;
    _adjustMaturityDate = adjustMaturityDate;
    
    _notional = notional;
    _parSpread = parSpread;
    
    _valuationRecoveryRate = valuationRecoveryRate;
    _curveRecoveryRate = curveRecoveryRate;
    
    _includeAccruedPremium = includeAccruedPremium;
    
    _numberOfIntegrationSteps = numberOfIntegrationSteps;
    
    _yieldCurve = yieldCurve;
    
    // TODO : Should we build the premium cashflow schedule in the CDS ctor?
  }
  
//----------------------------------------------------------------------------------------------------------------------------------------

  public String getBuySellProtection() {
    return _buySellProtection;
  }
   
  public String getProtectionBuyer() {
    return _protectionBuyer;
  }
   
  public String getProtectionSeller() {
    return _protectionSeller;
  }
   
  public String getReferenceEntity() {
    return _referenceEntity;
  }
  
//----------------------------------------------------------------------------------------------------------------------------------------
  
  public Currency getCurrency() {
    return _currency;
  }
  
  public String getDebtSeniority() {
    return _debtSeniority;
  }
  
  public String getRestructuringClause() {
    return _restructuringClause;
  }
  
  public Calendar getCalendar() {
    return _calendar;
  }
  
//----------------------------------------------------------------------------------------------------------------------------------------
  
  public ZonedDateTime getStartDate() {
    return _startDate;
  }
  
  public ZonedDateTime getEffectiveDate() {
    return _effectiveDate;
  }
  
  public ZonedDateTime getMaturityDate() {
    return _maturityDate;
  }
  
  public ZonedDateTime getValuationDate() {
    return _valuationDate;
  }

//----------------------------------------------------------------------------------------------------------------------------------------
  
  public String getScheduleGenerationMethod() {
    return _scheduleGenerationMethod;
  }
  
  public String getCouponFrequency() {
    return _couponFrequency;
  }
  
  public String getDayCountFractionConvention() {
    return _daycountFractionConvention;
  }
  
  public String getBusinessDayAdjustmentConvention() {
    return _businessdayAdjustmentConvention;
  }
  
  public boolean getAdjustMaturityDate() {
    return _adjustMaturityDate;
  }
  
//----------------------------------------------------------------------------------------------------------------------------------------
  
  public double getNotional() {
    return _notional;
  }
  
  public double getParSpread() {
    return _parSpread;
  }
  
  public double getValuationRecoveryRate() {
    return _valuationRecoveryRate;
  }
  
  public double getCurveRecoveryRate() {
    return _curveRecoveryRate;
  }
  
  public boolean getIncludeAccruedPremium() {
    return _includeAccruedPremium;
  }

  public int getNumberOfIntegrationSteps() {
    return _numberOfIntegrationSteps;
  }

  //----------------------------------------------------------------------------------------------------------------------------------------
  
  YieldCurve getYieldCurve() {
    return _yieldCurve;
  }

//----------------------------------------------------------------------------------------------------------------------------------------
  
  // Not sure about this
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + (_adjustMaturityDate ? 1231 : 1237);
    result = prime * result + ((_businessdayAdjustmentConvention == null) ? 0 : _businessdayAdjustmentConvention.hashCode());
    result = prime * result + ((_buySellProtection == null) ? 0 : _buySellProtection.hashCode());
    result = prime * result + ((_calendar == null) ? 0 : _calendar.hashCode());
    result = prime * result + ((_couponFrequency == null) ? 0 : _couponFrequency.hashCode());
    result = prime * result + ((_currency == null) ? 0 : _currency.hashCode());
    long temp;
    temp = Double.doubleToLongBits(_curveRecoveryRate);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((_daycountFractionConvention == null) ? 0 : _daycountFractionConvention.hashCode());
    result = prime * result + ((_debtSeniority == null) ? 0 : _debtSeniority.hashCode());
    result = prime * result + ((_effectiveDate == null) ? 0 : _effectiveDate.hashCode());
    result = prime * result + (_includeAccruedPremium ? 1231 : 1237);
    result = prime * result + ((_maturityDate == null) ? 0 : _maturityDate.hashCode());
    temp = Double.doubleToLongBits(_notional);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(_parSpread);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((_protectionBuyer == null) ? 0 : _protectionBuyer.hashCode());
    result = prime * result + ((_protectionSeller == null) ? 0 : _protectionSeller.hashCode());
    result = prime * result + ((_referenceEntity == null) ? 0 : _referenceEntity.hashCode());
    result = prime * result + ((_restructuringClause == null) ? 0 : _restructuringClause.hashCode());
    result = prime * result + ((_scheduleGenerationMethod == null) ? 0 : _scheduleGenerationMethod.hashCode());
    result = prime * result + ((_startDate == null) ? 0 : _startDate.hashCode());
    result = prime * result + ((_valuationDate == null) ? 0 : _valuationDate.hashCode());
    temp = Double.doubleToLongBits(_valuationRecoveryRate);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    result = prime * result + ((_yieldCurve == null) ? 0 : _yieldCurve.hashCode());
    return result;
  }
  
//----------------------------------------------------------------------------------------------------------------------------------------

  // Not sure about this
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CreditDefaultSwapDefinition other = (CreditDefaultSwapDefinition) obj;
    if (_adjustMaturityDate != other._adjustMaturityDate)
      return false;
    if (_businessdayAdjustmentConvention == null) {
      if (other._businessdayAdjustmentConvention != null)
        return false;
    } else if (!_businessdayAdjustmentConvention.equals(other._businessdayAdjustmentConvention))
      return false;
    if (_buySellProtection == null) {
      if (other._buySellProtection != null)
        return false;
    } else if (!_buySellProtection.equals(other._buySellProtection))
      return false;
    if (_calendar == null) {
      if (other._calendar != null)
        return false;
    } else if (!_calendar.equals(other._calendar))
      return false;
    if (_couponFrequency == null) {
      if (other._couponFrequency != null)
        return false;
    } else if (!_couponFrequency.equals(other._couponFrequency))
      return false;
    if (_currency == null) {
      if (other._currency != null)
        return false;
    } else if (!_currency.equals(other._currency))
      return false;
    if (Double.doubleToLongBits(_curveRecoveryRate) != Double.doubleToLongBits(other._curveRecoveryRate))
      return false;
    if (_daycountFractionConvention == null) {
      if (other._daycountFractionConvention != null)
        return false;
    } else if (!_daycountFractionConvention.equals(other._daycountFractionConvention))
      return false;
    if (_debtSeniority == null) {
      if (other._debtSeniority != null)
        return false;
    } else if (!_debtSeniority.equals(other._debtSeniority))
      return false;
    if (_effectiveDate == null) {
      if (other._effectiveDate != null)
        return false;
    } else if (!_effectiveDate.equals(other._effectiveDate))
      return false;
    if (_includeAccruedPremium != other._includeAccruedPremium)
      return false;
    if (_maturityDate == null) {
      if (other._maturityDate != null)
        return false;
    } else if (!_maturityDate.equals(other._maturityDate))
      return false;
    if (Double.doubleToLongBits(_notional) != Double.doubleToLongBits(other._notional))
      return false;
    if (Double.doubleToLongBits(_parSpread) != Double.doubleToLongBits(other._parSpread))
      return false;
    if (_protectionBuyer == null) {
      if (other._protectionBuyer != null)
        return false;
    } else if (!_protectionBuyer.equals(other._protectionBuyer))
      return false;
    if (_protectionSeller == null) {
      if (other._protectionSeller != null)
        return false;
    } else if (!_protectionSeller.equals(other._protectionSeller))
      return false;
    if (_referenceEntity == null) {
      if (other._referenceEntity != null)
        return false;
    } else if (!_referenceEntity.equals(other._referenceEntity))
      return false;
    if (_restructuringClause == null) {
      if (other._restructuringClause != null)
        return false;
    } else if (!_restructuringClause.equals(other._restructuringClause))
      return false;
    if (_scheduleGenerationMethod == null) {
      if (other._scheduleGenerationMethod != null)
        return false;
    } else if (!_scheduleGenerationMethod.equals(other._scheduleGenerationMethod))
      return false;
    if (_startDate == null) {
      if (other._startDate != null)
        return false;
    } else if (!_startDate.equals(other._startDate))
      return false;
    if (_valuationDate == null) {
      if (other._valuationDate != null)
        return false;
    } else if (!_valuationDate.equals(other._valuationDate))
      return false;
    if (Double.doubleToLongBits(_valuationRecoveryRate) != Double.doubleToLongBits(other._valuationRecoveryRate))
      return false;
    if (_yieldCurve == null) {
      if (other._yieldCurve != null)
        return false;
    } else if (!_yieldCurve.equals(other._yieldCurve))
      return false;
    return true;
  }
  
  //---------------------------------------------------------------------------------------------------------------------------------------- 
}

//----------------------------------------------------------------------------------------------------------------------------------------