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

import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.PolarView;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.gui.SunFragment;
import de.gdoeppert.sky.gui.SunSurface;
import de.gdoeppert.sky.model.PlanetPositionHrz;
import de.gdoeppert.sky.model.RiseSet;
import de.gdoeppert.sky.model.SkyEvent;
import de.gdoeppert.sky.model.SolSysElements;
import de.gdoeppert.sky.model.SolarElements;

public class SunTask extends AsyncTask<SkyActivity, String, SkyActivity> {

    PlanetPositionHrz[] winterTraj;
    PlanetPositionHrz[] summerTraj;
    PlanetPositionHrz[] equiTraj;
    View currentTab;
    private final SunFragment frag;

    public SunTask(SunFragment sun) {
        currentTab = sun.getRootView();
        frag = sun;
    }

    @Override
    protected void onPreExecute() {
        // currentTab.setVisibility(View.INVISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SkyActivity result) {
        if (result == null)
            return;
        updateSun(result);
        result.setProgressBarIndeterminateVisibility(false);
        super.onPostExecute(result);
        currentTab.setVisibility(View.VISIBLE);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {
        SkyActivity sky = params[0];

        if (isCancelled())
            return null;

        sky.getSolarElements().getNextEvent();
        sky.getSolarElements().getTraj();

        Calendar cal = new GregorianCalendar();
        cal.set(2011, 11, 21);
        SolSysElements solarWinter = sky.getSkyData().getSolarElements(cal);
        winterTraj = solarWinter.getTraj();
        if (isCancelled())
            return null;

        cal.set(2011, 05, 22);
        SolSysElements solarSummer = sky.getSkyData().getSolarElements(cal);
        summerTraj = solarSummer.getTraj();
        if (isCancelled())
            return null;

        cal.set(2011, 02, 21);
        SolSysElements solarEqui = sky.getSkyData().getSolarElements(cal);
        equiTraj = solarEqui.getTraj();

        return sky;
    }

    void updateSun(final SkyActivity sky) {

        SolarElements solarElements = sky.getSolarElements();

        TextView tx = (TextView) currentTab.findViewById(R.id.sunrise2);
        tx.setText(solarElements.getRise(solarElements.getRiseSet()));
        tx = (TextView) currentTab.findViewById(R.id.suntrans2);
        tx.setText(solarElements.getTransit(solarElements.getRiseSet()));

        tx = (TextView) currentTab.findViewById(R.id.eqoftime);
        tx.setText(solarElements.getEquTime());

        tx = (TextView) currentTab.findViewById(R.id.suntransalt2);
        tx.setText(solarElements.getTransitAlt(solarElements.getRiseSet()));
        tx = (TextView) currentTab.findViewById(R.id.sunset2);
        tx.setText(solarElements.getSet(solarElements.getRiseSet()));

        View v = currentTab.findViewById(R.id.polarView);
        final PolarView pv = (PolarView) v;
        pv.setTraj(solarElements.getTraj());
        pv.setPos(solarElements.getActPos());
        pv.setDisplayParams(solarElements.getDisplayParams());

        pv.setTrajWinter(winterTraj);
        pv.setTrajSummer(summerTraj);
        pv.setTrajEqui(equiTraj);

        tx = (TextView) currentTab.findViewById(R.id.sundist);
        tx.setText(solarElements.getDist());
        tx = (TextView) currentTab.findViewById(R.id.sundiam);
        tx.setText(solarElements.getDiam());
        tx = (TextView) currentTab.findViewById(R.id.sundecl);
        tx.setText(solarElements.getDeclination());
        tx = (TextView) currentTab.findViewById(R.id.sunra);
        tx.setText(solarElements.getRA());

        tx = (TextView) currentTab.findViewById(R.id.daylen);
        tx.setText(solarElements.getDayLen());

        RiseSet twiCiv = solarElements.getTwilightCivil();
        tx = (TextView) currentTab.findViewById(R.id.civilrise);
        tx.setText(solarElements.getRise(twiCiv));
        tx = (TextView) currentTab.findViewById(R.id.civilset);
        tx.setText(solarElements.getSet(twiCiv));
        tx = (TextView) currentTab.findViewById(R.id.civillen);
        tx.setText(solarElements.getTwilightLen(twiCiv, null));

        RiseSet twiNaut = solarElements.getTwilightNautic();
        tx = (TextView) currentTab.findViewById(R.id.nautrise);
        tx.setText(solarElements.getRise(twiNaut));
        tx = (TextView) currentTab.findViewById(R.id.nautset);
        tx.setText(solarElements.getSet(twiNaut));
        tx = (TextView) currentTab.findViewById(R.id.nautlen);
        tx.setText(solarElements.getTwilightLen(twiNaut, twiCiv));

        RiseSet twiAst = solarElements.getTwilightAstro();
        tx = (TextView) currentTab.findViewById(R.id.astrorise);
        tx.setText(solarElements.getRise(twiAst));
        tx = (TextView) currentTab.findViewById(R.id.astroset);
        tx.setText(solarElements.getSet(twiAst));
        tx = (TextView) currentTab.findViewById(R.id.astrolen);
        tx.setText(solarElements.getTwilightLen(twiAst, twiNaut));

        tx = (TextView) currentTab.findViewById(R.id.planetazi);
        tx.setText(solarElements.getAz());
        tx = (TextView) currentTab.findViewById(R.id.planetalt);
        tx.setText(solarElements.getAlt());

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT);

        df.setTimeZone(solarElements.getDisplayParams().df.getTimeZone());

        int evIds[] = {R.id.sunevent0, R.id.sunevent1, R.id.sunevent2,
                R.id.sunevent3};
        int evDatIds[] = {R.id.suneventdate0, R.id.suneventdate1,
                R.id.suneventdate2, R.id.suneventdate3};

        SkyEvent[] events = sky.getSolarElements().getNextEvent();
        for (int j = 0; j < events.length; j++) {

            tx = (TextView) currentTab.findViewById(evIds[j]);
            tx.setText(events[j].event);
            tx = (TextView) currentTab.findViewById(evDatIds[j]);
            Calendar cal = events[j].date;
            tx.setText(df.format(cal.getTime()));

            tx.setClickable(true);
            tx.setOnClickListener(frag);
        }

        tx = (TextView) currentTab.findViewById(R.id.same);
        tx.setText(solarElements.getSame());


        tx = (TextView) currentTab.findViewById(R.id.suncm);
        tx.setText(String.format("%5.1f°", solarElements.getCMAngle()));

        v = currentTab.findViewById(R.id.sunSurface2);
        SunSurface ss = (SunSurface) v;
        ss.setB0((float) solarElements.getB0Angle());
        if (solarElements.getObserver().localLat < 0) {
            ss.setP(180 + (float) solarElements.getPosAngle());
        } else {
            ss.setP((float) solarElements.getPosAngle());
        }
        ss.setParallactic(solarElements.getParallactic());

        ss.invalidate();
    }

}
