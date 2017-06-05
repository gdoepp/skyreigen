package de.gdoeppert.sky.model;

import android.util.Log;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Vector;

public class SkyData {

    public enum PlanetId {
        mercury, venus, mars, jupiter, saturn, uranus, neptune
    }

    ;

    private Calendar cal;
    private LunarElements lunarElements;
    private SolarElements solarElements;
    private final PlanetElements[] planetElements = new PlanetElements[PlanetId
            .values().length];
    private EarthLocation location = new EarthLocation();
    private DisplayParams displayParameter = null;
    private boolean sunDirty;
    private boolean moonDirty;
    private final boolean[] planetDirty = new boolean[PlanetId.values().length];
    private final boolean[] planetEventsDirty = new boolean[PlanetId.values().length];
    private final SolSysPositions solSysPositions = new SolSysPositions();
    private final Vector<EarthLocation> locations = new Vector<EarthLocation>();

    public SkyData() {

        this.cal = GregorianCalendar.getInstance();
        this.displayParameter = new DisplayParams(DateFormat.getDateInstance(),
                DateFormat.getTimeInstance(DateFormat.SHORT), false, false);

        setDirty();

    }

    public void addCal(int fieldid, int value) {
        cal.add(fieldid, value);
        setDirty();

    }

    public CharSequence formatDate(Date time) {

        return displayParameter.df.format(time);
    }

    public CharSequence formatTime(Date time) {

        return displayParameter.tf.format(time);
    }

    public int getCal(int fieldid) {
        return cal.get(fieldid);
    }

    public Calendar getCalClone() {
        return (Calendar) cal.clone();
    }

    public DisplayParams getDisplayParams() {
        return displayParameter;
    }

    public double getLocationAltitude() {
        return location.localAltitude;
    }

    public float getLocationGmt() {
        return location.gmt;
    }

    public int getLocationIndex() {
        if (location == null)
            return -1;
        return locations.indexOf(location);
    }

    public double getLocationLat() {
        return location.localLat;
    }

    public double getLocationLong() {
        return location.localLong;
    }

    public String getLocationName() {
        return location.name;
    }

    public boolean isLocationDummy() {
        return location.isDummy();
    }

    public Vector<EarthLocation> getLocations() {
        return locations;
    }

    public TimeZone getLocationTimeZone() {
        return location.getTimeZone();
    }

    public LunarElements getLunarElements() {
        if (lunarElements == null || moonDirty) {
            LunarElements le = new LunarElements(cal, location,
                    displayParameter);
            le.calculate(getSolarElements());
            lunarElements = le;
            moonDirty = false;
            Log.println(Log.DEBUG, "skyData", "moon clean");
        }
        return lunarElements;
    }

    public PlanetElements getPlanetElements(PlanetId planetId) {

        int planetIdx = planetId.ordinal();
        if (planetElements[planetIdx] == null
                || planetDirty[planetIdx]) {
            switch (planetId) {
                case mercury:
                    planetElements[planetIdx] = new MercuryElements(cal,
                            location, displayParameter);
                    break;
                case venus:
                    planetElements[planetIdx] = new VenusElements(cal,
                            location, displayParameter);
                    break;
                case mars:
                    planetElements[planetIdx] = new MarsElements(cal,
                            location, displayParameter);
                    break;
                case jupiter:
                    planetElements[planetIdx] = new JupiterElements(cal,
                            location, displayParameter);
                    break;
                case saturn:
                    planetElements[planetIdx] = new SaturnElements(cal,
                            location, displayParameter);
                    break;
                case uranus:
                    planetElements[planetIdx] = new UranusElements(cal,
                            location, displayParameter);
                    break;
                case neptune:
                    planetElements[planetIdx] = new NeptuneElements(cal,
                            location, displayParameter);
                    break;
            }
            planetElements[planetIdx].calculate();
            planetDirty[planetIdx] = false;
            Log.println(Log.DEBUG, "skyData", "planet " + planetId.name()
                    + " clean");
        }
        return planetElements[planetIdx];
    }

    public boolean getShowAzS() {

        return displayParameter.showAzS;
    }

    public boolean getShowHms() {

        return displayParameter.showHms;
    }

    public SolarElements getSolarElements() {
        if (solarElements == null || sunDirty) {
            SolarElements se = new SolarElements(cal, location,
                    displayParameter);
            se.calculate();
            solarElements = se;
            sunDirty = false;
            Log.println(Log.DEBUG, "skyData", "sun clean");

        }
        return solarElements;
    }

    public SolSysElements getSolarElements(Calendar cal) {
        SolSysElements sol = new SolarElements(cal, location, displayParameter);
        sol.calculate();
        return sol;
    }

    public SolSysPositions getSolSysPositions() {

        return solSysPositions;
    }

    public Date getTime() {
        return cal.getTime();
    }

    public long getTimeInMillis() {

        return cal.getTimeInMillis();
    }

    public void removeCurrentLocation() {
        locations.remove(location);
        setLocation(0);
    }

    public void resetCal() {
        cal = GregorianCalendar.getInstance();
        setDirty();
    }

    public void setCal(int fieldid, int value) {
        cal.set(fieldid, value);
        setDirty();
    }

    public void setLocation(int j) {
        location = locations.get(j);
        displayParameter.df.setTimeZone(location.getTimeZone());
        displayParameter.tf.setTimeZone(location.getTimeZone());
        setDirty();
    }

    public void setLocation(int idx, EarthLocation loc) {
        EarthLocation loc0 = ((idx >= 0 && idx < locations.size()) ? locations
                .get(idx) : null);
        if (loc0 != null && idx < locations.size() - 1) {
            locations.set(idx, loc);
        } else {
            while (idx >= locations.size()) {
                locations.add(new EarthLocation());
            }
            locations.insertElementAt(loc, idx);
        }

        setLocation(idx);
    }

    public void setShowAzS(boolean checked) {
        displayParameter.showAzS = checked;
        setDirty();
    }

    public void setShowHms(boolean checked) {
        displayParameter.showHms = checked;
        setDirty();
    }

    public void setTime(Date time) {
        cal.setTime(time);
        setDirty();
    }

    public void setTimeInMillis(long milliseconds) {
        cal.setTimeInMillis(milliseconds);
        setDirty();
    }

    private void setDirty() {
        sunDirty = true;
        moonDirty = true;
        for (PlanetId i : PlanetId.values()) {
            planetDirty[i.ordinal()] = true;
            planetEventsDirty[i.ordinal()] = true;
        }
        Log.println(Log.DEBUG, "skyData", "setDirty");
    }

}
