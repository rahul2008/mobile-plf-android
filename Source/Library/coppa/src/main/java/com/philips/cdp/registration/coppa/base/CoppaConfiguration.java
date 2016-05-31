
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.base;

import com.janrain.android.Jump;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 class CoppaConfiguration {



	 public static final String CONFIRMATION_COMMUNICATION_TO_SEND_AT = "confirmationCommunicationToSendAt";

	 public static final String STORED_AT = "storedAt";

	 public static final String ID = "id";

	 public static final String LOCALE = "locale";

	 public static final String GIVEN = "given";

	 public static final String CONFIRMATION_COMMUNICATION_SENT_AT = "confirmationCommunicationSentAt";

	 public static final String CONFIRMATION_STORED_AT = "confirmationStoredAt";

	 public static final String COMMUNICATION_SENT_AT = "communicationSentAt";

	 public static final String MICRO_SITE_ID = "microSiteID";

	 public static final String CONFIRMATION_GIVEN = "confirmationGiven";

	 public static final String NULL = "null";

	 public static final String CAMPAIGN_ID = "campaignId";

	 public static final String CONSENTS = "consents";

	 public static final String COPPA_COMMUNICATION_SENT_AT = "coppaCommunicationSentAt";

	 private static String coppaCommunicationSentAt;

	 private static Consent consent;

	 private static int consentIndex;

	 private static boolean isCampaignIdPresent;

	 public static Consent getConsent() {
		 return consent;
	 }
	 private static JSONArray consents;
	 public static JSONArray getCurrentConsentsArray(){
		 return consents;
	 }

	 public static String getCoppaCommunicationSentAt() {
		 return coppaCommunicationSentAt;
	 }

	 public static void clearConfiguration() {
		 coppaCommunicationSentAt = null;
		 consent = null;
	 }

	 public static int consentIndex(){
		 return consentIndex;
	 }

	 public static boolean isCampaignIdPresent(){
		 return isCampaignIdPresent;
	 }

	 public static void getCoopaConfigurationFlields(JSONObject jsonObject) {
		 consentIndex = 0;
		 isCampaignIdPresent = false;

		 if (!jsonObject.isNull(COPPA_COMMUNICATION_SENT_AT)) {
			 coppaCommunicationSentAt = (String) jsonObject.opt(COPPA_COMMUNICATION_SENT_AT);
			 System.out.println("consent sent @ : "+coppaCommunicationSentAt);
		 }

		 if (!jsonObject.isNull(CONSENTS)) {

			 consents = (JSONArray) Jump.getSignedInUser().opt(CONSENTS);
			 System.out.println("consent : "+consents);
			 consent = new Consent();
			 for (int i = 0; i < consents.length(); i++) {
				 JSONObject consentObj;
				 try {
					 consentObj = consents.getJSONObject(i);
					 if (!consentObj.isNull(CAMPAIGN_ID)
							 && !consentObj.optString(CAMPAIGN_ID).equalsIgnoreCase(NULL)
							 && consentObj.optString(CAMPAIGN_ID).equalsIgnoreCase(
							 RegistrationConfiguration.getInstance().getPilConfiguration().getCampaignID())) {
						 consentIndex = i;
						 isCampaignIdPresent = true;
						 if(!consentObj.optString(CONFIRMATION_GIVEN).equalsIgnoreCase(NULL)) {
							 consent.setConfirmationGiven(consentObj.optString(CONFIRMATION_GIVEN));
						 }
						 if(!consentObj.optString(MICRO_SITE_ID).equalsIgnoreCase(NULL)) {
							 consent.setMicroSiteID(consentObj.optString(MICRO_SITE_ID));
						 }
						 if(!consentObj.optString(COMMUNICATION_SENT_AT).equalsIgnoreCase(NULL)) {
							 consent.setCommunicationSentAt(consentObj.optString(COMMUNICATION_SENT_AT));
						 }
						 if(!consentObj.optString(CONFIRMATION_STORED_AT).equalsIgnoreCase(NULL)) {
							 consent.setConfirmationStoredAt(consentObj
									 .optString(CONFIRMATION_STORED_AT));
						 }
						 if(!consentObj.optString(CONFIRMATION_COMMUNICATION_SENT_AT).equalsIgnoreCase(NULL)) {
							 consent.setConfirmationCommunicationSentAt(consentObj
									 .optString(CONFIRMATION_COMMUNICATION_SENT_AT));
						 }
						 if(!consentObj.optString(CAMPAIGN_ID).equalsIgnoreCase(NULL)) {
							 consent.setCampaignId(consentObj.optString(CAMPAIGN_ID));
						 }
						 if(!consentObj.optString(GIVEN).equalsIgnoreCase(NULL)) {
							 consent.setGiven(consentObj.optString(GIVEN));
						 }
						 if(!consentObj.optString(LOCALE).equalsIgnoreCase(NULL)) {
							 consent.setLocale(consentObj.optString(LOCALE));
						 }
						 if(!consentObj.optString(ID).equalsIgnoreCase(NULL)) {
							 consent.setId(consentObj.optString(ID));
						 }
						 if(!consentObj.optString(STORED_AT).equalsIgnoreCase(NULL)) {
							 consent.setStoredAt(consentObj.optString(STORED_AT));
						 }
						 if(!consentObj.optString(CONFIRMATION_COMMUNICATION_TO_SEND_AT).equalsIgnoreCase(NULL)) {
							 consent.setConfirmationCommunicationToSendAt(consentObj
									 .optString(CONFIRMATION_COMMUNICATION_TO_SEND_AT));
						 }
					 }

				 } catch (JSONException e) {
					 e.printStackTrace();
				 }
			 }
		 }
	 }
}
