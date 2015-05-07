package com.philips.cl.di.reg.dao;

import com.janrain.android.Jump.ForgotPasswordResultHandler.ForgetPasswordError;

public class ForgotPasswordFailureInfo {

	private int errorCode;

	private String emailErrorMessage;

	private ForgetPasswordError error;

	public ForgetPasswordError getError() {
		return error;
	}

	public void setError(ForgetPasswordError error) {
		this.error = error;
	}

	public String getErrorDescription() {
		if (null != error && null != error.captureApiError) {
			return error.captureApiError.error_description;
		}
		return null;
	}

	public String getEmailErrorMessage() {
		return emailErrorMessage;
	}

	public void setEmailErrorMessage(String emailErrorMessage) {
		this.emailErrorMessage = emailErrorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
