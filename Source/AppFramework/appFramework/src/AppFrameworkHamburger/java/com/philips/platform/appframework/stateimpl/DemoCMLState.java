package com.philips.platform.appframework.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.UiLauncher;

/**
 * Created by philips on 29/06/17.
 */

public class DemoCMLState extends BaseState {

    private Context context;
    public DemoCMLState() {
        super(AppStates.TESTDICOMM);
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        //TODO:Needs to launch comm lib demo micro app
    }

    @Override
    public void init(Context context) {
        this.context=context;
    }

    @Override
    public void updateDataModel() {

    }
}
