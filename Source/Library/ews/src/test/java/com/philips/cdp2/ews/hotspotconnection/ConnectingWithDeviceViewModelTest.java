/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.hotspotconnection;

import android.os.Handler;

import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.settingdeviceinfo.DeviceFriendlyNameFetcher;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.wifi.WiFiConnectivityManager;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConnectingWithDeviceViewModelTest {

    private ConnectingWithDeviceViewModel subject;

    @Mock private WiFiConnectivityManager mockWiFiConnectivityManager;
    @Mock private DeviceFriendlyNameFetcher mockDeviceFriendlyNameFetcher;
    @Mock private WiFiUtil mockWiFiUtil;
    @Mock private Navigator mockNavigator;
    @Mock private Handler mockHandler;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new ConnectingWithDeviceViewModel(mockWiFiConnectivityManager,mockDeviceFriendlyNameFetcher,mockWiFiUtil,mockNavigator,mockHandler);
    }

    @Test
    public void itSendActionTagOnonHelpNeededPerform() throws Exception{
        subject.onHelpNeeded();
        verifyStatic();
        EWSTagger.trackActionSendData("specialEvents", "helpMeEnablingSetupMode");
    }
}