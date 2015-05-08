package com.philips.cl.di.reg.dao;

import com.janrain.android.capture.CaptureApiError;

public class ResendMailFailureInfo {

	private int errorCode;

	private String emailErrorMessage;

	private CaptureApiError error;

	public CaptureApiError getError() {
		return error;
	}

	public void setError(CaptureApiError error) {
		this.error = error;
	}

	public String getErrorDescription() {
		if (null != error) {
			return error.error_description;
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
