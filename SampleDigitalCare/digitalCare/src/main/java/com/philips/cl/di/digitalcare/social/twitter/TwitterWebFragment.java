package com.philips.cl.di.digitalcare.social.twitter;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;

public class TwitterWebFragment extends DigitalCareBaseFragment {

	private View mView = null;
	private WebView mWebView = null;
	// private ProgressDialog mProgressDialog = null;
	private ProgressBar mProgressBar = null;
	private final String TAG = TwitterWebFragment.class.getSimpleName();
	private String TWITTTERURL = "https://twitter.com/intent/tweet?source=webclient&text=";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		try {
			if (mView != null) {
				((ViewGroup) mView.getParent()).removeView(mView);
			}
			mView = inflater.inflate(R.layout.common_webview, container, false);

		} catch (InflateException e) {
		}
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initView();
		loadInAppFacebook();
		AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACTUS_TWITTER,
				getPreviousName());
	}

	private void loadInAppFacebook() {
		mWebView.loadUrl(getTWITTTERURL());
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				mProgressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mProgressBar.setVisibility(View.GONE);
			}

		});
	}

	private void initView() {
		mWebView = (WebView) mView.findViewById(R.id.webView);
		mProgressBar = (ProgressBar) mView
				.findViewById(R.id.common_webview_progress);
		mProgressBar.setVisibility(View.GONE);
	}

	private String getTWITTTERURL() {
		return TWITTTERURL
				+ getProductInformation();
	}

	protected String getProductInformation() {
		return "@"+ getActivity().getString(R.string.twitter_page)+ " " + getActivity().getResources().getString(
				R.string.support_productinformation)
				+ " "
				+ DigitalCareConfigManager.getInstance()
				.getConsumerProductInfo().getProductTitle()
				+ " "
				+ DigitalCareConfigManager.getInstance()
				.getConsumerProductInfo().getCtn();
	}


	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.contact_us);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void setViewParams(Configuration config) {
	}

	@Override
	public String setPreviousPageName() {
		return AnalyticsConstants.PAGE_CONTACTUS_TWITTER;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mWebView != null) {
			mWebView = null;
		}
	}

}