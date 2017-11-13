/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.setupsteps;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class FirstSetupStepsViewModelTest {

    private FirstSetupStepsViewModel subject;

    @Mock private Navigator mockNavigator;

    @Mock private StringProvider mockStringProvider;

    @Mock private BaseContentConfiguration mockBaseContentConfiguration;

    @Mock private HappyFlowContentConfiguration mockHappyFlowConfiguration;

    @Before
    public void setUp() throws Exception {
        mockStatic(EWSTagger.class);
        initMocks(this);
        subject = new FirstSetupStepsViewModel(mockNavigator, mockStringProvider, mockBaseContentConfiguration, mockHappyFlowConfiguration);
    }

    @Test
    public void itShouldShowPressAndFollowSetupScreenWhenYesButtonIsClicked() throws Exception {
        subject.onYesButtonClicked();
        verify(mockNavigator).navigateToCompletingDeviceSetupScreen();
    }

    @Test
    public void itShouldVerifyGetBody() throws Exception{
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(2131362066);
        subject.getBody(mockBaseContentConfiguration);
        verify(mockStringProvider).getString(R.string.label_ews_plug_in_body_default, mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldVerifyTitleText() throws Exception {
        when(mockHappyFlowConfiguration.getSetUpScreenTitle()).thenReturn(R.string.label_ews_plug_in_title_default);
        subject.getTitle(mockHappyFlowConfiguration);
        verify(mockStringProvider).getString(mockHappyFlowConfiguration.getSetUpScreenTitle());
    }

    @Test
    public void itShouldVerifyTaggerTrackPageCalledWithCorrectTag() throws Exception {
        subject.trackPageName();
        verifyStatic();
        EWSTagger.trackPage("setupStep1");
    }
}