/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirstSetupStepsViewModelTest {

    private FirstSetupStepsViewModel subject;

    @Mock private Navigator mockNavigator;

    @Mock private StringProvider mockStringProvider;

    @Mock private BaseContentConfiguration mockBaseContentConfiguration;

    @Mock private HappyFlowContentConfiguration mockHappyFlowConfiguration;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new FirstSetupStepsViewModel(mockNavigator, mockStringProvider, mockBaseContentConfiguration, mockHappyFlowConfiguration);
    }

    @Test
    public void shouldShowPressAndFollowSetupScreenWhenYesButtonIsClicked() throws Exception {
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
    public void itShouldVerifyTitleText() throws Exception{
        when(mockHappyFlowConfiguration.getSetUpScreenTitle()).thenReturn(R.string.label_ews_plug_in_title_default);
        subject.getTitle(mockHappyFlowConfiguration);
        verify(mockStringProvider).getString(mockHappyFlowConfiguration.getSetUpScreenTitle());
    }
}