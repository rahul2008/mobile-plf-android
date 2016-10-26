/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.datasevices.injection;

import android.support.annotation.NonNull;

import com.philips.platform.datasevices.database.Database;
import com.philips.platform.core.BackendIdProvider;
import com.philips.platform.core.BaseAppCore;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;
import com.philips.platform.core.monitors.DBMonitors;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.monitors.ExceptionMonitor;
import com.philips.platform.core.monitors.LoggingMonitor;
import com.philips.platform.datasync.Backend;
import com.philips.platform.datasync.UCoreAccessProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Module
public class CoreModule {

    @NonNull
    private final Eventing eventing;

    @Inject
    BaseAppDataCreator dataCreator;

    public CoreModule(@NonNull final Eventing eventing) {
        this.eventing = eventing;
    }

    @Provides
    @Singleton
    public Eventing provideEventing() {
        return eventing;
    }

    @Provides
    @Singleton
    BaseAppCore provideCore(
            @NonNull final Database database, @NonNull final Backend backend,
            @NonNull final LoggingMonitor loggingMonitor,
            @NonNull final ExceptionMonitor exceptionMonitor, @NonNull final DBMonitors dbMonitors) {
        List<EventMonitor> monitors = new ArrayList<>();
        monitors.add(loggingMonitor);
        monitors.add(exceptionMonitor);
        return new BaseAppCore(eventing, database, backend, monitors,dbMonitors);
    }

    @Provides
    @Singleton
    BackendIdProvider provideBackendIdProvider(@NonNull final UCoreAccessProvider uCoreAccessProvider) {
        return uCoreAccessProvider;
    }

}

