/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.startconnectwithdevice;

import android.support.annotation.NonNull;

import com.philips.platform.ews.R;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.HappyFlowContentConfiguration;
import com.philips.platform.ews.confirmwifi.ConfirmWifiNetworkViewModel;
import com.philips.platform.ews.navigation.FragmentNavigator;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class StartConnectWithDeviceViewModelTest {

    @Mock
    private Navigator navigatorMock;

    @Mock
    private StringProvider stringProviderMock;

    @Mock
    private WiFiUtil wiFiUtil;

    @Mock
    private HappyFlowContentConfiguration happyFlowContentConfigurationMock;

    @Mock
    private BaseContentConfiguration baseContentConfigurationMock;

    private StartConnectWithDeviceViewModel subject;

    @Mock
    private EWSTagger mockEWSTagger;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new StartConnectWithDeviceViewModel(navigatorMock, stringProviderMock, wiFiUtil,
                happyFlowContentConfigurationMock, baseContentConfigurationMock, mockEWSTagger);
        when(baseContentConfigurationMock.getDeviceName()).thenReturn(123435);
    }

    @Test
    public void itShouldNavigateToHomeWifiScreenIfWifiIsEnabledWhenClickedOnGettingStartedButton() throws Exception {
        stubHomeWiFiStatus();
        verify(mockEWSTagger).trackActionSendData("specialEvents", "getStartedToConnectWiFi");
        verify(navigatorMock).navigateToHomeNetworkConfirmationScreen();
    }

    @Test
    public void itShouldShowAWifiTroubleshootDialogIfWifiIsDisabledWhenClickedOnGettingStartedButton() throws Exception {
        when(wiFiUtil.isHomeWiFiEnabled()).thenReturn(false);
        ConfirmWifiNetworkViewModel.ViewCallback dialogShowable = spy(new DialogShowable());
        subject.setViewCallback(dialogShowable);
        stubHomeWiFiStatus();
        verify(dialogShowable).showTroubleshootHomeWifiDialog(any(BaseContentConfiguration.class), any(EWSTagger.class));
    }

    private void stubHomeWiFiStatus() {
        subject.onGettingStartedButtonClicked();
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

    private class DialogShowable implements ConfirmWifiNetworkViewModel.ViewCallback {

        @Override
        public void showTroubleshootHomeWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration, @NonNull EWSTagger ewsTagger) {

        }
    }

}