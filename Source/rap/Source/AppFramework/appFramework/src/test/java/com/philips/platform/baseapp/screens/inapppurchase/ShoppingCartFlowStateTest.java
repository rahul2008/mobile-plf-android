/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.inapppurchase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;

public class ShoppingCartFlowStateTest {
    private ShoppingCartFlowState shoppingCartState;

    @After
    public void tearDown(){
        shoppingCartState = null;
    }

    @Before
    public void setUp() throws Exception{
        shoppingCartState = new ShoppingCartFlowState();
    }

    @Test
    public void updateDataModel() throws Exception {
        shoppingCartState.updateDataModel();
        final int launchType = shoppingCartState.getLaunchType();
        assertTrue(launchType == IAPState.IAP_SHOPPING_CART_VIEW);
    }
}