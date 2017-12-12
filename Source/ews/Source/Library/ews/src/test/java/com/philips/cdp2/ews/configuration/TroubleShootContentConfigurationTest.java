/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.configuration;

import android.os.Parcel;

import com.philips.cdp2.ews.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class TroubleShootContentConfigurationTest {

    private TroubleShootContentConfiguration subject;

    @Mock
    private Parcel mockParcel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new TroubleShootContentConfiguration.Builder().build();
    }

    @Test
    public void itShouldVerifyResetConnectionTitle() throws Exception{
        assertEquals(subject.getResetConnectionTitle(), R.string.label_ews_support_reset_connection_title_default);
    }

    @Test
    public void itShouldVerifyResetConnectionBody() throws Exception{
        assertEquals(subject.getResetConnectionBody(), R.string.label_ews_support_reset_connection_body_default);
    }

    @Test
    public void itShouldVerifyResetConnectionImage() throws Exception{
        assertEquals(subject.getResetConnectionImage(), R.drawable.philips_logo_default);
    }

    @Test
    public void itShouldVerifyResetDeviceTitle() throws Exception{
        assertEquals(subject.getResetDeviceTitle(), R.string.label_ews_support_reset_device_title_default);
    }

    @Test
    public void itShouldVerifyResetDeviceConnectionBody() throws Exception{
        assertEquals(subject.getResetDeviceBody(), R.string.label_ews_support_reset_device_body_default);
    }

    @Test
    public void itShouldVerifyResetDeviceConnectionImage() throws Exception{
        assertEquals(subject.getResetDeviceImage(), R.drawable.philips_logo_default);
    }

    @Test
    public void itShouldSetupAccessPointTitle() throws Exception{
        assertEquals(subject.getSetUpAccessPointTitle(), R.string.label_ews_support_setup_access_point_title_default);
    }

    @Test
    public void itShouldSetupAccessPointBody() throws Exception{
        assertEquals(subject.getSetUpAccessPointBody(), R.string.label_ews_support_setup_access_point_body_default);
    }

    @Test
    public void itShouldSetupAccessPointImage() throws Exception{
        assertEquals(subject.getSetUpAccessPointImage(), R.drawable.philips_logo_default);
    }

    @Test
    public void itShouldConnectWrongPhoneTitle() throws Exception{
        assertEquals(subject.getConnectWrongPhoneTitle(), R.string.label_ews_support_wrong_phone_title_default);
    }

    @Test
    public void itShouldConnectWrongPhoneBody() throws Exception{
        assertEquals(subject.getConnectWrongPhoneBody(), R.string.label_ews_support_wrong_phone_body_default);
    }

    @Test
    public void itShouldConnectWrongPhoneImage() throws Exception{
        assertEquals(subject.getConnectWrongPhoneImage(), R.drawable.philips_logo_default);
    }

    @Test
    public void itShouldConnectWrongPhoneQuestion() throws Exception{
        assertEquals(subject.getConnectWrongPhoneQuestion(), R.string.label_ews_support_wrong_phone_question_default);
    }

    @Test
    public void itShouldReturnZeroOnDescribeContents() {
        assertEquals(subject.describeContents(), 0);
    }

    @Test
    public void itShouldVerifyResetConnnectionTitleWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setResetConnectionTitle(R.string.ews_title).build();
        assertEquals(subject.getResetConnectionTitle(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifyResetConnnectionBodyWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setResetConnectionBody(R.string.ews_title).build();
        assertEquals(subject.getResetConnectionBody(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifyResetConnnectionImageWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setResetConnectionImage(R.drawable.ic_ews_wrong_phone_connected).build();
        assertEquals(subject.getResetConnectionImage(), R.drawable.ic_ews_wrong_phone_connected);
    }

    @Test
    public void itShouldVerifyResetDeviceTitleWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setResetDeviceTitle(R.string.ews_title).build();
        assertEquals(subject.getResetDeviceTitle(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifyResetDeviceBodyWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setResetDeviceBody(R.string.ews_title).build();
        assertEquals(subject.getResetDeviceBody(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifyResetDeviceImageWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setResetDeviceImage(R.drawable.ic_ews_wrong_phone_connected).build();
        assertEquals(subject.getResetDeviceImage(), R.drawable.ic_ews_wrong_phone_connected);
    }

    @Test
    public void itShouldVerifySetUpAccessTitleWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setSetUpAccessPointTitle(R.string.ews_title).build();
        assertEquals(subject.getSetUpAccessPointTitle(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifySetupAccessBodyWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setSetUpAccessPointBody(R.string.ews_title).build();
        assertEquals(subject.getSetUpAccessPointBody(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifySetUpAccessImageWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setSetUpAccessPointImage(R.drawable.ic_ews_wrong_phone_connected).build();
        assertEquals(subject.getSetUpAccessPointImage(), R.drawable.ic_ews_wrong_phone_connected);
    }

    @Test
    public void itShouldVerifyConnectPhoneTitleWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setConnectWrongPhoneTitle(R.string.ews_title).build();
        assertEquals(subject.getConnectWrongPhoneTitle(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifyConnectPhoneBodyWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setConnectWrongPhoneBody(R.string.ews_title).build();
        assertEquals(subject.getConnectWrongPhoneBody(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifyConnectPhoneImageWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setConnectWrongPhoneImage(R.drawable.ic_ews_wrong_phone_connected).build();
        assertEquals(subject.getConnectWrongPhoneImage(), R.drawable.ic_ews_wrong_phone_connected);
    }

    @Test
    public void itShouldVerifyConnectWrongPhoneQuestionWithBuilder() throws Exception{
        subject = new TroubleShootContentConfiguration.Builder().setConnectWrongPhoneQuestion(R.string.ews_title).build();
        assertEquals(subject.getConnectWrongPhoneQuestion(), R.string.ews_title);
    }


    @Test
    public void itShouldVerifyParcelReadForSpecifiedTimes(){
        new TroubleShootContentConfiguration(mockParcel);
        verify(mockParcel, times(13)).readInt();
    }

    @Test
    public void itShouldWriteOnParcelDestOnWriteToParcel() {
        subject.writeToParcel(mockParcel, anyInt());
        verify(mockParcel, times(13)).writeInt(anyInt());
    }
}