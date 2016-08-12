package com.philips.cdp.prodreg.launcher;

import android.content.Context;

import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.UappListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PRInterface implements UappInterface {

    @Override
    public void init(final Context context, final UappDependencies uappDependencies) {
        ProdRegUiHelper.getInstance().init(context, uappDependencies);
    }

    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput, final UappListener uappListener) {
        ProdRegUiHelper.getInstance().launch(uiLauncher, uappLaunchInput, uappListener);
    }
}
