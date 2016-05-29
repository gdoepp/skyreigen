package de.gdoeppert.sky.tasks;

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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.naughter.aaplus.CAA2DCoordinate;
import com.naughter.aaplus.CAACoordinateTransformation;
import com.naughter.aaplus.CAAElliptical;
import com.naughter.aaplus.CAAElliptical.EllipticalObject;
import com.naughter.aaplus.CAAEllipticalPlanetaryDetails;
import com.naughter.aaplus.CAAMoon;
import com.naughter.aaplus.CAANutation;
import com.naughter.aaplus.CAASidereal;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.EquatorialViewGL;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.model.Constellation;
import de.gdoeppert.sky.model.ConstellationVertex;
import de.gdoeppert.sky.model.PlanetPositionEqu;
import de.gdoeppert.sky.model.PlanetPositionHrz;
import de.gdoeppert.sky.model.SolSysElements;

public class EquatorialTask extends AsyncTask<SkyActivity, String, SkyActivity> {

    Context context;
    View currentTab;
    private PlanetPositionEqu[] positions;
    private PlanetPositionEqu[] positions2;
    private PlanetPositionEqu[] positionsHorz;
    private int[] planetColors;
    private double meridian;
    private final AsyncTask<SkyActivity, String, SkyActivity> backgroundTask;

    static final int idx_moon = 0;
    static final int idx_sun = 1;
    static final int idx_mercury = 2;
    static final int idx_venus = 3;
    static final int idx_earth = 4;
    static final int idx_mars = 5;
    static final int idx_jupiter = 6;
    static final int idx_saturn = 7;
    static final int idx_uranus = 8;
    static final int idx_neptune = 9;
    static final int idx_count = 10;

    public EquatorialTask(View ecl, Context ctx,
                          AsyncTask<SkyActivity, String, SkyActivity> bktask) {
        currentTab = ecl;
        context = ctx;
        backgroundTask = bktask;
    }

