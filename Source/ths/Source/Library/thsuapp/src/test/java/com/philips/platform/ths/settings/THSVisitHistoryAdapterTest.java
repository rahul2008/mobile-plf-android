/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.entity.visit.VisitSchedule;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.pharmacy.THSPharmacyListFragment;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSVisitHistoryAdapterTest {

    THSVisitHistoryAdapter mTHSVisitHistoryAdapter;

    @Mock
    Context contextMock;

    @Mock
    VisitSchedule visitScheduleMock;


    THSVisitHistoryFragment thsVisitHistoryFragment;

    @Mock
    Label labelMock;

    @Mock
    VisitReport thsVisitReportMock;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    View.OnClickListener onClickMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    THSConsumer thsConsumer;

    @Mock
    Consumer consumerMock;

    @Mock
    ViewGroup viewGroupMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    THSPharmacyListFragment thsPharmacyListFragment;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List list = new ArrayList();
        list.add(thsVisitReportMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);
        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumer);
        THSManager.getInstance().setThsConsumer(thsConsumer);
        when(thsConsumer.getConsumer()).thenReturn(consumerMock);
        thsVisitHistoryFragment = new THSVisitHistoryFragmentMock();
        mTHSVisitHistoryAdapter = new THSVisitHistoryAdapter(list,thsVisitHistoryFragment);
        SupportFragmentTestUtil.startFragment(thsVisitHistoryFragment);
    }

    @Test
    public void onBindViewHolder() throws Exception {
        THSVisitHistoryAdapter.CustomViewHolder customViewHolder = new THSVisitHistoryAdapter.CustomViewHolder(viewGroupMock);
        when(thsVisitReportMock.getSchedule()).thenReturn(visitScheduleMock);
        when(visitScheduleMock.getActualStartTime()).thenReturn(43L);
        customViewHolder.mLabelAppointmrntDate = labelMock;
        customViewHolder.mLabelProviderName = labelMock;
        mTHSVisitHistoryAdapter.onBindViewHolder(customViewHolder,0);
        verify(customViewHolder.mLabelAppointmrntDate,times(1)).setText(anyString());
    }

    @Test
    public void getItemCount() throws Exception {
        final int itemCount = mTHSVisitHistoryAdapter.getItemCount();
        assert itemCount == 1;

    }

 /*   @Test
    public void onCreateViewHolder(){
        when(viewGroupMock.getContext()).thenReturn(contextMock);
        mTHSVisitHistoryAdapter.onCreateViewHolder(viewGroupMock,0);
    }
*/
}