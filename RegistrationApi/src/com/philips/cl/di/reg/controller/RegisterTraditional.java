
package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;
import com.philips.cl.di.reg.handlers.TraditionalRegistrationHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class RegisterTraditional implements Jump.SignInResultHandler, Jump.SignInCodeHandler {

	private Context mContext;

	private TraditionalRegistrationHandler mTraditionalRegisterHandler;

	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public RegisterTraditional(TraditionalRegistrationHandler traditionalRegisterHandler,
	        Context context, UpdateUserRecordHandler updateUserRecordHandler) {
		mTraditionalRegisterHandler = traditionalRegisterHandler;
		mContext = context;
		mUpdateUserRecordHandler = updateUserRecordHandler;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(mContext);
		mUpdateUserRecordHandler.updateUserRecordRegister();
		mTraditionalRegisterHandler.onRegisterSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {
		UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
		userRegistrationFailureInfo.setError(error.captureApiError);
		handleInvalidInputs(error.captureApiError, userRegistrationFailureInfo);
		userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
		mTraditionalRegisterHandler.onRegisterFailedWithFailure(userRegistrationFailureInfo);
	}

	private void handleInvalidInputs(CaptureApiError error,
			UserRegistrationFailureInfo userRegistrationFailureInfo) {
		if (null != error && null != error.error
		        && error.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
			try {
				JSONObject object = error.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_FIRST_NAME)) {
						userRegistrationFailureInfo
						        .setFirstNameErrorMessage(getErrorMessage(jsonObject
						                .getJSONArray(RegConstants.TRADITIONAL_REGISTRATION_FIRST_NAME)));
					}

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_EMAIL_ADDRESS)) {
						userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.TRADITIONAL_REGISTRATION_EMAIL_ADDRESS)));
					}

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_PASSWORD)) {
						userRegistrationFailureInfo.setPasswordErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.TRADITIONAL_REGISTRATION_PASSWORD)));
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
