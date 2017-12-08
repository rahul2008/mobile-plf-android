/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.injections;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.devicecache.DeviceCache;
import com.philips.cdp2.commlib.core.util.ConnectivityMonitor;
import com.philips.cdp2.commlib.lan.LanDeviceCache;
import com.philips.cdp2.commlib.lan.communication.LanCommunicationStrategy;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.FragmentNavigator;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.permission.PermissionHandler;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.util.StringProvider;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
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