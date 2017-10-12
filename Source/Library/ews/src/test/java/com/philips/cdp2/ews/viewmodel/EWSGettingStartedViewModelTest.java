/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.navigation.Navigator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, manifest = Config.NONE)
public class EWSGettingStartedViewModelTest {

    @Mock
    private Navigator navigatorMock;

    private EWSGettingStartedViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new EWSGettingStartedViewModel(navigatorMock);
    }

    @Test
    public void shouldNavigateToHomeWifiScreenIfWifiIsEnabledWhenClickedOnGettingStartedButton() throws Exception {
        stubHomeWiFiStatus();

        verify(navigatorMock).navigateToHomeNetworkConfirmationScreen();
    }

    private void stubHomeWiFiStatus() {
        viewModel.onGettingStartedButtonClicked();
    }
}