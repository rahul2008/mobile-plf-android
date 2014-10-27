package com.janrain.android.engage.net;

/*
 *InternetAccessibilityListener listener will say weather Internet is working or not.
 *
 * @author : Ritesh.jha@philips.com
 * 
 * @date : 27 Oct 2014
 */

public interface InternetAccessibilityListener {
	public void onInternetAvailable();
	public void onInternetNotAvailable();
}
