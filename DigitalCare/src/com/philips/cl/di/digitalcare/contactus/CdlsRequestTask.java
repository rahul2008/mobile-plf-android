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

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

/**
 * CdlsRequestAsyncTask is AsyncTask. This is generic class. Other classes just need to pass 
 * the listener context and URL in order to run this Async task.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since: 16 Dec 2014
 */
public class CdlsRequestTask extends AsyncTask<Void, Void, String> {
	private CdlsResponseCallback mCdlsResponseHandler = null;
	private String mURL = null;
	private Activity mActivity = null;
	
	private ProgressDialog mDialog = null;

	public CdlsRequestTask(Activity activity, String url, CdlsResponseCallback context) {
		mActivity = activity;
		mURL = url;
		mCdlsResponseHandler = context;
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (mDialog == null)
			mDialog = new ProgressDialog(mActivity);
		mDialog.setMessage("Loading...");	
		if (!(mActivity.isFinishing())) {
			mDialog.show();
		} else {
			mDialog.cancel();
			mDialog = null;
		}
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
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog.cancel();
			mDialog = null;
		}
		mCdlsResponseHandler.onCdlsResponseReceived(results);
	}
}
