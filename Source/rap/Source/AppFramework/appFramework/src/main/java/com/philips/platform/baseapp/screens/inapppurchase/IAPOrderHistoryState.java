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

public class IAPOrderHistoryState extends IAPState{
    @Override
    public void updateDataModel() {
        setLaunchType(IAPState.IAP_PURCHASE_HISTORY_VIEW);
    }
}
