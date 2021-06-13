package de.gdoeppert.sky.gui;

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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.PlanetPositionEcl;
import de.gdoeppert.sky.model.SolSysPositions;

public class SolsysView extends View {

    public void setPlanetsReady(boolean ovwReady) {
        this.planetsReady = ovwReady;
    }

    public void setPlanetColors(int[] planetColors) {
        this.planetColors = planetColors;
    }

    public void setEclipticPosition(PlanetPositionEcl[] eclipticPosition2) {
        this.eclipticPosition = eclipticPosition2;
    }

    private final String[] planet_names_short;
    private int h;
    private int w;
    private int planetColors[];
    private PlanetPositionEcl[] eclipticPosition;
    private boolean planetsReady = false;
    private float starSize;

    Paint paint = new Paint();

    public SolsysView(Context context, AttributeSet attrs) {
        super(context, attrs);
        planet_names_short = context.getResources().getStringArray(
                R.array.planets_short_array);
        context.getResources().getStringArray(R.array.bodies_array);
        planetsReady = false;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        w = this.getWidth();
        h = this.getHeight();
        Math.max(w, h);
        Math.min(w, h);

        paint.reset();
        paint.setTextSize(getContext().getApplicationContext().getResources()
                .getDimensionPixelSize(R.dimen.normalTextSize));
        starSize = getContext().getApplicationContext().getResources()
                .getDimensionPixelSize(R.dimen.normalStarSize);

        canvas.drawColor(Color.TRANSPARENT);
        if (planetsReady) {
            drawOverview(canvas, paint);
        }

    }

    private void drawOverview(Canvas canvas, Paint paint) {

        if (planetColors == null)
            return;

        float cx = w / 2;
        float cy = h / 2 + 4;

        float r = Math.min(cx, cy) * 0.9f;

        paint.setStyle(Style.STROKE);
        paint.setAlpha(60);
        paint.setColor(Color.GREEN);
        canvas.drawLine(cx, cy, cx, 0, paint);
        paint.setColor(Color.RED);
        canvas.drawLine(cx, cy, 0, cy, paint);
        paint.setColor(Color.BLUE);
        canvas.drawLine(cx, cy, w, cy, paint);
        paint.setColor(Color.MAGENTA);
        canvas.drawLine(cx, cy, cx, h, paint);

        paint.setStyle(Style.FILL);
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(cx, cy, 8, paint);

        double perihelion;

        for (int j = 2; j < SolSysPositions.idx_count; j++) {
            paint.setAlpha(80);
            paint.setStyle(Style.STROKE);
            paint.setColor(Color.DKGRAY);
            canvas.drawCircle(cx, cy, r * (j - 1) / 8, paint);
            paint.setStyle(Style.FILL);
            float angle;
            angle = (float) eclipticPosition[j].getLongitude();
            paint.setColor(planetColors[j]);
            perihelion = eclipticPosition[j].getPerihelion();
            float px = (float) (r * (j - 1) / 8f * Math.sin(-angle * Math.PI
                    / 180));
            float py = -(float) (r * (j - 1) / 8f * Math.cos(-angle * Math.PI
                    / 180));

            float ppfx = (float) (Math.sin(-perihelion * Math.PI / 180));
            float ppfy = -(float) (Math.cos(-perihelion * Math.PI / 180));
            float ppx = r * (j - 1) / 8f * ppfx;
            float ppy = r * (j - 1) / 8f * ppfy;
            paint.setAlpha(250);
            canvas.drawCircle(cx + px, cy + py, 4 * starSize, paint);
            canvas.drawText(planet_names_short[j - 2].substring(0, 1), cx + px
                    + 5, cy + py - 5, paint);
            canvas.drawLine(cx + ppx - 8 * starSize * ppfx,
                    cy + ppy - 8 * starSize * ppfy, cx + ppx, cy + ppy, paint);
        }
    }

    public void setSouthern(boolean b) {

    }

}
