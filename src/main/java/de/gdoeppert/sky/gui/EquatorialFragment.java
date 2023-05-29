package de.gdoeppert.sky.gui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.tasks.EquatorialTaskStarReader;
import de.gdoeppert.sky.tasks.EquatorialTask;

public class EquatorialFragment extends SkyFragment {

    private EquatorialViewGL eclV;

    public EquatorialFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.equatorial, container, false));
        update();
        return getRootView();

    }

    @Override
    public int getHelpId() {
        return R.string.help_equatorial;
    }

    public void cleanUp() {
        FrameLayout eclL, eclL0;
        eclL = (FrameLayout) getRootView().findViewById(R.id.solSysVrt);
        eclL0 = (FrameLayout) getRootView().findViewById(R.id.solSysHrz);
        while (eclL.getChildCount() > 0) {
            eclL.removeViewAt(0);
        }
        while (eclL0.getChildCount() > 0) {
            eclL0.removeViewAt(0);
        }
        eclV = null;
    }

    @Override
    public void update() {

        SkyActivity activity = (SkyActivity) getActivity();
        if (getRootView() == null) {
            updatePending = true;
            return;
        }

        boolean portrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        FrameLayout eclL, eclL0;
        if (portrait) {
            eclL = (FrameLayout) getRootView().findViewById(R.id.solSysVrt);
            eclL0 = (FrameLayout) getRootView().findViewById(R.id.solSysHrz);
        } else {
            eclL = (FrameLayout) getRootView().findViewById(R.id.solSysHrz);
            eclL0 = (FrameLayout) getRootView().findViewById(R.id.solSysVrt);
        }
        eclV = null;
        if (eclL.getChildCount() == 0) {

            if (eclL0.getChildCount() > 0) {
                eclV = (EquatorialViewGL) eclL0.getChildAt(0);
                eclL0.removeViewAt(0);
                Log.d("eqfrag", "move eclV");
            }
            if (eclV == null) {
                eclV = new EquatorialViewGL(activity, null);
                eclV.setId(SkyActivity.ECLIPTIC_VIEW_ID);
                Log.d("eqfrag", "new eclV");
            }
            eclL.addView(eclV);
        } else {
            eclV = (EquatorialViewGL) eclL.getChildAt(0);
            Log.d("eqfrag", "found eclV");
        }
        eclV.setFocusable(true);
        eclV.setVisibility(View.VISIBLE);

        eclV.setPortrait(portrait);
        activity.setProgressBarIndeterminateVisibility(true);
        eclV.setStarsReady(false);
        eclV.setPlanetsReady(false);

        stopTask(task);
        task = new EquatorialTask(getRootView(), activity);
        task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR,
                activity);
    }

    public void unselect() {
        super.unselect();
        Log.d("eqfrag", "unselect");
        if (eclV != null) {
            eclV.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (eclV != null) {
            eclV.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (eclV != null) {
            eclV.onPause();
        }
        cleanUp();
    }
}