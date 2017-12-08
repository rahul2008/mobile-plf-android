/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.error;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServerError {

    @SerializedName("incidentID")
    @Expose
    private String incidentID;

    @SerializedName("errorCode")
    @Expose
    private int errorCode;

    @SerializedName("description")
    @Expose
    private String description;

    public String getIncidentID() {
        return incidentID;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getDescription() {
        return description;
    }
}
