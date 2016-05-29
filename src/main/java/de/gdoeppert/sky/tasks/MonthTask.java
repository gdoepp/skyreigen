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

import java.util.Calendar;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.MonthView;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.model.RiseSetCalculator;
import de.gdoeppert.sky.model.RiseSetCalculator.RiseSetAll;
import de.gdoeppert.sky.model.SolarElements;

public class MonthTask extends AsyncTask<SkyActivity, String, SkyActivity> {

    Context context;
    View currentTab;
    private RiseSetAll[] rst = null;
    private final int month;
    private final int year;
    private final String[] planetNames;

    public MonthTask(View ecl, Context ctx, int m, int y, String[] planetNames) {
        currentTab = ecl;
        context = ctx;
        month = m;
        year = y;
        this.planetNames = planetNames;
    }

    @Override
    protected void onPreExecute() {
        // currentTab.setVisibility(View.INVISIBLE);
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(SkyActivity result) {
        MonthView view = (MonthView) currentTab.findViewById(R.id.monthView);
        view.setRst(rst);
        view.setPlanetNames(planetNames);
        view.setSolSysElements(result.getSkyData().getSolarElements());
        view.invalidate();
        super.onPostExecute(result);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {
        SkyActivity sky = params[0];

        SolarElements se = sky.getSolarElements();
        Calendar cal = sky.getSkyData().getCalClone();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);

        rst = RiseSetCalculator.createRstForMonth(se.getObserver(), cal,
                se.getGmt());

        return sky;
    }
}
