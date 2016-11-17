/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.inapppurchase;

import com.philips.platform.appframework.R;

import java.util.ArrayList;
import java.util.Arrays;

public class IAPRetailerFlowState extends IAPState {
    @Override
    public void updateDataModel() {
        setLaunchType(IAPState.IAP_CATALOG_VIEW);
        setCtnList(new ArrayList<>(Arrays.asList(getApplicationContext().getResources().getStringArray(R.array.productselection_ctnlist))));
    }
}
