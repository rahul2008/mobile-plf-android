/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.injections;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.lan.LanDeviceCache;
import com.philips.cdp2.commlib.lan.communication.LanCommunicationStrategy;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.configuration.HappyFlowContentConfiguration;
import com.philips.platform.ews.logger.EWSLogger;
import com.philips.platform.ews.navigation.Navigator;
import com.philips.platform.ews.permission.PermissionHandler;
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
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CommCentral.class, ConnectivityMonitor.class,EWSModule.class})
public class EWSModuleTest {

    private EWSModule subject;

    @Mock
    Context mockContext;

    @Mock
    FragmentManager mockFragmentManager;

    @Mock
    CommCentral mockCommCentral;

    @Mock
    WifiManager mockWifiManager;

    @Mock
    Navigator mockNavigator;

    @Mock
    PermissionHandler mockPermissionHandler;

    @Mock
    HappyFlowContentConfiguration mockHappyFlowContentConfiguration;

    @Mock
    StringProvider mockStringProvider;

    @Mock
    BaseContentConfiguration mockBaseContentConfiguration;

    @Mock
    EWSTagger mockEWSTagger;

    @Mock
    EWSLogger mockEWSLogger;

    @Mock
    WiFiUtil mockWiFiUtil;
    

    @Mock
    ConnectivityMonitor mockConnectivityMonitor;

    @Mock
    LanCommunicationStrategy mockCommunicationStrategy;

    @Mock
    NetworkNode mockNetworkNode;

    @Mock
    LanDeviceCache mockDeviceCache;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(ConnectivityMonitor.class);
        subject = new EWSModule(mockContext, mockFragmentManager, 1, mockCommCentral);
    }

    @Test
    public void providesWiFiManager() throws Exception {
        when(mockContext.getApplicationContext()).thenReturn(mockContext);
        when(mockContext.getSystemService(Context.WIFI_SERVICE)).thenReturn(mockWifiManager);
        assertEquals(mockWifiManager, subject.providesWiFiManager());
    }

    @Test
    public void provideCommCentral() throws Exception {
        assertEquals(mockCommCentral, subject.provideCommCentral());
    }

    @Test
    public void provideTemporaryAppliance() throws Exception {
        when(ConnectivityMonitor.forNetworkTypes(mockContext, ConnectivityManager.TYPE_WIFI)).thenReturn(mockConnectivityMonitor);
        whenNew(LanCommunicationStrategy.class).withArguments(mockNetworkNode,mockDeviceCache,mockConnectivityMonitor).thenReturn(mockCommunicationStrategy);
        subject.provideTemporaryAppliance();

    }

    @Test
    public void providesDiscoverHelper() throws Exception {
        assertNotNull(subject.providesDiscoverHelper());
    }

    @Test
    public void providesSetDeviceConnectViewModel() throws Exception {
        assertNotNull(subject.providesSetDeviceConnectViewModel(mockWiFiUtil, mockNavigator, mockBaseContentConfiguration, mockStringProvider, mockEWSTagger));
    }

    @Test
    public void provideSecondSetupStepsViewModel() throws Exception {
        assertNotNull(subject.provideSecondSetupStepsViewModel(mockNavigator, mockPermissionHandler, mockHappyFlowContentConfiguration, mockStringProvider, mockBaseContentConfiguration, mockEWSTagger, mockEWSLogger));
    }

    @Test
    public void provideNavigator() throws Exception {
        assertNotNull(subject.provideNavigator());
    }

    @Test
    public void provideHandlerWithMainLooper() throws Exception {
        assertNotNull(subject.provideHandlerWithMainLooper());
    }

}