/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework;

import android.content.Context;

import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.UappListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * Micro app framework provides below interface which has standard APIs to initialise, launch and set Launch parameters.

 */

public interface UappInterface {
    /**
     *  For intitalizing uApp
     */
    public void init(Context context, UappDependencies uappDependencies);

    /**
     *  For launching the uApp
     */
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput, UappListener uappListener);

}
