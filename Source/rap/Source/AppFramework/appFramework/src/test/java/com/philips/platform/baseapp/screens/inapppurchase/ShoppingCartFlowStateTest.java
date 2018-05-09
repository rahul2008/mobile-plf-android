/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.inapppurchase;

import com.philips.platform.TestAppFrameworkApplication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

public class ShoppingCartFlowStateTest {
    private IAPState shoppingCartState;

    @After
    public void tearDown(){
        shoppingCartState = null;
    }

    @Before
    public void setUp() throws Exception{
        shoppingCartState = new ShoppingCartFlowState();
    }
    public TestAppFrameworkApplication getApplicationContext(){
        return (TestAppFrameworkApplication) RuntimeEnvironment.application;
    }

    @Test
    public void updateDataModel() throws Exception {
        shoppingCartState.updateDataModel();
        final int launchType = shoppingCartState.getLaunchType();
        assert launchType == IAPState.IAP_SHOPPING_CART_VIEW;
    }
}