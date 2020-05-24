/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAMoonPerigeeApogee {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAMoonPerigeeApogee(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAMoonPerigeeApogee obj) {
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
        AAJNI.delete_CAAMoonPerigeeApogee(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double K(double Year) {
    return AAJNI.CAAMoonPerigeeApogee_K(Year);
  }

  public static double MeanPerigee(double k) {
    return AAJNI.CAAMoonPerigeeApogee_MeanPerigee(k);
  }

  public static double MeanApogee(double k) {
    return AAJNI.CAAMoonPerigeeApogee_MeanApogee(k);
  }

  public static double TruePerigee(double k) {
    return AAJNI.CAAMoonPerigeeApogee_TruePerigee(k);
  }

  public static double TrueApogee(double k) {
    return AAJNI.CAAMoonPerigeeApogee_TrueApogee(k);
  }

  public static double PerigeeParallax(double k) {
    return AAJNI.CAAMoonPerigeeApogee_PerigeeParallax(k);
  }

  public static double ApogeeParallax(double k) {
    return AAJNI.CAAMoonPerigeeApogee_ApogeeParallax(k);
  }

  public CAAMoonPerigeeApogee() {
    this(AAJNI.new_CAAMoonPerigeeApogee(), true);
  }

}
