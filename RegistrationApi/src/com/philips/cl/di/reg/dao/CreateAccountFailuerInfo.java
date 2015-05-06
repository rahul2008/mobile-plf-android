
package com.philips.cl.di.reg.dao;

public class CreateAccountFailuerInfo {

	/* kSocialEmailError = @"socialRegistration_emailAddress"; */
	private int errorCode;

	private String firstNameErrorMessage;

	private String emailErrorMessage;

	private String passwordErrorMessage;

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

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
