package de.gdoeppert.sky.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import de.gdoeppert.sky.Messages;
import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.LunarElements;
import de.gdoeppert.sky.model.SkyEvent;
import de.gdoeppert.sky.model.SolSysElements;
import de.gdoeppert.sky.tasks.MoonTask;

public class MoonFragment extends SkyFragment implements OnClickListener {

    @Override
    protected SolSysElements getElements() {
        SkyActivity activity = (SkyActivity) getActivity();
        return activity.getSkyData().getLunarElements();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRootView(inflater.inflate(R.layout.moon, container, false));
        update();
        return getRootView();
    }

    @Override
    public int getHelpId() {
        return R.string.help_moon;
    }

    @Override
    public void update() {
        Log.println(Log.DEBUG, "update", "moon"); //$NON-NLS-1$ //$NON-NLS-2$

        SkyActivity activity = (SkyActivity) getActivity();

        if (getRootView() == null) {
            updatePending = true;
            return;
        }

        PolarView polarView = (PolarView) getRootView().findViewById(
                R.id.polarView);

        polarView = (PolarView) getRootView().findViewById(R.id.polarView);
        polarView.setOrientation(null);
        registerSensorListener(polarView);
        activity.setProgressBarIndeterminateVisibility(true);

        stopTask(task);

        task = new MoonTask(this);
        task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR,
                activity);

        super.update();
    }

    @Override
    public void onClick(View button) {
        LunarElements elements = (LunarElements) getElements();
        SkyEvent[] events = elements.getNextEvent();
        SkyActivity activity = (SkyActivity) getActivity();

        int id = button.getId();
        if (id == R.id.moonevent0) {
            activity.showMessage(
                    Messages.getString("MoonFragment.event"), events[0].info); //$NON-NLS-1$
        } else if (id == R.id.moonevent1) {
            activity.showMessage(
                    Messages.getString("MoonFragment.event"), events[1].info); //$NON-NLS-1$
        } else if (id == R.id.moonevent2) {
            activity.showMessage(
                    Messages.getString("MoonFragment.event"), events[2].info); //$NON-NLS-1$
        } else if (id == R.id.moonevent3) {
            activity.showMessage(
                    Messages.getString("MoonFragment.event"), events[3].info); //$NON-NLS-1$
        } else if (id == R.id.moonevent4) {
            activity.showMessage(
                    Messages.getString("MoonFragment.event"), events[4].info); //$NON-NLS-1$
        } else if (id == R.id.moonevent5) {
            activity.showMessage(
                    Messages.getString("MoonFragment.event"), events[5].info); //$NON-NLS-1$
        } else if (id == R.id.mooneventdate0 || id == R.id.mooneventdate1 || id == R.id.mooneventdate2 || id == R.id.mooneventdate3 || id == R.id.mooneventdate4 || id == R.id.mooneventdate5) {
            activity.switchDate(button, this);
        }
    }

}