/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.ews.settingdeviceinfo;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.util.StringProvider;
import com.philips.platform.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConnectWithPasswordViewModelTest {

    @Mock
    private WiFiUtil wifiUtilMock;
    @Mock
    private Navigator navigatorMock;
    @Mock
    private BaseContentConfiguration mockBaseContentConfig;
    @Mock
    private StringProvider mockStringProvider;
    @Mock
    private View mockView;
    @Mock
    private InputMethodManager mockInputMethodManager;

    private ConnectWithPasswordViewModel subject;

    @Mock
    private EWSTagger mockEWSTagger;

    @Before
    public void setUp() {
        initMocks(this);

        mockStatic(EWSTagger.class);

        subject = new ConnectWithPasswordViewModel(wifiUtilMock, navigatorMock, mockBaseContentConfig, mockStringProvider, mockEWSTagger);
        when(mockBaseContentConfig.getDeviceName()).thenReturn(123435);
    }

    @Test
    public void itShouldCheckHomeWiFiSSIDShouldNotBeNull() {
        when(wifiUtilMock.getHomeWiFiSSD()).thenReturn("BrightEyes");

        assertNotNull(subject.getHomeWiFiSSID());
    }

    @Test
    public void updateUpdatePasswordOnTextChanged() {
        final String text = "abc";

        subject.onPasswordTextChanged(text, 0, 0, 1);

        assertEquals(text, subject.password.get());
    }

    @Test
    public void updateUpdateFriendlyDeviceNameOnTextChanged() {
        final String text = "abc";

        subject.onDeviceNameTextChanged(text, 0, 0, 1);

        assertEquals(text, subject.deviceFriendlyName.get());
    }

    @Test
    public void itShouldHaveEmptyStringInPassword() {
        assertEquals(subject.password.get(), "");
    }

    @Test
    public void itShouldSendConnectionTagsWhenWeRevisitThisPageAgain() {
        subject.onConnectButtonClicked();

        verify(navigatorMock).navigateToConnectingDeviceWithWifiScreen((String) any(), (String) any(), (String) any(), (String) any());
    }

    @Test
    public void itShouldSetTheDeviceFriendlyName() {
        final String text = "abc";
        subject.setDeviceFriendlyName(text);

        assertEquals(text, subject.deviceFriendlyName.get());
    }

    @Test
    public void itShouldVerifyTitleForViewModel() {
        subject.getTitle(mockBaseContentConfig);

        verify(mockStringProvider).getString(R.string.label_ews_password_title, mockBaseContentConfig.getDeviceName(), subject.getHomeWiFiSSID());
    }

    @Test
    public void itShouldVerifyTitleForViewMatches() {
        when(mockStringProvider.getString(R.string.label_ews_password_title, mockBaseContentConfig.getDeviceName(), subject.getHomeWiFiSSID())).thenReturn("device name");

        assertEquals("device name", subject.getTitle(mockBaseContentConfig));
    }

    @Test
    public void itShouldVerifyNoteForScreen() {
        subject.getNote(mockBaseContentConfig);

        verify(mockStringProvider).getString(R.string.label_ews_password_from_name_title, mockBaseContentConfig.getDeviceName());
    }

    @Test
    public void itShouldVerifyNoteForViewMatches() {
        when(mockStringProvider.getString(R.string.label_ews_password_from_name_title, mockBaseContentConfig.getDeviceName())).thenReturn("device name");

        assertEquals("device name", subject.getNote(mockBaseContentConfig));
    }

    @Test
    public void itShouldVerifyTrackPageName() {
        subject.trackPageName();

        verify(mockEWSTagger).trackPage("connectWithPassword");
    }

    @Test
    public void itShouldVerifyOnPasswordFocusChange() {
        subject.onPasswordFocusChange(mockView, mockInputMethodManager, false);

        verify(mockInputMethodManager).hideSoftInputFromWindow(mockView.getWindowToken(), 0);
    }

    @Test
    public void itShouldVerifyOnDeviceNameFocusChange() {
        subject.onDeviceNameFocusChange(mockView, mockInputMethodManager, false);

        verify(mockInputMethodManager).hideSoftInputFromWindow(mockView.getWindowToken(), 0);
    }
}
