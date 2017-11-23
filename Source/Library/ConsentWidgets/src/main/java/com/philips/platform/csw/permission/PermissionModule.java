package com.philips.platform.csw.permission;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.CreateConsentInteractor;
import com.philips.platform.catk.GetConsentInteractor;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.csw.ConsentBundleConfig;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionModule {

    private PermissionInterface permissionInterface;
    private ConsentBundleConfig config;

    public PermissionModule(PermissionInterface permissionInterface, ConsentBundleConfig config) {
        this.permissionInterface = permissionInterface;
        this.config = config;
    }

    @Provides
    PermissionInterface providePermissionInterface() {
        return permissionInterface;
    }

    @Provides
    List<ConsentDefinition> provideConsentDefinitions() {
        return config.getConsentDefinitions();
    }

    @Provides
    List<ConsentView> provideConsentViews() {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for (final ConsentDefinition definition : config.getConsentDefinitions()) {
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
    static GetConsentInteractor provideConsentInteractor(ConsentAccessToolKit consentAccessToolKit, List<ConsentDefinition> consentDefinitions) {
        return new GetConsentInteractor(consentAccessToolKit, consentDefinitions);
    }

    @Provides
    static PermissionAdapter providePermissionAdapter(List<ConsentView> consentViews) {
        return new PermissionAdapter(consentViews);
    }
}
