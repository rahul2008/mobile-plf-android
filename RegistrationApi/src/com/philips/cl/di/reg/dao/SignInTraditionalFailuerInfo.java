package com.philips.cl.di.reg.dao;

import com.janrain.android.Jump.SignInResultHandler.SignInError;

public class SignInTraditionalFailuerInfo {

	private int errorCode;

	private String emailErrorMessage;

	private String passwordErrorMessage;

	private SignInError error;

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

	public String getPasswordErrorMessage() {
		return passwordErrorMessage;
	}

	public void setPasswordErrorMessage(String passwordErrorMessage) {
		this.passwordErrorMessage = passwordErrorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
