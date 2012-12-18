/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import static com.opengamma.core.id.ExternalSchemes.bloombergTickerSecurityId;
import static com.opengamma.core.id.ExternalSchemes.ricSecurityId;
import static com.opengamma.core.id.ExternalSchemes.tullettPrebonSecurityId;
import static com.opengamma.financial.convention.InMemoryConventionBundleMaster.simpleNameSecurityId;

import javax.time.calendar.Period;

import org.apache.commons.lang.Validate;

import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.analytics.ircurve.IndexType;
import com.opengamma.financial.convention.businessday.BusinessDayConvention;
import com.opengamma.financial.convention.businessday.BusinessDayConventionFactory;
import com.opengamma.financial.convention.daycount.DayCount;
import com.opengamma.financial.convention.daycount.DayCountFactory;
import com.opengamma.financial.convention.frequency.Frequency;
import com.opengamma.financial.convention.frequency.SimpleFrequencyFactory;
import com.opengamma.id.ExternalId;
import com.opengamma.id.ExternalIdBundle;

/**
 * 
 */
public class AUConventions {

  public static synchronized void addFixedIncomeInstrumentConventions(final InMemoryConventionBundleMaster conventionMaster) {
    Validate.notNull(conventionMaster, "convention master");
    final BusinessDayConvention modified = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Modified Following");
    final BusinessDayConvention following = BusinessDayConventionFactory.INSTANCE.getBusinessDayConvention("Following");
    final DayCount act365 = DayCountFactory.INSTANCE.getDayCount("Actual/365");
    final Frequency semiAnnual = SimpleFrequencyFactory.INSTANCE.getFrequency(Frequency.SEMI_ANNUAL_NAME);
    final Frequency quarterly = SimpleFrequencyFactory.INSTANCE.getFrequency(Frequency.QUARTERLY_NAME);
    final Frequency annual = SimpleFrequencyFactory.INSTANCE.getFrequency(Frequency.ANNUAL_NAME);

    //TODO holiday associated with AUD swaps is Sydney
    final ExternalId au = ExternalSchemes.financialRegionId("AU");

    final Integer overnightPublicationLag = 0;

    final ConventionBundleMasterUtils utils = new ConventionBundleMasterUtils(conventionMaster);
    // IR FUTURES
    utils.addConventionBundle(ExternalIdBundle.of(simpleNameSecurityId("AUD_IR_FUTURE")), "AUD_IR_FUTURE", act365, modified, Period.ofMonths(3), 0, true, au);

    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU00O/N Index"), simpleNameSecurityId("AUD LIBOR O/N")), "AUD LIBOR O/N", act365,
        following, Period.ofDays(1), 0, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU00S/N Index"), simpleNameSecurityId("AUD LIBOR S/N")), "AUD LIBOR S/N", act365,
        following, Period.ofDays(1), 0, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU00T/N Index"), simpleNameSecurityId("AUD LIBOR T/N")), "AUD LIBOR T/N", act365,
        following, Period.ofDays(1), 0, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0001W Index"), simpleNameSecurityId("AUD LIBOR 1w")), "AUD LIBOR 1w", act365,
        following, Period.ofDays(1), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0002W Index"), simpleNameSecurityId("AUD LIBOR 2w")), "AUD LIBOR 2w", act365,
        following, Period.ofDays(1), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0001M Index"), simpleNameSecurityId("AUD LIBOR 1m")), "AUD LIBOR 1m", act365,
        following, Period.ofMonths(1), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0002M Index"), simpleNameSecurityId("AUD LIBOR 2m")), "AUD LIBOR 2m", act365,
        following, Period.ofMonths(2), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0003M Index"), simpleNameSecurityId("AUD LIBOR 3m")), "AUD LIBOR 3m", act365,
        following, Period.ofMonths(3), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0004M Index"), simpleNameSecurityId("AUD LIBOR 4m")), "AUD LIBOR 4m", act365,
        following, Period.ofMonths(4), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0005M Index"), simpleNameSecurityId("AUD LIBOR 5m")), "AUD LIBOR 5m", act365,
        following, Period.ofMonths(5), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0006M Index"), simpleNameSecurityId("AUD LIBOR 6m")), "AUD LIBOR 6m", act365,
        following, Period.ofMonths(6), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0007M Index"), simpleNameSecurityId("AUD LIBOR 7m")), "AUD LIBOR 7m", act365,
        following, Period.ofMonths(7), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0008M Index"), simpleNameSecurityId("AUD LIBOR 8m")), "AUD LIBOR 8m", act365,
        following, Period.ofMonths(8), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0009M Index"), simpleNameSecurityId("AUD LIBOR 9m")), "AUD LIBOR 9m", act365,
        following, Period.ofMonths(9), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0010M Index"), simpleNameSecurityId("AUD LIBOR 10m")), "AUD LIBOR 10m", act365,
        following, Period.ofMonths(10), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0011M Index"), simpleNameSecurityId("AUD LIBOR 11m")), "AUD LIBOR 11m", act365,
        following, Period.ofMonths(11), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("AU0012M Index"), simpleNameSecurityId("AUD LIBOR 12m")), "AUD LIBOR 12m", act365,
        following, Period.ofMonths(12), 2, false, au);

    //TODO need to check that these are right for deposit rates
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR1T Curncy"), simpleNameSecurityId("AUD DEPOSIT 1d")), "AUD DEPOSIT 1d", act365,
        following, Period.ofDays(1), 0, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR2T Curncy"), simpleNameSecurityId("AUD DEPOSIT 2d")), "AUD DEPOSIT 2d", act365,
        following, Period.ofDays(1), 0, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR3T Curncy"), simpleNameSecurityId("AUD DEPOSIT 3d")), "AUD DEPOSIT 3d", act365,
        following, Period.ofDays(1), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR1Z Curncy"), simpleNameSecurityId("AUD DEPOSIT 1w"),
        tullettPrebonSecurityId("ASDEPAUDSPT01W")), "AUD DEPOSIT 1w", act365, following, Period.ofDays(7), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR2Z Curncy"), simpleNameSecurityId("AUD DEPOSIT 2w"),
        tullettPrebonSecurityId("ASDEPAUDSPT02W")), "AUD DEPOSIT 2w", act365, following, Period.ofDays(14), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR3Z Curncy"), simpleNameSecurityId("AUD DEPOSIT 3w"),
        tullettPrebonSecurityId("ASDEPAUDSPT03W")), "AUD DEPOSIT 3w", act365, following, Period.ofDays(21), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRA Curncy"), simpleNameSecurityId("AUD DEPOSIT 1m"),
        tullettPrebonSecurityId("ASDEPAUDSPT01M")), "AUD DEPOSIT 1m", act365, following, Period.ofMonths(1), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRB Curncy"), simpleNameSecurityId("AUD DEPOSIT 2m"),
        tullettPrebonSecurityId("ASDEPAUDSPT02M")), "AUD DEPOSIT 2m", act365, following, Period.ofMonths(2), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRC Curncy"), simpleNameSecurityId("AUD DEPOSIT 3m"),
        tullettPrebonSecurityId("ASDEPAUDSPT03M")), "AUD DEPOSIT 3m", act365, following, Period.ofMonths(3), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRD Curncy"), simpleNameSecurityId("AUD DEPOSIT 4m"),
        tullettPrebonSecurityId("ASDEPAUDSPT04M")), "AUD DEPOSIT 4m", act365, following, Period.ofMonths(4), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRE Curncy"), simpleNameSecurityId("AUD DEPOSIT 5m"),
        tullettPrebonSecurityId("ASDEPAUDSPT05M")), "AUD DEPOSIT 5m", act365, following, Period.ofMonths(5), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRF Curncy"), simpleNameSecurityId("AUD DEPOSIT 6m"),
        tullettPrebonSecurityId("ASDEPAUDSPT06M")), "AUD DEPOSIT 6m", act365, following, Period.ofMonths(6), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRG Curncy"), simpleNameSecurityId("AUD DEPOSIT 7m"),
        tullettPrebonSecurityId("ASDEPAUDSPT07M")), "AUD DEPOSIT 7m", act365, following, Period.ofMonths(7), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRH Curncy"), simpleNameSecurityId("AUD DEPOSIT 8m"),
        tullettPrebonSecurityId("ASDEPAUDSPT08M")), "AUD DEPOSIT 8m", act365, following, Period.ofMonths(8), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRI Curncy"), simpleNameSecurityId("AUD DEPOSIT 9m"),
        tullettPrebonSecurityId("ASDEPAUDSPT09M")), "AUD DEPOSIT 9m", act365, following, Period.ofMonths(9), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRJ Curncy"), simpleNameSecurityId("AUD DEPOSIT 10m"),
        tullettPrebonSecurityId("ASDEPAUDSPT10M")), "AUD DEPOSIT 10m", act365, following, Period.ofMonths(10), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDRK Curncy"), simpleNameSecurityId("AUD DEPOSIT 11m"),
        tullettPrebonSecurityId("ASDEPAUDSPT11M")), "AUD DEPOSIT 11m", act365, following, Period.ofMonths(11), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR1 Curncy"), simpleNameSecurityId("AUD DEPOSIT 1y"),
        tullettPrebonSecurityId("ASDEPAUDSPT12M")), "AUD DEPOSIT 1y", act365, following, Period.ofYears(1), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR2 Curncy"), simpleNameSecurityId("AUD DEPOSIT 2y")), "AUD DEPOSIT 2y", act365,
        following, Period.ofYears(2), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR3 Curncy"), simpleNameSecurityId("AUD DEPOSIT 3y")), "AUD DEPOSIT 3y", act365,
        following, Period.ofYears(3), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR4 Curncy"), simpleNameSecurityId("AUD DEPOSIT 4y")), "AUD DEPOSIT 4y", act365,
        following, Period.ofYears(4), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADDR5 Curncy"), simpleNameSecurityId("AUD DEPOSIT 5y")), "AUD DEPOSIT 5y", act365,
        following, Period.ofYears(5), 2, false, au);

    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADBB1M Curncy"), simpleNameSecurityId("AUD BILL 1m")), "AUD BILL 1m", act365,
        following, Period.ofMonths(1), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADBB2M Curncy"), simpleNameSecurityId("AUD BILL 2m")), "AUD BILL 2m", act365,
        following, Period.ofMonths(2), 2, false, au);
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("ADBB3M Curncy"), simpleNameSecurityId("AUD BILL 3m")), "AUD BILL 3m", act365,
        following, Period.ofMonths(3), 2, false, au);

    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("RBACOR Index"), simpleNameSecurityId("RBA OVERNIGHT CASH RATE")),
        "RBA OVERNIGHT CASH RATE", act365, following, Period.ofDays(1), 0, false, au, overnightPublicationLag);

    final DayCount swapFixedDayCount = act365;
    final BusinessDayConvention swapFixedBusinessDay = modified;

    utils.addConventionBundle(ExternalIdBundle.of(simpleNameSecurityId("AUD_SWAP")), "AUD_SWAP", act365, modified, semiAnnual, 0, au, act365,
        modified, semiAnnual, 0, simpleNameSecurityId(IndexType.BBSW + "_AUD_P6M"), au, true);

    utils.addConventionBundle(ExternalIdBundle.of(simpleNameSecurityId("AUD_3M_SWAP")), "AUD_3M_SWAP", swapFixedDayCount, swapFixedBusinessDay,
        quarterly, 0, au, act365, modified, quarterly, 0, simpleNameSecurityId(IndexType.BBSW + "_AUD_P3M"), au, true);
    // simpleNameSecurityId("AUD Bank Bill 3m")
    utils.addConventionBundle(ExternalIdBundle.of(simpleNameSecurityId("AUD_6M_SWAP")), "AUD_6M_SWAP", swapFixedDayCount, swapFixedBusinessDay,
        semiAnnual, 0, au, act365, modified, semiAnnual, 0, simpleNameSecurityId(IndexType.BBSW + "_AUD_P6M"), au, true);
    // simpleNameSecurityId("AUD Bank Bill 6m")

    utils.addConventionBundle(ExternalIdBundle.of(simpleNameSecurityId("AUD_3M_FRA")), "AUD_3M_FRA", act365, modified, quarterly, 0, au, act365,
        modified, quarterly, 0, simpleNameSecurityId(IndexType.BBSW + "_AUD_P3M"), au, true);
    utils.addConventionBundle(ExternalIdBundle.of(simpleNameSecurityId("AUD_6M_FRA")), "AUD_6M_FRA", act365, modified, semiAnnual, 0, au, act365,
        modified, semiAnnual, 0, simpleNameSecurityId(IndexType.BBSW + "_AUD_P6M"), au, true);
    utils.addConventionBundle(ExternalIdBundle.of(simpleNameSecurityId("AUD_OIS_SWAP")), "AUD_OIS_SWAP", act365, modified, annual, 0, au, act365,
        modified, annual, 0, simpleNameSecurityId("RBA OVERNIGHT CASH RATE"), au, true, overnightPublicationLag);

    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("BBSW3M Index"), ricSecurityId("AUBABSL3M=AFMA"), simpleNameSecurityId(IndexType.BBSW + "_AUD_P3M")),
        "AUD Bank Bill 3m", act365, modified, Period.ofMonths(3), 0, true, au); // "AUD Bank Bill 3m"
    utils.addConventionBundle(ExternalIdBundle.of(bloombergTickerSecurityId("BBSW6M Index"), ricSecurityId("AUBABSL6M=AFMA"), simpleNameSecurityId(IndexType.BBSW + "_AUD_P6M")),
        "AUD Bank Bill 6m", act365, modified, Period.ofMonths(6), 0, true, au); // "AUD Bank Bill 6m"
    utils.addConventionBundle(ExternalIdBundle.of(simpleNameSecurityId("AUD_SWAPTION")), "AUD_SWAPTION", false);
  }

}