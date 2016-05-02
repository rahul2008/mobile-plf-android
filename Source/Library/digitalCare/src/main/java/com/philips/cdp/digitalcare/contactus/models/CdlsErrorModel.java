/**
 *	CdlsErrorModel is bean class for error.
 *
 * @author : Ritesh.jha@philips.com
 *
 * @since : 16 Dec 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.contactus.models;

public class CdlsErrorModel {
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
