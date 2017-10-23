/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, manifest = Config.NONE)
public class EWSGettingStartedViewModelTest {

    @Mock
    private Navigator navigatorMock;

    @Mock
    private StringProvider stringProviderMock;

    @Mock
    private HappyFlowContentConfiguration happyFlowContentConfigurationMock;

    @Mock
    private BaseContentConfiguration baseContentConfigurationMock;

    @Mock
    private EWSCallbackNotifier ewsCallbackNotifierMock;

    private EWSGettingStartedViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new EWSGettingStartedViewModel(navigatorMock, stringProviderMock, happyFlowContentConfigurationMock, baseContentConfigurationMock);
        when(baseContentConfigurationMock.getDeviceName()).thenReturn(123435);
    }

    @Test
    public void itShouldGiveTitle() throws Exception {
        viewModel.getTitle(happyFlowContentConfigurationMock, baseContentConfigurationMock);
        verify(stringProviderMock).getString(happyFlowContentConfigurationMock.getGettingStartedScreenTitle(),
                baseContentConfigurationMock.getDeviceName());
    }

    @Test
    public void itShouldGiveNote() throws Exception {
        viewModel.getNote(baseContentConfigurationMock);
        verify(stringProviderMock).getString(R.string.label_ews_get_started_description,
                baseContentConfigurationMock.getDeviceName());
    }

    @Test
    public void shouldNavigateToHomeWifiScreenIfWifiIsEnabledWhenClickedOnGettingStartedButton() throws Exception {
        stubHomeWiFiStatus();

        verify(navigatorMock).navigateToHomeNetworkConfirmationScreen();
    }

    private void stubHomeWiFiStatus() {
        viewModel.onGettingStartedButtonClicked();
    }

    @Test
    public void itShouldGoBackWork() throws Exception {
        viewModel.onBackPressed(ewsCallbackNotifierMock);
        verify(ewsCallbackNotifierMock, times(1)).onBackPressed();
    }
}