/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAANearParabolicObjectDetails {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAANearParabolicObjectDetails(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAANearParabolicObjectDetails obj) {
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
        AAJNI.delete_CAANearParabolicObjectDetails(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAANearParabolicObjectDetails() {
    this(AAJNI.new_CAANearParabolicObjectDetails__SWIG_0(), true);
  }

  public CAANearParabolicObjectDetails(CAANearParabolicObjectDetails arg0) {
    this(AAJNI.new_CAANearParabolicObjectDetails__SWIG_1(CAANearParabolicObjectDetails.getCPtr(arg0), arg0), true);
  }

  public void setHeliocentricRectangularEquatorial(CAA3DCoordinate value) {
    AAJNI.CAANearParabolicObjectDetails_HeliocentricRectangularEquatorial_set(swigCPtr, this, CAA3DCoordinate.getCPtr(value), value);
  }

  public CAA3DCoordinate getHeliocentricRectangularEquatorial() {
    long cPtr = AAJNI.CAANearParabolicObjectDetails_HeliocentricRectangularEquatorial_get(swigCPtr, this);
    return (cPtr == 0) ? null : new CAA3DCoordinate(cPtr, false);
  }

  public void setHeliocentricRectangularEcliptical(CAA3DCoordinate value) {
    AAJNI.CAANearParabolicObjectDetails_HeliocentricRectangularEcliptical_set(swigCPtr, this, CAA3DCoordinate.getCPtr(value), value);
  }

  public CAA3DCoordinate getHeliocentricRectangularEcliptical() {
    long cPtr = AAJNI.CAANearParabolicObjectDetails_HeliocentricRectangularEcliptical_get(swigCPtr, this);
    return (cPtr == 0) ? null : new CAA3DCoordinate(cPtr, false);
  }

  public void setHeliocentricEclipticLongitude(double value) {
    AAJNI.CAANearParabolicObjectDetails_HeliocentricEclipticLongitude_set(swigCPtr, this, value);
  }

  public double getHeliocentricEclipticLongitude() {
    return AAJNI.CAANearParabolicObjectDetails_HeliocentricEclipticLongitude_get(swigCPtr, this);
  }

  public void setHeliocentricEclipticLatitude(double value) {
    AAJNI.CAANearParabolicObjectDetails_HeliocentricEclipticLatitude_set(swigCPtr, this, value);
  }

  public double getHeliocentricEclipticLatitude() {
    return AAJNI.CAANearParabolicObjectDetails_HeliocentricEclipticLatitude_get(swigCPtr, this);
  }

  public void setTrueGeocentricRA(double value) {
    AAJNI.CAANearParabolicObjectDetails_TrueGeocentricRA_set(swigCPtr, this, value);
  }

  public double getTrueGeocentricRA() {
    return AAJNI.CAANearParabolicObjectDetails_TrueGeocentricRA_get(swigCPtr, this);
  }

  public void setTrueGeocentricDeclination(double value) {
    AAJNI.CAANearParabolicObjectDetails_TrueGeocentricDeclination_set(swigCPtr, this, value);
  }

  public double getTrueGeocentricDeclination() {
    return AAJNI.CAANearParabolicObjectDetails_TrueGeocentricDeclination_get(swigCPtr, this);
  }

  public void setTrueGeocentricDistance(double value) {
    AAJNI.CAANearParabolicObjectDetails_TrueGeocentricDistance_set(swigCPtr, this, value);
  }

  public double getTrueGeocentricDistance() {
    return AAJNI.CAANearParabolicObjectDetails_TrueGeocentricDistance_get(swigCPtr, this);
  }

  public void setTrueGeocentricLightTime(double value) {
    AAJNI.CAANearParabolicObjectDetails_TrueGeocentricLightTime_set(swigCPtr, this, value);
  }

  public double getTrueGeocentricLightTime() {
    return AAJNI.CAANearParabolicObjectDetails_TrueGeocentricLightTime_get(swigCPtr, this);
  }

  public void setAstrometricGeocentricRA(double value) {
    AAJNI.CAANearParabolicObjectDetails_AstrometricGeocentricRA_set(swigCPtr, this, value);
  }

  public double getAstrometricGeocentricRA() {
    return AAJNI.CAANearParabolicObjectDetails_AstrometricGeocentricRA_get(swigCPtr, this);
  }

  public void setAstrometricGeocentricDeclination(double value) {
    AAJNI.CAANearParabolicObjectDetails_AstrometricGeocentricDeclination_set(swigCPtr, this, value);
  }

  public double getAstrometricGeocentricDeclination() {
    return AAJNI.CAANearParabolicObjectDetails_AstrometricGeocentricDeclination_get(swigCPtr, this);
  }

  public void setAstrometricGeocentricDistance(double value) {
    AAJNI.CAANearParabolicObjectDetails_AstrometricGeocentricDistance_set(swigCPtr, this, value);
  }

  public double getAstrometricGeocentricDistance() {
    return AAJNI.CAANearParabolicObjectDetails_AstrometricGeocentricDistance_get(swigCPtr, this);
  }

  public void setAstrometricGeocentricLightTime(double value) {
    AAJNI.CAANearParabolicObjectDetails_AstrometricGeocentricLightTime_set(swigCPtr, this, value);
  }

  public double getAstrometricGeocentricLightTime() {
    return AAJNI.CAANearParabolicObjectDetails_AstrometricGeocentricLightTime_get(swigCPtr, this);
  }

  public void setElongation(double value) {
    AAJNI.CAANearParabolicObjectDetails_Elongation_set(swigCPtr, this, value);
  }

  public double getElongation() {
    return AAJNI.CAANearParabolicObjectDetails_Elongation_get(swigCPtr, this);
  }

  public void setPhaseAngle(double value) {
    AAJNI.CAANearParabolicObjectDetails_PhaseAngle_set(swigCPtr, this, value);
  }

  public double getPhaseAngle() {
    return AAJNI.CAANearParabolicObjectDetails_PhaseAngle_get(swigCPtr, this);
  }

}
