/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.os.Parcel;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderInfo;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class THSAvailableProviderTest {

    @Mock
    AvailableProvider availableProviderMock;

    THSAvailableProvider mThsAvailableProvider;

    @Mock
    ProviderInfo providerInfoMock;

    @Mock
    Date dateMock;

    @Mock
    Parcel parcel;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsAvailableProvider  = new THSAvailableProvider();
        mThsAvailableProvider.setAvailableProvider(availableProviderMock);
    }

    @Test
    public void getAvailableProvider() throws Exception {
        final AvailableProvider availableProvider = mThsAvailableProvider.getAvailableProvider();
        assertThat(availableProvider).isInstanceOf(AvailableProvider.class);
        assertNotNull(availableProvider);
    }

    @Test
    public void getProviderInfo() throws Exception {
        when(availableProviderMock.getProviderInfo()).thenReturn(providerInfoMock);
        final ProviderInfo providerInfo = mThsAvailableProvider.getProviderInfo();
        assertThat(providerInfo).isInstanceOf(ProviderInfo.class);
        assertNotNull(providerInfo);
    }

    @Test
    public void getAvailableAppointmentTimeSlots() throws Exception {
        List list = new ArrayList();
        list.add(dateMock);

        when(availableProviderMock.getAvailableAppointmentTimeSlots()).thenReturn(list);
        final List<Date> availableAppointmentTimeSlots = mThsAvailableProvider.getAvailableAppointmentTimeSlots();
        assertThat(availableAppointmentTimeSlots).isInstanceOf(List.class);
        assertNotNull(availableAppointmentTimeSlots);
        assertEquals(list.size(),1);
    }

    @Test
    public void THSAvailableProvidertest() {
        mThsAvailableProvider = new THSAvailableProvider(parcel);
        assertThat(mThsAvailableProvider).isInstanceOf(THSAvailableProvider.class);
        assertNotNull(mThsAvailableProvider);
    }

}