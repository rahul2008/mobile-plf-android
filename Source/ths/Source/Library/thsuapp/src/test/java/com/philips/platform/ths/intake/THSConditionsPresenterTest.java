/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.ths.intake;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class THSConditionsPresenterTest {

    private THSMedicalConditionsPresenter thsMedicalConditionsPresenter;

    @Mock
    private THSMedicalConditionsFragment pTHBaseViewMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    private FragmentActivity fragmentActivityMock;

    @Mock
    Activity mActivity;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    private THSConsumerWrapper pthConsumerMock;

    @Mock
    private THSConditionsList thsConditions;

    @Mock
    private THSSDKError pthsdkError;

    @Mock
    private Condition condition1Mock;

    @Mock
    private Condition condition2Mock;

    @Mock
    private Throwable throwableMock;

    @Mock
    private AppInfraInterface appInfraInterface;

    @Mock
    private AppTaggingInterface appTaggingInterface;

    @Mock
    private LoggingInterface loggingInterface;

    @Mock
    private THSConsumer thsConsumerMock;

    @Mock
    private ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        thsMedicalConditionsPresenter = new THSMedicalConditionsPresenter(pTHBaseViewMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(pthConsumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(pthConsumerMock.getConsumer()).thenReturn(consumerMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(pTHBaseViewMock.getFragmentActivity()).thenReturn(fragmentActivityMock);
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(true);

        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void onEventContinueBtn() throws Exception {
        thsMedicalConditionsPresenter.onEvent(R.id.continue_btn);
        verify(consumerManagerMock).updateConditions(any(Consumer.class), ArgumentMatchers.<Condition>anyList(), (SDKCallback<Void, SDKError>) any());
    }

    @Test
    public void onEventSkipBtn() throws Exception {
        thsMedicalConditionsPresenter.onEvent(R.id.conditions_skip);
        verify(pTHBaseViewMock, atLeast(1)).addFragment(any(THSFollowUpFragment.class), anyString(), any(Bundle.class), anyBoolean());
    }

    @Test
    public void getConditions() throws Exception {

    }

    @Test
    public void onResponse() throws Exception {
        List<Condition> conditions = new ArrayList<>();
        conditions.add(condition1Mock);
        conditions.add(condition2Mock);
        when(thsConditions.getConditions()).thenReturn(conditions);
        thsMedicalConditionsPresenter.onResponse(thsConditions, pthsdkError);
        verify(pTHBaseViewMock).setConditions(ArgumentMatchers.<THSCondition>anyList());
    }

    @Test
    public void onFailure() throws Exception {
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(false);
        thsMedicalConditionsPresenter.onFailure(throwableMock);
        //        verifyNoMoreInteractions(pTHBaseViewMock);
    }

    @Test
    public void onUpdateConditonResponse() throws Exception {
        thsMedicalConditionsPresenter.onUpdateConditonResponse(null, pthsdkError);
    }

    @Test
    public void onUpdateConditionFailure() throws Exception {
        when(pTHBaseViewMock.isFragmentAttached()).thenReturn(false);
        thsMedicalConditionsPresenter.onUpdateConditionFailure(throwableMock);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateConditionsFailsWithException() {
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).updateConditions(any(Consumer.class), ArgumentMatchers.<Condition>anyList(), (SDKCallback<Void, SDKError>) any());
        thsMedicalConditionsPresenter.onEvent(R.id.continue_btn);
        verify(consumerManagerMock).updateConditions(any(Consumer.class), ArgumentMatchers.<Condition>anyList(), (SDKCallback<Void, SDKError>) any());
    }

}
