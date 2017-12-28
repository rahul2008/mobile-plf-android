/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.provider;


import android.support.annotation.NonNull;

public class AppInfraInfo {
    @NonNull private String cssUrl;

    public AppInfraInfo(@NonNull String cssUrl) {
        this.cssUrl = cssUrl;
    }

    @NonNull
    public String getCssUrl() {
        return cssUrl;
    }

}
