package com.philips.cl.di.dev.pa.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.content.Context;

import com.philips.cl.di.dev.pa.dto.OutdoorAQIEventDto;
import com.philips.cl.di.dev.pa.interfaces.OutdoorAQIListener;
import com.philips.cl.di.dev.pa.screens.adapters.DatabaseAdapter;
import com.philips.cl.di.dev.pa.utils.DataParser;

public class TaskGetHttp extends Thread {
	
	private String url ;
	private int cityID ;
	
	private Context context ;
	
	private OutdoorAQIListener outdoorAQIListener ;
	
	public TaskGetHttp(String url, int cityID,Context context,OutdoorAQIListener outdoorAQIListener) {
		this.url = url ;
		this.cityID = cityID ;
		this.context = context ;
		this.outdoorAQIListener = outdoorAQIListener ;
	}
	
	@Override
	public void run() {
		try {
			String result = downloadUrl(url) ;
			if (result != null ) {
				DataParser dataParser = new DataParser(result) ;
				List<OutdoorAQIEventDto> outdoorAQIList = dataParser.parseOutdoorAQIData() ;
				
				DatabaseAdapter dbAdapter = new DatabaseAdapter(context) ;
				dbAdapter.open() ;
				dbAdapter.insertOutdoorAQI(outdoorAQIList, cityID) ;
				
				dbAdapter.close() ;
				
				outdoorAQIListener.updateOutdoorAQI() ;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves the web page content as an InputStream,
	 *  which it returns as a string.  
	 *
	 * @param  stringUrl	The given URL 	
	 * @return	Returns 	web page as a string 
	 */
	private String downloadUrl(String stringUrl) throws IOException {
		InputStream inputStream = null;
		HttpURLConnection conn = null ;
		try {
			URL url = new URL(stringUrl);
			
//			Log.d(getClass().getSimpleName(), "Start download from URL : " + url);

			conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(100000 /* milliseconds */);
			conn.setConnectTimeout(150000 /* milliseconds */);
			conn.setRequestMethod("GET");
			

			// Starts the query
			conn.connect();
//			Log.d(getClass().getSimpleName(), "received response [" + response + "]");
			inputStream = conn.getInputStream();

			// Convert the InputStream into a string
			String contentAsString = readFully(inputStream);
			return contentAsString;
		}
		catch(Exception e) {
			return null;
		}
		finally {
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
			if (inputStream != null) {
				inputStream.close();
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
