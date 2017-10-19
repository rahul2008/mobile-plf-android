/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.viewmodel;

import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.settingdeviceinfo.DeviceFriendlyNameFetcher;
import com.philips.cdp2.ews.settingdeviceinfo.SetDeviceInfoViewModel;
import com.philips.cdp2.ews.view.ConnectionEstablishDialogFragment;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SetDeviceInfoViewModelTest {

    @Mock
    private ApplianceSessionDetailsInfo sessionInfoMock;

    @Mock
    private WiFiUtil wifiUtilMock;

    @Mock
    private Navigator navigatorMock;

    @Mock
    private ConnectionEstablishDialogFragment dialogFragmentMock;

    @Mock
    private DeviceFriendlyNameFetcher mockDeviceFriendlyNameFetcher;
    private SetDeviceInfoViewModel viewModel;


    private static final String PRODUCT = "product 001";

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        viewModel = new SetDeviceInfoViewModel(wifiUtilMock, sessionInfoMock, navigatorMock,
                dialogFragmentMock, mockDeviceFriendlyNameFetcher);
    }

    @Test
    public void shouldCheckHomeWiFiSSIDShouldNotBeNull() throws Exception {
        when(wifiUtilMock.getHomeWiFiSSD()).thenReturn("BrightEyes");
        when(sessionInfoMock.getDeviceName()).thenReturn("Wakeup light");

        assertNotNull(viewModel.getHomeWiFiSSID());
    }

    @Test
    public void updateUpdatePasswordOnTextChanged() throws Exception {
        final String text = "abc";

        viewModel.onPasswordTextChanged(text, 0, 0, 1);

        assertEquals(text, viewModel.password.get());
    }

    @Test
    public void shouldHaveEmptyStringInPassword() {
        assertEquals(viewModel.password.get(), "");
    }

    @Test
    public void shouldSendConnectionTagsWhenWeRevisitThisPageAgain() throws Exception {
        viewModel.onConnectButtonClicked();

        verify(navigatorMock)
                .navigateToConnectingDeviceWithWifiScreen(anyString(), anyString(), anyString(),
                        anyString());
    }
}