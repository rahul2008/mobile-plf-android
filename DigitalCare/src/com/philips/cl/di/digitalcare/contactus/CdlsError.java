package com.philips.cl.di.digitalcare.contactus;
/*
 *	CdlsError is bean class error.
 * 
 * @author : Ritesh.jha@philips.com
 * 
 * @since : 16 Dec 2014
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
