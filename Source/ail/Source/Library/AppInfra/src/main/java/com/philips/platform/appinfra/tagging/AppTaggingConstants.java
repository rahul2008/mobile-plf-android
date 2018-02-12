/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

/**
 * The AppTagging Constants Class.
 */
public class AppTaggingConstants {


    public static final String BUNDLE_ID = "bundleId";

    public static final String LANGUAGE_KEY = "language";

    public static final String APPSID_KEY = "appsId";

    public static final String LOCAL_TIMESTAMP_KEY = "localTimeStamp";

    public static final String UTC_TIMESTAMP_KEY = "UTCTimestamp";

    public static final String PREVIOUS_PAGE_NAME = "previousPageName";

    public static final String COMPONENT_ID = "componentId";

    public static final String COMPONENT_VERSION = "componentVersion";

    //keys
    public static final String SendData = "sendData";


    //Actions
    public static final String TechnicalError = "TechnicalError";
    public static final String SuccessMessage = "appInfraSuccessMessage";

    ////Technical Errors
//////Service Discovery
    public static final String TagHomeCountryError = "get GomeCountry Error";
    public static final String TagSDRefreshError = "SD refresh failed";
    public static final String TagUrlByLanguageError = "getServiceUrlWithLanguagePreference error";
    public static final String TagUrlByCountryError = "getServiceUrlWithCountryPreference error";
    public static final String TagLocaleByLanguageError = "getServiceLocaleWithLanguagePreference, error";
    public static final String TagServicesByLanguageName = "getServicesWithLanguagePreference error";
    public static final String TagServicesByCountryError = "getServicesWithCountryPreference error";
    public static final String TagLocaleByCountryError = "getServiceLocaleWithCountryPreference, error";
    public static final String TagMalformedUrl = "applyURLParameters, malformed url";
    public static final String TagOldUrlAfterRefreshFail = "using old url after 24 hour refresh failed";
    public static final String TagNotReachable = "Network not reachable";

    ////AppInfra success
//////Service Discovery
    public static final String TagHomeCountry = "gethome country synchronous api called";
    public static final String TagSetHomeCountry = "setHomecountry called";
    public static final String TagSDRefresh = "SD refresh called";
    public static final String TagSDShouldNotRefresh = "SD shouldnot refresh";
    public static final String TagSDDatafromCache = "SD returning data from cache";
    public static final String TagSDDataExpired = "SD data expired - 24 hours over";
    public static final String TagDownloadPlatform = "Downloading platform URLS";
    public static final String TagDownloadProposition = "Downloading proposition URLS";
    public static final String TagCountryFromSim = "saveCountryCodefrom sim";
    public static final String TagCountryFromIP = "countryFetchedFromGEOIP";
    public static final String TagLocaleCorrection = "localeCorrectionForSDServer";
    public static final String TagCountryMapping = "country mapping present";
    public static final String TagDownloadStart = "SD data Download started";
    public static final String TagDownloadCompleted = "SD download completed with status";

}
