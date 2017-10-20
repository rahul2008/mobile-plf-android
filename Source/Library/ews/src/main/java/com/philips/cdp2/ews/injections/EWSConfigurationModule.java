/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.injections;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.util.StringProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class EWSConfigurationModule {

    @NonNull private final Resources resources;

    @NonNull private BaseContentConfiguration baseContentConfiguration;
    @NonNull private HappyFlowContentConfiguration happyFlowContentConfiguration;

    public EWSConfigurationModule(@NonNull Resources resources,
                                  @NonNull BaseContentConfiguration baseContentConfiguration,
                                  @NonNull HappyFlowContentConfiguration happyFlowContentConfiguration){
        this.resources = resources;
        this.baseContentConfiguration = baseContentConfiguration;
        this.happyFlowContentConfiguration = happyFlowContentConfiguration;
    }

    @Provides
    BaseContentConfiguration provideEWSConfigurationContent(){
        return baseContentConfiguration;
    }

    @Provides
    HappyFlowContentConfiguration provideHappyFlowContentConfiguration(){
        return happyFlowContentConfiguration;
    }

    @Provides
    StringProvider provideStringProvider() {
        return new StringProvider(resources);
    }
}
