/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.inapppurchase;

import com.philips.cdp.di.iap.integration.IAPLaunchInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IAPOrderHistoryStateTest {
    IAPOrderHistoryState iapOrderState;

    @Before
    public void setUp() throws Exception {
        iapOrderState = new IAPOrderHistoryState();
    }

    @After
    public void tearDown() throws Exception {
        iapOrderState = null;
    }

    @Test
    public void updateDataModel() throws Exception {
        iapOrderState.updateDataModel();
        final int launchType = iapOrderState.getLaunchType();
        assert launchType == IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW;
    }

}