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

import android.annotation.SuppressLint;
import android.util.Log;

import com.naughter.aaplus.CAADate;
import com.naughter.aaplus.CAADiameters;
import com.naughter.aaplus.CAAEarth;
import com.naughter.aaplus.CAAElliptical;
import com.naughter.aaplus.CAAElliptical.EllipticalObject;
import com.naughter.aaplus.CAAEllipticalPlanetaryDetails;
import com.naughter.aaplus.CAAEquationOfTime;
import com.naughter.aaplus.CAAEquinoxesAndSolstices;
import com.naughter.aaplus.CAAInterpolate;
import com.naughter.aaplus.CAAPhysicalSun;
import com.naughter.aaplus.CAAPhysicalSunDetails;
import com.naughter.aaplus.CAASidereal;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import de.gdoeppert.sky.Messages;
import de.gdoeppert.sky.model.RiseSet.Circ;

public class SolarElements extends SolSysElements {

    RiseSet twilightCivil = null;
    RiseSet twilightNautic = null;
    RiseSet twilightAstronomical = null;
    private CAAPhysicalSunDetails phyDetails;
    private double nextSolstice = 0;
    static double horizon = -0.833;

    public SolarElements(Calendar cal, EarthLocation loc, DisplayParams dp) {
        super(cal, loc, dp);
        theObject = EllipticalObject.SUN;
    }

    @Override
    protected void createRst() {

        Log.println(Log.DEBUG, "RST", "calc Rst");
        RiseSetCalculator rstcal = new RiseSetCalculator(getJD0(), theObject);
        riseSet = rstcal.createRst(horizon, getObserver().localLong,
                getObserver().localLat);

    }

    protected void calcRstTwi() {
        Log.println(Log.DEBUG, "sun", "calc RstTwi");
        RiseSetCalculator rstcal = new RiseSetCalculator(getJD0(), theObject);
        twilightCivil = rstcal.createRst(-6, getObserver().localLong,
                getObserver().localLat);
        twilightNautic = rstcal.createRst(-12, getObserver().localLong,
                getObserver().localLat);
        twilightAstronomical = rstcal.createRst(-18, getObserver().localLong,
                getObserver().localLat);
    }

    public float getLong() {
        return (float) details.getApparentGeocentricLongitude();
    }

    @Override
    protected PlanetPositionEqu getEquForTime(double time) {
        CAAEllipticalPlanetaryDetails details = CAAElliptical.Calculate(time,
                theObject);
        return new PlanetPositionEqu(time, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(), getJD0());

    }

    public RiseSet getTwilightCivil() {

        if (twilightCivil == null) {
            calcRstTwi();
        }
        return twilightCivil;
    }

    public RiseSet getTwilightNautic() {

        if (twilightNautic == null) {
            calcRstTwi();
        }
        return twilightNautic;
    }

    public RiseSet getTwilightAstro() {

        if (twilightAstronomical == null) {
            calcRstTwi();
        }
        return twilightAstronomical;
    }

    public String getDayLen() {
        if (getRiseSet().getRises() == Circ.below)
            return "0h"; //$NON-NLS-1$
        else if (getRiseSet().getRises() == Circ.above)
            return "24h"; //$NON-NLS-1$
        else {
            double t = getRiseSet().getSetNum() - getRiseSet().getRiseNum();
            t = t - Math.floor(t);
            return printTimeSpan(t);
        }
    }

    @SuppressLint("DefaultLocale")
    public String getEquTime() {
        double eqn = CAAEquationOfTime.Calculate(getJD());
        int min = (int) Math.floor(Math.abs(eqn));
        int sec = (int) ((Math.abs(eqn) - min) * 60);
        return String.format("%c%02d:%02d", eqn > 0 ? '+' : '-', min, sec); //$NON-NLS-1$
    }

    public String getTwilightLen(RiseSet twi, RiseSet twi0) {

        if (twi0 == null)
            twi0 = getRiseSet();

        if (twi0.getRises() == Circ.above || twi.getRises() == Circ.below)
            return "---"; //$NON-NLS-1$

        double len = 0;

        if (twi0.getRises() == Circ.rises && twi.getRises() == Circ.above) {
            len = (1 - (twi0.getSetNum() - twi0.getRiseNum()));
        } else if (getRiseSet().getRises() == Circ.below) {
            len = (twi.getSetNum() - twi0.getRiseNum());
        } else {
            len = (twi0.getRiseNum() - twi.getRiseNum());
        }
        len = len - Math.floor(len);
        return printTimeSpan(len);

    }

    public String getNightLen() {
        if (getRiseSet().getRises() == Circ.above)
            return "0h"; //$NON-NLS-1$
        else if (getRiseSet().getRises() == Circ.below)
            return "24h";
        else {
            double t = getRiseSet().getRiseNum() - getRiseSet().getSetNum();
            t = t - Math.floor(t);

            return printTimeSpan(t);
        }
    }

    public String getDiam() {
        StringWriter result = new StringWriter();
        PrintWriter pr = new PrintWriter(result);
        pr.printf(
                "%5.1f'", 2 * CAADiameters.SunSemidiameterA(CAAEarth.RadiusVector(getJD())) / 60); //$NON-NLS-1$
        return result.getBuffer().toString();
    }

