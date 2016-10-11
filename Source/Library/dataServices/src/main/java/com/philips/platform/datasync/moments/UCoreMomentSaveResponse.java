/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasync.moments;

import com.google.gson.annotations.Expose;

public class UCoreMomentSaveResponse {

    @Expose
    private String momentId;

    public String getMomentId() {
        return momentId;
    }
}
