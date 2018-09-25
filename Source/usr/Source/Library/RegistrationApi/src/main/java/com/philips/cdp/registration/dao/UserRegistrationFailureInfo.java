
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import android.content.Context;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.errors.ErrorType;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class user registration failure info
 */
public class UserRegistrationFailureInfo {
    private final String TAG = "UserRegistrationFailureInfo";

    private String errorTagging = "";

    private Context mContext;

    /* Error code */
    private int errorCode;

    /* Error description*/
    private String errorDescription;

    /* error */
    private CaptureApiError error;

    public UserRegistrationFailureInfo(Context pContext) {
        mContext = pContext;
    }

    /**
     * {@code setError} constructor to set error
     * {@link com.janrain.android.capture.CaptureApiError}
     *
     * @param error    error
     * @param pContext
     * @since 1.0.0
     */
    public UserRegistrationFailureInfo(CaptureApiError error, Context pContext) {
        this.error = error;
        this.mContext = pContext;
        setErrorTagging();
    }

    public void setErrorTagging(String errorTagging) {
        this.errorTagging = errorTagging;
        RLog.d(TAG, "setErrorTagging :" + this.errorTagging);
    }

    private void setErrorTagging() {
        if (null != error && null != error.raw_response) {
            this.errorTagging = getTaggingErrorDescription(error.raw_response);
            RLog.d(TAG, "setErrorTagging : getTaggingErrorDescription :" + errorTagging);
        }
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
            String message = getErrorMessageFromInvalidField(response);
            if (message != null && !message.isEmpty()) {
                RLog.e(TAG, "getErrorDescription : " + error.error_description);
                return message;
            } else {
                final String localizedJanrainError = new URError(mContext).getLocalizedError(ErrorType.JANRAIN, error.code);
                RLog.e(TAG, "getErrorDescription : " + localizedJanrainError);
                return localizedJanrainError;
            }
        } else {
            RLog.e(TAG, "getErrorDescription as error is null : " + errorDescription);
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
        RLog.e(TAG, "getErrorTagging : " + this.errorTagging);
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
        RLog.e(TAG, "getErrorCode : " + this.errorCode);
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
        RLog.e(TAG, "getError : " + error);
        return error;
    }


    /**
     * @param serverResponse
     * @return error description
     * @since 1.0.0
     */
    private String getTaggingErrorDescription(JSONObject serverResponse) {

        try {
//            RLog.d("RegUtility", "getTaggingErrorDescription : " + serverResponse.toString());
            return serverResponse.getString("error");
        } catch (JSONException e) {
            RLog.e(TAG, "getTaggingErrorDescription Exception: " + e.getMessage());
            return "";
        }
    }

    /**
     * @param serverResponse
     * @return error Message
     * @since 1.0.0
     */
    private String getErrorMessageFromInvalidField(JSONObject serverResponse) {
        try {
            JSONObject jsonObject = (JSONObject) serverResponse.get(RegConstants.INVALID_FIELDS);
            if (jsonObject != null) {
                jsonObject.keys();
                List<String> keys = new ArrayList<>();
                Iterator<?> i = jsonObject.keys();
                do {
                    String k = i.next().toString();
                    keys.add(k);
                } while (i.hasNext());

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < keys.size(); j++) {
                    JSONArray jsonObject1 = (JSONArray) jsonObject.opt(keys.get(j));
                    stringBuilder.append(jsonObject1.getString(0)).append("\n");
                }
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            //NOP
        }
        return null;
    }


}
