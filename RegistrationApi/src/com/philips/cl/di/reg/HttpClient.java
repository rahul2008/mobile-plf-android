
package com.philips.cl.di.reg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class HttpClient {

	final int TIME_OUT = 30000;

	private String ACCESS_TOKEN_HEADER = "x-accessToken";

	private String CONTENT_TYPE_HEADER = "Content-Type";

	private String CONTENT_TYPE = "application/x-www-form-urlencoded";

	private String LOG_TAG = "HttpClient";

	// ----- Post Method
	public String postData(String url, List<NameValuePair> nameValuePairs, String accessToken) {

		DefaultHttpClient httpClient = new DefaultHttpClient();
		try {
			HttpPost httppost = new HttpPost(url);
			httppost.setHeader(ACCESS_TOKEN_HEADER, accessToken);
			httppost.setHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE);
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = httpClient.execute(httppost);
			InputStream inputStream = httpResponse.getEntity().getContent();

			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}
			Log.i(LOG_TAG, "Returninge of doInBackground :" + stringBuilder.toString());
			return stringBuilder.toString();
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return null;
	}

	// ---- Get Method
	public String connectWithHttpGet(String url, String accessToken) {

		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader(ACCESS_TOKEN_HEADER, accessToken);

		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = httpResponse.getEntity().getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			StringBuilder stringBuilder = new StringBuilder();
			String bufferedStrChunk = null;

			while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
				stringBuilder.append(bufferedStrChunk);
			}
			Log.i(LOG_TAG, "Returninge of doInBackground :" + stringBuilder.toString());
			return stringBuilder.toString();

		} catch (ClientProtocolException cpe) {
			Log.e(LOG_TAG, "Exception related to httpResponse :" + cpe);
		} catch (IOException ioe) {
			Log.e(LOG_TAG, "IOException related to httpResponse :" + ioe);
		}

		return null;
	}

	private HttpParams getHttpParams(int timeout) {
		HttpParams httpparams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpparams, 10000);
		HttpConnectionParams.setSoTimeout(httpparams, timeout);
		return httpparams;
	}

	public String request(HttpResponse response) {
		String result = null;
		if (response.getStatusLine().getStatusCode() == 200) {
			InputStream in = null;
			BufferedReader reader = null;
			try {
				in = response.getEntity().getContent();
				reader = new BufferedReader(new InputStreamReader(in));
				StringBuilder str = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					str.append(line + "\n");
					line = null;
				}
				result = str.toString();
			} catch (Exception ex) {
				result = null;
			} finally {
				if (in != null)
					try {
						in.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				in = null;
				if (reader != null)
					try {
						reader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				reader = null;
			}
		}
		return result;
	}
}
