package com.philips.platform.ths.visit;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.visit.Visit;
import com.americanwell.sdk.manager.ConsumerManager;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.VisitManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.lang.reflect.Method;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
public class THSVisitSummaryPresenterTest {

    Method mMethod;

    THSVisitSummaryFragment thsVisitSummaryFragment;
    THSVisitSummaryPresenter thsVisitSummaryPresenter;


    @Mock
    AWSDK awsdkMock;

    @Mock
    Visit visitMock;


    @Mock
    Consumer consumerMoxk;

    @Mock
    THSConsumerWrapper thsConsumerMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    VisitManager visitManagerMock;


    @Mock
    THSSDKError thssdkError;

    @Mock
    SDKError sdkErrorMock;

    Throwable throwableMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    THSVisitSummary THSVisitSummaryMock;

    @Mock
    Address addressMock;

    @Mock
    Pharmacy pharmacyMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(thsConsumerMock.getConsumer()).thenReturn(consumerMoxk);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        when(awsdkMock.getVisitManager()).thenReturn(visitManagerMock);
        thsVisitSummaryFragment = new THSVisitSummaryFragment();
        thsVisitSummaryFragment.setActionBarListener(actionBarListenerMock);
        thsVisitSummaryPresenter = new THSVisitSummaryPresenter(thsVisitSummaryFragment);
    }

    @Test
    public void onEvent() throws Exception {
        thsVisitSummaryPresenter.onEvent(R.id.ths_visit_summary_continue_button);
    }

    @Test
    public void fetchVisitSummary() throws Exception {

    }

    @Test
    public void sendRatings() throws Exception {
        thsVisitSummaryPresenter.sendRatings(4.0f, 4.0f);
        verify(visitManagerMock).sendRatings(any(Visit.class), any(Integer.class), any(Integer.class), any(SDKCallback.class));
    }





    @Test
    public void onVisitSummaryResponse() throws Exception {
        when(thssdkError.getSdkError()).thenReturn(sdkErrorMock);
        thsVisitSummaryPresenter.onResponse(THSVisitSummaryMock, thssdkError);
        verifyNoMoreInteractions(awsdkMock);
    }

    @Test
    public void onRatingResponse() throws Exception {
        //thsVisitSummaryPresenter.onResponse(voidMock,sdkErrorMock);

    }

    @Test
    public void onFailure() throws Exception {
        thsVisitSummaryPresenter.onFailure(throwableMock);
    }

    @Test
    public void getVisitHistory() throws Exception {
       /* thsVisitSummaryPresenter.getVisitHistory();
        verify(consumerManagerMock).getVisitReports(any(Consumer.class), any(SDKLocalDate.class), null, any(SDKCallback.class));*/
    }

    @Test
    public void onResponse2() throws Exception {

    }

}