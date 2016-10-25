/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.core.injection;

import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;

import javax.inject.Singleton;

import dagger.Component;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
@Component(modules = {ApplicationModule.class,BackendModule.class})
public interface AppComponent {

    void injectApplication(DataServicesManager dataServicesManager);

    void injectUCoreProvider(UCoreAccessProvider uCoreAccessProvider);
}