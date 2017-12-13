/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.configuration;

import android.os.Parcel;

import com.philips.platform.ews.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class BaseContentConfigurationTest {

    private BaseContentConfiguration subject;

    @Mock
    Parcel mockParcel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new BaseContentConfiguration.Builder().build();
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

    @Test
    public void itShouldVerifySetDeviceNameWithBuilder() {
        subject = new BaseContentConfiguration.Builder()
                .setDeviceName(R.string.label_ews_password_body)
                .setAppName(R.string.label_ews_cancel_setup_body)
                .build();
        assertNotNull(subject);
        assertNotEquals(subject.getDeviceName(),R.string.ews_device_name_default);
        assertEquals(subject.getDeviceName(), R.string.label_ews_password_body);
    }

    @Test
    public void itShouldVerifySetAppNameWithBuilder() {
        subject = new BaseContentConfiguration.Builder()
                .setDeviceName(R.string.label_ews_password_body)
                .setAppName(R.string.label_ews_cancel_setup_body)
                .build();
        assertNotNull(subject);
        assertNotEquals(subject.getAppName(), R.string.ews_app_name_default);
        assertEquals(subject.getAppName(), R.string.label_ews_cancel_setup_body);
    }

    @Test
    public void itShouldVerifyParcelReadForSpecifiedTimes(){
        new BaseContentConfiguration(mockParcel);
        verify(mockParcel, times(2)).readInt();
    }


}