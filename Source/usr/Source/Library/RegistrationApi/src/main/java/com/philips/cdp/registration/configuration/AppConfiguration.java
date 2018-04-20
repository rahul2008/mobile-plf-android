/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;


import com.philips.cdp.registration.ui.utils.RLog;

import java.util.List;
import java.util.Map;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.DEFAULT;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.FALLBACK_HOME_COUNTRY;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.FLOW_EMAIL_VERIFICATION_REQUIRED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.FLOW_MINIMUM_AGE_LIMIT;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.FLOW_TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_UUID_UPLOAD_IN_ANALYTICS;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.PIL_CONFIGURATION_CAMPAIGN_ID;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.SHOW_COUNTRY_SELECTION;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.SIGNIN_PROVIDERS;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.SUPPORTED_HOME_COUNTRIES;

public class AppConfiguration extends BaseConfiguration {

    private final String TAG = AppConfiguration.class.getSimpleName();

    private static final String SD_COUNTRYMAPPING_ID_KEY = "servicediscovery.countryMapping";
    private static final String WE_CHAT_APP_ID_KEY = "weChatAppId";
    private static final String WE_CHAT_APP_SECRET_KEY = "weChatAppSecret";
    private static final String CLIENT_ID_KEY = "JanRainConfiguration.RegistrationClientID.";

    public String getWeChatAppId() {
        Object weChatAppIdObject = appInfraWrapper.getURProperty(WE_CHAT_APP_ID_KEY);
        RLog.d(TAG, "getWeChatAppId : " + getConfigPropertyValue(weChatAppIdObject));
        return getConfigPropertyValue(weChatAppIdObject);
    }

    public String getWeChatAppSecret() {
        Object weChatAppSecretObject = appInfraWrapper.getURProperty(WE_CHAT_APP_SECRET_KEY);
        RLog.d(TAG, "getWeChatAppSecret : " + getConfigPropertyValue(weChatAppSecretObject));
        return getConfigPropertyValue(weChatAppSecretObject);
    }

    public String getMicrositeId() {
        Object micrositeIdObject = appInfraWrapper.getAppIdentity().getMicrositeId();
        RLog.d(TAG, "getMicrositeId : " + getConfigPropertyValue(micrositeIdObject));
        return getConfigPropertyValue(micrositeIdObject);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getServiceDiscoveryCountryMapping() {
        Object serviceDiscoveryCountryMappingObject = appInfraWrapper.getAppInfraProperty(SD_COUNTRYMAPPING_ID_KEY);
        Map<String, String> hashMap = null;
        if (serviceDiscoveryCountryMappingObject != null) {

            hashMap = (Map<String, String>) serviceDiscoveryCountryMappingObject;
            return hashMap;
        }
        RLog.d(TAG, "getServiceDiscoveryCountryMapping : ");
        return hashMap;
    }

    public String getRegistrationEnvironment() {
        RLog.d(TAG, "getRegistrationEnvironment : " + appInfraWrapper.getAppState().toString());
        return appInfraWrapper.getAppState().toString();
    }

    public String getClientId(String environment) {
        Object clientIdObject = appInfraWrapper.getURProperty(CLIENT_ID_KEY + environment);
        RLog.d(TAG, "getClientId : " + getConfigPropertyValue(clientIdObject));
        return getConfigPropertyValue(clientIdObject);
    }

    public String getCampaignId() {
        Object campaignIdObject = appInfraWrapper.getURProperty(PIL_CONFIGURATION_CAMPAIGN_ID);
        RLog.d(TAG, "getCampaignId : " + getConfigPropertyValue(campaignIdObject));
        return getConfigPropertyValue(campaignIdObject);
    }

    public Object getEmailVerificationRequired() {
        RLog.d(TAG, "getEmailVerificationRequired  ");
        return appInfraWrapper.getURProperty(FLOW_EMAIL_VERIFICATION_REQUIRED);
    }

    public Object getTermsAndConditionsAcceptanceRequired() {
        RLog.d(TAG, "getTermsAndConditionsAcceptanceRequired  ");
        return appInfraWrapper.getURProperty(FLOW_TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED);
    }

    public Object getMinimunAgeObject() {
        RLog.d(TAG, "getMinimunAgeObject  ");
        return appInfraWrapper.getURProperty(FLOW_MINIMUM_AGE_LIMIT);
    }

    @SuppressWarnings("unchecked")
    public List<String> getProvidersForCountry(String countryCode) {
        RLog.d(TAG, "getProvidersForCountry  ");
        Object providersObject = appInfraWrapper.getURProperty(SIGNIN_PROVIDERS + countryCode);
        if (providersObject != null) {
            return (List<String>) providersObject;
        }

        providersObject = appInfraWrapper.getURProperty(SIGNIN_PROVIDERS + DEFAULT);
        return (List<String>) providersObject;
    }

    @SuppressWarnings("unchecked")
    public List<String> getSupportedHomeCountries() {
        RLog.d(TAG, "getSupportedHomeCountries  ");
        Object providersObject = appInfraWrapper.getURProperty(SUPPORTED_HOME_COUNTRIES);
        List<String> providersObjects = null;
        if (providersObject != null) {
            providersObjects = (List<String>) providersObject;
            return providersObjects;
        }
        return providersObjects;
    }

    public String getFallBackHomeCountry() {
        RLog.d(TAG, "getFallBackHomeCountry  ");
        Object providersObject = appInfraWrapper.getURProperty(FALLBACK_HOME_COUNTRY);
        if (providersObject != null) {
            return (String) providersObject;
        }
        return null;
    }


    public String getShowCountrySelection() {
        RLog.d(TAG, "getShowCountrySelection  ");
        Object showCountrySelectionObject = appInfraWrapper.getURProperty(SHOW_COUNTRY_SELECTION);
        return getConfigPropertyValue(showCountrySelectionObject);
    }

    public Object getHSDPUuidUpload() {
        return appInfraWrapper.getURProperty(HSDP_UUID_UPLOAD_IN_ANALYTICS);
    }


}
