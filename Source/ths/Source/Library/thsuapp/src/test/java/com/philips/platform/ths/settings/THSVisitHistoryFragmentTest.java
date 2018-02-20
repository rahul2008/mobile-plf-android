/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.ths.settings;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSVisitHistoryFragmentTest {

    private THSVisitHistoryFragment mThsVisitHistoryFragment;

    @Mock
    private ConsumerManager consumerManagerMock;

    @Mock
    private AWSDK awsdkMock;

    @Mock
    private Consumer consumerMock;

    @Mock
    private VisitReport visitReportMock;

    @Mock
    private THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    private THSConsumer thsConsumerMock;

    @Mock
    private ActionBarListener actionBarListenerMock;

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private AppTaggingInterface appTaggingInterface;

    @Mock
    private THSSDKError thssdkErrorMock;

    @Mock
    private SDKError sdkErrorMock;

    @Mock
    private AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    private ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    private LoggingInterface loggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

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

        mThsVisitHistoryFragment = new THSVisitHistoryFragmentMock();
        mThsVisitHistoryFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mThsVisitHistoryFragment);
    }

    @Test
    public void updateVisitHistoryView() throws Exception {
        List<VisitReport> visitList = new ArrayList<>();
        visitList.add(visitReportMock);
        mThsVisitHistoryFragment.updateVisitHistoryView(visitList);

        assertTrue(mThsVisitHistoryFragment.mThsVisitHistoryAdapter.getItemCount() == 1);
    }

}
