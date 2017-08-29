/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivitypowersleep;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class ConfigPortProperties implements PortProperties {

    public static final String KEY_OPERATION = "operation";
    public static final String KEY_NEXT_SESSION = "nextsession";

    public static final String THERAPY_MODE = "therapy";
    public static final String BASELINE_MODE = "baseline";
    public static final String CHARGING_MODE = "charge";

    @SerializedName(KEY_OPERATION)
    private String operation;

    @SerializedName(KEY_NEXT_SESSION)
    private String nextSession;

    @NonNull
    public String getOperation() throws InvalidPortPropertiesException {
        if (operation == null) {
            throw new InvalidPortPropertiesException();
        }
        return operation;
    }

    @NonNull
    public String getNextSession() throws InvalidPortPropertiesException {
        if (nextSession == null) {
            throw new InvalidPortPropertiesException();
        }
        return nextSession;
    }

    public boolean isCurrentOperationTherapy() {
        return THERAPY_MODE.equals(operation);
    }

    public boolean isCurrentOperationBaseline() {
        return BASELINE_MODE.equals(operation);
    }

    public boolean isNextSessionTherapy() {
        return THERAPY_MODE.equals(nextSession);
    }

    public boolean isNextSessionBaseline() {
        return BASELINE_MODE.equals(nextSession);
    }

    public boolean isCharging() {
        return CHARGING_MODE.equals(operation);
    }

    @VisibleForTesting
    void setOperation(String operation) {
        this.operation = operation;
    }

    @VisibleForTesting
    void setNextSession(String nextSession) {
        this.nextSession = nextSession;
    }
}
