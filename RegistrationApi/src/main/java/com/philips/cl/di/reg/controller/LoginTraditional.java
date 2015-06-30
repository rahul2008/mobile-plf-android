
package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.coppa.CoppaConfiguration;
import com.philips.cl.di.reg.coppa.CoppaExtension;
import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;
import com.philips.cl.di.reg.handlers.TraditionalLoginHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
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
		User user = new User(mContext);
		user.buildCoppaConfiguration();
		if(CoppaConfiguration.getCoppaCommunicationSentAt()!=null&& RegistrationHelper.getInstance().isCoppaFlow()){
			CoppaExtension coppaExtension = new CoppaExtension();
			coppaExtension.triggerSendCoppaMailAfterLogin(user.getUserInstance(mContext).getEmail());
		}
		mUpdateUserRecordHandler.updateUserRecordLogin();
		mTraditionalLoginHandler.onLoginSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {
		UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
		userRegistrationFailureInfo.setError(error.captureApiError);
		handleInvalidInputs(error.captureApiError, userRegistrationFailureInfo);
		handleInvalidCredentials(error.captureApiError, userRegistrationFailureInfo);
		userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
		mTraditionalLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo);
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

					if (!jsonObject.isNull(RegConstants.TRADITIONAL_SIGN_IN_PASSWORD)) {
						userRegistrationFailureInfo
						        .setPasswordErrorMessage(getErrorMessage(jsonObject
						                .getJSONArray(RegConstants.TRADITIONAL_SIGN_IN_PASSWORD)));
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

					if (!jsonObject.isNull(RegConstants.USER_INFORMATION_FORM)) {
						userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.USER_INFORMATION_FORM)));
					}

					if (!jsonObject.isNull(RegConstants.USER_INFORMATION_FORM)) {
						userRegistrationFailureInfo
						        .setPasswordErrorMessage(getErrorMessage(jsonObject
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
