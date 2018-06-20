/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAACalendarDate {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAACalendarDate(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAACalendarDate obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAACalendarDate(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CAACalendarDate() {
    this(AAJNI.new_CAACalendarDate(), true);
  }

  public void setYear(int value) {
    AAJNI.CAACalendarDate_Year_set(swigCPtr, this, value);
  }

  public int getYear() {
    return AAJNI.CAACalendarDate_Year_get(swigCPtr, this);
  }

  public void setMonth(int value) {
    AAJNI.CAACalendarDate_Month_set(swigCPtr, this, value);
  }

  public int getMonth() {
    return AAJNI.CAACalendarDate_Month_get(swigCPtr, this);
  }

  public void setDay(int value) {
    AAJNI.CAACalendarDate_Day_set(swigCPtr, this, value);
  }

  public int getDay() {
    return AAJNI.CAACalendarDate_Day_get(swigCPtr, this);
  }

}
