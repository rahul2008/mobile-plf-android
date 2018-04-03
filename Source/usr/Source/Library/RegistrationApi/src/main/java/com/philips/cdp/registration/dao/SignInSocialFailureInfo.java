
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import com.janrain.android.Jump.SignInResultHandler.SignInError;
import com.philips.cdp.registration.ui.utils.RLog;

/**
 * Class Sign in social failure info
 */
public class SignInSocialFailureInfo {

    private final String TAG = SignInSocialFailureInfo.class.getSimpleName();

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
     *
     * @return mDispalyNameErrorMessage display error message
     */
    public String getDisplayNameErrorMessage() {
        RLog.d(TAG, "getDisplayNameErrorMessage : " + mDisplayNameErrorMessage);
        return mDisplayNameErrorMessage;
    }

    /**
     * {@code setDisplayNameErrorMessage} method to set display name error message
     *
     * @param displayNameErrorMessage display name error message
     */
    public void setDisplayNameErrorMessage(String displayNameErrorMessage) {
        this.mDisplayNameErrorMessage = displayNameErrorMessage;
    }

    /**
     * {@code getError} method to get sign in error
     * {@link SignInError}
     *
     * @return mError error
     */
    public SignInError getError() {
        RLog.d(TAG, "getError : " + mError);
        return mError;
    }

    /**
     * {@code setError} method to set error
     *
     * @param error error
     */
    public void setError(SignInError error) {
        this.mError = error;
    }

    /**
     * {@code getErrorDescription}method to get error description
     *
     * @return error_description error description if available else null
     */
    public String getErrorDescription() {
        if (null != mError && null != mError.captureApiError) {
            RLog.d(TAG, "getErrorDescription : " + mError.captureApiError.error_description);
            return mError.captureApiError.error_description;
        }
        RLog.d(TAG, "getErrorDescription with NULL");
        return null;
    }

    /**
     * {@code getEmailErrorMessage} method to Get email error message
     *
     * @return mEmailErrorMessage Email error message
     */
    public String getEmailErrorMessage() {
        RLog.d(TAG, "getEmailErrorMessage " + mEmailErrorMessage);
        return mEmailErrorMessage;
    }

    /**
     * {@code setEmailErrorMessage }method to set email error message
     *
     * @param emailErrorMessage Email error message
     */
    public void setEmailErrorMessage(String emailErrorMessage) {
        this.mEmailErrorMessage = emailErrorMessage;
    }

    /**
     * {@code getErrorCode} method to get error code
     *
     * @return mErrorCode error code
     */
    public int getErrorCode() {
        RLog.d(TAG, "getErrorCode " + mErrorCode);
        return mErrorCode;
    }

    /**
     * {@code setErrorCode} method to set error code
     *
     * @param errorCode Error code
     */
    public void setErrorCode(int errorCode) {
        this.mErrorCode = errorCode;
    }

}
