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
import android.view.View;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.gui.SolsysView;
import de.gdoeppert.sky.model.SolSysPositions;

public class EclipticTask extends AsyncTask<SkyActivity, String, SkyActivity> {

    Context context;
    View currentTab;

    public EclipticTask(View ecl, Context ctx) {
        currentTab = ecl;
        context = ctx;
    }

    @Override
    protected void onPreExecute() {
        // currentTab.setVisibility(View.INVISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SkyActivity result) {

        SolsysView eclV = (SolsysView) currentTab.findViewById(R.id.solSysEcl);
        if (eclV != null) {
            SolSysPositions pos = result.getSkyData().getSolSysPositions();
            eclV.setEclipticPosition(pos.getEclipticPositions());
            eclV.setPlanetColors(pos.getPlanetColors());
            eclV.setPlanetsReady(true);

            eclV.invalidate();

            result.setProgressBarIndeterminateVisibility(false);
            currentTab.setVisibility(View.VISIBLE);
        }
        super.onPostExecute(result);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {
        SkyActivity sky = params[0];
        SolSysPositions pos = sky.getSkyData().getSolSysPositions();
        pos.calcPositions();

        return sky;
    }

}
