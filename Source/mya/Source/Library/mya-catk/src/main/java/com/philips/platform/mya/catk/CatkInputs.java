/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;

/**
 * This class is used to provide input parameters and customizations for Consent access tool kit.
 */

public class CatkInputs {

    private final AppInfraInterface appInfra;

    private final Context context;

    private CatkInputs(Builder builder) {
        appInfra = builder.appInfra;
        context = builder.context;
    }

    public Context getContext() {
        return context;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public static class Builder {

        private AppInfraInterface appInfra;

        private Context context;

        public Builder() {
        }

        public Builder setAppInfraInterface(AppInfraInterface appInfra) {
            this.appInfra = appInfra;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public CatkInputs build() {
            if (appInfra == null) {
                throw new InvalidInputException("AppInfraInterface not given, we need:\n-AppInfra\n-Context\n-ConsentDefinitions");
            }
            if (context == null) {
                throw new InvalidInputException("Context not given, we need:\n-AppInfra\n-Context\n-ConsentDefinitions");
            }

            return new CatkInputs(this);
        }
    }

    static class InvalidInputException extends RuntimeException {
        InvalidInputException(String message) {
            super(message);
        }
    }
}
