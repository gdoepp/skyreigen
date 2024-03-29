/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAMoonNodes {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAMoonNodes(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAMoonNodes obj) {
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
        AAJNI.delete_CAAMoonNodes(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double K(double Year) {
    return AAJNI.CAAMoonNodes_K(Year);
  }

  public static double PassageThroNode(double k) {
    return AAJNI.CAAMoonNodes_PassageThroNode(k);
  }

  public CAAMoonNodes() {
    this(AAJNI.new_CAAMoonNodes(), true);
  }

}
