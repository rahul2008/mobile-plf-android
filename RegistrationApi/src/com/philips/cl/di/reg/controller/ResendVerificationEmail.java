package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.dao.ResendMailFailureInfo;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.ResendVerificationEmailHandler;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class ResendVerificationEmail implements CaptureApiRequestCallback {

	private ResendVerificationEmailHandler mResendVerificationEmail;

	public ResendVerificationEmail(
			ResendVerificationEmailHandler resendVerificationEmail) {
		mResendVerificationEmail = resendVerificationEmail;
	}

	public void onSuccess() {
		mResendVerificationEmail.onResendVerificationEmailSuccess();

	}

	public void onFailure(CaptureApiError error) {

		ResendMailFailureInfo resendMailFailureInfo = new ResendMailFailureInfo();
		resendMailFailureInfo.setError(error);
		handleInvalidInputs(error, resendMailFailureInfo);
		handleInvalidCredentials(error, resendMailFailureInfo);
		FailureErrorMaping errorMapping = new FailureErrorMaping(null, error,
				null);
		int getError = errorMapping.checkCaptureApiError();
		resendMailFailureInfo.setErrorCode(getError);
		mResendVerificationEmail
				.onResendVerificationEmailFailedWithError(resendMailFailureInfo);
	}

	private void handleInvalidInputs(CaptureApiError error,
			ResendMailFailureInfo resendMailFailureInfo) {
		if (null != error && null != error.error
				&& error.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
			try {
				JSONObject object = error.raw_response;
				JSONObject jsonObject = (JSONObject) object
						.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject
							.isNull(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)) {
						resendMailFailureInfo
								.setEmailErrorMessage(getErrorMessage(jsonObject
										.getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)));
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleInvalidCredentials(CaptureApiError error,
			ResendMailFailureInfo resendMailFailureInfo) {
		if (null != error && null != error.error
				&& error.error.equals(RegConstants.INVALID_CREDENTIALS)) {
			try {
				JSONObject object = error.raw_response;
				JSONObject jsonObject = (JSONObject) object
						.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject
							.isNull(RegConstants.RESEND_VERIFICATION_FORM)) {
						resendMailFailureInfo
								.setEmailErrorMessage(getErrorMessage(jsonObject
										.getJSONArray(RegConstants.RESEND_VERIFICATION_FORM)));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private String getErrorMessage(JSONArray jsonArray) throws JSONException {
		if (null == jsonArray) {
			return null;
		}
		return (String) jsonArray.get(0);
	}
}
