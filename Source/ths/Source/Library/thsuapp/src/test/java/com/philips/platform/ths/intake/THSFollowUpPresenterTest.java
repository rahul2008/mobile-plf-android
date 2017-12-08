/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKPasswordError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerUpdate;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class THSFollowUpPresenterTest {

    THSFollowUpPresenter mTHSFollowUpPresenter;

    @Mock
    THSFollowUpFragment thsFollowUpFragmentMock;

    @Mock
    THSFollowUpViewInterface thsFollowUpViewInterfaceMock;

    @Mock
    THSVisitContext thsVisitContextMock;

    @Mock
    Throwable throwableMock;

    @Mock
    VisitContext visitContextMock;

    @Mock
    LegalText legalTextMock;

    @Mock
    Map map;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSSDKPasswordError thssdkPasswordErrorMock;

    @Mock
    SDKPasswordError sdkPasswordErrorMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    ConsumerUpdate consumerUpdateMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setVisitContext(thsVisitContextMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);
        List list = new ArrayList();
        list.add(legalTextMock);
        when(visitContextMock.getLegalTexts()).thenReturn(list);
        when(thsFollowUpViewInterfaceMock.validatePhoneNumber()).thenReturn(true);
        when(consumerManagerMock.getNewConsumerUpdate(consumerMock)).thenReturn(consumerUpdateMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(thsVisitContextMock.getVisitContext()).thenReturn(visitContextMock);
        mTHSFollowUpPresenter = new THSFollowUpPresenter(thsFollowUpFragmentMock,thsFollowUpViewInterfaceMock);
    }

    @Test
    public void onEvent() throws Exception {

        mTHSFollowUpPresenter.onEvent(R.id.pth_intake_follow_up_continue_button);

        verify(consumerManagerMock).updateConsumer(any(ConsumerUpdate.class),any(SDKValidatedCallback.class));
    }

    @Test
    public void updateConsumer() throws Exception {
        mTHSFollowUpPresenter.updateConsumer("1234567890");
        verify(consumerManagerMock).updateConsumer(any(ConsumerUpdate.class),any(SDKValidatedCallback.class));
    }

    @Test
    public void onUpdateConsumerValidationFailure() throws Exception {
        mTHSFollowUpPresenter.onUpdateConsumerValidationFailure(map);
    }

    @Test
    public void onUpdateConsumerResponse() throws Exception {
        when(thssdkPasswordErrorMock.getSdkPasswordError()).thenReturn(sdkPasswordErrorMock);
        mTHSFollowUpPresenter.onUpdateConsumerResponse(thsConsumerWrapperMock,thssdkPasswordErrorMock);
        verifyNoMoreInteractions(thsFollowUpFragmentMock);
    }

    @Test
    public void onUpdateConsumerFailure() throws Exception {
        mTHSFollowUpPresenter.onUpdateConsumerFailure(throwableMock);
//        verify(thsFollowUpFragmentMock).hideProgressBar();
    }

}