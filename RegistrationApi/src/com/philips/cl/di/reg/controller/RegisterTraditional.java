
package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.dao.CreateAccountFailuerInfo;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
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
		CreateAccountFailuerInfo createAccountFailuerInfo = new CreateAccountFailuerInfo();
		createAccountFailuerInfo.setError(error);
		handleInvalidInputs(error, createAccountFailuerInfo);
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null, null);
		int errorCondition = errorMapping.checkSignInError();
		createAccountFailuerInfo.setErrorCode(errorCondition);
		mTraditionalRegisterHandler.onRegisterFailedWithFailure(createAccountFailuerInfo);
	}

	private void handleInvalidInputs(SignInError error,
	        CreateAccountFailuerInfo createAccountFailuerInfo) {
		if (null != error.captureApiError && null != error.captureApiError.error
		        && error.captureApiError.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
			try {
				JSONObject object = error.captureApiError.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_FIRST_NAME)) {
						createAccountFailuerInfo
						        .setFirstNameErrorMessage(getErrorMessage(jsonObject
						                .getJSONArray(RegConstants.TRADITIONAL_REGISTRATION_FIRST_NAME)));
					}

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_EMAIL_ADDRESS)) {
						createAccountFailuerInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.TRADITIONAL_REGISTRATION_EMAIL_ADDRESS)));
					}

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_REGISTRATION_PASSWORD)) {
						createAccountFailuerInfo.setPasswordErrorMessage(getErrorMessage(jsonObject
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
