/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.inapppurchase;

import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.utility.CTNUtil;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.ArrayList;
import java.util.Arrays;

public class IAPRetailerFlowState extends IAPState {
    public static final String TAG =  IAPRetailerFlowState.class.getSimpleName();

    @Override
    public void updateDataModel() {
        RALog.d(TAG, "updateDataModel called ");
        setLaunchType(IAPState.IAP_CATALOG_VIEW);
        try {
            setCtnList(new ArrayList<>(Arrays.asList(CTNUtil.getCtnForCountry(getApplicationContext().getAppInfra().getServiceDiscovery().getHomeCountry()))));
        } catch (RuntimeException e) {
            RALog.e(TAG,e.getMessage());
            Toast.makeText(getApplicationContext(), R.string.RA_CTN_Null, Toast.LENGTH_LONG).show();
        }
    }
}
