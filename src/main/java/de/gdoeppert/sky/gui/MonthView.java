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
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.RiseSetCalculator.RSTitem;
import de.gdoeppert.sky.model.RiseSetCalculator.RiseSetAll;
import de.gdoeppert.sky.model.SolSysElements;

public class MonthView extends View implements OnTouchListener {

    protected RiseSetAll[] rst = null;
    protected SolSysElements se = null;
    protected DashPathEffect dpeS = new DashPathEffect(new float[]{10, 4, 4,
            4}, 0);
    protected DashPathEffect dpeT = new DashPathEffect(
            new float[]{4, 6, 4, 6}, 0);
    protected DashPathEffect dpeR = new DashPathEffect(new float[]{3, 0}, 0);
    private final Paint paint = new Paint();
    private final Path path = new Path();
    private float w;
    private float h;
    private float x1;
    private float y1;
    private float dy;
    private float dx;
    private final boolean[] showPlanet = new boolean[]{true, true, true,
            true, true, true, true};

    private String[] planetNames;
    private final int[] cols = new int[]{Color.MAGENTA, Color.YELLOW,
            Color.RED, Color.WHITE, 0xffffaf00, Color.GREEN, Color.BLUE};
    private float xNoon;
    private int toDraw;
    private boolean onoff = true;
    public static final int b_rise = 1;
    public static final int b_set = 2;
    public static final int b_transit = 4;

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnTouchListener(this);
    }

    public void setToDraw(int d) {
        toDraw = d;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);

        w = this.getWidth();
        x1 = w / 20;
        w -= 2 * x1;
        h = this.getHeight();
        y1 = h / 30;
        h -= 2 * y1;

        paint.reset();

        paint.setStyle(Style.STROKE);
        paint.setAlpha(255);
        paint.setColor(Color.WHITE);

        if (rst == null) {
            paint.setTextSize(getContext().getApplicationContext()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.normalTextSize));
            canvas.drawText("calculating", 20, h / 2.0f, paint);
            return;
        }

        Log.d("monthview", "drawing");

        dy = 1.0f * h / rst.length;
        dx = w;

        xNoon = se
                .localTime(rst[14].rst[RSTitem.sun.ordinal()].getTransitNum());

        drawAxes(canvas);

        paint.setStrokeWidth(4);

        drawMoon(canvas, RSTitem.moon.ordinal(), 0x6fb0b0b0);

        drawBg(canvas, RSTitem.astroTw.ordinal(), 0x6f282898);
        drawBg(canvas, RSTitem.nautTw.ordinal(), 0x6f2828b0);
        drawBg(canvas, RSTitem.civilTw.ordinal(), 0x6f6060d0);
        drawBg(canvas, RSTitem.sun.ordinal(), 0x6fe0e0ff);

        for (int j = 0; j < RSTitem.values().length - RSTitem.mercury.ordinal(); j++) {
            if ((toDraw & b_rise) != 0 && showPlanet[j]) {
                drawRise(canvas, j + RSTitem.mercury.ordinal(), cols[j]);
            }
            if ((toDraw & b_set) != 0 && showPlanet[j]) {
                drawSet(canvas, j + RSTitem.mercury.ordinal(), cols[j]);
            }
            if ((toDraw & b_transit) != 0 && showPlanet[j]) {
                drawTransit(canvas, j + RSTitem.mercury.ordinal(), cols[j]);
            }
        }
    }

    void drawAxes(Canvas canvas) {

        paint.setStyle(Style.STROKE);
        paint.setTextSize(getContext().getApplicationContext().getResources()
                .getDimensionPixelSize(R.dimen.normalTextSize) * 0.7f);

        // canvas.drawLine(x1, 0, x1, y1 + (rst.length - 1) * dy, paint);

        for (RiseSetAll r : rst) {

            paint.setColor(r.day % 5 == 0 ? 0xefffffff : 0x9f7f7f7f);

            canvas.drawLine((r.day % 10 == 0 ? x1 / 2 : x1), y1 + (r.day - 1)
                    * dy, x1 + 1 * dx, y1 + (r.day - 1) * dy, paint);
            if (r.day % 5 == 0) {
                canvas.drawText(r.day + ".", x1 / 10, y1 + (r.day - 1) * dy,
                        paint);
            }
        }

        canvas.drawLine(0, y1, x1 + w, y1, paint);

        for (int j = 0; j < 24; j++) {

            paint.setColor(j % 3 % 5 == 0 ? 0xefffffff : 0x9f7f7f7f);

            float x = (24f + j - xNoon * 24f + 12f) % 24f;

            canvas.drawLine(x1 + x * dx / 24f, (j % 3 == 0 ? 0 : y1 / 2f), x1
                    + x / 24f * dx, y1 + (rst.length - 1) * dy, paint);
            if (j % 3 == 0) {
                canvas.drawText(j + "h", x1 + x * dx / 24f, y1 * 0.9f, paint);
            }
        }

        for (int j = 0; j < planetNames.length; j++) {
            if (j < cols.length) {

                paint.setColor(Color.DKGRAY);
                Rect rect = new Rect();
                String name = "  " + planetNames[j] + "  ";
                paint.getTextBounds(name, 0, name.length(), rect);
                rect.offset((int) (x1 + j * dx * 0.14f), (int) (y1 + rst.length * dy - 2));
                rect.bottom += 5;
                rect.top -= 5;
                rect.left -= 5;
                rect.right += 5;
                paint.setStyle(Style.FILL);
                canvas.drawRect(rect, paint);
                //paint.setStyle(Style.STROKE);

                paint.setColor(cols[j]);
                canvas.drawText(name, x1 + j * dx * 0.14f, y1
                        + rst.length * dy - 2, paint);
            }
        }

    }

    void drawRise(Canvas canvas, int index, int col) {

        path.reset();
        paint.setPathEffect(dpeR);
        paint.setColor(col);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(4);

        boolean inLine = false;
        float xold = 0;
        for (RiseSetAll r : rst) {

            float y = (r.day - 1) * dy;
            float x = (1 + se.localTime(r.rst[index].getRiseNum()) - xNoon + 0.5f);
            x %= 1;
            x *= dx;

            boolean xValid = (r.rst[index].isRising());

            if (r.rst[index].isAlwaysAbove()) {
                x = (index / 10f - 0.7f) * x1;
                xValid = true;
            }

            if (inLine && xValid && Math.abs(xold - x) < w / 2) {
                path.lineTo(x1 + x, y1 + y);
                xold = x;
            } else if (xValid) {
                path.moveTo(x1 + x, y1 + y);
                inLine = true;
                xold = x;
            } else {
                inLine = false;
            }
        }
        canvas.drawPath(path, paint);
    }

    void drawSet(Canvas canvas, int index, int col) {

        path.reset();
        paint.setPathEffect(dpeS);
        paint.setColor(col);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(4);

        boolean inLine = false;
        float xold = 0;
        for (RiseSetAll r : rst) {
            float y = (r.day - 1) * dy;
            float x = (1 + se.localTime(r.rst[index].getSetNum()) - xNoon + 0.5f);
            x %= 1;
            x *= dx;

            boolean xValid = r.rst[index].isSetting();

            if (r.rst[index].isAlwaysAbove()) {
                x = 1 * dx + (0.0f + index / 10f) * x1;
                xValid = true;
            }

            if (inLine && xValid && Math.abs(xold - x) < w / 2) {
                path.lineTo(x1 + x, y1 + y);
                xold = x;
            } else if (xValid) {
                path.moveTo(x1 + x, y1 + y);
                xold = x;
                inLine = true;
            } else {
                inLine = false;
            }
        }
        canvas.drawPath(path, paint);
    }

    void drawTransit(Canvas canvas, int index, int col) {

        path.reset();
        paint.setPathEffect(dpeT);
        paint.setColor(col);
        paint.setStrokeWidth(4);
        paint.setStyle(Style.STROKE);

        boolean inLine = false;
        float xold = 0;
        for (RiseSetAll r : rst) {
            float y = (r.day - 1) * dy;
            float x = (1 + se.localTime(r.rst[index].getTransitNum()) - xNoon + 0.5f);
            x %= 1;
            x *= dx;

            boolean xValid = !r.rst[index].isAlwaysBelow();

            if (inLine && xValid && Math.abs(xold - x) < w / 2) {
                path.lineTo(x1 + x, y1 + y);
                xold = x;
            } else if (xValid) {
                path.moveTo(x1 + x, y1 + y);
                xold = x;
                inLine = true;
            } else {
                inLine = false;
            }
        }
        canvas.drawPath(path, paint);
    }

    void drawMoon(Canvas canvas, int index, int col) {

        path.reset();
        paint.setStyle(Style.FILL);

        int alpha = (col & 0xff000000) >> 24;
        int gray = col & 0xff;

        for (RiseSetAll r : rst) {

            float y = (r.day - 1) * dy;

            float xr;
            float xs;

            if (r.rst[index].isAlwaysAbove()) {
                xr = 0;
                xs = x1 + dx;
            } else if (r.rst[index].isAlwaysBelow()) {
                xr = 0;
                xs = 0;
            } else {
                xr = se.localTime(r.jd, r.rst[index].getRiseNum1d());
                xs = se.localTime(r.jd, r.rst[index].getSetNum1d());

                xr = xr - xNoon + 0.5f;
                xr *= dx;

                xs = xs - xNoon + 0.5f;
                xs *= dx;
            }

            if (xr < x1)
                xr = x1;
            if (xs < x1)
                xs = x1;

            if (xr > dx)
                xr = dx;
            if (xs > dx)
                xs = dx;

            int colGray = (int) (gray * r.moonLight);
            col = ((int) (alpha * r.moonLight)) << 24
                    | (colGray | colGray << 8 | colGray << 16);

            paint.setColor(col);

            if (xr < xs) {
                canvas.drawRect(x1 + xr, y1 + y - 0.5f * dy, x1 + xs, y1 + y
                        + 0.5f * dy, paint);
            } else if (xr > xs) {
                canvas.drawRect(x1, y1 + y - 0.5f * dy, x1 + xs, y1 + y + 0.5f
                        * dy, paint);
                canvas.drawRect(x1 + xr, y1 + y - 0.5f * dy, x1 + dx, y1 + y
                        + 0.5f * dy, paint);
            }
        }

    }

    void drawBg(Canvas canvas, int index, int col) {

        // there is at most one change between "below" and not below within one
        // month

        path.reset();

        paint.setColor(col);
        paint.setStyle(Style.FILL);

        boolean inArea = false;

        boolean below = (rst[0].rst[index].isAlwaysBelow());

        for (RiseSetAll r : rst) {

            float y = (r.day - 1) * dy;

            float x = se.localTime(r.rst[index].getRiseNum());

            x = x - xNoon + 0.5f;

            x *= dx;

            if (r.rst[index].isAlwaysAbove()) {
                x = 0;
            } else if (r.rst[index].isAlwaysBelow()) {
                if (below) {
                    continue;
                }
                below = true;
                continue;
            } else if (x > w / 2) {
                x -= 1 * dx;
            }
            below = false;

            if (inArea) {
                path.lineTo(x1 + x, y1 + y);
            } else {
                path.moveTo(x1 + x, y1 + y);
                inArea = true;
            }
        }

        for (int j = rst.length - 1; j >= 0; j--) {
            RiseSetAll r = rst[j];

            float y = (r.day - 1) * dy;

            float x = se.localTime(r.rst[index].getSetNum());

            x = x - xNoon + 0.5f;

            x *= dx;

            if (r.rst[index].isAlwaysAbove()) {
                x = dx;
            } else if (r.rst[index].isAlwaysBelow()) {
                if (below)
                    continue;
                x = 0;
                below = true;
                continue;
            } else if (x < w / 2) {
                x += 1 * dx;
            }
            below = false;

            path.lineTo(x1 + x, y1 + y);

        }
        path.close();
        canvas.drawPath(path, paint);
    }

    public void setRst(RiseSetAll[] rst) {
        this.rst = rst;
    }

    public void setSolSysElements(SolSysElements se) {
        this.se = se;
    }

    protected void showTable(SkyActivity activity) {

        if (rst == null || se == null)
            return;

        StringBuffer sb = new StringBuffer();
        for (RiseSetAll r : rst) {
            sb.append(String.format("%02d: ", r.day));
            sb.append(se.getRise(r.rst[RSTitem.astroTw.ordinal()]) + ","
                    + se.getRise(r.rst[RSTitem.nautTw.ordinal()])
                    + se.getRise(r.rst[RSTitem.civilTw.ordinal()])
                    + se.getRise(r.rst[RSTitem.sun.ordinal()]) + " / "
                    + se.getSet(r.rst[RSTitem.sun.ordinal()])
                    + se.getSet(r.rst[RSTitem.civilTw.ordinal()])
                    + se.getSet(r.rst[RSTitem.nautTw.ordinal()])
                    + se.getSet(r.rst[RSTitem.astroTw.ordinal()]) + " :");

            for (int j = RSTitem.mercury.ordinal(); j <= RSTitem.neptune
                    .ordinal(); j++) {
                sb.append(se.getRise(r.rst[j]) + "/" + se.getSet(r.rst[j])
                        + ", ");
            }
            sb.append("\n");
        }
        activity.showMessage("month", sb.toString());
    }

    public void setPlanetNames(String[] planetNames) {
        this.planetNames = planetNames;
    }

    @Override
    public boolean onTouch(View view, MotionEvent ev) {
        float x = ev.getX();
        float y = ev.getY();
        if (y > h - y1 && ev.getAction() == MotionEvent.ACTION_DOWN) {
            x = (x - x1) / (dx * 0.14f);
            int j = (int) Math.floor(x);

            if (j >= 0 && j < showPlanet.length) {
                showPlanet[j] = !showPlanet[j];
                this.invalidate();
            }
        }
        if (y > y1 && y < h - y1 && ev.getAction() == MotionEvent.ACTION_DOWN) {
            x = (x - x1) / (dx * 0.14f);
            int n = (int) Math.floor(x);
            if (n==3) {
                onoff = !onoff;
                for (int j = 0; j < showPlanet.length; j++) {
                    showPlanet[j] = onoff;
                }
                this.invalidate();
            }
        }
        return false;
    }

    public RiseSetAll[] getRst() {
        return rst;

    }
}
