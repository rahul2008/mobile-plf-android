package com.philips.cl.di.dev.pa.buyonline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.philips.cl.di.dev.pa.R;
import com.philips.cl.di.dev.pa.activity.BaseActivity;
import com.philips.cl.di.dev.pa.view.FontTextView;


public class WebActivity extends BaseActivity {

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_activity);
		initPage();
		
//		ViewGroup container = (LinearLayout) findViewById(R.id.shadowLL);
//		setBackground(container, R.drawable.ews_nav_bar_2x, Color.BLACK, .1F);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getString("url") != null) {
			webView.loadUrl(bundle.getString("url"));
		}else{
			finish();
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initPage() {
		webView = (WebView)findViewById(R.id.web_wv);
		webView.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setWebChromeClient(new WebChromeClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(false);
		webSettings.setUseWideViewPort(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.endsWith(".apk") || url.endsWith(".APK") || !url.startsWith("http://")) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					Uri content_url = Uri.parse(url.trim());
					intent.setData(content_url);
					startActivity(intent);
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

		});

		webView.setWebChromeClient(new WebChromeClient(){
			@Override  
			public void onReceivedTitle(WebView view, String title) {  
				super.onReceivedTitle(view, title);  
				((FontTextView)findViewById(R.id.title_text_tv)).setText(title);
			}  
		});
		
		
		findViewById(R.id.web_next_iv).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != webView && webView.canGoForward()) {
					webView.goForward();
				}
			}
		});
		findViewById(R.id.web_pre_iv).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (webView != null && webView.canGoBack()) {
					webView.goBack();
				} 
			}
		});
		findViewById(R.id.title_right_btn).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					if (null != webView) {
						webView.reload();
					}
			}
		});
		findViewById(R.id.title_left_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
