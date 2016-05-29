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
import android.util.FloatMath;
import android.view.View;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.DisplayParams;
import de.gdoeppert.sky.model.PlanetPositionHrz;

public class PolarView extends View {

    private PlanetPositionHrz[] traj;
    private PlanetPositionHrz[] trajWinter;
    private PlanetPositionHrz[] trajSummer;
    private PlanetPositionHrz[] trajEqui;
    private PlanetPositionHrz posNow;
    private float[] orientation;
    protected final String[] dir;
    protected DisplayParams displayParms;
    protected Paint meshPaint = new Paint();
    protected Paint areaPaint = new Paint();

    public PolarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            dir = context.getResources().getStringArray(
                    R.array.directions_array);
        } else
            dir = new String[]{"", "", "", "", "", "", "", ""};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            meshPaint.reset();
            meshPaint.setTextSize(getContext().getApplicationContext()
                    .getResources()
                    .getDimensionPixelSize(R.dimen.normalTextSize));
            canvas.drawColor(Color.TRANSPARENT);
            meshPaint.setColor(Color.LTGRAY);
            meshPaint.setStyle(Style.STROKE);
            float x = this.getWidth();
            float y = this.getHeight();
            float r = Math.min(x, y) / 22;

            for (int j = 9; j > 0; j--) {
                canvas.drawCircle(x / 2, y / 2, r * j, meshPaint);
                if (j % 3 == 0 && j != 0 && j != 9) {
                    canvas.drawText((9 - j) * 10 + "°", x / 2, y / 2 - r * j
                            + 5, meshPaint);
                }
            }

            for (int j = 0; j < 360; j += 15) {
                float dx = (float) (Math.sin(j / 180.0 * Math.PI) * r);
                float dy = (float) (-Math.cos(j / 180.0 * Math.PI) * r);
                canvas.drawLine(x / 2, y / 2, x / 2 + dx * 9, y / 2 + dy * 9,
                        meshPaint);
                if (j % 45 == 0) {

                    String hour = dir[j / 45];
                    canvas.drawText(hour, (float) (x / 2 + dx * 10.4 - 5), y
                            / 2 + dy * 10 + 5, meshPaint);
                }
            }

            if (trajWinter != null || trajSummer != null) {
                areaPaint.reset();
                areaPaint.setColor(Color.YELLOW);
                areaPaint.setStyle(Style.FILL);
                areaPaint.setAlpha(96);
                drawRange(canvas, trajWinter, trajSummer, areaPaint);
            }

            if (trajEqui != null) {
                meshPaint.setStrokeWidth(2);
                meshPaint.setColor(Color.CYAN);
                drawTraj(canvas, trajEqui, meshPaint);
            }

            if (traj != null && traj.length > 0) {
                meshPaint.setColor(Color.RED);
                meshPaint.setStrokeWidth(2);
                drawTraj(canvas, traj, meshPaint);

                if (traj[0].getAltitude() < 2) {
                    float azRise = (float) traj[0].getAzimuth();
                    float azRise0 = azRise;
                    if (azRise0 >= 90)
                        azRise0 = 105;
                    else
                        azRise0 = 75;
                    float ex = (float) (Math.sin(azRise0 / 180.0 * Math.PI) * r * 9.2);
                    float ey = (float) (-Math.cos(azRise0 / 180.0 * Math.PI)
                            * r * 9.2);

                    drawDegrees(canvas, x / 2 + ex, y / 2 + ey, azRise,
                            meshPaint, Paint.Align.LEFT);
                }
                if (traj[traj.length - 1].getAltitude() < 2) {
                    float azSet = (float) traj[traj.length - 1].getAzimuth();
                    float azSet0 = azSet;
                    if (azSet0 > 270)
                        azSet0 = 285;
                    else
                        azSet0 = 255;

                    float ex = (float) (Math.sin(azSet0 / 180.0 * Math.PI) * r * 9.2);
                    float ey = (float) (-Math.cos(azSet0 / 180.0 * Math.PI) * r * 9.2);

                    drawDegrees(canvas, x / 2 + ex, y / 2 + ey, azSet,
                            meshPaint, Paint.Align.RIGHT);
                }
            }
            if (posNow != null) {
                double alt = posNow.getAltitude();
                if (alt < 0.5) {
                    alt = -alt;
                    meshPaint.setColor(Color.BLUE);
                } else {
                    meshPaint.setColor(Color.GREEN);
                }
                float ex = (float) (Math.sin(posNow.getAzimuth() / 180.0
                        * Math.PI)
                        * r * (90 - alt) / 10.0);
                float ey = (float) (-Math.cos(posNow.getAzimuth() / 180.0
                        * Math.PI)
                        * r * (90 - alt) / 10.0);
                meshPaint.setStyle(Style.FILL);
                canvas.drawCircle(x / 2 + ex, y / 2 + ey, 4, meshPaint);
            }
            if (orientation != null) {
                float ex = (float) (FloatMath.sin(orientation[0]) * r
                        * (90 + orientation[1] / Math.PI * 180f) / 10.0f);
                float ey = (float) (-FloatMath.cos(orientation[0]) * r
                        * (90 + orientation[1] / Math.PI * 180f) / 10.0f);
                meshPaint.setStyle(Style.FILL);
                meshPaint.setColor(Color.WHITE);
                canvas.drawCircle(x / 2 + ex, y / 2 + ey, 4, meshPaint);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawRange(Canvas canvas, PlanetPositionHrz[] trajWinter2,
                           PlanetPositionHrz[] trajSummer2, Paint paint) {

        float x = this.getWidth();
        float y = this.getHeight();
        float r = Math.min(x, y) / 22;

        float winterSet;
        float winterRise;
        float summerSet;
        float summerRise;
        boolean first = true;

        if (trajWinter2 != null && trajWinter2.length > 0) {
            winterSet = (float) trajWinter2[trajWinter2.length - 1]
                    .getAzimuth();
            winterRise = (float) trajWinter2[0].getAzimuth();
        } else {
            winterRise = (float) trajSummer2[trajSummer2.length - 1]
                    .getAzimuth(); // summer set
            winterSet = (float) trajSummer2[0].getAzimuth() - 360 - 5; // summer
            // rise
        }

        if (trajSummer2 != null && trajSummer2.length > 0) {
            summerSet = (float) trajSummer2[trajSummer2.length - 1]
                    .getAzimuth();
            summerRise = (float) trajSummer2[0].getAzimuth() + 5;
        } else {
            summerSet = (float) trajWinter2[trajWinter2.length - 1]
                    .getAzimuth() + 360 + 5; // winter set
            summerRise = (float) trajWinter2[0].getAzimuth(); // winter rise
        }

        Path path = new Path();
        if (trajWinter2 != null) {
            for (int j = 0; j < trajWinter2.length; j++) {
                float ex = (float) (Math.sin(trajWinter2[j].getAzimuth()
                        / 180.0 * Math.PI)
                        * r * (90 - trajWinter2[j].getAltitude()) / 10.0);
                float ey = (float) (-Math.cos(trajWinter2[j].getAzimuth()
                        / 180.0 * Math.PI)
                        * r * (90 - trajWinter2[j].getAltitude()) / 10.0);
                if (first) {
                    path.moveTo(x / 2 + ex, y / 2 + ey);
                    first = false;
                } else {
                    path.lineTo(x / 2 + ex, y / 2 + ey);
                }
            }
        } else {
            path.moveTo(x / 2, y / 2 + r * 9);
        }

        for (float az = winterSet; az < summerSet; az += 5) {
            float ex = (float) (Math.sin(az / 180.0 * Math.PI) * r * 9);
            float ey = (float) (-Math.cos(az / 180.0 * Math.PI) * r * 9);
            if (first) {
                path.moveTo(x / 2 + ex, y / 2 + ey);
                first = false;
            } else {
                path.lineTo(x / 2 + ex, y / 2 + ey);
            }
        }

        if (trajSummer2 != null) {
            for (int j = trajSummer2.length; j > 0; j--) {
                float ex = (float) (Math.sin(trajSummer2[j - 1].getAzimuth()
                        / 180.0 * Math.PI)
                        * r * (90 - trajSummer2[j - 1].getAltitude()) / 10.0);
                float ey = (float) (-Math.cos(trajSummer2[j - 1].getAzimuth()
                        / 180.0 * Math.PI)
                        * r * (90 - trajSummer2[j - 1].getAltitude()) / 10.0);
                path.lineTo(x / 2 + ex, y / 2 + ey);
            }
        }
        for (float az = summerRise; az < winterRise; az += 5) {
            float ex = (float) (Math.sin(az / 180.0 * Math.PI) * r * 9);
            float ey = (float) (-Math.cos(az / 180.0 * Math.PI) * r * 9);
            path.lineTo(x / 2 + ex, y / 2 + ey);
        }

        path.close();
        canvas.drawPath(path, paint);

    }

    private void drawTraj(Canvas canvas, PlanetPositionHrz[] traj, Paint paint) {

        if (traj == null || traj.length == 0)
            return;

        float x = this.getWidth();
        float y = this.getHeight();
        float r = Math.min(x, y) / 22;
        Path path = new Path();
        boolean first = true;
        double time = traj[0].getTime();
        for (int j = 0; j < traj.length; j++) {
            if (traj[j].getTime() < time) {
                first = true;
            }
            time = traj[j].getTime();
            float ex = (float) (Math
                    .sin(traj[j].getAzimuth() / 180.0 * Math.PI)
                    * r
                    * (90 - traj[j].getAltitude()) / 10.0);
            float ey = (float) (-Math.cos(traj[j].getAzimuth() / 180.0
                    * Math.PI)
                    * r * (90 - traj[j].getAltitude()) / 10.0);

            if (first) {
                path.moveTo(x / 2 + ex, y / 2 + ey);
                first = false;
            } else {
                path.lineTo(x / 2 + ex, y / 2 + ey);
            }

        }
        canvas.drawPath(path, paint);
    }

    public void setPos(PlanetPositionHrz pos) {
        posNow = pos;
    }

    public void setTraj(PlanetPositionHrz[] traj) {
        this.traj = (traj != null ? traj.clone() : null);
    }

    public void setTrajWinter(PlanetPositionHrz[] traj2) {
        this.trajWinter = traj2;

    }

    public void setTrajSummer(PlanetPositionHrz[] traj2) {
        this.trajSummer = traj2;

    }

    public void setTrajEqui(PlanetPositionHrz[] traj2) {
        this.trajEqui = traj2;

    }

    public void setDisplayParams(DisplayParams dp) {
        displayParms = dp;
    }

    private void drawDegrees(Canvas canvas, float x, float y, float value,
                             Paint paint, Paint.Align align) {
        if (displayParms != null && displayParms.showAzS)
            value = (value + 180) % 360;
        String valueS = String.format("%3.0f°", value);
        float w = paint.getStrokeWidth();
        paint.setStrokeWidth(0);
        paint.setTextAlign(align);
        canvas.drawText(valueS, x, y, paint);
        paint.setStrokeWidth(w);
    }

    public void setOrientation(float[] orientations) {
        this.orientation = orientations;
    }

}
