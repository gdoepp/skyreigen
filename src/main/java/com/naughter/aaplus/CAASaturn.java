/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAASaturn {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAASaturn(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAASaturn obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  @SuppressWarnings("deprecation")
  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAASaturn(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double EclipticLongitude(double JD, boolean bHighPrecision) {
    return AAJNI.CAASaturn_EclipticLongitude(JD, bHighPrecision);
  }

  public static double EclipticLatitude(double JD, boolean bHighPrecision) {
    return AAJNI.CAASaturn_EclipticLatitude(JD, bHighPrecision);
  }

  public static double RadiusVector(double JD, boolean bHighPrecision) {
    return AAJNI.CAASaturn_RadiusVector(JD, bHighPrecision);
  }

  public CAASaturn() {
    this(AAJNI.new_CAASaturn(), true);
  }

}
