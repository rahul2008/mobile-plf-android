/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.AvailableProviders;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.cdp.registration.User;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

public class THSProviderNotAvailablePresenterTest {
    THSProviderNotAvailablePresenter mTHSProviderNotAvailablePresenter;

    @Mock
    Practice practiceMock;

    @Mock
    Date dateMock;

    @Mock
    THSProviderEntity thsProviderEntityMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    PracticeProvidersManager practiceProvidersManagerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    Consumer consumerMock;

    @Mock
    User userMock;

    @Mock
    AvailableProvider availableProviderMock;

    @Mock
    ProviderInfo providerInfoMock;


    @Mock
    THSProviderNotAvailableFragment thsProviderNotAvailableFragmentMock;

    @Mock
    THSAvailableProviderList thsAvailableProviderListmock;

    @Mock
    AvailableProviders availableProvidersMock;

    @Mock
    Provider providerMock;

    @Mock
    PracticeInfo practiceInfoMock;

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

        THSManager.getInstance().setAwsdk(awsdkMock);
        when(awsdkMock.getPracticeProvidersManager()).thenReturn(practiceProvidersManagerMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);
        when(thsProviderNotAvailableFragmentMock.getProvider()).thenReturn(providerMock);
        mTHSProviderNotAvailablePresenter = new THSProviderNotAvailablePresenter(thsProviderNotAvailableFragmentMock);

    }

    @Test
    public void onEvent() throws Exception {
        mTHSProviderNotAvailablePresenter.onEvent(R.id.calendar_view);
    }

    @Test
    public void launchProviderDetailsBasedOnAvailibilty() {
        mTHSProviderNotAvailablePresenter.launchProviderDetailsBasedOnAvailibilty(practiceMock, dateMock, thsProviderEntityMock);
    }

    @Test
    public void isAvailableListContainsProviderChosen(){
        List list = new ArrayList();
        list.add(availableProviderMock);

        when(thsAvailableProviderListmock.getAvailableProviders()).thenReturn(availableProvidersMock);
        when(availableProvidersMock.getAvailableProviders()).thenReturn(list);
        when(providerMock.getPracticeInfo()).thenReturn(practiceInfoMock);
        when(availableProviderMock.getProviderInfo()).thenReturn(providerInfoMock);

        when(providerInfoMock.getFirstName()).thenReturn("aaa");
        when(providerMock.getFirstName()).thenReturn("aaa");

        mTHSProviderNotAvailablePresenter.isAvailableListContainsProviderChosen(thsAvailableProviderListmock);

    }
}