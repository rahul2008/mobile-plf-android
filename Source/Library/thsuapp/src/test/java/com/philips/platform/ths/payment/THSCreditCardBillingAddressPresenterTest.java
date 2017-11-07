/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.billing.CreatePaymentRequest;
import com.americanwell.sdk.entity.billing.PaymentMethod;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class THSCreditCardBillingAddressPresenterTest {
    THSCreditCardBillingAddressPresenter mTHSCreditCardBillingAddressPresenter;

    @Mock
    THSCreditCardBillingAddressFragment thsCreditCardBillingAddressFragment;

    @Mock
    FragmentActivity fragmentActivityMock;

    @Mock
    Map mapMock;

    @Mock
    Address addressMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumerWrapper thsConsumermock;

    @Mock
    THSPaymentMethod thsPaymentMethodMock;

    @Mock
    PaymentMethod paymentMethodMock;

    @Mock
    Bundle bundle;

    @Mock
    AWSDK awsdk;

    @Mock
    Throwable throwableMock;

    @Mock
    THSCreatePaymentRequest thsCreatePaymentRequest;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    THSSDKError thssdkError;

    @Mock
    FragmentManager fragmentManagerMock;

    @Mock
    CreatePaymentRequest createPaymentRequestMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdk);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(thsCreditCardBillingAddressFragment.getFragmentActivity()).thenReturn(fragmentActivityMock);
        THSManager.getInstance().setPTHConsumer(thsConsumermock);
        when(thsConsumermock.getConsumer()).thenReturn(consumerMock);
        when(awsdk.getConsumerManager()).thenReturn(consumerManagerMock);
        mTHSCreditCardBillingAddressPresenter = new THSCreditCardBillingAddressPresenter(thsCreditCardBillingAddressFragment);
        when(thsCreditCardBillingAddressFragment.isFragmentAttached()).thenReturn(true);
    }

    @Test(expected = NullPointerException.class)
    public void onEvent() throws Exception {
        thsCreditCardBillingAddressFragment.mBundle = bundle;
        when(consumerManagerMock.getNewCreatePaymentRequest(consumerMock)).thenReturn(createPaymentRequestMock);
        when(thsCreatePaymentRequest.getCreatePaymentRequest()).thenReturn(createPaymentRequestMock);

        when(bundle.getString("cardHolderName")).thenReturn("123");
        when(bundle.getString("cardNumber")).thenReturn("sss");
        when(bundle.getInt("expirationMonth")).thenReturn(123);
        when(bundle.getInt("expirationYear")).thenReturn(456);
        when(bundle.getString("CVVcode")).thenReturn("380");

        mTHSCreditCardBillingAddressPresenter.onEvent(R.id.update_shipping_address);
    }

    @Test
    public void onGetPaymentMethodResponse() throws Exception {
        when(thssdkError.getSdkError()).thenReturn(sdkErrorMock);
        when(thsPaymentMethodMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        mTHSCreditCardBillingAddressPresenter.onGetPaymentMethodResponse(thsPaymentMethodMock,thssdkError);
        verifyNoMoreInteractions(awsdk);
    }

    @Test
    public void onGetPaymentMethodResponseNullSDKError() throws Exception {
        when(thsPaymentMethodMock.getPaymentMethod()).thenReturn(paymentMethodMock);
        when(thssdkError.getSdkError()).thenReturn(null);
        when(thsCreditCardBillingAddressFragment.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(fragmentActivityMock.getSupportFragmentManager()).thenReturn(fragmentManagerMock);
        mTHSCreditCardBillingAddressPresenter.onGetPaymentMethodResponse(thsPaymentMethodMock,thssdkError);
        verify(fragmentManagerMock).popBackStack(anyString(),anyInt());
    }

    @Test
    public void onGetPaymentFailure() throws Exception {
        mTHSCreditCardBillingAddressPresenter.onGetPaymentFailure(throwableMock);
    }

    @Test
    public void onValidationFailure() throws Exception {
        mTHSCreditCardBillingAddressPresenter.onValidationFailure(mapMock);
    }

}