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

import com.naughter.aaplus.CAAAngularSeparation;
import com.naughter.aaplus.CAADate;
import com.naughter.aaplus.CAAElliptical.EllipticalObject;
import com.naughter.aaplus.CAAEllipticalPlanetaryDetails;
import com.naughter.aaplus.CAAParallactic;
import com.naughter.aaplus.CAASidereal;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Vector;


@SuppressLint("DefaultLocale")
public abstract class SolSysElements {

    public static final double J2000 = 2451545.0;
    protected RiseSet riseSet = null;
    private EarthLocation observer;
    private double JD;
    private double JD0;
    protected EllipticalObject theObject;
    protected CAAEllipticalPlanetaryDetails details;
    protected PlanetPositionEqu poseq;
    protected PlanetPositionHrz[] traj = null;
    protected float gmt;
    private DisplayParams displayParams;
    protected static double horizon = 0.5667;
    protected List<SkyEvent> events = null;

    public SolSysElements(Calendar cal, EarthLocation location, DisplayParams dp) {
        super();
        setDisplayParams(dp);
        if (getJD() - getJD0() > 1)
            setJD0(getJD0() + 1);
        if (location == null) return;
        setObserver(location);
        if (location.gmt > 24 || location.gmt < -24) {
            setGmt((cal.getTimeZone().getOffset(cal.getTimeInMillis()) / 3600000.0f));
        } else {
            setGmt(location.gmt);
        }
        setJD(EarthLocation.currentJD(cal, false, gmt));
        setJD0(EarthLocation.currentJD(cal, true, gmt));

    }

    protected PlanetPositionHrz getHorizontalPosition(PlanetPositionEqu posEqu) {
        return PlanetPositionHrz
                .horziontalFromEquatorial(posEqu, getObserver());
    }

    protected abstract PlanetPositionEqu getEquForTime(double time);

    PlanetPositionHrz getPosForTime(double time) {
        PlanetPositionEqu equ = getEquForTime(time);
        return PlanetPositionHrz.horziontalFromEquatorial(equ, getObserver());
    }

    public PlanetPositionHrz getActPos() {

        return getPosForTime(getJD());
    }

