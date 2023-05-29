package de.gdoeppert.sky.gui;

import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;

import de.gdoeppert.sky.Messages;
import de.gdoeppert.sky.R;

// ...

public class PreferencesDialog extends DialogFragment {

    public PreferencesDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final SkyActivity activity = (SkyActivity) getActivity();

        final View layout = inflater.inflate(R.layout.preferencesdialog,
                container, false);

        getDialog().setTitle(Messages.getString("SkyActivity.preferences"));

        EditText ed = (EditText) layout.findViewById(R.id.temp);
        ed.setText(String.valueOf(activity.getTemperature()));
        ed = (EditText) layout.findViewById(R.id.humid);
        ed.setText(String.valueOf(activity.getHumidity()));
        CheckBox chb = (CheckBox) layout.findViewById(R.id.showptr);
        chb.setChecked(activity.isShowPointer());
        RadioButton rdb = (RadioButton) layout.findViewById(R.id.RA_HM);
        rdb.setChecked(activity.getSkyData().getShowHms());
        rdb = (RadioButton) layout.findViewById(R.id.azS);
        rdb.setChecked(activity.getSkyData().getShowAzS());

        final Button ok = (Button) layout.findViewById(R.id.prefOk);

        ok.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View btn) {
                try {
                    EditText ed = (EditText) layout.findViewById(R.id.temp);
                    String temp = ed.getText().toString();
                    activity.setTemperature(Double.valueOf(temp));
                    ed = (EditText) layout.findViewById(R.id.humid);
                    String humid = ed.getText().toString();
                    activity.setHumidity(Double.valueOf(humid));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CheckBox chb = (CheckBox) layout.findViewById(R.id.showptr);
                activity.setShowPointer(chb.isChecked());
                RadioButton rdb = (RadioButton) layout.findViewById(R.id.RA_HM);
                activity.getSkyData().setShowHms(rdb.isChecked());
                rdb = (RadioButton) layout.findViewById(R.id.azS);
                activity.getSkyData().setShowAzS(rdb.isChecked());
                PreferencesDialog.this.dismiss();
            }
        });

        final Button cancel = (Button) layout.findViewById(R.id.prefCancel);

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                PreferencesDialog.this.dismiss();
            }
        });

        return layout;
    }

}
