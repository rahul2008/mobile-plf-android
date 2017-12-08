/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.dls;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.flowmanager.UappStates;
import com.philips.platform.uappframework.launcher.UiLauncher;


public class DlsState extends BaseState {

    public DlsState() {
        super(UappStates.DLS);
    }
    private Context mContext;
    /**
     * to navigate
     * @param uiLauncher requires UiLauncher
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        Intent intent = new Intent(mContext,DLSActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    public void init(Context context) {
        this.mContext = context;
    }

    @Override
    public void updateDataModel() {

    }
}
