package com.philips.cdp2.ews.injections;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.configuration.EWSHappyFlowConfiguration;

import dagger.Module;
import dagger.Provides;

@Module
public class EWSConfigurationModule {

    @NonNull
    private EWSHappyFlowConfiguration configurationEWShappyFlow;

    public EWSConfigurationModule(@NonNull EWSHappyFlowConfiguration ewsHappyFlowConfiguration){
        this.configurationEWShappyFlow = ewsHappyFlowConfiguration;
    }

    @Provides
    EWSHappyFlowConfiguration provideEWSConfigurationContent(){
        return configurationEWShappyFlow;
    }
}
