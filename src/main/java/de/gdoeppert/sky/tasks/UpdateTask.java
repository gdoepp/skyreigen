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
import android.widget.SeekBar;
import android.widget.TextView;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.LunarOrbitView;
import de.gdoeppert.sky.gui.LunarSurface;
import de.gdoeppert.sky.gui.PlanetMoonsView;
import de.gdoeppert.sky.gui.PolarView;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.gui.SkyFragment;
import de.gdoeppert.sky.gui.SunSurface;
import de.gdoeppert.sky.model.LunarElements;
import de.gdoeppert.sky.model.PlanetElements;
import de.gdoeppert.sky.model.PlanetPositionHrz;
import de.gdoeppert.sky.model.SolSysElements;
import de.gdoeppert.sky.model.SolarElements;

public class UpdateTask extends AsyncTask<SkyActivity, String, SkyActivity> {

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    View currentTab;
    SolSysElements objElements;
    SkyFragment frag;

    public UpdateTask(SkyFragment frag, SolSysElements elem) {
        currentTab = frag.getRootView();
        objElements = elem;
        this.frag = frag;
    }

    @Override
    protected void onPostExecute(SkyActivity result) {
        update(result);
        // frag.update();
        super.onPostExecute(result);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {

        SkyActivity sky = params[0];
        sky.getSolarElements().update(sky.getSkyData().getCalClone()); // force
        // construction...
        if (objElements instanceof LunarElements) {
            sky.getLunarElements().update(sky.getSkyData().getCalClone(),
                    sky.getSolarElements());
        } else if (objElements != null
                && !(objElements instanceof SolarElements)) {
            sky.getLunarElements().update(sky.getSkyData().getCalClone());
            objElements.update(sky.getSkyData().getCalClone());
        }
        return sky;
    }

    private void update(final SkyActivity sky) {

        TextView tx = (TextView) currentTab.findViewById(R.id.planetazi);
        if (tx != null) {
            tx.setText(objElements.getAz());
        }
        tx = (TextView) currentTab.findViewById(R.id.planetalt);
        if (tx != null) {
            tx.setText(objElements.getAlt());
        }

        if (objElements instanceof LunarElements) {
            LunarElements lunarElements = (LunarElements) objElements;
            tx = (TextView) currentTab.findViewById(R.id.moonra);
            if (tx != null) {
                tx.setText(lunarElements.getRA());
            }
            tx = (TextView) currentTab.findViewById(R.id.moondecl);
            if (tx != null) {
                tx.setText(lunarElements.getDeclination());
            }
            tx = (TextView) currentTab.findViewById(R.id.moonage2);
            if (tx != null) {
                tx.setText(lunarElements.getAge());
            }
            tx = (TextView) currentTab.findViewById(R.id.moonphase);
            tx.setText(lunarElements.getPhase());
            tx = (TextView) currentTab.findViewById(R.id.moondisk);
            tx.setText(lunarElements.getDisk());

            View v = currentTab.findViewById(R.id.lunarViewMoon);
            LunarOrbitView lv = (LunarOrbitView) v;
            lv.setLunarLong(lunarElements.getLongNum());
            v = currentTab.findViewById(R.id.lunarSurface2);
            LunarSurface ls = (LunarSurface) v;
            ls.setPhase(lunarElements.getPhaseNum());
            ls.setBLimb(lunarElements.getBrightLimbNum(sky.getSolarElements()));
            ls.setParallactic(lunarElements.getParallactic());
            ls.invalidate();

        } else if (objElements instanceof SolarElements) {
            SolarElements solarElements = (SolarElements) objElements;
            View v = currentTab.findViewById(R.id.sunSurface2);
            SunSurface ss = (SunSurface) v;
            ss.setB0((float) solarElements.getB0Angle());
            if (solarElements.getObserver().localLat < 0) {
                ss.setP(180 + (float) solarElements.getPosAngle());
            } else {
                ss.setP((float) solarElements.getPosAngle());
            }
            ss.setParallactic(solarElements.getParallactic());
            ss.invalidate();
            tx = (TextView) currentTab.findViewById(R.id.suncm);
            tx.setText(String.format("%5.1f°", solarElements.getCMAngle()));

            tx = (TextView) currentTab.findViewById(R.id.sundecl);
            tx.setText(solarElements.getDeclination());
            tx = (TextView) currentTab.findViewById(R.id.sunra);
            tx.setText(solarElements.getRA());

        } else if (objElements instanceof PlanetElements) {
            PlanetElements planetElements = (PlanetElements) objElements;
            tx = (TextView) currentTab.findViewById(R.id.planetdecl);
            tx.setText(planetElements.getDeclination());
            tx = (TextView) currentTab.findViewById(R.id.planetra);
            tx.setText(planetElements.getRA());

        }

        View v = currentTab.findViewById(R.id.polarView);
        final PolarView pv = (PolarView) v;
        if (pv != null) {
            PlanetPositionHrz pos = objElements.getActPos();
            pv.setPos(pos);
        }
        tx = (TextView) currentTab.findViewById(R.id.planetvis);
        if (tx != null) {
            PlanetElements planetElements = (PlanetElements) objElements;
            String vis = planetElements.getRelVisMag(sky.getSolarElements(),
                    sky.getLunarElements(), objElements.getJD());
            tx.setText(vis);
        }

        v = currentTab.findViewById(R.id.planetMoonsView);
        PlanetMoonsView pmv = (PlanetMoonsView) v;
        if (pmv != null) {
            PlanetElements planetElements = (PlanetElements) objElements;
            pmv.update(planetElements, sky);

        }

        TextView dp = (TextView) currentTab.findViewById(R.id.date);
        if (dp != null) {
            dp.setText(sky.getSkyData().formatDate(sky.getSkyData().getTime()));
        }

        dp = (TextView) currentTab.findViewById(R.id.time);
        if (dp != null) {
            dp.setText(sky.getSkyData().formatTime(sky.getSkyData().getTime()));
        }
        SeekBar sb = (SeekBar) currentTab.findViewById(R.id.seekTime);
        if (sb != null) {
            if (objElements != null) {
                sb.setProgress((int) ((objElements.getJD() - objElements
                        .getJD0()) * 24 * 60));
            } else {
                sb.setProgress((int) ((objElements.getJD() - objElements
                        .getJD0())));
            }
        }
        currentTab.invalidate();

    }
}
