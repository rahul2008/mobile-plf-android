package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.response.orders.Orders;
import com.philips.cdp.di.iap.response.orders.OrdersData;
import com.philips.cdp.di.iap.response.orders.Pagination;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.RequestCode;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PurchaseHistoryFragmentTest {
    private Context mContext;
    PurchaseHistoryFragment purchaseHistoryFragment;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        purchaseHistoryFragment = PurchaseHistoryFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(purchaseHistoryFragment);
    }

    @Mock
    Message messageMock;
    @Test
    public void shouldShowErrorMessage() throws Exception {
        IAPNetworkError iapNetworkError=new IAPNetworkError(null,1,null);
        messageMock.obj=iapNetworkError;
        purchaseHistoryFragment.onGetOrderList(messageMock);

    }

    @Test
    public void shouldMoveTOEmptyPurcheseHistrory() throws Exception {
        OrdersData ordersData=new OrdersData();
        messageMock.obj=ordersData;
        messageMock.what = RequestCode.GET_ORDERS;
        purchaseHistoryFragment.onGetOrderList(messageMock);

    }


    List<Orders> ordersList;

    @Test
    public void shouldGetOrderDetailsWhenOrdersAreThere() throws Exception {

        Pagination pagination=new Pagination();
        ordersList=new ArrayList<>();
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