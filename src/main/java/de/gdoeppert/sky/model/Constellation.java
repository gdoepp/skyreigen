package de.gdoeppert.sky.model;

import java.util.Vector;

public class Constellation implements java.lang.Cloneable {
    private float decl = 0;
    private float ra = 0;
    private String name;
    private Vector<ConstellationVertex> vertices = new Vector<ConstellationVertex>(
            16);

    public boolean normalize(float x1, float x2) {
        boolean renorm = false;
        for (ConstellationVertex vert : getVertices()) {
            if (vert.getRa() < x1) {
                vert.setRa(vert.getRa() + 360);
                renorm = true;
            }
            if (vert.getRa() > x2) {
                vert.setRa(vert.getRa() - 360);
                renorm = true;
            }
        }
        return renorm;
    }

    @Override
    public Object clone() {
        Constellation obj = null;
        try {
            obj = (Constellation) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
        obj.setVertices(new Vector<ConstellationVertex>(getVertices().size()));
        for (ConstellationVertex vtx : getVertices()) {
            ConstellationVertex nvtx = new ConstellationVertex(vtx.getRa(),
                    vtx.getDecl());
            obj.getVertices().add(nvtx);
        }
        return obj;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vector<ConstellationVertex> getVertices() {
        return vertices;
    }

    public void setVertices(Vector<ConstellationVertex> vertices) {
        this.vertices = vertices;
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