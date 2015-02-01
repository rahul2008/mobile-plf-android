package com.philips.cl.di.dev.pa.digitalcare;

import android.content.Context;
import android.content.res.Resources;

public class ConfigurationManager {
	private static ConfigurationManager mConfigurationManager = null;

	private static Resources mResources = null;
	private static int[] keys = {};

	private ConfigurationManager(Context context) {
		mResources = context.getResources();
		keys = mResources.getIntArray(R.array.options_available);
	}

	public static ConfigurationManager getConfigucationInstance(Context context) {
		if (mConfigurationManager == null) {
			mConfigurationManager = new ConfigurationManager(context);
		}
		return mConfigurationManager;
	}
	
	public int[] getFeatureListKeys(){
		return keys;
	}
}
