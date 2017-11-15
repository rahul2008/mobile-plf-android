/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;

import android.support.annotation.NonNull;

import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;

import org.json.*;

import java.util.*;

import javax.inject.Inject;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.DEFAULT;

public class RegistrationConfiguration {

    @Inject
    HSDPConfiguration hsdpConfiguration;

    @Inject
    AppConfiguration appConfiguration;

    private RegistrationFunction prioritisedFunction = RegistrationFunction.Registration;

    UserRegistrationUIEventListener userRegistrationUIEventListener;

    public UserRegistrationUIEventListener getUserRegistrationUIEventListener() {
        return userRegistrationUIEventListener;
    }

    public void setUserRegistrationUIEventListener(UserRegistrationUIEventListener userRegistrationUIEventListener) {
        this.userRegistrationUIEventListener = userRegistrationUIEventListener;
    }

    private static volatile RegistrationConfiguration registrationConfiguration;

    private RegistrationConfiguration() {
        URInterface.getComponent().inject(this);
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
        String registrationClient = appConfiguration.getClientId(environment.getValue());
        if (registrationClient != null) {
            if (isJSONValid(registrationClient)) {
                try {
                    JSONObject jsonObject = new JSONObject(registrationClient);
                    if (!jsonObject.isNull(RegistrationHelper.getInstance().getCountryCode())) {
                        registrationClient = (String) jsonObject.get(RegistrationHelper.
                                getInstance().getCountryCode());
                        return registrationClient;
                    } else if (!jsonObject.isNull(DEFAULT)) {
                        registrationClient = (String) jsonObject.get(DEFAULT);
                        return registrationClient;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            RLog.e("RegistrationConfiguration", "Registration client is null");
        }

        return registrationClient;
    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get Microsite Id
     *
     * @return String
     */
    public String getMicrositeId() {
        String micrositeId = appConfiguration.getMicrositeId();
        if (null == micrositeId) {
            RLog.e("RegistrationConfiguration", "Microsite ID is null");
        }
        return micrositeId;
    }

    /**
     * This should not be supported for longer time
     *
     * @return String
     */
    public List<String> getServiceDiscoveryCountries() {
        HashMap<String,String> sdCountryMapping = appConfiguration.getServiceDiscoveryCountryMapping();
        if (null == sdCountryMapping) {
            RLog.e("RegistrationConfiguration", "sdCountryMapping is null");
            return null;
        }
        return  new ArrayList<>(sdCountryMapping.keySet());
    }



    /**
     * Get Campaign Id
     *
     * @return String
     */
    public String getCampaignId() {
        String campaignId = appConfiguration.getCampaignId();
        if (null == campaignId) {
            RLog.e("RegistrationConfiguration", "Campaign ID is null");
        }
        return campaignId;
    }

    /**
     * Get Registarion Environment
     *
     * @return String
     */
    public String getRegistrationEnvironment() {
        String registrationEnvironment = appConfiguration.getRegistrationEnvironment();
        if (null == registrationEnvironment) {
            RLog.e("RegistrationConfiguration", "Registration environment is null");
        }
        if (registrationEnvironment.equalsIgnoreCase("TEST"))
            return Configuration.TESTING.getValue();
        if (registrationEnvironment.equalsIgnoreCase("ACCEPTANCE"))
            return Configuration.EVALUATION.getValue();
        return registrationEnvironment;
    }


    /**
     * Status of email verification required
     *
     * @return boolean
     */
    public boolean isEmailVerificationRequired() {
        Object obj = appConfiguration.getEmailVerificationRequired();
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
        Object obj = appConfiguration.getTermsAndConditionsAcceptanceRequired();
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
            Object obj = appConfiguration.getMinimunAgeObject();
            if (obj != null) {
                JSONObject jsonObject = new JSONObject((String) obj);
                if (!jsonObject.isNull(countryCode)) {
                    return (int) jsonObject.get(countryCode);
                } else if (!jsonObject.isNull(DEFAULT)) {
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

        String sharedId = hsdpConfiguration.getHsdpSharedId();

        String secreteId = hsdpConfiguration.getHsdpSecretId();

        String baseUrl = hsdpConfiguration.getHsdpBaseUrl();

        String appName = hsdpConfiguration.getHsdpAppName();

        RLog.i("HSDP_TEST", "sharedId" + sharedId + "Secret " + secreteId + " baseUrl " + baseUrl);

        if (appName == null && sharedId == null && secreteId == null && baseUrl == null) {
            return null;
        }
        return new HSDPInfo(sharedId, secreteId, baseUrl, appName);
    }

    /**
     * Get provoders
     *
     * @param countryCode Country code
     * @return List of providers
     */
    public List<String> getProvidersForCountry(String countryCode) {
        return appConfiguration.getProvidersForCountry(countryCode);
    }

    public List<String> getSupportedHomeCountry() {
        return appConfiguration.getSupportedHomeCountries();
    }

    public String getFallBackHomeCountry() {
        return appConfiguration.getFallBackHomeCountry();
    }


    public synchronized void setPrioritisedFunction(RegistrationFunction prioritisedFunction) {
        this.prioritisedFunction = prioritisedFunction;
    }

    public synchronized RegistrationFunction getPrioritisedFunction() {
        return prioritisedFunction;
    }

    private boolean isEnvironementSet() {
        return getRegistrationEnvironment() != null;
    }

    public boolean isHsdpFlow() {
        return isEnvironementSet() && isHsdpInfoAvailable();
    }

    private boolean isHsdpInfoAvailable() {
        HSDPInfo hsdpInfo = getHSDPInfo();
        return hsdpInfo != null
                && hsdpInfo.getSecreteId() != null
                && hsdpInfo.getSharedId() != null;
    }
}
