package com.philips.cl.di.reg.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.Capture.InvalidApidChangeException;
import com.janrain.android.capture.CaptureApiError;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;

public class LoginTraditional implements Jump.SignInResultHandler,Jump.SignInCodeHandler {
	private Context context;
	private CaptureRecord user;
	private TraditionalLoginHandler traditionalLoginHandler;

	public LoginTraditional(TraditionalLoginHandler traditionalLoginHandler,
		   Context context, String email, String password) {
		this.traditionalLoginHandler = traditionalLoginHandler;
		this.context = context;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(context);
		user = Jump.getSignedInUser();
		
		try {
			
			SharedPreferences myPrefs = context.getSharedPreferences("MyPref", 0);
	        String microSiteId = myPrefs.getString("microSiteId", null);
	       
			
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDate = sdf.format(c.getTime());

			JSONObject visitedMicroSitesObject = new JSONObject();
			visitedMicroSitesObject.put("microSiteID", microSiteId);
			visitedMicroSitesObject.put("timestamp",currentDate);
			JSONArray visitedMicroSitesArray = new JSONArray();
			
			visitedMicroSitesArray.put(visitedMicroSitesObject);
			user.put("visitedMicroSites", visitedMicroSitesArray);
			
			try {
				user.synchronize(new Capture.CaptureApiRequestCallback() {
					@Override
					public void onSuccess() {

					}

					@Override
					public void onFailure(CaptureApiError e) {

					}
				});

			} catch (InvalidApidChangeException e) {

				e.printStackTrace();
			}
		} catch (JSONException e) {
			throw new RuntimeException("Unexpected ", e);
		}
		this.traditionalLoginHandler.onLoginSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {
		FailureErrorMaping ea = new FailureErrorMaping(error, null, null);
		int errorCondition = ea.checkSignInError();
		this.traditionalLoginHandler.onLoginFailedWithError(errorCondition);
	}
}
