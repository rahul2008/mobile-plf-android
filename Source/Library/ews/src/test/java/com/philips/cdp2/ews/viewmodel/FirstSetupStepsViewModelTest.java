/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class FirstSetupStepsViewModelTest {

    @InjectMocks private FirstSetupStepsViewModel subject;

    @Mock private Navigator mockNavigator;

    @Mock private StringProvider mockStringProvider;

    @Mock private BaseContentConfiguration mockBaseContentConfiguration;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldShowPressAndFollowSetupScreenWhenYesButtonIsClicked() throws Exception {
        subject.onYesButtonClicked();

        verify(mockNavigator).navigateToCompletingDeviceSetupScreen();
    }
}