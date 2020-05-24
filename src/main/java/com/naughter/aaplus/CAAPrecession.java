/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAPrecession {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAPrecession(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAPrecession obj) {
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
        AAJNI.delete_CAAPrecession(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static CAA2DCoordinate PrecessEquatorial(double Alpha, double Delta, double JD0, double JD) {
    return new CAA2DCoordinate(AAJNI.CAAPrecession_PrecessEquatorial(Alpha, Delta, JD0, JD), true);
  }

  public static CAA2DCoordinate PrecessEquatorialFK4(double Alpha, double Delta, double JD0, double JD) {
    return new CAA2DCoordinate(AAJNI.CAAPrecession_PrecessEquatorialFK4(Alpha, Delta, JD0, JD), true);
  }

  public static CAA2DCoordinate PrecessEcliptic(double Lambda, double Beta, double JD0, double JD) {
    return new CAA2DCoordinate(AAJNI.CAAPrecession_PrecessEcliptic(Lambda, Beta, JD0, JD), true);
  }

  public static CAA2DCoordinate EquatorialPMToEcliptic(double Alpha, double Delta, double Beta, double PMAlpha, double PMDelta, double Epsilon) {
    return new CAA2DCoordinate(AAJNI.CAAPrecession_EquatorialPMToEcliptic(Alpha, Delta, Beta, PMAlpha, PMDelta, Epsilon), true);
  }

  public static CAA2DCoordinate AdjustPositionUsingUniformProperMotion(double t, double Alpha, double Delta, double PMAlpha, double PMDelta) {
    return new CAA2DCoordinate(AAJNI.CAAPrecession_AdjustPositionUsingUniformProperMotion(t, Alpha, Delta, PMAlpha, PMDelta), true);
  }

  public static CAA2DCoordinate AdjustPositionUsingMotionInSpace(double r, double deltar, double t, double Alpha, double Delta, double PMAlpha, double PMDelta) {
    return new CAA2DCoordinate(AAJNI.CAAPrecession_AdjustPositionUsingMotionInSpace(r, deltar, t, Alpha, Delta, PMAlpha, PMDelta), true);
  }

  public CAAPrecession() {
    this(AAJNI.new_CAAPrecession(), true);
  }

}
