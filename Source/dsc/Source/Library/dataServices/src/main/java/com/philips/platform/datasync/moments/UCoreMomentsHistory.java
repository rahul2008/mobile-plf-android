/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.moments;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UCoreMomentsHistory {

    @SerializedName("moments")
    private List<UCoreMoment> uCoreMoments;

    private String syncurl;

    public List<UCoreMoment> getUCoreMoments() {
        return uCoreMoments;
    }

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
