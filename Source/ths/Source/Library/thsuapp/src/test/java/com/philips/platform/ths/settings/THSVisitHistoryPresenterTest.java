/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
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
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSVisitHistoryPresenterTest {
    private THSVisitHistoryPresenter mThsVisitHistoryPresenter;

    @Mock
    private THSVisitHistoryFragment thsVisitHistoryFragmentMock;

    @Mock
    private Context contextMock;

    @Mock
    private Throwable throwableMock;

    @Mock
    private ConsumerManager consumerManagerMock;

    @Mock
    private THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    private Consumer consumerMock;

    @Mock
    private THSConsumer thsConsumerMock;

    @Mock
    private VisitReport visitReportMock;

    @Mock
    private AWSDK awsdkMock;

    @Mock
    private SDKError sdkErrorMock;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private AppTaggingInterface appTaggingInterface;

    @Mock
    private AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    private ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    private LoggingInterface loggingInterface;

    @Mock
    private THSSDKError thssdkErrorMock;

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
    @SuppressWarnings("unchecked")
    public void getVisitHistory() throws Exception {
        mThsVisitHistoryPresenter.getVisitHistory();
        verify(consumerManagerMock).getVisitReports(any(Consumer.class), (SDKLocalDate)isNull(), (Boolean)isNull(), (SDKCallback<List<VisitReport>, SDKError>) any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getVisitHistoryAWSDKInstantiationException() throws Exception {
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).getVisitReports(any(Consumer.class), any(SDKLocalDate.class), anyBoolean(), (SDKCallback<List<VisitReport>, SDKError>) any());
        mThsVisitHistoryPresenter.getVisitHistory();
        verify(consumerManagerMock).getVisitReports(any(Consumer.class), (SDKLocalDate)isNull(), (Boolean)isNull(), (SDKCallback<List<VisitReport>, SDKError>) any());
    }

    @Test
    public void onResponseWithSdkErrorNull() throws Exception {
        List<VisitReport> list = new ArrayList<>();
        list.add(visitReportMock);

        mThsVisitHistoryPresenter.onResponse(list, null);
        verify(thsVisitHistoryFragmentMock).updateVisitHistoryView(ArgumentMatchers.<VisitReport>anyList());

    }

    @Test
    public void onResponseWithSdkErrorNotNull() throws Exception {
        List<VisitReport> list = new ArrayList<>();
        list.add(visitReportMock);

        mThsVisitHistoryPresenter.onResponse(list, sdkErrorMock);
        verify(thsVisitHistoryFragmentMock).showError(anyString(), anyBoolean(), anyBoolean());

    }

    @Test(expected = IllegalStateException.class)
    public void onFailure() throws Exception {
        mThsVisitHistoryPresenter.onFailure(throwableMock);
        verify(thsVisitHistoryFragmentMock).showError(anyString(), anyBoolean(), false);
    }

    @Test
    public void onFailureFragmentNotAttached() throws Exception {
        when(thsVisitHistoryFragmentMock.isFragmentAttached()).thenReturn(false);
        mThsVisitHistoryPresenter.onFailure(throwableMock);
    }

}
