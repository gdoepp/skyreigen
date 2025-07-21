package de.gdoeppert.sky.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.gdoeppert.sky.R;

public class NumPicker extends LinearLayout implements OnClickListener {


    public NumPicker(Context context, AttributeSet attrs) {

        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.numpic, this);
        Button btn = (Button) findViewById(R.id.tzh1);
        btn.setClickable(true);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.tzh2);
        btn.setClickable(true);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.tzm1);
        btn.setClickable(true);
        btn.setOnClickListener(this);
        btn = (Button) findViewById(R.id.tzm2);
        btn.setClickable(true);
        btn.setOnClickListener(this);
        TextView th = (TextView) findViewById(R.id.tzhtxt);
        TextView tm = (TextView) findViewById(R.id.tzmtxt);
        th.setText("00");
        tm.setText("00");

    }


    public float getOffset() {
        TextView ed = (TextView) this.findViewById(R.id.tzhtxt);
        int h = Integer.valueOf(ed.getText().toString());
        ed = (TextView) this.findViewById(R.id.tzmtxt);
        int m = Integer.valueOf(ed.getText().toString());
        TextView ts = (TextView) findViewById(R.id.tzsign);
        int s = "-".equals(ts.getText().toString()) ? -1 : 1;

        return s * (h + m / 60f);
    }

    public void setOffset(float f) {
        int sig = (f < 0 ? -1 : 1);
        f = Math.abs(f);
        int h = (int) Math.floor(f);
        int m = (int) Math.round((f - h) * 60);
        TextView th = (TextView) findViewById(R.id.tzhtxt);
        TextView tm = (TextView) findViewById(R.id.tzmtxt);
        TextView ts = (TextView) findViewById(R.id.tzsign);
        th.setText(String.format("%02d", h));
        tm.setText(String.format("%02d", m));
        ts.setText(sig < 0 ? "-" : "+");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LinearLayout lv = (LinearLayout) findViewById(R.id.numpicView);
        lv.layout(0, 0, r - l, b - t);
    }


    @Override
    public void onClick(View v) {

        Button btn = (Button) v;
        float f = getOffset();

        int id = btn.getId();
        if (id == R.id.tzh1) {
            f--;
        } else if (id == R.id.tzh2) {
            f++;
        } else if (id == R.id.tzm1) {
            f -= 0.25;
        } else if (id == R.id.tzm2) {
            f += 0.25;
        }
        if (f < -24) f += 24;
        if (f > 24) f -= 24;
        setOffset(f);
    }

    public void setEnabled(boolean e) {
        Button btn = (Button) findViewById(R.id.tzh1);
        btn.setEnabled(e);
        btn = (Button) findViewById(R.id.tzh2);
        btn.setEnabled(e);
        btn = (Button) findViewById(R.id.tzm1);
        btn.setEnabled(e);
        btn = (Button) findViewById(R.id.tzm2);
        btn.setEnabled(e);
    }

}
