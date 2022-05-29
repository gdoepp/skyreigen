/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 4.0.2
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.naughter.aaplus;

public class CAAPlanetaryPhenomena {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CAAPlanetaryPhenomena(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CAAPlanetaryPhenomena obj) {
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
        AAJNI.delete_CAAPlanetaryPhenomena(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public static double K(double Year, CAAPlanetaryPhenomena.Planet planet, CAAPlanetaryPhenomena.Type type) {
    return AAJNI.CAAPlanetaryPhenomena_K(Year, planet.swigValue(), type.swigValue());
  }

  public static double Mean(double k, CAAPlanetaryPhenomena.Planet planet, CAAPlanetaryPhenomena.Type type) {
    return AAJNI.CAAPlanetaryPhenomena_Mean(k, planet.swigValue(), type.swigValue());
  }

  public static double True(double k, CAAPlanetaryPhenomena.Planet planet, CAAPlanetaryPhenomena.Type type) {
    return AAJNI.CAAPlanetaryPhenomena_True(k, planet.swigValue(), type.swigValue());
  }

  public static double ElongationValue(double k, CAAPlanetaryPhenomena.Planet planet, boolean bEastern) {
    return AAJNI.CAAPlanetaryPhenomena_ElongationValue(k, planet.swigValue(), bEastern);
  }

  public CAAPlanetaryPhenomena() {
    this(AAJNI.new_CAAPlanetaryPhenomena(), true);
  }

  public final static class Planet {
    public final static CAAPlanetaryPhenomena.Planet MERCURY = new CAAPlanetaryPhenomena.Planet("MERCURY");
    public final static CAAPlanetaryPhenomena.Planet VENUS = new CAAPlanetaryPhenomena.Planet("VENUS");
    public final static CAAPlanetaryPhenomena.Planet MARS = new CAAPlanetaryPhenomena.Planet("MARS");
    public final static CAAPlanetaryPhenomena.Planet JUPITER = new CAAPlanetaryPhenomena.Planet("JUPITER");
    public final static CAAPlanetaryPhenomena.Planet SATURN = new CAAPlanetaryPhenomena.Planet("SATURN");
    public final static CAAPlanetaryPhenomena.Planet URANUS = new CAAPlanetaryPhenomena.Planet("URANUS");
    public final static CAAPlanetaryPhenomena.Planet NEPTUNE = new CAAPlanetaryPhenomena.Planet("NEPTUNE");

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static Planet swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + Planet.class + " with value " + swigValue);
    }

    private Planet(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private Planet(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private Planet(String swigName, Planet swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static Planet[] swigValues = { MERCURY, VENUS, MARS, JUPITER, SATURN, URANUS, NEPTUNE };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

  public final static class Type {
    public final static CAAPlanetaryPhenomena.Type INFERIOR_CONJUNCTION = new CAAPlanetaryPhenomena.Type("INFERIOR_CONJUNCTION");
    public final static CAAPlanetaryPhenomena.Type SUPERIOR_CONJUNCTION = new CAAPlanetaryPhenomena.Type("SUPERIOR_CONJUNCTION");
    public final static CAAPlanetaryPhenomena.Type OPPOSITION = new CAAPlanetaryPhenomena.Type("OPPOSITION");
    public final static CAAPlanetaryPhenomena.Type CONJUNCTION = new CAAPlanetaryPhenomena.Type("CONJUNCTION");
    public final static CAAPlanetaryPhenomena.Type EASTERN_ELONGATION = new CAAPlanetaryPhenomena.Type("EASTERN_ELONGATION");
    public final static CAAPlanetaryPhenomena.Type WESTERN_ELONGATION = new CAAPlanetaryPhenomena.Type("WESTERN_ELONGATION");
    public final static CAAPlanetaryPhenomena.Type STATION1 = new CAAPlanetaryPhenomena.Type("STATION1");
    public final static CAAPlanetaryPhenomena.Type STATION2 = new CAAPlanetaryPhenomena.Type("STATION2");

    public final int swigValue() {
      return swigValue;
    }

    public String toString() {
      return swigName;
    }

    public static Type swigToEnum(int swigValue) {
      if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
        return swigValues[swigValue];
      for (int i = 0; i < swigValues.length; i++)
        if (swigValues[i].swigValue == swigValue)
          return swigValues[i];
      throw new IllegalArgumentException("No enum " + Type.class + " with value " + swigValue);
    }

    private Type(String swigName) {
      this.swigName = swigName;
      this.swigValue = swigNext++;
    }

    private Type(String swigName, int swigValue) {
      this.swigName = swigName;
      this.swigValue = swigValue;
      swigNext = swigValue+1;
    }

    private Type(String swigName, Type swigEnum) {
      this.swigName = swigName;
      this.swigValue = swigEnum.swigValue;
      swigNext = this.swigValue+1;
    }

    private static Type[] swigValues = { INFERIOR_CONJUNCTION, SUPERIOR_CONJUNCTION, OPPOSITION, CONJUNCTION, EASTERN_ELONGATION, WESTERN_ELONGATION, STATION1, STATION2 };
    private static int swigNext = 0;
    private final int swigValue;
    private final String swigName;
  }

}
