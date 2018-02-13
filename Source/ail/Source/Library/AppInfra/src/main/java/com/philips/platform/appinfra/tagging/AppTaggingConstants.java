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
    public static final String SEND_DATA = "sendData";


    //Actions
    public static final String TECHNICAL_ERROR = "TechnicalError";
    public static final String SUCCESS_MESSAGE = "appInfraSuccessMessage";



    public static final String TagSDRefreshError = "SD refresh failed";
    public static final String TagUrlByLanguageError = "getServiceUrlWithLanguagePreference error";
    public static final String TagUrlByCountryError = "getServiceUrlWithCountryPreference error";
    public static final String TagLocaleByLanguageError = "getServiceLocaleWithLanguagePreference, error";
    public static final String TagServicesByLanguageName = "getServicesWithLanguagePreference error";
    public static final String TagServicesByCountryError = "getServicesWithCountryPreference error";
    public static final String TagLocaleByCountryError = "getServiceLocaleWithCountryPreference, error";


}
