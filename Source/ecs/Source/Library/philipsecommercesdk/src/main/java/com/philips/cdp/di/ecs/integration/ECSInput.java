package com.philips.cdp.di.ecs.integration;

import android.support.annotation.NonNull;

public interface ECSInput {


    String getPropositionID();
    @NonNull
    String getLocale();

    String getBaseUrl();
}
