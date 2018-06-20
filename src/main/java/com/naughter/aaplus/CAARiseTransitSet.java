/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAARiseTransitSet {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAARiseTransitSet(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAARiseTransitSet obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAARiseTransitSet(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static CAARiseTransitSetDetails Calculate(double JD, double Alpha1, double Delta1, double Alpha2, double Delta2, double Alpha3, double Delta3, double Longitude, double Latitude, double h0) {
    return new CAARiseTransitSetDetails(AAJNI.CAARiseTransitSet_Calculate(JD, Alpha1, Delta1, Alpha2, Delta2, Alpha3, Delta3, Longitude, Latitude, h0), true);
  }

  public CAARiseTransitSet() {
    this(AAJNI.new_CAARiseTransitSet(), true);
  }

}
