package com.philips.platform.csw.permission;

import java.util.ArrayList;
import java.util.List;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.CreateConsentInteractor;
import com.philips.platform.catk.GetConsentInteractor;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

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
    static CreateConsentInteractor provideCreateConsentInteractor(ConsentAccessToolKit consentAccessToolKit) {
        return new CreateConsentInteractor(consentAccessToolKit);
    }

    @Provides
    static GetConsentInteractor provideConsentInteractor(ConsentAccessToolKit consentAccessToolKit) {
        return new GetConsentInteractor(consentAccessToolKit);
    }

    @Provides
    static PermissionAdapter providePermissionAdapter(List<ConsentView> consentViews, HelpClickListener helpClickListener) {
        return new PermissionAdapter(consentViews, helpClickListener);
    }
}
