/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic.rdivide;

import com.opengamma.maths.highlevelapi.datatypes.primitive.OGArray;

/**
 * Does rdivide
 * 
 * @param <T> An OGArray type
 * @param <S> An OGArray type
 */
public interface RdivideAbstract<T extends OGArray<? extends Number>, S extends OGArray<? extends Number>> {
  
  OGArray<? extends Number> rdivide(T array1, S array2);
}