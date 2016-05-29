package de.gdoeppert.sky.gui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.naughter.aaplus.CAADate;

import java.util.Calendar;

import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.RiseSetCalculator.RiseSetAll;
import de.gdoeppert.sky.tasks.MonthTask;

public class MonthFragment extends SkyFragment implements OnClickListener,
        OnCheckedChangeListener {

    private int oldMonth = -1;
    private int oldYear = -1;
    private int month;
    private int year;
    private String[] planetNames;

    public MonthFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRootView(inflater.inflate(R.layout.month, container, false));

        Button dpbutt = (Button) getRootView().findViewById(R.id.monthDecr);
        dpbutt.setOnClickListener(this);
        dpbutt = (Button) getRootView().findViewById(R.id.monthIncr);
        dpbutt.setOnClickListener(this);
        dpbutt = (Button) getRootView().findViewById(R.id.monthNow);
        dpbutt.setOnClickListener(this);

        ToggleButton ch = (ToggleButton) getRootView().findViewById(R.id.chR);
        ch.setOnCheckedChangeListener(this);
        ch = (ToggleButton) getRootView().findViewById(R.id.chS);
        ch.setOnCheckedChangeListener(this);
        ch = (ToggleButton) getRootView().findViewById(R.id.chT);
        ch.setOnCheckedChangeListener(this);

        SkyActivity activity = (SkyActivity) getActivity();

        planetNames = activity.getResources().getStringArray(
                R.array.planets_array);

        Calendar cal = activity.getSkyData().getCalClone();
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
        oldMonth = -1;
        update();
        return getRootView();
    }

    @Override
    public int getHelpId() {
        return R.string.help_month;
    }

    @Override
    public void update() {

        SkyActivity activity = (SkyActivity) getActivity();

        TextView monthText = (TextView) getRootView().findViewById(
                R.id.monthText);

        monthText.setText(month + "." + year);

        MonthView view = (MonthView) getRootView().findViewById(R.id.monthView);

        RiseSetAll[] rst = view.getRst();
        if (rst != null) {
            double jd = rst[0].rst[0].getTransitNum();
            CAADate d = new CAADate(jd, true);
            oldMonth = d.Month();
            oldYear = d.Year();
        }

        boolean monthChanged = (oldMonth != month || oldYear != year);

        if (monthChanged) {

            stopTask(task);

            task = new MonthTask(getRootView(), activity, month, year,
                    planetNames);
            task.executeOnExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR,
                    activity);

            view.setClickable(true);
            view.setOnClickListener(this);
            view.setRst(null);
            view.setToDraw(7);
            ToggleButton ch = (ToggleButton) getRootView().findViewById(
                    R.id.chR);
            ch.setChecked(true);
            ch = (ToggleButton) getRootView().findViewById(R.id.chS);
            ch.setChecked(true);
            ch = (ToggleButton) getRootView().findViewById(R.id.chT);
            ch.setChecked(true);

            Log.d("month", "invalidate");
            view.invalidate();

            oldMonth = month;
            oldYear = year;
        }

    }

    @Override
    public void onResume() {
        Log.d("month", "resume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("month", "pause");
        super.onPause();
    }

    @Override
    public void onClick(View button) {
        SkyActivity activity = (SkyActivity) getActivity();

        switch (button.getId()) {
            case R.id.monthDecr:
            case R.id.monthIncr:
            case R.id.monthNow: {

                if (button.getId() == R.id.monthDecr) {
                    month--;
                    if (month < 1) {
                        month = 12;
                        year--;
                    }
                } else if (button.getId() == R.id.monthIncr) {
                    month++;
                    if (month > 12) {
                        month = 1;
                        year++;
                    }
                } else {
                    Calendar cal = activity.getSkyData().getCalClone();
                    month = cal.get(Calendar.MONTH) + 1;
                    year = cal.get(Calendar.YEAR);
                }

                update();
                break;

            }
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
        int b = 0;
        ToggleButton ch = (ToggleButton) getRootView().findViewById(R.id.chR);
        if (ch.isChecked())
            b |= MonthView.b_rise;
        ch = (ToggleButton) getRootView().findViewById(R.id.chS);
        if (ch.isChecked())
            b |= MonthView.b_set;
        ch = (ToggleButton) getRootView().findViewById(R.id.chT);
        if (ch.isChecked())
            b |= MonthView.b_transit;

        MonthView view = (MonthView) getRootView().findViewById(R.id.monthView);
        view.setToDraw(b);
        view.invalidate();
    }
}