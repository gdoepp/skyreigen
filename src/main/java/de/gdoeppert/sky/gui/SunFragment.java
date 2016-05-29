package de.gdoeppert.sky.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.SolSysElements;
import de.gdoeppert.sky.tasks.SunTask;

public class SunFragment extends SkyFragment implements OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.sun, container, false));
        update();
        return getRootView();
    }

    @Override
    protected SolSysElements getElements() {
        SkyActivity activity = (SkyActivity) getActivity();
        return activity.getSkyData().getSolarElements();
    }

    @Override
    public void update() {

        if (getRootView() == null) {
            updatePending = true;
            return;
        }

        Log.println(Log.DEBUG, "update", "sun"); //$NON-NLS-1$ //$NON-NLS-2$

        SkyActivity activity = (SkyActivity) getActivity();

        PolarView polarView = (PolarView) getRootView().findViewById(
                R.id.polarView);
        polarView.setOrientation(null);
        registerSensorListener(polarView);
        activity.setProgressBarIndeterminateVisibility(true);

        stopTask(task);

        task = new SunTask(this);
        task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR,
                activity);

        super.update();
    }

    @Override
    public int getHelpId() {
        return R.string.help_sun;
    }

    @Override
    public void onClick(View button) {

        SkyActivity activity = (SkyActivity) getActivity();

        switch (button.getId()) {

            case R.id.suneventdate0:
            case R.id.suneventdate1:
            case R.id.suneventdate2:
            case R.id.suneventdate3:
                activity.switchDate(button, this);
                break;

        }
    }
}