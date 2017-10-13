
package com.philips.platform.mya.demouapp;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.mya.MyaDependancies;
import com.philips.platform.mya.MyaInterface;
import com.philips.platform.mya.MyaSettings;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Interface for My account Initialization and launch of UI
 */
public class MyaDemouAppInterface implements UappInterface {
    private MyaInterface myaInterface = new MyaInterface();
    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        MyaDependancies myaDependancies = new MyaDependancies(uappDependencies.getAppInfra());
        MyaSettings myaSettings = new MyaSettings(uappSettings.getContext());
        myaInterface.init(myaDependancies, myaSettings);
    }

    /**
     * Launch Registrton UI
     *
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        myaInterface.launch(uiLauncher, uappLaunchInput);
    }
}
