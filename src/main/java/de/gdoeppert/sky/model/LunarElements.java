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

import com.naughter.aaplus.CAA2DCoordinate;
import com.naughter.aaplus.CAACoordinateTransformation;
import com.naughter.aaplus.CAADate;
import com.naughter.aaplus.CAADiameters;
import com.naughter.aaplus.CAADynamicalTime;
import com.naughter.aaplus.CAAEarth;
import com.naughter.aaplus.CAAEclipses;
import com.naughter.aaplus.CAALunarEclipseDetails;
import com.naughter.aaplus.CAAMoon;
import com.naughter.aaplus.CAAMoonIlluminatedFraction;
import com.naughter.aaplus.CAAMoonPhases;
import com.naughter.aaplus.CAANutation;
import com.naughter.aaplus.CAAPhysicalMoon;
import com.naughter.aaplus.CAAPhysicalMoonDetails;
import com.naughter.aaplus.CAARiseTransitSet;
import com.naughter.aaplus.CAARiseTransitSetDetails;
import com.naughter.aaplus.CAASolarEclipseDetails;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import de.gdoeppert.sky.Messages;

public class LunarElements extends SolSysElements {

    private double age = -1;
    private double eps;
    private Double moonLong;
    private double k;
    private double phase;
    private double geoElong;
    private double elongDiff;
    protected CAAPhysicalMoonDetails moonDetails;
    static double horizon = 0.125;

    public LunarElements(Calendar cal, EarthLocation loc, DisplayParams dp) {
        super(cal, loc, dp);
        theObject = null;
    }

    @Override
    protected void createRst() {

        Log.println(Log.DEBUG, "RST", "calc Rst");
        riseSet = createRst(getJD0(), horizon);
    }

    protected RiseSet createRst(double jd, double horizon) {
        Log.println(Log.DEBUG, "moon", "create rst");

        double deltaT = CAADynamicalTime.DeltaT(jd) / 86400;
        eps = CAANutation.TrueObliquityOfEcliptic(jd);

        double mlat = CAAMoon.EclipticLatitude(jd - 1 + deltaT);
        double mlon = CAAMoon.EclipticLongitude(jd - 1 + deltaT);
        CAA2DCoordinate mequ1 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlon, mlat, eps);

