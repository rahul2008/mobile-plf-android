
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.janrain.android.Jump;
import com.janrain.android.capture.Capture.CaptureApiRequestCallback;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class ResendVerificationEmail implements CaptureApiRequestCallback,JumpFlowDownloadStatusListener {

	private ResendVerificationEmailHandler mResendVerificationEmail;
	private Context mContext;
	private String mEmailAddress;
	public ResendVerificationEmail(final Context context, final ResendVerificationEmailHandler resendVerificationEmail) {
		mResendVerificationEmail = resendVerificationEmail;
		mContext = context;
	}

	public void onSuccess() {
		mResendVerificationEmail.onResendVerificationEmailSuccess();

	}

	public void onFailure(CaptureApiError error) {
		UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
		userRegistrationFailureInfo.setError(error);
		handleInvalidInputs(error, userRegistrationFailureInfo);
		handleInvalidCredentials(error, userRegistrationFailureInfo);
		userRegistrationFailureInfo.setErrorCode(error.code);
		mResendVerificationEmail
		        .onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
	}

	private void handleInvalidInputs(CaptureApiError error,
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		if (null != error && null != error.error
		        && error.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
			try {
				JSONObject object = error.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)) {
						userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_EMAIL_ADDRESS)));
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleInvalidCredentials(CaptureApiError error,
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		if (null != error && null != error.error
		        && error.error.equals(RegConstants.INVALID_CREDENTIALS)) {
			try {
				JSONObject object = error.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.RESEND_VERIFICATION_FORM)) {
						userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.RESEND_VERIFICATION_FORM)));
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

	public void resendVerificationMail(final String emailAddress){
		mEmailAddress = emailAddress;
		if(!UserRegistrationInitializer.getInstance().isJumpInitializated()){
			UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
		}else {
			Jump.resendEmailVerification(emailAddress, this);
			return;
		}

		if(!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()){
			RegistrationHelper.getInstance().initializeUserRegistration(mContext);
		}

	}

	@Override
	public void onFlowDownloadSuccess() {
		Jump.resendEmailVerification(mEmailAddress, this);
		UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

	}

	@Override
	public void onFlowDownloadFailure() {
		if(mResendVerificationEmail != null) {
			UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
			userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
			userRegistrationFailureInfo.setErrorCode(RegConstants.RESEND_MAIL_FAILED_SERVER_ERROR);
			mResendVerificationEmail.onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
		}
		UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();


	}
}
