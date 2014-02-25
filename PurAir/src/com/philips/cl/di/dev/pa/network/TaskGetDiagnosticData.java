package com.philips.cl.di.dev.pa.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.philips.cl.disecurity.DISecurity;

public class TaskGetDiagnosticData extends AsyncTask<String, Void, String[]> {
	Context context;

	public TaskGetDiagnosticData(Context pContext) {
		context = pContext;
	}

	ProgressDialog pDialog;

	@Override
	protected void onPreExecute() {
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Collecting diagnostics information...");
		pDialog.show();
	}

	@Override
	protected String[] doInBackground(String... urls) {
		String[] result = new String[urls.length];

		for (int i = 0; i < urls.length; i++) {
			result[i] = downloadUrl(urls[i]);
			result[i] = new DISecurity(null).decryptData(result[i], "dev01");
			// Escape early if cancel() is called
			if (isCancelled())
				break;
		}

		return result;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String[] response) {
		pDialog.dismiss();
		context = null;
	}

	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves the web page
	 * content as an InputStream, which it returns as a string.
	 * 
	 * @param stringUrl
	 *            The given URL
	 * @return Returns web page as a string
	 */
	private static String downloadUrl(String stringUrl) {
		InputStream inputStream = null;
		HttpURLConnection conn = null;
		String data = null;
		try {
			URL url;
			url = new URL(stringUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("content-type", "application/json");
			conn.connect();
			inputStream = conn.getInputStream();

			// Convert the InputStream into a string
			data = readFully(inputStream);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {

				}
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return data;
	}

	/**
	 * Reads an InputStream and converts it to a String.
	 * 
	 * @param inputStream
	 *            Input stream to convert to string
	 * @return Returns converted string
	 */
	//
	public static String readFully(InputStream inputStream) throws IOException,
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
