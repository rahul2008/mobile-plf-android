package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.dao.SignInSocialFailureInfo;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class ContinueSocialProviderLogin implements Jump.SignInResultHandler,
		Jump.SignInCodeHandler {
	private SocialProviderLoginHandler mSocialProviderLoginHandler;
	private Context mContext;
	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public ContinueSocialProviderLogin(
			SocialProviderLoginHandler socialProviderLoginHandler,
			Context context,UpdateUserRecordHandler updateUserRecordHandler) {
		mSocialProviderLoginHandler = socialProviderLoginHandler;
		mContext = context;
		mUpdateUserRecordHandler = updateUserRecordHandler;
	}

	public void onSuccess() {
		Jump.saveToDisk(mContext);
		mUpdateUserRecordHandler.updateUserRecordRegister();
		mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess();
	}

	public void onCode(String code) {
	}

	public void onFailure(SignInError error) 
	{
		SignInSocialFailureInfo signInSocialFailureInfo = new SignInSocialFailureInfo();
		signInSocialFailureInfo.setError(error);
		handleInvalidInputs(error, signInSocialFailureInfo);
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null, null);
		int errorCondition = errorMapping.checkSignInError();
		signInSocialFailureInfo.setErrorCode(errorCondition);
		mSocialProviderLoginHandler.onContinueSocialProviderLoginFailure(signInSocialFailureInfo);
		
	}
	
	private void handleInvalidInputs(SignInError error,
			SignInSocialFailureInfo signInSocialFailureInfo) {
		if (null != error.captureApiError && null != error.captureApiError.error
		        && error.captureApiError.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
			try {
				JSONObject object = error.captureApiError.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull("socialRegistration_emailAddress")) {
						signInSocialFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray("socialRegistration_emailAddress")));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}
	
	private String getErrorMessage(JSONArray jsonArray)
	        throws JSONException {
		if (null == jsonArray) {
			return null;
		}
		return (String) jsonArray.get(0);
	}
}
