package com.philips.platform.appinfra.rest;

/*
 * The Class for ServiceID Url formatting.
 */

public class ServiceIDUrlFormatting {

    public enum SERVICEPREFERENCE {BYCOUNTRY, BYLANGUAGE}

    private final static String BYCOUNTRYPREFIX = "serviceid://bycountry_/";   // added "_" to bycountry to make same length as that of bylanguage
    private final static String BYLANGUAGEPREFIX = "serviceid://bylanguage/";
    private final static int PREFIXLENGTH = BYCOUNTRYPREFIX.length();

    public static String formatUrl(String serviceID, SERVICEPREFERENCE preference, String urlExtension) {
        return ((preference == SERVICEPREFERENCE.BYCOUNTRY) ? BYCOUNTRYPREFIX : BYLANGUAGEPREFIX) + serviceID + "/" + (urlExtension == null ? "" : urlExtension);
    }

    public static boolean isServiceIDUrl(String url) {
        return (url.startsWith(BYCOUNTRYPREFIX) || url.startsWith(BYLANGUAGEPREFIX)) && url.indexOf("/", PREFIXLENGTH) > -1;
    }

    public static SERVICEPREFERENCE getPreference(String url) {
        return url.startsWith(BYCOUNTRYPREFIX) ? SERVICEPREFERENCE.BYCOUNTRY : SERVICEPREFERENCE.BYLANGUAGE;
    }

    public static String getServiceID(String url) {
        return url.substring(PREFIXLENGTH, url.indexOf("/", PREFIXLENGTH));
    }

    public static String getUrlExtension(String url) {
        return url.substring(url.indexOf("/", PREFIXLENGTH + 1));
    }

}
