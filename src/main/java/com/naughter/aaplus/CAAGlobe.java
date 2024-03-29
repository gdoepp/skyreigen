/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAGlobe {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAGlobe(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAGlobe obj) {
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
        AAJNI.delete_CAAGlobe(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double RhoSinThetaPrime(double GeographicalLatitude, double Height) {
    return AAJNI.CAAGlobe_RhoSinThetaPrime(GeographicalLatitude, Height);
  }

  public static double RhoCosThetaPrime(double GeographicalLatitude, double Height) {
    return AAJNI.CAAGlobe_RhoCosThetaPrime(GeographicalLatitude, Height);
  }

  public static double RadiusOfParallelOfLatitude(double GeographicalLatitude) {
    return AAJNI.CAAGlobe_RadiusOfParallelOfLatitude(GeographicalLatitude);
  }

  public static double RadiusOfCurvature(double GeographicalLatitude) {
    return AAJNI.CAAGlobe_RadiusOfCurvature(GeographicalLatitude);
  }

  public static double DistanceBetweenPoints(double GeographicalLatitude1, double GeographicalLongitude1, double GeographicalLatitude2, double GeographicalLongitude2) {
    return AAJNI.CAAGlobe_DistanceBetweenPoints(GeographicalLatitude1, GeographicalLongitude1, GeographicalLatitude2, GeographicalLongitude2);
  }

  public CAAGlobe() {
    this(AAJNI.new_CAAGlobe(), true);
  }

}
