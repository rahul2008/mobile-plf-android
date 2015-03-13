package com.philips.cl.di.digitalcare.social.twitter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.philips.cl.di.digitalcare.R;

/**
 * @Description Activity component used for Twitter authentication
 * @author naveen@philips.com
 * @since 11/Feb/2015
 */

public class TwitterWebViewActivity extends Activity {
	private WebView mWebView = null;
	public final static String EXTRA_URL = "extra_url";
	private ProgressDialog mDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(mgetView());
		final String url = this.getIntent().getStringExtra(EXTRA_URL);
		if (null == url) {
			Log.e("Twitter", "URL cannot be null");
			finish();
		}
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.loadUrl(url);
	}

	private View mgetView() {
		RelativeLayout mWebContainer = new RelativeLayout(this);
		mWebContainer.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		mWebView = new WebView(this);
		mWebView.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		mWebContainer.addView(mWebView);
		return mWebContainer;

	}

	@Override
	protected void onStop() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog.cancel();
			mDialog = null;
		}
		super.onStop();
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {

			try {
				if (mDialog != null && mDialog.isShowing()) {
					mDialog.dismiss();
					mDialog = null;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}

		@Override
		public void onLoadResource(WebView view, String url) {
			if (mDialog == null)
				mDialog = new ProgressDialog(TwitterWebViewActivity.this);
			mDialog.setMessage("Loading...");
			mDialog.show();
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
