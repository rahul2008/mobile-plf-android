/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package philips.app.sample;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.samplemicroapp.SampleAppInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;

import flowmanager.AppStates;

public class SampleState extends BaseState {

    public SampleState() {
        super(AppStates.SAMPLE);
    }

    /**
     * to navigate
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        new SampleAppInterface().launch(uiLauncher, null);
    }

    @Override
    public void init(Context context) {
    }

    @Override
    public void updateDataModel() {

    }
}
