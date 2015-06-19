
package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class ContinueSocialProviderLogin implements Jump.SignInResultHandler,
        Jump.SignInCodeHandler {

	private SocialProviderLoginHandler mSocialProviderLoginHandler;

	private Context mContext;

	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public ContinueSocialProviderLogin(SocialProviderLoginHandler socialProviderLoginHandler,
	        Context context, UpdateUserRecordHandler updateUserRecordHandler) {
		mSocialProviderLoginHandler = socialProviderLoginHandler;
		mContext = context;
		mUpdateUserRecordHandler = updateUserRecordHandler;
	}

	public void onSuccess() {
		Jump.saveToDisk(mContext);
		User user = new User(mContext);
		user.buildCoppaConfiguration();
		mUpdateUserRecordHandler.updateUserRecordRegister();
		mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess();
	}

	public void onCode(String code) {
	}

	public void onFailure(SignInError error) {
		UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
		userRegistrationFailureInfo.setError(error.captureApiError);
		handleInvalidInputs(error.captureApiError, userRegistrationFailureInfo);
		userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
		mSocialProviderLoginHandler
		        .onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
	}

	private void handleInvalidInputs(CaptureApiError error,
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		if (null != error && null != error.error
		        && error.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
			try {
				JSONObject object = error.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.SOCIAL_REGISTRATION_EMAIL_ADDRESS)) {
						userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.SOCIAL_REGISTRATION_EMAIL_ADDRESS)));
					}
					if (!jsonObject.isNull(RegConstants.SOCIAL_REGISTRATION_DISPLAY_NAME)) {
						userRegistrationFailureInfo
						        .setDisplayNameErrorMessage(getErrorMessage(jsonObject
						                .getJSONArray(RegConstants.SOCIAL_REGISTRATION_DISPLAY_NAME)));
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
