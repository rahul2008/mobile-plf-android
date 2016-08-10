/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework;

import android.content.Context;

import com.philips.platform.uappframework.configuration.uAppDependencies;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.listener.uAppListener;
/**
 * Micro app framework provides below interface which has standard APIs to initialise, launch and set Launch parameters.

 */

public interface uAppInterface {
    /**
     *  For intitalizing uApp
     */
    public void init(Context context, AppInfra appInfra);

    /**
     *  For launching the uApp
     */
    public void launch(UiLauncher uiLauncher, uAppListener uAppListener);

    /**
     *  For setting uApp launch input
     */
    public void setLaunchConfig(uAppDependencies launchConfig);
}
