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

    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        getInstance().init(uappDependencies, uappSettings);
    }

    protected PRUiHelper getInstance() {
        return PRUiHelper.getInstance();
    }

    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        getInstance().launch(uiLauncher, uappLaunchInput);
    }
}
