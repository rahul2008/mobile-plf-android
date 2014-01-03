package com.philips.cl.di.dev.pa.network;

import java.io.IOException;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.philips.cl.di.dev.pa.interfaces.ServerResponseListener;


import android.content.Context;

public class TaskGetHttp extends Thread {

	private String url ;
	private ServerResponseListener listener ;

	public TaskGetHttp(String url,Context context, ServerResponseListener listener) {
		this.url = url ;
		this.listener = listener ;
	}

	@Override
	public void run() {
			InputStream inputStream = null;
			HttpURLConnection conn = null ;
			try {
				URL urlConn = new URL(url);
				conn = (HttpURLConnection) urlConn.openConnection() ;
				conn.setRequestMethod("GET");
				// Starts the query
				conn.connect();
				int responseCode = conn.getResponseCode() ;
				String result = "" ;
				if ( responseCode == 200 ) {
					inputStream = conn.getInputStream();					
					result = readFully(inputStream) ;
				}				
				

				if ( listener != null ) {
					listener.receiveServerResponse(responseCode, result) ;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally {
				// Makes sure that the InputStream is closed after the app is
				// finished using it.
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					inputStream = null ;
				} 
				if( conn != null ) {
					conn.disconnect() ;
					conn = null ;
				}
			}
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
	}
