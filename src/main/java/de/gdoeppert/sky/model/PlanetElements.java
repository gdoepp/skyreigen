package de.gdoeppert.sky.model;

/* Android App Sky 
 * 
 * (c) Gerhard Döppert
 * 
 * SkyReigen.apk is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * SkyReigen.apk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import android.util.Log;

import com.naughter.aaplus.CAAAngularSeparation;
import com.naughter.aaplus.CAADate;
import com.naughter.aaplus.CAAElliptical;
import com.naughter.aaplus.CAAEllipticalPlanetaryDetails;
import com.naughter.aaplus.CAAPlanetaryPhenomena;
import com.naughter.aaplus.CAAPlanetaryPhenomena.EventType;
import com.naughter.aaplus.CAAPlanetaryPhenomena.PlanetaryObject;
import com.projectpluto.astro.AngularBrightnessData;
import com.projectpluto.astro.FixedBrightnessData;
import com.projectpluto.astro.VisLimit;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

import de.gdoeppert.sky.Messages;

public abstract class PlanetElements extends SolSysElements {

    public static final float MIN_VISIBILITY = -6f;

    public void setHumidity(double humidity) {
        if (humidity != this.humidity)
            visCurve = null;
        this.humidity = humidity;
    }

    public void setTemperature(double temperature) {
        if (temperature != this.temperature)
            visCurve = null;
        this.temperature = temperature;
    }

    public PlanetElements(Calendar cal, EarthLocation loc, DisplayParams dp) {
        super(cal, loc, dp);
        disk = 1;
        alt = loc.localAltitude;
        humidity = 80;
        temperature = 10;

    }

    protected double edist;
    protected double sdist;
    protected double sdiam;
    protected double mag;
    protected double disk;
    private VisLimit vl;
    private double lunarElong = -1000;
    private final double alt;
    private double humidity;
    private double temperature;
    protected PlanetPositionHrz[] visCurve = null;

    /*
     * (non-Javadoc)
     *
     * @see de.gdoeppert.sky.PlanetElement#getDistEarth()
     */
    public String getDistEarth() {
        StringWriter result = new StringWriter();
        PrintWriter pr = new PrintWriter(result);
        pr.printf("%5.2f", edist); //$NON-NLS-1$
        return result.getBuffer().toString();
    }

    @Override
    protected PlanetPositionEqu getEquForTime(double time) {
        CAAEllipticalPlanetaryDetails details = CAAElliptical.Calculate(time,
                theObject);
        return new PlanetPositionEqu(time, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(), getJD0());
    }

    /*
     * (non-Javadoc)
     *
     * @see de.gdoeppert.sky.PlanetElement#getDiam()
     */
    public String getDiam() {
        StringWriter result = new StringWriter();
        PrintWriter pr = new PrintWriter(result);
        pr.printf("%5.1f\"", 2 * sdiam); //$NON-NLS-1$
        return result.getBuffer().toString();
    }

    public double getRadius() {
        return sdiam / 3600;
    }

    public String getMagnitude() {
        StringWriter result = new StringWriter();
        PrintWriter pr = new PrintWriter(result);
        pr.printf("%5.1f", mag); //$NON-NLS-1$
        return result.getBuffer().toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.gdoeppert.sky.PlanetElement#getDistSun()
     */
    public String getDistSun() {
        StringWriter result = new StringWriter();
        PrintWriter pr = new PrintWriter(result);
        pr.printf("%5.2f", sdist); //$NON-NLS-1$
        return result.getBuffer().toString();
    }

    public String getDisk() {
        StringWriter result = new StringWriter();
        PrintWriter pr = new PrintWriter(result);
        pr.printf("%3.0f%%", disk * 100); //$NON-NLS-1$
        return result.getBuffer().toString();
    }

    public double getRelVisMagNum(SolSysElements solarElements,
                                  SolSysElements lunarElements, double JD, PlanetPositionHrz pos) {
        if (pos.getAltitude() < -1
                || (JD - getJD0() < solarElements.getRiseSet().getSetNum() && JD
                - getJD0() > solarElements.getRiseSet().getRiseNum())) {
            return -99;
        }

        vl = new VisLimit();

        FixedBrightnessData fd = new FixedBrightnessData();
        fd.setHtAboveSeaInMeters(alt);
        fd.setLatitude(getObserver().localLat / 180.0 * Math.PI);
        fd.setRelativeHumidity(humidity);
        fd.setTemperatureInC(temperature);

        CAADate date = new CAADate(JD, true);

        fd.setYear(date.Year());
        fd.setMonth(date.Month());

        fd.setZenithAngMoon((90 - lunarElements.getActPos(JD).getAltitude())
                * Math.PI / 180.);
        fd.setZenithAngSun((90 - solarElements.getActPos(JD).getAltitude())
                * Math.PI / 180.);
        if (lunarElong < -100)
            lunarElong = lunarElements.getElongationNum(solarElements)
                    * Math.PI / 180.;
        fd.setMoonElongation(lunarElong);

        vl.setBrightnessParams(fd);
        vl.setMask(4);

        AngularBrightnessData ad = new AngularBrightnessData();

        ad.setZenithAngle((90 - pos.getAltitude()) * Math.PI / 180);
        ad.setDistMoon(getElongationNum(lunarElements) * Math.PI / 180);
        ad.setDistSun(getElongationNum(solarElements) * Math.PI / 180);

        vl.computeSkyBrightness(ad);
        vl.computeExtinction();
        double lmag = vl.computeLimitingMag();
        return lmag - mag;
    }

    public String getRelVisMag(SolSysElements solarElements,
                               SolSysElements lunarElements, double JD) {

        PlanetPositionHrz position = getActPos(JD);
        if (position.getAltitude() < 0) {
            return "-"; //$NON-NLS-1$
        }
        double vmag = getRelVisMagNum(solarElements, lunarElements, JD,
                position);
        if (vmag < -90)
            return "-"; //$NON-NLS-1$
        return printDecimal(vmag);
    }

    synchronized public PlanetPositionHrz[] calcVisibilities(
            PlanetPositionHrz[] traj, SolSysElements solarElements,
            SolSysElements lunarElements) {

        if (visCurve != null)
            return visCurve;
        if (traj == null)
            return null;
        Log.println(Log.DEBUG, "planet", "calc visibility "
                + this.getClass().getName());

        Vector<PlanetPositionHrz> visTraj = new Vector<PlanetPositionHrz>();
        for (int j = 0; j < traj.length; j++) {
            PlanetPositionHrz p = traj[j];

			/*
             * if (p.getTime() < solarElements.getRiseSet().getSetNum() - 1f /
			 * 24 && p.getTime() > solarElements.getRiseSet().getRiseNum() + 1f
			 * / 24) { continue; // } // we are between rise-1h and set+1h
			 */

            double time = p.getTime();
            double vis = getRelVisMagNum(solarElements, lunarElements, time, p);

            if (vis > MIN_VISIBILITY) {
                PlanetPositionHrz pneu = new PlanetPositionHrz(time, vis, p,
                        getJD0());
                visTraj.add(pneu);
            }

            // more steps at dawn and dusk and if object is low

            RiseSet riseSetSun = solarElements.getRiseSet();
            RiseSet riseSetPlanet = getRiseSet();

            boolean moreSteps = (time > riseSetSun.getSetNum() && time < riseSetSun
                    .getSetNum() + 1. / 24)
                    || (time < riseSetSun.getRiseNum() && time > riseSetSun
                    .getRiseNum() - 1. / 24)
                    || (time > riseSetPlanet.getSetNum() - 2 / 24. && time < riseSetPlanet
                    .getSetNum() + 0.001)
                    || (time < riseSetPlanet.getRiseNum() + 2 / 24. && time > riseSetPlanet
                    .getRiseNum() - 0.001);

            if (moreSteps) {
                for (double t = time + 0.25 / 24.; t < (j < traj.length - 1 ? traj[j + 1]
                        .getTime() : time + 1 / 24.); t += 0.25 / 24.) {
                    PlanetPositionHrz pos = getPosForTime(t);
                    vis = getRelVisMagNum(solarElements, lunarElements, t, pos);
                    if (vis > MIN_VISIBILITY) {
                        PlanetPositionHrz pN = new PlanetPositionHrz(t, vis,
                                pos, getJD0());
                        visTraj.add(pN);
                    }
                }
            }
        }
        visCurve = visTraj.toArray(new PlanetPositionHrz[0]);
        return visCurve;
    }

    public PlanetMoon[] getMoons() {
        return null;
    }

    public double getMaxRadiusMoonOrbit() {
        return 1;
    }

    public double getMaxMoonDecl() {
        return 0;
    }

    public String getRingOrientation() {
        return null;
    }

    public SkyEvent[] getNextEvent() {
        Log.println(Log.DEBUG, "planet", "nxt event "
                + this.getClass().getName());

        PlanetaryObject planet = PlanetaryObject.swigToEnum(theObject
                .swigValue() - 1);

        if (events == null) {

            ArrayList<SkyEvent> events = new ArrayList<SkyEvent>();
            EventType[] eventTypesK;
            EventType[] eventTypes;
            String eventName[];

            if (planet.swigValue() >= PlanetaryObject.URANUS.swigValue()) {

                eventTypesK = new EventType[]{EventType.CONJUNCTION,
                        EventType.OPPOSITION};

                eventTypes = new EventType[]{EventType.CONJUNCTION,
                        EventType.OPPOSITION};
                eventName = new String[]{
                        Messages.getString("PlanetElements.conj"), Messages.getString("PlanetElements.opp")}; //$NON-NLS-1$ //$NON-NLS-2$

            } else if (planet.swigValue() >= PlanetaryObject.MARS.swigValue()) {

                eventTypesK = new EventType[]{EventType.CONJUNCTION,
                        EventType.OPPOSITION};

                eventTypes = new EventType[]{EventType.CONJUNCTION,
                        EventType.OPPOSITION, EventType.STATION1,
                        EventType.STATION2};
                eventName = new String[]{
                        Messages.getString("PlanetElements.conj"), Messages.getString("PlanetElements.opp"), Messages.getString("PlanetElements.stat"), Messages.getString("PlanetElements.stat")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            } else if (planet.swigValue() <= PlanetaryObject.NEPTUNE
                    .swigValue()) {

                eventTypesK = new EventType[]{EventType.SUPERIOR_CONJUNCTION,
                        EventType.INFERIOR_CONJUNCTION};

                eventTypes = new EventType[]{EventType.SUPERIOR_CONJUNCTION,
                        EventType.INFERIOR_CONJUNCTION,
                        EventType.EASTERN_ELONGATION,
                        EventType.WESTERN_ELONGATION, EventType.STATION1,
                        EventType.STATION2};
                eventName = new String[]{
                        Messages.getString("PlanetElements.supconj"), Messages.getString("PlanetElements.infconj"), //$NON-NLS-1$ //$NON-NLS-2$
                        Messages.getString("PlanetElements.elong"), Messages.getString("PlanetElements.wlong"), Messages.getString("PlanetElements.stat"), Messages.getString("PlanetElements.stat")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

            } else
                return null;

            double year = 2000 + (getJD() - J2000 - 6.25) / 365.2564;
            double k = -1;
            TimeZone tzGmt = TimeZone.getTimeZone("GMT");

            for (int j = 0; j < eventTypes.length; j++) {

                if (j < eventTypesK.length) {
                    k = CAAPlanetaryPhenomena.K(year, planet, eventTypesK[j]);
                }

                double eventTime = CAAPlanetaryPhenomena.True(k, planet,
                        eventTypes[j]);

                CAADate time = new CAADate(eventTime, true);
                Calendar cal = new GregorianCalendar(time.Year(),
                        time.Month() - 1, time.Day(), time.Hour(),
                        time.Minute());
                cal.setTimeZone(tzGmt);
                SkyEvent ev = new SkyEvent(eventName[j], cal, eventTime);
                double elong;
                if (eventTypes[j].swigValue() == EventType.EASTERN_ELONGATION
                        .swigValue()) {
                    elong = CAAPlanetaryPhenomena.ElongationValue(k, planet,
                            true);
                    ev.info = String
                            .format(Messages
                                    .getString("PlanetElements.elongform"), elong); //$NON-NLS-1$
                } else if (eventTypes[j].swigValue() == EventType.WESTERN_ELONGATION
                        .swigValue()) {
                    elong = CAAPlanetaryPhenomena.ElongationValue(k, planet,
                            false);
                    ev.info = String
                            .format(Messages
                                    .getString("PlanetElements.elongform"), elong); //$NON-NLS-1$
                } else if (eventTypes[j].swigValue() == EventType.INFERIOR_CONJUNCTION
                        .swigValue()) {
                    SolarElements sol = new SolarElements(cal, getObserver(),
                            getDisplayParams());
                    PlanetPositionEqu obj1 = getEquForTime(eventTime);
                    PlanetPositionEqu obj2 = sol.getEquForTime(eventTime);

                    double sep = CAAAngularSeparation.Separation(
                            obj1.getRa() / 15, obj1.getDecl(),
                            obj2.getRa() / 15, obj2.getDecl());

                    ev.info = String.format(
                            Messages.getString("PlanetElements.conjAng"), sep); //$NON-NLS-1$
                    if (Math.abs(sep) < sol.getRadius() + this.getRadius()) {
                        ev.info += "\n" + Messages.getString("PlanetElements.conjTransit"); //$NON-NLS-1$ $NON-NLS-2$
                    }

                }
                events.add(ev);
            }
            this.events = events;
        }
        SkyEvent[] eventsAsArray = events.toArray(new SkyEvent[0]);

        java.util.Arrays.sort(eventsAsArray, new Comparator<SkyEvent>() {

            @Override
            public int compare(SkyEvent arg0, SkyEvent arg1) {

                return arg0.date.compareTo(arg1.date);
            }

        });

        return eventsAsArray;
    }

    @Override
    public void calculate() {
        Log.println(Log.DEBUG, "planet", "calculate "
                + this.getClass().getName());

        super.calculate();

        details = CAAElliptical.Calculate(getJD(), theObject);
        edist = details.getApparentGeocentricDistance();
        events = null;
    }

    @Override
    protected void createRst() {

        Log.println(Log.DEBUG, "RST", "calc Rst");
        RiseSetCalculator rstcal = new RiseSetCalculator(getJD0(), theObject);
        riseSet = rstcal.createRst(horizon, getObserver().localLong,
                getObserver().localLat);

    }
}