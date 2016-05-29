package de.gdoeppert.sky.gui;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.SolSysElements;
import de.gdoeppert.sky.tasks.UpdateTask;

public class SkyFragment extends Fragment implements OnSeekBarChangeListener {
    private View rootView;
    protected boolean updatePending = false;
    private SensorEventListener sensorEventListener;

    protected AsyncTask<SkyActivity, ?, ?> task = null;

    protected SolSysElements getElements() {
        return null;
    }

    @Override
    public void onResume() {

        if (updatePending) {
            update();
            updatePending = false;
        }
        PolarView polarView = (PolarView) getRootView().findViewById(
                R.id.polarView);
        if (polarView != null) {
            registerSensorListener(polarView);
        }
        super.onResume();

    }

    public void update() {

        final SkyActivity activity = (SkyActivity) getActivity();

        Date date = activity.getSkyData().getTime();
        TextView dp = (TextView) getRootView().findViewById(R.id.date);

        if (dp != null) {
            dp.setText(activity.getSkyData().formatDate(date));
        }

        setupTime(activity, date);

    }

    protected void setupTime(final SkyActivity activity, Date date) {

        TextView dp = (TextView) getRootView().findViewById(R.id.time);

        if (dp != null) {
            dp.setText(activity.getSkyData().formatTime(date));
        }

        SeekBar sb = (SeekBar) getRootView().findViewById(R.id.seekTime);
        if (sb != null) {
            sb.setMax(24 * 60);
            sb.setKeyProgressIncrement(60);
            sb.setProgress((int) ((activity.getSolarElements().getJD() - activity
                    .getSolarElements().getJD0()) * 24 * 60));
            sb.setOnSeekBarChangeListener(this);
        }

        Button btn = (Button) getRootView().findViewById(R.id.now);
        if (btn != null) {

            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    Calendar calNow = GregorianCalendar.getInstance();
                    activity.getSkyData().setCal(Calendar.HOUR_OF_DAY,
                            calNow.get(Calendar.HOUR_OF_DAY));
                    activity.getSkyData().setCal(Calendar.MINUTE,
                            calNow.get(Calendar.MINUTE));

                    AsyncTask<SkyActivity, String, SkyActivity> task = new UpdateTask(
                            SkyFragment.this, getElements());
                    task.execute(activity);
                }
            });
        }

    }

    public void unselect() {
        if (getRootView() == null)
            return;
        Button btn = (Button) getRootView().findViewById(R.id.now);
        if (btn != null) {
            btn.setOnClickListener(null);
        }
        SeekBar sb = (SeekBar) getRootView().findViewById(R.id.seekTime);
        if (sb != null) {
            sb.setOnSeekBarChangeListener(null);
        }
        registerSensorListener(null);

    }

    @Override
    public void onStartTrackingTouch(SeekBar sb) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar sb) {
        final SkyActivity activity = (SkyActivity) getActivity();
        int t = sb.getProgress();

        int t0 = (int) ((activity.getSolarElements().getJD() - activity
                .getSolarElements().getJD0()) * 24 * 60);

        activity.getSkyData().addCal(Calendar.MINUTE, t - t0);

        AsyncTask<SkyActivity, String, SkyActivity> task = new UpdateTask(this,
                getElements());
        task.execute(activity);
    }

    @Override
    public void onProgressChanged(SeekBar sb, int t, boolean fromUser) {
        final SkyActivity activity = (SkyActivity) getActivity();
        TextView dp = (TextView) getRootView().findViewById(R.id.time);
        if (dp != null) {
            int t0 = (int) ((activity.getSolarElements().getJD() - activity
                    .getSolarElements().getJD0()) * 24 * 60);

            Calendar cal0 = activity.getSkyData().getCalClone();

            cal0.add(Calendar.MINUTE, t - t0);
            dp.setText(activity.getSkyData().formatTime(cal0.getTime()));

        }
    }

    protected void stopTask(AsyncTask<?, ?, ?> task) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
            try {
                task.get();
            } catch (CancellationException e) {
                // ignore
            } catch (InterruptedException e) {
                // ignore
            } catch (ExecutionException e) {
                // ignore
            }
        }
    }

    void registerSensorListener(PolarView pv) {
        SensorManager sensors = (SensorManager) getActivity().getSystemService(
                Context.SENSOR_SERVICE);
        if (sensorEventListener != null) {
            sensors.unregisterListener(sensorEventListener);
            Log.println(Log.DEBUG,
                    "tabchg", "unregister sensors " + sensorEventListener); //$NON-NLS-1$ //$NON-NLS-2$
            sensorEventListener = null;
        }

        if (pv != null && ((SkyActivity) getActivity()).isShowPointer()) {
            sensorEventListener = new PVSensorListener(pv);

            Log.println(Log.DEBUG,
                    "tabchg", "register sensors " + sensorEventListener); //$NON-NLS-1$ //$NON-NLS-2$

            Sensor sensor = sensors
                    .getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            if (sensor != null) {
                sensors.registerListener(sensorEventListener, sensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
            sensor = sensors.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensor != null) {
                sensors.registerListener(sensorEventListener, sensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    @Override
    public void onPause() {
        registerSensorListener(null);
        super.onPause();
    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public int getHelpId() {
        return R.string.info;
    }

}