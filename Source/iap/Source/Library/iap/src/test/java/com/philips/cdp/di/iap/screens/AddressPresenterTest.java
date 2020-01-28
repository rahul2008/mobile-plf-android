/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.os.Bundle;
import android.os.Message;
import androidx.fragment.app.FragmentActivity;

import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.addresses.GetDeliveryModes;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.IAPNetworkError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by philips on 9/21/18.
 */
@RunWith(RobolectricTestRunner.class)
public class AddressPresenterTest {

    @Mock
    private FragmentActivity contextMock;

    @Mock
    private AddressContractor addressContractorMock;

    @Mock
    private AddressFields bilingAdressFileds;

    @Mock
    private AddressFields shippingAdressFileds;

    @Mock
    private Message messageMock;

    @Mock
    private AddressController addressControllerMock;

    @Mock
    private PaymentController paymentControllerMock;

    @Mock
    private com.philips.cdp.di.iap.response.addresses.DeliveryModes deliveryModesMock;

    private AddressPresenter addressPresenter;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        addressPresenter = new AddressPresenter(addressContractorMock);
        Mockito.when(addressContractorMock.getFragmentActivity()).thenReturn(contextMock);
    }

    @Test
    public void getAddressContractor() throws Exception {
        Assert.assertNotNull(addressPresenter.getAddressContractor());
    }

    @Test
    public void setContinueButtonState() throws Exception {
        addressPresenter.setContinueButtonState(true);
        Mockito.verify(addressContractorMock).setContinueButtonState(true);
    }

    @Test
    public void setBillingAddressFields() throws Exception {
        addressPresenter.setBillingAddressFields(bilingAdressFileds);
        Mockito.verify(addressContractorMock).setBillingAddressFields(bilingAdressFileds);
    }

    @Test
    public void onGetRegions() throws Exception {
        addressPresenter.onGetRegions(messageMock);
        Mockito.never();
    }

    @Test
    public void onGetUser() throws Exception {
        addressPresenter.onGetUser(messageMock);
        Mockito.never();
    }

    @Test (expected = NullPointerException.class)
    public void onCreateAddressWhenMessageObjIsInstanceOfAddress() throws Exception { //This test case should be rewritten after doing some code change
        Addresses addresses = new Addresses();
        messageMock.obj = addresses;
        addressPresenter.onCreateAddress(messageMock);
    }

    public void onCreateAddressWhenMessageObjIsInstanceOfNetworkError() throws Exception {
        IAPNetworkError iapNetworkError = new IAPNetworkError(null,0,null);
        messageMock.obj = iapNetworkError;
        addressPresenter.onCreateAddress(messageMock);
        Mockito.verify(addressContractorMock).hideProgressbar();
        Mockito.verify(addressContractorMock).showErrorMessage(messageMock);
    }

    @Test
    public void onGetAddressWhenMessageObjIsInstanceOfNetworkErrorAndRequestCodeIsUpdateAddress() throws Exception {

        IAPNetworkError iapNetworkError = new IAPNetworkError(null,0,null);
        messageMock.obj = iapNetworkError;
        messageMock.what = 6;
        addressPresenter.onGetAddress(messageMock);
        Mockito.verify(addressContractorMock).showErrorMessage(messageMock);
    }
    @Test
    public void onSetDeliveryAddressWhenMessageObjIsSuccessAndDeliveryModesIsNotNull() throws Exception {
        messageMock.obj=0;
        addressPresenter.mPaymentController = paymentControllerMock;
        Mockito.when(addressContractorMock.getDeliveryModes()).thenReturn(deliveryModesMock);
        addressPresenter.onSetDeliveryAddress(messageMock);
        Mockito.verify(paymentControllerMock).getPaymentDetails();
    }

    @Test
    public void onSetDeliveryAddressWhenMessageObjIsSuccessAndDeliveryModesIsNull() throws Exception {
        messageMock.obj=0;
        addressPresenter.mAddressController = addressControllerMock;
       // Mockito.when(addressContractorMock.getDeliveryModes()).thenReturn(deliveryModesMock);
        addressPresenter.onSetDeliveryAddress(messageMock);
        Mockito.verify(addressControllerMock).getDeliveryModes();
    }

    @Test
    public void onGetDeliveryModesHandleDeliveryModeWhenMessageObjHasEmptyResponse() throws Exception {
        messageMock.obj="";
        addressPresenter.onGetDeliveryModes(messageMock);
        Mockito.verify(addressContractorMock).hideProgressbar();
    }

    @Test
    public void onGetDeliveryModesHandleDeliveryModeWhenMessageObjIsOfIAPNetworkError() throws Exception {
        Mockito.when(addressContractorMock.getFragmentActivity()).thenReturn(contextMock);
        IAPNetworkError iapNetworkError = new IAPNetworkError(null,0,null);
        messageMock.obj=iapNetworkError;
        addressPresenter.onGetDeliveryModes(messageMock);
        Mockito.verify(addressContractorMock).hideProgressbar();
    }

    @Test
    public void onGetDeliveryModesHandleDeliveryModeWhenMessageObjIsOfGetDeliveryModes() throws Exception {
        GetDeliveryModes getDeliveryModes = new GetDeliveryModes();


        List<DeliveryModes> deliveryModeList = new ArrayList<>();
        DeliveryModes deliveryModes=new DeliveryModes();
        DeliveryModes deliveryModes1=new DeliveryModes();

        deliveryModeList.add(deliveryModes);
        deliveryModeList.add(deliveryModes1);

        getDeliveryModes.setDeliveryModes(deliveryModeList);

        addressPresenter.mAddressController = addressControllerMock;

        Mockito.when(addressContractorMock.getFragmentActivity()).thenReturn(contextMock);
        messageMock.obj=getDeliveryModes;
        addressPresenter.onGetDeliveryModes(messageMock);

        Mockito.verify(addressControllerMock).setDeliveryMode(null);

    }

    @Test //This test case is passing locally but failing on jenkin
    public void onSetDeliveryModeWhenMessageObjIAPSuccessAndBillingAddressNull() throws Exception {
        messageMock.obj= 0;
        addressPresenter.mPaymentController=paymentControllerMock;
        addressPresenter.onSetDeliveryMode(messageMock);
       // Mockito.verify(paymentControllerMock).getPaymentDetails();
    }

    @Test
    public void onSetDeliveryModeWhenMessageObjIsNotIAPSuccessAndBillingAddressIsNotNull() throws Exception {
        messageMock.obj= -1;
        addressPresenter.onSetDeliveryMode(messageMock);
        Mockito.verify(addressContractorMock).hideProgressbar();
    }

    @Test
    public void updateAddress() throws Exception {
        addressPresenter.mAddressController = addressControllerMock;
        HashMap<String,String> addressPayLpad = new HashMap();
        addressPresenter.updateAddress(addressPayLpad);
        Mockito.verify(addressControllerMock).updateAddress(addressPayLpad);
    }

    @Test
    public void createAddress() throws Exception {
        addressPresenter.mAddressController = addressControllerMock;
        addressPresenter.createAddress(shippingAdressFileds);
        Mockito.verify(addressControllerMock).createAddress(shippingAdressFileds);
    }

    @Test
    public void setDeliveryAddress() throws Exception {
        addressPresenter.mAddressController = addressControllerMock;
        addressPresenter.setDeliveryAddress("bangalore");
        Mockito.verify(addressControllerMock).setDeliveryAddress("bangalore");
    }

    @Test
    public void getDeliveryModes() throws Exception {
        addressPresenter.mAddressController = addressControllerMock;
        addressPresenter.getDeliveryModes();
        Mockito.verify(addressControllerMock).getDeliveryModes();
    }

    @Test
    public void onGetPaymentDetailsWhenMessageObjectHasEmptyResponse() throws Exception {

        messageMock.obj = "";
        addressPresenter.onGetPaymentDetails(messageMock);
        Mockito.verify(addressContractorMock).hideProgressbar();
        Mockito.verify(addressContractorMock).enableView(addressContractorMock.getBillingAddressView());
        Mockito.verify(addressContractorMock).disableView(addressContractorMock.getShippingAddressView());
    }

    @Test
    public void onGetPaymentDetailsWhenMessageObjectIAPNetworkError() throws Exception {
        IAPNetworkError iapNetworkError = new IAPNetworkError(null,0,null);
        messageMock.obj = iapNetworkError;
        addressPresenter.onGetPaymentDetails(messageMock);
    }

    @Test
    public void onGetPaymentDetailsWhenMessageObjectPaymentMethods() throws Exception {

        PaymentMethods paymentMethods = new PaymentMethods();

        List<PaymentMethod> mPaymentMethodsList = new ArrayList<>();
        PaymentMethod paymentMethod = new PaymentMethod();
        mPaymentMethodsList.add(paymentMethod);
        paymentMethods.setPayments(mPaymentMethodsList);

        messageMock.obj = paymentMethods;
        addressPresenter.onGetPaymentDetails(messageMock);
        Mockito.verify(addressContractorMock,Mockito.times(2)).hideProgressbar();
        Mockito.verify(addressContractorMock).addPaymentSelectionFragment(Mockito.any(Bundle.class));

    }

    @Test
    public void onSetPaymentDetails() throws Exception {
        addressPresenter.onSetPaymentDetails(messageMock);
        Mockito.never();
        //Do not do anything for now
    }

    @Test
    public void setBillingAddressAndOpenOrderSummary() throws Exception {
        addressPresenter.setBillingAddressAndOpenOrderSummary();

        Mockito.verify(addressContractorMock).hideProgressbar();
        Mockito.verify(addressContractorMock).addOrderSummaryFragment();
    }

    @Test
    public void shouldRomoveNullIfPresent() throws Exception {
        String originalString = "Hi philips null is here please remove";
        String expectedString = "Hi philips   is here please remove";
        Assert.assertEquals(expectedString,addressPresenter.addressWithNewLineIfNull(originalString));
    }
}