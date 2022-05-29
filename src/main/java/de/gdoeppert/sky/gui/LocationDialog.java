package de.gdoeppert.sky.gui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import de.gdoeppert.sky.Messages;
import de.gdoeppert.sky.R;
import de.gdoeppert.sky.gui.SkyActivity.Frags;
import de.gdoeppert.sky.model.EarthLocation;

// ...

public class LocationDialog extends DialogFragment {

    public LocationDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final SkyActivity activity = (SkyActivity) getActivity();

        final View layout = inflater.inflate(R.layout.locdialog, container,
                false);

        getDialog().setTitle(Messages.getString("Location.title"));

        EditText ed = (EditText) layout.findViewById(R.id.city);
        if (activity.getSkyData().isLocationDummy()) {
            ed.setText("?"); //$NON-NLS-1$
        } else {
            ed.setText(activity.getSkyData().getLocationName());
        }
        ed = (EditText) layout.findViewById(R.id.latitude);
        ed.setText(String.valueOf(activity.getSkyData().getLocationLat()));
        ed = (EditText) layout.findViewById(R.id.longitude);
        ed.setText(String.valueOf(activity.getSkyData().getLocationLong()));
        ed = (EditText) layout.findViewById(R.id.altitude);
        ed.setText(String.valueOf(activity.getSkyData().getLocationAltitude()));

        CheckBox tzLocal = (CheckBox) layout.findViewById(R.id.chLocal);
        NumPicker np = (NumPicker) layout.findViewById(R.id.tzPicker);

