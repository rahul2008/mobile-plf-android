package com.philips.cdp.di.mec.screens.retailers


import android.annotation.TargetApi
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
import com.philips.cdp.di.ecs.constants.NetworkConstants

import com.philips.cdp.di.mec.R
import com.philips.cdp.di.mec.screens.MecBaseFragment
import com.philips.cdp.di.mec.utils.MECConstant
import java.net.MalformedURLException
import java.net.URL

class WebBuyFromRetailersFragment : MecBaseFragment(){

val TAG = WebBuyFromRetailersFragment::class.java!!.getName()
private var mWebView: WebView? = null
private var mUrl: String? = null
private var isPhilipsShop = false

override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val group = inflater.inflate(R.layout.mec_web_fragment, container, false) as ViewGroup
    createCustomProgressBar(group, BIG)
    //mUrl = getArguments()!!.getString(MECConstant.MEC_BUY_URL)
    //isPhilipsShop = getArguments()!!.getBoolean(MECConstant.MEC_IS_PHILIPS_SHOP)
    initializeWebView(group)
    return group
}

override fun onResume() {
    super.onResume()
    val title = getArguments()!!.getString(MECConstant.MEC_STORE_NAME)
    setTitleAndBackButtonVisibility(title, true)
    mWebView!!.onResume()
}

fun createInstance(args: Bundle, animType: MecBaseFragment.AnimationType): WebBuyFromRetailersFragment {
    val fragment = WebBuyFromRetailersFragment()
    args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal)
    fragment.setArguments(args)
    return fragment
}

internal fun initializeWebView(group: View) {
    mWebView = group.findViewById<View>(R.id.mec_webView) as WebView
    mWebView!!.settings.javaScriptEnabled = true
    mWebView!!.settings.domStorageEnabled = true
    mWebView!!.settings.setAppCacheEnabled(true)
    mWebView!!.settings.loadsImagesAutomatically = true
    mWebView!!.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    mWebView!!.webViewClient = object : WebViewClient() {

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            hideProgressBar()

        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            var tagUrl = url
            if (isPhilipsShop) {
                tagUrl = getPhilipsFormattedUrl(url)
            }
            super.onPageStarted(view, url, favicon)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
            if (url == null) return false

            try {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url)
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
    }

    mWebView!!.loadUrl(mUrl)
}

fun getPhilipsFormattedUrl(url: String): String {

//    val appName = IAPUtility.getInstance().getAppName()
//    val localeTag = IAPUtility.getInstance().getLocaleTag()
//    val builder = Uri.Builder().appendQueryParameter("origin", String.format(IAPAnalyticsConstant.PHILIPS_EXIT_LINK_PARAMETER, localeTag, appName, appName))
//
//    return if (isParameterizedURL(url)) {
//        url + "&" + builder.toString().replace("?", "")
//    } else {
//        url + builder.toString()
//    }
    return ""
}

private fun isParameterizedURL(url: String): Boolean {

    try {
        val urlString = URL(url)
        return urlString.query != null
    } catch (e: MalformedURLException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return false
}

private fun shouldHandleError(errorCode: Int): Boolean {
    return (errorCode == WebViewClient.ERROR_CONNECT
            || errorCode == WebViewClient.ERROR_BAD_URL
            || errorCode == WebViewClient.ERROR_TIMEOUT
            || errorCode == WebViewClient.ERROR_HOST_LOOKUP)
}

override fun handleBackEvent(): Boolean {
    if (mWebView!!.canGoBack()) {
        mWebView!!.goBack()
        return true
    }
    return false
}
}
