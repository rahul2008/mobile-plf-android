/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw.permission;

import com.philips.platform.mya.csw.permission.adapter.PermissionAdapter;

import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionModule {

    private PermissionInterface permissionInterface;
    private HelpClickListener helpClickListener;

    public PermissionModule(PermissionInterface permissionInterface, HelpClickListener helpClickListener) {
        this.permissionInterface = permissionInterface;
        this.helpClickListener = helpClickListener;
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
    static PermissionAdapter providePermissionAdapter(List<ConsentView> consentViews, HelpClickListener helpClickListener) {
        return new PermissionAdapter(consentViews, helpClickListener);
    }
}
