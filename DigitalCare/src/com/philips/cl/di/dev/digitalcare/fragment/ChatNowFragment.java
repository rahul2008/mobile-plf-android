package com.philips.cl.di.dev.digitalcare.fragment;

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

import com.philips.cl.di.dev.digitalcare.R;
import com.philips.cl.di.dev.digitalcare.bean.CdlsBean;
import com.philips.cl.di.dev.digitalcare.util.ParserController;

/*
 *	ChatNowFragment will help to inflate chat webpage on the screen.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Jan 2015
 */
@SuppressLint("SetJavaScriptEnabled")
public class ChatNowFragment extends DigitalCareBaseFragment {
	private WebView mWebView = null;
	private String mUrl = null;
	private ProgressDialog mDialog = null;
	private LinearLayout mLinearLayout = null;
	private FrameLayout.LayoutParams mParams = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.common_webview, container, false);
		ParserController parserController = ParserController
				.getParserControllInstance(getActivity());
		CdlsBean cdlsBean = parserController.getCdlsBean();
		// mUrl = cdlsBean.getChat().getContent();

		mUrl = "http://ph-india.livecom.net/5g/ch/?___________________________________________________________=&aid=WuF95jlNIAA%3D&gid=3&skill=undefined&tag=PHILIPS_GEN_GR&cat=&chan=LWC;LVC;LVI&fields=&customattr=Group%3APHILIPS_GEN_GR%3B%20Category%3A%3B%20Sub-category%3A%3B%20CTN%3A%3B%20Country%3AIN%3B%20Language%3AEN&sID=1mOYTHel%2BAI%3D&cID=uENOfpmJKAA%3D&lcId=SMS_IN_EN&url=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Ffragments%2Fchat_now_fragment.jsp%3FparentId%3DPB_IN_1%26userCountry%3Din%26userLanguage%3Den&ref=http%3A%2F%2Fwww.support.philips.com%2Fsupport%2Fcontact%2Fcontact_page.jsp%3FuserLanguage%3Den%26userCountry%3Din";
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Resources resource = getActivity().getResources();
		mLinearLayout = (LinearLayout) getActivity().findViewById(
				R.id.webViewParent);
		mParams = (FrameLayout.LayoutParams) mLinearLayout.getLayoutParams();
		mLinearLayout.setOnClickListener(clickListner);
		mDialog = ProgressDialog.show(getActivity(),
				resource.getString(R.string.loading),
				resource.getString(R.string.please_wait), true);
		mDialog.setCancelable(true);

		mWebView = (WebView) getActivity().findViewById(R.id.webView);
		Configuration config = getResources().getConfiguration();
//		setViewParams(config);
		// Log.i("testing", "url : " + mUrl);
		// mUrl = TextUtils.htmlEncode(mUrl);
		// String url = "<html><body>"+mUrl+"</body></html>";
		// mWebView.loadData(Html.fromHtml(url).toString(), "text/html",
		// "UTF-8");
		mWebView.loadUrl(mUrl);
		mWebView.setWebViewClient(new MyWebViewClient());
		WebSettings websettings = mWebView.getSettings();
		websettings.setJavaScriptEnabled(true);
		websettings.setLoadWithOverviewMode(true);
		websettings.setUseWideViewPort(true);
		websettings.setBuiltInZoomControls(true);
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

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		setViewParams(config);
	}

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
			// view.loadUrl(url);
			mDialog.show();
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			mDialog.dismiss();
			super.onPageFinished(view, url);
		}
	}
}
