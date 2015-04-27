package com.philips.cl.di.reg.settings;

import android.content.Context;

public abstract class RegistrationSettings {
	
	protected String mProductRegisterUrl = null;
	protected String mProductRegisterListUrl = null;
	protected String mPreferredCountryCode = null;
	protected String mPreferredLangCode = null;
	
	public String REGISTRATION_USE_PRODUCTION = "REGISTRATION_USE_PRODUCTION";

	public String REGISTRATION_USE_EVAL = "REGISTRATION_USE_EVAL";

	public String REGISTRATION_USE_DEVICE = "REGISTRATION_USE_DEVICE";
	
	public static final String REGISTRATION_API_PREFERENCE = "REGAPI_PREFERENCE";

	public static final String MICROSITE_ID = "microSiteID";

	public abstract void intializeRegistrationSettings(Context context, String captureClientId,
			String microSiteId, String registrationType, boolean isIntialize,
			String locale);
	
	
	public String getProductRegisterUrl() {
		return mProductRegisterUrl;
	}

	public String getProductRegisterListUrl() {
		return mProductRegisterListUrl;
	}

	public String getPreferredCountryCode() {
		return mPreferredCountryCode;
	}

	public String getPreferredLangCode() {
		return mPreferredLangCode;
	}

}
