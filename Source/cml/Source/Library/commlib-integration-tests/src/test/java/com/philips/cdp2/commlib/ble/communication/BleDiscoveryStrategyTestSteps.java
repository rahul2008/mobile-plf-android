/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.communication;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.ble.BleCacheData;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.HandlerProvider;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.BleScanRecord;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.reflect.Whitebox;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy.MANUFACTURER_PREAMBLE;
import static com.philips.cdp2.commlib.core.util.ContextProvider.setTestingContext;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDiscoveryStrategyTestSteps {
    private static final long TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS = TimeUnit.SECONDS.toMillis(10);
    private CommCentral commCentral;

    @Mock
    SHNDeviceScanner deviceScanner;

    @Mock
    BleTransportContext bleTransportContext;

    private BleDiscoveryStrategy bleDiscoveryStrategy;
    private BleDeviceCache bleDeviceCache;
    private static int cppId = 0;

    @Mock
    private Handler callbackHandlerMock;

    @Mock
    private BleScanRecord bleScanRecordMock;

    @Mock
    private Context contextMock;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Before
    public void setup() throws SHNBluetoothHardwareUnavailableException {
        initMocks(this);

        DICommLog.disableLogging();

        Handler mockMainThreadHandler = mock(Handler.class);
        HandlerProvider.enableMockedHandler(mockMainThreadHandler);

        final Context mockContext = mock(Context.class);

        bleDeviceCache = new BleDeviceCache(Executors.newSingleThreadScheduledExecutor());

        bleDiscoveryStrategy = new BleDiscoveryStrategy(mockContext, bleDeviceCache, deviceScanner) {
            @Override
            public int checkAndroidPermission(Context context, String permission) {
                return PERMISSION_GRANTED;
            }
        };
        when(bleTransportContext.getDiscoveryStrategy()).thenReturn(bleDiscoveryStrategy);

        when(callbackHandlerMock.post(runnableCaptor.capture())).thenAnswer(new Answer<Void>() {
            @Override
            public Void answer(final InvocationOnMock invocation) throws Throwable {
                runnableCaptor.getValue().run();
                return null;
            }
        });
    }

    @After
    public void tearDown() {
        Whitebox.setInternalState(CommCentral.class, "instanceWeakReference", new WeakReference<CommCentral>(null));
    }

    @Given("^a BlueLib mock$")
    public void aBlueLibMock() {
        // Not needed
    }

    @Given("^application has support for appliances?:$")
    public void applicationHasSupportFor(final List<String> applianceTypes) {
        ApplianceFactory testApplianceFactory = new ApplianceFactory() {
            @Override
            public boolean canCreateApplianceForNode(NetworkNode networkNode) {
                return applianceTypes.contains(networkNode.getDeviceType());
            }

            @Override
            public Appliance createApplianceForNode(final NetworkNode networkNode) {
                if (canCreateApplianceForNode(networkNode)) {
                    return new Appliance(networkNode, new BleCommunicationStrategy(networkNode.getCppId(), bleDeviceCache, callbackHandlerMock)) {
                        @Override
                        public String getDeviceType() {
                            return networkNode.getDeviceType();
                        }
                    };
                }
                return null;
            }

            @Override
            public Set<String> getSupportedDeviceTypes() {
                return new HashSet<>(applianceTypes);
            }
        };
        setTestingContext(contextMock);

        commCentral = new CommCentral(testApplianceFactory, bleTransportContext);
    }

    private static String createCppId() {
        return String.valueOf(++cppId);
    }

    @When("^starting discovery for BLE appliances$")
    public void startingDiscoveryForBLEAppliances() {
        try {
            this.commCentral.startDiscovery();
        } catch (MissingPermissionException | TransportUnavailableException ignored) {
            // These are normally thrown from BlueLib, which is mocked here.
        }
    }

    @When("^starting discovery for BLE appliances with model ids?:$")
    public void startingDiscoveryForBLEAppliancesWithModelIds(final List<String> modelIds) {
        try {
            this.commCentral.startDiscovery(new HashSet<String>() {{
                addAll(modelIds);
            }});
        } catch (MissingPermissionException | TransportUnavailableException ignored) {
            // These are normally thrown from BlueLib, which is mocked here.
        }
    }

    @Then("^startScanning is called (\\d+) time on BlueLib$")
    public void startscanningIsCalledTimeOnBlueLib(int times) {
        verify(deviceScanner, timeout(TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS).times(times)).startScanning(any(SHNDeviceScanner.SHNDeviceScannerListener.class), any(SHNDeviceScanner.ScannerSettingDuplicates.class), anyLong());
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

    @Then("^the following appliances? (?:are|is) in the list of available appliances:$")
    public void theFollowingAppliancesAreCreated(final List<String> appliances) {
        final Set<String> applianceNames = new HashSet<>(appliances);
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();

        final Set<String> availableApplianceNames = new HashSet<>();
        for (Appliance availableAppliance : availableAppliances) {
            availableApplianceNames.add(availableAppliance.getName());
        }
        assertTrue("Expected appliances " + applianceNames + " must appear in available appliances " + availableApplianceNames, availableApplianceNames.containsAll(applianceNames));
    }

    @Then("^the following appliances? (?:are|is) not in the list of available appliances:$")
    public void theFollowingAppliancesAreNotCreated(final List<String> appliances) {
        final Set<String> applianceNames = new HashSet<>(appliances);
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();

        final Set<String> availableApplianceNames = new HashSet<>();
        for (Appliance availableAppliance : availableAppliances) {
            availableApplianceNames.add(availableAppliance.getName());
        }

        // Take the intersection of both collections, should be empty
        applianceNames.retainAll(availableApplianceNames);
        assertTrue("Expected appliances " + appliances + " must not appear in available appliances " + availableApplianceNames, applianceNames.isEmpty());
    }

    @When("^(.*?) is discovered (\\d+) times? by BlueLib$")
    public void applianceIsDiscoveredMultipleTimesByBlueLib(String applianceName, int times) {
        SHNDevice shnDeviceMock = mock(SHNDevice.class);
        when(shnDeviceMock.getState()).thenReturn(SHNDevice.State.Connected);
        when(shnDeviceMock.getAddress()).thenReturn(createCppId());
        when(shnDeviceMock.getName()).thenReturn(applianceName);
        when(shnDeviceMock.getDeviceTypeName()).thenReturn(getApplianceTypeByName(applianceName));

        SHNDeviceFoundInfo shnDeviceFoundInfoMock = mock(SHNDeviceFoundInfo.class);
        when(shnDeviceFoundInfoMock.getShnDevice()).thenReturn(shnDeviceMock);
        when(shnDeviceFoundInfoMock.getBleScanRecord()).thenReturn(bleScanRecordMock);
        when(bleScanRecordMock.getManufacturerSpecificData(MANUFACTURER_PREAMBLE)).thenReturn(new byte[]{(byte) 80, 70, 49, 51, 51, 55}); // Model id 'PF1337'

        for (int i = 0; i < times; i++) {
            bleDiscoveryStrategy.deviceFound(null, shnDeviceFoundInfoMock);
        }
    }

    @When("^(.*?) is discovered (\\d+) times? by BlueLib, matching model id (.*?)$")
    public void applianceIsDiscoveredMultipleTimesByBlueLibWithModelId(String applianceName, int times, String modelId) {
        SHNDevice shnDeviceMock = mock(SHNDevice.class);
        when(shnDeviceMock.getState()).thenReturn(SHNDevice.State.Connected);
        when(shnDeviceMock.getAddress()).thenReturn(createCppId());
        when(shnDeviceMock.getName()).thenReturn(applianceName);
        when(shnDeviceMock.getDeviceTypeName()).thenReturn(getApplianceTypeByName(applianceName));

        SHNDeviceFoundInfo shnDeviceFoundInfoMock = mock(SHNDeviceFoundInfo.class);
        when(shnDeviceFoundInfoMock.getShnDevice()).thenReturn(shnDeviceMock);
        when(shnDeviceFoundInfoMock.getBleScanRecord()).thenReturn(bleScanRecordMock);

        when(bleScanRecordMock.getManufacturerSpecificData(MANUFACTURER_PREAMBLE)).thenReturn(modelId.getBytes());

        for (int i = 0; i < times; i++) {
            bleDiscoveryStrategy.deviceFound(null, shnDeviceFoundInfoMock);
        }
    }

    @When("^the cached data expires for the following appliances?:$")
    public void theCachedDataExpiresForTheFollowingAppliance(final List<String> appliances) {
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();

        for (String applianceName : appliances) {
            for (Appliance appliance : availableAppliances) {
                if (applianceName.equals(appliance.getName())) {
                    final BleCacheData cacheData = bleDeviceCache.getCacheData(appliance.getNetworkNode().getCppId());
                    if (cacheData == null) {
                        continue;
                    }

                    when(cacheData.getDevice().getState()).thenReturn(SHNDevice.State.Disconnected);
                    cacheData.getExpirationCallback().onCacheExpired(appliance.getNetworkNode());
                }
            }
        }
    }

    @Then("^the length of the list of available appliances is (\\d+)$")
    public void theNumberOfCreatedAppliancesIs(int numberOfAppliances) {
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();
        assertEquals("Number of created appliances doesn't match expected number.", numberOfAppliances, availableAppliances.size());
    }

    private String getApplianceTypeByName(final @NonNull String applianceName) {
        return applianceName.substring(0, applianceName.length() - 1);
    }

    @When("^the list of discovered appliances is cleared$")
    public void theListOfDiscoveredAppliancesIsCleared() {
        commCentral.clearDiscoveredAppliances();
    }
}
