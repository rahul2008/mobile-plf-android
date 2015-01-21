package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.digitalcare.R;

/*
 *	LocateNearYouFragment will help to inflate webpage on the screen.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Jan 2015
 */
@SuppressLint("SetJavaScriptEnabled")
public class LocateNearYouFragment extends BaseFragment {
	private WebView mWebView = null;
	private static final String mUrl = "http://www.philips.co.in/c/retail-store-locator/page/";
	private ProgressDialog mDialog = null;
	private LinearLayout mLinearLayout = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_locate_near_you,
				container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Resources resource = getActivity().getResources();
		mLinearLayout = (LinearLayout) getActivity().findViewById(
				R.id.webViewParent);
		mLinearLayout.setOnTouchListener(clickListner);
		mDialog = ProgressDialog.show(getActivity(), resource.getString(R.string.loading),
				resource.getString(R.string.please_wait), true);
		mDialog.setCancelable(true);

		mWebView = (WebView) getActivity().findViewById(R.id.webView);
		mWebView.loadUrl(mUrl);
		mWebView.setWebViewClient(new MyWebViewClient());
		WebSettings websettings = mWebView.getSettings();
		websettings.setJavaScriptEnabled(true);
		websettings.setLoadWithOverviewMode(true);
		// websettings.setUseWideViewPort(true);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	private View.OnTouchListener clickListner = new View.OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (v.getId()) {
			case R.id.webViewParent:
				mDialog.dismiss();
				break;
			}
			return false;
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
