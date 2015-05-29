package com.philips.cl.di.digitalcare.contactus;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.os.Looper;

import com.philips.cl.di.digitalcare.util.DLog;

public class RequestData extends Thread {

	private final String TAG = RequestData.class.getSimpleName();

	private ResponseCallback mResponseCallback = null;
	private String mResponse = null;
	private String mUrl = null;

	public RequestData(String url, ResponseCallback responseCallback) {
		mUrl = url;
		mResponseCallback = responseCallback;
		setPriority(Thread.MAX_PRIORITY);
	}

	@Override
	public void run() {
		Looper.prepare();
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(mUrl);
		try {
			HttpResponse response = httpClient.execute(httpGet, localContext);
			HttpEntity entity = response.getEntity();
			mResponse = getASCIIContentFromEntity(entity);
		} catch (Exception e) {
			DLog.e(TAG,
					"Failed to fetch Response Data : " + e.getLocalizedMessage());
		} finally {
			notifyResponseHandler();
		}
		Looper.loop();
	}
	
	protected void notifyResponseHandler(){
		if (mResponse != null){
			mResponseCallback.onResponseReceived(mResponse);
		}
	}

	protected String getASCIIContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {

		DLog.d(TAG, "Getting String response from HTTPENTITY");
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
}
