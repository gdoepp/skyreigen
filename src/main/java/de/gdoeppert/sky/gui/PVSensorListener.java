package de.gdoeppert.sky.gui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class PVSensorListener implements SensorEventListener {
    private final float[] orientations;
    private final float[] gravities = new float[]{0, 0, 0};
    private final float[] geomagnetics = new float[]{0, 0, 0};

    private final PolarView polarView;

    PVSensorListener(PolarView pv) {
        polarView = pv;
        orientations = new float[3];
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
        // nothing

    }

    @Override
    public void onSensorChanged(SensorEvent arg0) {

        final float alpha = 0.67f;

        if (arg0.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            for (int j = 0; j < 3; j++) {
                geomagnetics[j] = (1 - alpha) * arg0.values[j] + alpha
                        * geomagnetics[j];
            }
        }
        if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            for (int j = 0; j < 3; j++) {
                gravities[j] = (1 - alpha) * arg0.values[j] + alpha
                        * gravities[j];
            }
        }
        if (geomagnetics != null && gravities != null) {
            float[] RM = new float[9];
            float[] I = new float[9];
            SensorManager.getRotationMatrix(RM, I, gravities, geomagnetics);
            SensorManager.getOrientation(RM, orientations);

            if (polarView != null) {
                /*
                 * Log.println(Log.DEBUG, "PVListener", "setOrientation " +
				 * polarView); //$NON-NLS-1$ //$NON-NLS-2$
				 */
                polarView.setOrientation(orientations);
                polarView.invalidate();
            }
        }
    }

}
