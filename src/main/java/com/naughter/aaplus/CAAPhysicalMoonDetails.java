/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAPhysicalMoonDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAPhysicalMoonDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAPhysicalMoonDetails obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAAPhysicalMoonDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAAPhysicalMoonDetails() {
    this(AAJNI.new_CAAPhysicalMoonDetails(), true);
  }

  public void setLdash(double value) {
    AAJNI.CAAPhysicalMoonDetails_ldash_set(swigCPtr, this, value);
  }

  public double getLdash() {
    return AAJNI.CAAPhysicalMoonDetails_ldash_get(swigCPtr, this);
  }

  public void setBdash(double value) {
    AAJNI.CAAPhysicalMoonDetails_bdash_set(swigCPtr, this, value);
  }

  public double getBdash() {
    return AAJNI.CAAPhysicalMoonDetails_bdash_get(swigCPtr, this);
  }

  public void setLdash2(double value) {
    AAJNI.CAAPhysicalMoonDetails_ldash2_set(swigCPtr, this, value);
  }

  public double getLdash2() {
    return AAJNI.CAAPhysicalMoonDetails_ldash2_get(swigCPtr, this);
  }

  public void setBdash2(double value) {
    AAJNI.CAAPhysicalMoonDetails_bdash2_set(swigCPtr, this, value);
  }

  public double getBdash2() {
    return AAJNI.CAAPhysicalMoonDetails_bdash2_get(swigCPtr, this);
  }

  public void setL(double value) {
    AAJNI.CAAPhysicalMoonDetails_l_set(swigCPtr, this, value);
  }

  public double getL() {
    return AAJNI.CAAPhysicalMoonDetails_l_get(swigCPtr, this);
  }

  public void setB(double value) {
    AAJNI.CAAPhysicalMoonDetails_b_set(swigCPtr, this, value);
  }

  public double getB() {
    return AAJNI.CAAPhysicalMoonDetails_b_get(swigCPtr, this);
  }

  public void setP(double value) {
    AAJNI.CAAPhysicalMoonDetails_P_set(swigCPtr, this, value);
  }

  public double getP() {
    return AAJNI.CAAPhysicalMoonDetails_P_get(swigCPtr, this);
  }

}
