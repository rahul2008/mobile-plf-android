/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/

package com.philips.platform.datasync.moments;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class UCoreDetail {
    @Expose
    private String value;
    @Expose
    private String type;

    public String getValue() {
        return value;
    }

    public void setValue(@NonNull final String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(@NonNull final String type) {
        this.type = type;
    }
}
