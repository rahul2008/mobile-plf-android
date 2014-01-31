package com.philips.cl.disecurity;

import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;
import android.util.Log;

public class DISecurityTask extends AsyncTask<String, Void, String> {

	private ServerResponseListener listener;
	private int responseCode;
	private String deviceId;
	private String url;
	private final String TAG = DISecurityTask.class.getSimpleName();
	
	public DISecurityTask(ServerResponseListener listener) {
		this.listener = listener;
	}

	@Override
	protected String doInBackground(String... params) {
		url = params[0]; 
		deviceId = params[1];
		String data = params[2];
		Log.i(TAG, "url= "+url+" data= "+data);
		OutputStreamWriter out = null;
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		String result = "";
		try {
			URL urlConn = new URL(url);
			conn = (HttpURLConnection) urlConn.openConnection();
			conn.setRequestProperty("content-type", "application/octet-stream");
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			out = new OutputStreamWriter(conn.getOutputStream());
			// TODO add timeouts
			out.write(data);
			out.flush();

			conn.connect();

			responseCode = conn.getResponseCode();
			Log.i(TAG, "responseCode= " +responseCode);
			if (responseCode == 200) {
				inputStream = conn.getInputStream();
				result = readFully(inputStream);
			}
			// TODO detect decryption failed response

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inputStream = null;
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		if (listener != null) {
			listener.receiveServerResponse(responseCode, result, deviceId, url);
		}
	}

	/**
	 * Reads an InputStream and converts it to a String.
	 * 
	 * @param inputStream
	 *            Input stream to convert to string
	 * @return Returns converted string
	 */
	//
	public String readFully(InputStream inputStream) throws IOException,
			UnsupportedEncodingException {
		Reader reader = new InputStreamReader(inputStream, "UTF-8");

		int len = 1024;
		char[] buffer = new char[len];
		StringBuilder sb = new StringBuilder(len);
		int count;

		while ((count = reader.read(buffer)) > 0) {
			sb.append(buffer, 0, count);
		}

		return sb.toString();
	}

}
