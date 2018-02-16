/* Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;


import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraLogEventID;
import com.philips.platform.appinfra.logging.LoggingInterface;

import static com.philips.platform.appinfra.tagging.AppTaggingConstants.SUCCESS_MESSAGE;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.TECHNICAL_ERROR;

public class AppInfraTaggingUtil implements AppTaggingAction {
    private AppTaggingInterface appTagging;
    private LoggingInterface appInfraLogging;

    public static final String MALFORMED_URL = "malformed url after applying url parameters";
    public static final String NO_NETWORK = "Network not reachable";

    ////Technical Errors
    public static final String GET_HOME_COUNTRY_SYNCHRONOUS_ERROR =  " error while fetching synchronous getHomeCountry due to secure storage returned empty or null data";
    public static final String COUNTRY_CODE_SIM_ERROR = " Error fetching country code from sim";
    public static final String SD_DATA_EXPIRED = " ServiceDiscovery data expired";

    // AppInfra Tagging Categories
    public static final String SERVICE_DISCOVERY = "ServiceDiscovery";

    ////AppInfra success
    public static final String GET_HOME_COUNTRY_INVOKED = " getHomeCountry synchronous api called";
    public static final String GET_HOME_COUNTRY_SYNCHRONOUS_SUCCESS = " get home country  synchronous fetched  successfully";
    public static final String DOWNLOAD_PLATFORM_SERVICES_INVOKED = " Downloading platform services started";
    public static final String DOWNLOAD_PREPOSITION_SERVICES_INVOKED = " Downloading preposition services started";
    public static final String SD_SUCCESS = " Service discovery successfully fetched data from server ";
    public static final String SD_LOCAL_CACHE_DATA_SUCCESS = " ServiceDiscovery successfully fetched local cached data";
    public static final String GET_HOME_COUNTRY_SIM_SUCCESS = " Successfully fetched country code from sim";
    public static final String GET_HOME_COUNTRY_GEOIP_SUCCESS = " Successfully fetched country code  from geoip";
    public static final String SET_HOME_COUNTRY_SUCCESS = " Successfully setHomeCountry to country ";
    public static final String ADD_URL_PARAMETERS = " Successfully added the URL parameters";
    public static final String SD_FORCE_REFRESH_CALLED = "Service discovery force refreshed called";

    public AppInfraTaggingUtil(AppTaggingInterface appTagging, LoggingInterface appInfraLogInstance) {
        this.appTagging = appTagging;
        this.appInfraLogging = appInfraLogInstance;
    }

    @Override
    public void trackSuccessAction(String category, String message) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(message)) {
            appTagging.trackActionWithInfo(AppTaggingConstants.SEND_DATA, SUCCESS_MESSAGE, category.concat(":").concat(message));
            appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SERVICE_DISCOVERY, category.concat(":").concat(message));
        }
    }

    @Override
    public void trackErrorAction(String category, String message) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(message)) {
            appTagging.trackActionWithInfo(AppTaggingConstants.SEND_DATA, TECHNICAL_ERROR, "AIL:".concat(category).concat(":").concat(message));
            appInfraLogging.log(LoggingInterface.LogLevel.DEBUG, AppInfraLogEventID.AI_SERVICE_DISCOVERY, "AIL:".concat(category).concat(":").concat(message));
        }
    }
}
