/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.injection;

import android.content.Context;

import com.philips.platform.mya.chi.ConsentConfiguration;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CswModule {
    private final Context context;
    private List<ConsentConfiguration> consentConfigurationList;

    public CswModule(Context context, List<ConsentConfiguration> consentConfigurationList) {
        this.context = context;
        this.consentConfigurationList = consentConfigurationList;
    }

    @Singleton
    @Provides
    public Context provideAppContext() {
        return context;
    }

    @Provides
    public List<ConsentConfiguration> provideConsentCOnfiguration(){
        return consentConfigurationList;
    }
}