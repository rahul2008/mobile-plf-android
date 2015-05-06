
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
		handleInvalidInputs(error, createAccountFailuerInfo);
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null, null);
		int errorCondition = errorMapping.checkSignInError();
		createAccountFailuerInfo.setErrorCode(errorCondition);
		mTraditionalRegisterHandler.onRegisterFailedWithFailure(createAccountFailuerInfo);
	}

	private void handleInvalidInputs(SignInError error,
	        CreateAccountFailuerInfo createAccountFailuerInfo) {
		if (null != error.captureApiError && null != error.captureApiError.error
		        && error.captureApiError.error.equals("invalid_form_fields")) {
			try {
				JSONObject object = error.captureApiError.raw_response;
				JSONObject jsonObject = (JSONObject) object.get("invalid_fields");
				if (jsonObject != null) {

					if (!jsonObject.isNull("traditionalRegistration_firstName")) {
						createAccountFailuerInfo
						        .setFirstNameErrorMessage(getErrorMessage(jsonObject
						                .getJSONArray("traditionalRegistration_firstName")));
					}

					if (!jsonObject.isNull("traditionalRegistration_emailAddress")) {
						createAccountFailuerInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray("traditionalRegistration_emailAddress")));
					}

					if (!jsonObject.isNull("traditionalRegistration_password")) {
						createAccountFailuerInfo.setPasswordErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray("traditionalRegistration_password")));
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
