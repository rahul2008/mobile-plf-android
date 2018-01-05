package com.philips.platform.csw.permission;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.ConsentInteractor;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

import java.util.ArrayList;
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
    List<ConsentDefinition> provideConsentDefinitions() {
        return ConsentAccessToolKit.getInstance().getConsentDefinitions();
    }

    @Provides
    List<ConsentView> provideConsentViews() {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for (final ConsentDefinition definition : ConsentAccessToolKit.getInstance().getConsentDefinitions()) {
            consentViewList.add(new ConsentView(definition));
        }
        return consentViewList;
    }

    @Provides
    static ConsentAccessToolKit provideConsentAccessToolkit() {
        return ConsentAccessToolKit.getInstance();
    }

    @Provides
    static ConsentInteractor provideConsentInteractor(ConsentAccessToolKit consentAccessToolKit) {
        return new ConsentInteractor(consentAccessToolKit);
    }

    @Provides
    static PermissionAdapter providePermissionAdapter(List<ConsentView> consentViews, HelpClickListener helpClickListener) {
        return new PermissionAdapter(consentViews, helpClickListener);
    }
}
