/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.annotations.ConnectionErrorType;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.communication.events.ApplianceConnectErrorEvent;
import com.philips.cdp2.ews.communication.events.PairingSuccessEvent;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.view.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import junit.framework.Assert;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowInputMethodManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = Config.NONE)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest({EWSTagger.class, EWSDependencyProvider.class, EWSCallbackNotifier.class})
public class EWSWiFiConnectViewModelTest {

    @Mock
    private ApplianceSessionDetailsInfo sessionInfoMock;

    @Mock
    private WiFiUtil wifiUtilMock;

    @Mock
    private Fragment fragmentMock;

    @Mock
    private Navigator navigatorMock;
    @Mock
    private EventBus eventBusMock;

    @Mock
    private ConnectionEstablishDialogFragment dialogFragmentMock;

    private EWSWiFiConnectViewModel viewModel;

    @Mock
    private Handler handlerMock;

    @Mock
    private EWSCallbackNotifier ewsCallbackNotifier;

    @Mock
    private BaseContentConfiguration mockBaseContentConfig;

    @Mock
    private StringProvider mockStringProvider;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private static final String PRODUCT = "product 001";

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        PowerMockito.mockStatic(EWSTagger.class);

        PowerMockito.mockStatic(EWSDependencyProvider.class);
        EWSDependencyProvider ewsDependencyProviderMock = mock(EWSDependencyProvider.class);
        when(EWSDependencyProvider.getInstance()).thenReturn(ewsDependencyProviderMock);
        when(ewsDependencyProviderMock.getProductName()).thenReturn(PRODUCT);

        PowerMockito.mockStatic(EWSCallbackNotifier.class);
        when(EWSCallbackNotifier.getInstance()).thenReturn(ewsCallbackNotifier);

        viewModel = new EWSWiFiConnectViewModel(wifiUtilMock, sessionInfoMock, navigatorMock,
                dialogFragmentMock, mockBaseContentConfig, mockStringProvider);
        when(mockBaseContentConfig.getDeviceName()).thenReturn(123435);
        viewModel.setFragment(fragmentMock);
    }

    @Test
    public void shouldCheckHomeWiFiSSIDShouldNotBeNull() throws Exception {
        when(wifiUtilMock.getHomeWiFiSSD()).thenReturn("BrightEyes");
        when(sessionInfoMock.getDeviceName()).thenReturn("Wakeup light");

        assertNotNull(viewModel.getHomeWiFiSSID());
        assertNotNull(viewModel.getDeviceName());
    }

    @Test
    public void shouldHideKeyboardWhenPasswordEntryFieldLostFocus() throws Exception {
        final View view = new View(RuntimeEnvironment.application);
        final Activity activity = Robolectric.buildActivity(Activity.class).create().get();
        final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        ShadowInputMethodManager shadowInputMethodManager = Shadows.shadowOf(inputMethodManager);

        viewModel.onFocusChange(view, inputMethodManager, false);

        assertFalse(shadowInputMethodManager.isSoftInputVisible());
    }

    @Test
    public void updateUpdatePasswordOnTextChanged() throws Exception {
        final String text = "abc";

        viewModel.onTextChanged(text, 0, 0, 1);

        assertEquals(text, viewModel.password.get());
    }

    @Test
    public void shouldHaveEmptyStringInPassword() {
        assertEquals(viewModel.password.get(), "");
    }

    @Test
    public void shouldPersistCppIdOnApplianceDiscoverdAndPairingSuccessful() throws Exception {
        viewModel.showPairingSuccessEvent(new PairingSuccessEvent());

        verify(ewsCallbackNotifier).onApplianceDiscovered(sessionInfoMock.getCppId());
    }

    @Test
    public void shouldDismissConnectDialogBoxOnceApplianceIsDiscoveredAfterConnectedToHomeWifi() throws Exception {
        viewModel.showPairingSuccessEvent(new PairingSuccessEvent());

        verify(dialogFragmentMock).dismissAllowingStateLoss();
    }

