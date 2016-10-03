/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import android.os.Handler.Callback;

import com.philips.cdp.dicommclient.MockitoTestCase;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.communication.NullStrategy;
import com.philips.cdp.dicommclient.cpp.DefaultCppController;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SsdpServiceHelperDiscoveryTest extends MockitoTestCase {

    private static final int SHORT_TIMEOUT = 200;
    private static final int STOPSSDP_TESTDELAY = 300;
    private static final int STOPMESSAGE_TIMEOUT = STOPSSDP_TESTDELAY + 100;
    private static final int SLEEP_STEP = 20;

    private SsdpServiceHelper mHelper;
    private SsdpService mService;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        DiscoveryManager.createSharedInstance(getInstrumentation().getContext(), mock(DefaultCppController.class), new TestApplianceFactory());

        mService = mock(SsdpService.class);
        mHelper = new SsdpServiceHelper(mService, null);
        mHelper.setStopDelayForTesting(STOPSSDP_TESTDELAY);
    }

    @Override
    protected void tearDown() throws Exception {
        // Clean up resources
        DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
        super.tearDown();
    }

    private void waitForMessagesToBeProcessed(int timeout) {
        for (int i = 0; i < timeout; i += SLEEP_STEP) {
            if (mHelper.noMorePendingMessagesForTesting())
                break;
            try {
                Thread.sleep(SLEEP_STEP);
            } catch (Exception e) {
            }
        }
    }

    private DeviceModel generateSsdpDeviceModel(String udn, String cppId) {
        DeviceModel model = new DeviceModel(udn);
        SSDPdevice device = new SSDPdevice();
        device.setCppId(cppId);
        model.setSsdpDevice(device);
        return model;
    }

    // ***** START TESTS TO START STOP DISCOVERY WHEN METHODS ARE CALLED *****
    public void testDiscoveryConstructor() {
        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

//	public void testDiscoveryOnStart() {
//		mHelper.startDiscoveryAsync();
//		waitForMessagesToBeProcessed(SHORT_TIMEOUT);
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		verify(mService).startDeviceDiscovery(any(Callback.class));
//		verify(mService, never()).stopDeviceDiscovery();
//	}

    public void testDiscoveryOnStartStart() {
        mHelper.startDiscoveryAsync();
        mHelper.stopDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartStop() {
        mHelper.startDiscoveryAsync();
        mHelper.stopDiscoveryAsync();

        verify(mService, atMost(1)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

//	public void testDiscoveryOnStartProcessStop() {
//		mHelper.startDiscoveryAsync();
//		waitForMessagesToBeProcessed(SHORT_TIMEOUT);
//		mHelper.stopDiscoveryAsync();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		verify(mService).startDeviceDiscovery(any(Callback.class));
//		verify(mService, never()).stopDeviceDiscovery();
//	}

    public void testDiscoveryOnStartStopWait() {
        mHelper.startDiscoveryAsync();
        mHelper.stopDiscoveryAsync();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(1)).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartProcessStopWait() {
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stopDiscoveryAsync();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartStopStart() {
        mHelper.startDiscoveryAsync();
        mHelper.stopDiscoveryAsync();
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartProcessStopStart() {
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stopDiscoveryAsync();
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        // Must be called twice to set new DiscoveryEventLindiistener.
        verify(mService, times(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartStopWaitStart() {
        mHelper.startDiscoveryAsync();
        mHelper.stopDiscoveryAsync();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }

        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartProcessStopWaitStart() {
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stopDiscoveryAsync();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }

        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, times(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStop() {
        mHelper.stopDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopWait() {
        mHelper.stopDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStop() {
        mHelper.stopDiscoveryAsync();
        mHelper.stopDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStopWaitWait() {
        mHelper.stopDiscoveryAsync();
        mHelper.stopDiscoveryAsync();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStart() {
        mHelper.stopDiscoveryAsync();
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopWaitStart() {
        mHelper.stopDiscoveryAsync();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStartStop() {
        mHelper.stopDiscoveryAsync();
        mHelper.startDiscoveryAsync();
        mHelper.stopDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(1)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStartProcessStop() {
        mHelper.stopDiscoveryAsync();
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stopDiscoveryAsync();
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStartProcessStopWaitWait() {
        mHelper.stopDiscoveryAsync();
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stopDiscoveryAsync();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopWaitStartProcessStop() {
        mHelper.stopDiscoveryAsync();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stopDiscoveryAsync();
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopWaitStartProcessStopWait() {
        mHelper.stopDiscoveryAsync();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stopDiscoveryAsync();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService, times(2)).stopDeviceDiscovery();
    }

    @SuppressWarnings("unchecked")
    public void testDiscoverySyncLocalOnStart() {
        DiscoveryManager discMan = mock(DiscoveryManager.class);
        DiscoveryManager.setDummyDiscoveryManagerForTesting(discMan);

        mHelper.startDiscoveryAsync();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(discMan).syncLocalAppliancesWithSsdpStackDelayed();
        verify(discMan, never()).cancelSyncLocalAppliancesWithSsdpStack();

        DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
        DiscoveryManager.createSharedInstance(getInstrumentation().getTargetContext(), mock(DefaultCppController.class), mock(DICommApplianceFactory.class));
    }

    @SuppressWarnings("unchecked")
    public void testDiscoverySyncLocalOnStop() {
        DiscoveryManager discMan = mock(DiscoveryManager.class);
        DiscoveryManager.setDummyDiscoveryManagerForTesting(discMan);

        mHelper.stopDiscoveryAsync();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception e) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(discMan, never()).syncLocalAppliancesWithSsdpStackDelayed();
        verify(discMan).cancelSyncLocalAppliancesWithSsdpStack();

        DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
        DiscoveryManager.createSharedInstance(getInstrumentation().getTargetContext(), mock(DefaultCppController.class), mock(DICommApplianceFactory.class));
    }

    // ***** STOP TESTS TO START STOP DISCOVERY WHEN METHODS ARE CALLED *****

    public void testOnlineDevicesNull() {
        when(mService.getAliveDeviceList()).thenReturn(null);
        ArrayList<String> onlineCppIds = mHelper.getOnlineDevicesCppId();

        assertNotNull(onlineCppIds);
        assertEquals(0, onlineCppIds.size());
    }

    public void testOnlineDevicesEmpty() {
        when(mService.getAliveDeviceList()).thenReturn(new HashSet<DeviceModel>());
        ArrayList<String> onlineCppIds = mHelper.getOnlineDevicesCppId();

        assertNotNull(onlineCppIds);
        assertEquals(0, onlineCppIds.size());
    }

    public void testOnlineDevicesOneDevice() {
        DeviceModel model1 = generateSsdpDeviceModel("udn1", "cppId_1");
        HashSet<DeviceModel> aliveDevices = new HashSet<DeviceModel>();
        aliveDevices.add(model1);
        when(mService.getAliveDeviceList()).thenReturn(aliveDevices);
        ArrayList<String> onlineCppIds = mHelper.getOnlineDevicesCppId();

        assertNotNull(onlineCppIds);
        assertEquals(1, onlineCppIds.size());
        assertEquals("cppId_1", onlineCppIds.get(0));
    }

    public void testOnlineDevicesTwoDevices() {
        DeviceModel model1 = generateSsdpDeviceModel("udn1", "cppId_1");
        DeviceModel model2 = generateSsdpDeviceModel("udn2", "cppId_2");
        LinkedHashSet<DeviceModel> aliveDevices = new LinkedHashSet<DeviceModel>();
        aliveDevices.add(model1);
        aliveDevices.add(model2);
        when(mService.getAliveDeviceList()).thenReturn(aliveDevices);
        ArrayList<String> onlineCppIds = mHelper.getOnlineDevicesCppId();

        assertNotNull(onlineCppIds);
        assertEquals(2, onlineCppIds.size());
        assertEquals("cppId_1", onlineCppIds.get(0));
        assertEquals("cppId_2", onlineCppIds.get(1));
    }

    private static class TestApplianceFactory extends DICommApplianceFactory<TestAppliance> {

        @Override
        public boolean canCreateApplianceForNode(NetworkNode networkNode) {
            return true;
        }

        @Override
        public TestAppliance createApplianceForNode(NetworkNode networkNode) {
            return new TestAppliance(networkNode);
        }
    }

    private static class TestAppliance extends DICommAppliance {

        public TestAppliance(NetworkNode networkNode) {
            super(networkNode, new NullStrategy());
        }

        @Override
        public String getDeviceType() {
            return null;
        }
    }
}