    public PlanetPositionHrz getActPosNow(Calendar cal) {

        cal = (Calendar) cal.clone();
        Calendar now = new GregorianCalendar();
        cal.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, now.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, now.get(Calendar.SECOND));
        return getPosForTime(EarthLocation.currentJD(cal, false, gmt));
    }

    public PlanetPositionHrz getActPos(double time) {
        return getPosForTime(time);
    }

    public PlanetPositionHrz[] getTraj() {

        if (traj == null) {
            RiseSet rs = getRiseSet();
            if (rs == null || rs.isAlwaysBelow()) {
                return null;
            }

            Vector<PlanetPositionHrz> trajVec = new Vector<PlanetPositionHrz>();

            if (rs.isAlwaysAbove()) {
                for (double t1 = getJD0(); t1 < getJD0() + 1; t1 += 1.0 / 24) {
                    PlanetPositionHrz pos = getPosForTime(t1);
                    if (pos.getAltitude() >= horizon) {
                        trajVec.add(pos);
                    }
                }
                PlanetPositionHrz pos0 = getPosForTime(getJD0() + 1);
                if (pos0.getAltitude() >= horizon) {
                    trajVec.add(pos0);
                }

            } else {
                double rise = (rs.isRising() ? rs.getRiseNum() : getJD0());
                double set = (rs.isSetting() ? rs.getSetNum() : getJD0()+1);

                if (rise > set) {

                    for (double t1 = rise; t1 < getJD0() + 1; t1 += 1.0 / 24) {
                        PlanetPositionHrz pos = getPosForTime(t1);
                        if (pos.getAltitude() > -1) {
                            trajVec.add(pos);
                        }
                    }
                    PlanetPositionHrz pos0 = getPosForTime(getJD0() + 1);
                    if (pos0.getAltitude() > -1) {
                        trajVec.add(pos0);
                    }

                    for (double t1 = getJD0(); t1 < set; t1 += 1.0 / 24) {
                        PlanetPositionHrz pos = getPosForTime(t1);
                        if (pos.getAltitude() > -1) {
                            trajVec.add(pos);
                        }
                    }
                    double t1 = set;
                    PlanetPositionHrz pos = getPosForTime(t1);
                    trajVec.add(pos);

                } else {
                    poseq = getEquForTime(getJD());
                    for (double t1 = rise; t1 < set; t1 += 1.0 / 24) {
                        trajVec.add(getPosForTime(t1));
                    }
                    trajVec.add(getPosForTime(rs.getSetNum()));
                }
            }
            traj = trajVec.toArray(new PlanetPositionHrz[0]);
        }
        return traj;
    }

    protected String printTime(CAADate date) {

        return String.format("%02d:%02d", date.Hour(), date.Minute());
    }

    protected String printDecimal(double d) {
        return String.format("%5.2f", d);
    }

    protected String printDecimal0(double d) {
        return String.format("%4.0f", d);

    }

    protected String printTimeSpan(CAADate date, int times) {
        String str;
        if (date.Hour() > 0) {
            str= String.format("%02dh%02d", date.Hour(), date.Minute());
        } else {
            str= String.format("%02dmin", date.Minute());
        }
        if (times>1) {
            str=str + "\u00D72";
        }
        return str;
    }

    protected String printTimeSpan(double days, int times) {
        CAADate date = new CAADate();
        int hours = (int) Math.floor(days * 24);
        date.Set(0, 0, 0, hours, (int) (Math.round((days * 24 - hours) * 60)),
                0, true);
        return printTimeSpan(date,  times);
    }

    protected String printDeg(double deg) {
        return String.format("%5.1f°", deg);
    }

    protected String printAzimuth(double deg) {
        if (getDisplayParams().showAzS)
            deg = (deg + 180) % 360;
        return String.format("%3.0f°", deg);
    }

    public void calculate() {
        createRst();
        poseq = getEquForTime(getJD());
    }

    abstract protected void createRst();

    public void update(Calendar cal) {
        setJD(EarthLocation.currentJD(cal, false, gmt));
        poseq = getEquForTime(getJD());
    }

    public double getAltNum(double jd) {
        return PlanetPositionHrz.horziontalFromEquatorial(poseq, jd,
                getObserver()).getAltitude();
    }

    public String getAlt() {
        return String.format("%6.2f°", getAltNum(getJD()));
    }

    public double getElongationNum(SolSysElements other) {
        PlanetPositionEqu obj1 = getEquForTime(getJD());
        PlanetPositionEqu obj2 = other.getEquForTime(getJD());
        return CAAAngularSeparation.Separation(obj1.getRa() / 15,
                obj1.getDecl(), obj2.getRa() / 15, obj2.getDecl());
    }

    public String getElongation(SolSysElements other) {
        double elong = getElongationNum(other);
        return printDeg(elong);
    }

    public String getDeclination() {
        return String.format("%6.2f°", getDeclinationNum()); //$NON-NLS-1$
    }

    public String getRA() {
        if (getDisplayParams().showHms) {
            double d = poseq.getRa() / 15;
            int h = (int) Math.floor(d);
            d -= h;
            d *= 60;
            int m = (int) Math.floor(d);
            d -= m;
            d *= 60;
            int s = (int) Math.round(d);
            return String.format("%02d:%02d:%02d", h, m, s);
        } else {
            return String.format("%6.2f°", poseq.getRa());
        }
    }

    public String getAz() {
        double az = getActPos().getAzimuth();
        if (getDisplayParams().showAzS)
            az = (az + 180) % 360;
        return String.format("%6.2f°", az);
    }

    public String getPhys() {
        return null;
    }

    public float getDeclinationNum() {
        return (float) poseq.getDecl();
    }

    public float getParallactic() {
        if (getActPos().getAltitude() < 0 || getActPos().getAltitude() > 80)
            return 0;

        double theta = (CAASidereal.ApparentGreenwichSiderealTime(getJD()) * 15 + getObserver().localLong) % 360;
        double tau = theta - poseq.getRa();
        float pa = (float) CAAParallactic.ParallacticAngle(tau / 15,
                getObserver().localLat, getDeclinationNum());

        if (getObserver().localLat < 0)
            pa = pa - 180;

        return pa;
    }

    public String printDegMS(double value, boolean isAzim) {
        if (isAzim && getDisplayParams().showAzS)
            value = (value + 180) % 360;
        char sg = (value >= 0 ? ' ' : '-');
        value = Math.abs(value);
        int aD = (int) Math.floor(value);
        int aM = (int) Math.floor((value - aD) * 60);
        int aS = (int) Math.floor((value - aD - aM / 60.0) * 3600);
        return String.format("%c%3d°%02d'%02d\"", sg, aD, aM, aS);
    }

    public double getHorizon() {
        return horizon;
    }

    public double getJD() {
        return JD;
    }

    public void setJD(double jD) {
        JD = jD;
    }

    public EarthLocation getObserver() {
        return observer;
    }

    public void setObserver(EarthLocation observer) {
        this.observer = observer;
    }

    public double getJD0() {
        return JD0;
    }

    public void setJD0(double jD0) {
        JD0 = jD0;
    }

    public RiseSet getRiseSet() {
        return riseSet;
    }

    public DisplayParams getDisplayParams() {
        return displayParams;
    }

    public void setDisplayParams(DisplayParams displayParams) {
        this.displayParams = displayParams;
    }

    public float getGmt() {
        return gmt;
    }

    public void setGmt(float gmt) {
        this.gmt = gmt;
    }

    protected String printRST(double jdt0, double d) {

        CAADate rtsDate = new CAADate(jdt0 + d / 24 + getGmt() / 24.0, true);
        Character dayRel = ' ';
        if (d > 24)
            dayRel = '+';
        else if (d < 0)
            dayRel = '-';
        return String.format(Locale.ROOT, "%c%02d:%02d", dayRel,
                rtsDate.Hour(), rtsDate.Minute());
    }

    public float localTime(double jd, double d) {

        CAADate rtsDate = new CAADate(jd + d + getGmt() / 24.0, true);

        float dres = rtsDate.Hour() / 24f + rtsDate.Minute() / (24f * 60f);

        if (d > 1)
            dres += 1;
        if (d < 0)
            dres -= 1;

        return dres;
    }

    public float localTime(double d) {

        CAADate rtsDate = new CAADate(d + getGmt() / 24.0, true);

        float dres = rtsDate.Hour() / 24f + rtsDate.Minute() / (24f * 60f);

        return dres;
    }

    public String getRiseAz(RiseSet riseSet) {

        if (!riseSet.isRising())
            return "---";
        else {

            PlanetPositionHrz position = getPosForTime(riseSet.getRiseNum());
            return printAzimuth(position.getAzimuth());
        }
    }

    public String getSet(RiseSet riseSet) {

        if (!riseSet.isSetting())
            return "---";
        else {
            return printRST(riseSet.jd0, riseSet.rtsDetails.getSet());
        }
    }

    public String getTransit(RiseSet riseSet) {

        if (riseSet.isAlwaysBelow())
            return "---";
        else {
            return printRST(riseSet.jd0, riseSet.rtsDetails.getTransit());
        }
    }

    public String getRise(RiseSet riseSet) {

        Log.println(Log.DEBUG, "rise", "d=" + riseSet.rtsDetails.getRise());

        if (!riseSet.isRising())
            return "---";
        else {
            return printRST(riseSet.jd0, riseSet.rtsDetails.getRise());
        }
    }

    public String getTransitAlt(RiseSet riseSet) {

        if (riseSet.isAlwaysBelow())
            return "---";
        else {

            PlanetPositionHrz position = getPosForTime(riseSet.getTransitNum());

            return printDeg(position.getAltitude());
        }
    }

    public String getSetAz(RiseSet riseSet) {

        if (!riseSet.isSetting())
            return "---";
        else {

            PlanetPositionHrz position = getPosForTime(riseSet.getSetNum());
            return printAzimuth(position.getAzimuth());
        }
    }

}