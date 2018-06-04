/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk.injection;

import javax.inject.Singleton;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class CatkModule {
    private final Context context;
    private final AppInfraInterface appInfraInterface;

    public CatkModule(Context context, AppInfraInterface appInfraInterface) {
        this.context = context;
        this.appInfraInterface = appInfraInterface;
    }

    @Singleton
    @Provides
    public Context provideAppContext() {
        return context;
    }

    @Singleton
    @Provides
    public LoggingInterface providesLoggingInterface() {
        return appInfraInterface.getLogging();
    }

    @Singleton
    @Provides
    public RestInterface providesRestInterface() {
        return appInfraInterface.getRestClient();
    }

    @Singleton
    @Provides
    public ServiceDiscoveryInterface providesServiceDiscoveryInterfaceInterface() {
        return appInfraInterface.getServiceDiscovery();
    }
}