/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAPhysicalMars {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAPhysicalMars(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAPhysicalMars obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAAPhysicalMars(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static CAAPhysicalMarsDetails Calculate(double JD, boolean bHighPrecision) {
    return new CAAPhysicalMarsDetails(AAJNI.CAAPhysicalMars_Calculate(JD, bHighPrecision), true);
  }

  public CAAPhysicalMars() {
    this(AAJNI.new_CAAPhysicalMars(), true);
  }

}
