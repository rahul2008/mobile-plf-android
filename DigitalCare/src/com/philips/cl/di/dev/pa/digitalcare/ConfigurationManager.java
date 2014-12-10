package com.philips.cl.di.dev.pa.digitalcare;

import android.content.res.Resources;

public class ConfigurationManager {
	private static ConfigurationManager mConfigurationManager = null;

	private static Resources mResources = null;
	private static int[] keys = {};

	private ConfigurationManager() {
		mResources = DigitalCareApplication.getAppContext().getResources();
		keys = mResources.getIntArray(R.array.options_available);
	}

	public static ConfigurationManager getInstance() {
		if (mConfigurationManager == null) {
			mConfigurationManager = new ConfigurationManager();
		}
		return mConfigurationManager;
	}
	
	public int[] getFeatureListKeys(){
		return keys;
	}
}
