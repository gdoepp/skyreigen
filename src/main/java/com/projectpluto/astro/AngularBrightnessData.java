/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.projectpluto.astro;

public class AngularBrightnessData {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected AngularBrightnessData(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(AngularBrightnessData obj) {
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
        astroJNI.delete_AngularBrightnessData(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setZenithAngle(double value) {
    astroJNI.AngularBrightnessData_zenithAngle_set(swigCPtr, this, value);
  }

  public double getZenithAngle() {
    return astroJNI.AngularBrightnessData_zenithAngle_get(swigCPtr, this);
  }

  public void setDistMoon(double value) {
    astroJNI.AngularBrightnessData_distMoon_set(swigCPtr, this, value);
  }

  public double getDistMoon() {
    return astroJNI.AngularBrightnessData_distMoon_get(swigCPtr, this);
  }

  public void setDistSun(double value) {
    astroJNI.AngularBrightnessData_distSun_set(swigCPtr, this, value);
  }

  public double getDistSun() {
    return astroJNI.AngularBrightnessData_distSun_get(swigCPtr, this);
  }

  public AngularBrightnessData() {
    this(astroJNI.new_AngularBrightnessData(), true);
  }

}
