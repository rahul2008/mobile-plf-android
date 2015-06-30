
package com.philips.cl.di.reg.dao;

import com.janrain.android.capture.CaptureApiError;

public class UserRegistrationFailureInfo {

	private int errorCode;

	private String errorDescription;

	private String firstNameErrorMessage;

	private String emailErrorMessage;

	private String passwordErrorMessage;

	private String socialOnlyError;

	private String displayNameErrorMessage;

	public String getDisplayNameErrorMessage() {
		return displayNameErrorMessage;
	}

	public void setDisplayNameErrorMessage(String displayNameErrorMessage) {
		this.displayNameErrorMessage = displayNameErrorMessage;
	}

	public String getSocialOnlyError() {
		return socialOnlyError;
	}

	public void setSocialOnlyError(String socialOnlyError) {
		this.socialOnlyError = socialOnlyError;
	}

	private CaptureApiError error;

	public String getErrorDescription() {
		if (null != error) {
			return error.error_description;
		}
		return null;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getFirstNameErrorMessage() {
		return firstNameErrorMessage;
	}

	public void setFirstNameErrorMessage(String firstNameErrorMessage) {
		this.firstNameErrorMessage = firstNameErrorMessage;
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

	public CaptureApiError getError() {
		return error;
	}

	public void setError(CaptureApiError error) {
		this.error = error;
	}
}
