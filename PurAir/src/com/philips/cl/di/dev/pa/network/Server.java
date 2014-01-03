package com.philips.cl.di.dev.pa.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import android.os.AsyncTask;
import android.util.Log;

import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;

public class Server extends AsyncTask<String, Void, String> {
	private static final String TAG = "Server";

	private String dataToUpload ;
	private ServerResponseListener responseListener = null;

	private int responseCode ;

	public Server(String dataToUpload, ServerResponseListener responseListener) {
		this.dataToUpload = dataToUpload;
		this.responseListener = responseListener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... url) {
		String result = "";
		InputStream inputStream = null;
		OutputStreamWriter out = null ;
		HttpURLConnection conn = null ;
		try {
			URL urlConn = new URL(url[0]);
			conn = (HttpURLConnection) urlConn.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			out = new OutputStreamWriter(
					conn.getOutputStream());
			out.write(dataToUpload);
			out.close();
			// Starts the query
			conn.connect();
			int responseCode = conn.getResponseCode() ;
			
			if ( responseCode == 200 ) {
				inputStream = conn.getInputStream();	
				result = readFully(inputStream) ;
			}

		} catch (IOException e) {        
		}
		return result; 
	}

	/**
	 * Reads an InputStream and converts it to a String.  
	 *
	 * @param  inputStream	Input stream to convert to string 	
	 * @return	Returns 	converted string
	 */
	// 
	public String readFully(InputStream inputStream) throws IOException, UnsupportedEncodingException {
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

	@Override
	protected void onPostExecute(String result) {
		responseListener.receiveServerResponse(responseCode, result);
		Log.d(TAG, ""+new Date(System.currentTimeMillis())) ;
	}
}
