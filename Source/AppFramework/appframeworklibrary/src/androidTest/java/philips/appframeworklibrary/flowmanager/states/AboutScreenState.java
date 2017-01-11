/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package philips.appframeworklibrary.flowmanager.states;

import android.content.Context;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import philips.appframeworklibrary.flowmanager.base.BaseState;

/**
 * This class has UI extended from UIKIT about screen , It shows the current version of the app
 */
public class AboutScreenState extends BaseState {

    FragmentLauncher fragmentLauncher;

    public AboutScreenState() {
        super("about");
    }

    /**
     * Navigating to AboutScreenFragment
     *
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
    }

    @Override
    public void init(Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}

