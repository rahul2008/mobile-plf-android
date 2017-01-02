/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.communication.BleCommunicationStrategy;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.discovery.DiscoveryStrategy;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;

import org.mockito.Mock;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDiscoveryStrategyTestSteps {

    private CommCentral commCentral;

    @Mock
    SHNDeviceScanner deviceScanner;

    private BleDiscoveryStrategy bleDiscoveryStrategy;
    private BleDeviceCache bleDeviceCache;
    private Set<DiscoveryStrategy> discoveryStrategies;

    @Before
    public void setup() throws SHNBluetoothHardwareUnavailableException {
        initMocks(this);

        final Context mockContext = mock(Context.class);

        bleDeviceCache = new BleDeviceCache();
        discoveryStrategies = new HashSet<DiscoveryStrategy>() {{
            bleDiscoveryStrategy = new BleDiscoveryStrategy(mockContext, bleDeviceCache, deviceScanner, 30000L) {
                @Override
                int checkAndroidPermission(Context context, String permission) {
                    return PERMISSION_GRANTED;
                }
            };
            add(bleDiscoveryStrategy);
        }};
    }

    @Given("^a BlueLib mock$")
    public void aBlueLibMock() {
        // Not needed
    }

    @Given("^application has support for appliances?:$")
    public void applicationHasSupportFor(final List<String> applianceTypes) {
        DICommApplianceFactory testApplianceFactory = new DICommApplianceFactory() {
            @Override
            public boolean canCreateApplianceForNode(NetworkNode networkNode) {
                return applianceTypes.contains(networkNode.getModelName());
            }

            @Override
            public Appliance createApplianceForNode(final NetworkNode networkNode) {
                if (canCreateApplianceForNode(networkNode)) {
                    return new Appliance(networkNode, new BleCommunicationStrategy("aa:bb:cc:11:22:33", bleDeviceCache)) {
                        @Override
                        public String getDeviceType() {
                            return networkNode.getModelName();
                        }
                    };
                }
                return null;
            }

            @Override
            public Set<String> getSupportedModelNames() {
                return new HashSet<>(applianceTypes);
            }
        };
        commCentral = new CommCentral(discoveryStrategies, testApplianceFactory);
    }

    @When("^starting discovery for BLE appliances$")
    public void startingDiscoveryForBLEAppliances() {
        try {
            this.commCentral.startDiscovery();
        } catch (MissingPermissionException e) {
            // Ignore
        }
    }

    @Then("^startScanning is called (\\d+) time on BlueLib$")
    public void startscanningIsCalledTimeOnBlueLib(int times) {
        verify(deviceScanner, times(times)).startScanning(any(SHNDeviceScanner.SHNDeviceScannerListener.class), any(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());
    }

    @When("^starting discovery for BLE appliances (\\d+) times$")
    public void startingDiscoveryForBLEAppliancesTimes(int times) {
        while (times > 0) {
            startingDiscoveryForBLEAppliances();
            times--;
        }
    }

    @When("^stopping discovery for BLE appliances$")
    public void stoppingDiscoveryForBLEAppliances() {
        this.commCentral.stopDiscovery();
    }

    @Then("^stopScanning is called on BlueLib$")
    public void stopScanningIsCalledOnBlueLib() {
        verify(deviceScanner, atLeast(1)).stopScanning();
    }

    @Then("^stopScanning is not called on BlueLib$")
    public void stopScanningIsNotCalledOnBlueLib() {
        verify(deviceScanner, times(0)).stopScanning();
    }

    @Then("^the following appliances? (?:are|is) created:$")
    public void theFollowingAppliancesAreCreated(final List<String> appliances) {
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();

        final Set<String> availableApplianceNames = new HashSet<>();
        for (Appliance availableAppliance : availableAppliances) {
            availableApplianceNames.add(availableAppliance.getName());
        }
        assertTrue("All created appliances should be available through the ApplianceManager.", availableApplianceNames.containsAll(appliances));
    }

    @Then("^no appliances are created$")
    public void noAppliancesAreCreated() {
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();
        assertTrue("Available appliances should be an empty set.", availableAppliances.isEmpty());
    }

    @When("^(.*?) is discovered by BlueLib$")
    public void applianceIsDiscoveredByBlueLib(String applianceName) {
        SHNDevice shnDeviceMock = mock(SHNDevice.class);
        when(shnDeviceMock.getState()).thenReturn(SHNDevice.State.Connected);
        when(shnDeviceMock.getAddress()).thenReturn("00:11:22:33:44:55");
        when(shnDeviceMock.getName()).thenReturn(applianceName);
        when(shnDeviceMock.getDeviceTypeName()).thenReturn(getApplianceTypeByName(applianceName));

        SHNDeviceFoundInfo shnDeviceFoundInfoMock = mock(SHNDeviceFoundInfo.class);
        when(shnDeviceFoundInfoMock.getShnDevice()).thenReturn(shnDeviceMock);

        bleDiscoveryStrategy.deviceFound(null, shnDeviceFoundInfoMock);
        bleDiscoveryStrategy.deviceListener.onStateUpdated(shnDeviceMock);
    }

    private String getApplianceTypeByName(final @NonNull String applianceName) {
        return applianceName.substring(0, applianceName.length() - 1);
    }
}
