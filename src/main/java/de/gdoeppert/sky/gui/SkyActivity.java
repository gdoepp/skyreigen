package de.gdoeppert.sky.gui;

/* Android App Sky 
 * 
 * (c) Gerhard Döppert
 * 
 * SkyReigen.apk is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * SkyReigen.apk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import de.gdoeppert.sky.Messages;
import de.gdoeppert.sky.R;
import de.gdoeppert.sky.model.EarthLocation;
import de.gdoeppert.sky.model.LunarElements;
import de.gdoeppert.sky.model.SkyData;
import de.gdoeppert.sky.model.SolarElements;

public class SkyActivity extends FragmentActivity implements
        ActionBar.TabListener {

    int helpID = R.string.help_quick;

    private static final int OPTION_INFO_ID = 100098;
    private static final int OPTION_HELP_ID = 100099;
    private static final int OPTION_PREFERENCES_ID = 100010;
    public static final int ECLIPTIC_VIEW_ID = 100020;

    private String label_quart = null;

    static {
        System.loadLibrary("astro"); //$NON-NLS-1$
        System.loadLibrary("aaplus"); //$NON-NLS-1$
    }

    private boolean showPointer = true;
    private double temperature = 10;
    private double humidity = 80;
    private int currentTab = -1;

    private final SkyData skyData = new SkyData();

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    MyViewPager mViewPager;

    public SkyActivity() {
    }

    public double getHumidity() {
        return humidity;
    }

    public LunarElements getLunarElements() {
        LunarElements le = getSkyData().getLunarElements();

        return le;
    }

    public SolarElements getSolarElements() {
        SolarElements se = getSkyData().getSolarElements();
        return se;
    }

    public double getTemperature() {
        return temperature;
    }

    void switchDate(View textView, SkyFragment frag) {
        Log.println(Log.DEBUG, "skyactivity", "switching date"); //$NON-NLS-1$ //$NON-NLS-2$

        CharSequence dateStr = ((TextView) textView).getText();
        try {
            DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                    DateFormat.SHORT);
            Date date = df.parse((String) dateStr);
            getSkyData().setTime(date);
            switchToQuick();
            currentTab = 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.println(Log.DEBUG, "skyactivity", "on create"); //$NON-NLS-1$ //$NON-NLS-2$

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        if (label_quart == null) {
            label_quart = this.getResources().getString(R.string.quart);
        }

        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (MyViewPager) findViewById(R.id.pager);
        //mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        Log.println(Log.DEBUG, "view pager", "select page " //$NON-NLS-1$ //$NON-NLS-2$
                                + position);
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(actionBar.newTab()
                    .setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this));
        }

        if (savedInstanceState != null) {
            mSectionsPagerAdapter.restoreState(savedInstanceState);
            onRestoreInstanceState(savedInstanceState);
        }

        Log.println(Log.DEBUG, "sky activity", "create finished"); //$NON-NLS-1$ //$NON-NLS-2$

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, OPTION_PREFERENCES_ID, 0,
                Messages.getString("SkyActivity.preferences")).setIcon( //$NON-NLS-1$
                android.R.drawable.ic_menu_preferences);

        menu.add(OPTION_HELP_ID, OPTION_HELP_ID, 0,
                Messages.getString("SkyActivity.help")).setIcon( //$NON-NLS-1$
                android.R.drawable.ic_menu_help);

        menu.add(OPTION_INFO_ID, OPTION_INFO_ID, 0,
                Messages.getString("SkyActivity.info")).setIcon( //$NON-NLS-1$
                android.R.drawable.ic_menu_info_details);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case OPTION_HELP_ID:
                if (helpID > 0) {
                    showMessage(Messages.getString("SkyActivity.help"),
                            getResources().getString(helpID));
                }
                break;
            case OPTION_INFO_ID:
                showMessage(Messages.getString("SkyActivity.info"), getResources()
                        .getString(R.string.info));
                break;
            case OPTION_PREFERENCES_ID:
                PreferencesDialog pref = new PreferencesDialog();
                pref.show(this.getSupportFragmentManager(), "PrefDialog");
                // showDialog(PREFERENCES_DIALOG_ID);
                break;
        }

        return true;
    }

    @Override
    protected void onPause() {

        try {
            FileOutputStream fos = openFileOutput("locations.txt", MODE_PRIVATE); //$NON-NLS-1$
            PrintWriter pr = new PrintWriter(fos);
            for (EarthLocation loc : skyData.getLocations()) {
                if (!loc.isDummy()) {
                    pr.println(loc.name.replaceAll("\t", "") + "\t" + loc.localLong //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + "\t" + loc.localLat + "\t" + loc.localAltitude + "\t" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                            + loc.gmt);
                }
            }
            pr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileOutputStream fos = openFileOutput("preferences.txt", //$NON-NLS-1$
                    MODE_PRIVATE);
            PrintWriter pr = new PrintWriter(fos);
            pr.println("raHms=" + (skyData.getShowHms() ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            pr.println("showAzS=" + (skyData.getShowAzS() ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            pr.println("showPointer=" + (isShowPointer() ? "true" : "false")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            if (!getSkyData().isLocationDummy()) {
                pr.println("skyData.location=" + getSkyData().getLocationName()); //$NON-NLS-1$
            }
            pr.println("humidity=" + humidity); //$NON-NLS-1$
            pr.println("temperature=" + temperature); //$NON-NLS-1$
            pr.close();
        } catch (FileNotFoundException e) {
            // nothing... //e.printStackTrace();
        }
        super.onPause();
        int ct = currentTab;
    }

    @Override
    protected void onRestoreInstanceState(Bundle state) {

        Log.println(Log.DEBUG, "skyactivity", "on restore instance state"); //$NON-NLS-1$ //$NON-NLS-2$

        getSkyData().setTimeInMillis(state.getLong("time", new Date().getTime())); //$NON-NLS-1$

        mSectionsPagerAdapter.restoreState(state);
        currentTab = state.getInt("currentTab", 0); //$NON-NLS-1$
        if (currentTab < 0 || currentTab >= mViewPager.getChildCount()) {
            currentTab = 0;
        }
        mViewPager.setCurrentItem(currentTab);
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.println(Log.DEBUG, "skyactivity", "on save instance state"); //$NON-NLS-1$ //$NON-NLS-2$

		/*
         * if (planetIdx != AdapterView.INVALID_POSITION) {
		 * outState.putInt("planetIdx", planetIdx); //$NON-NLS-1$ }
		 */
        outState.putLong("time", getSkyData().getTimeInMillis()); //$NON-NLS-1$
        mSectionsPagerAdapter.saveState(outState);
        outState.putInt("currentTab", currentTab); //$NON-NLS-1$

        super.onSaveInstanceState(outState);
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onStart() {

        Log.println(Log.DEBUG, "skyactivity", "on start"); //$NON-NLS-1$ //$NON-NLS-2$
        restore();
        mViewPager.setCurrentItem(currentTab);
        mSectionsPagerAdapter.getItem(currentTab).update();
        Log.println(Log.DEBUG, "skyactivity", "on start -restored"); //$NON-NLS-1$ //$NON-NLS-2$
        super.onStart();
    }

    private void restore() {
        try {
            skyData.getLocations().clear();
            FileInputStream fis = openFileInput("locations.txt"); //$NON-NLS-1$
            BufferedReader rd = new BufferedReader(new InputStreamReader(fis));
            String line;
            try {
                while ((line = rd.readLine()) != null) {
                    String cols[] = line.split("\t"); //$NON-NLS-1$
                    if (cols.length == 5
                            && !cols[0].equals(Messages
                            .getString("SkyActivity.add"))) { //$NON-NLS-1$
                        EarthLocation loc = new EarthLocation(cols[0],
                                Double.valueOf(cols[1]),
                                Double.valueOf(cols[2]),
                                Double.valueOf(cols[3]), Float.valueOf(cols[4]));
                        skyData.getLocations().add(loc);
                    } else if (cols.length == 3
                            && !cols[0].equals(Messages
                            .getString("SkyActivity.add"))) { //$NON-NLS-1$
                        EarthLocation loc = new EarthLocation(cols[0],
                                Double.valueOf(cols[1]),
                                Double.valueOf(cols[2]), Double.valueOf(200),
                                Float.valueOf(-99));
                        skyData.getLocations().add(loc);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        skyData.normalizeLocations();

        try {
            FileInputStream fis = openFileInput("preferences.txt"); //$NON-NLS-1$
            BufferedReader rd = new BufferedReader(new InputStreamReader(fis));
            String line;
            try {
                while ((line = rd.readLine()) != null) {
                    String cols[] = line.split("="); //$NON-NLS-1$
                    if (cols.length == 2) {
                        if (cols[0].equals("raHms")) //$NON-NLS-1$
                            skyData.setShowHms(cols[1].equals("true")); //$NON-NLS-1$
                        else if (cols[0].equals("showPointer")) //$NON-NLS-1$
                            setShowPointer(cols[1].equals("true")); //$NON-NLS-1$
                        else if (cols[0].equals("skyData.getLocation()") ||
                                cols[0].equals("skyData.location")) { //$NON-NLS-1$
                            for (int j = 0; j < skyData.getLocations().size(); j++) {
                                if (!skyData.getLocations().get(j).isDummy()
                                        && skyData.getLocations().get(j).name
                                        .equals(cols[1])) {
                                    getSkyData().setLocation(j);
                                    break;
                                }
                            }
                        } else if (cols[0].equals("humidity")) { //$NON-NLS-1$
                            humidity = Double.valueOf(cols[1]);
                        } else if (cols[0].equals("temperature")) { //$NON-NLS-1$
                            temperature = Double.valueOf(cols[1]);
                        } else if (cols[0].equals("showAzS")) { //$NON-NLS-1$
                            skyData.setShowAzS(cols[1].equals("true")); //$NON-NLS-1$
                        }
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (!skyData.getLocations().isEmpty() && skyData.isLocationDummy()) {
            skyData.setLocation(0);
        }
    }

    void setLocationAdapter(Spinner spinner) {
        Vector<String> lcs = new Vector<String>();
        for (EarthLocation loc : skyData.getLocations()) {
            lcs.add(loc.toString());
        }
        ArrayAdapter<CharSequence> locationAdapter = new ArrayAdapter<CharSequence>(
                this, android.R.layout.simple_spinner_item,
                lcs.toArray(new String[0]));
        locationAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(locationAdapter);
        spinner.invalidate();
    }

    public void showMessage(String title, String msg) {

        FragmentManager fm = getSupportFragmentManager();
        MessageDialog messageDialog = new MessageDialog();
        messageDialog.setMessage(title, msg); //$NON-NLS-1$
        messageDialog.show(fm, "messageDialog"); //$NON-NLS-1$

    }

    private void switchToQuick() {
        Log.println(Log.DEBUG, "skyactivity", "switch to quick"); //$NON-NLS-1$ //$NON-NLS-2$
        getSkyData().getSolarElements();
        getSkyData().getLunarElements();
        if (mViewPager.getCurrentItem() == 0) {
            mSectionsPagerAdapter.getItem(Frags.itQuick.ordinal()).update();
        } else {
            mViewPager.setCurrentItem(0);
            currentTab = 0;
        }
    }

    public void setHelpId(int id) {
        helpID = id;

    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction arg1) {
        mViewPager.setCurrentItem(tab.getPosition());
        currentTab = mViewPager.getCurrentItem();

        SkyFragment frag = mSectionsPagerAdapter.getItem(currentTab);

        frag.update();

        helpID = frag.getHelpId();
        Log.println(Log.DEBUG, "skyactivity", //$NON-NLS-1$
                "on tab reselected " + tab.getPosition()); //$NON-NLS-1$

    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction arg1) {
        Log.println(Log.DEBUG, "skyactivity", //$NON-NLS-1$
                "on tab selected " + tab.getPosition()); //$NON-NLS-1$
        mViewPager.setCurrentItem(tab.getPosition());
        currentTab = mViewPager.getCurrentItem();

        SkyFragment frag = mSectionsPagerAdapter.getItem(tab.getPosition());

        frag.update();

        helpID = frag.getHelpId();

    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction arg1) {
        setProgressBarIndeterminateVisibility(false);

        SkyFragment frag = mSectionsPagerAdapter.getItem(tab.getPosition());
        frag.unselect();
        currentTab = -1;
        Log.d("skyactivity", //$NON-NLS-1$
                "on tab unselected " + tab.getPosition()); //$NON-NLS-1$

    }

    public SkyData getSkyData() {
        return skyData;
    }

    enum Frags {
        itQuick, itSun, itMoon, itPlanet, itEcliptic, itEquatorial, itMonth
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        Frags frags;

        SkyFragment fragments[] = new SkyFragment[Frags.values().length];

        FragmentManager fm;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }

        public void saveState(Bundle bundle) {
            Log.println(Log.DEBUG, "s-p-adapter", "save state"); //$NON-NLS-1$ //$NON-NLS-2$
            if (bundle == null)
                return;

            for (Frags f : Frags.values()) {
                SkyFragment frag = fragments[f.ordinal()];
                if (frag != null) {
                    bundle.putString("Frag" + f.name(), frag.getTag()); //$NON-NLS-1$
                }
            }
        }

        public void restoreState(Bundle bundle) {
            Log.println(Log.DEBUG, "s-p-adapter", "restore state"); //$NON-NLS-1$ //$NON-NLS-2$
            if (bundle == null)
                return;

            for (Frags f : Frags.values()) {
                String tag = bundle.getString("Frag" + f.name()); //$NON-NLS-1$
                if (tag != null) {
                    SkyFragment frag = (SkyFragment) fm.findFragmentByTag(tag);
                    if (frag != null) {
                        Log.println(Log.DEBUG, "s-p-adapter", "found fragment " //$NON-NLS-1$ //$NON-NLS-2$
                                + f.name());
                        fragments[f.ordinal()] = frag;
                    }
                }
            }

        }

        @Override
        public SkyFragment getItem(int position) {

            if (fragments[position] == null) {
                Log.println(Log.DEBUG, "s-p-adapter", "creating item " //$NON-NLS-1$ //$NON-NLS-2$
                        + position);

                switch (position) {

                    case 0:
                        fragments[position] = new QuickFragment();
                        break;
                    case 1:
                        fragments[position] = new SunFragment();
                        break;
                    case 2:
                        fragments[position] = new MoonFragment();
                        break;
                    case 3:
                        fragments[position] = new PlanetFragment();
                        break;
                    case 4:
                        fragments[position] = new EclipticFragment();
                        break;
                    case 5:
                        fragments[position] = new EquatorialFragment();
                        break;
                    case 6:
                        fragments[position] = new MonthFragment();
                        break;
                }

            }
            return fragments[position];

        }

        @Override
        public int getCount() {
            return Frags.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale.getDefault();
            switch (position) {
                case 0:
                    return Messages.getString("SkyActivity.quick"); //$NON-NLS-1$
                case 1:
                    return Messages.getString("SkyActivity.sun"); //$NON-NLS-1$
                case 2:
                    return Messages.getString("SkyActivity.moon"); //$NON-NLS-1$
                case 3:
                    return Messages.getString("SkyActivity.planet"); //$NON-NLS-1$
                case 4:
                    return Messages.getString("SkyActivity.ecliptic"); //$NON-NLS-1$
                case 5:
                    return Messages.getString("SkyActivity.equatorial"); //$NON-NLS-1$
                case 6:
                    return Messages.getString("SkyActivity.month"); //$NON-NLS-1$
            }
            return null;
        }
    }

    public void setTemperature(Double valueOf) {
        temperature = valueOf;

    }

    public void setHumidity(Double valueOf) {
        humidity = valueOf;

    }

    public void setShowPointer(boolean checked) {
        showPointer = checked;

    }

    public boolean isShowPointer() {
        return showPointer;
    }

}
