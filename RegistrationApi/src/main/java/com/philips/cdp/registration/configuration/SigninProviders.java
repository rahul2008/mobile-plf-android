
package com.philips.cdp.registration.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class SigninProviders {

	private final String DEFAULT = "default";

	private HashMap<String, ArrayList<String>> providers;

	public HashMap<String, ArrayList<String>> getProviders() {
		return providers;
	}

	public void setProviders(HashMap<String, ArrayList<String>> providers) {
		this.providers = providers;
	}

	public ArrayList<String> getProvidersForCountry(String countryCode) {
		ArrayList<String> signinProviders = providers.get(countryCode.toUpperCase(Locale
		        .getDefault()));
		if (null == signinProviders) {
			signinProviders = providers.get(DEFAULT.toUpperCase(Locale.getDefault()));
		}
		return signinProviders;
	}

}
