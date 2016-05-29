/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAStellarMagnitudes {
    private long swigCPtr;
    protected boolean swigCMemOwn;

    protected CAAStellarMagnitudes(long cPtr, boolean cMemoryOwn) {
        swigCMemOwn = cMemoryOwn;
        swigCPtr = cPtr;
    }

    protected static long getCPtr(CAAStellarMagnitudes obj) {
        return (obj == null) ? 0 : obj.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        if (swigCPtr != 0) {
            if (swigCMemOwn) {
                swigCMemOwn = false;
                AAJNI.delete_CAAStellarMagnitudes(swigCPtr);
            }
            swigCPtr = 0;
        }
    }

    public static double CombinedMagnitude(double m1, double m2) {
        return AAJNI.CAAStellarMagnitudes_CombinedMagnitude__SWIG_0(m1, m2);
    }

    public static double CombinedMagnitude(int Magnitudes, SWIGTYPE_p_double pMagnitudes) {
        return AAJNI.CAAStellarMagnitudes_CombinedMagnitude__SWIG_1(Magnitudes, SWIGTYPE_p_double.getCPtr(pMagnitudes));
    }

    public static double BrightnessRatio(double m1, double m2) {
        return AAJNI.CAAStellarMagnitudes_BrightnessRatio(m1, m2);
    }

    public static double MagnitudeDifference(double brightnessRatio) {
        return AAJNI.CAAStellarMagnitudes_MagnitudeDifference(brightnessRatio);
    }

    public CAAStellarMagnitudes() {
        this(AAJNI.new_CAAStellarMagnitudes(), true);
    }

}