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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import de.gdoeppert.sky.Messages;
import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.PlanetElements;
import de.gdoeppert.sky.model.PlanetMoon;

public class PlanetMoonsView extends View {

    protected Canvas canvas = null;
    protected Paint paint = new Paint();

    private PlanetMoon[] planetMoons = null;
    private double maxRadius;
    private final Rect bounds = new Rect();

    public PlanetMoonsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPlanetMoons(PlanetMoon[] m, double maxRadius) {
        planetMoons = m;
        this.maxRadius = maxRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (planetMoons == null)
            return;

        this.canvas = canvas;

        float x = this.getWidth() * 0.5f;
        float y = this.getHeight() * 0.6f;
        float r = x;

        paint.reset();

        paint.setTextSize(getContext().getApplicationContext().getResources()
                .getDimensionPixelSize(R.dimen.normalTextSize));
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setStyle(Style.FILL);
        canvas.drawCircle(x, y, (float) (r / maxRadius), paint);

        paint.setColor(Color.CYAN);

        int xText = 5;
        for (int j = 0; j < planetMoons.length; j++) {
            PlanetMoon moon = planetMoons[j];
            paint.setColor(moon.getColor());
            drawMoon(moon, canvas, paint, r, x, y);
            String name = moon.getName();
            paint.getTextBounds(name, 0, name.length(), bounds);
            canvas.drawText(name, xText, 20, paint);
            xText += bounds.width() + 12;
        }

    }

    void drawMoon(PlanetMoon moon, Canvas canvas, Paint paint, float r,
                  float x, float y) {

        float cx = (float) (x + moon.getX() / maxRadius * r);
        float cy = (float) (y - moon.getY() / maxRadius * r);

        if (!moon.isOcultated()) {
            canvas.drawCircle(cx, cy, 2, paint);
        }
    }

    public void update(PlanetElements planetElements, final SkyActivity sky) {

        PlanetMoon[] moons = planetElements.getMoons();
        if (moons != null) {
            setMinimumHeight((int) (planetElements.getMaxMoonDecl() * 90));
            setPlanetMoons(moons, planetElements.getMaxRadiusMoonOrbit());
            setVisibility(View.VISIBLE);
            final StringBuffer info = new StringBuffer();
            for (PlanetMoon moon : moons) {
                if (moon.isOcultated())
                    info.append(moon.getName()
                            + " " + Messages.getString("PlanetTask.ocultated") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                if (moon.isTransit())
                    info.append(moon.getName()
                            + " " + Messages.getString("PlanetTask.in_transit") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                if (moon.isShadowTransit())
                    info.append(moon.getName()
                            + " " + Messages.getString("PlanetTask.in_shadowTransit") + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
            if (info.length() > 0) {
                setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        sky.showMessage(
                                Messages.getString("PlanetMoonsView.0"), info.toString()); //$NON-NLS-1$
                    }
                });
                setClickable(true);
                setBackgroundColor(Color.DKGRAY);
            } else {
                setClickable(false);
                setBackgroundColor(Color.TRANSPARENT);
            }
        } else {
            setVisibility(View.GONE);
        }
    }
}
