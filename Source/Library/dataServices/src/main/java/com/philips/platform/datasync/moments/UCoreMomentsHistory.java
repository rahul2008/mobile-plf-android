/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.moments;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UCoreMomentsHistory {

    @SerializedName("moments")
    private List<UCoreMoment> uCoreMoments;

    public List<UCoreMoment> getUCoreMoments() {
        return uCoreMoments;
    }
    private String syncurl;

    public void setUCoreMoments(final List<UCoreMoment> uCoreMoments) {
        this.uCoreMoments = uCoreMoments;
    }

    public String getSyncurl() {
        return syncurl;
    }

    public void setSyncurl(String syncurl) {
        this.syncurl = syncurl;
    }
}
