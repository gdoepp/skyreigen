package de.gdoeppert.sky.gui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.EarthLocation;
import de.gdoeppert.sky.model.SolSysPositions;
import de.gdoeppert.sky.tasks.EclipticTask;

public class EclipticFragment extends SkyFragment {

    private Timer timer;

    public EclipticFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.ecliptic, container, false));
        update();
        return getRootView();

    }

    @Override
    public void onProgressChanged(SeekBar sb, int t, boolean fromUser) {
        final SkyActivity activity = (SkyActivity) getActivity();
        updateDate(activity, t);
    }

    @Override
    public void onStopTrackingTouch(SeekBar sb) {
        final SkyActivity activity = (SkyActivity) getActivity();
        int t = sb.getProgress();

        int t0 = (int) activity.getSolarElements().getJD();

        updateEclView(activity, t0 + t - 183);

        updateDate(activity, t);

    }

    @Override
    public void unselect() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.unselect();
    }

    @Override
    public int getHelpId() {
        return R.string.help_solsys;
    }

    @Override
    public void update() {

        SkyActivity activity = (SkyActivity) getActivity();
        if (getRootView() == null) {
            updatePending = true;
            return;
        }

        SolsysView eclV = (SolsysView) getRootView().findViewById(
                R.id.solSysEcl);

        activity.setProgressBarIndeterminateVisibility(true);
        eclV.setPlanetsReady(false);

        stopTask(task);

        super.update();

        updateEclView(activity, activity.getSkyData().getSolarElements()
                .getJD());
    }

    @Override
    protected void setupTime(final SkyActivity activity, Date date) {

        SeekBar sb = (SeekBar) getRootView().findViewById(R.id.seekTime);
        if (sb != null) {
            sb.setMax(366);
            sb.setKeyProgressIncrement(30);
            sb.setProgress(183);
            sb.setOnSeekBarChangeListener(this);
        }

        Button btn = (Button) getRootView().findViewById(R.id.now);
        if (btn != null) {

            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }

                    final Calendar calNow = activity.getSkyData().getCalClone();

                    updateEclView(activity,
                            EarthLocation.currentJD(calNow, false, 0));

                    SeekBar sb = (SeekBar) getRootView().findViewById(
                            R.id.seekTime);
                    if (sb != null) {
                        sb.setProgress(183);
                    }
                }
            });
        }
        btn = (Button) getRootView().findViewById(R.id.play);
        if (btn != null) {

            btn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                        return;
                    }

                    timer = new java.util.Timer();
                    final Handler mHandler = new Handler();
                    final Calendar calNow = activity.getSkyData().getCalClone();
                    final double jd0 = EarthLocation
                            .currentJD(calNow, false, 0) - 183;
                    calNow.add(Calendar.DAY_OF_YEAR, (int) (activity
                            .getSkyData().getSolSysPositions().getJD()
                            - jd0 - 183));
                    final SeekBar sb = (SeekBar) getRootView().findViewById(
                            R.id.seekTime);
                    final TextView dp = (TextView) getRootView().findViewById(
                            R.id.date);

                    timer.scheduleAtFixedRate(new TimerTask() {

                        double jd = activity.getSkyData().getSolSysPositions()
                                .getJD();

                        @Override
                        public void run() {

                            mHandler.post(new Runnable() {

                                @Override
                                public void run() {

                                    activity.getSkyData().getSolSysPositions()
                                            .setJd(jd);
                                    SolSysPositions pos = activity.getSkyData()
                                            .getSolSysPositions();
                                    pos.calcPositions();

                                    SolsysView eclV = (SolsysView) getRootView()
                                            .findViewById(R.id.solSysEcl);
                                    if (eclV != null) {
                                        eclV.setEclipticPosition(pos
                                                .getEclipticPositions());
                                        eclV.setPlanetColors(pos
                                                .getPlanetColors());
                                        eclV.setPlanetsReady(true);
                                        eclV.invalidate();
                                    }
                                    if (sb != null) {
                                        sb.setProgress((int) (jd - jd0));
                                    }

                                    if (dp != null) {
                                        dp.setText(activity.getSkyData()
                                                .formatDate(calNow.getTime()));
                                    }
                                }
                            });
                            jd += 2;
                            calNow.add(Calendar.DAY_OF_YEAR, 2);
                            if (jd > jd0 + 365) {
                                this.cancel();
                                EclipticFragment.this.timer = null;
                            }

                        }

                    }, 200, 200);
                }
            });
        }
    }

    private void updateDate(final SkyActivity activity, int t) {
        TextView dp = (TextView) getRootView().findViewById(R.id.date);

        if (dp != null) {

            Calendar cal0 = activity.getSkyData().getCalClone();

            cal0.add(Calendar.DAY_OF_YEAR, t - 183);
            dp.setText(activity.getSkyData().formatDate(cal0.getTime()));

        }
    }

    private void updateEclView(SkyActivity activity, double jd) {

        activity.getSkyData().getSolSysPositions().setJd(jd);

        task = new EclipticTask(getRootView(), activity);
        task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR,
                activity);
    }

}