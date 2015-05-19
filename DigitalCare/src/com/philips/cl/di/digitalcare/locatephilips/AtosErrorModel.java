package com.philips.cl.di.digitalcare.locatephilips;
/**
 *	AtosErrorModel is bean class for error.
 * 
 * @author: ritesh.jha@philips.com
 * 
 * @since: 9 May 2015
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
