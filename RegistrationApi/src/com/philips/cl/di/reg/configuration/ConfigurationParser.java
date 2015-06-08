
package com.philips.cl.di.reg.configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigurationParser {

	private final String PRODUCTION = "Production";

	private final String EVALUATION = "Evaluation";

	private final String DEVELOPMENT = "Development";

	private final String REGISTRATION_CLIENT_ID = "RegistrationClientID";

	private final String CAMPAIGN_ID = "CampaignID";

	private final String REGISTRATION_ENVIRONMENT = "RegistrationEnvironment";

	private final String MICROSITE_ID = "MicrositeID";

	private final String SOCIAL_PROVIDERS = "SocialProviders";

	private final String PIL_CONFIGURATION = "PILConfiguration";

	private final String JAN_RAIN_CONFIGURATION = "JanRainConfiguration";

	public RegistrationConfiguration parse(JSONObject configurationJson) {
		RegistrationConfiguration registrationConfiguration = new RegistrationConfiguration();
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
			if (!configurationJson.isNull(SOCIAL_PROVIDERS)) {
				JSONObject socialProviders = configurationJson.getJSONObject(SOCIAL_PROVIDERS);
				registrationConfiguration.setSocialProviders(parseSocialProviders(socialProviders));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return registrationConfiguration;
	}

	private SocialProviders parseSocialProviders(JSONObject socialProviders)
	        throws JSONException {
		SocialProviders providers = new SocialProviders();
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
		configuration.setClientIds(registrationClientId);
		return configuration;
	}
}
