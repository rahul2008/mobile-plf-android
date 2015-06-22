
package com.philips.cl.di.reg.coppa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.configuration.RegistrationConfiguration;

public class CoppaConfiguration {

	private static final String CONFIRMATION_COMMUNICATION_TO_SEND_AT = "confirmationCommunicationToSendAt";

	private static final String STORED_AT = "storedAt";

	private static final String ID = "id";

	private static final String LOCALE = "locale";

	private static final String GIVEN = "given";

	private static final String CONFIRMATION_COMMUNICATION_SENT_AT = "confirmationCommunicationSentAt";

	private static final String CONFIRMATION_STORED_AT = "confirmationStoredAt";

	private static final String COMMUNICATION_SENT_AT = "communicationSentAt";

	private static final String MICRO_SITE_ID = "microSiteID";

	private static final String CONFIRMATION_GIVEN = "confirmationGiven";

	private static final String NULL = "null";

	private static final String CAMPAIGN_ID = "campaignId";

	private static final String CONSENTS = "consents";

	private static final String COPPA_COMMUNICATION_SENT_AT = "coppaCommunicationSentAt";

	private static String coppaCommunicationSentAt;

	private static Consent consent;

	public static Consent getConsent() {
		return consent;
	}

	public static String getCoppaCommunicationSentAt() {
		return coppaCommunicationSentAt;
	}

	public static void clearConfiguration() {
		coppaCommunicationSentAt = null;
		consent = null;
	}

	public static void getCoopaConfigurationFlields(JSONObject jsonObject) {

	
		
		if (!jsonObject.isNull(COPPA_COMMUNICATION_SENT_AT)) {
			coppaCommunicationSentAt = (String) jsonObject.opt(COPPA_COMMUNICATION_SENT_AT);
			System.out.println("consent sent @ : "+coppaCommunicationSentAt);
		}

		if (!jsonObject.isNull(CONSENTS)) {
			
			JSONArray consents = (JSONArray) Jump.getSignedInUser().opt(CONSENTS);
			System.out.println("consent : "+consents);
			consent = new Consent();
			for (int i = 0; i < consents.length(); i++) {
				JSONObject consentObj;
				try {
					consentObj = consents.getJSONObject(i);
					if (!consentObj.isNull(CAMPAIGN_ID)
					        && !consentObj.optString(CAMPAIGN_ID).equalsIgnoreCase(NULL)
					        && consentObj.optString(CAMPAIGN_ID).equalsIgnoreCase(
					                RegistrationConfiguration.getInstance().getPilConfiguration()
					                        .getCampaignID())) {
						consent.setConfirmationGiven(consentObj.optString(CONFIRMATION_GIVEN));
						consent.setMicroSiteID(consentObj.optString(MICRO_SITE_ID));
						consent.setCommunicationSentAt(consentObj.optString(COMMUNICATION_SENT_AT));
						consent.setConfirmationStoredAt(consentObj
						        .optString(CONFIRMATION_STORED_AT));
						consent.setConfirmationCommunicationSentAt(consentObj
						        .optString(CONFIRMATION_COMMUNICATION_SENT_AT));
						consent.setCampaignId(consentObj.optString(CAMPAIGN_ID));
						consent.setGiven(consentObj.optString(GIVEN));
						consent.setLocale(consentObj.optString(LOCALE));
						consent.setId(consentObj.optString(ID));
						consent.setStoredAt(consentObj.optString(STORED_AT));
						consent.setConfirmationCommunicationToSendAt(consentObj
						        .optString(CONFIRMATION_COMMUNICATION_TO_SEND_AT));
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
