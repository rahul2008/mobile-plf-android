/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappframework;

import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.io.Serializable;


/**
 * Micro app framework provides interface which has standard APIs to initialise, launch and set Launch parameters.
 @since 1.0.0
 */

public interface UappInterface extends Serializable {
    /**
     *  For initializing uApp.
     *  @param uappDependencies Object of UappDependencies
     *  @param uappSettings Object of UppSettings
     *  @since 1.0.0
     */
    public void init( UappDependencies uappDependencies,UappSettings uappSettings);

    /**
     *  For launching the uApp.
     *  @param uappLaunchInput Object of  UappLaunchInput
     *  @param  uiLauncher Object of UiLauncher
     *  @since 1.0.0
     */
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput);

}
