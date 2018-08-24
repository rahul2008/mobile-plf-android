/*
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.microapp;

/**
 * EWSFinishSuccessListener provides notification for the success of EWS finish.
 */
public interface EwsResultListener {

    public static final int EWS_RESULT = 10000;


    void onEWSFinishSuccess();
    void onEWSError(int errorCode);
}
