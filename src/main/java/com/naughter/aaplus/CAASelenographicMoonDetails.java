/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAASelenographicMoonDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAASelenographicMoonDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAASelenographicMoonDetails obj) {
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
        AAJNI.delete_CAASelenographicMoonDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAASelenographicMoonDetails() {
    this(AAJNI.new_CAASelenographicMoonDetails__SWIG_0(), true);
  }

  public CAASelenographicMoonDetails(CAASelenographicMoonDetails arg0) {
    this(AAJNI.new_CAASelenographicMoonDetails__SWIG_1(CAASelenographicMoonDetails.getCPtr(arg0), arg0), true);
  }

  public void setL0(double value) {
    AAJNI.CAASelenographicMoonDetails_l0_set(swigCPtr, this, value);
  }

  public double getL0() {
    return AAJNI.CAASelenographicMoonDetails_l0_get(swigCPtr, this);
  }

  public void setB0(double value) {
    AAJNI.CAASelenographicMoonDetails_b0_set(swigCPtr, this, value);
  }

  public double getB0() {
    return AAJNI.CAASelenographicMoonDetails_b0_get(swigCPtr, this);
  }

  public void setC0(double value) {
    AAJNI.CAASelenographicMoonDetails_c0_set(swigCPtr, this, value);
  }

  public double getC0() {
    return AAJNI.CAASelenographicMoonDetails_c0_get(swigCPtr, this);
  }

}
