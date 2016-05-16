
package com.philips.cdp.registration.controller;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class ForgotPassword implements Jump.ForgotPasswordResultHandler , JumpFlowDownloadStatusListener{

	private ForgotPasswordHandler mForgotPaswordHandler;
	private Context mContext;
	public ForgotPassword(final Context context, ForgotPasswordHandler forgotPaswordHandler) {
		mForgotPaswordHandler = forgotPaswordHandler;
		mContext = context;
	}

	@Override
	public void onSuccess() {
		mForgotPaswordHandler.onSendForgotPasswordSuccess();

	}

	@Override
	public void onFailure(ForgetPasswordError error) {
		UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
		userRegistrationFailureInfo.setError(error.captureApiError);
		handleAccountExistance(error.captureApiError, userRegistrationFailureInfo);
		handleOnlySocialSignIn(error.captureApiError, userRegistrationFailureInfo);
		userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
		mForgotPaswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo);
	}

	private void handleAccountExistance(CaptureApiError error,
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		if (null != error && null != error.error
		        && error.error.equals(RegConstants.NO_SUCH_ACCOUNT)) {
			try {
				JSONObject object = error.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.FORGOT_PASSWORD_FORM)) {
						userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.FORGOT_PASSWORD_FORM)));
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void handleOnlySocialSignIn(CaptureApiError error,
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		if (null != error && null != error.error
		        && error.code == RegConstants.ONLY_SOCIAL_SIGN_IN_ERROR_CODE) {
			try {
				JSONObject object = error.raw_response;

				if (!object.isNull(RegConstants.MESSAGE)) {
					userRegistrationFailureInfo.setSocialOnlyError(object
					        .getString(RegConstants.MESSAGE));
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

	private String mEmailAddress;

	public void performForgotPassword(final String  emailAddress){
		mEmailAddress = emailAddress;
		if(!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
			UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
		}else {
			Jump.performForgotPassword(emailAddress, this);
			UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
			return;
		}
		if(!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()){
			RegistrationHelper.getInstance().initializeUserRegistration(mContext);
		}
	}

	@Override
	public void onFlowDownloadSuccess() {
		Jump.performForgotPassword(mEmailAddress, this);
		UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();
	}

	@Override
	public void onFlowDownloadFailure() {
		if (mForgotPaswordHandler != null) {
			UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
			userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.JanRain_Server_Connection_Failed));
			userRegistrationFailureInfo.setErrorCode(RegConstants.FORGOT_PASSWORD_FAILED_SERVER_ERROR);
			mForgotPaswordHandler.onSendForgotPasswordFailedWithError(userRegistrationFailureInfo);
		}
		UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();


	}
}
