package com.philips.cl.di.digitalcare.faq;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.philips.cl.di.digitalcare.ConsumerProductInfo;
import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.DigitalCareConfigManager;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;

public class FaqFragment extends DigitalCareBaseFragment {

	private View mView = null;
	private WebView mWebView = null;
	private ProgressDialog mProgressDialog = null;

	private String FAQ_URL = "http://www.philips.com/content/%s/%s_%s/standalone-faqs/%s.html";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (mView == null) {
			mView = inflater.inflate(R.layout.common_webview, container, false);
		}
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initView();

		loadFaq();
	}

	private void loadFaq() {
		mWebView.loadUrl(getFaqUrl());
		
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
				startProgressDialog();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				closeProgressDialog();
			}

		});
	}

	private void initView() {
		mWebView = (WebView) mView.findViewById(R.id.webView);
	}

	private String getFaqUrl() {
		String language = DigitalCareConfigManager
				.getInstance().getLocale()
				.getLanguage().toLowerCase();

		String country = DigitalCareConfigManager
				.getInstance().getLocale()
				.getCountry().toUpperCase();

		ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
				.getInstance()
				.getConsumerProductInfo();
		return String.format(FAQ_URL, consumerProductInfo.getSector(), language, country, consumerProductInfo.getCtn());
	}

	protected void closeProgressDialog() {

		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
	}

	protected void startProgressDialog() {
		if (mProgressDialog == null)
			mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setMessage(getActivity().getResources().getString(
				R.string.loading));
		mProgressDialog.setCancelable(false);
		if (!(getActivity().isFinishing())) {
			mProgressDialog.show();
		}
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.view_faq);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void setViewParams(Configuration config) {
	}

	@Override
	public String setPreviousPageName() {
		return AnalyticsConstants.PAGE_FAQ;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mWebView != null) {
			mWebView = null;
		}
	}

}
