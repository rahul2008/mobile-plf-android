/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest(Parcel.class)
public class ContentConfigurationTest {

    @Mock
    BaseContentConfiguration mockBaseContentConfiguration;
    @Mock
    HappyFlowContentConfiguration mockHappyFlowContentConfiguration;
    @Mock
    TroubleShootContentConfiguration mockTroubleShootContentConfiguration;
    @Mock
    Parcel mockParcel;

    ContentConfiguration subject;
    ContentConfiguration subject1;

    @Before
    public void setUp() throws Exception {
        mockStatic(Parcel.class);
        initMocks(this);
        subject = new ContentConfiguration(mockBaseContentConfiguration,mockHappyFlowContentConfiguration,mockTroubleShootContentConfiguration);
        subject1 = new ContentConfiguration();
    }

    @Test
    public void itShouldReturnMockBaseContentConfig() throws Exception {
        assertSame(subject.getBaseContentConfiguration(), mockBaseContentConfiguration);
    }

    @Test
    public void itShouldReturnMockHappyFlowContentConfig() throws Exception {
        assertSame(subject.getHappyFlowContentConfiguration(), mockHappyFlowContentConfiguration);
    }

    @Test
    public void itShouldReturnMockTroubleShootContentConfig() throws Exception {
        assertSame(subject.getTroubleShootContentConfiguration(), mockTroubleShootContentConfiguration);
    }

    @Test
    public void itShouldVerifyBaseContentConfigIsNonNull() throws Exception {
        assertNotSame(subject1.getBaseContentConfiguration(), mockBaseContentConfiguration);
        assertNotNull(subject1.getBaseContentConfiguration());
    }

    @Test
    public void itShouldVerifyHappyFlowContentConfigIsNonNull() throws Exception {
        assertNotNull(subject1.getHappyFlowContentConfiguration());
    }

    @Test
    public void itShouldVerifyTroubleShootContentConfigIsNonNull() throws Exception {
        assertNotNull(subject1.getTroubleShootContentConfiguration());
    }

    @Test
    public void itShouldReturnZeroOnDescribeContents() throws Exception {
        assertEquals(subject.describeContents(), 0);
    }

    @Test
    public void itShouldWriteOnParcelDestOnWriteToParcel() throws Exception {
        int flag = 1;
        subject.writeToParcel(mockParcel,flag);
        verify(mockParcel).writeParcelable(mockBaseContentConfiguration,flag);
        verify(mockParcel).writeParcelable(mockHappyFlowContentConfiguration,flag);
        verify(mockParcel).writeParcelable(mockTroubleShootContentConfiguration,flag);
    }

}