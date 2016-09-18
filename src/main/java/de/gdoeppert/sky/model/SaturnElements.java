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

import android.graphics.Color;

import com.naughter.aaplus.CAA3DCoordinate;
import com.naughter.aaplus.CAADiameters;
import com.naughter.aaplus.CAAEarth;
import com.naughter.aaplus.CAAElliptical.EllipticalObject;
import com.naughter.aaplus.CAAIlluminatedFraction;
import com.naughter.aaplus.CAASaturn;
import com.naughter.aaplus.CAASaturnMoonDetail;
import com.naughter.aaplus.CAASaturnMoons;
import com.naughter.aaplus.CAASaturnMoonsDetails;
import com.naughter.aaplus.CAASaturnRingDetails;
import com.naughter.aaplus.CAASaturnRings;

import java.util.Calendar;

import de.gdoeppert.sky.Messages;

public class SaturnElements extends PlanetElements {

    // private String ringLabel;
    public SaturnElements(Calendar cal, EarthLocation loc, DisplayParams dp) {
        super(cal, loc, dp);
        theObject = EllipticalObject.SATURN;
        // ringLabel = context.getResources().getString(R.string.planetring);

    }

    @Override
    public void calculate() {
        super.calculate();

        sdist = CAASaturn.RadiusVector(getJD(), false);
        sdiam = CAADiameters.SaturnEquatorialSemidiameterA(edist);
        disk = CAAIlluminatedFraction.IlluminatedFraction(sdist,
                CAAEarth.RadiusVector(getJD(), false), edist);
        CAASaturnRingDetails rings = CAASaturnRings.Calculate(getJD(), false);
        mag = CAAIlluminatedFraction.SaturnMagnitudeAA(sdist, edist,
                Math.abs(rings.getDeltaU()), Math.abs(rings.getB2())); // B2

    }

    @Override
    public PlanetMoon[] getMoons() {

        PlanetMoon[] moons = new PlanetMoon[6];
        final int[] colors = new int[]{Color.MAGENTA, Color.RED,
                Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE};
        final String[] names = new String[]{
                Messages.getString("SaturnElements.mimas"), Messages.getString("SaturnElements.enceladus"), Messages.getString("SaturnElements.tethys"), Messages.getString("SaturnElements.dione"), Messages.getString("SaturnElements.rhea"), Messages.getString("SaturnElements.titan")}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$

        CAASaturnMoonsDetails stMoons = CAASaturnMoons.Calculate(getJD(), false);

        CAASaturnMoonDetail[] stMoonsArr = new CAASaturnMoonDetail[]{
                stMoons.getSatellite1(), stMoons.getSatellite2(),
                stMoons.getSatellite3(), stMoons.getSatellite4(),
                stMoons.getSatellite5(), stMoons.getSatellite6(),};

        for (int j = 0; j < 6; j++) {
            CAASaturnMoonDetail moon = stMoonsArr[j];
            CAA3DCoordinate xyz = moon.getApparentRectangularCoordinates();

            moons[j] = new PlanetMoon();
            moons[j].setColor(colors[j]);
            moons[j].setName(names[j]);
            moons[j].setOcultated(moon.getBInOccultation());
            moons[j].setTransit(moon.getBInTransit());
            moons[j].setX(xyz.getX());
            moons[j].setY(xyz.getY());

        }
        return moons;
    }

    @Override
    public String getPhys() {
        final String dir = Messages.getString("SaturnElements.nesw"); //$NON-NLS-1$
        CAASaturnRingDetails ringDetails = CAASaturnRings.Calculate(getJD(), false);
        Character NS = (ringDetails.getB2() > 0 ? dir.charAt(0) : dir.charAt(2));
        Character WE = (ringDetails.getP() > 0 ? dir.charAt(1) : dir.charAt(3));

        return String.format(
                "\n$ring$: %c%2.0f %c%2.0f\n$ring$ ø: %3.0f\"/%3.0f\"", NS,
                Math.abs(ringDetails.getB2()), WE,
                Math.abs(ringDetails.getP()), ringDetails.getA(),
                ringDetails.getB()); //$NON-NLS-1$

    }

    @Override
    public double getMaxRadiusMoonOrbit() {
        return 23;
    }

    @Override
    public double getMaxMoonDecl() {
        return 1.8;
    }
}