    @Override
    protected void onPostExecute(SkyActivity result) {

        EquatorialViewGL eclV = (EquatorialViewGL) currentTab
                .findViewById(SkyActivity.ECLIPTIC_VIEW_ID);

        eclV.setJd(result.getSolarElements().getJD());

        eclV.setPositions(positions);
        eclV.setPositions2(positions2);
        eclV.setPositionsHorz(positionsHorz);
        eclV.setJd(result.getSolarElements().getJD());
        eclV.setPlanetColors(planetColors);
        eclV.setMeridian(meridian);

        eclV.setSouthern(result.getSkyData().getLocationLat() < 0);

        eclV.invalidate();
        result.setProgressBarIndeterminateVisibility(false);

        super.onPostExecute(result);

        if (backgroundTask != null) {
            backgroundTask.executeOnExecutor(
                    android.os.AsyncTask.THREAD_POOL_EXECUTOR, result);
        }

        currentTab.setVisibility(View.VISIBLE);
        eclV.setConstellations(constellations);

        eclV.setPlanetsReady(true);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {
        SkyActivity sky = params[0];

        if (isCancelled()) {
            return sky;
        }

        synchronized (lock) {
            if (constellations == null) {
                readConstellations();
            }
        }

        calcPositions(sky.getSolarElements());

        Log.println(Log.DEBUG, "equat", "ready");

        return sky;
    }

    @Override
    protected void onCancelled() {
        if (backgroundTask != null
                && backgroundTask.getStatus() != AsyncTask.Status.FINISHED) {
            backgroundTask.cancel(true);
        }
        super.onCancelled();
    }

    private static Object lock = new Object();
    private static List<Constellation> constellations = null;

    private void readConstellations() {

        if (EquatorialTask.constellations != null)
            return;

        ArrayList<Constellation> constellations = new ArrayList<Constellation>(
                60);
        try {
            Log.println(Log.DEBUG, "equat", "read constell");

            Map<String, ConstellationVertex> namePos = new TreeMap<String, ConstellationVertex>();

            InputStream istr = context.getResources().openRawResource(
                    R.raw.constell_bound_20);
            BufferedReader rd = new BufferedReader(new InputStreamReader(istr));
            String abbr = "";
            Constellation constellation = new Constellation();
            while (true) {
                String line = rd.readLine();
                if (line == null)
                    break;
                line = line.trim();
                String[] cols = line.split("[ \t]+");
                if (cols.length == 4 && cols[3].equals("O")) {
                    float ra = Float.valueOf(cols[0]) / 24 * 360;
                    float decl = Float.valueOf(cols[1]);

                    String abbr_new = cols[2];
                    if (!abbr_new.equals(abbr)) {
                        constellation.setName(abbr);

                        if (constellation.getVertices().size() > 0) {
                            if (constellation.normalize(0, 360)) {
                                constellation.normalize(-90, 270);
                                constellations.add(constellation);
                                constellation = (Constellation) constellation
                                        .clone();
                                constellation.normalize(90, 450);
                            }

                            constellations.add(constellation);
                            constellation = new Constellation();
                        }
                        abbr = abbr_new;
                    }
                    constellation.getVertices().add(
                            new ConstellationVertex(ra, decl));
                } else if (cols.length == 3) {
                    namePos.put(cols[0],
                            new ConstellationVertex(Float.valueOf(cols[2]),
                                    Float.valueOf(cols[1])));
                }
            }
            if (constellation.getVertices().size() > 0) {
                constellation.setName(abbr);
                constellation.normalize(0, 360);
                constellations.add(constellation);
            }
            for (Constellation c : constellations) {
                ConstellationVertex np = namePos.get(c.getName());
                c.setDecl(np.getDecl());
                c.setRa(np.getRa());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        EquatorialTask.constellations = constellations;
    }

    protected void calcPositions(SolSysElements solarElements) {
        Log.println(Log.DEBUG, "equat", "calc pos");

        planetColors = new int[idx_count];
        positions = new PlanetPositionEqu[idx_count];
        positions2 = new PlanetPositionEqu[idx_count];

        double theta = CAASidereal.ApparentGreenwichSiderealTime(solarElements
                .getJD());
        meridian = (theta * 15 + solarElements.getObserver().localLong) % 360;

        double eps = CAANutation.TrueObliquityOfEcliptic(solarElements.getJD());
        double mlat = CAAMoon.EclipticLatitude(solarElements.getJD());
        double mlon = CAAMoon.EclipticLongitude(solarElements.getJD());
        CAA2DCoordinate mequ1 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlon, mlat, eps);

        positions[idx_moon] = new PlanetPositionEqu(solarElements.getJD(),
                mequ1.getX(), mequ1.getY(), solarElements.getJD0());

        mlat = CAAMoon.EclipticLatitude(solarElements.getJD() + 1);
        mlon = CAAMoon.EclipticLongitude(solarElements.getJD() + 1);
        mequ1 = CAACoordinateTransformation
                .Ecliptic2Equatorial(mlon, mlat, eps);
        positions2[idx_moon] = new PlanetPositionEqu(solarElements.getJD() + 1,
                mequ1.getX(), mequ1.getY(), solarElements.getJD0() + 1);
        planetColors[idx_moon] = 0xef00ffff;

        CAAEllipticalPlanetaryDetails details = CAAElliptical.Calculate(
                solarElements.getJD(), EllipticalObject.SUN);
        positions[idx_sun] = new PlanetPositionEqu(solarElements.getJD(),
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        details = CAAElliptical.Calculate(solarElements.getJD() + 7,
                EllipticalObject.SUN);
        positions2[idx_sun] = new PlanetPositionEqu(solarElements.getJD() + 7,
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0() + 7);
        planetColors[idx_sun] = 0xefffff00;

        details = CAAElliptical.Calculate(solarElements.getJD(),
                EllipticalObject.MERCURY);
        positions[idx_mercury] = new PlanetPositionEqu(solarElements.getJD(),
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        details = CAAElliptical.Calculate(solarElements.getJD() + 7,
                EllipticalObject.MERCURY);
        positions2[idx_mercury] = new PlanetPositionEqu(
                solarElements.getJD() + 7, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        planetColors[idx_mercury] = 0xefdfdf40;

        details = CAAElliptical.Calculate(solarElements.getJD(),
                EllipticalObject.VENUS);
        positions[idx_venus] = new PlanetPositionEqu(solarElements.getJD(),
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        details = CAAElliptical.Calculate(solarElements.getJD() + 7,
                EllipticalObject.VENUS);
        positions2[idx_venus] = new PlanetPositionEqu(
                solarElements.getJD() + 7, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0() + 7);
        planetColors[idx_venus] = 0xefffff80;

        details = CAAElliptical.Calculate(solarElements.getJD(),
                EllipticalObject.MARS);
        positions[idx_mars] = new PlanetPositionEqu(solarElements.getJD(),
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        details = CAAElliptical.Calculate(solarElements.getJD() + 15,
                EllipticalObject.MARS);
        positions2[idx_mars] = new PlanetPositionEqu(
                solarElements.getJD() + 15, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0() + 15);
        planetColors[idx_mars] = 0xefff5020;

        details = CAAElliptical.Calculate(solarElements.getJD(),
                EllipticalObject.JUPITER);
        positions[idx_jupiter] = new PlanetPositionEqu(solarElements.getJD(),
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        details = CAAElliptical.Calculate(solarElements.getJD() + 30,
                EllipticalObject.JUPITER);
        positions2[idx_jupiter] = new PlanetPositionEqu(
                solarElements.getJD() + 30, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0() + 30);
        planetColors[idx_jupiter] = 0xefffffff;

        details = CAAElliptical.Calculate(solarElements.getJD(),
                EllipticalObject.SATURN);
        positions[idx_saturn] = new PlanetPositionEqu(solarElements.getJD(),
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        details = CAAElliptical.Calculate(solarElements.getJD() + 30,
                EllipticalObject.SATURN);
        positions2[idx_saturn] = new PlanetPositionEqu(
                solarElements.getJD() + 30, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0() + 30);
        planetColors[idx_saturn] = 0xefe0e080;

        details = CAAElliptical.Calculate(solarElements.getJD(),
                EllipticalObject.URANUS);
        positions[idx_uranus] = new PlanetPositionEqu(solarElements.getJD(),
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        details = CAAElliptical.Calculate(solarElements.getJD() + 365,
                EllipticalObject.URANUS);
        positions2[idx_uranus] = new PlanetPositionEqu(
                solarElements.getJD() + 365, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0() + 365);
        planetColors[idx_uranus] = 0xef00ffa0;

        details = CAAElliptical.Calculate(solarElements.getJD(),
                EllipticalObject.NEPTUNE);
        positions[idx_neptune] = new PlanetPositionEqu(solarElements.getJD(),
                details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0());
        details = CAAElliptical.Calculate(solarElements.getJD() + 365,
                EllipticalObject.NEPTUNE);
        positions2[idx_neptune] = new PlanetPositionEqu(
                solarElements.getJD() + 365, details.getApparentGeocentricRA(),
                details.getApparentGeocentricDeclination(),
                solarElements.getJD0() + 365);
        planetColors[idx_neptune] = 0xef0080ff;

        planetColors[idx_earth] = 0xff1010ff;

        for (int j = 0; j < idx_count; j++) {
            if (j != idx_earth) {
                if (positions2[j].getRa() < 90 && positions[j].getRa() > 270)
                    positions2[j].setRa(positions2[j].getRa() + 360);
                if (positions2[j].getRa() > 270 && positions[j].getRa() < 90)
                    positions2[j].setRa(positions2[j].getRa() - 360);
            }
        }

        positionsHorz = new PlanetPositionEqu[37];

        for (int j = 0; j < 37; j++) {
            positionsHorz[j] = PlanetPositionEqu.equatorialFromHorizontal(
                    new PlanetPositionHrz(solarElements.getJD(), 0, 0, j * 10,
                            solarElements.getJD0()), solarElements
                            .getObserver());
            if (positionsHorz[j].getRa() < 0)
                positionsHorz[j].setRa(positionsHorz[j].getRa() + 360);
            if (positionsHorz[j].getRa() > 360)
                positionsHorz[j].setRa(positionsHorz[j].getRa() - 360);
        }

    }

}
