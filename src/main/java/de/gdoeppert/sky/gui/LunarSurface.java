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

public class LunarSurface extends View {

    protected Canvas canvas = null;
    protected RectF oval = new RectF();
    protected RectF ovalBr = new RectF();
    protected RectF ovalMd = new RectF();
    protected RectF ovalEq = new RectF();
    protected double phase;
    protected float brightLimb;
    protected float parallactic;
    protected float p;
    protected float b0;
    protected float l0;
    protected boolean posSet = false;
    Paint paint = new Paint();

    public LunarSurface(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setP(float angle) {
        p = angle;
        posSet = true;
    }

    public void setB0(float angle) {
        b0 = angle;
    }

    public void setL0(double l) {
        l0 = (float) l;
    }

    public void setPhase(double p) {
        phase = p;
    }

    public void setBLimb(double bl) {
        brightLimb = (float) bl;
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

        paint.reset();

        canvas.drawColor(Color.TRANSPARENT);

        canvas.translate(x, y);
        canvas.save();

        paint.setStyle(Style.FILL);
        paint.setColor(0xff404040);
        oval.set(-r, -r, r, r);
        canvas.drawArc(oval, 0, 360, false, paint);

        // -------------------------

        float angle = (brightLimb > 180 ? 270 : 90);
        canvas.rotate(angle + parallactic - brightLimb);

        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setStyle(Style.FILL);
        canvas.drawArc(oval, angle, 180, false, paint);
        float s = FloatMath.cos((float) (Math.PI * phase / 180.0));
        if (phase >= 90) {
            paint.setColor(0xff404040);
        } else {
            paint.setColor(Color.WHITE);
            angle += 180;
        }

        float q = Math.abs(s) * r;

        ovalBr.set(-q, -r, q, r);
        paint.setAlpha(255);
        paint.setStyle(Style.FILL);
        canvas.drawArc(ovalBr, angle, 360, false, paint);
        /*
         * paint.setStyle(Style.STROKE); paint.setColor(Color.RED);
		 * canvas.drawLine(0, 0, brightLimb > 180 ? r : -r, 0, paint);
		 */

        if (posSet) {
            canvas.restore();
            canvas.rotate(-p + parallactic);

            paint.setStyle(Style.STROKE);
            paint.setColor(Color.BLUE);

            float l1 = 0, a1 = 90;
            l1 = FloatMath.sin((float) (l0 / 180.0 * Math.PI));
            if (l1 < 0) {
                l1 = -l1;
                a1 = 270;
            }

            ovalMd.set(-l1 * r, -r, l1 * r, r);

            canvas.drawArc(ovalMd, a1, 180, false, paint);

            paint.setColor(0xff008000);
            float b1 = 0;
            a1 = 0;
            b1 = FloatMath.sin((float) (b0 / 180.0 * Math.PI));
            if (b1 < 0) {
                b1 = -b1;
                a1 = 180;
            }

            ovalEq.set(-r, -b1 * r, r, b1 * r);

            canvas.drawArc(ovalEq, a1, 180, false, paint);

            canvas.drawLine(r, 0, r - 5, -3, paint);
            canvas.drawLine(r, 0, r - 5, 3, paint);
        }

    }
}
