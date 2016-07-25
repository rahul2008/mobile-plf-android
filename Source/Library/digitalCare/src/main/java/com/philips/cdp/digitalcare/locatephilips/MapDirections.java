/**
 * MapDirections will help to get the end-to-end directions. It will hit google
 * map directions API and gets JSON/XML response, which contains lat,lng and
 * other necessary informations.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 19 May
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.locatephilips;


import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.philips.cdp.digitalcare.locatephilips.parser.MapDirectionsParser;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * This class responsible for drawaing & deciding the source & destination line of
 * the user to the required philips customer/service center.
 */
public class MapDirections {

    private static final String TAG = MapDirections.class.getSimpleName();
    protected MapDirectionResponse mMapDirectionResponse = null;

    public MapDirections(MapDirectionResponse mapDirectionResponse, LatLng source, LatLng
            destination) {
        mMapDirectionResponse = mapDirectionResponse;

        // Getting URL to the Google Directions API
        final String url = getDirectionsUrl(source, destination);
        final DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions
        // API
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        final String str_origin = "origin=" + origin.latitude + ","
                + origin.longitude;
        // Destination of route
        final String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Sensor enabled
        final String sensor = "sensor=false&units=metric&mode=driving";
        // Building the parameters to the web service
        final String parameters = str_origin + "&" + str_dest + "&" + sensor;
        // Output format
        final String output = "json";
        // Building the url to the web service
        final String url = "https://maps.googleapis.com/maps/api/directions/"
                + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(final String param1) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            final URL url = new URL(param1);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            final InputStreamReader inputStreamReader = new InputStreamReader(
                    iStream, "UTF-8");

            final BufferedReader br = new BufferedReader(inputStreamReader);

            final StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            DigiCareLogger.e(TAG, e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /**
     * MapDirectionResponse interface serves callback service as soon as the
     * info available.
     */
    public interface MapDirectionResponse {
        void onReceived(ArrayList<LatLng> arrayList);
    }

    /**
     * Network wrapper code for fetching the network data.
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (IOException e) {
                DigiCareLogger.e(TAG, "Background Task" + e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            final ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends
            AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(
                String... jsonData) {

            JSONObject jObject = null;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                final MapDirectionsParser parser = new MapDirectionsParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (JSONException e) {
                DigiCareLogger.e(TAG, "JSON Exception while getting the map releated json : " + e);
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            // PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                // lineOptions = new PolylineOptions();

                // Fetching i-th route
                final List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    final HashMap<String, String> point = path.get(j);

                    final double lat = Double.parseDouble(point.get("lat"));
                    final double lng = Double.parseDouble(point.get("lng"));
                    final LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }
                //
                // // Adding all the points in the route to LineOptions
                // lineOptions.addAll(points);
                // lineOptions.width(2);
                // lineOptions.color(Color.RED);
            }

            mMapDirectionResponse.onReceived(points);

            // Drawing polyline in the Google Map for the i-th route
            // map.addPolyline(lineOptions);
        }
    }
}