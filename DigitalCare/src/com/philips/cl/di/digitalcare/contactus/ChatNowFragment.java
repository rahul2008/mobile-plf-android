package com.philips.cl.di.digitalcare.contactus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cl.di.digitalcare.DigitalCareBaseFragment;
import com.philips.cl.di.digitalcare.R;
import com.philips.cl.di.digitalcare.analytics.AnalyticsConstants;
import com.philips.cl.di.digitalcare.analytics.AnalyticsTracker;
import com.philips.cl.di.digitalcare.util.DigitalCareContants;

/**
 * ChatNowFragment will help to inflate chat webpage on the screen.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 16 Jan 2015
 */
@SuppressLint("SetJavaScriptEnabled")
public class ChatNowFragment extends DigitalCareBaseFragment {
	private WebView mWebView = null;
	private String mUrl = null;
	private ProgressDialog mDialog = null;
	private RelativeLayout mLinearLayout = null;
	private FrameLayout.LayoutParams mParams = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.common_webview, container, false);
		// CdlsResponseParser cdlsResponseParserHelper = CdlsResponseParser
		// .getParserControllInstance(getActivity());
		// /* CdlsBean cdlsBean = */cdlsResponseParserHelper.getCdlsBean();
		// mUrl = cdlsBean.getChat().getContent();

		setChatEndPoint(DigitalCareContants.CHAT_LINK);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_EXIT_LINK,
				AnalyticsConstants.ACTION_KEY_EXIT_LINK, getChatEndPoint());

		Resources resource = getActivity().getResources();
		mLinearLayout = (RelativeLayout) getActivity().findViewById(
				R.id.webViewParent);
		mParams = (FrameLayout.LayoutParams) mLinearLayout.getLayoutParams();
		mLinearLayout.setOnClickListener(this);
		mDialog = ProgressDialog.show(getActivity(),
				resource.getString(R.string.loading),
				resource.getString(R.string.please_wait), true);
		mDialog.setCancelable(true);

		mWebView = (WebView) getActivity().findViewById(R.id.webView);
		/* Configuration config = */getResources().getConfiguration();
		// setViewParams(config);
		// mUrl = TextUtils.htmlEncode(mUrl);
		// String url = "<html><body>"+mUrl+"</body></html>";
		// mWebView.loadData(Html.fromHtml(url).toString(), "text/html",
		// "UTF-8");
		if (getChatEndPoint() != null)
			setupWebView();
	}

	protected void setupWebView() {
		mWebView.loadUrl(getChatEndPoint());
		mWebView.setWebViewClient(new MyWebViewClient());
		WebSettings websettings = mWebView.getSettings();
		websettings.setJavaScriptEnabled(true);
		websettings.setLoadWithOverviewMode(true);
		websettings.setUseWideViewPort(true);
		websettings.setBuiltInZoomControls(true);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

	@Override
	public void setViewParams(Configuration config) {

		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mLinearLayout.setLayoutParams(mParams);
	}

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			showDialog();
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			dismissDialog();
			super.onPageFinished(view, url);
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.webViewParent) {
			dismissDialog();
		}
	}

	protected void dismissDialog() {
		mDialog.dismiss();
	}

	protected void showDialog() {
		mDialog.show();
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.chat_with_philips);
	}

	protected void setChatEndPoint(final String url) {

		if (url.startsWith("http://") || url.startsWith("https://"))
			mUrl = url;
	}

	protected String getChatEndPoint() {
		return mUrl;
	}

	@Override
	public String setPreviousPageName() {
		return AnalyticsConstants.PAGE_CONTACTUS_CHATNOW;
	}
}
