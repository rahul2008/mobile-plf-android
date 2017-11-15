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
import com.philips.cdp.registration.User;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
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
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSProviderInfo thsProviderInfoMock;

    @Mock
    PracticeProvidersManager practiceProvidersManagerMock;

    @Mock
    User userMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().TEST_FLAG = true;
        when(userMock.getHsdpUUID()).thenReturn("abc");
        when(userMock.getGivenName()).thenReturn("Spoorti");
        when(userMock.getEmail()).thenReturn("sssss");
        when(userMock.getHsdpAccessToken()).thenReturn("2233");
        when(userMock.getHsdpAccessToken()).thenReturn("eeee");
        THSManager.getInstance().setUser(userMock);

        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        List list = new ArrayList();
        list.add(thsConsumerMock);
        when(thsConsumerMock.getDependents()).thenReturn(list);
        when(thsConsumerMock.getDependents()).thenReturn(list);
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
        when(thsAvailableProviderListBasedOnDateFragmentMock.isFragmentAttached()).thenReturn(true);
        when(thssdkError.getSdkError()).thenReturn(null);
        mThsAvailableProviderListBasedOnDatePresenter.onResponse(thsAvailableProviderListMock,thssdkError);
        verify(thsAvailableProviderListBasedOnDateFragmentMock).updateProviderAdapterList(thsAvailableProviderListMock);
    }

    @Test
    public void onFailure() throws Exception {
        mThsAvailableProviderListBasedOnDatePresenter.onFailure(throwable);
        verify(thsAvailableProviderListBasedOnDateFragmentMock).showToast(anyInt());
    }

    @Test
    public void getAvailableProvidersBasedOnDate() throws Exception {
        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);
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
        verify(thsAvailableProviderListBasedOnDateFragmentMock, atLeast(1)).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class), anyBoolean());
    }

}