        tzLocal.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                NumPicker np = (NumPicker) layout.findViewById(R.id.tzPicker);
                np.setEnabled(!arg1);
                float offset = (arg1
                        || activity.getSkyData().getLocationGmt() < -24 ? TimeZone
                        .getDefault().getOffset(0) / 3600000f : activity
                        .getSkyData().getLocationGmt());
                np.setOffset(offset);
            }

        });

        if (activity.getSkyData().getLocationGmt() > 24
                || activity.getSkyData().getLocationGmt() < -24) {
            tzLocal.setChecked(true);
            np.setEnabled(false);
        } else {
            tzLocal.setChecked(false);
            np.setEnabled(true);
        }
        float offset = (tzLocal.isChecked()
                || activity.getSkyData().getLocationGmt() < -24 ? TimeZone
                .getDefault().getOffset(0) / 3600000f : activity.getSkyData()
                .getLocationGmt());
        np.setOffset(offset);

        Button btn = (Button) layout.findViewById(R.id.locCancel);

        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View btn0) {

                Spinner sp = (Spinner) activity.mSectionsPagerAdapter
                        .getItem(Frags.itQuick.ordinal()).getRootView()
                        .findViewById(R.id.location);
                if (activity.getSkyData().isLocationDummy()) {
                    sp.setSelection(0);
                }
                LocationDialog.this.dismiss();
            }
        });

        btn = (Button) layout.findViewById(R.id.locDelete);
        btn.setEnabled(activity.getSkyData().getLocations().size()>2 && !activity.getSkyData().isLocationDummy()); // 1+dummy
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View btn0) {

                Spinner sp = (Spinner) activity.mSectionsPagerAdapter
                        .getItem(Frags.itQuick.ordinal()).getRootView()
                        .findViewById(R.id.location);

                if (sp.getCount() > 1 ) {
                    activity.getSkyData().removeCurrentLocation();
                    activity.setLocationAdapter(sp);
                    sp.setSelection(0);
                }
                LocationDialog.this.dismiss();
            }
        });

        btn = (Button) layout.findViewById(R.id.locOk);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View btn0) {
                EditText ed = (EditText) layout.findViewById(R.id.city);
                String city = ed.getText().toString();
                ed = (EditText) layout.findViewById(R.id.latitude);
                String lat = ed.getText().toString();
                ed = (EditText) layout.findViewById(R.id.longitude);
                String lng = ed.getText().toString();
                CheckBox tzLocal = (CheckBox) layout.findViewById(R.id.chLocal);
                float gmt = -99;
                if (tzLocal.isChecked()) {
                    gmt = -99;
                } else {
                    NumPicker np = (NumPicker) layout
                            .findViewById(R.id.tzPicker);
                    gmt = np.getOffset();
                }

                ed = (EditText) layout.findViewById(R.id.altitude);
                String alt = ed.getText().toString();

                int locIdx = activity.getSkyData().getLocationIndex();

                Spinner sp = (Spinner) activity.mSectionsPagerAdapter
                        .getItem(Frags.itQuick.ordinal()).getRootView()
                        .findViewById(R.id.location);

                try {
                    EarthLocation loc = new EarthLocation(city, Double
                            .valueOf(lng), Double.valueOf(lat), Double
                            .valueOf(alt), gmt);
                    activity.getSkyData().setLocation(locIdx, loc);

                    activity.setLocationAdapter(sp);
                    sp.setSelection(locIdx, true);
                    LocationDialog.this.dismiss();
                } catch (Exception e) {
                    // sp.setSelection(0);
                    return;
                }

            }
        });

        Button loc = (Button) layout.findViewById(R.id.locate);
        loc.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View btn) {

                LocationManager lm = (LocationManager) activity
                        .getSystemService(Context.LOCATION_SERVICE);

                if (lm == null) return;

                Criteria whatFor = new Criteria();
                String providerName = lm.getBestProvider(whatFor, true);

                if (providerName != null) {
                    Location loc = null;

                    try {
                        loc = lm.getLastKnownLocation(providerName);
                    } catch (SecurityException ex) {
                        // nothing
                    }
                    if (loc != null) {
                        EditText ed = (EditText) layout
                                .findViewById(R.id.latitude);
                        ed.setText(String.valueOf(loc.getLatitude()));
                        ed = (EditText) layout.findViewById(R.id.longitude);
                        ed.setText(String.valueOf(loc.getLongitude()));
                        if (loc.hasAltitude()) {
                            ed = (EditText) layout.findViewById(R.id.altitude);
                            ed.setText(String.valueOf(loc.getAltitude()));
                        }

                        Geocoder coder = new Geocoder(activity);
                        if (coder != null)
                            try {
                                List<Address> addresses = coder
                                        .getFromLocation(loc.getLatitude(),
                                                loc.getLongitude(), 1);
                                for (Address addr : addresses) {
                                    if (addr.getLocality() == null
                                            || !addr.hasLatitude()
                                            || !addr.hasLongitude())
                                        continue;
                                    ed = (EditText) layout
                                            .findViewById(R.id.city);
                                    ed.setText(String.valueOf(addr
                                            .getLocality()));
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                    }
                }
            }
        });
        Button lookup = (Button) layout.findViewById(R.id.lookup);
        lookup.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View btn) {
                EditText ed = (EditText) layout.findViewById(R.id.city);
                String city = ed.getText().toString();
                LocationManager lm = (LocationManager) activity
                        .getSystemService(Context.LOCATION_SERVICE);

                Criteria whatFor = new Criteria();

                String providerName = lm.getBestProvider(whatFor, true);

                if (providerName == null || providerName.equals("")) return;

                try {
                    if(ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                    lm.requestLocationUpdates(providerName, 20000l, 1f,
                            new LocationListener() {

                                @Override
                                public void onLocationChanged(Location arg0) {

                                }

                                @Override
                                public void onProviderDisabled(String arg0) {

                                }

                                @Override
                                public void onProviderEnabled(String arg0) {

                                }

                                @Override
                                public void onStatusChanged(String arg0, int arg1,
                                                            Bundle arg2) {

                                }
                            });
                    }else {
                        // ignore
                    }

                } catch (Exception ex) {
                    // ignore
                }
                Geocoder coder = new Geocoder(activity);

                Address address = null;
                try {
                    List<Address> addresses = coder
                            .getFromLocationName(city, 1);
                    for (Address addr : addresses) {
                        if (addr.getLocality() == null || !addr.hasLatitude()
                                || !addr.hasLongitude())
                            continue;
                        address = addr;
                        break;
                    }
                } catch (IOException e) {
                    // ignore
                    e.printStackTrace();
                }

                if (address == null) { // Geocoder not working, try
                    // workaround...
                    try {
                        address = getLocationFromString(city);
                    } catch (JSONException e) {
                        // ignore
                        e.printStackTrace();
                    }
                }
                if (address != null) {
                    setLocation(layout, address);
                }
            }

            void setLocation(final View layout, Address addr) {
                EditText ed;
                ed = (EditText) layout.findViewById(R.id.city);
                ed.setText(String.valueOf(addr.getLocality()));
                ed = (EditText) layout.findViewById(R.id.latitude);
                ed.setText(String.valueOf(addr.getLatitude()));
                ed = (EditText) layout.findViewById(R.id.longitude);
                ed.setText(String.valueOf(addr.getLongitude()));
            }
        });

        return layout;
    }

    public static Address getLocationFromString(String address)
            throws JSONException {

        address = address.replaceAll(" ", "%20");
        StringBuilder stringBuilder = new StringBuilder();
        try {
            URL httpGet = new URL(
                    "http://maps.google.com/maps/api/geocode/json?address="
                            + address + "&ka&sensor=false");
            HttpURLConnection httpCon = (HttpURLConnection) httpGet.openConnection();

            InputStream stream = httpCon.getInputStream();

            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (Exception e) {
            return null;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject = new JSONObject(stringBuilder.toString());

        double lng = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lng");

        double lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0)
                .getJSONObject("geometry").getJSONObject("location")
                .getDouble("lat");

        Address ad = new android.location.Address(Locale.getDefault());
        ad.setLongitude(lng);
        ad.setLatitude(lat);
        ad.setLocality(address);
        return ad;
    }

    public static List<Address> getStringFromLocation(double lat, double lng)
            throws  IOException, JSONException {

        String address = String
                .format(Locale.ENGLISH,
                        "http://maps.googleapis.com/maps/api/geocode/json?latlng=%1$f,%2$f&sensor=true&language="
                                + Locale.getDefault().getCountry(), lat, lng);
        URL url = new URL(address);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        InputStream stream = httpCon.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();

        List<Address> retList = null;

        int b;
        while ((b = stream.read()) != -1) {
            stringBuilder.append((char) b);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject = new JSONObject(stringBuilder.toString());

        retList = new ArrayList<Address>();

        if ("OK".equalsIgnoreCase(jsonObject.getString("status"))) {
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String indiStr = result.getString("formatted_address");
                Address addr = new Address(Locale.getDefault());
                addr.setAddressLine(0, indiStr);
                retList.add(addr);
            }
        }

        return retList;
    }
}
