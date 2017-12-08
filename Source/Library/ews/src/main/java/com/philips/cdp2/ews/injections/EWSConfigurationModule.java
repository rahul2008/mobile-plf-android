/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.injections;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.util.StringProvider;

import dagger.Module;
import dagger.Provides;

@Module
public class EWSConfigurationModule {

    @NonNull private final Context context;

    @NonNull private ContentConfiguration  contentConfiguration;

    public EWSConfigurationModule(@NonNull Context context,
                                  @NonNull ContentConfiguration contentConfiguration){
        this.context = context;
        this.contentConfiguration = contentConfiguration;
    }

    @Provides
    public BaseContentConfiguration provideBaseContentConfiguration(){
        return contentConfiguration.getBaseContentConfiguration();
    }

    @Provides
    HappyFlowContentConfiguration provideHappyFlowContentConfiguration(){
        return contentConfiguration.getHappyFlowContentConfiguration();
    }

    @Provides
    TroubleShootContentConfiguration provideTroubleShootContentConfiguration(){
        return contentConfiguration.getTroubleShootContentConfiguration();
    }

    @Provides
    StringProvider provideStringProvider() {
        return new StringProvider(context);
    }
}
