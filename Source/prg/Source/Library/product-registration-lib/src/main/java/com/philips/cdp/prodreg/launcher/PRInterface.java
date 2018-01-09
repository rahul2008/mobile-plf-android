package com.philips.cdp.prodreg.launcher;

import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * It is used to initialize and launch Product Registration
 */
public class PRInterface implements UappInterface {

    /**
     * API used for initializing Product Registration
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     * @since 1.0.0
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        getInstance().init(uappDependencies, uappSettings);
        PRUiHelper.getInstance().setAppInfraInstance(((PRDependencies)
                uappDependencies).getAppInfra());
    }


    protected PRUiHelper getInstance() {
        return PRUiHelper.getInstance();
    }

    /**
     * API used for Launching Product Registration as activity or fragment
     * @param uiLauncher - UiLauncher uiLauncher
     * @param uappLaunchInput - UappLaunchInput uappLaunchInput
     * @since 1.0.0
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        getInstance().launch(uiLauncher, uappLaunchInput);
    }
}
