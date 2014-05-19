package com.philips.cl.di.reg.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import com.janrain.android.Jump;
import com.janrain.android.capture.Capture.InvalidApidChangeException;
import com.janrain.android.capture.CaptureRecord;
import com.janrain.android.capture.Capture;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;

public class RegisterTraditional implements Jump.SignInResultHandler,Jump.SignInCodeHandler {
	private Context context;
	private CaptureRecord user;
	private TraditionalRegistrationHandler traditionalRegisterHandler;

	public RegisterTraditional(TraditionalRegistrationHandler traditionalRegisterHandler,Context context) {
		this.traditionalRegisterHandler = traditionalRegisterHandler;
		this.context = context;
		
	}
	@Override
	public void onSuccess() {
		Jump.saveToDisk(context);
		user = Jump.getSignedInUser();
		
		SharedPreferences myPrefs = context.getSharedPreferences("MyPref", 0);
        String microSiteId = myPrefs.getString("microSiteId", null);
        
		
		String preferredLanguage  = Locale.getDefault().getDisplayLanguage();
		String country = context.getResources().getConfiguration().locale.getDisplayCountry();
		
		//visitedMicroSites
		try {
			
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDate = sdf.format(c.getTime());
			
		JSONObject visitedMicroSitesObject = new JSONObject();
		visitedMicroSitesObject.put("microSiteID", microSiteId);
		visitedMicroSitesObject.put("timestamp", currentDate);
		JSONArray visitedMicroSitesArray = new JSONArray();
		visitedMicroSitesArray.put(visitedMicroSitesObject);
		//roles
		JSONObject rolesObject = new JSONObject();
        rolesObject.put("role", "consumer");
        rolesObject.put("role_assigned", currentDate);
        JSONArray rolesArray = new JSONArray();
		rolesArray.put(rolesObject);
		//PrimaryAddress
		JSONObject primaryAddressObject = new JSONObject();
		primaryAddressObject.put("country", country);
		primaryAddressObject.put("address1", "");
		primaryAddressObject.put("address2", "");
		primaryAddressObject.put("address3", "");
		primaryAddressObject.put("city", "");
		primaryAddressObject.put("company", "");
		primaryAddressObject.put("dayTimePhoneNumber", "");
		primaryAddressObject.put("houseNumber", "");
		primaryAddressObject.put("mobile", "");
		primaryAddressObject.put("phone", "");
		primaryAddressObject.put("state", "");
		primaryAddressObject.put("zip", "");
		primaryAddressObject.put("zipPlus4", "");

		JSONArray primaryAddressArray = new JSONArray();
		primaryAddressArray.put(primaryAddressObject);
		user.put("visitedMicroSites", visitedMicroSitesArray);
		user.put("roles", rolesArray);
		user.put("preferredLanguage", preferredLanguage);
		user.put("primaryAddress", primaryAddressObject);
		
		try{
		user.synchronize(new Capture.CaptureApiRequestCallback() {
			
			@Override
			public void onSuccess() {
				
				
			}
			
			@Override
			public void onFailure(CaptureApiError e) {
				
				
			}
		});

		}
		catch (InvalidApidChangeException e) {
			
			e.printStackTrace();
		}
		
		}
		catch (JSONException e) {
			throw new RuntimeException("Unexpected ", e);
		}
		this.traditionalRegisterHandler.onRegisterSuccess();
		}
	@Override
	public void onCode(String code) {
		
		}

	@Override
	public void onFailure(SignInError error) {
		FailureErrorMaping ea = new FailureErrorMaping(error, null, null);
		int errorCondition = ea.checkSignInError();
		this.traditionalRegisterHandler.onRegisterFailedWithFailure(errorCondition);
	}

}
