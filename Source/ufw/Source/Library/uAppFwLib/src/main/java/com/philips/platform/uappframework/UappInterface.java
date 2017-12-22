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
 * Micro app framework provides below interface which has standard APIs to initialise, launch and set Launch parameters.

 */

public interface UappInterface extends Serializable {
    /**
     *  For intitalizing uApp
     *  @param uappDependencies : Object of UappDependencies
     *  @param uappSettings: : Object of UappSettings
     */
    public void init( UappDependencies uappDependencies,UappSettings uappSettings);

    /**
     *  For launching the uApp
     *  @param uappLaunchInput: : Object of  UappLaunchInput
     *  @param  uiLauncher : Object of UiLauncher
     */
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput);

}
