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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.EquatorialViewGL;
import de.gdoeppert.sky.gui.SkyActivity;
import de.gdoeppert.sky.model.Star;

public class EquatorialBackgroundTask extends
        AsyncTask<SkyActivity, String, SkyActivity> {

    Context context;
    View currentTab;

    public EquatorialBackgroundTask(View ecl, Context ctx) {
        currentTab = ecl;
        context = ctx;
    }

    @Override
    protected void onPostExecute(SkyActivity result) {

        EquatorialViewGL eclV = (EquatorialViewGL) currentTab
                .findViewById(SkyActivity.ECLIPTIC_VIEW_ID);

        eclV.setStars(stars);

        eclV.invalidate();

        super.onPostExecute(result);

        eclV.setPlanetsReady(true);
        eclV.setStarsReady(true);
    }

    @Override
    protected SkyActivity doInBackground(SkyActivity... params) {
        SkyActivity sky = params[0];

        synchronized (lock) {
            if (stars == null) {
                readStarLocations();
            }
        }
        if (isCancelled()) {
            return sky;
        }

        Log.println(Log.DEBUG, "equat", "stars ready");

        return sky;
    }

    private static Object lock = new Object();
    private static Vector<Star> stars = null;

    @SuppressWarnings("unchecked")
    private void readStarLocations() {

        if (stars != null)
            return;

        Vector<Star> stars = null;
        Log.println(Log.DEBUG, "equat", "read stars");

        try {
            FileInputStream input = context.openFileInput("stars.bin");
            ObjectInputStream objInput = new ObjectInputStream(input);
            stars = (Vector<Star>) objInput.readObject();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }

        if (stars == null) {

            stars = new Vector<Star>(900);

            try {

                InputStream istr = context.getResources().openRawResource(
                        R.raw.bright_stars);
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        istr));
                while (true) {
                    String line = rd.readLine();
                    if (line == null)
                        break;
                    String[] cols = line.split("[,\t]");
                    if (cols.length < 8)
                        continue;
                    Star star = new Star(Double.valueOf(cols[0]),
                            Double.valueOf(cols[1]), Double.valueOf(cols[2]),
                            cols[3], Double.valueOf(cols[4]),
                            Double.valueOf(cols[5]), Double.valueOf(cols[6]),
                            Double.valueOf(cols[7]));
                    stars.add(star);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ObjectOutputStream oout = null;
            try {
                FileOutputStream out = context.openFileOutput("stars.bin", 0);
                oout = new ObjectOutputStream(out);
                oout.writeObject(stars);
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            } finally {
                try {
                    oout.close();
                } catch (IOException e) {
                }
            }
        }

        EquatorialBackgroundTask.stars = stars;

        Log.println(Log.DEBUG, "equat", "stars ready");
    }

}
