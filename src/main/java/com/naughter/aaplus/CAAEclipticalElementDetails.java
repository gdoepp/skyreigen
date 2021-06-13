/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAEclipticalElementDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAEclipticalElementDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAEclipticalElementDetails obj) {
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
        AAJNI.delete_CAAEclipticalElementDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAAEclipticalElementDetails() {
    this(AAJNI.new_CAAEclipticalElementDetails__SWIG_0(), true);
  }

  public CAAEclipticalElementDetails(CAAEclipticalElementDetails arg0) {
    this(AAJNI.new_CAAEclipticalElementDetails__SWIG_1(CAAEclipticalElementDetails.getCPtr(arg0), arg0), true);
  }

  public void setI(double value) {
    AAJNI.CAAEclipticalElementDetails_i_set(swigCPtr, this, value);
  }

  public double getI() {
    return AAJNI.CAAEclipticalElementDetails_i_get(swigCPtr, this);
  }

  public void setW(double value) {
    AAJNI.CAAEclipticalElementDetails_w_set(swigCPtr, this, value);
  }

  public double getW() {
    return AAJNI.CAAEclipticalElementDetails_w_get(swigCPtr, this);
  }

  public void setOmega(double value) {
    AAJNI.CAAEclipticalElementDetails_omega_set(swigCPtr, this, value);
  }

  public double getOmega() {
    return AAJNI.CAAEclipticalElementDetails_omega_get(swigCPtr, this);
  }

}
