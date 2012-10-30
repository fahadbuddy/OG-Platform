/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAinterfaces;

import com.opengamma.maths.highlevelapi.datatypes.primitive.OGArray;

/**
 * DOGMA is the OpenGamma Maths Assembly language. 
 * It's rather like Octave, but due to the lack of operator overloading and syntax definition there are compromises!
 */
public interface DOGMAArithmeticAPI {

  /* ADD */
  /**
   * Adds OGArray arrays.
   * If more than two arrays are given the additions are applied from left to right and accumulated.
   * e.g. (((array[0]+array[1])+array[2])+array[3])... 
   * @param array the arrays to add together
   * @return the sum of the arrays  
   */
  OGArray<? extends Number> plus(OGArray<? extends Number>... array);

  /**
   * Adds two OGArraySuper<? extends Number> arrays
   * @param array1 the first array
   * @param array2 the second array
   * @return the sum of the arrays 
   */
  OGArray<? extends Number> plus(OGArray<? extends Number> array1, OGArray<? extends Number> array2);
  
  /**
   * Adds a number to an OGArraySuper<? extends Number> array
   * @param array1 the first array
   * @param number the number
   * @return array1 plus a number
   */
  OGArray<? extends Number> plus(OGArray<? extends Number> array1, double number);
  
  /**
   * Adds a number to an OGArraySuper<? extends Number> array
   * @param array1 the first array
   * @param number the number
   * @return array1 plus a number 
   */
  OGArray<? extends Number> plus(OGArray<? extends Number> array1, int number);  
  

  /* SUBTRACT */
  /**
   * Subtracts OGArray arrays.
   * If more than two arrays are given the additions are applied from left to right and accumulated.
   * e.g. (((array[0]-array[1])-array[2])-array[3])... 
   * @param array the arrays to subtract
   * @return array1 - array2 
   */
  OGArray<? extends Number> minus(OGArray<? extends Number>... array);

  /**
   * Subtracts an OGArraySuper<? extends Number> from an OGSparseArray
   * @param array1 the first array
   * @param array2 the second array
   * @return array1 - array2
   */
  OGArray<? extends Number> minus(OGArray<? extends Number> array1, OGArray<? extends Number> array2);
  
  /**
   * Subtracts a number to an OGArraySuper<? extends Number> array
   * @param array1 the first array
   * @param number the number
   * @return array1 minus a number
   */
  OGArray<? extends Number> minus(OGArray<? extends Number> array1, double number);
  
  /**
   * Subtracts a number to an OGArraySuper<? extends Number> array
   * @param array1 the first array
   * @param number the number
   * @return array1 minus a number 
   */
  OGArray<? extends Number> minus(OGArray<? extends Number> array1, int number);    

  /* LDIVIDE */
  /**
   * Element by element left division
   * @param array1 the first array
   * @param array2 the second array
   * @return array1 ldivided element-wise by array2. i.e. array1.\array2
   */
  OGArray<? extends Number> ldivide(OGArray<? extends Number> array1, OGArray<? extends Number> array2);

  /**
   * Matrix left division
   * @param array1 the first array
   * @param array2 the second array
   * @return array1\array2
   */
  OGArray<? extends Number> mldivide(OGArray<? extends Number> array1, OGArray<? extends Number> array2);

  
  /* RDIVIDE */
  /**
   * Element by element right division
   * @param array1 the first array
   * @param array2 the second array
   * @return array1 rdivided element-wise by array2. i.e. array1./array2
   */
  OGArray<? extends Number> rdivide(OGArray<? extends Number> array1, OGArray<? extends Number> array2);

  /**
   * Element by element right division
   * @param array1 the first array
   * @param number the number
   * @return array1 rdivided element-wise by a number. i.e. array1./number
   */
  OGArray<? extends Number> rdivide(OGArray<? extends Number> array1, double number);  

  /**
   * Element by element right division
   * @param array1 the first array
   * @param number the number
   * @return array1 rdivided element-wise by a number. i.e. array1./number
   */
  OGArray<? extends Number> rdivide(OGArray<? extends Number> array1, int number);  

