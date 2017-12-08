package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
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
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OrderSummaryFragmentTest {
    private Context mContext;
    OrderSummaryFragment orderSummaryFragment;
    @Mock
    PaymentMethod mockPaymentMethod;

    @Before
    public void setUp() {
        initMocks(this);

        mContext = RuntimeEnvironment.application;
        Mockito.when(mockPaymentMethod.getBillingAddress()).thenReturn(new Addresses());

        final Bundle bundle = new Bundle();
        bundle.putSerializable(IAPConstant.SELECTED_PAYMENT,mockPaymentMethod);

        orderSummaryFragment = OrderSummaryFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = RuntimeException.class)
    public void shouldDisplayAddressSelectionFragment() {

        SupportFragmentTestUtil.startFragment(orderSummaryFragment);
    }

    @Mock
    View viewMock;

    @Mock
    TextView tvCheckOutMock;

    @Mock
    RecyclerView recyclerViewMock;

    @Mock
    Button button1;
    @Mock
    Button button2;

    /* mPayNowBtn = (Button) rootView.findViewById(R.id.pay_now_btn);
        mPayNowBtn.setOnClickListener(this);
        mCancelBtn = (Button) rootView.findViewById(R.id.cancel_btn);*/



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


        Mockito.when(viewMock.findViewById(R.id.tv_checkOutSteps)).thenReturn(tvCheckOutMock);
        orderSummaryFragment.initializeViews(viewMock);

    }

    @Test(expected = IllegalStateException.class)
    public void shouldTrackAndSetTitleOfPage_WhenOnResumeIscalled() throws Exception {
        orderSummaryFragment.onResume();

    }

    @Test
    public void sholudDismissProgressBar_WhenOnStopIsCalled() throws Exception {
    orderSummaryFragment.onStop();
    }

    @Test
    public void sholudUnRegisterAllEvents_WhenOnDestroyViewIsCalled() throws Exception {
        orderSummaryFragment.onDestroyView();

    }


    @Test(expected = IllegalStateException.class)
    public void sholudPlaceOrder_onPayBtnClicked() throws Exception {
        orderSummaryFragment.onAttach(mContext);
        Mockito.when(viewMock.getId()).thenReturn(R.id.pay_now_btn);
        orderSummaryFragment.onClick(viewMock);

    }

    @Test(expected = NullPointerException.class)
    public void sholudCancelOrtder_WhenCancelButtonIsClicked() throws Exception {

        orderSummaryFragment.onAttach(mContext);
        Mockito.when(viewMock.getId()).thenReturn(R.id.cancel_btn);
        orderSummaryFragment.onClick(viewMock);

    }

    @Test
    public void shouldStartProsuctDetailsFragmentOnEventRecieved() throws Exception {
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

    @Mock
    Message messageMock;



    @Test
    public void sholudShowErrorOngettingIapNetworkErrorMessage() throws Exception {
        IAPNetworkError iapNetworkError=new IAPNetworkError(null,0,null);
        messageMock.obj=iapNetworkError;
        orderSummaryFragment.onGetAddress(messageMock);

    }

    @Test
    public void sholudShowErrorOngettingShippingAddressData() throws Exception {
        GetShippingAddressData getShippingAddressData=new GetShippingAddressData();
        messageMock.obj=getShippingAddressData;
        orderSummaryFragment.onGetAddress(messageMock);

    }

    @Test
    public void shloudShowAddressFragmentOnEmptyMessageRecieved() throws Exception {
        messageMock.obj="";
        orderSummaryFragment.onGetAddress(messageMock);
    }
}