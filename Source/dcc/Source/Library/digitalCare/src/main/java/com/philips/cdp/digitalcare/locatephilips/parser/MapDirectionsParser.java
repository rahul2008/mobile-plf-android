/**
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.locatephilips.parser;

import com.google.android.gms.maps.model.LatLng;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * MapDirectionsParser parse JSON response received from google map for
 * directions.
 */
public class MapDirectionsParser {

    private static final String TAG = MapDirectionsParser.class.getSimpleName();

    /**
     * Receives a JSONObject and returns a list of lists containing latitude and
     * longitude
     */
    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        final List<List<HashMap<String, String>>> routes = new ArrayList<List<
                HashMap<String, String>>>();
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;
        try {
            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                final List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = null;
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps
                                .get(k)).get("polyline")).get("points");
                        final List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            final HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat",
                                    Double.toString(list.get(l).latitude));
                            hm.put("lng",
                                    Double.toString(list.get(l).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }
        } catch (JSONException e) {
            DigiCareLogger.e(TAG, "JSONException : " + e);
        }
        return routes;
    }

    /**
     * Method to decode polyline points
     */
    private List<LatLng> decodePoly(String encoded) {

        final List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0;
        final int len = encoded.length();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b, shift = 0;
            int result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = isResultTrue(result) ? getIntAValue(result) : getIntBValue(result);
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = (isResultTrue(result) ? getIntAValue(result) : (getIntBValue(result)));
            lng += dlng;

            LatLng p = new LatLng(getLatitudeValue(lat),
                    ((getLatitudeValue(lng))));
            poly.add(p);
        }
        return poly;
    }

    private double getLatitudeValue(double lat) {
        return lat / 1E5;
    }

    private boolean isResultTrue(int result) {
        return (result & 1) != 0;
    }

    private int getIntBValue(int result) {
        return result >> 1;
    }

    private int getIntAValue(int result) {
        return ~(getIntBValue(result));
    }
}