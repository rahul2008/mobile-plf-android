/**
 * Utils class contains common utility methods required across framework under
 * different scenario's.
 *
 * @author naveen@philips.com
 *
 * @since Feb 10, 2015
 *
 *  Copyright (c) 2016 Philips. All rights reserved.
 *
 */
package com.philips.cdp.digitalcare.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.telephony.TelephonyManager;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class Utils {


    public static String loadWebPageContent(final String webpageUrl, final WebView webView, final ProgressBar progressBar) {
        webView.loadUrl(webpageUrl);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

        });

        return webView.getUrl();
    }

	public boolean isSimAvailable(final Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int SIM_STATE = telephonyManager.getSimState();

            switch (SIM_STATE) {
                case TelephonyManager.SIM_STATE_ABSENT: //SimState = "No Sim Found!";
                case TelephonyManager.SIM_STATE_NETWORK_LOCKED: //SimState = "Network Locked!";
                case TelephonyManager.SIM_STATE_PIN_REQUIRED: //SimState = "PIN Required to access SIM!";
                case TelephonyManager.SIM_STATE_PUK_REQUIRED: //SimState = "PUK Required to access SIM!"; // Personal Unblocking Code
                case TelephonyManager.SIM_STATE_UNKNOWN: //SimState = "Unknown SIM State!";
                    return false;
                default:
                    return true;
            }
    }
}
