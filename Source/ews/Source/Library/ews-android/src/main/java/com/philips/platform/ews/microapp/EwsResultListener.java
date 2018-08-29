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
    int EWS_RESULT_SUCCESS = 10000;
    int EWS_RESULT_FAILURE = 20000;

    String EWS_RESULT_FAILURE_DATA = "ewsresultfailuredata";

    void onEWSFinishSuccess();

    void onEWSError(int errorCode);
}
