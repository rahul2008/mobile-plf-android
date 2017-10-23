/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.injections;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.util.StringProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class EWSConfigurationModule {

    @NonNull private final Context context;

    @NonNull private BaseContentConfiguration baseContentConfiguration;
    @NonNull private HappyFlowContentConfiguration happyFlowContentConfiguration;
    @NonNull private TroubleShootContentConfiguration troubleShootContentConfiguration ;

    public EWSConfigurationModule(@NonNull Context context,
                                  @NonNull BaseContentConfiguration baseContentConfiguration,
                                  @NonNull HappyFlowContentConfiguration happyFlowContentConfiguration,
                                  @NonNull TroubleShootContentConfiguration troubleShootContentConfiguration){
        this.context = context;
        this.baseContentConfiguration = baseContentConfiguration;
        this.happyFlowContentConfiguration = happyFlowContentConfiguration;
        this.troubleShootContentConfiguration = troubleShootContentConfiguration;
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
    TroubleShootContentConfiguration provideTroubleShootContentConfiguration(){
        return troubleShootContentConfiguration;
    }

    @Provides
    StringProvider provideStringProvider() {
        return new StringProvider(context);
    }
}
