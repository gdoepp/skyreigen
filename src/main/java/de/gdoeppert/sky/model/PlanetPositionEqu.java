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

public class PlanetPositionEqu {

    double time;
    double timeHr;
    private double ra;
    private double decl;

    public PlanetPositionEqu(double t, double r, double d, double JD0) {
        time = t;
        timeHr = (t - JD0) * 24;
        setRa(r * 15);
        setDecl(d);
    }

    public static PlanetPositionEqu equatorialFromHorizontal(
            PlanetPositionHrz posHrz, EarthLocation observer) {

        double theta = CAASidereal.ApparentGreenwichSiderealTime(posHrz.getTime());

        CAA2DCoordinate equCoord = CAACoordinateTransformation
                .Horizontal2Equatorial(posHrz.getAzimuth(), posHrz.getAltitude(),
                        observer.localLat);

        return new PlanetPositionEqu(posHrz.getTime(), theta + observer.localLong
                / 15 - equCoord.getX(), equCoord.getY(), posHrz.getTime());
    }

    public double getRa() {
        return ra;
    }

    public void setRa(double ra) {
        this.ra = ra;
    }

    public double getDecl() {
        return decl;
    }

    public void setDecl(double decl) {
        this.decl = decl;
    }

}
