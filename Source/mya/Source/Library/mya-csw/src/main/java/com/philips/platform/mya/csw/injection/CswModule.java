/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.injection;

import android.content.Context;

import com.philips.platform.pif.chi.ConsentRegistryInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class CswModule {
    private final Context context;
    private final ConsentRegistryInterface consentRegistryInterface;
    private List<ConsentDefinition> consentDefinitionList;

    public CswModule(Context context, ConsentRegistryInterface consentRegistryInterface, List<ConsentDefinition> consentDefinitionList) {
        this.context = context;
        this.consentRegistryInterface = consentRegistryInterface;
        this.consentDefinitionList = consentDefinitionList;
    }

    @Singleton
    @Provides
    public Context provideAppContext() {
        return context;
    }

    @Provides
    public ConsentRegistryInterface provideConsentRegistryInterface() {
        return consentRegistryInterface;
    }

    @Provides
    public List<ConsentDefinition> provideConsentDefinition() {
        return consentDefinitionList;
    }
}