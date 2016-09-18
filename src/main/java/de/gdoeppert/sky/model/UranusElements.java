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

import com.naughter.aaplus.CAADiameters;
import com.naughter.aaplus.CAAEarth;
import com.naughter.aaplus.CAAElliptical.EllipticalObject;
import com.naughter.aaplus.CAAIlluminatedFraction;
import com.naughter.aaplus.CAAUranus;

import java.util.Calendar;


public class UranusElements extends PlanetElements {

    public UranusElements(Calendar cal, EarthLocation loc, DisplayParams dp) {
        super(cal, loc, dp);
        theObject = EllipticalObject.URANUS;
    }

    @Override
    public void calculate() {
        super.calculate();

        sdist = CAAUranus.RadiusVector(getJD(), false);
        sdiam = CAADiameters.UranusSemidiameterA(edist);

        double sedist = CAAEarth.RadiusVector(getJD(), false);
        disk = CAAIlluminatedFraction.IlluminatedFraction(sdist, sedist, edist);
        mag = CAAIlluminatedFraction.UranusMagnitudeAA(sdist, edist);
    }
}
