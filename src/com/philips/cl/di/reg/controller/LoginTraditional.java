package com.philips.cl.di.reg.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.settings.JanrainConfigurationSettings;

public class LoginTraditional implements Jump.SignInResultHandler,
		Jump.SignInCodeHandler {
	private Context mContext;
	private TraditionalLoginHandler mTraditionalLoginHandler;
	
	private String CONSUMER_TIMESTAMP = "timestamp";
	
	private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	private String CONSUMER_VISITED_MICROSITE_IDS = "visitedMicroSites";
	
	private String LOG_TAG = "LoginTraditional";

	public LoginTraditional(TraditionalLoginHandler traditionalLoginHandler,
			Context context, String email, String password) {
		mTraditionalLoginHandler = traditionalLoginHandler;
		mContext = context;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(mContext);
        CaptureRecord user = Jump.getSignedInUser();
		
		SharedPreferences myPrefs = mContext.getSharedPreferences(JanrainConfigurationSettings.REGISTRATION_API_PREFERENCE, 0);
        String microSiteId = myPrefs.getString(JanrainConfigurationSettings.MICROSITE_ID, null);
		
		try {
			
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			String currentDate = sdf.format(c.getTime());

			JSONObject visitedMicroSitesObject = new JSONObject();
			visitedMicroSitesObject.put(JanrainConfigurationSettings.MICROSITE_ID, microSiteId);
			visitedMicroSitesObject.put(CONSUMER_TIMESTAMP, currentDate);
			JSONArray visitedMicroSitesArray = new JSONArray();

			visitedMicroSitesArray.put(visitedMicroSitesObject);
			user.put(CONSUMER_VISITED_MICROSITE_IDS, visitedMicroSitesArray);

			try {
				JSONObject jsonObject = CaptureRecord.loadFromDisk(mContext);
				user.synchronize(new Capture.CaptureApiRequestCallback() {
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
		mTraditionalLoginHandler.onLoginSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {
		FailureErrorMaping ea = new FailureErrorMaping(error, null, null);
		int errorCondition = ea.checkSignInError();
		mTraditionalLoginHandler.onLoginFailedWithError(errorCondition);
	}
}
