package com.philips.cl.di.reg.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.dao.ForgotPasswordFailureInfo;
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
		ForgotPasswordFailureInfo forgotPasswordFailureInfo = new ForgotPasswordFailureInfo();
		forgotPasswordFailureInfo.setError(error);
		handleAccountExistance(error, forgotPasswordFailureInfo);
		FailureErrorMaping errorMapping = new FailureErrorMaping(null, null, error);
		int getError = errorMapping.checkFogetPassWordError();
		forgotPasswordFailureInfo.setErrorCode(getError);
		mForgotPaswordHandler.onSendForgotPasswordFailedWithError(forgotPasswordFailureInfo);
		
		}
	
	private void handleAccountExistance(ForgetPasswordError error,
			ForgotPasswordFailureInfo forgotPasswordFailureInfo) {
		if (null != error.captureApiError && null != error.captureApiError.error
		        && error.captureApiError.error.equals(RegConstants.NO_SUCH_ACCOUNT)) {
			try {
				JSONObject object = error.captureApiError.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.FORGOT_PASSWORD_FORM)) {
						forgotPasswordFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.FORGOT_PASSWORD_FORM)));
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
