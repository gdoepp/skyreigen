/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAEquinoxesAndSolstices {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAEquinoxesAndSolstices(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAEquinoxesAndSolstices obj) {
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
        AAJNI.delete_CAAEquinoxesAndSolstices(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double NorthwardEquinox(int Year, boolean bHighPrecision) {
    return AAJNI.CAAEquinoxesAndSolstices_NorthwardEquinox(Year, bHighPrecision);
  }

  public static double NorthernSolstice(int Year, boolean bHighPrecision) {
    return AAJNI.CAAEquinoxesAndSolstices_NorthernSolstice(Year, bHighPrecision);
  }

  public static double SouthwardEquinox(int Year, boolean bHighPrecision) {
    return AAJNI.CAAEquinoxesAndSolstices_SouthwardEquinox(Year, bHighPrecision);
  }

  public static double SouthernSolstice(int Year, boolean bHighPrecision) {
    return AAJNI.CAAEquinoxesAndSolstices_SouthernSolstice(Year, bHighPrecision);
  }

  public static double LengthOfSpring(int Year, boolean bNorthernHemisphere, boolean bHighPrecision) {
    return AAJNI.CAAEquinoxesAndSolstices_LengthOfSpring(Year, bNorthernHemisphere, bHighPrecision);
  }

  public static double LengthOfSummer(int Year, boolean bNorthernHemisphere, boolean bHighPrecision) {
    return AAJNI.CAAEquinoxesAndSolstices_LengthOfSummer(Year, bNorthernHemisphere, bHighPrecision);
  }

  public static double LengthOfAutumn(int Year, boolean bNorthernHemisphere, boolean bHighPrecision) {
    return AAJNI.CAAEquinoxesAndSolstices_LengthOfAutumn(Year, bNorthernHemisphere, bHighPrecision);
  }

  public static double LengthOfWinter(int Year, boolean bNorthernHemisphere, boolean bHighPrecision) {
    return AAJNI.CAAEquinoxesAndSolstices_LengthOfWinter(Year, bNorthernHemisphere, bHighPrecision);
  }

  public CAAEquinoxesAndSolstices() {
    this(AAJNI.new_CAAEquinoxesAndSolstices(), true);
  }

}
