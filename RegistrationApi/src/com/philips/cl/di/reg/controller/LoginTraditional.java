
package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.dao.SignInTraditionalFailuerInfo;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class LoginTraditional implements Jump.SignInResultHandler, Jump.SignInCodeHandler {

	private Context mContext;

	private TraditionalLoginHandler mTraditionalLoginHandler;

	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public LoginTraditional(TraditionalLoginHandler traditionalLoginHandler, Context context,
	        UpdateUserRecordHandler updateUserRecordHandler, String email, String password) {
		mTraditionalLoginHandler = traditionalLoginHandler;
		mContext = context;
		mUpdateUserRecordHandler = updateUserRecordHandler;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(mContext);
		mUpdateUserRecordHandler.updateUserRecordLogin();
		mTraditionalLoginHandler.onLoginSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {
		SignInTraditionalFailuerInfo signInTraditionalFailuerInfo = new SignInTraditionalFailuerInfo();
		signInTraditionalFailuerInfo.setError(error);
		handleInvalidInputs(error, signInTraditionalFailuerInfo);
		handleInvalidCredentials(error, signInTraditionalFailuerInfo);
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null, null);
		int errorCondition = errorMapping.checkSignInError();
		signInTraditionalFailuerInfo.setErrorCode(errorCondition);
		mTraditionalLoginHandler.onLoginFailedWithError(signInTraditionalFailuerInfo);
	}

	private void handleInvalidInputs(SignInError error,
	        SignInTraditionalFailuerInfo signInTraditionalFailuerInfo) {
		if (null != error.captureApiError && null != error.captureApiError.error
		        && error.captureApiError.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
			try {
				JSONObject object = error.captureApiError.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)) {
						signInTraditionalFailuerInfo
						        .setEmailErrorMessage(getErrorMessage(jsonObject
						                .getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)));
					}

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_SIGN_IN_PASSWORD)) {
						signInTraditionalFailuerInfo
						        .setPasswordErrorMessage(getErrorMessage(jsonObject
						                .getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_PASSWORD)));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void handleInvalidCredentials(SignInError error,
	        SignInTraditionalFailuerInfo signInTraditionalFailuerInfo) {
		if (null != error.captureApiError && null != error.captureApiError.error
		        && error.captureApiError.error.equals(RegConstants.INVALID_CREDENTIALS)) {
			try {
				JSONObject object = error.captureApiError.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.USER_INFORMATION_FORM)) {
						signInTraditionalFailuerInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.USER_INFORMATION_FORM)));
					}

					if (!jsonObject.isNull(RegConstants.USER_INFORMATION_FORM)) {
						signInTraditionalFailuerInfo.setPasswordErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.USER_INFORMATION_FORM)));
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
