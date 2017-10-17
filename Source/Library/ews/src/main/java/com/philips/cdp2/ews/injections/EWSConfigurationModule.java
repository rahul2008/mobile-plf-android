package com.philips.cdp2.ews.injections;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.configuration.EWSStartContentConfiguration;

import java.io.Serializable;
import java.util.Map;

import dagger.Module;
import dagger.Provides;

@Module
public class EWSConfigurationModule {

    @NonNull
    private Map<String, Serializable> configurationMap;

    public EWSConfigurationModule(@NonNull Map<String, Serializable> configurationMap){
        this.configurationMap = configurationMap;
    }

    @Provides
    EWSStartContentConfiguration provideEWSConfigurationContent(){
        return (EWSStartContentConfiguration) configurationMap.get(EWSStartContentConfiguration.class.getName());
    }
}
