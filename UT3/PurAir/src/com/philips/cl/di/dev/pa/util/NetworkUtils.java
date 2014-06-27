package com.philips.cl.di.dev.pa.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.philips.cl.di.dev.pa.datamodel.ResponseDto;

public class NetworkUtils {

	/**
	 * Given a URL, establishes an HttpUrlConnection and retrieves the web page content as an InputStream,
	 *  which it returns as a string.  
	 *
	 * @param  stringUrl	The given URL 	
	 * @param  int          ConnectTimeout in milliseconds, else -1
	 * @param  int          readTimeout in milliseconds, else -1
	 * @return	Returns 	ResponseDto object containing ResponseCode and ResponseString 
	 */
	public static ResponseDto downloadUrl(String stringUrl, int connectTimeOut, int readTimeOut)   {
		if (stringUrl == null || stringUrl.isEmpty()) return null;
		InputStream inputStream = null;
		HttpURLConnection conn = null ;
		String data = null ;
		try {
			URL url ;
			url = new URL(stringUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("content-type", "application/json") ;
			if(connectTimeOut!= -1)
				conn.setConnectTimeout(connectTimeOut);
			if(readTimeOut!=-1)
				conn.setReadTimeout(readTimeOut);
			// Starts the query
			conn.connect();
			int responseCode = conn.getResponseCode();
			
			inputStream = conn.getInputStream();

			// Convert the InputStream into a string
			data = readFully(inputStream);			
			return new ResponseDto(responseCode, data);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					inputStream = null ;
				} catch (IOException e) {
					e.printStackTrace();
				}				
			} 
			if( conn != null ) {
				conn.disconnect() ;
				conn = null ;
			}
		}
		return null;
	}
	
	/**
	 * this in-turn calls downloadUrl with the given url and 
	 * setting the other two parameters as default
	 * @param stringUrl: the data to be downloaded from
	 * @return ResponseDto object containing ResponseCode and ResponseString
	 */
	public static ResponseDto downloadUrl(String stringUrl)
	{
		return downloadUrl(stringUrl, -1, -1);
	}
	
	
	/**
	 * this in-turn calls downloadUrl with the given url and connectTimeOut, and
	 * setting readTimeOut as default
	 * @param stringUrl: the data to be downloaded from
	 * @param  int ConnectTimeout in milliseconds, else -1
	 * @return ResponseDto object containing ResponseCode and ResponseString
	 */
	public static ResponseDto downloadUrl(String stringUrl, int connectTimeOut)
	{
		return downloadUrl(stringUrl, connectTimeOut, -1);
	}

	/**
	 * Reads an InputStream and converts it to a String.  
	 *
	 * @param  inputStream	Input stream to convert to string 	
	 * @return	Returns 	converted string
	 */
	// 
	public static String readFully(InputStream inputStream) throws IOException, UnsupportedEncodingException {
		if (inputStream == null) return "";
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
	
	/**
	 * return network connection status
	 */
	public static boolean isNetworkConnected(Context context)
	{
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connManager.getActiveNetworkInfo();
		if(netInfo!=null && netInfo.isConnected())
		{
			return true;
		}
		else
		{
			return false;
		}
		
	}

}
