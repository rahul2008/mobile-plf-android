/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import com.philips.platform.pif.chi.ConsentConfiguration;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.csw.CswInterface;
import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionModule {

    private PermissionInterface permissionInterface;
    private HelpClickListener helpClickListener;
    private List<ConsentConfiguration> consentConfigurations;

    public PermissionModule(PermissionInterface permissionInterface, HelpClickListener helpClickListener) {
        this.permissionInterface = permissionInterface;
        this.helpClickListener = helpClickListener;

        List<ConsentConfiguration> configs = CswInterface.getCswComponent().getConsentConfigurations();
        this.consentConfigurations = configs == null ? new ArrayList<ConsentConfiguration>() : configs;
    }

    @Provides
    PermissionInterface providePermissionInterface() {
        return permissionInterface;
    }

    @Provides
    HelpClickListener provideHelpClickListener() {
        return helpClickListener;
    }

    @Provides
    List<ConsentConfiguration> provideConfigurations() {
        return consentConfigurations;
    }

    @Provides
    List<ConsentView> provideConsentViews(List<ConsentConfiguration> configurations) {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for (ConsentConfiguration configuration : configurations) {
            for (final ConsentDefinition definition : configuration.getConsentDefinitionList()) {
                consentViewList.add(new ConsentView(definition, configuration.getHandlerInterface()));
            }
        }
        return consentViewList;
    }

    @Provides
    static PermissionAdapter providePermissionAdapter(List<ConsentView> consentViews, HelpClickListener helpClickListener) {
        return new PermissionAdapter(consentViews, helpClickListener);
    }
}
