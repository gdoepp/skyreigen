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

 * 
 */

import android.graphics.Color;

import com.naughter.aaplus.CAA3DCoordinate;
import com.naughter.aaplus.CAADiameters;
import com.naughter.aaplus.CAAEarth;
import com.naughter.aaplus.CAAElliptical.Object;
import com.naughter.aaplus.CAAGalileanMoonDetail;
import com.naughter.aaplus.CAAGalileanMoons;
import com.naughter.aaplus.CAAGalileanMoonsDetails;
import com.naughter.aaplus.CAAIlluminatedFraction;
import com.naughter.aaplus.CAAJupiter;
import com.naughter.aaplus.CAAPhysicalJupiter;
import com.naughter.aaplus.CAAPhysicalJupiterDetails;

import java.util.Calendar;

import de.gdoeppert.sky.Messages;

public class JupiterElements extends PlanetElements {

    private CAAPhysicalJupiterDetails physDetails;

    public JupiterElements(Calendar cal, EarthLocation loc, DisplayParams dp) {
        super(cal, loc, dp);
        theObject = Object.JUPITER;
    }

    @Override
    public void calculate() {

        super.calculate();
        sdist = CAAJupiter.RadiusVector(getJD(), false);
        sdiam = CAADiameters.JupiterEquatorialSemidiameterA(edist);
        double sedist = CAAEarth.RadiusVector(getJD(), false);
        disk = CAAIlluminatedFraction.IlluminatedFraction(sdist, sedist, edist);
        double phaseAngle = CAAIlluminatedFraction.PhaseAngle(sdist, sedist,
                edist);
        if (phaseAngle != phaseAngle) { // NaN: arccos 1+eps
            phaseAngle = 0;
        }
        mag = CAAIlluminatedFraction.JupiterMagnitudeAA(sdist, edist,
                phaseAngle);
        physDetails = CAAPhysicalJupiter.Calculate(getJD(), false);

    }

    @Override
    public PlanetMoon[] getMoons() {

        PlanetMoon[] moons = new PlanetMoon[4];
        final int[] colors = new int[]{Color.MAGENTA, Color.RED,
                Color.YELLOW, Color.GREEN};
        final String[] names = new String[]{
                Messages.getString("JupiterElements.io"), Messages.getString("JupiterElements.europa"), Messages.getString("JupiterElements.ganymed"), Messages.getString("JupiterElements.callisto")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

        CAAGalileanMoonsDetails jpMoons = CAAGalileanMoons.Calculate(getJD(), false);

        CAAGalileanMoonDetail[] jpMoonsArr = new CAAGalileanMoonDetail[]{
                jpMoons.getSatellite1(), jpMoons.getSatellite2(),
                jpMoons.getSatellite3(), jpMoons.getSatellite4()};

        for (int j = 0; j < 4; j++) {
            CAAGalileanMoonDetail moon = jpMoonsArr[j];
            CAA3DCoordinate xyz = moon.getApparentRectangularCoordinates();

            moons[j] = new PlanetMoon();
            moons[j].setColor(colors[j]);
            moons[j].setName(names[j]);
            moons[j].setOcultated(moon.getBInOccultation());
            moons[j].setTransit(moon.getBInTransit());
            moons[j].setShadowTransit(moon.getBInShadowTransit());
            moons[j].setX((getObserver().localLat >= 0 ? xyz.getX() : -xyz
                    .getX()));
            ;
            moons[j].setY((getObserver().localLat >= 0 ? xyz.getY() : -xyz
                    .getY()));

        }
        return moons;
    }

    @Override
    public double getMaxRadiusMoonOrbit() {
        return 27;
    }

    @Override
    public double getMaxMoonDecl() {
        return 1;
    }

    @Override
    public void update(Calendar cal) {
        super.update(cal);
        physDetails = CAAPhysicalJupiter.Calculate(getJD(), false);
    }

    @Override
    public String getPhys() {
        return String.format("\nP: %5.1f°\nD: %5.1f°\nW1: %5.1f°\nW2: %5.1f°",
                physDetails.getP(), physDetails.getDE(),
                physDetails.getApparentw1(), physDetails.getApparentw2());

    }

}
