/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.platform.appinfra.AppInfraInterface;

import android.content.Context;

/**
 * This class is used to provide input parameters and customizations for Consent access tool kit.
 */

public class CatkInputs {

    private AppInfraInterface appInfra;

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public void setAppInfra(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }
}
