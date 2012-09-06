/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import static org.testng.AssertJUnit.assertEquals;

import javax.time.calendar.LocalDate;

import org.testng.annotations.Test;

import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;

/**
 *
 */
public class IMMFutureAndFutureOptionQuarterlyExpiryCalculatorTest {
  private static final IMMFutureAndFutureOptionQuarterlyExpiryCalculator CALCULATOR = IMMFutureAndFutureOptionQuarterlyExpiryCalculator.getInstance();
  static final Calendar WEEKEND_CALENDAR = new MondayToFridayCalendar("a");
  private static final Calendar CALENDAR = new MyCalendar();
  private static final LocalDate AUGUST = LocalDate.of(2012, 8, 1);
  private static final LocalDate SEPTEMBER_START = LocalDate.of(2012, 9, 1);
  private static final LocalDate SEPTEMBER_END = LocalDate.of(2012, 9, 29);

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNegativeN() {
    CALCULATOR.getExpiry(-1, AUGUST, CALENDAR);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testZeroN() {
    CALCULATOR.getExpiry(0, AUGUST, CALENDAR);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullDate() {
    CALCULATOR.getExpiry(1, null, CALENDAR);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullCalendar() {
    CALCULATOR.getExpiry(2, AUGUST, null);
  }

  @Test
  public void testDifferentMonth() {
    assertEquals(LocalDate.of(2012, 9, 17), CALCULATOR.getExpiry(1, AUGUST, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 9, 17), CALCULATOR.getExpiry(1, AUGUST, CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 17), CALCULATOR.getExpiry(2, AUGUST, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 14), CALCULATOR.getExpiry(2, AUGUST, CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 18), CALCULATOR.getExpiry(3, AUGUST, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 18), CALCULATOR.getExpiry(3, AUGUST, CALENDAR));
    assertEquals(LocalDate.of(2013, 6, 17), CALCULATOR.getExpiry(4, AUGUST, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 6, 17), CALCULATOR.getExpiry(4, AUGUST, CALENDAR));
    assertEquals(LocalDate.of(2013, 9, 16), CALCULATOR.getExpiry(5, AUGUST, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 9, 16), CALCULATOR.getExpiry(5, AUGUST, CALENDAR));
    assertEquals(LocalDate.of(2013, 12, 16), CALCULATOR.getExpiry(6, AUGUST, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 12, 16), CALCULATOR.getExpiry(6, AUGUST, CALENDAR));
  }

  @Test
  public void testExpiryMonthBeforeExpiry() {
    assertEquals(LocalDate.of(2012, 9, 17), CALCULATOR.getExpiry(1, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 9, 17), CALCULATOR.getExpiry(1, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 17), CALCULATOR.getExpiry(2, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 14), CALCULATOR.getExpiry(2, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 18), CALCULATOR.getExpiry(3, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 18), CALCULATOR.getExpiry(3, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2013, 6, 17), CALCULATOR.getExpiry(4, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 6, 17), CALCULATOR.getExpiry(4, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2013, 9, 16), CALCULATOR.getExpiry(5, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 9, 16), CALCULATOR.getExpiry(5, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2013, 12, 16), CALCULATOR.getExpiry(6, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 12, 16), CALCULATOR.getExpiry(6, SEPTEMBER_START, CALENDAR));
  }

  @Test
  public void testExpiryMonthAfterExpiry() {
    assertEquals(LocalDate.of(2012, 12, 17), CALCULATOR.getExpiry(1, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 14), CALCULATOR.getExpiry(1, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 18), CALCULATOR.getExpiry(2, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 18), CALCULATOR.getExpiry(2, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2013, 6, 17), CALCULATOR.getExpiry(3, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 6, 17), CALCULATOR.getExpiry(3, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2013, 9, 16), CALCULATOR.getExpiry(4, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 9, 16), CALCULATOR.getExpiry(4, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2013, 12, 16), CALCULATOR.getExpiry(5, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 12, 16), CALCULATOR.getExpiry(5, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2014, 3, 17), CALCULATOR.getExpiry(6, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2014, 3, 17), CALCULATOR.getExpiry(6, SEPTEMBER_END, CALENDAR));
  }

  private static class MyCalendar implements Calendar {
    private static final LocalDate BANK_HOLIDAY = LocalDate.of(2012, 12, 17);

    public MyCalendar() {
    }

    @Override
    public boolean isWorkingDay(final LocalDate date) {
      if (date.equals(BANK_HOLIDAY)) {
        return false;
      }
      return WEEKEND_CALENDAR.isWorkingDay(date);
    }

    @Override
    public String getConventionName() {
      return null;
    }

  }
}