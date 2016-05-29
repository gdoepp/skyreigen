package de.gdoeppert.sky.model;

import com.naughter.aaplus.CAA2DCoordinate;
import com.naughter.aaplus.CAACoordinateTransformation;
import com.naughter.aaplus.CAASidereal;


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


public class PlanetPositionHrz {

    private double time;
    private double timeHr;
    private double azimuth;
    private double altitude;
    private double vmag;

    public PlanetPositionHrz(double t, double vis, double alt, double az,
                             double JD0) {
        setTime(t);
        setVmag(vis);
        setTimeHr((getTime() - JD0) * 24);
        setAzimuth(az);
        setAltitude(alt);
    }

    public PlanetPositionHrz(double t, double vis, PlanetPositionHrz pos,
                             double JD0) {
        setTime(t);
        setVmag(vis);
        setTimeHr((getTime() - JD0) * 24);
        setAzimuth(pos.getAzimuth());
        setAltitude(pos.getAltitude());
    }

    static PlanetPositionHrz horziontalFromEquatorial(PlanetPositionEqu posEqu, EarthLocation observer) {
        return horziontalFromEquatorial(posEqu, posEqu.time, observer);
    }

    static PlanetPositionHrz horziontalFromEquatorial(PlanetPositionEqu posEqu, double time, EarthLocation observer) {
        double theta = CAASidereal.ApparentGreenwichSiderealTime(time);
        CAA2DCoordinate hrzCoord = CAACoordinateTransformation.Equatorial2Horizontal(theta + observer.localLong / 15.0 - posEqu.getRa() / 15.0,
                posEqu.getDecl(), observer.localLat);

        PlanetPositionHrz hrz = new PlanetPositionHrz(time, -99,
                hrzCoord.getY(), (hrzCoord.getX() + 180) % 360,
                (posEqu.time - posEqu.timeHr / 24));

        return hrz;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public double getAzimuth() {
        return azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    public double getVmag() {
        return vmag;
    }

    public void setVmag(double vmag) {
        this.vmag = vmag;
    }

    public double getTimeHr() {
        return timeHr;
    }

    public void setTimeHr(double timeHr) {
        this.timeHr = timeHr;
    }
}
