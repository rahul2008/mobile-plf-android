/*
 * Copyright (c) 2016. Philips Electronics India Ltd
 * All rights reserved. Reproduction in whole or in part is prohibited without
 * the written consent of the copyright holder.
 */

package cdp.philips.com.mydemoapp.injection;

import com.philips.platform.core.BaseAppDataCreator;
import com.philips.platform.core.Eventing;

import javax.inject.Singleton;

import cdp.philips.com.mydemoapp.DataSyncApplication;
import cdp.philips.com.mydemoapp.activity.DemoActivity;
import cdp.philips.com.mydemoapp.reciever.BaseAppBroadcastReceiver;
import cdp.philips.com.mydemoapp.temperature.TemperaturePresenter;
import cdp.philips.com.mydemoapp.temperature.TemperatureTimeLineFragment;
import cdp.philips.com.mydemoapp.temperature.TemperatureTimeLineFragmentcAdapter;
import dagger.Component;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Singleton
@Component(modules = {ApplicationModule.class, CoreModule.class, DatabaseModule.class, BackendModule.class, MonitorModule.class, RegistrationModule.class})
public interface AppComponent {
    void injectApplication(DataSyncApplication app);

    Eventing getEventing();

    BaseAppDataCreator getDataCreator();

    void injectFragment(TemperatureTimeLineFragment temperatureTimeLineFragment);

    void injectTemperatureAdapter(TemperatureTimeLineFragmentcAdapter temperatureTimeLineFragmentcAdapter);
    void injectTemperature(TemperaturePresenter temperaturePresenter);

    void injectActivity(DemoActivity demoActivity);

    void injectReciever(BaseAppBroadcastReceiver baseAppBroadcastReceiver);
}