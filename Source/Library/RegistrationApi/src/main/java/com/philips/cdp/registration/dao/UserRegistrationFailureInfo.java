
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.ui.utils.RegUtility;

import org.json.JSONObject;

/**
 * Class user registration failure info
 */
public class UserRegistrationFailureInfo {

    /* Error code */
    private int errorCode;

    /* Error description*/
    private String errorDescription;

    /* error */
    private CaptureApiError error;


    /**
     * {@code getErrorDescription } method to get error description
     * @return errorDescription error description
     */
    public String getErrorDescription() {
        if (null != error) {
            JSONObject response = error.raw_response;
            String message = RegUtility.getErrorMessageFromInvalidField(response);
            if (message != null && !message.isEmpty()) {
                return message;
            }
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
