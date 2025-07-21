package de.gdoeppert.sky.gui;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.LunarElements;
import de.gdoeppert.sky.model.SkyData;
import de.gdoeppert.sky.model.SolarElements;

public class QuickFragment extends SkyFragment implements OnClickListener,
        OnItemSelectedListener, OnDateSetListener, OnTimeSetListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRootView(inflater.inflate(R.layout.quick, container, false));

        Spinner spinner = (Spinner) getRootView().findViewById(R.id.location);

        SkyActivity activity = (SkyActivity) getActivity();

        activity.setLocationAdapter(spinner);

        TextView dp = (TextView) getRootView().findViewById(R.id.date);

        if (dp != null) {
            dp.setText(activity.getSkyData().formatDate(
                    activity.getSkyData().getTime()));
            dp = (TextView) getRootView().findViewById(R.id.time);
            dp.setText(activity.getSkyData().formatTime(
                    activity.getSkyData().getTime()));
        }

        Button dpbutt = (Button) getRootView().findViewById(R.id.dateDecr);
        dpbutt.setOnClickListener(this);
        dpbutt = (Button) getRootView().findViewById(R.id.dateIncr);
        dpbutt.setOnClickListener(this);
        dpbutt = (Button) getRootView().findViewById(R.id.dateNow);
        dpbutt.setOnClickListener(this);

        Button locEdit = (Button) getRootView().findViewById(R.id.locEdit);
        locEdit.setOnClickListener(this);

        int idx = activity.getSkyData().getLocationIndex();
        if (idx >= 0 && spinner.getSelectedItemPosition() != idx) {
            spinner.setSelection(idx);
        } else if (idx < 0) {
            spinner.setSelection(0);
            activity.getSkyData().normalizeLocations();
            activity.getSkyData().setLocation(0);
        }

        spinner.setOnItemSelectedListener(this);

        update();

        return getRootView();
    }

    @Override
    public int getHelpId() {
        return R.string.help_quick;
    }

    @Override
    public void update() {

        if (getRootView() == null) {
            updatePending = true;
            return;
        }
        Log.println(Log.DEBUG, "update", "quick"); //$NON-NLS-1$ //$NON-NLS-2$

        SkyActivity activity = (SkyActivity) getActivity();

        SkyData skyData = activity.getSkyData();

        SolarElements se = skyData.getSolarElements();
        LunarElements le = skyData.getLunarElements();

        TextView tx = (TextView) getRootView().findViewById(R.id.sunrise);
        tx.setText(se.getRise(se.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.sunriseazi);
        tx.setText(se.getRiseAz(se.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.suntrans);
        tx.setText(se.getTransit(se.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.suntransalt);
        tx.setText(se.getTransitAlt(se.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.sunset);
        tx.setText(se.getSet(se.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.sunsetazi);
        tx.setText(se.getSetAz(se.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.sidertime);
        tx.setText(se.getSiderealTime());

        tx = (TextView) getRootView().findViewById(R.id.moonrise);
        tx.setText(le.getRise(le.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.moonriseazi);
        tx.setText(le.getRiseAz(le.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.moontrans);
        tx.setText(le.getTransit(le.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.moontransalt);
        tx.setText(le.getTransitAlt(le.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.moonset);
        tx.setText(le.getSet(le.getRiseSet()));
        tx = (TextView) getRootView().findViewById(R.id.moonsetazi);
        tx.setText(le.getSetAz(le.getRiseSet()));

        tx = (TextView) getRootView().findViewById(R.id.moonph1);
        tx.setText(le.getQuarter());

        View v = getRootView().findViewById(R.id.lunarSurface);
        LunarSurface ls = (LunarSurface) v;
        ls.setPhase(le.getPhaseNum());
        ls.setBLimb(le.getBrightLimbNum(activity.getSolarElements()));
        ls.setParallactic(le.getParallactic());
        ls.invalidate();

        tx = (TextView) getRootView().findViewById(R.id.moonage);
        tx.setText(le.getAge());

        TextView dp = (TextView) getRootView().findViewById(R.id.date);
        Date date = skyData.getTime();
        dp.setText(activity.getSkyData().formatDate(date));
        dp.setClickable(true);
        dp.setOnClickListener(this);
        dp = (TextView) getRootView().findViewById(R.id.time);
        dp.setText(activity.getSkyData().formatTime(date));
        dp.setClickable(true);
        dp.setOnClickListener(this);

        super.update();
    }

    @Override
    protected void setupTime(SkyActivity activity, Date date) {
    }

    @Override
    public void unselect() {

        if (getRootView() == null)
            return;

    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {
        Log.println(Log.DEBUG, "skyactivity", "on date set"); //$NON-NLS-1$ //$NON-NLS-2$

        SkyActivity activity = (SkyActivity) getActivity();
        SkyData skyData = activity.getSkyData();

        skyData.setCal(Calendar.YEAR, year);
        skyData.setCal(Calendar.MONTH, monthOfYear);
        skyData.setCal(Calendar.DAY_OF_MONTH, dayOfMonth);

        TextView dp = (TextView) getRootView().findViewById(R.id.date);
        dp.setText(skyData.formatDate(skyData.getTime()));
        skyData.getSolarElements();
        skyData.getLunarElements();

        update();
    }

    @Override
    public void onTimeSet(TimePicker arg0, int hour, int min) {
        Log.println(Log.DEBUG, "skyactivity", "on time set"); //$NON-NLS-1$ //$NON-NLS-2$

        SkyActivity activity = (SkyActivity) getActivity();
        SkyData skyData = activity.getSkyData();

        skyData.setCal(Calendar.HOUR_OF_DAY, hour);
        skyData.setCal(Calendar.MINUTE, min);
        TextView dp = (TextView) getRootView().findViewById(R.id.time);
        dp.setText(skyData.formatTime(skyData.getTime()));
        skyData.getSolarElements();
        skyData.getLunarElements();
        update();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void showDatePicker() {
        DateDialog date = new DateDialog();

        SkyActivity activity = (SkyActivity) getActivity();
        SkyData skyData = activity.getSkyData();

        Bundle args = new Bundle();
        args.putInt("year", skyData.getCal(Calendar.YEAR));
        args.putInt("month", skyData.getCal(Calendar.MONTH));
        args.putInt("day", skyData.getCal(Calendar.DAY_OF_MONTH));
        date.setArguments(args);

        date.setCallBack(this);
        date.show(getActivity().getFragmentManager(), "Date Picker");
    }

    private void showTimePicker() {
        TimeDialog time = new TimeDialog();

        SkyActivity activity = (SkyActivity) getActivity();
        SkyData skyData = activity.getSkyData();

        Bundle args = new Bundle();
        args.putInt("hour", skyData.getCal(Calendar.HOUR_OF_DAY));
        args.putInt("minute", skyData.getCal(Calendar.MINUTE));
        time.setArguments(args);

        time.setCallBack(this);
        time.show(getActivity().getFragmentManager(), "Date Picker");
    }

    @Override
    public void onClick(View button) {
        SkyActivity activity = (SkyActivity) getActivity();
        SkyData skyData = activity.getSkyData();

        int id = button.getId();
        if (id == R.id.locEdit) {
            LocationDialog locDialog = new LocationDialog();
            locDialog.show(this.getFragmentManager(), "LocationDialog");
        } else if (id == R.id.date) {
            showDatePicker();
        } else if (id == R.id.time) {
            showTimePicker();
        } else if (id == R.id.dateDecr || id == R.id.dateIncr || id == R.id.dateNow) {
            if (button.getId() == R.id.dateDecr) {
                skyData.addCal(Calendar.DAY_OF_MONTH, -1);
            } else if (button.getId() == R.id.dateIncr) {
                skyData.addCal(Calendar.DAY_OF_MONTH, 1);
            } else {
                skyData.resetCal();
            }

            Date date = activity.getSkyData().getTime();
            TextView dp = (TextView) getRootView().findViewById(R.id.date);
            dp.setText(activity.getSkyData().formatDate(date));
            dp = (TextView) getRootView().findViewById(R.id.time);
            dp.setText(activity.getSkyData().formatTime(date));

            update();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        SkyActivity activity = (SkyActivity) getActivity();
        SkyData skyData = activity.getSkyData();
        if (arg0.getId() == R.id.location) {

            if (arg2 >= activity.getSkyData().getLocations().size()) return;

            skyData.setLocation(arg2);

            if (skyData.getLocations().get(arg2).isDummy()) {
                LocationDialog locDialog = new LocationDialog();
                locDialog.show(this.getFragmentManager(), "LocationDialog");
            } else {
                update();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}