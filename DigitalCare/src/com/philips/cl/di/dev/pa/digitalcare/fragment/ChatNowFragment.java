package com.philips.cl.di.dev.pa.digitalcare.fragment;

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
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.digitalcare.R;

/*
 *	ChatNowFragment will help to inflate chat webpage on the screen.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Jan 2015
 */
@SuppressLint("SetJavaScriptEnabled")
public class ChatNowFragment extends BaseFragment {
	private WebView mWebView = null;
	private String mUrl = null;
	private ProgressDialog mDialog = null;
	private LinearLayout mLinearLayout = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.common_webview, container, false);
		mUrl = getActivity().getResources().getString(R.string.url_chat);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Resources resource = getActivity().getResources();
		mLinearLayout = (LinearLayout) getActivity().findViewById(
				R.id.webViewParent);
		mLinearLayout.setOnClickListener(clickListner);
		mDialog = ProgressDialog.show(getActivity(),
				resource.getString(R.string.loading),
				resource.getString(R.string.please_wait), true);
		mDialog.setCancelable(true);

		mWebView = (WebView) getActivity().findViewById(R.id.webView);
		mWebView.loadUrl(mUrl);
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
	}

	private View.OnClickListener clickListner = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.webViewParent) {
				mDialog.dismiss();
			}
		}
	};

	private class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			mDialog.show();
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			mDialog.dismiss();
			super.onPageFinished(view, url);
		}
	}
}
