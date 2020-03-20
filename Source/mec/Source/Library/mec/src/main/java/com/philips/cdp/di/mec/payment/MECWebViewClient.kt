package com.philips.cdp.di.mec.payment

import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.*

class MECWebViewClient : WebViewClient() {


    protected fun shouldOverrideUrlLoading(url: String?): Boolean {
        return false
    }

    private fun shouldHandleError(errorCode: Int): Boolean {
        return (errorCode == WebViewClient.ERROR_CONNECT
                || errorCode == WebViewClient.ERROR_BAD_URL
                || errorCode == WebViewClient.ERROR_TIMEOUT
                || errorCode == WebViewClient.ERROR_HOST_LOOKUP)
    }


    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return this@MECWebViewClient.shouldOverrideUrlLoading(url)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        return this@MECWebViewClient.shouldOverrideUrlLoading(request?.getUrl().toString())
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)
        handler?.proceed()
    }

    @SuppressWarnings
    override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
        //do nothing
    }

    override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
        // Redirect to deprecated method, so you can use it in all SDK versions
        if (error != null && shouldHandleError(error.getErrorCode())) {
          /*  if (isVisible()) {
                onReceivedError(view, error.getErrorCode(), error.getDescription().toString(), error.getUrl().toString())
            }*/
        }
    }



    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }
}
