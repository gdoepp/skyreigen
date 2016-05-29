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
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.View;

public class SunSurface extends View {

    protected Canvas canvas = null;

    protected float p;
    protected float b0;
    protected float parallactic;
    protected Paint paint = new Paint();
    protected RectF oval = new RectF();

    public SunSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setP(float angle) {
        p = angle;
    }

    public void setB0(float angle) {
        b0 = angle;
    }

    public void setParallactic(double pl) {
        parallactic = (float) pl;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;

        float x = this.getWidth() * 0.45f;
        float y = this.getHeight() * 0.45f;
        float r = Math.min(x, y);

        canvas.drawColor(Color.TRANSPARENT);
        paint.reset();
        canvas.translate(x, y);
        canvas.rotate(-p + parallactic);

        paint.setStyle(Style.FILL);
        paint.setColor(0xffffff80);

        oval.set(-r, -r, r, r);

        canvas.drawArc(oval, 0, 360, false, paint);

        paint.setStyle(Style.STROKE);
        paint.setColor(Color.BLUE);
        canvas.drawLine(0, r, 0, -r, paint);

        paint.setColor(0xff009000);
        float b1 = 0, a1 = 0;
        b1 = FloatMath.sin((float) (b0 / 180.0 * Math.PI));
        if (b1 < 0) {
            b1 = -b1;
            a1 = 190;
        }

        RectF ovalEq = new RectF(-r, -b1 * r, r, b1 * r);

        canvas.drawArc(ovalEq, a1, 180, false, paint);

        canvas.drawLine(r, 0, r - 5, -3, paint);
        canvas.drawLine(r, 0, r - 5, 3, paint);
    }
}
