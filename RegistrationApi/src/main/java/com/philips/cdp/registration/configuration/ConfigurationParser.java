
package com.philips.cdp.registration.configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

public class ConfigurationParser {

	private final String TESTING = "Testing";

	private final String PRODUCTION = "Production";

	private final String EVALUATION = "Evaluation";

	private final String DEVELOPMENT = "Development";

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

	public void parse(JSONObject configurationJson) {
		RegistrationConfiguration registrationConfiguration = RegistrationConfiguration
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
				registrationConfiguration.setSocialProviders(parseSocialProviders(socialProviders));
			}

			if (!configurationJson.isNull(FLOW)) {
				JSONObject flowConfiguartion = configurationJson.getJSONObject(FLOW);
				registrationConfiguration.setFlow(parseFlowConfiguration(flowConfiguartion));
			}

		} catch (Exception e) {
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
			}
			socialProviderMap.put(country.toUpperCase(Locale.getDefault()), list);
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
			if (!flowConfiguration.getString(EMAIL_VERIFICATION_REQUIRED).equals("null")) {
				configuration.setEmailVerificationRequired(Boolean.parseBoolean(flowConfiguration
						.getString(EMAIL_VERIFICATION_REQUIRED).toLowerCase(Locale.getDefault())));
			}
		}

		if (!flowConfiguration.isNull(TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED)) {
			if (!flowConfiguration.getString(TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED).equals("null")) {
				configuration.setTermsAndConditionsAcceptanceRequired(Boolean.parseBoolean(flowConfiguration
						.getString(TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED).toLowerCase(Locale.getDefault())));
			}
		}

		JSONObject minimumAgeLimitConfiguartion = flowConfiguration.getJSONObject(MINIMUM_AGE_LIMIT);
		Iterator<String> nameItr = minimumAgeLimitConfiguartion.keys();
		HashMap<String, String> minAgeMap = new HashMap<String, String>();
		while(nameItr.hasNext()) {
			String name = nameItr.next();
			minAgeMap.put(name, minimumAgeLimitConfiguartion.getString(name));
		}
		configuration.setMinAgeLimit(minAgeMap);
		return configuration;
	}

	private JanRainConfiguration parseJanRainConfiguration(JSONObject janRainConfiguration)
	        throws JSONException {
		JanRainConfiguration configuration = new JanRainConfiguration();
		RegistrationClientId registrationClientId = new RegistrationClientId();
		JSONObject regIds = janRainConfiguration.getJSONObject(REGISTRATION_CLIENT_ID);
		if (!regIds.isNull(DEVELOPMENT)) {
			registrationClientId.setDevelopmentId(regIds.getString(DEVELOPMENT));
		}
		if (!regIds.isNull(EVALUATION)) {
			registrationClientId.setEvaluationId(regIds.getString(EVALUATION));
		}
		if (!regIds.isNull(PRODUCTION)) {
			registrationClientId.setProductionId(regIds.getString(PRODUCTION));
		}
		if (!regIds.isNull(TESTING)) {
			registrationClientId.setTestingId(regIds.getString(TESTING));
		}
		configuration.setClientIds(registrationClientId);
		return configuration;
	}
}
