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
import com.naughter.aaplus.CAAMars;
import com.naughter.aaplus.CAAPhysicalMars;
import com.naughter.aaplus.CAAPhysicalMarsDetails;

import java.util.Calendar;

public class MarsElements extends PlanetElements {
    private CAAPhysicalMarsDetails physDetails;

    public MarsElements(Calendar cal, EarthLocation loc, DisplayParams dp) {
        super(cal, loc, dp);
        theObject = EllipticalObject.MARS;
    }

    @Override
    public void calculate() {
        super.calculate();

        sdist = CAAMars.RadiusVector(getJD());
        sdiam = CAADiameters.MarsSemidiameterA(edist);
        double sedist = CAAEarth.RadiusVector(getJD());
        disk = CAAIlluminatedFraction.IlluminatedFraction(sdist, sedist, edist);
        double phaseAngle = CAAIlluminatedFraction.PhaseAngle(sdist, sedist,
                edist);
        if (phaseAngle != phaseAngle) { // NaN: arccos 1+eps
            phaseAngle = 0;
        }
        mag = CAAIlluminatedFraction.MarsMagnitudeAA(sdist, edist, phaseAngle);
        physDetails = CAAPhysicalMars.Calculate(getJD());

    }

    @Override
    public void update(Calendar cal) {
        super.update(cal);
        physDetails = CAAPhysicalMars.Calculate(getJD());
    }

    @Override
    public String getPhys() {
        return String.format("\nP: %5.1f°\nD: %5.1f°\nW: %5.1f°",
                physDetails.getP(), physDetails.getDE(), physDetails.getW());

    }

}
