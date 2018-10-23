/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.appointment;

import android.os.Build;
import android.os.Parcel;
import android.support.annotation.RequiresApi;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.AvailableProviders;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class THSAvailableProviderListTest {

    THSAvailableProviderList mThsAvailableProviderList;

    @Mock
    AvailableProviders availableProvidersMock;

    @Mock
    AvailableProvider availableProviderMock;

    @Mock
    Parcel parcelMock;

    @Mock
    Date dateMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mThsAvailableProviderList = new THSAvailableProviderList();
    }

    @Test
    public void getAvailableProviders() throws Exception {
        mThsAvailableProviderList.setAvailableProviders(availableProvidersMock);
        final AvailableProviders availableProviders = mThsAvailableProviderList.getAvailableProviders();
        assertNotNull(availableProviders);
        assertThat(availableProviders).isInstanceOf(AvailableProviders.class);
    }

    @Test
    public void getAvailableProvidersList() throws Exception {
        List availableList = new ArrayList();
        availableList.add(availableProviderMock);

        when(availableProvidersMock.getAvailableProviders()).thenReturn(availableList);

        mThsAvailableProviderList.setAvailableProviders(availableProvidersMock);
        final List<AvailableProvider> availableProvidersList = mThsAvailableProviderList.getAvailableProvidersList();
        assertNotNull(availableProvidersList);
        assertThat(availableProvidersList).isInstanceOf(List.class);
    }

    @Test
    public void getDate() throws Exception {
        mThsAvailableProviderList.setAvailableProviders(availableProvidersMock);
        when(availableProvidersMock.getDate()).thenReturn(dateMock);
        final Date date = mThsAvailableProviderList.getDate();
        assertNotNull(date);
        assertThat(date).isInstanceOf(Date.class);
    }

    @Test
    public void describeContents() throws Exception {
        final int i = mThsAvailableProviderList.describeContents();
        assert i == 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Test
    public void writeToParcel() throws Exception {
        mThsAvailableProviderList = new THSAvailableProviderList(parcelMock);
        mThsAvailableProviderList.writeToParcel(parcelMock,0);
    }
}