//    @Test
//    public void shouldShowDialogBoxDismissWhenConnectRequestIsSent() throws Exception {
//        viewModel.connectApplianceToHomeWiFi();
//
//        verify(dialogFragmentMock).show(fragmentMock.getFragmentManager(), fragmentMock.getClass().getName());
//    }
//
//    @Test
//    public void shouldPostTimeoutRunnableWithSpecificDelayWhenConnectEventIsSend() throws Exception {
//        ArgumentCaptor<Long> integerArgumentCaptor = ArgumentCaptor.forClass(Long.class);
//        viewModel.connectApplianceToHomeWiFi();
//
//        verify(handlerMock).postDelayed(any(Runnable.class), integerArgumentCaptor.capture());
//
//        assertEquals(APPLIANCE_PAIR_TIME_OUT, integerArgumentCaptor.getValue().intValue());
//    }
//
//    @Test
//    public void shouldShowIncorrectPasswordScreenWhenTimeoutHappened() throws Exception {
//        ArgumentCaptor<Runnable> argumentCaptor = ArgumentCaptor.forClass(Runnable.class);
//        viewModel.connectApplianceToHomeWiFi();
//
//        verify(handlerMock).postDelayed(argumentCaptor.capture(), anyLong());
//        argumentCaptor.getValue().run();
//
//        verifyConnectionUnsuccessfulScreenIsShown();
//    }
//
//    @Test
//    public void shouldShowIncorrectPasswordScreenWhenConnectionErrorEventIsReceived() throws Exception {
//        viewModel.onConnectionErrorOccured(new ApplianceConnectErrorEvent(ConnectionErrorType.WRONG_CREDENTIALS));
//
//        verifyConnectionUnsuccessfulScreenIsShown();
//    }
//
//    private void verifyConnectionUnsuccessfulScreenIsShown() {
//        verify(navigatorMock).navigateToConnectionUnsuccessfulTroubleShootingScreen(null);
//        verify(dialogFragmentMock).dismissAllowingStateLoss();
//        verify(handlerMock).removeCallbacks(any(Runnable.class));
//    }

    @Test
    public void shouldDismissDialogWhenWrongWifiEventReceived() throws Exception {
        viewModel.onConnectionErrorOccured(new ApplianceConnectErrorEvent(ConnectionErrorType.WRONG_HOME_WIFI));

        verify(dialogFragmentMock).dismissAllowingStateLoss();
    }

    @Test
    public void shouldConnectAutomaticallyIfWeAreInHomeWifiState() throws Exception {
        when(wifiUtilMock.getCurrentWifiState()).thenReturn(WiFiUtil.HOME_WIFI);

        viewModel.onStart();

        verify(dialogFragmentMock).show(any(FragmentManager.class), any(String.class));
//        verify(handlerMock).postDelayed(viewModel.timeoutRunnable, APPLIANCE_PAIR_TIME_OUT);
    }

    @Test
    public void shouldNotConnectIfWeAreInHotspotWifi() throws Exception {
        when(wifiUtilMock.getCurrentWifiState()).thenReturn(WiFiUtil.DEVICE_HOTSPOT_WIFI);

        viewModel.onStart();

        verify(dialogFragmentMock, Mockito.never()).show(any(FragmentManager.class), any(String.class));
//        verify(handlerMock, Mockito.never()).postDelayed(viewModel.timeoutRunnable, APPLIANCE_PAIR_TIME_OUT);
    }

    @Test
    public void shouldNotConnectIfWeAreInUnknownWifi() throws Exception {
        when(wifiUtilMock.getCurrentWifiState()).thenReturn(WiFiUtil.UNKNOWN_WIFI);

        viewModel.onStart();

        verify(dialogFragmentMock, Mockito.never()).show(any(FragmentManager.class), any(String.class));
//        verify(handlerMock, Mockito.never()).postDelayed(viewModel.timeoutRunnable, APPLIANCE_PAIR_TIME_OUT);
    }

    @Test
    public void shouldSendConnectionTagsWhenWeRevisitThisPageAgain() throws Exception {
        viewModel.connectApplianceToHomeWiFi();

        verify(navigatorMock).navigateToConnectingDeviceWithWifiScreen(anyString(), anyString(), anyString());
    }

    @Test
    public void shouldNotShowConnectionDialogIfAlreadyAddedAndNotNull() throws Exception {
        when(dialogFragmentMock.isAdded()).thenReturn(true);
        viewModel.onStart();

        verify(dialogFragmentMock, Mockito.never()).show(any(FragmentManager.class), any(String.class));
    }

    @Test
    public void shouldShowConnectionDialogIfNotAdded() throws Exception {
        when(dialogFragmentMock.isAdded()).thenReturn(false);
        when(wifiUtilMock.getCurrentWifiState()).thenReturn(WiFiUtil.HOME_WIFI);
        viewModel.onStart();

        verify(dialogFragmentMock).show(any(FragmentManager.class), any(String.class));
    }

    @Test
    public void itShouldVerifyTitleForViewModel() throws Exception {
        viewModel.getTitle(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_password_title, mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID());
    }

    @Test
    public void itShouldVerifyTitleForViewMatches() throws Exception{
        when(mockStringProvider.getString(R.string.label_ews_password_title, mockBaseContentConfig.getDeviceName(), viewModel.getHomeWiFiSSID())).thenReturn("device name");
        Assert.assertEquals("device name", viewModel.getTitle(mockBaseContentConfig));
    }

    @Test
    public void itShouldVerifyNoteForScreen() throws Exception {
        viewModel.getNote(mockBaseContentConfig);
        verify(mockStringProvider).getString(R.string.label_ews_password_from_name_title, mockBaseContentConfig.getDeviceName());
    }

    @Test
    public void itShouldVerifyNoteForViewMatches() throws Exception{
        when(mockStringProvider.getString(R.string.label_ews_password_from_name_title, mockBaseContentConfig.getDeviceName())).thenReturn("device name");
        Assert.assertEquals("device name", viewModel.getNote(mockBaseContentConfig));
    }

}