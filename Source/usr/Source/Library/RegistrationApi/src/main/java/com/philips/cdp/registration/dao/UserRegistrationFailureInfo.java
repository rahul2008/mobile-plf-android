
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegUtility;

import org.json.JSONObject;

/**
 * Class user registration failure info
 */
public class UserRegistrationFailureInfo {
    private final String TAG = UserRegistrationFailureInfo.class.getSimpleName();

    /* Error code */
    private int errorCode;

    /* Error description*/
    private String errorDescription;

    /* error */
    private CaptureApiError error;

    public void setErrorTagging(String errorTagging) {
        this.errorTagging = errorTagging;
    }

    private void setErrorTagging() {
        if (null != error && null != error.raw_response)
            this.errorTagging = RegUtility.getTaggingErrorDescription(error.raw_response);
    }

    private String errorTagging = "";

    public UserRegistrationFailureInfo() {

    }

    /**
     * {@code setError} constructor to set error
     * {@link com.janrain.android.capture.CaptureApiError}
     *
     * @param error error
     * @since 1.0.0
     */
    public UserRegistrationFailureInfo(CaptureApiError error) {
        this.error = error;
        setErrorTagging();
    }


    /**
     * {@code getErrorDescription } method to get error description
     *
     * @return errorDescription error description
     * @since 1.0.0
     */
    public String getErrorDescription() {
        if (null != error) {
            JSONObject response = error.raw_response;
            String message = RegUtility.getErrorMessageFromInvalidField(response);
            if (message != null && !message.isEmpty()) {
                return message;
            }
            RLog.d(TAG, "getErrorDescription : " + error.error_description);
            return error.error_description;
        } else {
            RLog.d(TAG, "getErrorDescription as error is null : " + errorDescription);
            return errorDescription;
        }
    }

    /**
     * {@code getErrorDescription } method to get error non localized description
     *
     * @return non localized description error description String
     * @since 18.1.0
     */

    public String getErrorTagging() {
        RLog.d(TAG, "getErrorTagging : " + this.errorTagging);
        return this.errorTagging;
    }

    /**
     * {@code setErrorDescription}method do set error description
     *
     * @param errorDescription error description
     * @since 1.0.0
     */
    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    /**
     * {@code getErrorCode} method to get error code
     *
     * @return errorCode error code
     * @since 1.0.0
     */
    public int getErrorCode() {
        RLog.d(TAG, "getErrorCode : " + this.errorCode);
        return errorCode;
    }

    /**
     * {@code setErrorCode } method to set error code
     *
     * @param errorCode error code
     * @since 1.0.0
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * {@code getError}method to get captured api error
     * {@link com.janrain.android.capture.CaptureApiError}
     *
     * @return error
     * @since 1.0.0
     */
    public CaptureApiError getError() {
        RLog.d(TAG, "getError : " + error);
        return error;
    }

    /**
     * {@code setError} method to set error
     * {@link com.janrain.android.capture.CaptureApiError}
     *
     * @param error error
     * @since 1.0.0
     */


}
