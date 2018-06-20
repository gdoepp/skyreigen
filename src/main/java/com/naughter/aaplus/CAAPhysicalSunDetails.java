/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAPhysicalSunDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAPhysicalSunDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAPhysicalSunDetails obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAAPhysicalSunDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAAPhysicalSunDetails() {
    this(AAJNI.new_CAAPhysicalSunDetails(), true);
  }

  public void setP(double value) {
    AAJNI.CAAPhysicalSunDetails_P_set(swigCPtr, this, value);
  }

  public double getP() {
    return AAJNI.CAAPhysicalSunDetails_P_get(swigCPtr, this);
  }

  public void setB0(double value) {
    AAJNI.CAAPhysicalSunDetails_B0_set(swigCPtr, this, value);
  }

  public double getB0() {
    return AAJNI.CAAPhysicalSunDetails_B0_get(swigCPtr, this);
  }

  public void setL0(double value) {
    AAJNI.CAAPhysicalSunDetails_L0_set(swigCPtr, this, value);
  }

  public double getL0() {
    return AAJNI.CAAPhysicalSunDetails_L0_get(swigCPtr, this);
  }

}
