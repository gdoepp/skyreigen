/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAASaturnRingDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAASaturnRingDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAASaturnRingDetails obj) {
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
        AAJNI.delete_CAASaturnRingDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setB2(double value) {
    AAJNI.CAASaturnRingDetails_B_set(swigCPtr, this, value);
  }

  public double getB2() {
    return AAJNI.CAASaturnRingDetails_B_get(swigCPtr, this);
  }

  public void setBdash(double value) {
    AAJNI.CAASaturnRingDetails_Bdash_set(swigCPtr, this, value);
  }

  public double getBdash() {
    return AAJNI.CAASaturnRingDetails_Bdash_get(swigCPtr, this);
  }

  public void setP(double value) {
    AAJNI.CAASaturnRingDetails_P_set(swigCPtr, this, value);
  }

  public double getP() {
    return AAJNI.CAASaturnRingDetails_P_get(swigCPtr, this);
  }

  public void setA(double value) {
    AAJNI.CAASaturnRingDetails_a_set(swigCPtr, this, value);
  }

  public double getA() {
    return AAJNI.CAASaturnRingDetails_a_get(swigCPtr, this);
  }

  public void setB(double value) {
    AAJNI.CAASaturnRingDetails_b_set(swigCPtr, this, value);
  }

  public double getB() {
    return AAJNI.CAASaturnRingDetails_b_get(swigCPtr, this);
  }

  public void setDeltaU(double value) {
    AAJNI.CAASaturnRingDetails_DeltaU_set(swigCPtr, this, value);
  }

  public double getDeltaU() {
    return AAJNI.CAASaturnRingDetails_DeltaU_get(swigCPtr, this);
  }

  public void setU1(double value) {
    AAJNI.CAASaturnRingDetails_U1_set(swigCPtr, this, value);
  }

  public double getU1() {
    return AAJNI.CAASaturnRingDetails_U1_get(swigCPtr, this);
  }

  public void setU2(double value) {
    AAJNI.CAASaturnRingDetails_U2_set(swigCPtr, this, value);
  }

  public double getU2() {
    return AAJNI.CAASaturnRingDetails_U2_get(swigCPtr, this);
  }

  public CAASaturnRingDetails() {
    this(AAJNI.new_CAASaturnRingDetails(), true);
  }

}
