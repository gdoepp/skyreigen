/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAElliptical {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAElliptical(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAElliptical obj) {
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
        AAJNI.delete_CAAElliptical(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double DistanceToLightTime(double Distance) {
    return AAJNI.CAAElliptical_DistanceToLightTime(Distance);
  }

  public static CAAEllipticalPlanetaryDetails Calculate(double JD, CAAElliptical.EllipticalObject object, boolean bHighPrecision) {
    return new CAAEllipticalPlanetaryDetails(AAJNI.CAAElliptical_Calculate__SWIG_0(JD, object.swigValue(), bHighPrecision), true);
  }

  public static double SemiMajorAxisFromPerihelionDistance(double q, double e) {
    return AAJNI.CAAElliptical_SemiMajorAxisFromPerihelionDistance(q, e);
  }

  public static double MeanMotionFromSemiMajorAxis(double a) {
    return AAJNI.CAAElliptical_MeanMotionFromSemiMajorAxis(a);
  }

  public static CAAEllipticalObjectDetails Calculate(double JD, CAAEllipticalObjectElements elements, boolean bHighPrecision) {
    return new CAAEllipticalObjectDetails(AAJNI.CAAElliptical_Calculate__SWIG_1(JD, CAAEllipticalObjectElements.getCPtr(elements), elements, bHighPrecision), true);
  }

  public static double InstantaneousVelocity(double r, double a) {
    return AAJNI.CAAElliptical_InstantaneousVelocity(r, a);
  }

  public static double VelocityAtPerihelion(double e, double a) {
    return AAJNI.CAAElliptical_VelocityAtPerihelion(e, a);
  }

  public static double VelocityAtAphelion(double e, double a) {
    return AAJNI.CAAElliptical_VelocityAtAphelion(e, a);
  }

  public static double LengthOfEllipse(double e, double a) {
    return AAJNI.CAAElliptical_LengthOfEllipse(e, a);
  }

  public static double CometMagnitude(double g, double delta, double k, double r) {
    return AAJNI.CAAElliptical_CometMagnitude(g, delta, k, r);
  }

  public static double MinorPlanetMagnitude(double H, double delta, double G, double r, double PhaseAngle) {
    return AAJNI.CAAElliptical_MinorPlanetMagnitude(H, delta, G, r, PhaseAngle);
  }

  public CAAElliptical() {
    this(AAJNI.new_CAAElliptical(), true);
  }

  public final static class EllipticalObject {
    public final static CAAElliptical.EllipticalObject SUN = new CAAElliptical.EllipticalObject("SUN");
    public final static CAAElliptical.EllipticalObject MERCURY = new CAAElliptical.EllipticalObject("MERCURY");
    public final static CAAElliptical.EllipticalObject VENUS = new CAAElliptical.EllipticalObject("VENUS");
    public final static CAAElliptical.EllipticalObject MARS = new CAAElliptical.EllipticalObject("MARS");
    public final static CAAElliptical.EllipticalObject JUPITER = new CAAElliptical.EllipticalObject("JUPITER");
    public final static CAAElliptical.EllipticalObject SATURN = new CAAElliptical.EllipticalObject("SATURN");
    public final static CAAElliptical.EllipticalObject URANUS = new CAAElliptical.EllipticalObject("URANUS");
    public final static CAAElliptical.EllipticalObject NEPTUNE = new CAAElliptical.EllipticalObject("NEPTUNE");

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static EllipticalObject swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + EllipticalObject.class + " with value " + swigValue);
    }

    private EllipticalObject(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private EllipticalObject(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private EllipticalObject(String swigName, EllipticalObject swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static EllipticalObject[] swigValues = { SUN, MERCURY, VENUS, MARS, JUPITER, SATURN, URANUS, NEPTUNE };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

}
