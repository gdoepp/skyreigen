/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAElementsPlanetaryOrbit {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAElementsPlanetaryOrbit(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAElementsPlanetaryOrbit obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        AAJNI.delete_CAAElementsPlanetaryOrbit(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double MercuryMeanLongitude(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryMeanLongitude(JD);
  }

  public static double MercurySemimajorAxis(double arg0) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercurySemimajorAxis(arg0);
  }

  public static double MercuryEccentricity(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryEccentricity(JD);
  }

  public static double MercuryInclination(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryInclination(JD);
  }

  public static double MercuryLongitudeAscendingNode(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryLongitudeAscendingNode(JD);
  }

  public static double MercuryLongitudePerihelion(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryLongitudePerihelion(JD);
  }

  public static double VenusMeanLongitude(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusMeanLongitude(JD);
  }

  public static double VenusSemimajorAxis(double arg0) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusSemimajorAxis(arg0);
  }

  public static double VenusEccentricity(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusEccentricity(JD);
  }

  public static double VenusInclination(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusInclination(JD);
  }

  public static double VenusLongitudeAscendingNode(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusLongitudeAscendingNode(JD);
  }

  public static double VenusLongitudePerihelion(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusLongitudePerihelion(JD);
  }

  public static double EarthMeanLongitude(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthMeanLongitude(JD);
  }

  public static double EarthSemimajorAxis(double arg0) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthSemimajorAxis(arg0);
  }

  public static double EarthEccentricity(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthEccentricity(JD);
  }

  public static double EarthInclination(double arg0) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthInclination(arg0);
  }

  public static double EarthLongitudePerihelion(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthLongitudePerihelion(JD);
  }

  public static double MarsMeanLongitude(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsMeanLongitude(JD);
  }

  public static double MarsSemimajorAxis(double arg0) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsSemimajorAxis(arg0);
  }

  public static double MarsEccentricity(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsEccentricity(JD);
  }

  public static double MarsInclination(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsInclination(JD);
  }

  public static double MarsLongitudeAscendingNode(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsLongitudeAscendingNode(JD);
  }

  public static double MarsLongitudePerihelion(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsLongitudePerihelion(JD);
  }

  public static double JupiterMeanLongitude(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterMeanLongitude(JD);
  }

  public static double JupiterSemimajorAxis(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterSemimajorAxis(JD);
  }

  public static double JupiterEccentricity(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterEccentricity(JD);
  }

  public static double JupiterInclination(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterInclination(JD);
  }

  public static double JupiterLongitudeAscendingNode(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterLongitudeAscendingNode(JD);
  }

  public static double JupiterLongitudePerihelion(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterLongitudePerihelion(JD);
  }

  public static double SaturnMeanLongitude(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnMeanLongitude(JD);
  }

  public static double SaturnSemimajorAxis(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnSemimajorAxis(JD);
  }

  public static double SaturnEccentricity(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnEccentricity(JD);
  }

  public static double SaturnInclination(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnInclination(JD);
  }

  public static double SaturnLongitudeAscendingNode(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnLongitudeAscendingNode(JD);
  }

  public static double SaturnLongitudePerihelion(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnLongitudePerihelion(JD);
  }

  public static double UranusMeanLongitude(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusMeanLongitude(JD);
  }

  public static double UranusSemimajorAxis(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusSemimajorAxis(JD);
  }

  public static double UranusEccentricity(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusEccentricity(JD);
  }

  public static double UranusInclination(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusInclination(JD);
  }

  public static double UranusLongitudeAscendingNode(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusLongitudeAscendingNode(JD);
  }

  public static double UranusLongitudePerihelion(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusLongitudePerihelion(JD);
  }

  public static double NeptuneMeanLongitude(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneMeanLongitude(JD);
  }

  public static double NeptuneSemimajorAxis(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneSemimajorAxis(JD);
  }

  public static double NeptuneEccentricity(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneEccentricity(JD);
  }

  public static double NeptuneInclination(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneInclination(JD);
  }

  public static double NeptuneLongitudeAscendingNode(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneLongitudeAscendingNode(JD);
  }

  public static double NeptuneLongitudePerihelion(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneLongitudePerihelion(JD);
  }

  public static double MercuryMeanLongitudeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryMeanLongitudeJ2000(JD);
  }

  public static double MercuryInclinationJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryInclinationJ2000(JD);
  }

  public static double MercuryLongitudeAscendingNodeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryLongitudeAscendingNodeJ2000(JD);
  }

  public static double MercuryLongitudePerihelionJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MercuryLongitudePerihelionJ2000(JD);
  }

  public static double VenusMeanLongitudeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusMeanLongitudeJ2000(JD);
  }

  public static double VenusInclinationJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusInclinationJ2000(JD);
  }

  public static double VenusLongitudeAscendingNodeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusLongitudeAscendingNodeJ2000(JD);
  }

  public static double VenusLongitudePerihelionJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_VenusLongitudePerihelionJ2000(JD);
  }

  public static double EarthMeanLongitudeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthMeanLongitudeJ2000(JD);
  }

  public static double EarthInclinationJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthInclinationJ2000(JD);
  }

  public static double EarthLongitudeAscendingNodeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthLongitudeAscendingNodeJ2000(JD);
  }

  public static double EarthLongitudePerihelionJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_EarthLongitudePerihelionJ2000(JD);
  }

  public static double MarsMeanLongitudeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsMeanLongitudeJ2000(JD);
  }

  public static double MarsInclinationJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsInclinationJ2000(JD);
  }

  public static double MarsLongitudeAscendingNodeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsLongitudeAscendingNodeJ2000(JD);
  }

  public static double MarsLongitudePerihelionJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_MarsLongitudePerihelionJ2000(JD);
  }

  public static double JupiterMeanLongitudeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterMeanLongitudeJ2000(JD);
  }

  public static double JupiterInclinationJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterInclinationJ2000(JD);
  }

  public static double JupiterLongitudeAscendingNodeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterLongitudeAscendingNodeJ2000(JD);
  }

  public static double JupiterLongitudePerihelionJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_JupiterLongitudePerihelionJ2000(JD);
  }

  public static double SaturnMeanLongitudeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnMeanLongitudeJ2000(JD);
  }

  public static double SaturnInclinationJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnInclinationJ2000(JD);
  }

  public static double SaturnLongitudeAscendingNodeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnLongitudeAscendingNodeJ2000(JD);
  }

  public static double SaturnLongitudePerihelionJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_SaturnLongitudePerihelionJ2000(JD);
  }

  public static double UranusMeanLongitudeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusMeanLongitudeJ2000(JD);
  }

  public static double UranusInclinationJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusInclinationJ2000(JD);
  }

  public static double UranusLongitudeAscendingNodeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusLongitudeAscendingNodeJ2000(JD);
  }

  public static double UranusLongitudePerihelionJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_UranusLongitudePerihelionJ2000(JD);
  }

  public static double NeptuneMeanLongitudeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneMeanLongitudeJ2000(JD);
  }

  public static double NeptuneInclinationJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneInclinationJ2000(JD);
  }

  public static double NeptuneLongitudeAscendingNodeJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneLongitudeAscendingNodeJ2000(JD);
  }

  public static double NeptuneLongitudePerihelionJ2000(double JD) {
    return AAJNI.CAAElementsPlanetaryOrbit_NeptuneLongitudePerihelionJ2000(JD);
  }

  public CAAElementsPlanetaryOrbit() {
    this(AAJNI.new_CAAElementsPlanetaryOrbit(), true);
  }

}
