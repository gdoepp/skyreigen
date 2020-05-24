/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.1
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAEaster {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAEaster(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAEaster obj) {
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
        AAJNI.delete_CAAEaster(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static CAAEasterDetails Calculate(int nYear, boolean GregorianCalendar) {
    return new CAAEasterDetails(AAJNI.CAAEaster_Calculate(nYear, GregorianCalendar), true);
  }

  public CAAEaster() {
    this(AAJNI.new_CAAEaster(), true);
  }

}
