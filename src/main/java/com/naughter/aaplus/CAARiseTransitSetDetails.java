/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAARiseTransitSetDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAARiseTransitSetDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAARiseTransitSetDetails obj) {
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
        AAJNI.delete_CAARiseTransitSetDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setBRiseValid(boolean value) {
    AAJNI.CAARiseTransitSetDetails_bRiseValid_set(swigCPtr, this, value);
  }

  public boolean getBRiseValid() {
    return AAJNI.CAARiseTransitSetDetails_bRiseValid_get(swigCPtr, this);
  }

  public void setRise(double value) {
    AAJNI.CAARiseTransitSetDetails_Rise_set(swigCPtr, this, value);
  }

  public double getRise() {
    return AAJNI.CAARiseTransitSetDetails_Rise_get(swigCPtr, this);
  }

  public void setBTransitValid(boolean value) {
    AAJNI.CAARiseTransitSetDetails_bTransitValid_set(swigCPtr, this, value);
  }

  public boolean getBTransitValid() {
    return AAJNI.CAARiseTransitSetDetails_bTransitValid_get(swigCPtr, this);
  }

  public void setBTransitAboveHorizon(boolean value) {
    AAJNI.CAARiseTransitSetDetails_bTransitAboveHorizon_set(swigCPtr, this, value);
  }

  public boolean getBTransitAboveHorizon() {
    return AAJNI.CAARiseTransitSetDetails_bTransitAboveHorizon_get(swigCPtr, this);
  }

  public void setTransit(double value) {
    AAJNI.CAARiseTransitSetDetails_Transit_set(swigCPtr, this, value);
  }

  public double getTransit() {
    return AAJNI.CAARiseTransitSetDetails_Transit_get(swigCPtr, this);
  }

  public void setBSetValid(boolean value) {
    AAJNI.CAARiseTransitSetDetails_bSetValid_set(swigCPtr, this, value);
  }

  public boolean getBSetValid() {
    return AAJNI.CAARiseTransitSetDetails_bSetValid_get(swigCPtr, this);
  }

  public void setSet(double value) {
    AAJNI.CAARiseTransitSetDetails_Set_set(swigCPtr, this, value);
  }

  public double getSet() {
    return AAJNI.CAARiseTransitSetDetails_Set_get(swigCPtr, this);
  }

  public CAARiseTransitSetDetails() {
    this(AAJNI.new_CAARiseTransitSetDetails(), true);
  }

}
