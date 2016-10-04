
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import com.janrain.android.capture.CaptureApiError;

/**
 * Class user registration failure info
 */
public class UserRegistrationFailureInfo {

    /* Error code */
    private int errorCode;

    /* Error description*/
    private String errorDescription;

    /* first name error message */
    private String firstNameErrorMessage;

    /* email error message */
    private String emailErrorMessage;

    /* password error message */
    private String passwordErrorMessage;

    /* social only error */
    private String socialOnlyError;

    /* display name error message */
    private String displayNameErrorMessage;

    /* error */
    private CaptureApiError error;

    /**
     * Get display name error message
     * @return displayNameErrorMessage display name error message
     */
    public String getDisplayNameErrorMessage() {
        return displayNameErrorMessage;
    }

    /**
     *  {@code displayNameErrorMessage} method to set display name error message
     * @param displayNameErrorMessage display name error message
     */
    public void setDisplayNameErrorMessage(String displayNameErrorMessage) {
        this.displayNameErrorMessage = displayNameErrorMessage;
    }

    /**
     * {@code getSocialOnlyError } method to get social only error
     * @return socialOnlyError social only error
     */
    public String getSocialOnlyError() {
        return socialOnlyError;
    }

    /**
     * {@code setSocialOnlyError} method to Set Social only error
     * @param socialOnlyError social only error
     */
    public void setSocialOnlyError(String socialOnlyError) {
        this.socialOnlyError = socialOnlyError;
    }


    /**
     * {@code getErrorDescription } method to get error description
     * @return errorDescription error description
     */
    public String getErrorDescription() {
        if (null != error) {
            return error.error_description;
        } else {
            return errorDescription;
        }
    }

    /**
     *{@code setErrorDescription}method do set error description
     * @param errorDescription error description
     */
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    /**
     * {@code getErrorCode} method to get error code
     * @return errorCode error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * {@code setErrorCode } method to set error code
     * @param errorCode error code
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * {@code getFirstNameErrorMessage} method to get first name error message
     * @return firstNameErrorMessage first name error message
     */
    public String getFirstNameErrorMessage() {
        return firstNameErrorMessage;
    }

    /**
     *
     * {@code firstNameErrorMessage} method to Set first name error mesage
     * @param firstNameErrorMessage first name error message
     */
    public void setFirstNameErrorMessage(String firstNameErrorMessage) {
        this.firstNameErrorMessage = firstNameErrorMessage;
    }

    /**
     * {@code getEmailErrorMessage } method to get email error message
     * @return emailErrorMessage email error message
     */
    public String getEmailErrorMessage() {
        return emailErrorMessage;
    }

    /**
     * {@code setEmailErrorMessage} method to set email error message
     * @param emailErrorMessage email error message
     */
    public void setEmailErrorMessage(String emailErrorMessage) {
        this.emailErrorMessage = emailErrorMessage;
    }

    /**
     * {@code getPasswordErrorMessage} method to get password error message
     * @return passwordErrorMessage password error message
     */
    public String getPasswordErrorMessage() {
        return passwordErrorMessage;
    }

    /**
     * {@code setPasswordErrorMessage} method to set password error message
     * @param passwordErrorMessage password error message
     */
    public void setPasswordErrorMessage(String passwordErrorMessage) {
        this.passwordErrorMessage = passwordErrorMessage;
    }

    /**
     * {@code getError}method to get captured api error
     *{@link com.janrain.android.capture.CaptureApiError}
     * @return error
     */
    public CaptureApiError getError() {
        return error;
    }

    /**
     * {@code setError} method to set error
     *{@link com.janrain.android.capture.CaptureApiError}
     * @param error error
     */
    public void setError(CaptureApiError error) {
        this.error = error;
    }


}
