/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.injections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;

import dagger.Module;
import dagger.Provides;

@Module
public class EWSConfigurationModule {

    @NonNull
    private BaseContentConfiguration configurationEWShappyFlow;

    @NonNull
    private HappyFlowContentConfiguration happyFlowContentConfiguration;

    public EWSConfigurationModule(@NonNull BaseContentConfiguration baseContentConfiguration, @NonNull HappyFlowContentConfiguration happyFlowContentConfiguration){
        this.configurationEWShappyFlow = baseContentConfiguration;
        this.happyFlowContentConfiguration = happyFlowContentConfiguration;
    }

    @Provides
    BaseContentConfiguration provideEWSConfigurationContent(){
        return configurationEWShappyFlow;
    }

    @Provides
    HappyFlowContentConfiguration provideHappyFlowContentConfiguration(){
        return happyFlowContentConfiguration;
    }
}
