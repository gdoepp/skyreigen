/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAATopocentricEclipticDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAATopocentricEclipticDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAATopocentricEclipticDetails obj) {
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
        AAJNI.delete_CAATopocentricEclipticDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAATopocentricEclipticDetails() {
    this(AAJNI.new_CAATopocentricEclipticDetails__SWIG_0(), true);
  }

  public CAATopocentricEclipticDetails(CAATopocentricEclipticDetails arg0) {
    this(AAJNI.new_CAATopocentricEclipticDetails__SWIG_1(CAATopocentricEclipticDetails.getCPtr(arg0), arg0), true);
  }

  public void setLambda(double value) {
    AAJNI.CAATopocentricEclipticDetails_Lambda_set(swigCPtr, this, value);
  }

  public double getLambda() {
    return AAJNI.CAATopocentricEclipticDetails_Lambda_get(swigCPtr, this);
  }

  public void setBeta(double value) {
    AAJNI.CAATopocentricEclipticDetails_Beta_set(swigCPtr, this, value);
  }

  public double getBeta() {
    return AAJNI.CAATopocentricEclipticDetails_Beta_get(swigCPtr, this);
  }

  public void setSemidiameter(double value) {
    AAJNI.CAATopocentricEclipticDetails_Semidiameter_set(swigCPtr, this, value);
  }

  public double getSemidiameter() {
    return AAJNI.CAATopocentricEclipticDetails_Semidiameter_get(swigCPtr, this);
  }

}
