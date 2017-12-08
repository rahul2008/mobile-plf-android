/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.moments;

import com.google.gson.annotations.Expose;

public class UCoreMomentSaveResponse {
    @Expose
    private String momentId;

    public String getMomentId() {
        return momentId;
    }

    public void setMomentId(final String momentId) {
        this.momentId = momentId;
    }
}
