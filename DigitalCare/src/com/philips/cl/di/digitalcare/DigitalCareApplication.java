package com.philips.cl.di.digitalcare;

import android.content.Context;
import android.content.res.Resources;

/*
 *	DigitalCareApplication is Application class for DigitalCare app. 
 *  Here we can maintain the instances at digital care app level.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 5 Dec 2014
 */
public class DigitalCareApplication {

	private Resources mResources = null;
	private static int[] mFeatureKeys = null;
	private String[] mFeatureAvailable = null;
	private static DigitalCareApplication mDigitalCareInstance = null;

	/*
	 * Initialize everything required for DigitalCare as singleton.
	 */
	private DigitalCareApplication(Context context) {

		mResources = context.getResources();
		mFeatureAvailable = mResources
				.getStringArray(R.array.supported_features);
		mFeatureKeys = new int[mFeatureAvailable.length];
		// mFeatureKeys = mResources.getIntArray(R.array.options_available);
		getFeaturesAvailable();
	}
	
	public static DigitalCareApplication getInstance(Context context){
		if(mDigitalCareInstance==null){
			 mDigitalCareInstance = new DigitalCareApplication(context);
		}
		return mDigitalCareInstance;
	}

	private void getFeaturesAvailable() {

		for (int i = 0; i < mFeatureAvailable.length; i++) {
			try {
				mFeatureKeys[i] = Integer.parseInt(mFeatureAvailable[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * This will give list of all buttons(features) on the Support Screen.
	 */
	public int[] getFeatureListKeys() {
		return mFeatureKeys;
	}
}
