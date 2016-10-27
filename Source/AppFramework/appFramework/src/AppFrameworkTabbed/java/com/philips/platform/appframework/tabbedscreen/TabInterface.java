/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.tabbedscreen;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.flowmanager.HomeTabbedActivity;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class TabInterface implements UappInterface {
    private Context context = null;
    private UiLauncher uiLauncher = null;

    public TabInterface() {
    }

    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        this.context = uappSettings.getContext();
    }

    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        this.uiLauncher = uiLauncher;
        TabLaunchInput tabLaunchInput = (TabLaunchInput) uappLaunchInput;

        if (uiLauncher instanceof ActivityLauncher) {
            ActivityLauncher activityLauncher = (ActivityLauncher) uiLauncher;
            this.invokeTabScreenAsActivity(activityLauncher, tabLaunchInput);
        } else {
            FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
            this.invokeTabScreenAsFragment(fragmentLauncher, tabLaunchInput);
        }
    }

    private void invokeTabScreenAsActivity(ActivityLauncher activityLauncher, TabLaunchInput launchInput) {
        context.startActivity(new Intent(context, HomeTabbedActivity.class));
    }

    private void invokeTabScreenAsFragment(FragmentLauncher fragmentLauncher, TabLaunchInput launchInput) {
        context.startActivity(new Intent(context, HomeTabbedActivity.class));
    }
}

