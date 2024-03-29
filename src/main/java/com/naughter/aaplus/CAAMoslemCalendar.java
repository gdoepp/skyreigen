/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAMoslemCalendar {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAMoslemCalendar(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAMoslemCalendar obj) {
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
        AAJNI.delete_CAAMoslemCalendar(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static CAACalendarDate MoslemToJulian(int Year, int Month, int Day) {
    return new CAACalendarDate(AAJNI.CAAMoslemCalendar_MoslemToJulian(Year, Month, Day), true);
  }

  public static CAACalendarDate JulianToMoslem(int Year, int Month, int Day) {
    return new CAACalendarDate(AAJNI.CAAMoslemCalendar_JulianToMoslem(Year, Month, Day), true);
  }

  public static boolean IsLeap(int Year) {
    return AAJNI.CAAMoslemCalendar_IsLeap(Year);
  }

  public CAAMoslemCalendar() {
    this(AAJNI.new_CAAMoslemCalendar(), true);
  }

}
