
package com.philips.platform.urdemo;

import android.content.Context;
import android.content.Intent;

import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * Interface for User Registration Initialization and launch of UI
 */
public class URDemouAppInterface implements UappInterface {

    private Context context;

    static AppInfraInterface appInfra;

    /**
     * Initialize Registration coponent
     *
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        this.context = uappSettings.getContext();
        appInfra = uappDependencies.getAppInfra();
        URDependancies urDependancies = new URDependancies(appInfra);
        URSettings urSettings = new URSettings(context);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
    }

    /**
     * Launch Registrton UI
     *
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (uiLauncher instanceof ActivityLauncher) {
            Intent intent = new Intent(context, URStandardDemoActivity.class);
            context.startActivity(intent);
        }
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

}
