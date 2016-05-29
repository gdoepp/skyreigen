package de.gdoeppert.sky.model;

import java.text.DateFormat;

public class DisplayParams {
    public DateFormat df;
    public DateFormat tf;
    public boolean showHms;
    public boolean showAzS;

    public DisplayParams(DateFormat df, DateFormat tf, boolean showHms,
                         boolean showAzS) {
        this.df = df;
        this.tf = tf;
        this.showHms = showHms;
        this.showAzS = showAzS;
    }
}