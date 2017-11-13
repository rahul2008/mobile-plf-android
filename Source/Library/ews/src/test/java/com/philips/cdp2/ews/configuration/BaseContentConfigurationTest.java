/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.configuration;

import android.os.Parcel;

import com.philips.cdp2.ews.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class BaseContentConfigurationTest {

    @InjectMocks
    BaseContentConfiguration subject;

    @Mock
    Parcel mockParcel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void itShouldReturnDefaultDeviceNameOnGetDeviceName() {
        assertEquals(subject.getDeviceName(), R.string.ews_device_name_default);
    }

    @Test
    public void itShouldReturnDefaultAppNameOnGetAppName() {
        assertEquals(subject.getAppName(), R.string.ews_app_name_default);
    }

    @Test
    public void itShouldReturnZeroOnDescribeContents() {
        assertEquals(subject.describeContents(), 0);
    }

    @Test
    public void itShouldWriteOnParcelDestOnWriteToParcel() {
        subject.writeToParcel(mockParcel, anyInt());
        verify(mockParcel, times(2)).writeInt(anyInt());
    }
}