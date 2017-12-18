/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.setupsteps;

import android.app.Dialog;
import android.support.v4.app.Fragment;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.HappyFlowContentConfiguration;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.permission.PermissionHandler;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.util.GpsUtil;
import com.philips.platform.ews.util.StringProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.platform.ews.setupsteps.SecondSetupStepsViewModel.ACCESS_COARSE_LOCATION;
import static org.junit.Assert.assertTrue;
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

    @Mock
    private PermissionHandler permissionHandlerMock;

    @Mock
    private Fragment fragmentMock;

    private SecondSetupStepsViewModel subject;

    @Mock
    private StringProvider mockStringProvider;

    @Mock
    private HappyFlowContentConfiguration mockHappyFlowConfiguration;

    @Mock
    private BaseContentConfiguration baseContentConfiguration;

    @Mock
    private Dialog dialogMock;

    @Mock
    private SecondSetupStepsViewModel.LocationPermissionFlowCallback mockLocationPermissionFlowCallback;

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
                permissionHandlerMock,
                mockStringProvider, mockHappyFlowConfiguration, baseContentConfiguration, mockEWSTagger, mockEWSLogger);

        subject.setFragment(fragmentMock);
    }

    @Test
    public void itShouldRequestForLocationPermissionIfItsNotGrantedAlreadyWhenONextButtonClicked() throws Exception {
        setPermissionGranted(false);
        subject.setLocationPermissionFlowCallback(mockLocationPermissionFlowCallback);
        subject.onNextButtonClicked();
        verify(mockLocationPermissionFlowCallback).showLocationPermissionDialog(baseContentConfiguration);
    }

    @Test
    public void itShouldShowGPSEnableDialogIfGPSIsOffWhenONextButtonClicked() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(false, true);
        subject.onNextButtonClicked();
    }

    @Test
    public void itShouldSendRequestToConnectToApplianceHotspotWhenPermissionIsExplicitlyGrantedByUser() throws Exception {
        final int[] grantedPermission = new int[1];
        when(permissionHandlerMock.areAllPermissionsGranted(grantedPermission)).thenReturn(true);
        assertTrue(subject.areAllPermissionsGranted(grantedPermission));
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
    public void itShouldVerifyLocationPermissionInAppNotificationTag() throws Exception {
        subject.tagLocationPermission();
        verify(mockEWSTagger).trackInAppNotification("setupStep2", "Location Permission");
    }

    @Test
    public void itShouldVerifyAllowInAppNotificationResponseTag() throws Exception {
        subject.tagLocationPermissionAllow();
        verify(mockEWSTagger).trackInAppNotificationResponse("Allow");
    }

    @Test
    public void itShouldVerifyCancelInAppNotificationResponseTag() throws Exception {
        subject.tagLocationPermissionCancel();
        verify(mockEWSTagger).trackInAppNotificationResponse("Cancel setup");
    }

    @Test
    public void itShouldVerifyLocationDisabledInAppNotificationTag() throws Exception {
        subject.tagLocationDisabled();
        verify(mockEWSTagger).trackInAppNotification("setupStep2", "Location Disabled");
    }

    @Test
    public void itShouldVerifyLocationOpenSettingsInAppNotificationResponseTag() throws Exception {
        subject.tagLocationOpenSettings();
        verify(mockEWSTagger).trackInAppNotificationResponse("openLocationSettings");
    }

    private void setPermissionGranted(final boolean permissionGranted) {
        when(permissionHandlerMock.hasPermission(fragmentMock.getContext(), ACCESS_COARSE_LOCATION)).
                thenReturn(permissionGranted);
    }

    private void stubGPSSettings(final boolean enabled, final boolean isGpsRequiredForWifiScan) {
        mockStatic(GpsUtil.class);
        when(GpsUtil.isGPSRequiredForWifiScan()).thenReturn(isGpsRequiredForWifiScan);
        when(GpsUtil.isGPSEnabled(fragmentMock.getContext())).thenReturn(enabled);
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
    public void itShouldRequestForLocationPermissionIfItsNotGrantedAlreadyWhenAsked() throws Exception {
        setPermissionGranted(false);
        subject.setLocationPermissionFlowCallback(mockLocationPermissionFlowCallback);
        subject.connectPhoneToDeviceHotspotWifi();

        verify(mockLocationPermissionFlowCallback).showLocationPermissionDialog(baseContentConfiguration);
    }

    @Test
    public void itShouldShowGPSEnableDialogIfGPSIsOffWhenAsked() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(false, true);
        subject.setLocationPermissionFlowCallback(mockLocationPermissionFlowCallback);
        subject.connectPhoneToDeviceHotspotWifi();

        verify(mockLocationPermissionFlowCallback).showGPSEnableDialog(baseContentConfiguration);
    }


    @Test
    public void itShouldShowNextPasswordEntryScreenWhenPhoneIsConnectedToApplianceHotspot() throws Exception {
        sendEventToShowPasswordEntryScreen();

        verify(navigatorMock).navigateToConnectToDeviceWithPasswordScreen(anyString());
    }

    private void sendEventToShowPasswordEntryScreen() {
        subject.showPasswordEntryScreenEvent();
    }

    @Test
    public void itShouldCallPostDelayedOnHandlerWhenConnectedToApplianceHotspot() throws Exception {
        stubGPSSettings(true, true);
        setPermissionGranted(true);

        subject.connectPhoneToDeviceHotspotWifi();

        verify(navigatorMock).navigateToConnectingPhoneToHotspotWifiScreen();
    }

    @Test
    public void itShouldConnectToApplianceHotspotWhenLocationPermissionIsAlreadyGrantedWhenAsked() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(true, true);

        subject.connectPhoneToDeviceHotspotWifi();

        verifyConnectRequest();
    }

    private void verifyConnectRequest() {
        verify(navigatorMock).navigateToConnectingPhoneToHotspotWifiScreen();
    }
}