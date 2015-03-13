package com.philips.cl.di.digitalcare;

import android.content.Context;
import android.content.res.Resources;

import com.philips.cl.di.digitalcare.util.FragmentObserver;

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
	private static int[] mFeatureKeys = {};

	/*
	 * Initialize everything required for DigitalCare as singleton.
	 */
	public DigitalCareApplication(Context context) {

		if (mFragmentObserver == null) {
			mFragmentObserver = new FragmentObserver();
			mResources = context.getResources();
			mFeatureKeys = mResources.getIntArray(R.array.options_available);
		}
		// mDigitalCareInstance = new DigitalCareApplication(context);
	}

	/*
	 * This will give list of all buttons(features) on the Support Screen.
	 */
	public static int[] getFeatureListKeys() {
		return mFeatureKeys;
	}

	/*
	 * This will return the FragmentObserver instance.
	 */
	public static FragmentObserver getFragmentObserverInstance() {
		return mFragmentObserver;
	}
}
