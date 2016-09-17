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
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import java.io.PrintWriter;
import java.io.StringWriter;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.PlanetElements;
import de.gdoeppert.sky.model.PlanetPositionHrz;

public class VisibilityView extends View {

    private PlanetPositionHrz[] vislist;
    private final String label;
    private final String invisible;
    private final Paint meshPaint = new Paint();
    private final Path path = new Path();

    class Intervall {
        public int first;
        public int last;
    }

    ;

    Intervall rangeIdx = new Intervall();

    public VisibilityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        label = context.getResources().getString(R.string.visibility_label);
        invisible = context.getResources().getString(R.string.visibility_no);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
        meshPaint.reset();
        meshPaint.setTextSize(0.8f * getContext().getApplicationContext()
                .getResources().getDimensionPixelSize(R.dimen.normalTextSize));
        meshPaint.setStyle(Style.STROKE);
        float w = this.getWidth();
        float h = this.getHeight();
        float h0 = h - 20;
        float hd = h0 / 10;
        float m0 = 1;

        if (vislist != null) {

            if (vislist.length == 0) {
                drawInvisible(canvas, h);
                return;
            }

            float maxvis = PlanetElements.MIN_VISIBILITY - 1f;
            for (PlanetPositionHrz p : vislist) {
                maxvis = (float) Math.max(maxvis, p.getVmag());
            }

            if (maxvis > 3) {
                m0 = 1;
            } else if (maxvis > -2) {
                m0 = -3f;
            } else if (maxvis > PlanetElements.MIN_VISIBILITY) {
                m0 = PlanetElements.MIN_VISIBILITY;
            } else {
                drawInvisible(canvas, h);
                return;
            }

            trimRange(m0, rangeIdx);

            if (rangeIdx.last == rangeIdx.first) {
                if (rangeIdx.last < vislist.length - 1)
                    rangeIdx.last++;
                if (rangeIdx.first > 0)
                    rangeIdx.first--;
            }

            path.reset();
            double x0 = (Math.floor(vislist[rangeIdx.first].getTimeHr()) + 24) % 24;
            double xn = Math.ceil(vislist[rangeIdx.last].getTimeHr()) % 24;
            if (xn == x0)
                xn += 23.99;
            double xd = (w - 25) / ((xn - x0 + 24) % 24);
            meshPaint.setColor(Color.WHITE);
            canvas.drawLine(20, h0, w, h0, meshPaint);

            double duration = (xn - x0 + 24) % 24;

            if (duration > 2) {
                for (int j = (int) x0; (j % 24) != (int) xn; j++) {
                    if ((j + 24.) % 24 == 0) {
                        meshPaint.setColor(Color.LTGRAY);
                    } else {
                        meshPaint.setColor(Color.GRAY);
                    }
                    canvas.drawLine((float) (20 + (j - x0) * xd), h0,
                            (float) (20 + (j - x0) * xd), 0, meshPaint);
                    canvas.drawText(String.format("%02.0f", (j + 24.) % 24),
                            (float) (20 + (j - x0) * xd) - 4, h - 2, meshPaint);
                }
            } else {
                meshPaint.setColor(Color.GRAY);
                double delta = (duration > 1 ? 0.5 : 0.25);
                for (double t = 0; t < duration + 0.1; t += delta) {
                    canvas.drawLine((float) (20 + t * xd), h0, (float) (20 + t
                            * xd), 0, meshPaint);
                    canvas.drawText(formatHour((x0 + t + 24.) % 24),
                            (float) (20 + t * xd) - 4, h - 2, meshPaint);
                }
            }
            meshPaint.setColor(Color.GRAY);
            for (int j = 1; j <= 8; j++) {
                canvas.drawLine(20, h0 - j * hd, w, h0 - j * hd, meshPaint);
                canvas.drawText("" + (int) (j + m0), 0, h0 - j * hd + 4,
                        meshPaint);
            }
            canvas.drawText("mag", 0, h0 - 9 * hd, meshPaint);
            canvas.drawText("h>", 0, h, meshPaint);

            meshPaint.setColor(Color.RED);
            path.moveTo(
                    (float) (20 + ((vislist[rangeIdx.first].getTimeHr() - x0 + 24) % 24)
                            * xd),
                    (float) (h0 - (vislist[rangeIdx.first].getVmag() - m0) * hd));
            for (int j = rangeIdx.first + 1; j <= rangeIdx.last; j++) {
                float x = 20 + (float) (((vislist[j].getTimeHr() - x0 + 24) % 24) * xd);
                float y = (float) (h0 - (vislist[j].getVmag() - m0) * hd);
                if (vislist[j].getTime() > vislist[j - 1].getTime()) {
                    path.lineTo(x, y);
                } else {
                    path.moveTo(x, y);
                }
            }
            canvas.drawPath(path, meshPaint);
        } else {
            meshPaint.setColor(Color.WHITE);
            canvas.drawText(label, 20, h / 2, meshPaint);
        }
    }

    void drawInvisible(Canvas canvas, float h) {
        meshPaint.setColor(Color.WHITE);
        canvas.drawText(invisible, 20, h / 2, meshPaint);
    }

    void trimRange(float m0, Intervall rangeIdx) {
        rangeIdx.first = 0;
        rangeIdx.last = vislist.length - 1;

        while (vislist[rangeIdx.first].getVmag() < m0 - 0.5
                && rangeIdx.first < rangeIdx.last) {
            rangeIdx.first++;
        }
        while (vislist[rangeIdx.last].getVmag() < m0 - 0.5
                && rangeIdx.first < rangeIdx.last) {
            rangeIdx.last--;
        }
    }

    private String formatHour(double d) {
        int hr = (int) Math.floor(d);
        int min = (int) ((d - hr) * 60);
        StringWriter result = new StringWriter();
        PrintWriter pr = new PrintWriter(result);
        pr.printf("%02d:%02d", hr, min);
        return result.getBuffer().toString();
    }

    public void setVisCurve(PlanetPositionHrz[] trajV) {
        vislist = trajV;
    }
}
