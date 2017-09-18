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
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyByte;
import static org.mockito.Matchers.anyList;
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
    THSConsumer thsConsumerMock;

    @Mock
    View.OnClickListener onClickMock;

    @Mock
    ConsumerManager consumerManagerMock;

    @Mock
    Consumer consumerMock;

    @Mock
    ViewGroup viewGroupMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List list = new ArrayList();
        list.add(thsVisitReportMock);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);
        when(awsdkMock.getConsumerManager()).thenReturn(consumerManagerMock);
        thsVisitHistoryFragment = new THSVisitHistoryFragmentMock();
        SupportFragmentTestUtil.startFragment(thsVisitHistoryFragment);
        mTHSVisitHistoryAdapter = new THSVisitHistoryAdapter(list,thsVisitHistoryFragment);
    }

    @Test
    public void onBindViewHolder() throws Exception {
        THSVisitHistoryAdapter.CustomViewHolder customViewHolder = new THSVisitHistoryAdapter.CustomViewHolder(viewGroupMock);
        when(thsVisitReportMock.getSchedule()).thenReturn(visitScheduleMock);
        when(visitScheduleMock.getActualStartTime()).thenReturn(43L);
        customViewHolder.mLabelAppointmrntDate = labelMock;
        customViewHolder.mLabelProviderName = labelMock;
        mTHSVisitHistoryAdapter.onBindViewHolder(customViewHolder,0);
        verify(customViewHolder.mLabelAppointmrntDate,times(2)).setText(anyString());
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