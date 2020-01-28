/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.Country;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.addresses.Region;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.IAPConstant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class OrderSummaryFragmentTest {

    @Mock
    private PaymentMethod mockPaymentMethod;

    @Mock
    private PaymentController paymentControllerMock;

    @Mock
    private View viewMock;

    @Mock
    private RecyclerView recyclerViewMock;

    @Mock
    private Button button1;

    @Mock
    private Button button2;

    @Mock
    private Message messageMock;

    private Context mContext;

    private IAPOrderSummaryFragmentMock orderSummaryFragment;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = getInstrumentation().getContext();
        Mockito.when(mockPaymentMethod.getBillingAddress()).thenReturn(new Addresses());

        final Bundle bundle = new Bundle();
        bundle.putSerializable(IAPConstant.SELECTED_PAYMENT,mockPaymentMethod);

        orderSummaryFragment = new IAPOrderSummaryFragmentMock();
        orderSummaryFragment.setArguments(bundle);
        orderSummaryFragment.mPaymentController = paymentControllerMock;
    }

    @Test(expected = RuntimeException.class)
    public void shouldDisplayAddressSelectionFragment() {

//        SupportFragmentTestUtil.startFragment(orderSummaryFragment);
    }

    @Test
    public void shouldInitializeViews() throws Exception {

        Mockito.when(viewMock.findViewById(R.id.shopping_cart_recycler_view)).thenReturn(recyclerViewMock);
        Mockito.when(viewMock.findViewById(R.id.pay_now_btn)).thenReturn(button1);
        Mockito.when(viewMock.findViewById(R.id.cancel_btn)).thenReturn(button2);

        orderSummaryFragment.mContext=mContext;

        PaymentMethod paymentMethod=new PaymentMethod();

        Country country=new Country();
        country.setIsocode("isoCode");

        Region region=new Region();

        Addresses addresses=new Addresses();
        addresses.setCountry(country);
        addresses.setRegion(region);

        paymentMethod.setBillingAddress(addresses);

        Bundle bundle=new Bundle();
        bundle.putSerializable(IAPConstant.SELECTED_PAYMENT,paymentMethod);
        orderSummaryFragment.setArguments(bundle);
    }

    @Test
    public void sholudPlaceOrder_onPayBtnClicked() throws Exception {
        orderSummaryFragment.onAttach(mContext);
        Mockito.when(viewMock.getId()).thenReturn(R.id.pay_now_btn);
    }

    @Test(expected = NullPointerException.class)
    public void sholudCancelOrtder_WhenCancelButtonIsClicked() throws Exception {
        orderSummaryFragment.onAttach(mContext);
        Mockito.when(viewMock.getId()).thenReturn(R.id.cancel_btn);
        orderSummaryFragment.onClick(viewMock);
    }

    @Test
    public void shouldStartProductDetailsFragmentOnEventRecieved() throws Exception {
        orderSummaryFragment.onEventReceived("PRODUCT_DETAIL_FRAGMENT");
    }

    @Test(expected = NullPointerException.class)
    public void shouldStartProsuctCatalogFragmentOnEventRecieved() throws Exception {
        orderSummaryFragment.onEventReceived("IAP_LAUNCH_PRODUCT_CATALOG");
    }

    @Test
    public void shouldStartDeliveryModeFragmentOnEventRecieved() throws Exception {
        orderSummaryFragment.onEventReceived("IAP_EDIT_DELIVERY_MODE");
    }

    @Test
    public void sholudShowErrorOngettingIapNetworkErrorMessage() throws Exception {
        IAPNetworkError iapNetworkError = new IAPNetworkError(null, 0, null);
        messageMock.obj=iapNetworkError;
        orderSummaryFragment.onGetAddress(messageMock);
    }

    @Test
    public void sholudShowErrorOngettingShippingAddressData() throws Exception {
        GetShippingAddressData getShippingAddressData = new GetShippingAddressData();
        messageMock.obj=getShippingAddressData;
        orderSummaryFragment.onGetAddress(messageMock);

    }

    @Test
    public void shloudShowAddressFragmentOnEmptyMessageRecieved() throws Exception {
        messageMock.obj="";
        orderSummaryFragment.onGetAddress(messageMock);
    }
}