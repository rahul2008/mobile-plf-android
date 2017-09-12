/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.content.Context;
import android.os.Bundle;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.AvailableProviders;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSAvailableProviderListBasedOnDatePresenterTest {

    THSAvailableProviderListBasedOnDatePresenter mThsAvailableProviderListBasedOnDatePresenter;
    @Mock
    THSAvailableProviderListBasedOnDateFragment thsAvailableProviderListBasedOnDateFragmentMock;

    @Mock
    OnDateSetChangedInterface onDateSetChangedInterfaceMock;

    @Mock
    THSAvailableProviderList thsAvailableProviderListMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    AvailableProviders availableProvidersMock;

    @Mock
    Date dateMock;

    @Mock
    Throwable throwable;

    @Mock
    Context contextMock;

    @Mock
    THSSDKError thssdkError;

    @Mock
    SDKError sdkError;

    @Mock
    Practice practiceMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSProviderInfo thsProviderInfoMock;

    @Mock
    PracticeProvidersManager practiceProvidersManagerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(thsAvailableProviderListMock.getAvailableProviders()).thenReturn(availableProvidersMock);
        when(thssdkError.getSdkError()).thenReturn(sdkError);
        THSManager.getInstance().setAwsdk(awsdkMock);
        mThsAvailableProviderListBasedOnDatePresenter = new THSAvailableProviderListBasedOnDatePresenter(thsAvailableProviderListBasedOnDateFragmentMock,onDateSetChangedInterfaceMock);
    }

    @Test
    public void onEvent() throws Exception {
        mThsAvailableProviderListBasedOnDatePresenter.onEvent(R.id.calendar_view);
    }

    @Test
    public void onResponse() throws Exception {
        mThsAvailableProviderListBasedOnDatePresenter.onResponse(thsAvailableProviderListMock,thssdkError);
        verify(thsAvailableProviderListBasedOnDateFragmentMock).updateProviderAdapterList(thsAvailableProviderListMock);
    }

    @Test
    public void onFailure() throws Exception {
        mThsAvailableProviderListBasedOnDatePresenter.onFailure(throwable);
        verify(thsAvailableProviderListBasedOnDateFragmentMock).showToast(anyString());
    }

    @Test
    public void getAvailableProvidersBasedOnDate() throws Exception {
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(thsAvailableProviderListBasedOnDateFragmentMock.getContext()).thenReturn(contextMock);
        when(thsAvailableProviderListBasedOnDateFragmentMock.getPractice()).thenReturn(practiceMock);
        //when(thsAvailableProviderListBasedOnDateFragmentMock.mDate).thenReturn(dateMock);
        thsAvailableProviderListBasedOnDateFragmentMock.mDate = dateMock;
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiceProvidersManagerMock);
        mThsAvailableProviderListBasedOnDatePresenter.getAvailableProvidersBasedOnDate();
    }

    @Test
    public void launchAvailableProviderDetailFragment() throws Exception {
        mThsAvailableProviderListBasedOnDatePresenter.launchAvailableProviderDetailFragment(thsProviderInfoMock,dateMock,practiceMock);
        verify(thsAvailableProviderListBasedOnDateFragmentMock).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class));
    }

}