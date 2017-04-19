/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.ble.discovery;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.cdp2.commlib.ble.BleDeviceCache.CacheData;
import com.philips.cdp2.commlib.ble.communication.BleCommunicationStrategy;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.cdp2.commlib.core.util.HandlerProvider;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.BleScanRecord;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.philips.cdp2.commlib.ble.discovery.BleDiscoveryStrategy.MANUFACTURER_PREAMBLE;
import static com.philips.pins.shinelib.capabilities.SHNCapabilityDeviceInformation.SHNDeviceInformationType.SerialNumber;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BleDiscoveryStrategyTestSteps {
    private static final int TIMEOUT_EXTERNAL_WRITE_OCCURRED_MS = 100;
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
    private SHNCapabilityDeviceInformation deviceInformationMock;

    @Mock
    private SHNCentral shnCentral;

    @Captor
    private ArgumentCaptor<Runnable> runnableCaptor;

    @Captor
    private ArgumentCaptor<BleDiscoveryStrategy.DeviceListener> deviceListenerCaptor;

    @Before
    public void setup() throws SHNBluetoothHardwareUnavailableException {
        initMocks(this);

        DICommLog.disableLogging();

        Handler mockMainThreadHandler = mock(Handler.class);
        HandlerProvider.enableMockedHandler(mockMainThreadHandler);

        final Context mockContext = mock(Context.class);

        bleDeviceCache = new BleDeviceCache(Executors.newSingleThreadScheduledExecutor());

        bleDiscoveryStrategy = new BleDiscoveryStrategy(mockContext, bleDeviceCache, deviceScanner, shnCentral) {
            @Override
            int checkAndroidPermission(Context context, String permission) {
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
                    return new Appliance(networkNode, new BleCommunicationStrategy(networkNode.getCppId(), bleDeviceCache, callbackHandlerMock)) {
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
        commCentral = new CommCentral(testApplianceFactory, bleTransportContext);
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

    //TODO: Check with Peter F. whether there is a better method iso timeout(), to improve stability
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

    @Then("^(.*?) with cppId (.*?) is in the list of available appliances$")
    public void theFollowingAppliancesWirhCppIdAreCreated( String applianceName, String cppId) {
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();
        final Set<String> availableApplianceNames = new HashSet<>();
        final Set<String> availableCppIds = new HashSet<>();

        for (Appliance availableAppliance : availableAppliances) {
            availableApplianceNames.add(availableAppliance.getName());
            availableCppIds.add(availableAppliance.getNetworkNode().getCppId());
        }
        assertTrue("Expected appliance " + applianceName + " must appear in available appliances " + availableApplianceNames, availableApplianceNames.contains(applianceName));
        assertTrue("Expected cppId " + cppId + " must appear in available cppIds " + availableCppIds, availableCppIds.contains(cppId));
    }

    @Then("^(.*?) with cppId (.*?) and modelId (.*?) is in the filtered list of available appliances$")
    public void theFollowingAppliancesWithCppIdAndModelIdAreCreated( String applianceName, String cppId, String modelId) {
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();
        final Set<String> availableApplianceNames = new HashSet<>();
        final Set<String> availableCppIds = new HashSet<>();
        final Set<String> availableModelIds = new HashSet<>();

        for (Appliance availableAppliance : availableAppliances) {
            availableApplianceNames.add(availableAppliance.getName());
            availableCppIds.add(availableAppliance.getNetworkNode().getCppId());
            availableModelIds.add(availableAppliance.getNetworkNode().getModelId());
        }
        assertTrue("Expected appliance " + applianceName + " must appear in available appliances " + availableApplianceNames, availableApplianceNames.contains(applianceName));
        assertTrue("Expected cppId " + cppId + " must appear in available cppIds " + availableCppIds, availableCppIds.contains(cppId));
        assertTrue("Expected modelId " + modelId + " must appear in available modelIds " + availableModelIds, availableModelIds.contains(modelId));
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
        availableApplianceNames.retainAll(applianceNames);
        assertTrue("Expected appliances " + applianceNames + " must not appear in available appliances " + availableApplianceNames, availableApplianceNames.isEmpty());
    }

    @When("^(.*?) with cppId (.*?) is discovered (\\d+) times? by BlueLib$")
    public void applianceIsDiscoveredMultipleTimesByBlueLib(String applianceName, String cppId, int times) {
        createShnDeviceMock(applianceName, cppId, null, times);
    }

    @When("^(.*?) with cppId (.*?) is discovered (\\d+) times? by BlueLib, matching model id (.*?)$")
    public void applianceIsDiscoveredMultipleTimesByBlueLibWithModelId(String applianceName, String cppId, int times, String modelId) {
        byte[] modelIdArray = new byte[8];
        System.arraycopy(MANUFACTURER_PREAMBLE, 0, modelIdArray, 0, MANUFACTURER_PREAMBLE.length);
        byte[] modelIdBytes = modelId.getBytes();
        System.arraycopy(modelIdBytes, 0, modelIdArray, MANUFACTURER_PREAMBLE.length, modelIdBytes.length);

        createShnDeviceMock(applianceName, cppId, modelIdArray, times);
    }

    private SHNDevice createShnDeviceMock(String applianceName, final String cppId, byte[] modelIdArray, int times) {
        final SHNDeviceWrapper shnDeviceMock = mock(SHNDeviceWrapper.class);
        final SHNDeviceImpl shnDeviceImplMock = mock(SHNDeviceImpl.class);

        final String deviceMacAddress = createMacAddress();

        // Properties
        when(shnDeviceMock.getState()).thenReturn(SHNDevice.State.Connected);
        when(shnDeviceMock.getAddress()).thenReturn(deviceMacAddress);
        when(shnDeviceMock.getName()).thenReturn(applianceName);
        when(shnDeviceMock.getDeviceTypeName()).thenReturn(getApplianceTypeByName(applianceName));
        when(shnDeviceMock.getInternalDevice()).thenReturn(shnDeviceImplMock);

        // DIS -> CPP ID
        when(shnDeviceMock.getCapability(SHNCapabilityDeviceInformation.class)).thenReturn(deviceInformationMock);
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                SHNCapabilityDeviceInformation.Listener listener = invocation.getArgumentAt(1, SHNCapabilityDeviceInformation.Listener.class);
                listener.onDeviceInformation(SerialNumber, cppId, new Date());

                return null;
            }
        }).when(deviceInformationMock).readDeviceInformation(eq(SerialNumber), any(SHNCapabilityDeviceInformation.Listener.class));

        // Device found info (advertisement)
        SHNDeviceFoundInfo shnDeviceFoundInfoMock = mock(SHNDeviceFoundInfo.class);
        when(shnDeviceFoundInfoMock.getShnDevice()).thenReturn(shnDeviceMock);
        when(shnDeviceFoundInfoMock.getDeviceAddress()).thenReturn(deviceMacAddress);

        // Model id
        when(shnDeviceFoundInfoMock.getBleScanRecord()).thenReturn(bleScanRecordMock);
        when(bleScanRecordMock.getManufacturerSpecificData()).thenReturn(modelIdArray);

        doNothing().when(shnDeviceMock).registerSHNDeviceListener(deviceListenerCaptor.capture());

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                deviceListenerCaptor.getValue().onStateUpdated(shnDeviceMock);
                return null;
            }
        }).when(shnDeviceMock).connect();

        for (int i = 0; i < times; i++) {
            bleDiscoveryStrategy.deviceFound(null, shnDeviceFoundInfoMock);
        }

        return shnDeviceMock;
    }

    @When("^the cached data expires for the following appliances?:$")
    public void theCachedDataExpiresForTheFollowingAppliance(final List<String> appliances) {
        final Set<? extends Appliance> availableAppliances = commCentral.getApplianceManager().getAvailableAppliances();

        for (String applianceName : appliances) {
            for (Appliance appliance : availableAppliances) {
                if (applianceName.equals(appliance.getName())) {
                    final CacheData cacheData = bleDeviceCache.findByCppId(appliance.getNetworkNode().getCppId());
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

    private static String createCppId() {
        return String.valueOf(++cppId);
    }

    private static String createMacAddress() {
        return "addr-" + createCppId();
    }

}
