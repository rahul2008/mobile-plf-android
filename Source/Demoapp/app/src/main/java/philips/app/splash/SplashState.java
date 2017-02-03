/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package philips.app.splash;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import flowmanager.AppStates;
import philips.app.base.AppFrameworkBaseActivity;

public class SplashState extends BaseState {

    public SplashState() {
        super(AppStates.SPLASH);
    }

    @Override
    public void navigate(final UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        ((AppFrameworkBaseActivity) fragmentLauncher.getFragmentActivity()).
                addFragment(new SplashFragment(), SplashFragment.TAG);
    }

    @Override
    public void init(final Context context) {

    }

    @Override
    public void updateDataModel() {

    }
}
