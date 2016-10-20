/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package com.philips.platform.appframework.injection;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.temperature.TemperaturePresenter;
import com.philips.platform.appframework.temperature.TemperatureTimeLineFragment;
import com.philips.platform.appframework.temperature.TemperatureTimeLineFragmentcAdapter;
import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;

import javax.inject.Singleton;

import dagger.Component;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
@Component(modules = {ApplicationModule.class, CoreModule.class, DatabaseModule.class, BackendModule.class, MonitorModule.class, RegistrationModule.class})
public interface AppComponent {
    void injectApplication(AppFrameworkApplication app);

    Eventing getEventing();

    BaseAppDataCreator getDataCreator();

    void injectFragment(TemperatureTimeLineFragment temperatureTimeLineFragment);

    void injectTemperatureAdapter(TemperatureTimeLineFragmentcAdapter temperatureTimeLineFragmentcAdapter);
    void injectTemperature(TemperaturePresenter temperaturePresenter);
}