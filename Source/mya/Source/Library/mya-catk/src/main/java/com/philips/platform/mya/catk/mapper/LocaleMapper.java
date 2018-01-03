/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk.mapper;

import java.util.Locale;

public class LocaleMapper {

    private static final String LOCALE_SEPERATOR = "-";
    private static int IDX_LANGUAGE = 0;
    private static int IDX_COUNTRY = 1;

    public static Locale toLocale(String locale) {
        if(!locale.contains(LOCALE_SEPERATOR) || locale.split(LOCALE_SEPERATOR).length != 2 || locale.split(LOCALE_SEPERATOR)[0].length() != 2 || locale.split(LOCALE_SEPERATOR)[1].length() != 2 ) throw new IllegalStateException("Locale string needs to be in the following format: nl-NL");
        String[] localeParts = locale.split(LOCALE_SEPERATOR);
        return new Locale(localeParts[IDX_LANGUAGE], localeParts[IDX_COUNTRY]);
    }

    public static String toLanguageString(Locale locale) {
        if(isMissingCountry(locale)) throw new IllegalStateException("BackendConsent is missing a Locale that specifies a country:" + locale);
        if(isMissingLanguage(locale)) throw new IllegalStateException("BackendConsent is missing a Locale that specifies a language:" + locale);
        return locale.toString().replace('_', '-');
    }

    private static boolean isMissingLanguage(Locale locale) {
        String language = locale.getISO3Language();
        return language == null || "".equals(language);
    }

    private static boolean isMissingCountry(Locale locale) {
        String country = locale.getISO3Country();
        return country == null || "".equals(country);
    }
}
