/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class StartConnectWithDeviceViewModelTest {

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

    private StartConnectWithDeviceViewModel subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new StartConnectWithDeviceViewModel(navigatorMock, stringProviderMock, happyFlowContentConfigurationMock, baseContentConfigurationMock);
        when(baseContentConfigurationMock.getDeviceName()).thenReturn(123435);
    }

    @Test
    public void shouldNavigateToHomeWifiScreenIfWifiIsEnabledWhenClickedOnGettingStartedButton() throws Exception {
        stubHomeWiFiStatus();
        verifyStatic(times(1));
        EWSTagger.trackActionSendData("specialEvents", "getStartedToConnectWiFi");

        verify(navigatorMock).navigateToHomeNetworkConfirmationScreen();
    }

    private void stubHomeWiFiStatus() {
        subject.onGettingStartedButtonClicked();
    }

    @Test
    public void itShouldVerifyBackPressEventWhenCalled() throws Exception {
        subject.onBackPressed(ewsCallbackNotifierMock);
        verify(ewsCallbackNotifierMock, times(1)).onBackPressed();
    }

    @Test
    public void itShouldVerifyTitleForViewModel() throws Exception {
        subject.getTitle(happyFlowContentConfigurationMock, baseContentConfigurationMock);
        verify(stringProviderMock).getString(happyFlowContentConfigurationMock.getGettingStartedScreenTitle(),
                baseContentConfigurationMock.getDeviceName());
    }

    @Test
    public void itShouldVerifyNoteForViewModel() throws Exception {
        subject.getNote(baseContentConfigurationMock);
        verify(stringProviderMock).getString(R.string.label_ews_get_started_description,
                baseContentConfigurationMock.getDeviceName());
    }
}