
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
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

public class RegistrationConfiguration {

    public static final String UR = "UR";



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


    public void setRegistrationClientId(@NonNull Configuration environment, @NonNull String value) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        boolean isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().setPropertyForKey("JanRainConfiguration." +
                        "RegistrationClientID." +
                        environment.getValue(),
                UR,
                value,
                configError);
        if (!isSuccesss) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
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
                getPropertyForKey("PILConfiguration." +
                        "MicrositeID", UR, configError);
        if (null == micrositeId) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
        return micrositeId;
    }

    /**
     * Set Microsite Id
     *
     * @param micrositeId String
     */
    public void setMicrositeId(@NonNull String micrositeId) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        boolean isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "MicrositeID",
                UR,
                micrositeId,
                configError);
        if (!isSuccesss) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
    }



    /**
     * Get Microsite Id
     *
     * @return String
     */
    public String getCampaignId() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        String campaignId = (String) RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("PILConfiguration." +
                        "CampaignId", UR, configError);
        if (null == campaignId) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
        return campaignId;
    }

    /**
     * Set Microsite Id
     *
     * @param campaignId String
     */
    public void setCampainId(@NonNull String campaignId) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        boolean isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "CampaignId",
                UR,
                campaignId,
                configError);
        if (!isSuccesss) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
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
                getPropertyForKey("PILConfiguration." +
                        "RegistrationEnvironment", UR, configError);
        if (null == registrationEnvironment) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
        return registrationEnvironment;
    }


    /**
     * Set Registration Environment
     *
     * @param registrationEnvironment Configuration
     */
    public void setRegistrationEnvironment(final Configuration registrationEnvironment) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        boolean isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().setPropertyForKey("PILConfiguration." +
                        "RegistrationEnvironment",
                UR,
                registrationEnvironment.getValue(),
                configError);
        if (!isSuccesss) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
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
                getPropertyForKey("Flow." +
                        "EmailVerificationRequired", UR, configError);
        if (obj != null) {
            return Boolean.parseBoolean((String) obj);
        }

        return true;
    }

    /**
     * Set Email verification status
     *
     * @param emailVerificationRequired
     */
    public void setEmailVerificationRequired(boolean emailVerificationRequired) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        boolean isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().setPropertyForKey("Flow." +
                        "EmailVerificationRequired",
                UR,
                "" + emailVerificationRequired,
                configError);
        if (!isSuccesss) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
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
                getPropertyForKey("Flow." +
                        "TermsAndConditionsAcceptanceRequired", UR, configError);
        if (obj != null) {
            return Boolean.parseBoolean((String) obj);
        }

        return false;


    }

    /**
     * Set terms and condition acceptance required or no
     *
     * @param termsAndConditionsAcceptanceRequired
     */
    public void setTermsAndConditionsAcceptanceRequired(boolean termsAndConditionsAcceptanceRequired) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        boolean isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                getConfigInterface().setPropertyForKey("Flow." +
                        "TermsAndConditionsAcceptanceRequired",
                UR,
                "" + termsAndConditionsAcceptanceRequired,
                configError);
        if (!isSuccesss) {
            RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                    "Error Message : " + configError.toString());
        }
    }


    /**
     * Set Min age Limit
     *
     * @param minAgeLimit Map with Key value pair of country code and min age
     */

    public void setMinAgeLimit(HashMap<String, String> minAgeLimit) {
        Set set = minAgeLimit.keySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            AppConfigurationInterface.AppConfigurationError configError = new
                    AppConfigurationInterface.AppConfigurationError();
            boolean isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                    getConfigInterface().setPropertyForKey("Flow." +
                            "MinimumAgeLimit." + key
                    ,
                    UR,
                    minAgeLimit.get(key),
                    configError);
            if (!isSuccesss) {
                RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                        "Error Message : " + configError.toString());
            }
        }
    }

    /**
     * Get minimium age for country
     *
     * @param countryCode Country code
     * @return integer value of min age if mapping available else 0
     */
    public int getMinAgeLimitByCountry(String countryCode) {
        String DEFAULT = "default";
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();


        Object obj = RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("Flow." +
                        "MinimumAgeLimit." + countryCode, UR, configError);
        if (obj != null) {
            String minAge = (String) obj;
            return Integer.parseInt(minAge);
        }

        obj = RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("Flow." +
                        "MinimumAgeLimit." + DEFAULT, UR, configError);
        if (obj != null) {
            String minAge = (String) obj;
            return Integer.parseInt(minAge);
        }


        return 0;
    }



 /*   "SigninProviders.NL": ["googleplus", "facebook","sinaweibo","qq" ],
            "SigninProviders.US": ["googleplus", "facebook","sinaweibo","qq" ],
            "SigninProviders.default": ["googleplus", "facebook","sinaweibo","qq" ]*/


    /**
     * Set providers in Hash map first param is country code and other is enabled providers list in Hash map
     *
     * @param providers
     */
    public void setProviders(@NonNull HashMap<String, ArrayList<String>> providers) {
        String DEFAULT = "default";
        Set keySet = providers.keySet();
        Iterator iterator = keySet.iterator();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            ArrayList<String> signinProviders = null;
            signinProviders = providers.get(key.toUpperCase());
            if (null == signinProviders) {
                signinProviders = providers.get(DEFAULT);
            }
            if (signinProviders != null) {

                AppConfigurationInterface.AppConfigurationError configError = new
                        AppConfigurationInterface.AppConfigurationError();
                boolean isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                        getConfigInterface().setPropertyForKey("SigninProviders." +
                                key.toUpperCase(Locale.ROOT)
                        ,
                        UR,
                        signinProviders,
                        configError);
                if (!isSuccesss) {
                    RLog.e("provider RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                            "provider Error Message : " + configError.toString());
                }

            }

        }
    }


    /**
     * Get HSDP information for specified configuration
     *
     * @param configuration
     * @return HSDPInfo Object
     */
    public HSDPInfo getHSDPInfo(Configuration configuration) {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        HSDPInfo hsdpInfo = new HSDPInfo();

        String appName = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("HsdpConfiguration." +
                        configuration.getValue() + "." + "AppName", UR, configError);
        String sharedId = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("HsdpConfiguration." +
                        configuration.getValue() + "." + "SharedId", UR, configError);

        String secreteId = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("HsdpConfiguration." +
                        configuration.getValue() + "." + "SecreteId", UR, configError);

        String baseUrl = (String) RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("HsdpConfiguration." +
                        configuration.getValue() + "." + "BaseURL", UR, configError);

        hsdpInfo.setApplicationName(appName);
        hsdpInfo.setSharedId(sharedId);
        hsdpInfo.setSecreteId(secreteId);
        hsdpInfo.setBaseURL(baseUrl);

        if(appName== null && sharedId == null && secreteId==null&& baseUrl==null){
            return null;
        }

        return hsdpInfo;
    }

    /**
     * Set HSDP Information for specific Configuraton
     *
     * @param configuration
     * @param hsdpInfo      HSDP Information
     */
    public void setHSDPInfo(Configuration configuration, HSDPInfo hsdpInfo) {
        RegistrationHelper.getInstance().setAppInfraInstance(AppInfraSingleton.getInstance());
        if (hsdpInfo != null) {
            AppConfigurationInterface.AppConfigurationError configError = new
                    AppConfigurationInterface.AppConfigurationError();
            boolean isSuccesss;
            isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                    getConfigInterface().setPropertyForKey(
                    "HsdpConfiguration." +
                            configuration.getValue() + "." +
                            "AppName",
                    UR,
                    hsdpInfo.getApplicationName(),
                    configError);

            isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                    getConfigInterface().setPropertyForKey(
                    "HsdpConfiguration." +
                            configuration.getValue() + "." +
                            "SecreteId",
                    UR,
                    hsdpInfo.getSecreteId(),
                    configError);

            isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                    getConfigInterface().setPropertyForKey(
                    "HsdpConfiguration." +
                            configuration.getValue() + "." +
                            "SharedId",
                    UR,
                    hsdpInfo.getSharedId(),
                    configError);

            isSuccesss = RegistrationHelper.getInstance().getAppInfraInstance().
                    getConfigInterface().setPropertyForKey(
                    "HsdpConfiguration." +
                            configuration.getValue() + "." +
                            "BaseURL",
                    UR,
                    hsdpInfo.getBaseURL(),
                    configError);

            if (!isSuccesss) {
                RLog.e("RegistrationConfiguration", "Error Code : " + configError.getErrorCode() +
                        "Error Message : " + configError.toString());
            }
        }

    }


    /**
     * Get provoders
     *
     * @param countryCode Country code
     * @return List of providers
     */
    public ArrayList<String> getProvidersForCountry(String countryCode) {

        String DEFAULT = "default";
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();


        Object obj = RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("SigninProviders." +
                        countryCode.toUpperCase(Locale.ROOT), UR, configError);

        if (obj != null) {
            return (ArrayList<String>) obj;
        }

        obj = RegistrationHelper.
                getInstance().getAppInfraInstance().
                getConfigInterface().
                getPropertyForKey("SigninProviders." +
                        DEFAULT.toUpperCase(Locale.ROOT), UR, configError);
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




        HSDPInfo hsdpInfo = getHSDPInfo(RegUtility.getConfiguration(environment));

        if (hsdpInfo == null) {
            RLog.i("HSDP_STATUS","HSDP configuration is not configured for " + environment + " environment ");
            return false;
           // throw new RuntimeException("HSDP configuration is not configured for " + environment + " environment ");
        }
        if (null != hsdpInfo) {

            String exception = buildException(hsdpInfo);

            if (null != exception) {
                throw new RuntimeException("HSDP configuration is not configured for " + getRegistrationEnvironment() + " environment for " + exception.toString().substring(4));
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
