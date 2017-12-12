/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.setupsteps;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.HappyFlowContentConfiguration;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.util.StringProvider;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class FirstSetupStepsViewModelTest {

    private FirstSetupStepsViewModel subject;

    @Mock private Navigator mockNavigator;

    @Mock private StringProvider mockStringProvider;

    @Mock private BaseContentConfiguration mockBaseContentConfiguration;

    @Mock private HappyFlowContentConfiguration mockHappyFlowConfiguration;

    @Mock private EWSTagger mockEWSTagger;
    @Before
    public void setUp() throws Exception {
        mockStatic(EWSTagger.class);
        initMocks(this);
        subject = new FirstSetupStepsViewModel(mockNavigator, mockStringProvider, mockBaseContentConfiguration, mockHappyFlowConfiguration, mockEWSTagger);
    }

    @Test
    public void itShouldShowPressAndFollowSetupScreenWhenYesButtonIsClicked() throws Exception {
        subject.onYesButtonClicked();
        verify(mockNavigator).navigateToCompletingDeviceSetupScreen();
    }

    @Test
    public void itShouldVerifyGetBody() throws Exception{
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(2131362066);
        when(mockHappyFlowConfiguration.getSetUpScreenBody()).thenReturn(R.string.label_ews_plug_in_body_default);
        subject.getBody(mockBaseContentConfiguration,mockHappyFlowConfiguration);
        verify(mockStringProvider).getString(mockHappyFlowConfiguration.getSetUpScreenBody(), mockBaseContentConfiguration.getDeviceName());
    }

    @Test
    public void itShouldVerifyTitleText() throws Exception {
        when(mockBaseContentConfiguration.getDeviceName()).thenReturn(2131362066);
        when(mockHappyFlowConfiguration.getSetUpScreenTitle()).thenReturn(R.string.label_ews_plug_in_title_default);
        subject.getTitle(mockHappyFlowConfiguration);
        verify(mockStringProvider).getString(mockHappyFlowConfiguration.getSetUpScreenTitle());
    }

    @Test
    public void itShouldVerifyTaggerTrackPageCalledWithCorrectTag() throws Exception {
        subject.trackPageName();
        verify(mockEWSTagger).trackPage("setupStep1");
    }
}