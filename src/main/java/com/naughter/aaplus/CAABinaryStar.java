/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAABinaryStar {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAABinaryStar(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAABinaryStar obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAABinaryStar(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static CAABinaryStarDetails Calculate(double t, double P, double T, double e, double a, double i, double omega, double w) {
    return new CAABinaryStarDetails(AAJNI.CAABinaryStar_Calculate(t, P, T, e, a, i, omega, w), true);
  }

  public static double ApparentEccentricity(double e, double i, double w) {
    return AAJNI.CAABinaryStar_ApparentEccentricity(e, i, w);
  }

  public CAABinaryStar() {
    this(AAJNI.new_CAABinaryStar(), true);
  }

}
