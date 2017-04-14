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
import com.philips.cdp.registration.ui.utils.URInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.DEFAULT;

public class RegistrationConfiguration {

    @Inject
    HSDPConfiguration hsdpConfiguration;

    @Inject
    AppConfiguration appConfiguration;

    private RegistrationFunction prioritisedFunction = RegistrationFunction.Registration;

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
                if(isJSONValid(registrationClient)){
                    try {
                        JSONObject jsonObject = new JSONObject(registrationClient);
                        System.out.println("jsonObject : "+jsonObject);
                        if(!jsonObject.isNull(RegistrationHelper.getInstance().getCountryCode())){
                            registrationClient =  (String) jsonObject.get(RegistrationHelper.
                                    getInstance().getCountryCode());
                            System.out.println("registrationClient : "+registrationClient);
                            return registrationClient;
                        }else if(!jsonObject.isNull(DEFAULT)){
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

        String sharedId = hsdpConfiguration.getHsdpSharedId();

        String secreteId = hsdpConfiguration.getHsdpSecretId();

        String baseUrl = hsdpConfiguration.getHsdpBaseUrl();

        String appName = hsdpConfiguration.getHsdpAppName();

        RLog.i("HSDP_TEST", "sharedId" + sharedId + "Secret " + secreteId + " baseUrl " + baseUrl);

        if (appName == null && sharedId == null && secreteId == null && baseUrl == null) {
            return null;
        }
        HSDPInfo hsdpInfo = new HSDPInfo(sharedId, secreteId, baseUrl, appName);
        return hsdpInfo;
    }

    /**
     * Get provoders
     *
     * @param countryCode Country code
     * @return List of providers
     */
    public ArrayList<String> getProvidersForCountry(String countryCode) {
        List<String> providers = appConfiguration.getProvidersForCountry(countryCode);
        return (ArrayList<String>) providers;
    }


    public synchronized void setPrioritisedFunction(RegistrationFunction prioritisedFunction) {
        this.prioritisedFunction = prioritisedFunction;
    }

    public synchronized RegistrationFunction getPrioritisedFunction() {
        return prioritisedFunction;
    }

    private boolean isEnvironementSet() {
        String environment = getRegistrationEnvironment();
        if (environment == null) {
            return false;
        }
        return true;
    }

    public boolean isHsdpFlowWithoutBaseUrl() {
        if(!isEnvironementSet()) {
            return false;
        }

        HSDPInfo hsdpInfo = getHSDPInfo();

        if (hsdpInfo == null) {
            return false;
        }
        return isHsdpConfigurationExists(hsdpInfo);
    }

    private boolean isHsdpConfigurationExists(HSDPInfo hsdpInfo) {
        return null != hsdpInfo.getApplicationName() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getSecreteId();
    }

    public boolean isHsdpFlow() {

        if(!isEnvironementSet()) {
            return false;
        }

        HSDPInfo hsdpInfo = getHSDPInfo();

        if (hsdpInfo == null) {
            return false;
        }
        if (null != hsdpInfo) {

            String exception = buildException(hsdpInfo);

            if (null != exception) {
                throw new RuntimeException("HSDP configuration is not configured for " +
                        getRegistrationEnvironment() + " environment for " + exception.toString().substring(4));
            }
        }

        return isHsdpConfigurationComplete(hsdpInfo);
    }

    private boolean isHsdpConfigurationComplete(HSDPInfo hsdpInfo) {
        return null != hsdpInfo.getApplicationName() && null != hsdpInfo.getSharedId()
                && null != hsdpInfo.getSecreteId()
                && null != hsdpInfo.getBaseURL();
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
