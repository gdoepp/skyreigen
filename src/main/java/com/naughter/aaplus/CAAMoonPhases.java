/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAMoonPhases {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAMoonPhases(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAMoonPhases obj) {
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
        AAJNI.delete_CAAMoonPhases(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double K(double Year) {
    return AAJNI.CAAMoonPhases_K(Year);
  }

  public static double MeanPhase(double k) {
    return AAJNI.CAAMoonPhases_MeanPhase(k);
  }

  public static double TruePhase(double k) {
    return AAJNI.CAAMoonPhases_TruePhase(k);
  }

  public CAAMoonPhases() {
    this(AAJNI.new_CAAMoonPhases(), true);
  }

}
