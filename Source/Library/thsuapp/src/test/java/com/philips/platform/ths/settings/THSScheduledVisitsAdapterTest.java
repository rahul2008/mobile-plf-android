/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.settings;

import android.view.View;

import com.americanwell.sdk.entity.visit.Appointment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class THSScheduledVisitsAdapterTest {
    THSScheduledVisitsAdapter mTHSScheduledVisitsAdapter;

    @Mock
    THSScheduledVisitsFragment thsScheduledVisitsFragmentMock;

    @Mock
    Appointment appointmentMock;

    @Mock
    View viewMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        List list = new ArrayList();
        list.add(appointmentMock);
        mTHSScheduledVisitsAdapter = new THSScheduledVisitsAdapter(list,thsScheduledVisitsFragmentMock);
    }

    @Test
    public void onBindViewHolder() throws Exception {
        THSScheduledVisitsAdapter.CustomViewHolder customViewHolder = new THSScheduledVisitsAdapter.CustomViewHolder(viewMock);
       // mTHSScheduledVisitsAdapter.onBindViewHolder(customViewHolder,0);
    }

    @Test
    public void getItemCount() throws Exception {

    }

    @Test
    public void showCancelDialog() throws Exception {

    }

}