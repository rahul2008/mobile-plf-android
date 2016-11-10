/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;

import android.support.annotation.NonNull;

import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.DEFAULT;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

public class RegistrationConfiguration {

    private RegistrationFunction prioritisedFunction = RegistrationFunction.Registration;

    private static volatile RegistrationConfiguration registrationConfiguration;

    private RegistrationConfiguration() {
    }

    public static synchronized RegistrationConfiguration getInstance() {
        if (registrationConfiguration == null) {
            synchronized (RegistrationConfiguration.class) {
                if (registrationConfiguration == null) {
                    registrationConfiguration = new RegistrationConfiguration();
                }
            }
        }
        return registrationConfiguration;
    }

    public String getRegistrationClientId(@NonNull Configuration environment) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();

        Object obj = RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." +
                        environment.getValue(), UR, configError);
        String registrationClient = null;
        if (obj != null) {
            registrationClient = (String) obj;
        } else {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }

        return registrationClient;
    }


    /**
     * Get Microsite Id
     *
     * @return String
     */
    public String getMicrositeId() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        String micrositeId = (String) RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.PILCONFIGURATION_MICROSITE_ID,
                        UR, configError);
        if (null == micrositeId) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
        return micrositeId;
    }


    /**
     * Get Campaign Id
     *
     * @return String
     */
    public String getCampaignId() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        String campaignId = (String) RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.PIL_CONFIGURATION_CAMPAIGN_ID, UR,
                        configError);
        if (null == campaignId) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
        return campaignId;
    }

    /**
     * Get Registarion Environment
     *
     * @return String
     */
    public String getRegistrationEnvironment() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        String registrationEnvironment = (String) RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.PILCONFIGURATION_REGISTRATION_ENVIRONMENT
                        , UR, configError);
        if (null == registrationEnvironment) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
        return registrationEnvironment;
    }



    /**
     * Status of email verification required
     *
     * @return boolean
     */
    public boolean isEmailVerificationRequired() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        Object obj = RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.FLOW_EMAIL_VERIFICATION_REQUIRED
                        , UR, configError);
        if (obj != null) {
            return Boolean.parseBoolean((String) obj);
        }

        return true;
    }

    /**
     * Status of terms and condition accepatance
     *
     * @return boolean
     */
    public boolean isTermsAndConditionsAcceptanceRequired() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();

        Object obj = RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.
                        FLOW_TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED, UR, configError);
        if (obj != null) {
            return Boolean.parseBoolean((String) obj);
        }
        return false;
    }

    /**
     * Get minimium age for country
     *
     * @param countryCode Country code
     * @return integer value of min age if mapping available else 0
     */
    public int getMinAgeLimitByCountry(String countryCode) {
        try {
            AppConfigurationInterface.AppConfigurationError configError = new
                    AppConfigurationInterface.AppConfigurationError();
            Object obj = RegistrationHelper.
                    getInstance().getAppInfraInstance().
                    getConfigInterface().
                    getPropertyForKey(URConfigurationConstants.FLOW_MINIMUM_AGE_LIMIT
                            , UR, configError);
            if (obj != null) {
                JSONObject jsonObject = new JSONObject((String)obj);
                if(!jsonObject.isNull(countryCode)){
                    return (int) jsonObject.get(countryCode);
                }else if(!jsonObject.isNull(DEFAULT)){
                    return (int) jsonObject.get(DEFAULT);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * Get HSDP information for specified configuration
     *
     * @return HSDPInfo Object
     */
    public HSDPInfo getHSDPInfo() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        HSDPInfo hsdpInfo = new HSDPInfo();

        String appName = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME
                        , UR, configError);
        String sharedId = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.HSDP_CONFIGURATION_SHARED
                        , UR, configError);

        String secreteId = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.HSDP_CONFIGURATION_SECRET
                        , UR, configError);

        String baseUrl = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL
                        , UR, configError);

        hsdpInfo.setApplicationName(appName);
        hsdpInfo.setSharedId(sharedId);
        hsdpInfo.setSecreteId(secreteId);
        hsdpInfo.setBaseURL(baseUrl);

        if (appName == null && sharedId == null && secreteId == null && baseUrl == null) {
            return null;
        }

        return hsdpInfo;
    }

    /**
     * Get provoders
     *
     * @param countryCode Country code
     * @return List of providers
     */
    public ArrayList<String> getProvidersForCountry(String countryCode) {

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();


        Object obj = RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.SIGNIN_PROVIDERS +
                        countryCode, UR, configError);

        if (obj != null) {
            return (ArrayList<String>) obj;
        }

        obj = RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey(URConfigurationConstants.SIGNIN_PROVIDERS +
                        DEFAULT, UR, configError);
        if (obj != null) {
            return (ArrayList<String>) obj;
        }
        return null;
    }


    public synchronized void setPrioritisedFunction(RegistrationFunction prioritisedFunction) {
        this.prioritisedFunction = prioritisedFunction;
    }

    public synchronized RegistrationFunction getPrioritisedFunction() {
        return prioritisedFunction;
    }


    public boolean isHsdpFlow() {

        String environment = getRegistrationEnvironment();
        if (environment == null) {
            return false;
        }


        HSDPInfo hsdpInfo = getHSDPInfo();

        if (hsdpInfo == null) {
            RLog.i("HSDP_STATUS", "HSDP configuration is not configured for " + environment +
                    " environment ");
            return false;
            // throw new RuntimeException("HSDP configuration is not configured for " + environment + " environment ");
        }
        if (null != hsdpInfo) {

            String exception = buildException(hsdpInfo);

            if (null != exception) {
                throw new RuntimeException("HSDP configuration is not configured for " +
                        getRegistrationEnvironment() + " environment for " + exception.toString().substring(4));
            }
        }


        return (null != hsdpInfo.getApplicationName() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getSecreteId()
                && null != hsdpInfo.getBaseURL());
    }

    private String buildException(HSDPInfo hsdpInfo) {
        String exception = null;

        if (hsdpInfo.getApplicationName() == null) {
            exception += "Application Name";
        }

        if (hsdpInfo.getSharedId() == null) {
            if (null != exception) {
                exception += ",shared key ";
            } else {
                exception += "shared key ";
            }
        }
        if (hsdpInfo.getSecreteId() == null) {
            if (null != exception) {
                exception += ",Secret key ";
            } else {
                exception += "Secret key ";
            }
        }

        if (hsdpInfo.getBaseURL() == null) {
            if (null != exception) {
                exception += ",Base Url ";
            } else {
                exception += "Base Url ";
            }
        }
        return exception;
    }


}
