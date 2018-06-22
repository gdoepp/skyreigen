/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.projectpluto.astro;

public class FixedBrightnessData {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected FixedBrightnessData(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(FixedBrightnessData obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        astroJNI.delete_FixedBrightnessData(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setZenithAngMoon(double value) {
    astroJNI.FixedBrightnessData_zenithAngMoon_set(swigCPtr, this, value);
  }

  public double getZenithAngMoon() {
    return astroJNI.FixedBrightnessData_zenithAngMoon_get(swigCPtr, this);
  }

  public void setZenithAngSun(double value) {
    astroJNI.FixedBrightnessData_zenithAngSun_set(swigCPtr, this, value);
  }

  public double getZenithAngSun() {
    return astroJNI.FixedBrightnessData_zenithAngSun_get(swigCPtr, this);
  }

  public void setMoonElongation(double value) {
    astroJNI.FixedBrightnessData_moonElongation_set(swigCPtr, this, value);
  }

  public double getMoonElongation() {
    return astroJNI.FixedBrightnessData_moonElongation_get(swigCPtr, this);
  }

  public void setHtAboveSeaInMeters(double value) {
    astroJNI.FixedBrightnessData_htAboveSeaInMeters_set(swigCPtr, this, value);
  }

  public double getHtAboveSeaInMeters() {
    return astroJNI.FixedBrightnessData_htAboveSeaInMeters_get(swigCPtr, this);
  }

  public void setLatitude(double value) {
    astroJNI.FixedBrightnessData_latitude_set(swigCPtr, this, value);
  }

  public double getLatitude() {
    return astroJNI.FixedBrightnessData_latitude_get(swigCPtr, this);
  }

  public void setTemperatureInC(double value) {
    astroJNI.FixedBrightnessData_temperatureInC_set(swigCPtr, this, value);
  }

  public double getTemperatureInC() {
    return astroJNI.FixedBrightnessData_temperatureInC_get(swigCPtr, this);
  }

  public void setRelativeHumidity(double value) {
    astroJNI.FixedBrightnessData_relativeHumidity_set(swigCPtr, this, value);
  }

  public double getRelativeHumidity() {
    return astroJNI.FixedBrightnessData_relativeHumidity_get(swigCPtr, this);
  }

  public void setYear(double value) {
    astroJNI.FixedBrightnessData_year_set(swigCPtr, this, value);
  }

  public double getYear() {
    return astroJNI.FixedBrightnessData_year_get(swigCPtr, this);
  }

  public void setMonth(double value) {
    astroJNI.FixedBrightnessData_month_set(swigCPtr, this, value);
  }

  public double getMonth() {
    return astroJNI.FixedBrightnessData_month_get(swigCPtr, this);
  }

  public FixedBrightnessData() {
    this(astroJNI.new_FixedBrightnessData(), true);
  }

}
