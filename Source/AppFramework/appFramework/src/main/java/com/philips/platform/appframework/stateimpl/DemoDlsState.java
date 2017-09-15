package com.philips.platform.appframework.stateimpl;

import android.content.Context;
import android.widget.Toast;

import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.uappframework.launcher.UiLauncher;

/*
 * @author: Ritesh Jha
 * @date: 28th Aug 2017
 */

public class DemoDlsState extends BaseState {
    private Context mContext;

    public DemoDlsState() {
        super(AppStates.TESTDLS);
    }

    public void navigate(UiLauncher uiLauncher) {
    }

    public void init(Context context) {
        this.mContext = context;
        Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show();
    }

    public void updateDataModel() {
    }
}
