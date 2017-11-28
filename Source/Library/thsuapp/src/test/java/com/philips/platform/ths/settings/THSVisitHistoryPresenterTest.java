/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.cost.THSVisit;
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

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class THSVisitHistoryPresenterTest {
    THSVisitHistoryPresenter mThsVisitHistoryPresenter;

    @Mock
    THSVisitHistoryFragment thsVisitHistoryFragmentMock;

    @Mock
    Context contextMock;

    @Mock
    Throwable throwableMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    Consumer consumerMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    VisitReport visitReportMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSSDKError thssdkErrorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(thsVisitHistoryFragmentMock.getContext()).thenReturn(contextMock);

        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);

        THSManager.getInstance().setAwsdk(awsdkMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);

        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);

        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(thsVisitHistoryFragmentMock.isFragmentAttached()).thenReturn(true);

        mThsVisitHistoryPresenter = new THSVisitHistoryPresenter(thsVisitHistoryFragmentMock);
    }

    @Test
    public void onEvent() throws Exception {
        mThsVisitHistoryPresenter.onEvent(-1);
    }

    @Test
    public void getVisitHistory() throws Exception {
        mThsVisitHistoryPresenter.getVisitHistory();
        verify(consumerManagerMock).getVisitReports(any(Consumer.class),any(SDKLocalDate.class),anyBoolean(),any(SDKCallback.class));
    }

    @Test
    public void getVisitHistoryAWSDKInstantiationException() throws Exception {
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).getVisitReports(any(Consumer.class),any(SDKLocalDate.class),anyBoolean(),any(SDKCallback.class));
        mThsVisitHistoryPresenter.getVisitHistory();
        verify(consumerManagerMock).getVisitReports(any(Consumer.class),any(SDKLocalDate.class),anyBoolean(),any(SDKCallback.class));
    }

    @Test
    public void onResponseWithSdkErrorNull() throws Exception {
        List list = new ArrayList();
        list.add(visitReportMock);

        mThsVisitHistoryPresenter.onResponse(list,null);
        verify(thsVisitHistoryFragmentMock).updateVisitHistoryView(anyList());
        verify(thsVisitHistoryFragmentMock).hideProgressBar();

    }

    @Test
    public void onResponseWithSdkErrorNotNull() throws Exception {
        List list = new ArrayList();
        list.add(visitReportMock);

        mThsVisitHistoryPresenter.onResponse(list,sdkErrorMock);
        verify(thsVisitHistoryFragmentMock).showError(anyString(),anyBoolean());

    }

    @Test(expected = IllegalStateException.class)
    public void onFailure() throws Exception {
        mThsVisitHistoryPresenter.onFailure(throwableMock);
        verify(thsVisitHistoryFragmentMock).showError(anyString(),anyBoolean());
    }

    @Test
    public void onFailureFragmentNotAttached() throws Exception {
        when(thsVisitHistoryFragmentMock.isFragmentAttached()).thenReturn(false);
        mThsVisitHistoryPresenter.onFailure(throwableMock);
    }

}