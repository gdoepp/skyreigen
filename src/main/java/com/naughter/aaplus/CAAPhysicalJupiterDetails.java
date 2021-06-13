/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAPhysicalJupiterDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAPhysicalJupiterDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAPhysicalJupiterDetails obj) {
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
        AAJNI.delete_CAAPhysicalJupiterDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAAPhysicalJupiterDetails() {
    this(AAJNI.new_CAAPhysicalJupiterDetails__SWIG_0(), true);
  }

  public CAAPhysicalJupiterDetails(CAAPhysicalJupiterDetails arg0) {
    this(AAJNI.new_CAAPhysicalJupiterDetails__SWIG_1(CAAPhysicalJupiterDetails.getCPtr(arg0), arg0), true);
  }

  public void setDE(double value) {
    AAJNI.CAAPhysicalJupiterDetails_DE_set(swigCPtr, this, value);
  }

  public double getDE() {
    return AAJNI.CAAPhysicalJupiterDetails_DE_get(swigCPtr, this);
  }

  public void setDS(double value) {
    AAJNI.CAAPhysicalJupiterDetails_DS_set(swigCPtr, this, value);
  }

  public double getDS() {
    return AAJNI.CAAPhysicalJupiterDetails_DS_get(swigCPtr, this);
  }

  public void setGeometricw1(double value) {
    AAJNI.CAAPhysicalJupiterDetails_Geometricw1_set(swigCPtr, this, value);
  }

  public double getGeometricw1() {
    return AAJNI.CAAPhysicalJupiterDetails_Geometricw1_get(swigCPtr, this);
  }

  public void setGeometricw2(double value) {
    AAJNI.CAAPhysicalJupiterDetails_Geometricw2_set(swigCPtr, this, value);
  }

  public double getGeometricw2() {
    return AAJNI.CAAPhysicalJupiterDetails_Geometricw2_get(swigCPtr, this);
  }

  public void setApparentw1(double value) {
    AAJNI.CAAPhysicalJupiterDetails_Apparentw1_set(swigCPtr, this, value);
  }

  public double getApparentw1() {
    return AAJNI.CAAPhysicalJupiterDetails_Apparentw1_get(swigCPtr, this);
  }

  public void setApparentw2(double value) {
    AAJNI.CAAPhysicalJupiterDetails_Apparentw2_set(swigCPtr, this, value);
  }

  public double getApparentw2() {
    return AAJNI.CAAPhysicalJupiterDetails_Apparentw2_get(swigCPtr, this);
  }

  public void setP(double value) {
    AAJNI.CAAPhysicalJupiterDetails_P_set(swigCPtr, this, value);
  }

  public double getP() {
    return AAJNI.CAAPhysicalJupiterDetails_P_get(swigCPtr, this);
  }

}
