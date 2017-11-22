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

public class HappyFlowContentConfigurationTest {

    private HappyFlowContentConfiguration subject;

    @Mock
    private Parcel mockParcel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new HappyFlowContentConfiguration.Builder().build();
    }

    @Test
    public void itShouldVerifyDefaultGettingStartedScreenTitle() throws Exception{
        assertEquals(subject.getGettingStartedScreenTitle(), R.string.label_ews_get_started_title_default);
    }

    @Test
    public void itShouldVerifyDefaultGettingStartedScreenImage() throws Exception{
        assertEquals(subject.getGettingStartedScreenImage(), R.drawable.philips_logo_default);
    }

    @Test
    public void itShouldVerifyDefaultSetupScreenTitle() throws Exception{
        assertEquals(subject.getSetUpScreenTitle(), R.string.label_ews_plug_in_title_default);
    }

    @Test
    public void itShouldVerifyDefaultSetupScreenBody() throws Exception{
        assertEquals(subject.getSetUpScreenBody(), R.string.label_ews_plug_in_body_default);
    }

    @Test
    public void itShouldVerifyDefaultSetupScreenImage() throws Exception{
        assertEquals(subject.getSetUpScreenImage(), R.drawable.power_button_default);
    }

    @Test
    public void itShouldVerifyDefaultSetupVerifyScreenTitle() throws Exception{
        assertEquals(subject.getSetUpVerifyScreenTitle(), R.string.label_ews_verify_ready_title_default);
    }

    @Test
    public void itShouldVerifyDefaultSetupVerifyScreenQuestion() throws Exception{
        assertEquals(subject.getSetUpVerifyScreenQuestion(), R.string.label_ews_verify_ready_question_default);
    }

    @Test
    public void itShouldVerifyDefaultSetupVerifyScreenYesButton() throws Exception{
        assertEquals(subject.getSetUpVerifyScreenYesButton(), R.string.button_ews_verify_ready_yes_default);
    }

    @Test
    public void itShouldVerifyDefaultSetupVerifyScreenNoButton() throws Exception{
        assertEquals(subject.getSetUpVerifyScreenNoButton(), R.string.button_ews_verify_ready_no_default);
    }

    @Test
    public void itShouldVerifyDefaultSetupVerifyScreenImage() throws Exception{
        assertEquals(subject.getSetUpVerifyScreenImage(), R.drawable.power_button_default);
    }

    @Test
    public void itShouldReturnZeroOnDescribeContents() {
        assertEquals(subject.describeContents(), 0);
    }

    @Test
    public void itShouldVerifySetGettingStartedTitleWithBuilder() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setGettingStartedScreenTitle(R.string.ews_title).build();
        assertEquals(subject.getGettingStartedScreenTitle(), R.string.ews_title);
    }


    @Test
    public void itShouldVerifySetupScreenTitle() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setSetUpScreenTitle(R.string.ews_title).build();
        assertEquals(subject.getSetUpScreenTitle(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifySetupScreenBody() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setSetUpScreenBody(R.string.ews_title).build();
        assertEquals(subject.getSetUpScreenBody(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifySetupScreenImage() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setSetUpScreenImage(R.drawable.ews_onboarding_image).build();
        assertEquals(subject.getSetUpScreenImage(), R.drawable.ews_onboarding_image);
    }

    @Test
    public void itShouldVerifySetupVerifyScreenTitle() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setSetUpVerifyScreenTitle(R.string.ews_title).build();
        assertEquals(subject.getSetUpVerifyScreenTitle(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifySetupVerifyScreenQuestion() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setSetUpVerifyScreenQuestion(R.string.ews_title).build();
        assertEquals(subject.getSetUpVerifyScreenQuestion(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifySetupVerifyScreenYesButton() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setSetUpVerifyScreenYesButton(R.string.ews_title).build();
        assertEquals(subject.getSetUpVerifyScreenYesButton(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifySetupVerifyScreenNoButton() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setSetUpVerifyScreenNoButton(R.string.ews_title).build();
        assertEquals(subject.getSetUpVerifyScreenNoButton(), R.string.ews_title);
    }

    @Test
    public void itShouldVerifySetupVerifyScreenImage() throws Exception{
        subject = new HappyFlowContentConfiguration.Builder().setSetUpVerifyScreenImage(R.drawable.ews_onboarding_image).build();
        assertEquals(subject.getSetUpVerifyScreenImage(), R.drawable.ews_onboarding_image);
    }

    @Test
    public void itShouldWriteOnParcelDestOnWriteToParcel() {
        subject.writeToParcel(mockParcel, anyInt());
        verify(mockParcel, times(10)).writeInt(anyInt());
    }

    @Test
    public void itShouldVerifyParcelReadForSpecifiedTimes(){
        new HappyFlowContentConfiguration(mockParcel);
        verify(mockParcel, times(10)).readInt();
    }

}