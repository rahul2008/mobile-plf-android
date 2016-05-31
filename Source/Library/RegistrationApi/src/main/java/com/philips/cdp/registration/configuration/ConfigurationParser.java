
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.events.SocialProvider;
import com.philips.cdp.registration.settings.RegistrationEnvironmentConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ConfigurationParser {

    private final String REGISTRATION_CLIENT_ID = "RegistrationClientID";

    private final String CAMPAIGN_ID = "CampaignID";

    private final String REGISTRATION_ENVIRONMENT = "RegistrationEnvironment";

    private final String MICROSITE_ID = "MicrositeID";

    private final String SIGNIN_PROVIDERS = "SigninProviders";

    private final String PIL_CONFIGURATION = "PILConfiguration";

    private final String JAN_RAIN_CONFIGURATION = "JanRainConfiguration";

    private final String FLOW = "Flow";

    private final String MINIMUM_AGE_LIMIT = "MinimumAgeLimit";

    private final String EMAIL_VERIFICATION_REQUIRED = "EmailVerificationRequired";

    private final String TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED = "TermsAndConditionsAcceptanceRequired";

    private final String HSDP_CONFIGURATION = "HSDPConfiguration";


    public void parse(JSONObject configurationJson) {
        RegistrationStaticConfiguration registrationConfiguration = RegistrationStaticConfiguration
                .getInstance();
        try {
            if (!configurationJson.isNull(JAN_RAIN_CONFIGURATION)) {
                JSONObject janRainConfiguration = configurationJson
                        .getJSONObject(JAN_RAIN_CONFIGURATION);
                registrationConfiguration
                        .setJanRainConfiguration(parseJanRainConfiguration(janRainConfiguration));
            }
            if (!configurationJson.isNull(PIL_CONFIGURATION)) {
                JSONObject pILConfiguration = configurationJson.getJSONObject(PIL_CONFIGURATION);
                registrationConfiguration
                        .setPilConfiguration(parsePILConfiguration(pILConfiguration));
            }
            if (!configurationJson.isNull(SIGNIN_PROVIDERS)) {
                JSONObject socialProviders = configurationJson.getJSONObject(SIGNIN_PROVIDERS);
                registrationConfiguration.setSignInProviders(parseSocialProviders(socialProviders));
            }

            if (!configurationJson.isNull(FLOW)) {
                JSONObject flowConfiguartion = configurationJson.getJSONObject(FLOW);
                registrationConfiguration.setFlow(parseFlowConfiguration(flowConfiguartion));
            }

            if (!configurationJson.isNull(HSDP_CONFIGURATION)) {
                throw new RuntimeException("HSDP configuration should not be provided via json file");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private SigninProviders parseSocialProviders(JSONObject socialProviders)
            throws JSONException {
        SigninProviders providers = new SigninProviders();
        HashMap<String, ArrayList<String>> socialProviderMap = new HashMap<String, ArrayList<String>>();
        Iterator<String> socialProviderIterator = socialProviders.keys();
        while (socialProviderIterator.hasNext()) {
            String country = socialProviderIterator.next();
            JSONArray providerNames = socialProviders.getJSONArray(country);
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < providerNames.length(); i++) {
                list.add(providerNames.getString(i));
                if(providerNames.getString(i).equals(SocialProvider.TWITTER)){
                    throw new RuntimeException( SocialProvider.TWITTER +" Provider is not supporting");
                }
            }
            socialProviderMap.put(country.toUpperCase(), list);
        }
        providers.setProviders(socialProviderMap);
        return providers;
    }

    private PILConfiguration parsePILConfiguration(JSONObject pILConfiguration)
            throws JSONException {
        PILConfiguration configuration = new PILConfiguration();
        if (!pILConfiguration.isNull(MICROSITE_ID)) {
            configuration.setMicrositeId(pILConfiguration.getString(MICROSITE_ID));
        }

        if (!pILConfiguration.isNull(REGISTRATION_ENVIRONMENT)) {
            configuration.setRegistrationEnvironment(pILConfiguration
                    .getString(REGISTRATION_ENVIRONMENT));
        }

        if (!pILConfiguration.isNull(CAMPAIGN_ID)) {
            configuration.setCampaignID(pILConfiguration.getString(CAMPAIGN_ID));
        }
        return configuration;
    }

    private Flow parseFlowConfiguration(JSONObject flowConfiguration)
            throws JSONException {
        Flow configuration = new Flow();
        configuration.setEmailVerificationRequired(true);
        if (!flowConfiguration.isNull(EMAIL_VERIFICATION_REQUIRED)) {
            configuration.setEmailVerificationRequired(flowConfiguration.getBoolean(EMAIL_VERIFICATION_REQUIRED));
        }

        if (!flowConfiguration.isNull(TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED)) {
            configuration.setTermsAndConditionsAcceptanceRequired(flowConfiguration
                    .getBoolean(TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED));
        }

        if (!flowConfiguration.isNull(MINIMUM_AGE_LIMIT)) {
            JSONObject minimumAgeLimitConfiguartion = flowConfiguration.getJSONObject(MINIMUM_AGE_LIMIT);
            Iterator<String> nameItr = minimumAgeLimitConfiguartion.keys();
            HashMap<String, String> minAgeMap = new HashMap<String, String>();
            while (nameItr.hasNext()) {
                String name = nameItr.next();
                minAgeMap.put(name, minimumAgeLimitConfiguartion.getString(name));
            }
            configuration.setMinAgeLimit(minAgeMap);
        }
        return configuration;
    }

    private JanRainConfiguration parseJanRainConfiguration(JSONObject janRainConfiguration)
            throws JSONException {
        JanRainConfiguration configuration = new JanRainConfiguration();
        RegistrationClientId registrationClientId = new RegistrationClientId();
        JSONObject regIds = janRainConfiguration.getJSONObject(REGISTRATION_CLIENT_ID);
        if (!regIds.isNull(RegistrationEnvironmentConstants.DEV)) {
            registrationClientId.setDevelopmentId(regIds.getString(RegistrationEnvironmentConstants.DEV));
        }
        if (!regIds.isNull(RegistrationEnvironmentConstants.EVAL)) {
            registrationClientId.setEvaluationId(regIds.getString(RegistrationEnvironmentConstants.EVAL));
        }
        if (!regIds.isNull(RegistrationEnvironmentConstants.PROD)) {
            registrationClientId.setProductionId(regIds.getString(RegistrationEnvironmentConstants.PROD));
        }
        if (!regIds.isNull(RegistrationEnvironmentConstants.TESTING)) {
            registrationClientId.setTestingId(regIds.getString(RegistrationEnvironmentConstants.TESTING));
        }

        if (!regIds.isNull(RegistrationEnvironmentConstants.STAGING)) {
            registrationClientId.setStagingId(regIds.getString(RegistrationEnvironmentConstants.STAGING));
        }

        configuration.setClientIds(registrationClientId);
        return configuration;
    }
}
