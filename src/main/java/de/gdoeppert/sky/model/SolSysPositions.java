package de.gdoeppert.sky.model;

import android.util.Log;

import com.naughter.aaplus.CAAEarth;
import com.naughter.aaplus.CAAJupiter;
import com.naughter.aaplus.CAAMars;
import com.naughter.aaplus.CAAMercury;
import com.naughter.aaplus.CAANeptune;
import com.naughter.aaplus.CAAPlanetPerihelionAphelion;
import com.naughter.aaplus.CAASaturn;
import com.naughter.aaplus.CAAUranus;
import com.naughter.aaplus.CAAVenus;

public class SolSysPositions {

    public static final int idx_moon = 0;
    public static final int idx_sun = 1;
    public static final int idx_mercury = 2;
    public static final int idx_venus = 3;
    public static final int idx_earth = 4;
    public static final int idx_mars = 5;
    public static final int idx_jupiter = 6;
    public static final int idx_saturn = 7;
    public static final int idx_uranus = 8;
    public static final int idx_neptune = 9;
    public static final int idx_count = 10;

    protected PlanetPositionEcl[] eclipticPosition;
    protected int planetColors[];
    protected double jd;

    public int[] getPlanetColors() {
        return planetColors;
    }

    public PlanetPositionEcl[] getEclipticPositions() {
        return eclipticPosition;
    }

    public void setJd(double jd) {
        this.jd = jd;
    }

    public void calcPositions() {
        Log.println(Log.DEBUG, "eclipt", "calc pos");

        planetColors = new int[idx_count];
        eclipticPosition = new PlanetPositionEcl[idx_count];

        planetColors[idx_moon] = 0xef00ffff;

        planetColors[idx_sun] = 0xefffff00;

        double year = 2000 + (jd - SolSysElements.J2000) / 365.2564;

        double k = CAAPlanetPerihelionAphelion.MercuryK(year);
        double perihelion = CAAMercury
                .EclipticLongitude(CAAPlanetPerihelionAphelion.Mercury(Math.floor(k)), false);
        eclipticPosition[idx_mercury] = new PlanetPositionEcl(jd,
                CAAMercury.EclipticLongitude(jd, false), 0, perihelion);
        planetColors[idx_mercury] = 0xefdfdf40;

        k = CAAPlanetPerihelionAphelion.VenusK(year);
        perihelion = CAAVenus.EclipticLongitude(CAAPlanetPerihelionAphelion
                .Venus(Math.floor(k)), false);
        eclipticPosition[idx_venus] = new PlanetPositionEcl(jd,
                CAAVenus.EclipticLongitude(jd, false), 0, perihelion);
        planetColors[idx_venus] = 0xefffff80;

        k = CAAPlanetPerihelionAphelion.MarsK(year);
        perihelion = CAAMars.EclipticLongitude(CAAPlanetPerihelionAphelion
                .Mars(Math.floor(k)), false);
        eclipticPosition[idx_mars] = new PlanetPositionEcl(jd,
                CAAMars.EclipticLongitude(jd, false), 0, perihelion);
        planetColors[idx_mars] = 0xefff5020;

        k = CAAPlanetPerihelionAphelion.JupiterK(year);
        perihelion = CAAJupiter.EclipticLongitude(CAAPlanetPerihelionAphelion
                .Jupiter(Math.floor(k)), false);
        eclipticPosition[idx_jupiter] = new PlanetPositionEcl(jd,
                CAAJupiter.EclipticLongitude(jd, false), 0, perihelion);
        planetColors[idx_jupiter] = 0xefffffff;

        k = CAAPlanetPerihelionAphelion.SaturnK(year);
        perihelion = CAASaturn.EclipticLongitude(CAAPlanetPerihelionAphelion
                .Saturn(Math.floor(k)), false);
        eclipticPosition[idx_saturn] = new PlanetPositionEcl(jd,
                CAASaturn.EclipticLongitude(jd, false), 0, perihelion);
        planetColors[idx_saturn] = 0xefe0e080;

        k = CAAPlanetPerihelionAphelion.UranusK(year);
        perihelion = CAAUranus.EclipticLongitude(CAAPlanetPerihelionAphelion
                .Uranus(Math.floor(k)), false);
        eclipticPosition[idx_uranus] = new PlanetPositionEcl(jd,
                CAAUranus.EclipticLongitude(jd, false), 0, perihelion);
        planetColors[idx_uranus] = 0xef00ffa0;

        k = CAAPlanetPerihelionAphelion.NeptuneK(year);
        perihelion = CAANeptune.EclipticLongitude(CAAPlanetPerihelionAphelion
                .Neptune(Math.floor(k)), false);

        eclipticPosition[idx_neptune] = new PlanetPositionEcl(jd,
                CAANeptune.EclipticLongitude(jd, false), 0, perihelion);

        planetColors[idx_neptune] = 0xef0080ff;

        eclipticPosition[idx_earth] = new PlanetPositionEcl(jd,
                CAAEarth.EclipticLongitudeJ2000(jd, false), 0,
                CAAEarth.EclipticLongitudeJ2000(CAAPlanetPerihelionAphelion
                        .EarthPerihelion(Math.floor(CAAPlanetPerihelionAphelion
                                .EarthK(year))), false));
        planetColors[idx_earth] = 0xff1010ff;

    }

    public double getJD() {

        return jd;
    }

}
