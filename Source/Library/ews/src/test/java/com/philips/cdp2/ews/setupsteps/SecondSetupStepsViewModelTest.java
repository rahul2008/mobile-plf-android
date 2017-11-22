/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.setupsteps;

import android.app.Dialog;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.communication.events.DeviceConnectionErrorEvent;
import com.philips.cdp2.ews.communication.events.ShowPasswordEntryScreenEvent;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.connectionestabilish.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.GpsUtil;
import com.philips.cdp2.ews.util.StringProvider;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.cdp2.ews.setupsteps.SecondSetupStepsViewModel.ACCESS_COARSE_LOCATION;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({GpsUtil.class, EWSLogger.class, EWSTagger.class})
public class SecondSetupStepsViewModelTest {

    @Mock
    private Navigator navigatorMock;

    @Mock
    private EventBus eventBusMock;

    @Mock
    private PermissionHandler permissionHandlerMock;

    @Mock
    private Fragment fragmentMock;

    @Mock
    private ConnectionEstablishDialogFragment connectingDialogMock;

    @Mock
    private Handler handlerMock;

    private SecondSetupStepsViewModel subject;

    @Mock
    private DialogFragment unsuccessfulDialogMock;

    @Mock private StringProvider mockStringProvider;

    @Mock private HappyFlowContentConfiguration mockHappyFlowConfiguration;

    @Mock private BaseContentConfiguration baseContentConfiguration;

    @Mock
    private Dialog dialogMock;

    @Mock
    private SecondSetupStepsViewModel.LocationPermissionFlowCallback mockLocationPermissionFlowCallback;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSLogger.class);
        mockStatic(EWSTagger.class);
        setupImmediateHandler();
        subject = new SecondSetupStepsViewModel(navigatorMock, eventBusMock, permissionHandlerMock,
                connectingDialogMock, unsuccessfulDialogMock, handlerMock,
                mockStringProvider, mockHappyFlowConfiguration,baseContentConfiguration);

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
    public void itShouldCancelConnectingDialogOnDeviceConnectionError() throws Exception {
        subject.deviceConnectionError(new DeviceConnectionErrorEvent());

        verify(connectingDialogMock).dismissAllowingStateLoss();
    }

    @Test
    public void itShouldRemoveHandlerCallbackOnDeviceConnectionError() throws Exception {
        subject.deviceConnectionError(new DeviceConnectionErrorEvent());

        verify(handlerMock).removeCallbacks(any(Runnable.class));
    }

    @Test
    public void itShouldShowChooseCurrentStateScreenWhenNoButtonIsClicked() throws Exception {
        subject.onNoButtonClicked();
        verify(navigatorMock).navigateToResetConnectionTroubleShootingScreen();
    }

    @Test
    public void itShouldSendWifiBlinkingActionTagOnNextButtonClick() throws Exception {
        subject.onNextButtonClicked();
        verifyStatic();
        EWSTagger.trackActionSendData("specialEvents", "wifiBlinking");
    }

    @Test
    public void itShouldSendWifiNotBlinkingActionTagOnNoButtonClick() throws Exception {
        subject.onNoButtonClicked();
        verifyStatic();
        EWSTagger.trackActionSendData("specialEvents", "wifiNotBlinking");
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
        verify(eventBusMock).unregister(Matchers.anyObject());
        verify(navigatorMock).navigateToConnectingPhoneToHotspotWifiScreen();
    }

    @Test
    public void itShouldCheckAnalyticsPageName() throws Exception {
        subject.trackPageName();
        verifyStatic();
        EWSTagger.trackPage("setupStep2");
    }

    @Test
    public void itShouldVerifyGetQuestionText() throws Exception{
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

    @Test
    public void itShouldUnregisterFromEventBusWhenPhoneIsConnectedToApplianceHotspot() throws Exception {
        sendEventToShowPasswordEntryScreen();

        verify(eventBusMock).unregister(subject);
    }

    private void sendEventToShowPasswordEntryScreen() {
        subject.showPasswordEntryScreenEvent(new ShowPasswordEntryScreenEvent());
    }


    @Test
    public void itShouldShowUnsuccessfulDialogOnDeviceConnectionError() throws Exception {
        subject.showUnsuccessfulDialog();
        when(unsuccessfulDialogMock.getDialog()).thenReturn(dialogMock);
        verify(unsuccessfulDialogMock).show(any(FragmentManager.class), any(String.class));
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
       verify(eventBusMock).unregister(Matchers.anyObject());
        verify(navigatorMock).navigateToConnectingPhoneToHotspotWifiScreen();
    }


    private void setupImmediateHandler() {
        // Run any post delayed task immediately.
        when(handlerMock.postDelayed(any(Runnable.class), anyLong())).thenAnswer(
                new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                        runnable.run();
                        return null;
                    }
                }
        );
    }
}