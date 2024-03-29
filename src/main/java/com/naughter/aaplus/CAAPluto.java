/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAPluto {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAPluto(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAPluto obj) {
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
        AAJNI.delete_CAAPluto(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double EclipticLongitude(double JD) {
    return AAJNI.CAAPluto_EclipticLongitude(JD);
  }

  public static double EclipticLatitude(double JD) {
    return AAJNI.CAAPluto_EclipticLatitude(JD);
  }

  public static double RadiusVector(double JD) {
    return AAJNI.CAAPluto_RadiusVector(JD);
  }

  public CAAPluto() {
    this(AAJNI.new_CAAPluto(), true);
  }

}
