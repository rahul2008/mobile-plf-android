package com.philips.platform.csw.permission;

import com.philips.platform.consenthandlerinterface.ConsentConfiguration;
import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;
import com.philips.platform.csw.permission.adapter.PermissionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dagger.Module;
import dagger.Provides;

@Module
public class PermissionModule {

    private PermissionInterface permissionInterface;
    private HelpClickListener helpClickListener;
    private Map<ConsentHandlerInterface, ConsentConfiguration> map;

    public PermissionModule(PermissionInterface permissionInterface, HelpClickListener helpClickListener) {
        this.permissionInterface = permissionInterface;
        this.helpClickListener = helpClickListener;
        this.map = null; // TODO: Get from CatKInputs
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
    Map<ConsentHandlerInterface, ConsentConfiguration> provideConfigurations() {
        return map;
    }

    @Provides
    ConsentHandlerInterface provideConsentHandlerInterface() {
        // TODO: Provide Interface
        return null;
    }


    @Provides
    List<ConsentDefinition> provideConsentDefinitions() {
        // TODO: Provide Definitions
        return null;
    }

    @Provides
    List<ConsentView> provideConsentViews(List<ConsentDefinition> definitions) {
        final List<ConsentView> consentViewList = new ArrayList<>();
        for (final ConsentDefinition definition : definitions) {
            consentViewList.add(new ConsentView(definition));
        }
        return consentViewList;
    }

    @Provides
    static PermissionAdapter providePermissionAdapter(List<ConsentView> consentViews, HelpClickListener helpClickListener) {
        return new PermissionAdapter(consentViews, helpClickListener);
    }
}
