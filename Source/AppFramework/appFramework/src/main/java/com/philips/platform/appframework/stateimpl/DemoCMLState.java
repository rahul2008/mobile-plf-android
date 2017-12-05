/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.CommlibUappDependencies;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * State class to launch Comm lib demo micro app.
 */

public class DemoCMLState extends DemoBaseState {

    private static final String TAG = DemoCMLState.class.getSimpleName();
    private Context context;

    public DemoCMLState() {
        super(AppStates.TESTDICOMM);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        CommlibUapp uAppInterface = getCommLibUApp();
        if (uAppInterface != null) {
            RALog.d(TAG, "CommlibUApp is null");
            try {
                final AppFrameworkApplication appContext = ((AppFrameworkApplication) context.getApplicationContext());

                uAppInterface.init(new CommlibUappDependencies() {
                    @Override
                    public CommCentral getCommCentral() {
                        RALog.i("testing","DemoCML getCommCentralInstance - " + appContext.getCommCentralInstance());
                        return appContext.getCommCentralInstance();
                    }
                }, new UappSettings(context.getApplicationContext()));

            } catch (UnsatisfiedLinkError error) {
                RALog.d(TAG, "Not able to find native implementation");
            }
            uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                    getDLSThemeConfiguration(context.getApplicationContext()), 0, null), null);
        }
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }

    public CommlibUapp getCommLibUApp() {
        return CommlibUapp.get();
    }

    @Override
    public void updateDataModel() {

    }
}
