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
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import de.gdoeppert.sky.R;

public class LunarOrbitView extends View {

    public void setSolarLong(float solarLong) {
        this.solarLong = solarLong;
    }

    public void setAscLong(float ascLong) {
        this.ascLong = ascLong;
    }

    public void setPerigeeLong(float perigeeLong) {
        this.perigeeLong = perigeeLong;
    }

    public void setLunarLong(float lunarLong) {
        this.lunarLong = lunarLong;
    }

    protected RectF oval = new RectF();
    protected Canvas canvas = null;
    protected float solarLong;
    protected float ascLong;
    protected float perigeeLong;
    protected float lunarLong;
    protected Paint paint = new Paint();

    public LunarOrbitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        paint.reset();
        paint.setTextSize(getContext().getApplicationContext().getResources()
                .getDimensionPixelSize(R.dimen.normalTextSize));
        canvas.drawColor(Color.TRANSPARENT);
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Style.STROKE);
        float x = this.getWidth();
        float y = this.getHeight();
        float r = Math.min(x, y) * 0.4f;

        oval.set(x * 0.45f - r, y / 2 - r, x * 0.45f + r, y / 2 + r);

        canvas.drawOval(oval, paint);

        drawLunarOrbit();
    }

    public void drawLunarOrbit() {
        paint.setColor(Color.GREEN);
        paint.setStyle(Style.FILL);
        paint.setAlpha(50);
        canvas.drawArc(oval, solarLong - ascLong, -180, true, paint);
        paint.setColor(Color.CYAN);
        paint.setStyle(Style.FILL);
        paint.setAlpha(25);
        canvas.drawArc(oval, solarLong, -180, true, paint);
        float r = oval.right - oval.centerX();
        float x = (float) (oval.centerX() + r * 1.07
                * Math.cos((solarLong - ascLong) / 180f * Math.PI));
        float y = (float) (oval.centerY() + r * 1.07
                * Math.sin((solarLong - ascLong) / 180f * Math.PI));
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        paint.setTextAlign(Align.CENTER);
        canvas.drawText("Ω", x, y, paint);

        paint.setColor(Color.RED);
        paint.setStyle(Style.STROKE);
        paint.setAlpha(250);
        x = (float) (oval.centerX() + r
                * Math.cos((solarLong - perigeeLong) / 180f * Math.PI));
        y = (float) (oval.centerY() + r
                * Math.sin((solarLong - perigeeLong) / 180f * Math.PI));
        float x2 = (float) (oval.centerX() + r
                * Math.cos((solarLong - perigeeLong + 180) / 180f * Math.PI));
        float y2 = (float) (oval.centerY() + r
                * Math.sin((solarLong - perigeeLong + 180) / 180f * Math.PI));
        canvas.drawLine(x, y, x2, y2, paint);
        paint.setColor(Color.WHITE);
        paint.setAlpha(255);
        x = (float) (oval.centerX() + r * 0.6
                * Math.cos((solarLong - perigeeLong) / 180f * Math.PI));
        y = (float) (oval.centerY() + r * 0.6
                * Math.sin((solarLong - perigeeLong) / 180f * Math.PI));
        canvas.drawText("ω", x, y, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Style.STROKE);
        paint.setAlpha(255);
        x = (float) (oval.centerX() + r
                * Math.cos((solarLong - lunarLong) / 180f * Math.PI));
        y = (float) (oval.centerY() + r
                * Math.sin((solarLong - lunarLong) / 180f * Math.PI));
        canvas.drawCircle(x, y, 10, paint);
        RectF medilun = new RectF(x - 10, y - 10, x + 10, y + 10);
        paint.setStyle(Style.FILL);
        paint.setAlpha(200);
        canvas.drawArc(medilun, 270, 180, true, paint);

        paint.setColor(Color.YELLOW);
        paint.setStyle(Style.FILL);
        paint.setAlpha(200);
        x = getWidth() - 10;
        y = (oval.centerY());
        canvas.drawCircle(x, y, 10, paint);

        paint.setColor(Color.BLUE);
        paint.setStyle(Style.FILL);
        paint.setAlpha(200);
        x = (oval.centerX());
        y = (oval.centerY());
        canvas.drawCircle(x, y, 10, paint);

    }

}
