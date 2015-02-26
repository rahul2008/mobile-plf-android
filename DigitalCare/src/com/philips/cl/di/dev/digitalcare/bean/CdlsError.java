package com.philips.cl.di.dev.digitalcare.bean;
/*
 *	CdlsError is bean class error.
 * 
 * Author : Ritesh.jha@philips.com
 * 
 * Creation Date : 16 Dec 2015
 */
public class CdlsError {
	private String mErrorCode = null;
	private String mErrorMessage = null;

	public String getErrorCode() {
		return mErrorCode;
	}

	public void setErrorCode(String mErrorCode) {
		this.mErrorCode = mErrorCode;
	}

	public String getErrorMessage() {
		return mErrorMessage;
	}

	public void setErrorMessage(String mErrorMessage) {
		this.mErrorMessage = mErrorMessage;
	}
}