        mlat = CAAMoon.EclipticLatitude(jd + deltaT);
        mlon = CAAMoon.EclipticLongitude(jd + deltaT);
        CAA2DCoordinate mequ2 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlon, mlat, eps);

        mlat = CAAMoon.EclipticLatitude(jd + 1 + deltaT);
        mlon = CAAMoon.EclipticLongitude(jd + 1 + deltaT);
        CAA2DCoordinate mequ3 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlon, mlat, eps);

        CAARiseTransitSetDetails rtsDetails = CAARiseTransitSet.Calculate(
                getJD0(), mequ1.getX(), mequ1.getY(), mequ2.getX(),
                mequ2.getY(), mequ3.getX(), mequ3.getY(),
                -getObserver().localLong, getObserver().localLat, horizon);

        return new RiseSet(rtsDetails, jd);
    }

    public String getDistance() {
        return String.format("%5.0f", CAAMoon.RadiusVector(getJD()) / 1000); //$NON-NLS-1$
    }

    public String getDiam() {
        return String
                .format("%5.1f'", 2 * CAADiameters.GeocentricMoonSemidiameter(CAAMoon.RadiusVector(getJD())) / 60); //$NON-NLS-1$
    }

    public float getLongNum() {
        calcEclLong();
        return moonLong.floatValue();
    }

    public String getLong() {
        return String.format("%5.1f°", getLongNum()); //$NON-NLS-1$
    }

    private void calcEclLong() {

        if (moonLong == null) {
            moonLong = CAAMoon.EclipticLongitude(getJD());
        }
    }

    public String getAsc() {
        return String.format("%5.1f°", getAscNum()); //$NON-NLS-1$
    }

    public String getPeri() {
        return String.format("%5.1f°", getPeriNum()); //$NON-NLS-1$
    }

    public String getPhase() {
        return String.format("%5.0f°", phase);
    }

    public String getBrightLimb(SolSysElements solar) {
        return String.format("%5.0f°", getBrightLimbNum(solar)); //$NON-NLS-1$
    }

    public String getDisk() {
        return String
                .format("%5.0f%%", CAAMoonIlluminatedFraction.IlluminatedFraction(phase) * 100); //$NON-NLS-1$
    }

    @Override
    protected PlanetPositionEqu getEquForTime(double time) {
        double mlat = CAAMoon.EclipticLatitude(time);
        double mlon = CAAMoon.EclipticLongitude(time);
        CAA2DCoordinate mequ1 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlon, mlat, eps);

        return new PlanetPositionEqu(time, mequ1.getX(), mequ1.getY(), getJD0());

    }

    public SkyEvent[] getNextEvent() {
        Log.println(Log.DEBUG, "moon", "nxt event");

        double k1 = Math.ceil(k * 4);
        int k2 = ((int) k1 % 4 + 4) % 4;
        k1 /= 4;

        int numEvents = 4;

        CAASolarEclipseDetails sec = CAAEclipses.CalculateSolar(k1
                + ((4 - k2) % 4) * 0.25);
        if (sec != null && sec.getFlags() != 0) {
            numEvents++;
        }
        CAALunarEclipseDetails lec = CAAEclipses.CalculateLunar(k1
                + ((4 - k2 + 2) % 4) * 0.25);
        if (lec.getBEclipse()) {
            numEvents++;
        }

        SkyEvent[] events = new SkyEvent[numEvents];

        double[] phdates = new double[]{CAAMoonPhases.TruePhase(k1),
                CAAMoonPhases.TruePhase(k1 + 0.25),
                CAAMoonPhases.TruePhase(k1 + 0.5),
                CAAMoonPhases.TruePhase(k1 + 0.75)};

        String[] names = new String[]{
                Messages.getString("LunarElements.newMoonL"), Messages.getString("LunarElements.firstLong"), Messages.getString("LunarElements.fullLong"), Messages.getString("LunarElements.lastLong")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        for (int j = 0; j < 4; j++) {
            CAADate time = new CAADate(phdates[j], true);
            Calendar cal = new GregorianCalendar(time.Year(), time.Month() - 1,
                    time.Day(), time.Hour(), time.Minute());
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            events[j] = new SkyEvent(names[(j + k2) % 4], cal, phdates[j]);
            events[j].info = null;
        }

        age = (getJD() - (events[(4 - k2) % 4].jd - 29.53)) % 29.53;

        int j = 4;
        if (sec != null && sec.getFlags() != 0) {
            CAADate time = new CAADate(sec.getTimeOfMaximumEclipse(), true);
            Calendar cal = new GregorianCalendar(time.Year(), time.Month() - 1,
                    time.Day(), time.Hour(), time.Minute());
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));

            events[j] = new SkyEvent(
                    Messages.getString("LunarElements.secl"), cal, sec.getTimeOfMaximumEclipse()); //$NON-NLS-1$
            events[j].info = Messages.getString("LunarElements.solareclipse") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$

            if ((sec.getFlags() & CAASolarEclipseDetails.CENTRAL_ECLIPSE) != 0) {
                events[j].info += Messages.getString("LunarElements.central") + ", "; //$NON-NLS-1$
            }
            if ((sec.getFlags() & CAASolarEclipseDetails.NON_CENTRAL_ECLIPSE) != 0) {
                events[j].info += Messages.getString("LunarElements.notcentral") + ", "; //$NON-NLS-1$
            }

            if ((sec.getFlags() & CAASolarEclipseDetails.TOTAL_ECLIPSE) != 0) {
                events[j].info += Messages.getString("LunarElements.total"); //$NON-NLS-1$
            }
            if ((sec.getFlags() & CAASolarEclipseDetails.ANNULAR_TOTAL_ECLIPSE) != 0) {
                events[j].info += Messages.getString("LunarElements.total_annular"); //$NON-NLS-1$
            }
            if ((sec.getFlags() & CAASolarEclipseDetails.ANNULAR_ECLIPSE) != 0) {
                events[j].info += Messages.getString("LunarElements.annular"); //$NON-NLS-1$
            }
            if ((sec.getFlags() & CAASolarEclipseDetails.PARTIAL_ECLIPSE) != 0) {
                events[j].info += Messages.getString("LunarElements.partial_semi"); //$NON-NLS-1$
            }
            events[j].info += "\n";

/*
            if (Math.abs(sec.getGamma()) < 0.9972) {
                events[j].info += Messages.getString("LunarElements.central") + ", "; //$NON-NLS-1$ //$NON-NLS-2$
                if (sec.getU() < 0) {
                    events[j].info += Messages.getString("LunarElements.total"); //$NON-NLS-1$
                } else if (sec.getU() < 0.0047) {
                    if (sec.getU() < 0.00464 * Math.sqrt((1 - sec.getGamma()
                            * sec.getGamma()))) {
                        events[j].info += Messages
                                .getString("LunarElements.total_annular"); //$NON-NLS-1$
                    } else {
                        events[j].info += Messages
                                .getString("LunarElements.annular"); //$NON-NLS-1$
                    }
                } else {
                    events[j].info += Messages
                            .getString("LunarElements.annular"); //$NON-NLS-1$
                }
                events[j].info += "\n"; //$NON-NLS-1$
            } else if (Math.abs(sec.getGamma()) < 0.9972 + Math.abs(sec.getU())) {
                events[j].info += Messages
                        .getString("LunarElements.notcentral") + ", " + Messages.getString("LunarElements.totalOrAnnular") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            */
            double mag = sec.getGreatestMagnitude();
            if (mag > 0) {
                events[j].info += Messages.getString("LunarElements.mag") + String.format("%5.2f", mag) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            events[j].info += Messages.getString("LunarElements.gamma") + String.format("%5.2f", sec.getGamma()) + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            j++;
        }

        if (lec.getBEclipse()) {
            CAADate time = new CAADate(lec.getTimeOfMaximumEclipse(), true);
            Calendar cal = new GregorianCalendar(time.Year(), time.Month() - 1,
                    time.Day(), time.Hour(), time.Minute());
            cal.setTimeZone(TimeZone.getTimeZone("GMT"));
            events[j] = new SkyEvent(
                    Messages.getString("LunarElements.leclipse"), cal, lec.getTimeOfMaximumEclipse()); //$NON-NLS-1$

            String total = null, penumbra = null, partial = null;
            Calendar cal1 = (Calendar) cal.clone();
            Calendar cal2 = (Calendar) cal.clone();
            if (lec.getTotalPhaseSemiDuration() > 0) {
                cal1.add(Calendar.MINUTE,
                        -(int) lec.getTotalPhaseSemiDuration());
                cal2.add(Calendar.MINUTE, (int) lec.getTotalPhaseSemiDuration());
                total = getDisplayParams().tf.format(cal1.getTime()) + " - "
                        + getDisplayParams().tf.format(cal2.getTime());
                cal1 = (Calendar) cal.clone();
                cal2 = (Calendar) cal.clone();
            }
            if (lec.getPartialPhasePenumbraSemiDuration() > 0) {
                cal1.add(Calendar.MINUTE,
                        -(int) lec.getPartialPhasePenumbraSemiDuration());
                cal2.add(Calendar.MINUTE,
                        (int) lec.getPartialPhasePenumbraSemiDuration());
                penumbra = getDisplayParams().tf.format(cal1.getTime()) + " - "
                        + getDisplayParams().tf.format(cal2.getTime());
                cal1 = (Calendar) cal.clone();
                cal2 = (Calendar) cal.clone();
            }
            if (lec.getPartialPhaseSemiDuration() > 0) {
                cal1.add(Calendar.MINUTE,
                        -(int) lec.getPartialPhaseSemiDuration());
                cal2.add(Calendar.MINUTE,
                        (int) lec.getPartialPhaseSemiDuration());
                partial = getDisplayParams().tf.format(cal1.getTime()) + " - "
                        + getDisplayParams().tf.format(cal2.getTime());
            }
            events[j].info = Messages.getString("LunarElements.mag") + ": " + String.format("%5.2f", lec.getUmbralMagnitude()) + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    + (total != null ? Messages
                    .getString("LunarElements.total_semi") + ": " + total : "") + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    + (partial != null ? Messages
                    .getString("LunarElements.partial_semi") + ": " + partial : "") + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    + (penumbra != null ? Messages
                    .getString("LunarElements.penum_semi") + ": " + penumbra : "") + "\n"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            j++;
        }

        java.util.Arrays.sort(events, new Comparator<SkyEvent>() {

            @Override
            public int compare(SkyEvent arg0, SkyEvent arg1) {

                return arg0.date.compareTo(arg1.date);
            }

        });

        return events;
    }

    public String getAge() {

        if (age < 0) {
            double mph = CAAMoonPhases.MeanPhase(Math.floor(k));
            age = (getJD() - mph) % 29.53f;
        }
        return printDecimal(age);
    }

    public String getQuarter() {
        if (age < 0)
            getAge();
        if (phase > 172)
            return Messages.getString("LunarElements.NewMoon"); //$NON-NLS-1$
        else if (phase < 8)
            return Messages.getString("LunarElements.FullMoon"); //$NON-NLS-1$
        else if (Math.abs(phase - 90) < 7)
            return Messages.getString("LunarElements.HalfMoon"); //$NON-NLS-1$

        if (phase > 90 && elongDiff < 180)
            return Messages.getString("LunarElements.waxcres"); //$NON-NLS-1$
        if (phase < 90 && elongDiff < 180)
            return Messages.getString("LunarElements.waxmoon"); //$NON-NLS-1$
        if (phase < 90 && elongDiff > 180)
            return Messages.getString("LunarElements.wanmoon"); //$NON-NLS-1$
        if (phase > 90 && elongDiff > 180)
            return Messages.getString("LunarElements.wancres"); //$NON-NLS-1$
        int q = (int) Math.floor((age / 29.53) * 4);
        return String.valueOf(q + 1);
    }

    public void calculate(SolSysElements solar) {
        Log.println(Log.DEBUG, "moon", "calculate");

        super.calculate();
        double year = 2000 + (getJD() - J2000 - 6.25) / 365.2564;
        k = CAAMoonPhases.K(year);
        geoElong = CAAMoonIlluminatedFraction.GeocentricElongation(
                poseq.getRa() / 15, poseq.getDecl(), solar.poseq.getRa() / 15,
                solar.poseq.getDecl());
        elongDiff = (poseq.getRa() - solar.poseq.getRa() + 360) % 360;
        phase = CAAMoonIlluminatedFraction.PhaseAngle(geoElong,
                CAAMoon.RadiusVector(getJD()),
                CAAEarth.RadiusVector(getJD(), false) * 149597871);
        moonDetails = CAAPhysicalMoon.CalculateTopocentric(getJD(),
                -getObserver().localLong, getObserver().localLat);
    }

    @Override
    public double getHorizon() {
        return horizon;
    }

    public void update(Calendar cal, SolSysElements solar) {
        Log.println(Log.DEBUG, "planet", "update");
        super.update(cal);
        moonLong = CAAMoon.EclipticLongitude(getJD());
        geoElong = CAAMoonIlluminatedFraction.GeocentricElongation(
                poseq.getRa() / 15, poseq.getDecl(), solar.poseq.getRa() / 15,
                solar.poseq.getDecl());
        phase = CAAMoonIlluminatedFraction.PhaseAngle(geoElong,
                CAAMoon.RadiusVector(getJD()),
                CAAEarth.RadiusVector(getJD(), false) * 149597871);
        moonDetails = CAAPhysicalMoon.CalculateTopocentric(getJD(),
                -getObserver().localLong, getObserver().localLat);

    }

    public float getAscNum() {
        return (float) CAAMoon.MeanLongitudeAscendingNode(getJD());
    }

    public float getPeriNum() {
        return (float) CAAMoon.MeanLongitudePerigee(getJD());
    }

    public double getPhaseNum() {
        return phase;
    }

    public double getBrightLimbNum(SolSysElements solar) {
        double angle = CAAMoonIlluminatedFraction.PositionAngle(
                solar.poseq.getRa() / 15, solar.poseq.getDecl(),
                poseq.getRa() / 15, poseq.getDecl());
        return (getObserver().localLat >= 0 ? angle : (angle + 180) % 360);
    }

    public double getElongation() {
        return getPhaseNum();
    }

    public float getPosAngle() {
        return (float) moonDetails.getP();
    }

    public float getB0() {
        return (float) moonDetails.getB();
    }

    public float getL0() {
        return (float) moonDetails.getL();
    }

    public String[] getLib() {
        final String dir = Messages.getString("LunarElements.nesw"); //$NON-NLS-1$

        Character WE = moonDetails.getL() < 0 ? dir.charAt(1) : dir.charAt(3);
        Character SN = moonDetails.getB() < 0 ? dir.charAt(2) : dir.charAt(0);

        return new String[]{
                String.format("%c%3.1f", SN, Math.abs(moonDetails.getB())), //$NON-NLS-1$
                String.format("%c%3.1f", WE, Math.abs(moonDetails.getL()))}; //$NON-NLS-1$

    }

}
