/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Label;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSDependentListAdapterTest {

    @Mock
    THSConsumer thsConsumer1;

    @Mock
    THSConsumer thsConsumer2;

    @Mock
    THSConsumer parent;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumerWrapper thsConsumerWrapperMock;

    @Mock
    Consumer consumerMock;

    THSDependentListAdapter mTHSDependentListAdapter;

    @Mock
    OnItemClickListener onItemClickListenerMock;

    @Mock
    ViewGroup viewGroupMock;

    @Mock
    Context contextMock;

    @Mock
    ImageView imageViewMock;

    @Mock
    Label labelMock;

    @Mock
    RelativeLayout relativeLayoutMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List list = new ArrayList();
        list.add(thsConsumer1);
        list.add(thsConsumer2);
        parent.setDependents(list);
        when(parent.getDependents()).thenReturn(list);
        THSManager.getInstance().setThsParentConsumer(parent);
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setPTHConsumer(thsConsumerWrapperMock);
        when(thsConsumerWrapperMock.getConsumer()).thenReturn(consumerMock);

        mTHSDependentListAdapter = new THSDependentListAdapter(contextMock);
    }

    @Test
    public void setOnItemClickListener() throws Exception {
        mTHSDependentListAdapter.setOnItemClickListener(onItemClickListenerMock);
        assertNotNull(mTHSDependentListAdapter.mOnItemClickListener);
        assertThat(mTHSDependentListAdapter.mOnItemClickListener).isInstanceOf(OnItemClickListener.class);
    }

    @Test
    public void onBindViewHolder() throws Exception {
        THSDependentListAdapter.CustomViewHolder customViewHolder = new THSDependentListAdapter.CustomViewHolder(viewGroupMock);
        customViewHolder.logo = imageViewMock;
        customViewHolder.label = labelMock;
        customViewHolder.relativeLayout = relativeLayoutMock;
        customViewHolder.initials = labelMock;
        mTHSDependentListAdapter.onBindViewHolder(customViewHolder,0);
        verify(customViewHolder.relativeLayout).setOnClickListener(any(View.OnClickListener.class));
    }

    @Test
    public void getItemCount() throws Exception {
        final int itemCount = mTHSDependentListAdapter.getItemCount();
        assert itemCount == 2;
    }

}