    /*
     * (non-Javadoc)
     *
     * @see de.gdoeppert.sky.PlanetElement#getMagnitude()
     */
    public String getDist() {
        StringWriter result = new StringWriter();
        PrintWriter pr = new PrintWriter(result);
        pr.printf("%5.3f", details.getApparentGeocentricDistance()); //$NON-NLS-1$
        return result.getBuffer().toString();
    }

    public SkyEvent[] getNextEvent() {
        Log.println(Log.DEBUG, "sun", "nxt event");

        if (events == null) {
            CAADate date = new CAADate(getJD(), true);
            int year = date.Year();
            int mon = date.Month();
            events = new ArrayList<SkyEvent>();
            String[] names = new String[]{
                    Messages.getString("SolarElements.spring"), Messages.getString("SolarElements.summer"), Messages.getString("SolarElements.autumn"), Messages.getString("SolarElements.winter")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

            double seasons[] = {
                    CAAEquinoxesAndSolstices
                            .NorthwardEquinox(mon > 3 ? year + 1 : year),
                    CAAEquinoxesAndSolstices
                            .NorthernSolstice(mon > 6 ? year + 1 : year),
                    CAAEquinoxesAndSolstices
                            .SouthwardEquinox(mon > 9 ? year + 1 : year),
                    CAAEquinoxesAndSolstices.SouthernSolstice(year)};

            nextSolstice = seasons[1];
            if (nextSolstice < getJD() || (mon > 6 && getJD() < seasons[3])) {
                nextSolstice = seasons[3];
            }

            int offset = (getObserver().localLat < 0 ? 2 : 0);
            for (int j = 0; j < 4; j++) {
                CAADate time = new CAADate(seasons[j], true);
                Calendar cal = new GregorianCalendar(time.Year(),
                        time.Month() - 1, time.Day(), time.Hour(),
                        time.Minute());
                cal.setTimeZone(TimeZone.getTimeZone("GMT"));
                events.add(new SkyEvent(names[(j + offset) % 4], cal,
                        seasons[j]));
            }

            java.util.Collections.sort(events, new Comparator<SkyEvent>() {

                @Override
                public int compare(SkyEvent arg0, SkyEvent arg1) {

                    return arg0.date.compareTo(arg1.date);
                }

            });
        }
        return events.toArray(new SkyEvent[0]);
    }

    public String getSame() {

        if (nextSolstice == 0)
            return "?"; // we need day of next solstice

        double ra = poseq.getRa(); // rectasc is a better target than
        // declination

        double delta = nextSolstice - getJD(); // days until next solstice

        Log.d("same", "delta = " + delta);

        double candidate = nextSolstice + delta; // same number of days after
        // next solstice

        ra = (360 + 180 - ra) % 360; // the rectasc. with the same declination

        delta = CAAInterpolate.Zero(
                CAAElliptical.Calculate(candidate - 10, theObject)
                        .getApparentGeocentricRA() * 15 - ra, CAAElliptical
                        .Calculate(candidate, theObject)
                        .getApparentGeocentricRA()
                        * 15 - ra,
                CAAElliptical.Calculate(candidate + 10, theObject)
                        .getApparentGeocentricRA() * 15 - ra);

        Log.d("same", "delta = " + (delta * 10));

        candidate += delta * 10; // x-step is 10 days

        CAADate time = new CAADate(candidate, true);
        Calendar cal = new GregorianCalendar(time.Year(), time.Month() - 1,
                time.Day(), time.Hour(), time.Minute());
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

        df.setTimeZone(getDisplayParams().df.getTimeZone());

        return df.format(cal.getTime());

    }

    @Override
    public void calculate() {
        Log.println(Log.DEBUG, "sun", "calculate");

        super.calculate();
        phyDetails = CAAPhysicalSun.Calculate(getJD());
        details = CAAElliptical.Calculate(getJD(), theObject);
        nextSolstice = 0;
        events = null;
    }

    @Override
    public double getHorizon() {
        return horizon;
    }

    @Override
    public void update(Calendar cal) {
        super.update(cal);
        phyDetails = CAAPhysicalSun.Calculate(getJD());
    }

    public double getRadius() {
        return CAADiameters.SunSemidiameterA(CAAEarth.RadiusVector(getJD())) / 3600;
    }

    public double getPosAngle() {
        return phyDetails.getP();
    }

    public double getCMAngle() {
        return phyDetails.getL0();
    }

    public double getB0Angle() {
        return phyDetails.getB0();
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getPhys() {
        return String.format("\nP: %5.1f°\nB0: %5.1f°\nL0: %5.1f°",
                getPosAngle(), getB0Angle(), getCMAngle());
    }

    @SuppressLint("DefaultLocale")
    public String getSiderealTime() {
        double theta = (CAASidereal.ApparentGreenwichSiderealTime(getJD())
                + getObserver().localLong / 15 + 24) % 24;

        int h = (int) Math.floor(theta);
        int m = (int) ((theta - h) * 60);

        return String.format(" %02d:%02d", h, m);

    }

}
