/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.settingscreen.SettingsFragment;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.statecontroller.UIState;

public class SettingsFragmentState extends UIState {
    UICoCoInterface uiCoCoUserReg;

    public SettingsFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        ((AppFrameworkBaseActivity)context).showFragment( new SettingsFragment(), new SettingsFragment().getClass().getSimpleName());
    }
}
