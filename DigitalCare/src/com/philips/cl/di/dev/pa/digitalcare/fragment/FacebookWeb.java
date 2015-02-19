package com.philips.cl.di.dev.pa.digitalcare.fragment;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cl.di.dev.pa.digitalcare.R;

public class FacebookWeb extends DigitalCareBaseFragment {

	private View view = null;
	private LinearLayout mOptionParent = null;
	private FrameLayout.LayoutParams mParams = null;
	private WebView mWebview = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_socialweb_facebook,
				container, false);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Resources resource = getActivity().getResources();

		Configuration config = resource.getConfiguration();
		mOptionParent = (LinearLayout) getActivity().findViewById(
				R.id.fbwebContainer);
		mParams = (FrameLayout.LayoutParams) mOptionParent.getLayoutParams();
		mWebview = (WebView) view.findViewById(R.id.webview);
		mWebview.loadUrl("https://m.facebook.com/PhilipsIndia?");
		setViewParams(config);
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);

		setViewParams(config);
	}

	private void setViewParams(Configuration config) {
		if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {

			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
		} else {
			mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
		}
		mOptionParent.setLayoutParams(mParams);
	}
}
