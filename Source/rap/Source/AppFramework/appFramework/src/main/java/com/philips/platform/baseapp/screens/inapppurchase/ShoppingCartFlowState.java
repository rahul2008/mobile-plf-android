/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.inapppurchase;

import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.settingscreen.IndexSelectionListener;
import com.philips.platform.baseapp.screens.utility.CTNUtil;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
import java.util.Arrays;

public class ShoppingCartFlowState extends IAPState{

    @Override
    public void updateDataModel() {
        setLaunchType(IAPState.IAP_SHOPPING_CART_VIEW);
    }
}
