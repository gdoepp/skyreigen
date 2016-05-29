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
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;

import java.util.List;
import java.util.Vector;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.Constellation;
import de.gdoeppert.sky.model.PlanetPositionEqu;
import de.gdoeppert.sky.model.SolSysPositions;
import de.gdoeppert.sky.model.Star;

public class EquatorialViewGL extends GLSurfaceView {

    private final GLRenderer renderer;

    @Override
    public void onPause() {
        super.onPause();
        renderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        renderer.onResume();
    }

    public void setStarsReady(boolean eclReady) {
        this.starsReady = eclReady;
        requestRender();
    }

    public void setPlanetsReady(boolean ovwReady) {
        this.planetsReady = ovwReady;
        if (ovwReady) {
            float posSun = ((float) positions[SolSysPositions.idx_sun].getRa());
            renderer.resetModel(posSun);
            requestRender();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (isPortrait) {
            renderer.resetView(1f * getHeight() / getWidth());
        } else {
            renderer.resetView(1f * getWidth() / getHeight());
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public void setPlanetColors(int[] planetColors) {
        this.planetColors = planetColors;
    }

    public void setJd(double jd) {
        this.jd = jd;
    }

    public void setStars(Vector<Star> stars) {
        this.stars = stars;
    }

    public void setConstellations(List<Constellation> constellations) {
        this.constellations = constellations;
    }

    public void setPositions(PlanetPositionEqu[] positions2) {
        this.positions = positions2;
    }

    final String[] planet_names;

    boolean isSouthernHemisphere = false;

    int planetColors[];
    double jd;
    Vector<Star> stars = null;
    List<Constellation> constellations = null;
    PlanetPositionEqu[] positions;
    PlanetPositionEqu[] positions2;
    PlanetPositionEqu[] positionsHorz;
    boolean starsReady = false;
    boolean planetsReady = false;
    boolean isPortrait = true;
    final String time_evening;
    final String time_morning;
    final String time_midnight;
    final String time_meridian;
    float meridian;

    Paint paint = new Paint();
    private float mPreviousX;
    private float mPreviousY;

    private final float starSize;
    private final float textSize;

    private final ScaleGestureDetector multiGestures = new ScaleGestureDetector(
            this.getContext(), new OnScaleGestureListener() {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (detector.isInProgress()) {
                float scale = detector.getScaleFactor();
                EquatorialViewGL.this.onScale(
                        scale,
                        detector.getFocusX(),
                        EquatorialViewGL.this.getHeight()
                                - detector.getFocusY());
            }
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector arg0) {

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {

        }

    });

    public EquatorialViewGL(Context context, AttributeSet attrs) {
        super(context, attrs);
        planet_names = context.getResources().getStringArray(
                R.array.bodies_array);
        time_evening = context.getResources().getString(R.string.time_evening);
        time_midnight = context.getResources()
                .getString(R.string.time_midnight);
        time_morning = context.getResources().getString(R.string.time_morning);
        time_meridian = context.getResources()
                .getString(R.string.time_meridian);

        textSize = context.getResources().getDimension(R.dimen.normalTextSize);
        starSize = context.getResources().getDimension(R.dimen.normalStarSize);

        starsReady = false;
        planetsReady = false;

        // setDebugFlags(GLSurfaceView.DEBUG_CHECK_GL_ERROR);

        setEGLContextClientVersion(1);

        setEGLConfigChooser(8, 8, 8, 8, 0, 0); // needed for emulator

        // Set the Renderer for drawing on the GLSurfaceView
        renderer = new GLRenderer(this);
        setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        setLongClickable(true);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        getParent().requestDisallowInterceptTouchEvent(true);

        if (event.getPointerCount() > 1) {
            boolean multi = multiGestures.onTouchEvent(event);
            if (multi)
                return true;
        }

        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPreviousX = x;
                mPreviousY = y;

                break;

            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                if (isSouthernHemisphere) {
                    dx *= -1;
                    dy *= -1;
                }
                float dx1 = (isPortrait ? dy : dx);
                float dy1 = (isPortrait ? -dx : dy);

                boolean ok = renderer.scroll(dx1 / this.getWidth(),
                        dy1 / this.getHeight());

                mPreviousX = x;
                mPreviousY = y;

                if (ok) {
                    requestRender();
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    // allow ViewPager to take care
                    return false;
                }
                break;
        }

        return true;
    }

    protected void onScale(float scale, float x, float y) {
        Log.d("glview", "scale: " + scale + " pos: " + x + ", " + y);

        renderer.zoom(x, y, scale);

        requestRender();
    }

    public void setPortrait(boolean port) {
        isPortrait = port;
        requestRender();
    }

    public void setPositions2(PlanetPositionEqu[] positions2) {
        this.positions2 = positions2;
    }

    public void setSouthern(boolean b) {
        isSouthernHemisphere = b;
    }

    public void setMeridian(double meridian) {
        this.meridian = (float) meridian;
    }

    public void setPositionsHorz(PlanetPositionEqu[] positionsHorz) {
        this.positionsHorz = positionsHorz;
    }

    public float getTextSize() {
        return textSize;
    }

    public float getStarSize() {
        return starSize;
    }

    public void clear() {
    }

}
