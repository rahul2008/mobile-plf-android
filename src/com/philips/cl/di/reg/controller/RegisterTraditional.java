package com.philips.cl.di.reg.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.Capture.InvalidApidChangeException;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.settings.JanrainConfigurationSettings;

public class RegisterTraditional implements Jump.SignInResultHandler,
		Jump.SignInCodeHandler {
	private Context mContext;
	
	private TraditionalRegistrationHandler mTraditionalRegisterHandler;

	private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private String CONSUMER_TIMESTAMP = "timestamp";

	private String CONSUMER_VISITED_MICROSITE_IDS = "visitedMicroSites";

	private String CONSUMER_ROLE = "role";

	private String CONSUMER_ROLES = "roles";

	private String CONSUMER_ROLE_ASSIGNED = "role_assigned";

	private String CONSUMER_COUNTRY = "country";

	private String CONSUMER_ADDRESS1 = "address1";

	private String CONSUMER_ADDRESS2 = "address2";

	private String CONSUMER_ADDRESS3 = "address3";

	private String CONSUMER_CITY = "city";

	private String CONSUMER_COMPANY = "company";

	private String CONSUMER_PHONE_NUMBER = "dayTimePhoneNumber";

	private String CONSUMER_HOUSE_NUMBER = "houseNumber";

	private String CONSUMER_MOBILE = "mobile";

	private String CONSUMER_PHONE = "phone";

	private String CONSUMER_STATE = "state";

	private String CONSUMER_ZIP = "zip";
	
	private String CONSUMER_NAME = "consumer";
	
	private String CONSUMER_ZIP_PLUS = "zipPlus4";

	private String CONSUMER_PREFERED_LANGUAGE = "preferredLanguage";

	private String CONSUMER_PRIMARY_ADDRESS = "primaryAddress";
	
	private String LOG_TAG = "RegisterTraditional";

	public RegisterTraditional(
			TraditionalRegistrationHandler traditionalRegisterHandler,
			Context context) {
		mTraditionalRegisterHandler = traditionalRegisterHandler;
		mContext = context;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(mContext);
		
		CaptureRecord mUser = Jump.getSignedInUser();
		
		SharedPreferences myPrefs = mContext.getSharedPreferences(JanrainConfigurationSettings.REGISTRATION_API_PREFERENCE, 0);
        String microSiteId = myPrefs.getString(JanrainConfigurationSettings.MICROSITE_ID, null);
		
		// visitedMicroSites
		try {

			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String currentDate = sdf.format(c.getTime());

			JSONObject visitedMicroSitesObject = new JSONObject();
			visitedMicroSitesObject.put(JanrainConfigurationSettings.MICROSITE_ID, microSiteId);
			visitedMicroSitesObject.put(CONSUMER_TIMESTAMP, currentDate);
			JSONArray visitedMicroSitesArray = new JSONArray();
			visitedMicroSitesArray.put(visitedMicroSitesObject);
			// roles
			JSONObject rolesObject = new JSONObject();
			rolesObject.put(CONSUMER_ROLE, CONSUMER_NAME);
			rolesObject.put(CONSUMER_ROLE_ASSIGNED, currentDate);
			JSONArray rolesArray = new JSONArray();
			rolesArray.put(rolesObject);
			// PrimaryAddress
			JSONObject primaryAddressObject = new JSONObject();
			primaryAddressObject.put(CONSUMER_COUNTRY, JanrainConfigurationSettings.getInstance().getPreferredCountryCode());
			primaryAddressObject.put(CONSUMER_ADDRESS1, "");
			primaryAddressObject.put(CONSUMER_ADDRESS2, "");
			primaryAddressObject.put(CONSUMER_ADDRESS3, "");
			primaryAddressObject.put(CONSUMER_CITY, "");
			primaryAddressObject.put(CONSUMER_COMPANY, "");
			primaryAddressObject.put(CONSUMER_PHONE_NUMBER, "");
			primaryAddressObject.put(CONSUMER_HOUSE_NUMBER, "");
			primaryAddressObject.put(CONSUMER_MOBILE, "");
			primaryAddressObject.put(CONSUMER_PHONE, "");
			primaryAddressObject.put(CONSUMER_STATE, "");
			primaryAddressObject.put(CONSUMER_ZIP, "");
			primaryAddressObject.put(CONSUMER_ZIP_PLUS, "");

			JSONArray primaryAddressArray = new JSONArray();
			primaryAddressArray.put(primaryAddressObject);
			mUser.put(CONSUMER_VISITED_MICROSITE_IDS, visitedMicroSitesArray);
			mUser.put(CONSUMER_ROLES, rolesArray);
			mUser.put(CONSUMER_PREFERED_LANGUAGE, JanrainConfigurationSettings.getInstance().getPreferredLangCode());
			mUser.put(CONSUMER_PRIMARY_ADDRESS, primaryAddressObject);
			

			try {
				JSONObject jsonObject = CaptureRecord.loadFromDisk(mContext);
				mUser.synchronize(new Capture.CaptureApiRequestCallback() {

					@Override
					public void onSuccess() {

					}

					@Override
					public void onFailure(CaptureApiError e) {

					}
				}, jsonObject);

			} catch (InvalidApidChangeException e) {

				e.printStackTrace();
			}

		} catch (JSONException e) {
			Log.e(LOG_TAG,"On success, Caught JSON Exception");
		}
		mTraditionalRegisterHandler.onRegisterSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null, null);
		int errorCondition = errorMapping.checkSignInError();
		mTraditionalRegisterHandler.onRegisterFailedWithFailure(errorCondition);
	}

}
