package de.gdoeppert.sky.model;

import android.util.Log;

import com.naughter.aaplus.CAA2DCoordinate;
import com.naughter.aaplus.CAACoordinateTransformation;
import com.naughter.aaplus.CAADynamicalTime;
import com.naughter.aaplus.CAAElliptical;
import com.naughter.aaplus.CAAElliptical.EllipticalObject;
import com.naughter.aaplus.CAAEllipticalPlanetaryDetails;
import com.naughter.aaplus.CAAMoon;
import com.naughter.aaplus.CAANutation;
import com.naughter.aaplus.CAARiseTransitSet;
import com.naughter.aaplus.CAARiseTransitSetDetails;

import java.util.Calendar;

public class RiseSetCalculator {

    final double deltaT;
    final CAAEllipticalPlanetaryDetails details1;
    final CAAEllipticalPlanetaryDetails details2;
    final CAAEllipticalPlanetaryDetails details3;
    final double jd;

    public RiseSetCalculator(double jd, EllipticalObject theObject) {
        this.jd = jd;
        deltaT = CAADynamicalTime.DeltaT(jd) / 86400;
        details2 = CAAElliptical.Calculate(jd + deltaT, theObject, false);
        if (theObject == EllipticalObject.NEPTUNE
                || theObject == EllipticalObject.URANUS
                || theObject == EllipticalObject.SATURN) {
            details1 = details2;
            details3 = details2;
        } else {
            details1 = CAAElliptical.Calculate(jd - 1 + deltaT, theObject, false);
            details3 = CAAElliptical.Calculate(jd + 1 + deltaT, theObject, false);
        }
    }

    public RiseSet createRst(double horizon, double longi, double lati) {

        CAARiseTransitSetDetails rtsDetails = CAARiseTransitSet.Calculate(jd,
                details1.getApparentGeocentricRA(),
                details1.getApparentGeocentricDeclination(),
                details2.getApparentGeocentricRA(),
                details2.getApparentGeocentricDeclination(),
                details3.getApparentGeocentricRA(),
                details3.getApparentGeocentricDeclination(), -longi, lati,
                horizon);

        return new RiseSet(rtsDetails, jd);

    }

    public enum RSTitem {
        sun, civilTw, nautTw, astroTw, moon, mercury, venus, mars, jupiter, saturn, uranus, neptune
    }

    ;

    public static class RiseSetAll {
        RiseSetAll(int d, double jd) {
            this.jd = jd;
            day = d;
            rst = new RiseSet[RSTitem.values().length];
        }

        public double jd;
        public int day;
        public RiseSet[] rst;
        public float moonLight;
    }

