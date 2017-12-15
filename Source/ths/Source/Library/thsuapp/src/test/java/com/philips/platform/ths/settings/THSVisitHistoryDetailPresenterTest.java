/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.FileAttachment;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKErrorReason;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.entity.visit.VisitReportDetail;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import org.apache.xerces.util.URI;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.URL;
import java.util.Map;

import retrofit2.http.Url;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSVisitHistoryDetailPresenterTest {

    THSVisitHistoryDetailPresenter mThsVisitHistoryDetailPresenter;

    @Mock
    THSVisitHistoryDetailFragment thsVisitHistoryDetailFragmentMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Consumer consumerMock;

    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlListener> requestCaptor;

    @Captor
    private ArgumentCaptor<SDKCallback<VisitReportDetail, SDKError>> requestCaptorForVisitReportDetail;


    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    VisitReport visitReportMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    THSSDKError thssdkErrorMock;

    @Mock
    Throwable throwableMock;

    @Mock
    VisitReportDetail visitReportDetailMock;

    @Mock
    SDKError sdkErrorMock;

    @Mock
    Context contextMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    FileAttachment fileAttachmentMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(thssdkErrorMock.getSdkError()).thenReturn(sdkErrorMock);

        when(thsVisitHistoryDetailFragmentMock.isFragmentAttached()).thenReturn(true);

        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);

        mThsVisitHistoryDetailPresenter = new THSVisitHistoryDetailPresenter(thsVisitHistoryDetailFragmentMock);
    }

    @Test
    public void onEventOnSuccess_Hippa() throws Exception {
        mThsVisitHistoryDetailPresenter.onEvent(R.id.ths_download_report_hippa_notice_link);
        verify(serviceDiscoveryMock).getServiceUrlWithCountryPreference(anyString(), requestCaptor.capture());
        final ServiceDiscoveryInterface.OnGetServiceUrlListener value = requestCaptor.getValue();
        URL URL = new URL("http://google.com") ;
        value.onSuccess(URL);
        verify(thsVisitHistoryDetailFragmentMock).showHippsNotice(anyString());
    }

    @Test
    public void onEventOnFail_Hippa() throws Exception {
        mThsVisitHistoryDetailPresenter.onEvent(R.id.ths_download_report_hippa_notice_link);
        verify(serviceDiscoveryMock).getServiceUrlWithCountryPreference(anyString(), requestCaptor.capture());
        final ServiceDiscoveryInterface.OnGetServiceUrlListener value = requestCaptor.getValue();
        value.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT,"failed");
        verify(thsVisitHistoryDetailFragmentMock).hideProgressBar();
    }

    @Test
    public void downloadReport() throws Exception {
        when(thsVisitHistoryDetailFragmentMock.getVisitReport()).thenReturn(visitReportMock);
        when(thsVisitHistoryDetailFragmentMock.getContext()).thenReturn(contextMock);
        mThsVisitHistoryDetailPresenter.downloadReport();
        verify(consumerManagerMock).getVisitReportAttachment(any(Consumer.class),any(VisitReport.class),any(SDKCallback.class));
    }

    @Test
    public void downloadReportAWSDKInstantiationException() throws Exception {
        when(thsVisitHistoryDetailFragmentMock.getVisitReport()).thenReturn(visitReportMock);
        when(thsVisitHistoryDetailFragmentMock.getContext()).thenReturn(contextMock);
        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).getVisitReportAttachment(any(Consumer.class),any(VisitReport.class),any(SDKCallback.class));
        mThsVisitHistoryDetailPresenter.downloadReport();
        verify(consumerManagerMock).getVisitReportAttachment(any(Consumer.class),any(VisitReport.class),any(SDKCallback.class));
    }

    @Test(expected = NullPointerException.class)
    public void onResponse_sdkError_null() throws Exception {
        mThsVisitHistoryDetailPresenter.onResponse(fileAttachmentMock,null);
        verify(appTaggingInterface).trackActionWithInfo(anyString(),any(Map.class));
    }

    @Test
    public void onResponse() throws Exception {
        when(sdkErrorMock.getSDKErrorReason()).thenReturn(SDKErrorReason.ATTACHMENT_NOT_FOUND);
        mThsVisitHistoryDetailPresenter.onResponse(fileAttachmentMock,sdkErrorMock);
        verify(thsVisitHistoryDetailFragmentMock).showError(anyString());
    }

    @Test(expected = IllegalStateException.class)
    public void onFailure() throws Exception {
        mThsVisitHistoryDetailPresenter.onFailure(throwableMock);
    }

    @Test
    public void getVisitReportDetailOnSuccess() throws Exception {
        when(thsVisitHistoryDetailFragmentMock.getVisitReport()).thenReturn(visitReportMock);
        when(thsVisitHistoryDetailFragmentMock.getContext()).thenReturn(contextMock);
        mThsVisitHistoryDetailPresenter.getVisitReportDetail(visitReportMock);
        verify(consumerManagerMock).getVisitReportDetail(any(Consumer.class),any(VisitReport.class),requestCaptorForVisitReportDetail.capture());
        final SDKCallback<VisitReportDetail, SDKError> value1 = requestCaptorForVisitReportDetail.getValue();
        value1.onResponse(visitReportDetailMock,sdkErrorMock);
        verify(thsVisitHistoryDetailFragmentMock).hideProgressBar();
    }

    @Test(expected = IllegalStateException.class)
    public void getVisitReportDetailOnError() throws Exception {
        when(thsVisitHistoryDetailFragmentMock.getVisitReport()).thenReturn(visitReportMock);
        when(thsVisitHistoryDetailFragmentMock.getContext()).thenReturn(contextMock);
        mThsVisitHistoryDetailPresenter.getVisitReportDetail(visitReportMock);
        verify(consumerManagerMock).getVisitReportDetail(any(Consumer.class),any(VisitReport.class),requestCaptorForVisitReportDetail.capture());
        final SDKCallback<VisitReportDetail, SDKError> value1 = requestCaptorForVisitReportDetail.getValue();
        value1.onFailure(throwableMock);
        verify(thsVisitHistoryDetailFragmentMock).hideProgressBar();
    }

    @Test
    public void getVisitReportDetailOnException() throws Exception {
        when(thsVisitHistoryDetailFragmentMock.getVisitReport()).thenReturn(visitReportMock);
        when(thsVisitHistoryDetailFragmentMock.getContext()).thenReturn(contextMock);


        doThrow(AWSDKInstantiationException.class).when(consumerManagerMock).getVisitReportDetail(any(Consumer.class),any(VisitReport.class),any(SDKCallback.class));
        mThsVisitHistoryDetailPresenter.getVisitReportDetail(visitReportMock);

        verify(consumerManagerMock).getVisitReportDetail(any(Consumer.class),any(VisitReport.class),any(SDKCallback.class));
//        verify(thsVisitHistoryDetailFragmentMock).hideProgressBar();
    }

}