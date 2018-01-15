package com.philips.platform.mya.catk.mapper;

import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.*;

public class LocaleMapperTest {

    @Test
    public void itShouldMapToCorrectLocale() throws Exception {
        assertEquals("en-CA", LocaleMapper.toLanguageString(Locale.CANADA));
        assertEquals("de-DE", LocaleMapper.toLanguageString(Locale.GERMANY));
    }

    @Test
    public void itShouldMapFromCorrectLocale() throws Exception {
        assertEquals(Locale.CANADA, LocaleMapper.toLocale("en-CA"));
        assertEquals(Locale.GERMANY, LocaleMapper.toLocale("de-DE"));
    }

    @Test
    public void toLanguage_invalid_cases() throws Exception {
        assertInvalidLocale(Locale.GERMAN);
        assertInvalidLocale(Locale.ENGLISH);
    }

    @Test
    public void toLocale_invalid_cases() throws Exception {
        assertInvalidLanguageTag("");
        assertInvalidLanguageTag(Locale.ENGLISH.toString());
        assertInvalidLanguageTag(Locale.GERMAN.toString());
        assertInvalidLanguageTag("nl");
        assertInvalidLanguageTag("_NL");
        assertInvalidLanguageTag("nl:NL");
        assertInvalidLanguageTag("_NL");
        assertInvalidLanguageTag("asdfasd");
        assertInvalidLanguageTag("asdfasdfasasf-asdfasdfasdfasdf");
    }

    private void assertInvalidLanguageTag(String locale) {
        try {
            LocaleMapper.toLocale(locale);
            fail("invalid Locale:" + locale);
        } catch(IllegalStateException ignore){}
    }

    private void assertInvalidLocale(Locale locale) {
        try {
            LocaleMapper.toLanguageString(locale);
            fail("invalid language tag: " + locale);
        } catch(IllegalStateException ignore){}
    }
}