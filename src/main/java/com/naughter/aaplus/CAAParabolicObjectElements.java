/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAParabolicObjectElements {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAParabolicObjectElements(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAParabolicObjectElements obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAAParabolicObjectElements(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAAParabolicObjectElements() {
    this(AAJNI.new_CAAParabolicObjectElements(), true);
  }

  public void setQ(double value) {
    AAJNI.CAAParabolicObjectElements_q_set(swigCPtr, this, value);
  }

  public double getQ() {
    return AAJNI.CAAParabolicObjectElements_q_get(swigCPtr, this);
  }

  public void setI(double value) {
    AAJNI.CAAParabolicObjectElements_i_set(swigCPtr, this, value);
  }

  public double getI() {
    return AAJNI.CAAParabolicObjectElements_i_get(swigCPtr, this);
  }

  public void setW(double value) {
    AAJNI.CAAParabolicObjectElements_w_set(swigCPtr, this, value);
  }

  public double getW() {
    return AAJNI.CAAParabolicObjectElements_w_get(swigCPtr, this);
  }

  public void setOmega(double value) {
    AAJNI.CAAParabolicObjectElements_omega_set(swigCPtr, this, value);
  }

  public double getOmega() {
    return AAJNI.CAAParabolicObjectElements_omega_get(swigCPtr, this);
  }

  public void setJDEquinox(double value) {
    AAJNI.CAAParabolicObjectElements_JDEquinox_set(swigCPtr, this, value);
  }

  public double getJDEquinox() {
    return AAJNI.CAAParabolicObjectElements_JDEquinox_get(swigCPtr, this);
  }

  public void setT(double value) {
    AAJNI.CAAParabolicObjectElements_T_set(swigCPtr, this, value);
  }

  public double getT() {
    return AAJNI.CAAParabolicObjectElements_T_get(swigCPtr, this);
  }

}
