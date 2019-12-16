/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.adapters.ShoppingCartAdapter;
import com.philips.cdp.di.iap.cart.ShoppingCartData;
import com.philips.cdp.di.iap.response.carts.DeliveryModeEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.response.carts.ProductEntity;
import com.philips.cdp.di.iap.response.carts.StockEntity;
import com.philips.cdp.di.iap.utils.IAPConstant;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ShoppingCartFragmentTest {

    private Context mContext;
    private ShoppingCartFragment shoppingCartFragment;

    @Mock
    private ShoppingCartAdapter.OutOfStockListener outOfStockListenerMock;

    @Before
    public void setUp() {
        initMocks(this);

        shoppingCartFragment = ShoppingCartFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);

        mContext = getInstrumentation().getContext();
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {
//        SupportFragmentController.of(shoppingCartFragment).create().start().resume();
        assertNotNull( shoppingCartFragment );
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithEMPTY_CART_FRAGMENT_REPLACEDString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
    }

    @Test
    public void stopFragment_whenOnStopIsCalled() throws Exception {
        shoppingCartFragment.onPause();
        shoppingCartFragment.onStop();
        shoppingCartFragment.onDestroyView();
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithBUTTON_STATE_CHANGEDString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.BUTTON_STATE_CHANGED.toString());
    }

   @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithPRODUCT_DETAIL_FRAGMENTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.PRODUCT_DETAIL_FRAGMENT);
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_LAUNCH_PRODUCT_CATALOGString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_LAUNCH_PRODUCT_CATALOG);
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_DELETE_PRODUCTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_DELETE_PRODUCT);
    }

    @Test
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_EDIT_DELIVERY_MODEString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_EDIT_DELIVERY_MODE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_UPDATE_PRODUCT_COUNTString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_UPDATE_PRODUCT_COUNT);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldAddEmptyCartFragment_WhenOnEventRecievedWithIAP_DELETE_PRODUCT_CONFIRMString() throws Exception {
        shoppingCartFragment.onEventReceived(IAPConstant.IAP_DELETE_PRODUCT_CONFIRM);
    }

    @Test
    public void shouldStart_ProductDetailFragment() throws Exception {
        StockEntity stockEntity=new StockEntity();
        stockEntity.setStockLevel(3);
        ProductEntity productEntity=new ProductEntity();
        productEntity.setStock(stockEntity);

        EntriesEntity entriesEntity=new EntriesEntity();

        entriesEntity.setProduct(productEntity);

        ShoppingCartData shoppingCartData=new ShoppingCartData(entriesEntity,new DeliveryModeEntity());
        ArrayList<ShoppingCartData> shoppingCartDataArrayList=new ArrayList<>();
        shoppingCartDataArrayList.add(shoppingCartData);


        ShoppingCartAdapter mAdapter = new ShoppingCartAdapter(mContext, shoppingCartDataArrayList, outOfStockListenerMock );

        shoppingCartData.setStockLevel(3);
        mAdapter.setTheProductDataForDisplayingInProductDetailPage(shoppingCartData);

        mAdapter.getItemCount();
        mAdapter.getItemViewType(0);
        mAdapter.getItemViewType(1);
        mAdapter.getQuantityStatusInfo();
        mAdapter.getNewCount();
        mAdapter.getSelectedItemPosition();

        shoppingCartFragment.startProductDetailFragment(mAdapter);

    }
}