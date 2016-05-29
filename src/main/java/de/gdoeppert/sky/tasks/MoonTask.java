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
import de.gdoeppert.sky.gui.LunarOrbitView;
import de.gdoeppert.sky.gui.LunarSurface;
import de.gdoeppert.sky.gui.MoonFragment;
import de.gdoeppert.sky.gui.PolarView;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.model.LunarElements;
import de.gdoeppert.sky.model.SkyEvent;

public class MoonTask extends AsyncTask<SkyActivity, String, SkyActivity> {

    View currentTab;
    SkyEvent[] events;
    MoonFragment frag;

    public MoonTask(MoonFragment moon) {
        currentTab = moon.getRootView();
        frag = moon;
    }

    @Override
    protected void onPreExecute() {
        // currentTab.setVisibility(View.INVISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SkyActivity result) {
        updateMoon(result);
        result.setProgressBarIndeterminateVisibility(false);
        super.onPostExecute(result);
        currentTab.setVisibility(View.VISIBLE);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {
        SkyActivity sky = params[0];

        events = sky.getLunarElements().getNextEvent();

        sky.getLunarElements().getTraj();
        return sky;
    }

    void updateMoon(final SkyActivity sky) {
        LunarElements lunarElements = sky.getLunarElements();
        TextView tx = (TextView) currentTab.findViewById(R.id.moonrise2);
        tx.setText(lunarElements.getRise(lunarElements.getRiseSet()));
        tx = (TextView) currentTab.findViewById(R.id.moontrans2);
        tx.setText(lunarElements.getTransit(lunarElements.getRiseSet()));
        tx = (TextView) currentTab.findViewById(R.id.moontransalt2);
        tx.setText(lunarElements.getTransitAlt(lunarElements.getRiseSet()));
        tx = (TextView) currentTab.findViewById(R.id.moonset2);
        tx.setText(lunarElements.getSet(lunarElements.getRiseSet()));

        tx = (TextView) currentTab.findViewById(R.id.moondist);
        tx.setText(lunarElements.getDistance());
        tx = (TextView) currentTab.findViewById(R.id.moondiam);
        tx.setText(lunarElements.getDiam());

        tx = (TextView) currentTab.findViewById(R.id.moonphase);
        tx.setText(lunarElements.getPhase());
        String[] lib = lunarElements.getLib();
        tx = (TextView) currentTab.findViewById(R.id.moonlibra1);
        tx.setText(lib[0]);
        tx = (TextView) currentTab.findViewById(R.id.moonlibra2);
        tx.setText(lib[1]);
        tx = (TextView) currentTab.findViewById(R.id.moondisk);
        tx.setText(lunarElements.getDisk());

        tx = (TextView) currentTab.findViewById(R.id.moondecl);
        tx.setText(lunarElements.getDeclination());
        tx = (TextView) currentTab.findViewById(R.id.moonra);
        tx.setText(lunarElements.getRA());

        tx = (TextView) currentTab.findViewById(R.id.planetazi);
        tx.setText(lunarElements.getAz());
        tx = (TextView) currentTab.findViewById(R.id.planetalt);
        tx.setText(lunarElements.getAlt());

        tx = (TextView) currentTab.findViewById(R.id.moonph1a);
        tx.setText(lunarElements.getQuarter()); //$NON-NLS-1$

        View v = currentTab.findViewById(R.id.lunarSurface2);

        LunarSurface ls = (LunarSurface) v;

        ls.setPhase(lunarElements.getPhaseNum());
        ls.setBLimb(lunarElements.getBrightLimbNum(sky.getSolarElements()));

        ls.setB0(lunarElements.getB0());
        ls.setL0(lunarElements.getL0());
        if (lunarElements.getObserver().localLat < 0) {
            ls.setP(180 + lunarElements.getPosAngle());
        } else {
            ls.setP(lunarElements.getPosAngle());
        }
        ls.setParallactic(lunarElements.getParallactic());

        ls.invalidate();

        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT);
        df.setTimeZone(lunarElements.getDisplayParams().df.getTimeZone());

        tx = (TextView) currentTab.findViewById(R.id.moonage2);
        tx.setText(lunarElements.getAge());

        int evIds[] = {R.id.moonevent0, R.id.moonevent1, R.id.moonevent2,
                R.id.moonevent3, R.id.moonevent4, R.id.moonevent5};
        int evDatIds[] = {R.id.mooneventdate0, R.id.mooneventdate1,
                R.id.mooneventdate2, R.id.mooneventdate3, R.id.mooneventdate4,
                R.id.mooneventdate5};

        for (int j = 0; j < 6; j++) {
            tx = (TextView) currentTab.findViewById(evIds[j]);
            tx.setText("");
            tx.setBackgroundColor(Color.TRANSPARENT);
            tx.setClickable(false);
            tx = (TextView) currentTab.findViewById(evDatIds[j]);
            tx.setText("");
            tx.setClickable(false);
        }
        for (int j = 0; j < events.length; j++) {
            tx = (TextView) currentTab.findViewById(evIds[j]);
            tx.setText(events[j].event);
            if (events[j].info == null) {
                tx.setBackgroundColor(Color.TRANSPARENT);
                tx.setClickable(false);
            } else {
                tx.setBackgroundColor(Color.DKGRAY);
                tx.setOnClickListener(frag);
            }
            tx = (TextView) currentTab.findViewById(evDatIds[j]);
            Calendar cal = events[j].date;
            tx.setText(df.format(cal.getTime()));
            tx.setClickable(true);
            tx.setOnClickListener(frag);
        }

        v = currentTab.findViewById(R.id.lunarViewMoon);
        LunarOrbitView lv = (LunarOrbitView) v;
        lv.setAscLong(lunarElements.getAscNum());
        lv.setPerigeeLong(lunarElements.getPeriNum());
        lv.setLunarLong(lunarElements.getLongNum());
        lv.setSolarLong(sky.getSolarElements().getLong());

        v = currentTab.findViewById(R.id.polarView);
        final PolarView pv = (PolarView) v;
        pv.setTraj(lunarElements.getTraj());
        pv.setPos(lunarElements.getActPos());
        pv.setDisplayParams(lunarElements.getDisplayParams());
    }

}
