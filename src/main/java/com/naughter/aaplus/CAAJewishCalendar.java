/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAJewishCalendar {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAJewishCalendar(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAJewishCalendar obj) {
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
        AAJNI.delete_CAAJewishCalendar(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static CAACalendarDate DateOfPesach(int Year, boolean bGregorianCalendar) {
    return new CAACalendarDate(AAJNI.CAAJewishCalendar_DateOfPesach__SWIG_0(Year, bGregorianCalendar), true);
  }

  public static CAACalendarDate DateOfPesach(int Year) {
    return new CAACalendarDate(AAJNI.CAAJewishCalendar_DateOfPesach__SWIG_1(Year), true);
  }

  public static boolean IsLeap(int Year) {
    return AAJNI.CAAJewishCalendar_IsLeap(Year);
  }

  public static int DaysInYear(int Year) {
    return AAJNI.CAAJewishCalendar_DaysInYear(Year);
  }

  public CAAJewishCalendar() {
    this(AAJNI.new_CAAJewishCalendar(), true);
  }

}
