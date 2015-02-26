package com.philips.cl.di.dev.digitalcare.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.AsyncTask;

import com.philips.cl.di.dev.digitalcare.listners.LongRunningTaskInterface;

/*
 * LongRunningTask is AsyncTask. This is generic class. Other classes just need to pass 
 * the listener context and URL in order to run this Async task.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Dec 2015
 */
public class LongRunningTask extends AsyncTask<Void, Void, String> {
	private LongRunningTaskInterface mInterfaceContext = null;
	private String mURL = null;

	public LongRunningTask(String url, LongRunningTaskInterface context) {
		mURL = url;
		mInterfaceContext = context;
	}

	private String getASCIIContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		InputStream in = entity.getContent();

		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n > 0) {
			byte[] b = new byte[4096];
			n = in.read(b);

			if (n > 0)
				out.append(new String(b, 0, n, "UTF-8"));
		}

		return out.toString().trim();
	}

	@Override
	protected String doInBackground(Void... params) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(mURL);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);

			HttpEntity entity = response.getEntity();

			text = getASCIIContentFromEntity(entity);

		} catch (Exception e) {
			return e.getLocalizedMessage();
		}
		return text;
	}

	protected void onPostExecute(String results) {
		mInterfaceContext.responseReceived(results);
	}
}
