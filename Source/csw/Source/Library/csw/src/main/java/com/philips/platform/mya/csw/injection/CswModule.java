/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.injection;

import android.content.Context;

import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CswModule {
    private final Context context;

    public CswModule(Context context) {
        this.context = context;
    }

    @Singleton
    @Provides
    public Context provideAppContext() {
        return context;
    }
}