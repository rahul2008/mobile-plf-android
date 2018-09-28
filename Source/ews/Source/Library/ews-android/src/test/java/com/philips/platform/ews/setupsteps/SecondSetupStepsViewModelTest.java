/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.setupsteps;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.HappyFlowContentConfiguration;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.util.GpsUtil;
import com.philips.platform.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GpsUtil.class, EWSLogger.class, EWSTagger.class})
public class SecondSetupStepsViewModelTest {

    @Mock
    private Navigator navigatorMock;

    private SecondSetupStepsViewModel subject;

    @Mock
    private StringProvider mockStringProvider;

    @Mock
    private HappyFlowContentConfiguration mockHappyFlowConfiguration;

    @Mock
    private BaseContentConfiguration baseContentConfiguration;

    @Mock
    private EWSTagger mockEWSTagger;

    @Mock
    private EWSLogger mockEWSLogger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSLogger.class);
        mockStatic(EWSTagger.class);
        subject = new SecondSetupStepsViewModel(navigatorMock,
                mockStringProvider, mockHappyFlowConfiguration, baseContentConfiguration, mockEWSTagger, mockEWSLogger);
    }

    @Test
    public void itShouldShowChooseCurrentStateScreenWhenNoButtonIsClicked() throws Exception {
        subject.onNoButtonClicked();
        verify(navigatorMock).navigateToResetConnectionTroubleShootingScreen();
    }

    @Test
    public void itShouldSendWifiBlinkingActionTagOnNextButtonClick() throws Exception {
        subject.onNextButtonClicked();
        verify(mockEWSTagger).trackActionSendData("specialEvents", "wifiBlinking");
    }

    @Test
    public void itShouldSendWifiNotBlinkingActionTagOnNoButtonClick() throws Exception {
        subject.onNoButtonClicked();
        verify(mockEWSTagger).trackActionSendData("specialEvents", "wifiNotBlinking");
    }

    @Test
    public void itShouldStartConnection() throws Exception {
        subject.startConnection();
        verify(navigatorMock).navigateToConnectingPhoneToHotspotWifiScreen();
    }

    @Test
    public void itShouldCheckAnalyticsPageName() throws Exception {
        subject.trackPageName();
        verify(mockEWSTagger).trackPage("setupStep2");
    }

    @Test
    public void itShouldVerifyGetQuestionText() throws Exception {
        when(mockHappyFlowConfiguration.getSetUpVerifyScreenQuestion()).thenReturn(R.string.label_ews_verify_ready_question_default);
        subject.getQuestion(mockHappyFlowConfiguration);
        verify(mockStringProvider).getString(mockHappyFlowConfiguration.getSetUpVerifyScreenQuestion());
    }

    @Test
    public void itShouldVerifyTitleText() throws Exception {
        when(mockHappyFlowConfiguration.getSetUpVerifyScreenTitle()).thenReturn(R.string.label_ews_verify_ready_title_default);
        subject.getTitle(mockHappyFlowConfiguration);
        verify(mockStringProvider).getString(mockHappyFlowConfiguration.getSetUpVerifyScreenTitle());
    }

    @Test
    public void itShouldVerifyYesButtonText() throws Exception {
        when(mockHappyFlowConfiguration.getSetUpVerifyScreenYesButton()).thenReturn(R.string.button_ews_verify_ready_yes_default);
        subject.getYesButton(mockHappyFlowConfiguration);
        verify(mockStringProvider).getString(mockHappyFlowConfiguration.getSetUpVerifyScreenYesButton());
    }

    @Test
    public void itShouldVerifyNoButtonText() throws Exception {
        when(mockHappyFlowConfiguration.getSetUpVerifyScreenNoButton()).thenReturn(R.string.button_ews_verify_ready_no_default);
        subject.getNoButton(mockHappyFlowConfiguration);
        verify(mockStringProvider).getString(mockHappyFlowConfiguration.getSetUpVerifyScreenNoButton());
    }

    @Test
    public void itShouldShowNextPasswordEntryScreenWhenPhoneIsConnectedToApplianceHotspot() throws Exception {
        sendEventToShowPasswordEntryScreen();
        verify(navigatorMock).navigateToConnectToDeviceWithPasswordScreen(anyString());
    }

    @Test
    public void itShouldCallPostDelayedOnHandlerWhenConnectedToApplianceHotspot() throws Exception {
        subject.connectPhoneToDeviceHotspotWifi();
        verify(navigatorMock).navigateToConnectingPhoneToHotspotWifiScreen();
    }

    private void sendEventToShowPasswordEntryScreen() {
        subject.showPasswordEntryScreenEvent();
    }
}