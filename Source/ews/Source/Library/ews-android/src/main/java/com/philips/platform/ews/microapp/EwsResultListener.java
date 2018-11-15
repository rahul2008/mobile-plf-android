/*
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.microapp;

import java.io.Serializable;

/**
 * EwsResultListener provides notification for the success of EWS finish.
 */
public interface EwsResultListener {
    int LAUNCH_EWS_REQUEST = 9999;

    int EWS_RESULT_SUCCESS = 10000;
    int EWS_RESULT_FAILURE = 20000;
    int EWS_RESULT_CANCEL = 30000;

    String EWS_RESULT_FAILURE_DATA = "ewsresultfailuredata";

    void onEWSFinishSuccess();

    void onEWSError(int errorCode);

    /**
     * Call back to inform application that EWS has been cancelled
     * @since  2018.5.0
     */
    void onEWSCancelled();
}
