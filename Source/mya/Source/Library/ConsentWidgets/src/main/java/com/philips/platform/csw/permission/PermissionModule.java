/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.permission;

import com.philips.platform.consenthandlerinterface.ConsentHandlerMapping;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionModule {

    private PermissionInterface permissionInterface;
    private HelpClickListener helpClickListener;
    private List<ConsentHandlerMapping> consentHandlerMappings;

    public PermissionModule(PermissionInterface permissionInterface, HelpClickListener helpClickListener) {
        this.permissionInterface = permissionInterface;
        this.helpClickListener = helpClickListener;

        List<ConsentHandlerMapping> configs = CswInterface.getCswComponent().getConsentConfigurations();
        this.consentHandlerMappings = configs == null ? new ArrayList<ConsentHandlerMapping>() : configs;
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
    List<ConsentHandlerMapping> provideConfigurations() {
        return consentHandlerMappings;
    }

    @Provides
    List<ConsentView> provideConsentViews(List<ConsentHandlerMapping> configurations) {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for (ConsentHandlerMapping configuration : configurations) {
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
