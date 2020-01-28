/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.os.Bundle;
import android.os.Message;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.orders.Orders;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.response.orders.Pagination;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestCode;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class PurchaseHistoryFragmentTest {

    @Mock
    private Message messageMock;

    private PurchaseHistoryFragment purchaseHistoryFragment;

    @Before
    public void setUp() {
        initMocks(this);

        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        purchaseHistoryFragment = PurchaseHistoryFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() throws Exception {
//        SupportFragmentController.of(purchaseHistoryFragment).create().start().resume();
    }

    @Test
    public void shouldShowErrorMessage() throws Exception {
        IAPNetworkError iapNetworkError = new IAPNetworkError(null, 1, null);
        messageMock.obj=iapNetworkError;
        purchaseHistoryFragment.onGetOrderList(messageMock);
    }

    @Test
    public void shouldMoveTOEmptyPurcheseHistrory() throws Exception {
        OrdersData ordersData = new OrdersData();
        messageMock.obj=ordersData;
        messageMock.what = RequestCode.GET_ORDERS;
        purchaseHistoryFragment.onGetOrderList(messageMock);

    }

    @Test
    public void shouldGetOrderDetailsWhenOrdersAreThere() throws Exception {
        Pagination pagination=new Pagination();
        List<Orders> ordersList = new ArrayList<>();
        Orders orders=new Orders();
        ordersList.add(orders);

        OrdersData ordersData=new OrdersData();
        ordersData.setOrders(ordersList);
        ordersData.setPagination(pagination);
        messageMock.obj=ordersData;
        messageMock.what = RequestCode.GET_ORDERS;
        purchaseHistoryFragment.onGetOrderList(messageMock);
    }
}