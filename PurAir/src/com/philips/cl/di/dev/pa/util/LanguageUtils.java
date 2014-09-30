package com.philips.cl.di.dev.pa.util;

import java.util.HashMap;
import java.util.Locale;

import com.philips.cl.di.dev.pa.constant.AppConstants;

public class LanguageUtils {
	
	public static final String DEFAULT_LANGUAGE = "EN";
	
	private static HashMap<Locale, String> customMapping = null;
	static {
		customMapping = new HashMap<Locale, String>();
		customMapping.put(Locale.TRADITIONAL_CHINESE, AppConstants.TRADITIONAL_CHINESE_LANGUAGE_CODE);
		customMapping.put(Locale.SIMPLIFIED_CHINESE, AppConstants.SIMPLIFIED_CHINESE_LANGUAGE_CODE);
	}
	
	public static String getLanguageForLocale(Locale loc) {
		if (loc == null) return DEFAULT_LANGUAGE;

		
		String language = loc.getLanguage();
		if (language == null || language.isEmpty()) return DEFAULT_LANGUAGE;
		if(!customMapping.containsKey(loc)) return DEFAULT_LANGUAGE;
		
		for (Locale custom : customMapping.keySet()) {
			if (!custom.equals(loc)) continue;
			language = customMapping.get(custom);
		}
		
		return language.toUpperCase();
	}
}
