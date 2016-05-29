package de.gdoeppert.sky.model;


public class PlanetMoon {
    private double x;
    private double y;
    private boolean ocultated;
    private boolean transit;
    private boolean shadowTransit;
    private String name;
    private int color;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOcultated() {
        return ocultated;
    }

    public void setOcultated(boolean ocultated) {
        this.ocultated = ocultated;
    }

    public boolean isTransit() {
        return transit;
    }

    public void setTransit(boolean transit) {
        this.transit = transit;
    }

    public boolean isShadowTransit() {
        return shadowTransit;
    }

    public void setShadowTransit(boolean shadowTransit) {
        this.shadowTransit = shadowTransit;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
