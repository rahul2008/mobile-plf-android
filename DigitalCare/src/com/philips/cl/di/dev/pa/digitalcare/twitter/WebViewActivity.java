package com.philips.cl.di.dev.pa.digitalcare.twitter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.philips.cl.di.dev.pa.digitalcare.R;

public class WebViewActivity extends Activity {

	private WebView webView;

	public final static String EXTRA_URL = "extra_url";
	private ProgressDialog mDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_webview);

		setTitle("Login");

		final String url = this.getIntent().getStringExtra(EXTRA_URL);
		if (null == url) {
			Log.e("Twitter", "URL cannot be null");
			finish();
		}

		webView = (WebView) findViewById(R.id.webView);
		webView.setWebViewClient(new MyWebViewClient());
		webView.loadUrl(url);
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {

			try {
				if (mDialog.isShowing()) {
					mDialog.dismiss();
					mDialog = null;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			if (mDialog == null) {
				mDialog = new ProgressDialog(WebViewActivity.this);
				mDialog.setMessage("Loading...");
				mDialog.show();
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (url.contains(getResources()
					.getString(R.string.twitter_callback))) {
				Uri uri = Uri.parse(url);

				/* Sending results back */
				String verifier = uri
						.getQueryParameter(getString(R.string.twitter_oauth_verifier));
				Intent resultIntent = new Intent();
				resultIntent.putExtra(
						getString(R.string.twitter_oauth_verifier), verifier);
				setResult(RESULT_OK, resultIntent);

				/* closing webview */
				finish();
				return true;
			}
			return false;
		}
	}

}
