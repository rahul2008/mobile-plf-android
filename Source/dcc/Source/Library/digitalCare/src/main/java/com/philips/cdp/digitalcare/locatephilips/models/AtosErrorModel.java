package com.philips.cdp.digitalcare.locatephilips.models;
/**
 *	AtosErrorModel is bean class for error.
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 */
public class AtosErrorModel {
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
