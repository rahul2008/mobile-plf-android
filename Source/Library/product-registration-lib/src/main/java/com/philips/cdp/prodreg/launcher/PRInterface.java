package com.philips.cdp.prodreg.launcher;

import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PRInterface implements UappInterface {

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
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
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        getInstance().launch(uiLauncher, uappLaunchInput);
    }
}
