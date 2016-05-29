package de.gdoeppert.sky.gui;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;

public class TimeDialog extends DialogFragment {

    OnTimeSetListener ontimeSet;

    public TimeDialog() {
    }

    public void setCallBack(OnTimeSetListener ontime) {
        ontimeSet = ontime;
    }

    private int hour, minute;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        hour = args.getInt("hour");
        minute = args.getInt("minute");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), ontimeSet, hour, minute,
                true);
    }

}