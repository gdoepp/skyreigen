package de.gdoeppert.sky.model;

import java.io.Serializable;

public class Star implements Serializable {

    private static final long serialVersionUID = 1L;
    private float ra;
    private float decl;
    private float mag;

    public Star(double ra_h, double ra_m, double ra_s, String decl_sg,
                double decl_g, double decl_m, double decl_s, double mag) {
        setRa((float) (((ra_h * 60 + ra_m) * 60 + ra_s) / 86400.0 * 360));
        setDecl((float) (decl_g + decl_m / 60 + decl_s / 3600));
        if (decl_sg.equals("-"))
            setDecl(-getDecl());
        this.setMag((float) mag);
    }

    public float getRa() {
        return ra;
    }

    public void setRa(float ra) {
        this.ra = ra;
    }

    public float getDecl() {
        return decl;
    }

    public void setDecl(float decl) {
        this.decl = decl;
    }

    public float getMag() {
        return mag;
    }

    public void setMag(float mag) {
        this.mag = mag;
    }
}