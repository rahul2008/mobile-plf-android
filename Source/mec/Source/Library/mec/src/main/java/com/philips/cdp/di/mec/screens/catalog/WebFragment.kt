package com.philips.cdp.di.mec.screens.catalog

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.RelativeLayout

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.screens.MecBaseFragment


abstract class WebFragment : MecBaseFragment() {

    val TAG = WebFragment::class.java.name
     lateinit var mWebView: WebView
    private var mUrl: String? = null
    private var mParentContainer: RelativeLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewGroup = inflater.inflate(R.layout.mec_web_fragment, container, false) as ViewGroup
        mParentContainer = viewGroup.findViewById<View>(R.id.mec_web_container) as RelativeLayout
        mWebView = viewGroup.findViewById<View>(R.id.mec_webView) as WebView
        mWebView.webViewClient = IAPWebViewClient()
        mWebView.settings.javaScriptEnabled = isJavaScriptEnable()
        mWebView.settings.saveFormData = false
        createCustomProgressBar(mParentContainer, MEDIUM)

        mUrl = getWebUrl()
        return viewGroup
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mWebView.loadUrl(mUrl)
    }

    override fun onResume() {
        super.onResume()
        //setCartIconVisibility(false)
        mWebView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mWebView.onPause()
    }

    protected abstract fun isJavaScriptEnable(): Boolean

    protected abstract fun getWebUrl(): String?

    protected fun shouldOverrideUrlLoading(url: String): Boolean {
        if (url == null) return false

        try {
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return true
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
                return true
            }
        } catch (e: Exception) {
            // Avoid crash due to not installed app which can handle the specific url scheme
            return false
        }

    }

    private fun shouldHandleError(errorCode: Int): Boolean {
        return (errorCode == WebViewClient.ERROR_CONNECT
                || errorCode == WebViewClient.ERROR_BAD_URL
                || errorCode == WebViewClient.ERROR_TIMEOUT
                || errorCode == WebViewClient.ERROR_HOST_LOOKUP)
    }

    private inner class IAPWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            return this@WebFragment.shouldOverrideUrlLoading(url)
        }

        @TargetApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return this@WebFragment.shouldOverrideUrlLoading(request.url.toString())
        }

//        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
//            super.onPageStarted(view, url, favicon)
//        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            super.onReceivedSslError(view, handler, error)
            handler.proceed() // Ignore SSL certificate errors
        }

        override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
            hideProgressBar()
        }

        @TargetApi(Build.VERSION_CODES.M)
        override fun onReceivedError(view: WebView, req: WebResourceRequest, rerr: WebResourceError?) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            if (rerr != null && shouldHandleError(rerr.errorCode)) {
                if (isVisible()) {
                    onReceivedError(view, rerr.errorCode, rerr.description.toString(), req.url.toString())
                }
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            hideProgressBar()
            super.onPageFinished(view, url)
        }
    }

    override fun handleBackEvent(): Boolean {
        if (mWebView.canGoBack()) {
            mWebView.goBack()
            return true
        }
        return false
    }
}
