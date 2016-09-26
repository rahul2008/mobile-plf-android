/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;

import javax.inject.Singleton;

import cdp.philips.com.mydemoapp.DataSyncApplicationClass;
import cdp.philips.com.mydemoapp.datasync.temperature.TemperatureTimeLineFragment;
import cdp.philips.com.mydemoapp.datasync.trackers.TemperatureTracker;
import dagger.Component;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
@Component(modules = {ApplicationModule.class, CoreModule.class, DatabaseModule.class, BackendModule.class, RegistrationModule.class})
public interface AppComponent {
    void injectApplication(DataSyncApplicationClass app);

    Eventing getEventing();

    BaseAppDataCreator getDataCreator();

    void injectTemperature(TemperatureTracker baseTracker);

    void injectFragment(TemperatureTimeLineFragment temperatureTimeLineFragment);
}