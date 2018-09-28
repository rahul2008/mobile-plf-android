/*
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights reserved.
 */
package com.philips.platform.ews.startconnectwithdevice;

import android.support.v4.app.Fragment;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.HappyFlowContentConfiguration;
import com.philips.platform.ews.navigation.FragmentNavigator;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.permission.PermissionHandler;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.util.GpsUtil;
import com.philips.platform.ews.util.StringProvider;
import com.philips.platform.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.platform.ews.startconnectwithdevice.StartConnectWithDeviceViewModel.ACCESS_COARSE_LOCATION;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GpsUtil.class, EWSTagger.class})
public class StartConnectWithDeviceViewModelTest {

    @Mock
    private Navigator navigatorMock;

    @Mock
    private StringProvider stringProviderMock;

    @Mock
    private StartConnectWithDeviceViewModel.ViewCallback dialogShowable;

    @Mock
    private WiFiUtil wiFiUtil;

    @Mock
    private HappyFlowContentConfiguration happyFlowContentConfigurationMock;

    @Mock
    private BaseContentConfiguration baseContentConfigurationMock;

    private StartConnectWithDeviceViewModel subject;

    @Mock
    private EWSTagger mockEWSTagger;

    @Mock
    private PermissionHandler permissionHandlerMock;

    @Mock
    private Fragment fragmentMock;

    @Mock
    private StartConnectWithDeviceViewModel.LocationPermissionFlowCallback mockLocationPermissionFlowCallback;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new StartConnectWithDeviceViewModel(navigatorMock, permissionHandlerMock, stringProviderMock, wiFiUtil,
                happyFlowContentConfigurationMock, baseContentConfigurationMock, mockEWSTagger);
        when(baseContentConfigurationMock.getDeviceName()).thenReturn(123435);
        subject.setFragment(fragmentMock);
    }

    @Test
    public void itShouldRequestForLocationPermissionIfItsNotGrantedAlreadyWhenONGetStartButtonClicked() throws Exception {
        setPermissionGranted(false);
        subject.setLocationPermissionFlowCallback(mockLocationPermissionFlowCallback);
        subject.onGettingStartedButtonClicked();
        verify(mockLocationPermissionFlowCallback).showLocationPermissionDialog(baseContentConfigurationMock);
    }

    @Test
    public void itShouldShowGPSEnableDialogIfGPSIsOffWhenONGetStartButtonClicked() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(false, true);
        subject.onGettingStartedButtonClicked();
    }

    @Test
    public void itShouldVerifyPermissionIsExplicitlyGrantedByUser() throws Exception {
        final int[] grantedPermission = new int[1];
        when(permissionHandlerMock.areAllPermissionsGranted(grantedPermission)).thenReturn(true);
        assertTrue(subject.areAllPermissionsGranted(grantedPermission));
    }

    @Test
    public void itShouldNavigateToPoweredOnScreenIfLocationPermissionIsGrantedAndWifiIsEnabledWhenClickedOnGettingStartedButton() throws Exception {
        setPermissionGranted(true);
        when(wiFiUtil.isHomeWiFiEnabled()).thenReturn(true);
        stubHomeWiFiStatus();
       // verify(mockEWSTagger).trackActionSendData("specialEvents", "getStartedToConnectWiFi");
        verify(navigatorMock).navigateToDevicePoweredOnConfirmationScreen();
    }

    @Test
    public void itShouldVerifyLocationPermissionInAppNotificationTag() throws Exception {
        subject.tagLocationPermission();
        verify(mockEWSTagger).trackInAppNotification("getStarted", "Location Permission");
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
        verify(mockEWSTagger).trackInAppNotification("getStarted", "Location Disabled");
    }

    @Test
    public void itShouldVerifyLocationOpenSettingsInAppNotificationResponseTag() throws Exception {
        subject.tagLocationOpenSettings();
        verify(mockEWSTagger).trackInAppNotificationResponse("openLocationSettings");
    }

    @Test
    public void itShouldRequestForLocationPermissionIfItsNotGrantedAlreadyWhenAsked() throws Exception {
        setPermissionGranted(false);
        subject.setLocationPermissionFlowCallback(mockLocationPermissionFlowCallback);
        subject.onGettingStartedButtonClicked();
        verify(mockLocationPermissionFlowCallback).showLocationPermissionDialog(baseContentConfigurationMock);
    }

    @Test
    public void itShouldShowGPSEnableDialogIfGPSIsOffWhenAsked() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(false, true);
        subject.setLocationPermissionFlowCallback(mockLocationPermissionFlowCallback);
        subject.onGettingStartedButtonClicked();
        verify(mockLocationPermissionFlowCallback).showGPSEnableDialog(baseContentConfigurationMock);
    }

    @Test
    public void itShouldNavigateToPoweredOnScreenWhenLocationPermissionIsAlreadyGrantedWhenAsked() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(true, true);
        subject.onGettingStartedButtonClicked();
        verify(navigatorMock).navigateToDevicePoweredOnConfirmationScreen();
    }

    @Test
    public void itShouldShowAWifiTroubleshootDialogIfWifiIsDisabledWhenClickedOnGettingStartedButton() throws Exception {
        setPermissionGranted(true);
        stubGPSSettings(false, false);
        when(wiFiUtil.isHomeWiFiEnabled()).thenReturn(false);
        subject.setViewCallback(dialogShowable);
        stubHomeWiFiStatus();
        verify(dialogShowable).showTroubleshootHomeWifiDialog(any(BaseContentConfiguration.class), any(EWSTagger.class));
    }

    @Test
    public void itShouldVerifyTrackPageIsCalledWithCorrectTag() throws Exception {
        subject.trackPageName();
        verify(mockEWSTagger).trackPage("getStarted");
    }

    @Test
    public void itShouldVerifyTitleForViewModel() throws Exception {
        subject.getTitle(happyFlowContentConfigurationMock, baseContentConfigurationMock);
        verify(stringProviderMock).getString(happyFlowContentConfigurationMock.getGettingStartedScreenTitle(),
                baseContentConfigurationMock.getDeviceName());
    }

    @Test
    public void itShouldVerifyNoteForViewModel() throws Exception {
        subject.getBody(baseContentConfigurationMock);
        verify(stringProviderMock).getString(R.string.label_ews_get_started_body,
                baseContentConfigurationMock.getDeviceName());
    }

    @Test
    public void itShouldVerifyDescriptionForViewModel() throws Exception {
        subject.getDescription(baseContentConfigurationMock);
        verify(stringProviderMock).getString(R.string.label_ews_get_started_description,
                baseContentConfigurationMock.getDeviceName());
    }

    @Test
    public void itShouldVerifyOnDestroyStopEWSEventingChannel() throws Exception {
        callOnDestroy();
    }

    @Test
    public void itShouldVerifyOnDestroySendTagPauseLifecycleInfo() throws Exception {
        callOnDestroy();
        verify(mockEWSTagger).pauseLifecycleInfo();
    }

    public void callOnDestroy() {
        when(navigatorMock.getFragmentNavigator()).thenReturn(mock(FragmentNavigator.class));
        when(navigatorMock.getFragmentNavigator().shouldFinish()).thenReturn(true);
        subject.onDestroy();

    }

    private void stubHomeWiFiStatus() {
        subject.onGettingStartedButtonClicked();
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
}