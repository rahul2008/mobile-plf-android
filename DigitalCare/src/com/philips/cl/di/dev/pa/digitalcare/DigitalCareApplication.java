package com.philips.cl.di.dev.pa.digitalcare;

import android.content.Context;
import android.content.res.Resources;

import com.philips.cl.di.dev.pa.digitalcare.util.FragmentObserver;

/*
 *	DigitalCareApplication is Application class for DigitalCare app. 
 *  Here we can maintain the instances at digital care app level.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @creation Date : 5 Dec 2015
 */
public class DigitalCareApplication {

	private static FragmentObserver mFragmentObserver = null;
	private static Resources mResources = null;
	private static int[] keys = {};
	private static DigitalCareApplication mDigitalCareInstance = null;

	/*
	 * Initialize everything required for DigitalCare as singleton.
	 */
	private DigitalCareApplication(Context context) {

		if (mFragmentObserver == null) {
			mFragmentObserver = new FragmentObserver();
			mResources = context.getResources();
			keys = mResources.getIntArray(R.array.options_available);
		}
	}

	public static void getDigitalCareInstance(Context context) {
		if (mDigitalCareInstance == null) {
			mDigitalCareInstance = new DigitalCareApplication(context);
		}
	}

	/*
	 * This will give list of all buttons(features) on the Support Screen.
	 */
	public static int[] getFeatureListKeys() {
		return keys;
	}

	/*
	 * This will return the FragmentObserver instance.
	 */
	public static FragmentObserver getFragmentObserverInstance() {
		return mFragmentObserver;
	}
}
