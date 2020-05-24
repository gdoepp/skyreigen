/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAADynamicalTime {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAADynamicalTime(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAADynamicalTime obj) {
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
        AAJNI.delete_CAADynamicalTime(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double DeltaT(double JD) {
    return AAJNI.CAADynamicalTime_DeltaT(JD);
  }

  public static double CumulativeLeapSeconds(double JD) {
    return AAJNI.CAADynamicalTime_CumulativeLeapSeconds(JD);
  }

  public static double TT2UTC(double JD) {
    return AAJNI.CAADynamicalTime_TT2UTC(JD);
  }

  public static double UTC2TT(double JD) {
    return AAJNI.CAADynamicalTime_UTC2TT(JD);
  }

  public static double TT2TAI(double JD) {
    return AAJNI.CAADynamicalTime_TT2TAI(JD);
  }

  public static double TAI2TT(double JD) {
    return AAJNI.CAADynamicalTime_TAI2TT(JD);
  }

  public static double TT2UT1(double JD) {
    return AAJNI.CAADynamicalTime_TT2UT1(JD);
  }

  public static double UT12TT(double JD) {
    return AAJNI.CAADynamicalTime_UT12TT(JD);
  }

  public static double UT1MinusUTC(double JD) {
    return AAJNI.CAADynamicalTime_UT1MinusUTC(JD);
  }

  public CAADynamicalTime() {
    this(AAJNI.new_CAADynamicalTime(), true);
  }

}
