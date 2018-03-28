/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSCheckPharmacyConditionsPresenterTest {

    THSCheckPharmacyConditionsPresenter mTHSCheckPharmacyConditionsPresenter;

    @Mock
    THSCheckPharmacyConditionsFragment thsCheckPharmacyConditionsFragmentMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapper;

    @Mock
    Context context;

    @Mock
    Throwable throwable;

    @Mock
    Pharmacy pharmacyMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    Address addressMock;

    @Mock
    ActionBarListener actionBarListener;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerWrapper);
        when(thsConsumerWrapper.getConsumer()).thenReturn(consumerMock);
        when(thsCheckPharmacyConditionsFragmentMock.getContext()).thenReturn(context);
        mTHSCheckPharmacyConditionsPresenter = new THSCheckPharmacyConditionsPresenter(thsCheckPharmacyConditionsFragmentMock, actionBarListener);
    }

    @Test
    public void onEvent() throws Exception {
        mTHSCheckPharmacyConditionsPresenter.onEvent(0);
    }

    @Test
    public void fetchConsumerPreferredPharmacy() throws Exception {
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        mTHSCheckPharmacyConditionsPresenter.fetchConsumerPreferredPharmacy();
        verify(consumerManagerMock).getConsumerPharmacy(any(Consumer.class),any(SDKCallback.class));
    }

    @Test
    public void getConsumerShippingAddress() throws Exception {
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        mTHSCheckPharmacyConditionsPresenter.getConsumerShippingAddress();
        verify(consumerManagerMock).getShippingAddress(any(Consumer.class),any(SDKCallback.class));
    }

    @Test
    public void onPharmacyReceived() throws Exception {
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        mTHSCheckPharmacyConditionsPresenter.onPharmacyReceived(pharmacyMock,sdkErrorMock);
        verify(consumerManagerMock).getShippingAddress(any(Consumer.class),any(SDKCallback.class));
    }

    @Test
    public void onSuccessfulFetch() throws Exception {
        mTHSCheckPharmacyConditionsPresenter.onSuccessfulFetch(addressMock,sdkErrorMock);
        verify(thsCheckPharmacyConditionsFragmentMock).displayPharmacyAndShippingPreferenceFragment(null,addressMock);
    }

    @Test
    public void onFailure() throws Exception {
        mTHSCheckPharmacyConditionsPresenter.onFailure(throwable);
    }

}