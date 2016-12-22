/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.discovery;

import android.content.Context;

import com.philips.cdp.dicommclient.discovery.strategy.DiscoveryStrategy;
import com.philips.cdp2.commlib.BleDeviceCache;
import com.philips.cdp2.commlib.CommCentral;
import com.philips.cdp2.commlib.appliance.MixerFactory;
import com.philips.cdp2.commlib.context.BleTransportContext;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;

import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDiscoveryStrategyTestSteps {

    private CommCentral commCentral;
    private MixerFactory applianceFactory;

    @Before
    public void setup() throws SHNBluetoothHardwareUnavailableException {
        initMocks(this);

        final Context mockContext = Mockito.mock(Context.class);
        final SHNCentral mockShnCentral = Mockito.mock(SHNCentral.class);

        final BleDeviceCache bleDeviceCache = new BleDeviceCache();
        final SHNDeviceScanner deviceScanner = Mockito.mock(SHNDeviceScanner.class);

        BleTransportContext bleTransportContext = new BleTransportContext(mockContext, false);
        when(bleTransportContext.createBlueLib(any(Context.class), anyBoolean())).thenReturn(mockShnCentral);

        this.applianceFactory = new MixerFactory(bleTransportContext);

        Set<DiscoveryStrategy> discoveryStrategies = new HashSet<DiscoveryStrategy>() {{
            add(new BleDiscoveryStrategy(mockContext, bleDeviceCache, deviceScanner, 30000L));
        }};

        this.commCentral = new CommCentral(discoveryStrategies, applianceFactory);
    }

    @Given("^a BlueLib mock$")
    public void setupBlueLibMock() {

    }
}
