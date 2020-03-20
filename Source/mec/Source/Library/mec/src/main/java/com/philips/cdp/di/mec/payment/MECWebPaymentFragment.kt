package com.philips.cdp.di.mec.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.screens.MecBaseFragment

class MECWebPaymentFragment :MecBaseFragment() {


    override fun getFragmentTag(): String {
        return "MECWebPaymentFragment"
    }

    companion object {
        val TAG:String="MECWebPaymentFragment"
    }

    protected lateinit var mWebView: WebView
    private var mUrl: String? = null
    private var mProgressBar : FrameLayout? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val group = inflater.inflate(R.layout.mec_web_fragment, container, false) as ViewGroup
        mProgressBar = group.findViewById(R.id.mec_progress_bar_container) as FrameLayout
        showProgressBar(mProgressBar)
        mWebView = group.findViewById<View>(R.id.mec_webView) as WebView
       // mWebView.webViewClient= IAPWebViewClient()
        mWebView.settings.javaScriptEnabled = true
        mWebView.settings.saveFormData = false
        mWebView.settings.javaScriptCanOpenWindowsAutomatically = true
        mWebView.settings.setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 9_3 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13E233 Safari/601.1")
        mWebView.settings.domStorageEnabled = true
        mWebView.settings.setAppCacheEnabled(true)
        mWebView.settings.loadsImagesAutomatically = true
        mWebView.settings.useWideViewPort = true
        return group
    }
}