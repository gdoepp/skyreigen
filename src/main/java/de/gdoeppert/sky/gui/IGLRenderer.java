package de.gdoeppert.sky.gui;

import android.opengl.GLSurfaceView;

public interface IGLRenderer extends GLSurfaceView.Renderer {
    void toggleShowConstNames();

    void resetView(float aspectRatio);

    void resetModel(float posSun);

    // / move view area by distX/Y
    boolean scroll(float distX, float distY, boolean isPortrait);

    // / zoom view area about x/y by scale
    void zoom(float x, float y, float scale);
}
