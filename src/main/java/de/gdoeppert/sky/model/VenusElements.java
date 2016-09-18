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
import com.naughter.aaplus.CAAVenus;

import java.util.Calendar;

public class VenusElements extends PlanetElements {

    public VenusElements(Calendar cal, EarthLocation loc, DisplayParams dp) {
        super(cal, loc, dp);
        theObject = EllipticalObject.VENUS;
    }

    @Override
    public void calculate() {
        super.calculate();

        sdist = CAAVenus.RadiusVector(getJD(), false);
        sdiam = CAADiameters.VenusSemidiameterA(edist);
        double sedist = CAAEarth.RadiusVector(getJD(), false);
        disk = CAAIlluminatedFraction.IlluminatedFraction(sdist, sedist, edist);
        double phaseAngle = CAAIlluminatedFraction.PhaseAngle(sdist, sedist,
                edist);
        if (phaseAngle != phaseAngle) { // NaN: arccos 1+eps
            phaseAngle = 0;
        }
        mag = CAAIlluminatedFraction.VenusMagnitudeAA(sdist, edist, phaseAngle);
    }
}
