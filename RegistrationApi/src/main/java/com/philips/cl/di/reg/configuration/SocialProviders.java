
package com.philips.cl.di.reg.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SocialProviders {

	private final String DEFAULT = "default";

	private HashMap<String, ArrayList<String>> providers;

	public HashMap<String, ArrayList<String>> getProviders() {
		return providers;
	}

	public void setProviders(HashMap<String, ArrayList<String>> providers) {
		this.providers = providers;
	}

	public ArrayList<String> getSocialProvidersForCountry(String countryCode) {
		ArrayList<String> socialProciders = providers.get(countryCode.toUpperCase(Locale
		        .getDefault()));
		if (null == socialProciders) {
			socialProciders = providers.get(DEFAULT.toUpperCase(Locale.getDefault()));
		}
		return socialProciders;
	}

}
