package com.philips.cdp.di.ecs.integration;

import android.support.annotation.NonNull;

public interface ECSInput {

    @NonNull
    String getPropositionID();
    @NonNull
    String getLocale();
    @NonNull
    String getBaseUrl();
}
