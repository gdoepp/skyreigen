/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAEquinoxesAndSolstices {
    private long swigCPtr;
    protected boolean swigCMemOwn;

    protected CAAEquinoxesAndSolstices(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(CAAEquinoxesAndSolstices obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

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

    public static double NorthwardEquinox(int Year) {
        return AAJNI.CAAEquinoxesAndSolstices_NorthwardEquinox(Year);
    }

    public static double NorthernSolstice(int Year) {
        return AAJNI.CAAEquinoxesAndSolstices_NorthernSolstice(Year);
    }

    public static double SouthwardEquinox(int Year) {
        return AAJNI.CAAEquinoxesAndSolstices_SouthwardEquinox(Year);
    }

    public static double SouthernSolstice(int Year) {
        return AAJNI.CAAEquinoxesAndSolstices_SouthernSolstice(Year);
    }

    public static double LengthOfSpring(int Year, boolean bNorthernHemisphere) {
        return AAJNI.CAAEquinoxesAndSolstices_LengthOfSpring__SWIG_0(Year, bNorthernHemisphere);
    }

    public static double LengthOfSpring(int Year) {
        return AAJNI.CAAEquinoxesAndSolstices_LengthOfSpring__SWIG_1(Year);
    }

    public static double LengthOfSummer(int Year, boolean bNorthernHemisphere) {
        return AAJNI.CAAEquinoxesAndSolstices_LengthOfSummer__SWIG_0(Year, bNorthernHemisphere);
    }

    public static double LengthOfSummer(int Year) {
        return AAJNI.CAAEquinoxesAndSolstices_LengthOfSummer__SWIG_1(Year);
    }

    public static double LengthOfAutumn(int Year, boolean bNorthernHemisphere) {
        return AAJNI.CAAEquinoxesAndSolstices_LengthOfAutumn__SWIG_0(Year, bNorthernHemisphere);
    }

    public static double LengthOfAutumn(int Year) {
        return AAJNI.CAAEquinoxesAndSolstices_LengthOfAutumn__SWIG_1(Year);
    }

    public static double LengthOfWinter(int Year, boolean bNorthernHemisphere) {
        return AAJNI.CAAEquinoxesAndSolstices_LengthOfWinter__SWIG_0(Year, bNorthernHemisphere);
    }

    public static double LengthOfWinter(int Year) {
        return AAJNI.CAAEquinoxesAndSolstices_LengthOfWinter__SWIG_1(Year);
    }

    public CAAEquinoxesAndSolstices() {
        this(AAJNI.new_CAAEquinoxesAndSolstices(), true);
    }

}