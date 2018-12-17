/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import com.philips.cdp.di.iap.cart.LocalShoppingCartPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class LocalShoppingCartPresenterTest {
    LocalShoppingCartPresenter mLocalShoppingCartPresenter;

    @Before
    public void setUp() throws Exception {
        mLocalShoppingCartPresenter = new LocalShoppingCartPresenter(null, null);
    }

    @Test
    public void testGetCurrentCartDetails() throws Exception {
        mLocalShoppingCartPresenter.getCurrentCartDetails();
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mLocalShoppingCartPresenter.deleteProduct(null);
    }

    @Test
    public void testUpdateProductQuantity() throws Exception {
        mLocalShoppingCartPresenter.updateProductQuantity(null, 0, 20);
    }

    @Test
    public void testAddProductToCart() throws Exception {
        mLocalShoppingCartPresenter.addProductToCart(null, null, null, false);
    }

    @Test
    public void testGetProductCartCount() throws Exception {
        mLocalShoppingCartPresenter.getProductCartCount(null, null);
    }

    @Test
    public void testBuyProduct() throws Exception {
        mLocalShoppingCartPresenter.buyProduct(null, null, null);
    }

}