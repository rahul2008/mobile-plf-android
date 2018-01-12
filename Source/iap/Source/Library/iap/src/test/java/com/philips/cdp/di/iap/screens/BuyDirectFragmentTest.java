package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;
import com.philips.cdp.di.iap.controller.BuyDirectController;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.Country;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetUser;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.IAPNetworkError;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class BuyDirectFragmentTest {
    @Mock
    private Context contextMock;
    BuyDirectFragment buyDirectFragment;


    @Before
    public void setUp() {
        initMocks(this);
        buyDirectFragment = BuyDirectFragment.createInstance(new Bundle(), InAppBaseFragment.AnimationType.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void shouldDisplayBuyDirectFragment() {

        SupportFragmentTestUtil.startFragment(buyDirectFragment);
    }

    @Mock
    Message messageMock;

    @Mock
    BuyDirectController buyDirectControllerMock;

    @Test
    public void shouldDisplayOnCartCreateWithNull() {
        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;
        buyDirectFragment.onCreateCart(messageMock);
    }
    @Test
    public void shouldDisplayOnAddToCartWithNull() {
        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;
        buyDirectFragment.onAddToCart(messageMock);
    }
    @Test
    public void shouldDisplayOnGetRegionsWithNull() {
        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;
        messageMock.obj="";
        buyDirectFragment.onGetRegions(messageMock);
    }

    @Test
    public void shouldCallOnGetUser() throws Exception {

        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;

        Country country=new Country();
        country.setIsocode("911");
        country.setName("INN");

        Addresses addresses=new Addresses();
        addresses.setTitleCode("sahhhh");
        addresses.setCountry(country);
        GetUser getUser=new GetUser();
        getUser.setDefaultAddress(addresses);
        messageMock.obj=getUser;
        buyDirectFragment.onGetUser(messageMock);
    }

    @Test
    public void shouldCallOnGetUserWhenDecfaultAddressIsNUll() throws Exception {

        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;
        GetUser getUser=new GetUser();
        getUser.setDefaultAddress(null);
        messageMock.obj=getUser;
        buyDirectFragment.onGetUser(messageMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldCallOnGetUser_WhenIAPNetworkErrorComes() throws Exception {

        IAPNetworkError iapNetworkError=new IAPNetworkError(null,1,null);
        messageMock.obj=iapNetworkError;
        buyDirectFragment.onGetUser(messageMock);
    }

    @Test
    public void shouldCall_onSetDeliveryAddress_WhenIAPNetworkErrorComes() throws Exception {
        IAPNetworkError iapNetworkError=new IAPNetworkError(null,1,null);
        iapNetworkError.setmIAPErrorCode(1001);
        messageMock.obj=iapNetworkError;
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onSetDeliveryAddress(messageMock);

    }

    @Test
    public void shouldCall_onSetDeliveryAddress_WhenNoIAPNetworkErrorComes() throws Exception {

        messageMock.obj="";
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;
        buyDirectFragment.onSetDeliveryAddress(messageMock);

    }

    @Test
    public void shouldCall_onGetDeliveryMode_WHenIapNteworkErrorComes() throws Exception {
        IAPNetworkError iapNetworkError=new IAPNetworkError(null,1,null);
        iapNetworkError.setmIAPErrorCode(1001);
        messageMock.obj=iapNetworkError;
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onGetDeliveryMode(messageMock);

    }

    @Test
    public void shouldCall_onGetDeliveryMode_WHenGetDeliveryModesComes() throws Exception {

        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;

        List<DeliveryModes> deliveryModesList=new ArrayList<>();
        deliveryModesList.add(null);

        GetDeliveryModes getDeliveryModes=new GetDeliveryModes();
        getDeliveryModes.setDeliveryModes(deliveryModesList);
        messageMock.obj=getDeliveryModes;
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onGetDeliveryMode(messageMock);

    }

    @Test
    public void shouldSetDeliveryMode_WhenNetworkErrorCome() throws Exception {
        IAPNetworkError iapNetworkError=new IAPNetworkError(null,1,null);
        iapNetworkError.setmIAPErrorCode(1001);
        messageMock.obj=iapNetworkError;
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onSetDeliveryMode(messageMock);

    }

    @Test
    public void shouldSetDeliveryMode_WhenNoNetworkError() throws Exception {
        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;
        messageMock.obj="";
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onSetDeliveryMode(messageMock);

    }

    @Test
    public void shouldCall_OnGetPaymentMode_ForEmptyResponse() throws Exception {
        messageMock.obj="";
     buyDirectFragment.onGetPaymentMode(messageMock);
    }

    //IAPNetworkError

    @Test
    public void shouldCall_OnGetPaymentMode_ForIAPNetworkError() throws Exception {
        IAPNetworkError iapNetworkError=new IAPNetworkError(null,1,null);
        iapNetworkError.setmIAPErrorCode(1001);
        messageMock.obj=iapNetworkError;
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onGetPaymentMode(messageMock);
    }

    //PaymentMethods

    @Test
    public void shouldCall_OnGetPaymentMode_ForPaymentMethods() throws Exception {

        buyDirectFragment.mBuyDirectController=buyDirectControllerMock;

        PaymentMethods paymentMethods=new PaymentMethods();

        PaymentMethod paymentMethod=new PaymentMethod();
        List<PaymentMethod> paymentMethodList=new ArrayList<>();
        paymentMethodList.add(paymentMethod);

        paymentMethods.setPayments(paymentMethodList);

        messageMock.obj=paymentMethods;
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onGetPaymentMode(messageMock);
    }

    @Test
    public void shouldCall_onSetPaymentMode_ForIapNetworkError() throws Exception {

        IAPNetworkError iapNetworkError=new IAPNetworkError(null,1,null);
        iapNetworkError.setmIAPErrorCode(1001);
        messageMock.obj=iapNetworkError;
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onSetPaymentMode(messageMock);

    }

    @Test
    public void shouldCall_onSetPaymentMode_ForEmptyIapNetworkError() throws Exception {

        messageMock.obj="";
        buyDirectFragment.mContext=contextMock;
        buyDirectFragment.onSetPaymentMode(messageMock);

    }

    @Test
    public void doNothingOnDeleteCart() throws Exception {
        buyDirectFragment.onDeleteCart(messageMock);

    }

    @Test
    public void finishDialogOnDialogOkClick() throws Exception {
        buyDirectFragment.onDialogOkClick();
    }
}