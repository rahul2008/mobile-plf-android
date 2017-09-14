/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.widget.Toast;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 04/07/17.
 */

public class DemoBLLState extends BaseState {
    private Context context;

    public DemoBLLState() {
        super(AppStates.TESTBLUELIB);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        //TODO:Needs to launch blue lib demo micro app
    }

    @Override
    public void init(Context context) {
        this.context = context;
        Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateDataModel() {

    }
}
