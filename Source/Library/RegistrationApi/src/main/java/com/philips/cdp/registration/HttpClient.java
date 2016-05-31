
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration;

import android.support.v4.util.Pair;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class HttpClient {

	final int TIME_OUT = 30000;

	private String ACCESS_TOKEN_HEADER = "x-accessToken";

	private String CONTENT_TYPE_HEADER = "Content-Type";

	private String REQUEST_METHOD_POST = "POST";

	private String REQUEST_METHOD_GET = "GET";

	private String CONTENT_TYPE = "application/x-www-form-urlencoded";

	private String LOG_TAG = "HttpClient";

	public String callPost(String urlString, List<Pair<String, String>> nameValuePairs, String accessToken){
		URL url = null;
		OutputStream outputStream = null;
		BufferedWriter bufferedWriter = null;
		BufferedReader bufferedReader = null;
		StringBuilder inputResponse = new StringBuilder();
		try {
			url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);
			connection.setRequestProperty(CONTENT_TYPE_HEADER, CONTENT_TYPE);
			connection.setRequestMethod(REQUEST_METHOD_POST);
			javax.net.ssl.SSLSocketFactory sf = createSslSocketFactory();
			connection.setSSLSocketFactory(sf);
			connection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			outputStream = connection.getOutputStream();
			DataOutputStream wr = new DataOutputStream(outputStream);
			wr.writeBytes(getPostString(nameValuePairs));
			wr.flush();
			wr.close();
			int responseCode = connection.getResponseCode();
			Log.i(LOG_TAG, "HTTPsURLConnection  response code: " + responseCode);
			inputResponse = new StringBuilder();
			bufferedReader = getBufferedReader(inputResponse, responseCode, new InputStreamReader(connection.getInputStream()), new InputStreamReader(connection.getErrorStream()));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error in POST call " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			Log.e(LOG_TAG, "Error in POST call " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if(bufferedWriter != null)
					bufferedWriter.close();
				if(bufferedReader != null)
					bufferedReader.close();
			} catch (IOException e) {
				Log.e(LOG_TAG, "Error in POST call " + e.getMessage());
				e.printStackTrace();
			}

		}
		return inputResponse.toString();

	}

	private BufferedReader getBufferedReader( StringBuilder inputResponse, int responseCode, InputStreamReader in, InputStreamReader in2) throws IOException {
		BufferedReader bufferedReader  = null;
		String input;
		if (responseCode == HttpsURLConnection.HTTP_OK) {
			bufferedReader = new BufferedReader(in);
			while ((input = bufferedReader.readLine()) != null) {
				inputResponse.append(input);
			}
		} else {
			bufferedReader = new BufferedReader(in2);
			while ((input = bufferedReader.readLine()) != null) {
				inputResponse.append(input);
			}
		}
		return bufferedReader;
	}

	private String getPostString(List<Pair<String, String>> nameValuePairs){
		StringBuilder postString = new StringBuilder();
		boolean firstItem = true;

		for (Pair pair : nameValuePairs)
		{
			if (firstItem)
				firstItem = false;
			else {
				postString.append("&");
			}
			String s = (String)pair.first;
			try {
				postString.append(URLEncoder.encode((String) pair.first, "UTF-8"));
				postString.append("=");
				postString.append(URLEncoder.encode((String) pair.second, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return postString.toString();
	}


	public String callGet(String urlString, String accessToken){
		URL url = null;
		BufferedReader bufferedReader = null;
		StringBuilder inputResponse =  new StringBuilder();
		try{
			url = new URL(urlString);
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			connection.setRequestProperty(ACCESS_TOKEN_HEADER, accessToken);
			connection.setRequestProperty(CONTENT_TYPE_HEADER, CONTENT_TYPE);
			connection.setRequestMethod(REQUEST_METHOD_GET);
			javax.net.ssl.SSLSocketFactory sf = createSslSocketFactory();
			connection.setSSLSocketFactory(sf);
			connection.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});


			int responseCode = connection.getResponseCode();
			Log.i(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection response code" + responseCode);
			bufferedReader = getBufferedReader(inputResponse, responseCode, new InputStreamReader(connection.getInputStream()), new InputStreamReader(connection.getErrorStream()));


		}catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection" + e.getMessage());
			e.printStackTrace();
		} catch(Exception e){

		}finally {
			try {
				if(bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e) {
				Log.e(LOG_TAG, "Returninge of doInBackground :HTTPURLConnection" + e.getMessage());
				e.printStackTrace();
			}

		}

		return inputResponse.toString();

	}

	private  javax.net.ssl.SSLSocketFactory createSslSocketFactory() throws Exception {
		TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) {
			}
		} };
		SSLContext sslContext = SSLContext.getInstance("TLS");

		TrustManager tm = new X509TrustManager() {

			public void checkClientTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType)
					throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		sslContext.init(new KeyManager[0], new TrustManager[] { tm }, new SecureRandom());
		return sslContext.getSocketFactory();
	}


}
