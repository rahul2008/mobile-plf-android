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
