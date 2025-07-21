package de.gdoeppert.sky.gui;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.naughter.aaplus.CAADate;

import de.gdoeppert.sky.Messages;
import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.EarthLocation;
import de.gdoeppert.sky.model.PlanetElements;
import de.gdoeppert.sky.model.PlanetPositionHrz;
import de.gdoeppert.sky.model.SkyData.PlanetId;
import de.gdoeppert.sky.model.SkyEvent;
import de.gdoeppert.sky.model.SolSysElements;
import de.gdoeppert.sky.tasks.PlanetTask;
import de.gdoeppert.sky.tasks.PlanetVisibilityTask;

public class PlanetFragment extends SkyFragment implements
        OnItemSelectedListener, OnClickListener {

    private PolarView polarView;
    private PlanetId planetIdx;
    protected AsyncTask<SkyActivity, ?, ?> task2 = null;

    @Override
    protected SolSysElements getElements() {
        if (planetIdx == null)
            return null;
        SkyActivity activity = (SkyActivity) getActivity();
        return activity.getSkyData().getPlanetElements(planetIdx);
    }

    @Override
    public int getHelpId() {
        return R.string.help_planet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRootView(inflater.inflate(R.layout.planet, container, false));

        Spinner spinner = (Spinner) getRootView().findViewById(R.id.planetlist);

        ArrayAdapter<CharSequence> planetsAdapter = ArrayAdapter
                .createFromResource(this.getActivity(), R.array.planets_array,
                        R.layout.spinneritem);

        spinner.setAdapter(planetsAdapter);
        if (savedInstanceState == null) {
            spinner.setSelection(3);
            planetIdx = PlanetId.jupiter;
        } else {
            planetIdx = PlanetId.values()[savedInstanceState.getInt(
                    "planetIdx", 3)]; //$NON-NLS-1$
            spinner.setSelection(planetIdx.ordinal());
        }
        spinner.setOnItemSelectedListener(this);

        update();

        return getRootView();
    }

    @Override
    public void unselect() {
        if (getRootView() == null)
            return;
    }

    @Override
    public void update() {
        Log.println(Log.DEBUG, "update", "planet"); //$NON-NLS-1$ //$NON-NLS-2$

        if (getRootView() == null) {
            updatePending = true;
            return;
        }

        polarView = (PolarView) getRootView().findViewById(R.id.polarView);

        updatePlanet();
        registerSensorListener(polarView);

        super.update();
    }

    private void updatePlanet() {
        final SkyActivity activity = (SkyActivity) getActivity();

        PlanetElements planetElements = activity.getSkyData()
                .getPlanetElements(planetIdx);

        activity.setProgressBarIndeterminateVisibility(true);
        getRootView().setVisibility(View.INVISIBLE);

        polarView.setOrientation(null);
        polarView.setTraj(null);
        polarView.setPos(null);

        View btn = getRootView().findViewById(R.id.pldet);
        if (btn != null) {
            btn.setClickable(true);
            final PlanetElements plElem = planetElements;
            btn.setOnClickListener(new OnClickListener() {

                @SuppressLint("DefaultLocale")
                @Override
                public void onClick(View v) {

                    double t = EarthLocation.currentJD(activity.getSkyData()
                            .getCalClone(), false, plElem.getGmt());

                    PlanetPositionHrz pos = plElem.getActPos(t);
                    CAADate rtsDate = new CAADate(pos.getTime()
                            + plElem.getGmt() / 24.0, true);
                    String time = String.format(
                            "%02d:%02d", rtsDate.Hour(), rtsDate.Minute()); //$NON-NLS-1$
                    String alt = plElem.printDegMS(pos.getAltitude(), false);
                    String az = plElem.printDegMS(pos.getAzimuth(), true);
                    String ring = getActivity().getResources().getString(
                            R.string.planetring);
                    String extra = plElem.getPhys();
                    if (extra == null)
                        extra = ""; //$NON-NLS-1$
                    extra = extra.replace("$ring$", ring); //$NON-NLS-1$
                    activity.showMessage(
                            Messages.getString("SkyActivity.Details"), //$NON-NLS-1$
                            Messages.getString("PlanetTask.pos_at_time") + time + Messages.getString("SunTask.currpos_alt") + alt + Messages.getString("SunTask.currpos_az") + az + extra); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
                }
            });
        }

        stopTask(task);

        stopTask(task2);

        PlanetVisibilityTask pvt = new PlanetVisibilityTask(getRootView(),
                planetIdx);
        task = new PlanetTask(this, planetElements, pvt);
        task2 = pvt;
        task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR,
                activity);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getRootView() == null)
            return;

        Spinner sp = (Spinner) getRootView().findViewById(R.id.planetlist);
        int planetIdx = (sp != null ? sp.getSelectedItemPosition()
                : AdapterView.INVALID_POSITION);
        outState.putInt("planetIdx", planetIdx); //$NON-NLS-1$
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {

        if (arg0.getId() == R.id.planetlist) {
            int selectedItemPosition = arg0.getSelectedItemPosition();
            if (selectedItemPosition != AdapterView.INVALID_POSITION
                    && selectedItemPosition != planetIdx.ordinal()) {
                planetIdx = PlanetId.values()[selectedItemPosition];
                update();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onClick(View arg0) {
        SkyActivity activity = (SkyActivity) getActivity();
        PlanetElements planetElements = activity.getSkyData()
                .getPlanetElements(planetIdx);
        SkyEvent[] planetEvents = planetElements.getNextEvent();

        int id = arg0.getId();
        if (id == R.id.planetevent0) {
            activity.showMessage(
                    Messages.getString("PlanetFragment.plev"), planetEvents[0].info); //$NON-NLS-1$
        } else if (id == R.id.planetevent1) {
            activity.showMessage(
                    Messages.getString("PlanetFragment.plev"), planetEvents[1].info); //$NON-NLS-1$
        } else if (id == R.id.planetevent2) {
            activity.showMessage(
                    Messages.getString("PlanetFragment.plev"), planetEvents[2].info); //$NON-NLS-1$
        } else if (id == R.id.planetevent3) {
            activity.showMessage(
                    Messages.getString("PlanetFragment.plev"), planetEvents[3].info); //$NON-NLS-1$
        } else if (id == R.id.planetevent4) {
            activity.showMessage(
                    Messages.getString("PlanetFragment.plev"), planetEvents[4].info); //$NON-NLS-1$
        } else if (id == R.id.planetevent5) {
            activity.showMessage(
                    Messages.getString("PlanetFragment.plev"), planetEvents[5].info); //$NON-NLS-1$
        } else if (id == R.id.planeteventdate0 || id == R.id.planeteventdate1 || id == R.id.planeteventdate2 || id == R.id.planeteventdate3 || id == R.id.planeteventdate4 || id == R.id.planeteventdate5) {
            activity.switchDate(arg0, this);
        }

    }
}
