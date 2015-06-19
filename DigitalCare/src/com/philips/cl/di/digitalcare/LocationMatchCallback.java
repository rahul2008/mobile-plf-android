package com.philips.cl.di.digitalcare;

import java.util.Locale;

public interface LocationMatchCallback {

	void onCountryFallbackLocaleReceive(Locale locale);
	
	void onLocaleLocaleReceive(Locale locale);
	
}