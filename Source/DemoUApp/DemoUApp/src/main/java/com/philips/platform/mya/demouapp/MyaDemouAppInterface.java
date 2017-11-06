
package com.philips.platform.mya.demouapp;

import com.philips.platform.mya.MyaDependencies;
import com.philips.platform.mya.MyaInterface;
import com.philips.platform.mya.MyaLaunchInput;
import com.philips.platform.mya.MyaSettings;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * Interface for My account Initialization and launch of UI
 */
public class MyaDemouAppInterface implements UappInterface {

    private MyaInterface myaInterface;

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
     * Launch Registrton UI
     *
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        MyaLaunchInput myaLaunchInput = new MyaLaunchInput();
        myaLaunchInput.setContext(((MyaDemouAppLaunchInput)uappLaunchInput).getContext());
        myaInterface.launch(uiLauncher, myaLaunchInput);
    }
}