  /**
   * Element by element right division
   * @param number the number
   * @param array1 the first array 
   * @return number is broadcast and then rdivided element-wise by array1. i.e. number/array1
   */
  OGArray<? extends Number> rdivide(double number, OGArray<? extends Number> array1);  

  /**
   * Element by element right division
   * @param number the number
   * @param array1 the first array
   * @return array1 is broadcast and then rdivided element-wise by array1
   */
  OGArray<? extends Number> rdivide(int number, OGArray<? extends Number> array1);    
  
  
  /* MRDIVIDE */
  /**
   * Right division, which effectively returns x = inverse(A)*b
   * If A is not square a minimum norm solution is returned.
   * @param matrixA the first array
   * @param vectorb the second array
   * @return A\b
   */
  OGArray<? extends Number> mrdivide(OGArray<? extends Number> matrixA, OGArray<? extends Number> vectorb);

  /* TIMES */
  /**
   * Element wise multiplication. For more than two inputs it returns the multiplications
   * applied and accumulated from left to right, e.g.
   * (((array[0]*array[1])*array[2])*array[3])... 
   * @param array the arrays to multiply
   * @return the cumulative product of array multiplication
   */
  OGArray<? extends Number> times(OGArray<? extends Number>... array);

  /**
   * Element wise multiplication. of two OGArray arrays
   * @param array1 the first array
   * @param array2 the second array
   * @return the element wise product of the arrays 
   */
  OGArray<? extends Number> times(OGArray<? extends Number> array1, OGArray<? extends Number> array2);

  /**
   * Element wise multiplication of OGArray with a number
   * @param array1 the first array
   * @param number a number
   * @return the element wise product of the arrays with the number 
   */
  OGArray<? extends Number> times(OGArray<? extends Number> array1, double number);
  
  /**
   * Element wise multiplication of OGArray with a number
   * @param array1 the first array
   * @param number a number
   * @return the element wise product of the arrays with the number 
   */
  OGArray<? extends Number> times(OGArray<? extends Number> array1, int number);
  
  /**
   * Element wise multiplication of OGArray with a number
   * @param number a number
   * @param array1 the first array
   * @return the element wise product of the arrays with the number 
   */
  OGArray<? extends Number> times(double number, OGArray<? extends Number> array1);

  /**
   * Element wise multiplication of OGArray with a number
   * @param number a number
   * @param array1 the first array
   * @return the element wise product of the arrays with the number 
   */
  OGArray<? extends Number> times(int number, OGArray<? extends Number> array1);  
  
  /* MTIMES */
  /**
   * Matrix multiplication. For more two inputs it returns the multiplications
   * applied and accumulated from left to right, e.g.
   * array1*array2 
   * @param array1 the first array
   * @param array2 the second array 
   * @return the cumulative product of array multiplication
   */
  OGArray<? extends Number> mtimes(OGArray<? extends Number> array1, OGArray<? extends Number> array2);
  
  /**
   * Matrix multiplication. For more than two inputs it returns the multiplications
   * applied and accumulated from left to right, e.g.
   * (((array[0]*array[1])*array[2])*array[3])... 
   * @param array the arrays to multiply
   * @return the cumulative product of array multiplication
   */
  OGArray<? extends Number> mtimes(OGArray<? extends Number>... array);

  /* POWER */
  /**
   * Element by element raise to a power
   * @param array1 the array
   * @param array2 scalar the power
   * @return array1 raised element by element to the power array2
   */
  OGArray<? extends Number> power(OGArray<? extends Number> array1, OGArray<? extends Number> array2);

  /**
   * Raise a matrix to a power
   * @param array1 the first array
   * @param array2 scalar
   * @return array1 raised to the power array2
   */
  OGArray<? extends Number> mpower(OGArray<? extends Number> array1, OGArray<? extends Number> array2);

  /* TRANSPOSE */
  /**
   * Transpose of an array
   * @param array the array to transpose
   * @return the transpose of the array
   */
  OGArray<? extends Number> transpose(OGArray<? extends Number> array);


}