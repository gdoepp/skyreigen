package de.gdoeppert.sky.gui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.opengl.GLES10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.SystemClock;
import android.util.Log;

import com.naughter.aaplus.CAA2DCoordinate;
import com.naughter.aaplus.CAACoordinateTransformation;
import com.naughter.aaplus.CAANutation;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import de.gdoeppert.sky.model.Constellation;
import de.gdoeppert.sky.model.ConstellationVertex;
import de.gdoeppert.sky.model.SolSysPositions;
import de.gdoeppert.sky.model.Star;

public class GLRenderer implements GLSurfaceView.Renderer {

    private class Pos implements Comparable<Pos> {
        Pos(int idx, float pos, float pos0) {
            this.pos0 = pos0;
            this.pos = pos;
            this.idx = idx;
            this.korr = 0;
        }

        @Override
        public int compareTo(Pos other) {
            float res = pos0 - other.pos0;
            if (res == 0)
                res = pos - other.pos;
            if (res == 0)
                res = idx - other.idx;
            return (int) (res * 10);
        }

        int idx;
        float korr;
        float pos;

        float pos0;
    }

    private final static ShortBuffer indicesBuffer = createShortBuffer(new short[]{
            0, 1, 2, 1, 2, 3});

    private final static FloatBuffer texBufferRot = createFloatBuffer(new float[]{
            1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0.0f});

    private final static FloatBuffer texBufferNorm = createFloatBuffer(new float[]{
            0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f});

    private final static FloatBuffer equatorBuf = createFloatBuffer(new float[]{
            0, 0, 360, 0});

    private static final int maxDecl = 45;

    private static final int minDecl = -45;

    private static final int planetSize[] = new int[SolSysPositions.idx_count];

    static {
        planetSize[SolSysPositions.idx_sun] = 16;
        planetSize[SolSysPositions.idx_moon] = 12;
        planetSize[SolSysPositions.idx_jupiter] = 6;
        planetSize[SolSysPositions.idx_venus] = 6;
        planetSize[SolSysPositions.idx_uranus] = 3;
        planetSize[SolSysPositions.idx_neptune] = 3;
        planetSize[SolSysPositions.idx_mars] = 5;
        planetSize[SolSysPositions.idx_mercury] = 5;
        planetSize[SolSysPositions.idx_saturn] = 5;
    }

