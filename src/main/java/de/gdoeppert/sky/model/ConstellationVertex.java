package de.gdoeppert.sky.model;

public class ConstellationVertex {
    private float ra;
    private float decl;

    public ConstellationVertex(float r, float d) {
        setRa(r);
        setDecl(d);
    }

    public float getDecl() {
        return decl;
    }

    public void setDecl(float decl) {
        this.decl = decl;
    }

    public float getRa() {
        return ra;
    }

    public void setRa(float ra) {
        this.ra = ra;
    }
}