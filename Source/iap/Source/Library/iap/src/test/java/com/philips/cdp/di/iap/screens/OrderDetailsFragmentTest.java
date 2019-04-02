/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.model.OrderDetailRequest;
import com.philips.cdp.di.iap.model.OrderDetailRequestTest;
import com.philips.cdp.di.iap.response.orders.OrderDetail;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.NetworkURLConstants;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentController;

import java.util.HashMap;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class OrderDetailsFragmentTest {

    @Mock
    private AppInfraInterface appInfraMock;

    @Mock
    private LoggingInterface loggingMock;

    @Mock
    private IAPUser mUser;

    @Mock
    private View viewMock;

    private Context mContext;

    private StoreListener mStore;

    private OrderDetailsFragment orderDetailsFragmentTest;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = getInstrumentation().getContext();

        when(loggingMock.createInstanceForComponent(any(), any())).thenReturn(loggingMock);
        when(appInfraMock.getLogging()).thenReturn(loggingMock);
        CartModelContainer.getInstance().setAppInfraInstance(appInfraMock);

        TestUtils.getStubbedStore();
        TestUtils.getStubbedHybrisDelegate();

        mStore = (new MockStore(mContext, mUser)).getStore(new MockIAPSetting(mContext),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mStore.initStoreConfig(/*"en", "US",*/ null);

        Bundle bundle = new Bundle();
        OrderDetail orderDetail = getOrderDetailFromJSON();

        bundle.putParcelable(IAPConstant.ORDER_DETAIL, orderDetail);
        orderDetailsFragmentTest = OrderDetailsFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
        orderDetailsFragmentTest.mOrderDetail = orderDetail;
        orderDetailsFragmentTest.setArguments(bundle);
    }

    @Test
    public void shouldDisplayAddressSelectionFragment() {
        SupportFragmentController.of(orderDetailsFragmentTest).create().start().resume();
    }

    private OrderDetail getOrderDetailFromJSON() {
        String oneAddress = TestUtils.readFile(OrderDetailRequestTest.class, "order_detail.txt");
        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ORDER_NUMBER, NetworkURLConstants.DUMMY_ORDER_ID);
        OrderDetailRequest request = new OrderDetailRequest(mStore, query, null);
        Object response = request.parseResponse(oneAddress);

        return (OrderDetail) response;
    }

    @Test(expected = ClassCastException.class)
    public void shouldAddFragmentWhenCancelButtonISClicked() throws Exception {
        orderDetailsFragmentTest.onAttach(mContext);
        Mockito.when(viewMock.getId()).thenReturn(R.id.btn_cancel);
        orderDetailsFragmentTest.onClick(viewMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCallWhenCallButtonISClicked() throws Exception {
        orderDetailsFragmentTest.onAttach(mContext);
        Mockito.when(viewMock.getId()).thenReturn(R.id.btn_call);
        orderDetailsFragmentTest.onClick(viewMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldUpdateProductList() throws Exception {
        orderDetailsFragmentTest.updateUiOnProductList();
    }
}