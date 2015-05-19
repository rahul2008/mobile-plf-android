package com.philips.cl.di.reg.dao;

import com.janrain.android.Jump.SignInResultHandler.SignInError;

public class SignInSocialFailureInfo {

	private int errorCode;

	private String emailErrorMessage;

	private SignInError error;
	
	private String displayNameErrorMessage;
	
	public String getDisplayNameErrorMessage() {
		return displayNameErrorMessage;
	}

	public void setDisplayNameErrorMessage(String displayNameErrorMessage) {
		this.displayNameErrorMessage = displayNameErrorMessage;
	}

	public SignInError getError() {
		return error;
	}

	public void setError(SignInError error) {
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
