package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.ForgotPasswordHandler;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class ForgotPassword implements Jump.ForgotPasswordResultHandler {
    
	private ForgotPasswordHandler mForgotPaswordHandler;

    public ForgotPassword(ForgotPasswordHandler forgotPaswordHandler) {
		mForgotPaswordHandler = forgotPaswordHandler;
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
		FailureErrorMaping errorMapping = new FailureErrorMaping(null, null, error);
		int getError = errorMapping.checkFogetPassWordError();
		userRegistrationFailureInfo.setErrorCode(getError);
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
		//{"stat":"error","code":540,"error_description":"an error was triggered in the flow","error":"triggered_error","request_id":"wy9phq44563h722z","message":"that account is social signin only"}
		
		if (null != error && null != error.error
		        && error.code==RegConstants.ONLY_SOCIAL_SIGN_IN_ERROR_CODE) {
			try {
				JSONObject object = error.raw_response;
				
					if (!object.isNull("message")) {
						userRegistrationFailureInfo.setSocialOnlyError(object.getString("message"));
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
