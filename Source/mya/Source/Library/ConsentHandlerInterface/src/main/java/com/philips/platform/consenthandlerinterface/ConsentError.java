/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.consenthandlerinterface;

public class ConsentError {

    public static final int CONSENT_SUCCESS = 0;
    public static final int CONSENT_ERROR = -1;
    public static final int CONSENT_ERROR_NO_CONNECTION = 2;
    public static final int CONSENT_ERROR_CONNECTION_TIME_OUT = 3;
    public static final int CONSENT_ERROR_AUTHENTICATION_FAILURE = 4;
    public static final int CONSENT_ERROR_SERVER_ERROR = 5;
    public static final int CONSENT_ERROR_INSUFFICIENT_STOCK_ERROR = 6;
    public static final int CONSENT_ERROR_UNKNOWN = 7;

    private final String error;
    private final int errorCode;

    public ConsentError(String error, int errorCode) {
        this.error = error;
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
