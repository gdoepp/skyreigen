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

import android.location.Location;
import android.util.Log;

import com.naughter.aaplus.CAADate;

import java.util.Calendar;
import java.util.TimeZone;

import de.gdoeppert.sky.Messages;

public class EarthLocation {

    public double localLong;
    public double localLat;
    public double localAltitude;
    public float gmt;
    public String name;

    public EarthLocation(String name, double localLong, double localLat,
                         double localAlt, float gmt) {
        this.localLong = localLong;
        this.localLat = localLat;
        this.name = name;
        this.gmt = gmt;
        this.localAltitude = localAlt;
    }

    public EarthLocation() {
        name = null;
        gmt = 0;
        localLong = 0;
        localLat = 0;
        localAltitude = 0;
    }

    public boolean isDummy() {
        return name == null;
    }

    public EarthLocation(String name, Location location, int gmt) {
        localLong = location.getLongitude();
        localLat = location.getLatitude();
        if (location.hasAltitude()) {
            localAltitude = location.getAltitude();
        } else {
            localAltitude = -999;
        }
        this.gmt = gmt;
    }

    @Override
    public String toString() {
        if (isDummy()) {
            return Messages.getString("SkyActivity.add");
        }
        return name;
    }

    public TimeZone getTimeZone() {
        if (gmt > 24 || gmt < -24) {
            return TimeZone.getDefault();
        }

        String g = (gmt < 0 ? "-" + (timehm(-gmt)) : "+" + timehm(gmt));
        return TimeZone.getTimeZone("GMT" + g);
    }

    private String timehm(float t) {
        int h = (int) Math.floor(t);
        int m = Math.round((t - h) * 60);
        return String.format("%02d:%02d", h, m);
    }

    static public double currentJD(Calendar cal, boolean midnight, double gmt) {

        double JD;
        CAADate ld = new CAADate();
        if (midnight) {
            ld.Set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0, true);
            Log.println(Log.DEBUG, "EL",
                    "caaDate=" + ld.Year() + "/" + ld.Month() + "/" + ld.Day());
        } else {
            ld.Set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
                    cal.get(Calendar.DAY_OF_MONTH),
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
                    cal.get(Calendar.SECOND), true);
        }
        JD = ld.Julian();
        Log.println(Log.DEBUG, "EL", "JD=" + JD);
        if (midnight) {
            JD -= gmt / 24.;
        } else {
            JD -= cal.getTimeZone().getOffset(cal.getTimeInMillis()) / 3600000.0 / 24.0;
        }
        return JD;
    }

}