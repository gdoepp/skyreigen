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

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.gui.VisibilityView;
import de.gdoeppert.sky.model.PlanetPositionHrz;
import de.gdoeppert.sky.model.SkyData;

public class PlanetVisibilityTask extends
        AsyncTask<SkyActivity, String, SkyActivity> {

    View currentTab;
    PlanetPositionHrz[] trajV;
    SkyData.PlanetId planetId;

    public PlanetVisibilityTask(View planet, SkyData.PlanetId planetId) {
        currentTab = planet;
        this.planetId = planetId;
    }

    @Override
    protected void onCancelled() {
        trajV = null;
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(SkyActivity result) {
        if (result != null) {
            updatePlanet(result);
        }
        result.setProgressBarIndeterminateVisibility(false);
        super.onPostExecute(result);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {
        SkyActivity sky = params[0];

        sky.getSkyData().getPlanetElements(planetId)
                .setHumidity(sky.getHumidity());
        sky.getSkyData().getPlanetElements(planetId)
                .setTemperature(sky.getTemperature());

        trajV = sky
                .getSkyData()
                .getPlanetElements(planetId)
                .calcVisibilities(
                        sky.getSkyData().getPlanetElements(planetId).getTraj(),
                        sky.getSkyData().getSolarElements(),
                        sky.getSkyData().getLunarElements());

        if (this.isCancelled()) {
            trajV = null;
            return null;
        }

        return sky;
    }

    private void updatePlanet(SkyActivity sky) {

        View v = currentTab.findViewById(R.id.visibilityViewPlanet);
        VisibilityView vv = (VisibilityView) v;
        if (trajV != null)
            vv.setVisCurve(trajV);
        vv.invalidate();
    }

}
