
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import com.janrain.android.Jump.SignInResultHandler.SignInError;

/**
 *  Class Sign in social failure info
 */
public class SignInSocialFailureInfo {


	/* Error code */
	private int mErrorCode;

	/* Email error code */
	private String mEmailErrorMessage;

	/* Error */
	private SignInError mError;

	/* Display name error message */
	private String mDisplayNameErrorMessage;

	/**
	 * {@code getDisplayNameErrorMessage } method to get dispaly name error message
	 * @return mDispalyNameErrorMessage display error message
     */
	public String getDisplayNameErrorMessage() {
		return mDisplayNameErrorMessage;
	}

	/**
	 *{@code setDisplayNameErrorMessage} method to set display name error message
	 * @param displayNameErrorMessage    display name error message
     */
    public void setDisplayNameErrorMessage(String displayNameErrorMessage) {
		this.mDisplayNameErrorMessage = displayNameErrorMessage;
	}

	/**
	 *  {@code getError} method to get sign in error
	 *  {@link SignInError}
	 * @return mError error
     */
	public SignInError getError() {
		return mError;
	}

	/**
	 * {@code setError} method to set error
	 * @param error error
     */
	public void setError(SignInError error) {
		this.mError = error;
	}

	/**
	 * {@code getErrorDescription}method to get error description
	 * @return error_description error description if available else null
	 */
	public String getErrorDescription() {
		if (null != mError && null != mError.captureApiError) {
			return mError.captureApiError.error_description;
		}

		return null;
	}

	/**
	 * {@code getEmailErrorMessage} method to Get email error message
	 * @return mEmailErrorMessage Email error message
     */
	public String getEmailErrorMessage() {
		return mEmailErrorMessage;
	}

	/**
	 * {@code setEmailErrorMessage }method to set email error message
	 * @param emailErrorMessage Email error message
     */
	public void setEmailErrorMessage(String emailErrorMessage) {
		this.mEmailErrorMessage = emailErrorMessage;
	}

	/**
	 * {@code getErrorCode} method to get error code
	 * @return mErrorCode error code
     */
	public int getErrorCode() {
		return mErrorCode;
	}

	/**
	 * {@code setErrorCode} method to set error code
	 * @param errorCode Error code
     */
	public void setErrorCode(int errorCode) {
		this.mErrorCode = errorCode;
	}

}