    // / Utility function
    private static FloatBuffer createFloatBuffer(float[] array) {
        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(array.length * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = tempBuffer.asFloatBuffer();
        floatBuffer.put(array);
        floatBuffer.position(0);
        return floatBuffer;
    }

    // / Utility function
    private static FloatBuffer createFloatBuffer(Vector<Float> vector) {

        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(vector.size() * 4);
        tempBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = tempBuffer.asFloatBuffer();

        for (int j = 0; j < vector.size(); j++) {
            floatBuffer.put(j, vector.get(j));
        }
        floatBuffer.position(0);
        return floatBuffer;
    }

    // / Utility function
    private static ShortBuffer createShortBuffer(short[] array) {
        ByteBuffer tempBuffer = ByteBuffer.allocateDirect(array.length * 2);
        tempBuffer.order(ByteOrder.nativeOrder());
        ShortBuffer shortBuffer = tempBuffer.asShortBuffer();
        shortBuffer.put(array);
        shortBuffer.position(0);
        return shortBuffer;
    }

    public GLRenderer(EquatorialViewGL equatorialViewGL) {
        glSurfaceView = equatorialViewGL;
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        if (glSurfaceView.planetsReady) {
            // logFrameRate();
            Log.d("glrend", "draw frame");

            GL11 gl11 = null;
            if (gl instanceof GL11) {
                gl11 = (GL11) gl;

            } else {
                return;
            }

            updateView(gl11);

            GLES10.glClear(GLES10.GL_COLOR_BUFFER_BIT
                    | GLES10.GL_DEPTH_BUFFER_BIT);

            gl.glMatrixMode(GL10.GL_MODELVIEW);

			/*
             * the world coordinates are: x: 0 to 360 (right ascension), y: -45
			 * to 45 (declination) we render the sky twice, giving a range of x:
			 * -360 to 360
			 */
            render(gl11);
            gl.glPushMatrix();
            gl.glTranslatef(-360, 0, 0);
            render(gl11);
            gl.glPopMatrix();

        }
    }

    public void onPause() {
    }

    public void onResume() {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        GL11 gl11 = null;
        if (gl instanceof GL11) {
            gl11 = (GL11) gl;

        } else {
            return;
        }
        updateView(gl11);
        Log.d("glrend", "surface changed");
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        texturesMap.clear(); // discard textures belonging to another EGL
        // context

        eclipticBuf = null; // discard buffers, may not be valid anymore
        horizon1Buf = null;
        horizon2Buf = null;
        zoodiacBuf = null;
        textposPlanet = null;
        constellationOutlinesBuf = null;
        starBuffer = null;
        planetBuffer = null;

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glColor4f(1f, 0f, 0f, 1f);
        if (glSurfaceView.isPortrait) { // keep right ascension always on the
            // major dimension
            gl.glRotatef(90, 0, 0, -1);
        }

        Log.d("glrend", "surface created");
    }

    /**
     * initialize view according to aspect ratio
     */
    public synchronized void resetView(float aspectRatio) {
        viewDecl = 90f; // 45° north/south of equator
        viewRa = 90f * aspectRatio;
        dirty = true;
    }

    /**
     * initialize world coordinates (only right ascension of the sun is of
     * interest)
     */
    public synchronized void resetModel(float posSun) {
        minRa = posSun - 360; // we show the celestial equator with the sun at
        // left and right
        maxRa = posSun;
        declCenter = 0; // center at declination 0
        raCenter = (maxRa + minRa) / 2; // center at midnight
        dirty = true;
    }

    // / move view area by distX/Y
    public synchronized boolean scroll(float distX, float distY) {

        distX *= viewRa; // scale from normalized device coord to ra/decl
        distY *= viewDecl;

        if (!glSurfaceView.isPortrait && Math.abs(distX) > Math.abs(distY)) {
            if ((raCenter == minRa + viewRa / 2 && distX < 0)
                    || (raCenter == maxRa - viewRa / 2 && distX > 0)) {
                return false; // limit reached
            }
        } else if (glSurfaceView.isPortrait
                && Math.abs(distY) > Math.abs(distX)) {
            if ((declCenter == minDecl + viewDecl / 2 && distY < 0)
                    || (declCenter == maxDecl - viewDecl / 2 && distY > 0)) {
                return false; // limit reached
            }
        }

        raCenter += distX; // move center
        declCenter += distY;

        if (raCenter < minRa + viewRa / 2) { // ... but not beyond viewable area
            raCenter = minRa + viewRa / 2;
        }
        if (raCenter > maxRa - viewRa / 2) {
            raCenter = maxRa - viewRa / 2;
        }
        if (declCenter < minDecl + viewDecl / 2) {
            declCenter = minDecl + viewDecl / 2;
        }
        if (declCenter > maxDecl - viewDecl / 2) {
            declCenter = maxDecl - viewDecl / 2;
        }

        dirty = true;

        return true;
    }

    // / zoom view area about x/y by scale
    public synchronized void zoom(float x, float y, float scale) {

        if (dirty) // need valid matrices, wait for update
            return;

        scale = 1 / scale;

        // Limit: 2° and -/+ 45°
        if (viewDecl * scale > maxDecl - minDecl) {
            scale = (maxDecl - minDecl) / viewDecl;
        }

        if (viewDecl * scale < 2f) {
            scale = 2f / viewDecl;
        }

        viewDecl *= scale;
        viewRa *= scale;

        float[] world = translateScreenCoordsToWorld(new float[]{x, y});

        float b = world[1];
        float a = world[0];

        raCenter = scale * raCenter + a * (1 - scale);
        declCenter = scale * declCenter + b * (1 - scale);

        Log.d("glrend", "zoom at " + raCenter + ", " + declCenter);

        if (declCenter + viewDecl / 2 > maxDecl) { // adjust field of view
            declCenter = maxDecl - viewDecl / 2;
        }

        if (declCenter - viewDecl / 2 < minDecl) {
            declCenter = minDecl + viewDecl / 2;
        }
        if (raCenter + viewRa / 2 > maxRa) {
            raCenter = maxRa - viewRa / 2;
        }

        if (raCenter - viewRa / 2 < minRa) {
            raCenter = minRa + viewRa / 2;
        }

        dirty = true;

        Log.d("glrend", "zoom at " + raCenter + ", " + declCenter);
    }

    // / create cached disks for the planets
    private void bindTexture4Disk(GL10 gl, int col) {

        if (!texturesMap.containsKey("Disk-c-" + col)) {

            // Draw the text
            Paint diskPaint = new Paint();
            diskPaint.setColor(col);

            // Create an empty, mutable bitmap
            Bitmap bitmap = Bitmap
                    .createBitmap(64, 64, Bitmap.Config.ARGB_4444);
            // get a canvas to paint over the bitmap
            Canvas canvas = new Canvas(bitmap);
            bitmap.eraseColor(0);
            canvas.drawCircle(32, 32, 30, diskPaint);
            gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                    GL10.GL_MODULATE);
            int[] texture = new int[1];
            gl.glGenTextures(1, texture, 0);
            texturesMap.put("Disk-c-" + col, texture[0]);

            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);

            // Create Nearest Filtered Texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                    GL10.GL_LINEAR);

            // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_CLAMP_TO_EDGE);

            // Use the Android GLUtils to specify a two-dimensional texture
            // image
            // from our bitmap
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // Clean up
            bitmap.recycle();
        } else {
            int texture = texturesMap.get("Disk-c-" + col);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
            // Create Nearest Filtered Texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                    GL10.GL_LINEAR);

            // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_CLAMP_TO_EDGE);
        }
    }

    // create cached textures for texts
    private void bindTexture4Text(GL10 gl, String text, int col, int alignment) {

        // better use one big texture for allstrings?
        // Length is limited

        if (!texturesMap.containsKey(text)) {

            // Draw the text
            Paint textPaint = new Paint();
            textPaint.setTextSize(28);
            textPaint.setAntiAlias(true);
            textPaint.setColor(col);
            Rect bounds = new Rect();
            // draw the text
            textPaint.getTextBounds(text + ".", 0, text.length() + 1, bounds);

            // Create an empty, mutable bitmap
            Bitmap bitmap;

            if (alignment != 0 || glSurfaceView.isPortrait)
                bitmap = Bitmap.createBitmap(32, 256, Bitmap.Config.ARGB_4444);
            else
                bitmap = Bitmap.createBitmap(256, 32, Bitmap.Config.ARGB_4444);

            // get a canvas to paint over the bitmap
            Canvas canvas = new Canvas(bitmap);

            bitmap.eraseColor(0);

            if (alignment != 0 || glSurfaceView.isPortrait) {
                canvas.rotate(-90);
            }

            float xoffset = 0;
            if (alignment > 0)
                xoffset = -bounds.width();
            else if (alignment == 0) {
                xoffset = (glSurfaceView.isPortrait ? -256 : 0)
                        + (256 - bounds.width()) / 2.0f;
            } else
                xoffset = -256;

            float yoffset = (32 - bounds.height()) / 2.0f;

            canvas.drawText(text, xoffset, yoffset + bounds.height() - 4f,
                    textPaint);
            gl.glTexEnvx(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                    GL10.GL_MODULATE);
            /*
             * gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_COLOR,
			 * GL10.GL_BLEND);
			 */
            int[] texture = new int[1];
            gl.glGenTextures(1, texture, 0);
            texturesMap.put(text, texture[0]);

            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture[0]);

            // Create Nearest Filtered Texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                    GL10.GL_LINEAR);

            // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_CLAMP_TO_EDGE);

            // Use the Android GLUtils to specify a two-dimensional texture
            // image
            // from our bitmap
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            // Clean up
            bitmap.recycle();
        } else {
            int texture = texturesMap.get(text);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, texture);
            // Create Nearest Filtered Texture
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                    GL10.GL_NEAREST);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                    GL10.GL_LINEAR);

            // Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_CLAMP_TO_EDGE);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_CLAMP_TO_EDGE);
        }
    }

    private float blue(int i) {
        return (i & 0xff) / 255.0f;
    }

    private FloatBuffer createRectangleForText(float x, float y, int alignment) {

        float scale = glSurfaceView.getTextSize() / 2f;

        if (glSurfaceView.isPortrait) {
            scale *= viewDecl / glSurfaceView.getWidth();
        } else {
            scale *= viewRa / glSurfaceView.getWidth();
        }

        if (glSurfaceView.isSouthernHemisphere) {
            alignment *= -1;
        }

        float[] rectangle = null;

        if (alignment < 0)
            rectangle = new float[]{x - scale, y, x + scale, y, x - scale,
                    y + 16 * scale, x + scale, y + 16 * scale};
        else if (alignment == 0) {
            if (glSurfaceView.isPortrait) {
                rectangle = new float[]{x - scale, y - 8 * scale, x + scale,
                        y - 8 * scale, x - scale, y + 8 * scale, x + scale,
                        y + 8 * scale};
            } else {
                rectangle = new float[]{x - 8 * scale, y - scale,
                        x + 8 * scale, y - scale, x - 8 * scale, y + scale,
                        x + 8 * scale, y + scale};
            }
        } else if (alignment > 0)
            rectangle = new float[]{x - scale, y - 16 * scale, x + scale,
                    y - 16 * scale, x - scale, y, x + scale, y};

        FloatBuffer rectangleBuffer = createFloatBuffer(rectangle);
        return rectangleBuffer;
    }

    private void drawDisk(GL10 gl, float x, float y, float radius, int col) {

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glColor4f(1f, 1f, 1f, 1f);

        bindTexture4Disk(gl, col);

        float scale = viewRa * glSurfaceView.getStarSize();

        if (glSurfaceView.isPortrait) {
            scale /= glSurfaceView.getHeight();
        } else {
            scale /= glSurfaceView.getWidth();
        }

        radius *= scale;

        float[] rectangle = new float[]{x - radius, y - radius, x + radius,
                y - radius, x - radius, y + radius, x + radius, y + radius};

        FloatBuffer rectangleBuffer = createFloatBuffer(rectangle);

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, rectangleBuffer);

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBufferNorm);

        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT,
                indicesBuffer);

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_BLEND);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    private void drawText(GL10 gl, String text, float x, float y, int col,
                          int alignment) {

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

        gl.glColor4f(1f, 1f, 1f, 1f);

        bindTexture4Text(gl, text, col, alignment);

        FloatBuffer rectangleBuffer = createRectangleForText(x, y, alignment);

        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, rectangleBuffer);

        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0,
                glSurfaceView.isSouthernHemisphere ? texBufferNorm
                        : texBufferRot);

        gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT,
                indicesBuffer);

        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glDisable(GL10.GL_BLEND);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    private float green(int i) {
        return (i & 0xff00) / 65535.0f;
    }

    private long startMs = 0;

    private int frameCnt = 0;

    @SuppressWarnings("unused")
    private void logFrameRate() {
        long elapsedMs = SystemClock.elapsedRealtime();
        double elSec = (elapsedMs - startMs) / 1000;
        if (elSec >= 1.0) {
            Log.v("glrend", frameCnt / elSec + " fps");
            frameCnt = 0;
            startMs = SystemClock.elapsedRealtime();
        }
        frameCnt++;
    }

    // / create float buffer for the constellation outlines
    private void prepareConstellations() {
        if (constellationOutlinesBuf == null) {
            constellationOutlinesBuf = new Vector<FloatBuffer>();

            for (Constellation constellation : glSurfaceView.constellations) { // constellations
                // boundaries
                Vector<Float> outline = null;
                ConstellationVertex former = null;
                ConstellationVertex first = null;
                for (ConstellationVertex vert : constellation.getVertices()) {
                    if (former == null) {
                        outline = new Vector<Float>();
                        outline.add(vert.getRa());
                        outline.add(vert.getDecl());
                        first = vert;
                    } else {
                        double ra1 = vert.getRa();
                        if (vert.getRa() < 90 && former.getRa() > 270) {
                            ra1 += 360;
                        }
                        if (vert.getRa() > 270 && former.getRa() < 90) {
                            ra1 -= 360;
                        }
                        outline.add((float) ra1);
                        outline.add(vert.getDecl());

                        if (ra1 != vert.getRa()) {
                            constellationOutlinesBuf
                                    .add(createFloatBuffer(outline));
                            outline = new Vector<Float>();
                            outline.add(vert.getRa());
                            outline.add(vert.getDecl());
                        }
                    }
                    former = vert;
                }
                outline.add(first.getRa());
                outline.add(first.getDecl());
                constellationOutlinesBuf.add(createFloatBuffer(outline));

            }
        }
    }

    // / create float buffer for the ecliptic line
    private void prepareEcliptic() {
        if (eclipticBuf == null) {

            double eps = CAANutation.TrueObliquityOfEcliptic(glSurfaceView.jd);

            float[] ecliptic = new float[37 * 2];

            for (int j = 0; j <= 36; j++) {
                CAA2DCoordinate position = CAACoordinateTransformation
                        .Ecliptic2Equatorial(j * 10, 0, eps);

                ecliptic[j * 2] = (float) position.getX() * 15;
                ecliptic[j * 2 + 1] = (float) position.getY();

            }
            eclipticBuf = createFloatBuffer(ecliptic);
        }
    }

    // / create float buffer for the horizon
    private void prepareHorizon() {
        if (glSurfaceView.positionsHorz != null && horizon1Buf == null) {

            Vector<Float> horizon1 = null;
            Vector<Float> horizon2 = null;
            Vector<Float> horizon = null;

            if (horizon1 == null) {
                horizon1 = new Vector<Float>();
                horizon = horizon1;
            }
            for (int j = 0; j <= 36; j++) {

                double ra1 = glSurfaceView.positionsHorz[j].getRa();

                double ra2 = ra1;

                if (j > 0) {

                    double ra0 = glSurfaceView.positionsHorz[j - 1].getRa();

                    if (ra0 < 90 && ra1 > 270) {
                        ra2 -= 360;
                    }
                    if (ra0 > 270 && ra1 < 90) {
                        ra2 += 360;
                    }

                    if (ra1 != ra2) {
                        horizon.add((float) ra2);
                        horizon.add((float) glSurfaceView.positionsHorz[j]
                                .getDecl());
                        horizon2 = new Vector<Float>();
                        horizon = horizon2;
                    }
                }
                horizon.add((float) ra1);
                horizon.add((float) glSurfaceView.positionsHorz[j].getDecl());
            }
            if (horizon1 != null) {
                horizon1Buf = createFloatBuffer(horizon1);
            }
            if (horizon2 != null) {
                horizon2Buf = createFloatBuffer(horizon2);
            }
        }
    }

    // / create float buffer and set for the planets
    private void preparePlanets() {

        if (planetBuffer == null || textposPlanet == null) {

            textposPlanet = new TreeSet<Pos>();

            for (int j = 0; j < SolSysPositions.idx_count; j++) {
                if (j == SolSysPositions.idx_earth
                        || j == SolSysPositions.idx_sun)
                    continue;
                Pos txtpos = new Pos(j,
                        (float) glSurfaceView.positions[j].getRa(),
                        (float) glSurfaceView.positions[j].getRa());
                textposPlanet.add(txtpos);
            }
            float posSun = ((float) glSurfaceView.positions[SolSysPositions.idx_sun]
                    .getRa());

            textposPlanet.add(new Pos(SolSysPositions.idx_sun, posSun + 2,
                    posSun));

            textposPlanet.add(new Pos(SolSysPositions.idx_sun, posSun - 2,
                    posSun));

            Pos p_last = null;
            for (Pos p : textposPlanet) { // order labels, move them away from
                // sun if
                // necessary
                if (p_last != null && p.idx != SolSysPositions.idx_sun
                        && p.pos > posSun) {
                    if (Math.abs(p_last.pos + p_last.korr - p.pos - p.korr) < 2.5) {
                        p.korr = p_last.pos + p_last.korr - p.pos + 3;
                    }
                }
                p_last = p;
            }
            p_last = null;
            for (Pos p : textposPlanet.descendingSet()) {
                if (p_last != null && p.idx != SolSysPositions.idx_sun
                        && p.pos < posSun) {
                    if (Math.abs(p_last.pos + p_last.korr - p.pos - p.korr) < 2.5) {
                        p.korr = p_last.pos + p_last.korr - p.pos - 3;
                    }
                }
                p_last = p;
            }

            Vector<Float> planets = new Vector<Float>();

            for (Pos p : textposPlanet) {

                planets.add((float) glSurfaceView.positions[p.idx].getRa());
                planets.add((float) glSurfaceView.positions[p.idx].getDecl());
                planets.add(red(glSurfaceView.planetColors[p.idx]));
                planets.add(green(glSurfaceView.planetColors[p.idx]));
                planets.add(blue(glSurfaceView.planetColors[p.idx]));
                planets.add(0.5f);

                planets.add((float) glSurfaceView.positions2[p.idx].getRa());
                planets.add((float) glSurfaceView.positions2[p.idx].getDecl());
                planets.add(red(glSurfaceView.planetColors[p.idx]));
                planets.add(green(glSurfaceView.planetColors[p.idx]));
                planets.add(blue(glSurfaceView.planetColors[p.idx]));
                planets.add(0.5f);
            }
            planetBuffer = createFloatBuffer(planets);

        }
    }

    // create float buffer for the stars (as points)
    private void prepareStars() {
        if (starBuffer == null) {
            int nStars = glSurfaceView.stars.size();
            float[] stars = new float[nStars * 3];

            for (int j = 0; j < nStars; j++) { // stars
                Star s = glSurfaceView.stars.get(j);
                stars[j * 3] = s.getRa();
                stars[j * 3 + 1] = s.getDecl();

                float sw = (s.getMag() <= 1 ? 4 : (s.getMag() <= 2 ? 3f : (s
                        .getMag() <= 3 ? 2 : 1)));

                stars[j * 3 + 2] = sw; // point size
            }
            starBuffer = createFloatBuffer(stars);
        }
    }

    // / create float buffer for the lines every 30°
    private void prepareZoodiac() {
        if (zoodiacBuf == null) {
            float[] zoodiac = new float[13 * 4];

            for (int j = 0; j <= 12; j++) {
                zoodiac[j * 4] = j * 30;
                zoodiac[j * 4 + 1] = -40;

                zoodiac[j * 4 + 2] = j * 30;
                zoodiac[j * 4 + 3] = 40;
            }

            zoodiacBuf = createFloatBuffer(zoodiac);
        }
    }

    private float red(int i) {
        return (i & 0xff0000) / ((float) 0xff0000);
    }

    private void render(GL11 gl11) {

        gl11.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        prepareZoodiac();

        gl11.glColor4f(0f, 0.1f, 0.9f, 0.5f); // blue lines every 30°
        gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, zoodiacBuf);

        gl11.glDrawArrays(GL10.GL_LINES, 0, zoodiacBuf.limit() / 2);

        prepareEcliptic();

        gl11.glColor4f(1f, 0f, 0f, 0.5f); // red line for the ecliptic
        gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, eclipticBuf);

        gl11.glDrawArrays(GL10.GL_LINE_STRIP, 0, eclipticBuf.limit() / 2);

        prepareHorizon();

        gl11.glLineWidth(6); // thick brown line marking the horizon

        if (horizon1Buf != null) {
            gl11.glColor4f(0.3f, 0.2f, 0f, 0.1f);
            gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, horizon1Buf);

            gl11.glDrawArrays(GL10.GL_LINE_STRIP, 0, horizon1Buf.limit() / 2);
        }
        if (horizon2Buf != null) { // may be split in two parts
            gl11.glColor4f(0.3f, 0.2f, 0f, 0.1f);
            gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, horizon2Buf);

            gl11.glDrawArrays(GL10.GL_LINE_STRIP, 0, horizon2Buf.limit() / 2);
        }

        gl11.glLineWidth(1);

        gl11.glColor4f(1f, 1f, 1f, 0.5f);
        gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, equatorBuf); // equator:
        // straight line

        gl11.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);

        // despite rotation for hemisphere, labels should stay at top
        float top = (glSurfaceView.isSouthernHemisphere ? minDecl : maxDecl);
        float bottom = (glSurfaceView.isSouthernHemisphere ? maxDecl : minDecl);

        float[] meridian = new float[]{glSurfaceView.meridian, top * 0.7f,
                glSurfaceView.meridian, bottom};

        FloatBuffer floatBuf = createFloatBuffer(meridian);

        gl11.glColor4f(0.8f, 0.8f, 0.3f, 0.7f);
        gl11.glVertexPointer(2, GL10.GL_FLOAT, 0, floatBuf);

        gl11.glDrawArrays(GL10.GL_LINE_STRIP, 0, 2);

        gl11.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        if (glSurfaceView.starsReady) { // the stars are loaded in the
            renderStars(gl11); // background and may no be ready
        }
        renderConstellations(gl11);
        renderPlanets(gl11);

        gl11.glColor4f(1f, 1f, 1f, 1f);

        float ra0 = (float) glSurfaceView.positions[SolSysPositions.idx_sun]
                .getRa();

        drawText(gl11, glSurfaceView.time_midnight, (ra0 + 180f) % 360f, top,
                Color.LTGRAY, 1);

        drawText(gl11, glSurfaceView.time_morning, (315f + ra0) % 360f, top,
                Color.LTGRAY, 1);

        drawText(gl11, glSurfaceView.time_evening, (ra0 + 45f) % 360f, top,
                Color.LTGRAY, 1);

        float diff1 = (720f + glSurfaceView.meridian - ra0 - 45f) % 360f;
        float diff2 = (720f + glSurfaceView.meridian - ra0 + 45f) % 360f;
        float diff3 = (720f + glSurfaceView.meridian - ra0 - 180f) % 360f;

        if (diff1 > 2 && diff1 < 358 && diff2 > 2 && diff2 < 358 && diff3 > 2
                && diff3 < 358) { // unless something is overwritten

            drawText(gl11, glSurfaceView.time_meridian, glSurfaceView.meridian,
                    top, Color.YELLOW, 1);
        }

    }

    // / show constellation outlines and their short names
    private void renderConstellations(GL10 gl) {

        prepareConstellations();

        for (Constellation constellation : glSurfaceView.constellations) {
            drawText(gl, constellation.getName(), constellation.getRa(),
                    constellation.getDecl(), Color.argb(0x8f, 0, 0xbf, 0), 0);
        }

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glColor4f(0f, 0.4f, 0f, 0.8f);
        for (FloatBuffer fb : constellationOutlinesBuf) {
            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, fb);
            gl.glDrawArrays(GL10.GL_LINE_STRIP, 0, fb.limit() / 2);
        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

    }

    // / show planet disks, names and lines to future position
    private void renderPlanets(GL11 gl) {

        preparePlanets();

        // despite rotation for hemisphere, labels should stay at bottom
        float bottom = (glSurfaceView.isSouthernHemisphere ? maxDecl : minDecl);

        for (Pos p : textposPlanet) {

            drawDisk(gl, (float) glSurfaceView.positions[p.idx].getRa(),
                    (float) glSurfaceView.positions[p.idx].getDecl(),
                    planetSize[p.idx], glSurfaceView.planetColors[p.idx]);

            drawText(gl, glSurfaceView.planet_names[p.idx], p.pos + p.korr,
                    bottom, glSurfaceView.planetColors[p.idx], -1);
        }
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        planetBuffer.position(0);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 6 * 4, planetBuffer);
        planetBuffer.position(2);
        gl.glColorPointer(4, GL10.GL_FLOAT, 6 * 4, planetBuffer);
        gl.glDrawArrays(GL10.GL_LINES, 0, planetBuffer.limit() / 6);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

    }

    // / show the stars as points
    private void renderStars(GL11 gl) {

        prepareStars();

        gl.glColor4f(1f, 1f, 1f, 1f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL11.GL_POINT_SIZE_ARRAY_OES);
        starBuffer.position(0);
        gl.glVertexPointer(2, GL10.GL_FLOAT, 3 * 4, starBuffer);
        starBuffer.position(2);
        gl.glPointSizePointerOES(GL11.GL_FLOAT, 3 * 4, starBuffer);
        gl.glDrawArrays(GL10.GL_POINTS, 0, 900); // to about magnitude 5

        gl.glColor4f(0.6f, 0.6f, 0.6f, 1f); // dimmer stars
        gl.glDrawArrays(GL10.GL_POINTS, 900, (starBuffer.limit() / 3 - 900));

        gl.glDisableClientState(GL11.GL_POINT_SIZE_ARRAY_OES);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

    }

    private float[] translateScreenCoordsToWorld(float[] screenCoords) {

        float[] output = new float[4];

        GLU.gluProject(0f, 0f, 0f, modelViewMatrix, 0, projectMatrix, 0,
                viewVectorParams, 0, output, 0);

        GLU.gluUnProject(screenCoords[0], screenCoords[1], output[2],
                modelViewMatrix, 0, projectMatrix, 0, viewVectorParams, 0,
                output, 0);

        if (output[0] < minRa) // normalize
            output[0] += 360;
        if (output[0] > maxRa)
            output[0] -= 360;

        return output;

    }

    private synchronized void updateView(GL11 gl) {
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glClearColor(0.1f, 0.1f, 0.1f, 0.1f);

        float degS = declCenter - viewDecl / 2;
        float degN = declCenter + viewDecl / 2;
        float ra1 = raCenter - viewRa / 2;
        float ra2 = raCenter + viewRa / 2;

        Log.d("glrend", "updateView: " + degS + ", " + degN + ", " + ra1 + ", "
                + ra2);

        if (glSurfaceView.isSouthernHemisphere) { // rotate view for nothern
            // hemisphere

            if (glSurfaceView.isPortrait) {
                GLU.gluOrtho2D(gl, degN, degS, -ra2, -ra1);
            } else {
                GLU.gluOrtho2D(gl, ra1, ra2, degN, degS);
            }
        } else {

            if (glSurfaceView.isPortrait) {
                GLU.gluOrtho2D(gl, degS, degN, -ra1, -ra2);
            } else {
                GLU.gluOrtho2D(gl, ra2, ra1, degS, degN);
            }
        }
        gl.glGetIntegerv(GL11.GL_VIEWPORT, viewVectorParams, 0);
        gl.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix, 0);
        gl.glGetFloatv(GL11.GL_PROJECTION_MATRIX, projectMatrix, 0);
        dirty = false;

    }

    private Vector<FloatBuffer> constellationOutlinesBuf = null;

    private float declCenter = 0;
    private volatile boolean dirty = true;
    private FloatBuffer eclipticBuf = null;
    private final EquatorialViewGL glSurfaceView;
    private FloatBuffer horizon1Buf = null;
    private FloatBuffer horizon2Buf = null;
    private float maxRa = 0;
    private float minRa = -360;

    private final float[] modelViewMatrix = new float[16];
    private FloatBuffer planetBuffer = null;
    private final float[] projectMatrix = new float[16];

    private float raCenter = 0;

    private FloatBuffer starBuffer = null;

    private TreeSet<Pos> textposPlanet = null;

    private final Map<String, Integer> texturesMap = new HashMap<String, Integer>();

    private float viewDecl = 90f;

    private float viewRa = 200f;

    private final int[] viewVectorParams = new int[4];

    private FloatBuffer zoodiacBuf = null;

}