    protected RiseSet createRstMoonLow(double horizon, double longi, double lati) {

        double eps = CAANutation.TrueObliquityOfEcliptic(jd);

        double mlong1 = CAAMoon.MeanLongitude(jd - 1 + deltaT);
        double mlong2 = CAAMoon.MeanLongitude(jd + deltaT);
        double mlong3 = CAAMoon.MeanLongitude(jd + 1 + deltaT);

        CAA2DCoordinate mequ01 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlong1, 0, eps);
        CAA2DCoordinate mequ02 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlong2, 0, eps);
        CAA2DCoordinate mequ03 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlong3, 0, eps);

        CAARiseTransitSetDetails rtsDetails = CAARiseTransitSet.Calculate(jd,
                mequ01.getX(), mequ01.getY(), mequ02.getX(), mequ02.getY(),
                mequ03.getX(), mequ03.getY(), -longi, lati, horizon);

        return new RiseSet(rtsDetails, jd);
    }

    public static RiseSetAll[] createRstForMonth(EarthLocation observer,
                                                 Calendar cal0, double gmt) {
        Log.println(Log.DEBUG, "rst", "calc RstYear");
        Calendar cal = (Calendar) cal0.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        final int days = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        RiseSetAll[] rslist = new RiseSetAll[days];
        double jd = EarthLocation.currentJD(cal, true, gmt);

        Log.println(Log.DEBUG, "rst", "jd=" + jd + " cal=" + cal.toString());

        for (int j = 0; j < days; j++) {

            RiseSetAll rsDay = new RiseSetAll(j + 1, jd + j);

            RiseSetCalculator rstcal = new RiseSetCalculator(jd + j,
                    EllipticalObject.SUN);
            RiseSetCalculator rstcalMerc = new RiseSetCalculator(jd + j,
                    EllipticalObject.MERCURY);
            RiseSetCalculator rstcalVen = new RiseSetCalculator(jd + j,
                    EllipticalObject.VENUS);
            RiseSetCalculator rstcalMar = new RiseSetCalculator(jd + j,
                    EllipticalObject.MARS);
            RiseSetCalculator rstcalJup = new RiseSetCalculator(jd + j,
                    EllipticalObject.JUPITER);
            RiseSetCalculator rstcalSat = new RiseSetCalculator(jd + j,
                    EllipticalObject.SATURN);
            RiseSetCalculator rstcalUra = new RiseSetCalculator(jd + j,
                    EllipticalObject.URANUS);
            RiseSetCalculator rstcalNep = new RiseSetCalculator(jd + j,
                    EllipticalObject.NEPTUNE);

            rsDay.rst[RSTitem.sun.ordinal()] = rstcal.createRst(
                    SolarElements.horizon, observer.localLong,
                    observer.localLat);
            rsDay.rst[RSTitem.civilTw.ordinal()] = rstcal.createRst(-6,
                    observer.localLong, observer.localLat);
            rsDay.rst[RSTitem.nautTw.ordinal()] = rstcal.createRst(-12,
                    observer.localLong, observer.localLat);
            rsDay.rst[RSTitem.astroTw.ordinal()] = rstcal.createRst(-18,
                    observer.localLong, observer.localLat);
            rsDay.rst[RSTitem.mercury.ordinal()] = rstcalMerc.createRst(
                    PlanetElements.horizon, observer.localLong,
                    observer.localLat);
            rsDay.rst[RSTitem.venus.ordinal()] = rstcalVen.createRst(
                    PlanetElements.horizon, observer.localLong,
                    observer.localLat);
            rsDay.rst[RSTitem.mars.ordinal()] = rstcalMar.createRst(
                    PlanetElements.horizon, observer.localLong,
                    observer.localLat);
            rsDay.rst[RSTitem.jupiter.ordinal()] = rstcalJup.createRst(
                    PlanetElements.horizon, observer.localLong,
                    observer.localLat);
            rsDay.rst[RSTitem.saturn.ordinal()] = rstcalSat.createRst(
                    PlanetElements.horizon, observer.localLong,
                    observer.localLat);
            rsDay.rst[RSTitem.uranus.ordinal()] = rstcalUra.createRst(
                    PlanetElements.horizon, observer.localLong,
                    observer.localLat);
            rsDay.rst[RSTitem.neptune.ordinal()] = rstcalNep.createRst(
                    PlanetElements.horizon, observer.localLong,
                    observer.localLat);

            rsDay.rst[RSTitem.moon.ordinal()] = rstcal.createRstMoonLow(
                    LunarElements.horizon, observer.localLong,
                    observer.localLat);

            float D = (float) (CAAMoon.MeanElongation(rsDay.jd) / 180 * Math.PI);
            float i = (float) (Math.PI - D); // should be sufficient
            /*
             * float M = (float) (CAAEarth.SunMeanAnomaly(rsDay.jd) / 180 *
			 * Math.PI); float M1 = (float) (CAAMoon.MeanAnomaly(rsDay.jd) / 180
			 * * Math.PI);
			 * 
			 * float i = (float) ((Math.PI - D - 0.1098 * Math.sin(M1) + 0.036
			 * Math.sin(M) - 0.03 * Math.sin(2 * D - M1)) - 0.012 * Math .sin(2
			 * * D));
			 */
            rsDay.moonLight = (float) ((1 + Math.cos(i)) / 2);
            // rsDay.moonLight *= rsDay.moonLight;

            rslist[j] = rsDay;
        }
        Log.println(Log.DEBUG, "rst", "end calc RstYear");
        return rslist;
    }
}