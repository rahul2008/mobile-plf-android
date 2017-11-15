
package com.philips.platform.mya.demouapp;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.mya.MyaDependencies;
import com.philips.platform.mya.MyaInterface;
import com.philips.platform.mya.MyaLaunchInput;
import com.philips.platform.mya.MyaSettings;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Interface for My account Initialization and launch of UI
 */
public class MyaDemouAppInterface implements UappInterface {

    private MyaInterface myaInterface;
    private List<ConsentDefinition> consentDefinitionList;

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        myaInterface = new MyaInterface();
        MyaDependencies myaDependencies = new MyaDependencies(uappDependencies.getAppInfra());
        myaDependencies.setApplicationName(((MyaDemouAppDependencies)uappDependencies).getApplicationName());
        myaDependencies.setPropositionName(((MyaDemouAppDependencies)uappDependencies).getPropositionName());
        MyaSettings myaSettings = new MyaSettings(uappSettings.getContext());
        myaInterface.init(myaDependencies, myaSettings);
    }

    /**
     * Launch MyAccount UI
     *
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        MyaLaunchInput myaLaunchInput = new MyaLaunchInput();
        myaLaunchInput.setContext(((MyaDemouAppLaunchInput)uappLaunchInput).getContext());
        myaLaunchInput.setConsentDefinition(createConsentDefinitions(myaLaunchInput.getContext(), Locale.getDefault()));
        myaInterface.launch(uiLauncher, myaLaunchInput);
    }

    @NonNull
    private List<ConsentDefinition> createConsentDefinitions(Context context, Locale currentLocale) {
        final List<ConsentDefinition> consentDefinitions = new ArrayList<>();
        consentDefinitions.add( new ConsentDefinition(context.getString(R.string.consent_moment_description), context.getString(R.string.consent_moment_help), "moment", 1 , currentLocale));
        return consentDefinitions;
    }
}
