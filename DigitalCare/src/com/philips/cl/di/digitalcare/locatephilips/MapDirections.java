package com.philips.cl.di.digitalcare.locatephilips;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.philips.cl.di.digitalcare.util.DLog;

/**
 * MapDirections will help to get the end-to-end directions. It will hit google
 * map directions API and gets JSON/XML response, which contains lat,lng and
 * other necessary informations.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 19 May 2015
 */
public class MapDirections {

	private MapDirectionResponse mMapDirectionResponse = null;
	private String TAG = MapDirections.class.getSimpleName();

	interface MapDirectionResponse {
		void onReceived(ArrayList<LatLng> arrayList);
	}

	MapDirections(MapDirectionResponse mapDirectionResponse, LatLng source,
			LatLng destination) {
		mMapDirectionResponse = mapDirectionResponse;

		// Getting URL to the Google Directions API
		String url = getDirectionsUrl(source, destination);
		DownloadTask downloadTask = new DownloadTask();
		// Start downloading json data from Google Directions
		// API
		downloadTask.execute(url);
	}

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;
		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
		// Sensor enabled
		String sensor = "sensor=false&units=metric&mode=driving";
		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;
		// Output format
		String output = "json";
		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;

		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream, "UTF-8"));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			DLog.d(TAG, e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				DLog.d(TAG, "Background Task" + e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				MapDirectionsParser parser = new MapDirectionsParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
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
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

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