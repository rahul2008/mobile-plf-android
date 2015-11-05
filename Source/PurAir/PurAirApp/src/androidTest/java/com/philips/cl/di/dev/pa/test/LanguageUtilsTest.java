package com.philips.cl.di.dev.pa.test;

import java.util.Locale;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.util.LanguageUtils;

public class LanguageUtilsTest extends TestCase {
	
	public static final String LANGUAGE_ENGLISH = "EN";
	public static final String LANGUAGE_FRENCH = "FR";
	public static final String LANGUAGE_CHINESE_TRAD = "ZH-HANT";
	public static final String LANGUAGE_CHINESE_SIMP = "ZH-HANS";
	
	public void testDefaultLanguageEnglish() {
		assertEquals(LANGUAGE_ENGLISH, LanguageUtils.DEFAULT_LANGUAGE);
	}

	public void testGetLanguageNull() {
		Locale loc = null;
		String language = LanguageUtils.getLanguageForLocale(loc);
		
		assertEquals(LanguageUtils.DEFAULT_LANGUAGE, language);
	}

	public void testGetLanguageEnglish() {
		Locale loc = Locale.ENGLISH;
		String language = LanguageUtils.getLanguageForLocale(loc);
		
		assertEquals(LANGUAGE_ENGLISH, language);
	}

	public void testGetLanguageLocaleEmptyLanguage() {
		Locale loc = new Locale("");
		String language = LanguageUtils.getLanguageForLocale(loc);
		
		assertEquals(LANGUAGE_ENGLISH, language);
	}

	public void testGetLanguageTraditionalChinese() {
		Locale loc = Locale.TRADITIONAL_CHINESE;
		String language = LanguageUtils.getLanguageForLocale(loc);
		
		assertEquals(LANGUAGE_CHINESE_TRAD, language);
	}

	public void testGetLanguageSimplifiedChinese() {
		Locale loc = Locale.SIMPLIFIED_CHINESE;
		String language = LanguageUtils.getLanguageForLocale(loc);
		
		assertEquals(LANGUAGE_CHINESE_SIMP, language);
	}

	public void testGetLanguageFrench() {
		Locale loc = Locale.FRENCH;
		String language = LanguageUtils.getLanguageForLocale(loc);
		
		assertEquals(LANGUAGE_ENGLISH, language);
	}

}
