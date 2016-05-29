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

import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.PlanetFragment;
import de.gdoeppert.sky.gui.PlanetMoonsView;
import de.gdoeppert.sky.gui.PolarView;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.gui.VisibilityView;
import de.gdoeppert.sky.model.PlanetElements;
import de.gdoeppert.sky.model.PlanetPositionHrz;
import de.gdoeppert.sky.model.SkyEvent;

public class PlanetTask extends AsyncTask<SkyActivity, String, SkyActivity> {

    @Override
    protected void onCancelled() {
        if (planetVisibilityTask != null
                && planetVisibilityTask.getStatus() != AsyncTask.Status.FINISHED) {
            planetVisibilityTask.cancel(true);
        }
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        // currentTab.setVisibility(View.INVISIBLE);
        super.onPreExecute();
    }

    View currentTab;
    PlanetElements planetElements;
    PlanetVisibilityTask planetVisibilityTask;
    PlanetFragment frag;

    private SkyEvent[] events;

    public PlanetTask(PlanetFragment planet, PlanetElements pe,
                      PlanetVisibilityTask pvt) {
        currentTab = planet.getRootView();
        planetElements = pe;
        planetVisibilityTask = pvt;
        frag = planet;
    }

    @Override
    protected void onPostExecute(SkyActivity result) {
        updatePlanet(result);
        super.onPostExecute(result);
        if (planetVisibilityTask != null) {
            planetVisibilityTask.executeOnExecutor(
                    android.os.AsyncTask.THREAD_POOL_EXECUTOR, result);
        }
        currentTab.setVisibility(View.VISIBLE);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {
        SkyActivity sky = params[0];

        sky.getLunarElements(); // force construction...
        sky.getSolarElements();
        planetElements.calculate();
        events = planetElements.getNextEvent();
        planetElements.getTraj();
        return sky;
    }

    private void updatePlanet(final SkyActivity sky) {

        TextView tx = (TextView) currentTab.findViewById(R.id.planetrise2);
        tx.setText(planetElements.getRise(planetElements.getRiseSet()));
        tx = (TextView) currentTab.findViewById(R.id.planettrans2);
        tx.setText(planetElements.getTransit(planetElements.getRiseSet()));
        tx = (TextView) currentTab.findViewById(R.id.planettransalt2);
        tx.setText(planetElements.getTransitAlt(planetElements.getRiseSet()));
        tx = (TextView) currentTab.findViewById(R.id.planetset2);
        tx.setText(planetElements.getSet(planetElements.getRiseSet()));

        tx = (TextView) currentTab.findViewById(R.id.planetdistsun);
        tx.setText(planetElements.getDistSun());
        tx = (TextView) currentTab.findViewById(R.id.planetdistearth);
        tx.setText(planetElements.getDistEarth());

        tx = (TextView) currentTab.findViewById(R.id.planetmag);
        tx.setText(planetElements.getMagnitude());
        tx = (TextView) currentTab.findViewById(R.id.planetdiam);
        tx.setText(planetElements.getDiam());

        tx = (TextView) currentTab.findViewById(R.id.planetdecl);
        tx.setText(planetElements.getDeclination());
        tx = (TextView) currentTab.findViewById(R.id.planetra);
        tx.setText(planetElements.getRA());

        tx = (TextView) currentTab.findViewById(R.id.planetelong);
        tx.setText(planetElements.getElongation(sky.getSolarElements()));
        tx = (TextView) currentTab.findViewById(R.id.planetdisk);
        tx.setText(planetElements.getDisk());

        tx = (TextView) currentTab.findViewById(R.id.planetazi);
        tx.setText(planetElements.getAz());
        tx = (TextView) currentTab.findViewById(R.id.planetalt);
        tx.setText(planetElements.getAlt());

        tx = (TextView) currentTab.findViewById(R.id.planetvis);
        planetElements.setHumidity(sky.getHumidity());
        planetElements.setTemperature(sky.getTemperature());
        String vis = planetElements.getRelVisMag(sky.getSolarElements(),
                sky.getLunarElements(), planetElements.getJD());
        tx.setText(vis);

        PlanetPositionHrz[] traj = planetElements.getTraj();

        View v = currentTab.findViewById(R.id.polarView);
        final PolarView pv = (PolarView) v;
        pv.setTraj(traj);
        pv.setPos(planetElements.getActPos());
        pv.setDisplayParams(planetElements.getDisplayParams());

        v = currentTab.findViewById(R.id.visibilityViewPlanet);
        VisibilityView vv = (VisibilityView) v;
        vv.setVisCurve(null);

        v = currentTab.findViewById(R.id.planetMoonsView);
        PlanetMoonsView pmv = (PlanetMoonsView) v;
        pmv.update(planetElements, sky);

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT);
        df.setTimeZone(planetElements.getDisplayParams().df.getTimeZone());

        int evIds[] = {R.id.planetevent0, R.id.planetevent1,
                R.id.planetevent2, R.id.planetevent3, R.id.planetevent4,
                R.id.planetevent5};
        int evDatIds[] = {R.id.planeteventdate0, R.id.planeteventdate1,
                R.id.planeteventdate2, R.id.planeteventdate3,
                R.id.planeteventdate4, R.id.planeteventdate5};

        for (int j = 0; j < 6; j++) {
            tx = (TextView) currentTab.findViewById(evIds[j]);
            tx.setText(""); //$NON-NLS-1$
            tx.setBackgroundColor(Color.TRANSPARENT);
            tx.setClickable(false);
            tx = (TextView) currentTab.findViewById(evDatIds[j]);
            tx.setText(""); //$NON-NLS-1$
            tx.setClickable(false);
        }

        for (int j = 0; events != null && j < events.length; j++) {
            tx = (TextView) currentTab.findViewById(evIds[j]);
            tx.setText(events[j].event);
            if (events[j].info != null) {
                tx.setBackgroundColor(Color.DKGRAY);
                tx.setOnClickListener(frag);
            }
            tx = (TextView) currentTab.findViewById(evDatIds[j]);
            Calendar cal = events[j].date;
            tx.setText(df.format(cal.getTime()));
            tx.setClickable(true);
            tx.setOnClickListener(frag);
        }

        currentTab.invalidate();
        currentTab.forceLayout();
    }
}
