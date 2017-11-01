package com.philips.platform.catk.injection;


import android.util.Log;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppInfraModule {

    private final AppInfraInterface appInfraInterface;

    public AppInfraModule(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
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
    public UserLocale providesLocale() {
        LocaleListener listener = new LocaleListener();
        appInfraInterface.getServiceDiscovery().getServiceLocaleWithCountryPreference("ds.consentservice", listener);
        return new UserLocale(listener.locale);
    }

    public static class LocaleListener implements ServiceDiscoveryInterface.OnGetServiceLocaleListener {

        private String locale;

        @Override
        public void onSuccess(String locale) {
            this.locale = locale;
        }

        @Override
        public void onError(ERRORVALUES errorvalues, String s) {
            throw new RuntimeException("Error retrieving locale: " + s);
        }
    }
}
