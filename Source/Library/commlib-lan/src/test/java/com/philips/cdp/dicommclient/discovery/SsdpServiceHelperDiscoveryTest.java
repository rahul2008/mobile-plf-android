/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import android.os.Handler.Callback;

import com.philips.cdp.dicommclient.MockitoTestCase;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceFactory;
import com.philips.cdp2.commlib.core.communication.NullCommunicationStrategy;
import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.common.ssdp.models.DeviceModel;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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

        mService = mock(SsdpService.class);
        mHelper = new SsdpServiceHelper(mService, null);
        mHelper.setStopDelayForTesting(STOPSSDP_TESTDELAY);
    }

    private void waitForMessagesToBeProcessed(int timeout) {
        for (int i = 0; i < timeout; i += SLEEP_STEP) {
            if (mHelper.noMorePendingMessagesForTesting())
                break;
            try {
                Thread.sleep(SLEEP_STEP);
            } catch (Exception ignored) {
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
//		mHelper.start();
//		waitForMessagesToBeProcessed(SHORT_TIMEOUT);
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		verify(mService).startDeviceDiscovery(any(Callback.class));
//		verify(mService, never()).stopDeviceDiscovery();
//	}

    public void testDiscoveryOnStartStart() {
        mHelper.start();
        mHelper.stop();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartStop() {
        mHelper.start();
        mHelper.stop();

        verify(mService, atMost(1)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

//	public void testDiscoveryOnStartProcessStop() {
//		mHelper.start();
//		waitForMessagesToBeProcessed(SHORT_TIMEOUT);
//		mHelper.stop();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		verify(mService).startDeviceDiscovery(any(Callback.class));
//		verify(mService, never()).stopDeviceDiscovery();
//	}

    public void testDiscoveryOnStartStopWait() {
        mHelper.start();
        mHelper.stop();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(1)).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartProcessStopWait() {
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stop();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartStopStart() {
        mHelper.start();
        mHelper.stop();
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartProcessStopStart() {
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stop();
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        // Must be called twice to set new DiscoveryEventLindiistener.
        verify(mService, times(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartStopWaitStart() {
        mHelper.start();
        mHelper.stop();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }

        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStartProcessStopWaitStart() {
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stop();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }

        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, times(2)).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStop() {
        mHelper.stop();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopWait() {
        mHelper.stop();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStop() {
        mHelper.stop();
        mHelper.stop();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStopWaitWait() {
        mHelper.stop();
        mHelper.stop();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, never()).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStart() {
        mHelper.stop();
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopWaitStart() {
        mHelper.stop();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStartStop() {
        mHelper.stop();
        mHelper.start();
        mHelper.stop();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService, atMost(1)).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStartProcessStop() {
        mHelper.stop();
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stop();
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService, never()).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopStartProcessStopWaitWait() {
        mHelper.stop();
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stop();

        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopWaitStartProcessStop() {
        mHelper.stop();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stop();
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService).stopDeviceDiscovery();
    }

    public void testDiscoveryOnStopWaitStartProcessStopWait() {
        mHelper.stop();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.start();
        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
        mHelper.stop();
        try {
            Thread.sleep(STOPMESSAGE_TIMEOUT);
        } catch (Exception ignored) {
        }
        mHelper.removePendingMessagesOnQueueForTesting();

        verify(mService).startDeviceDiscovery(any(Callback.class));
        verify(mService, times(2)).stopDeviceDiscovery();
    }

//    @SuppressWarnings("unchecked")
//    public void testDiscoverySyncLocalOnStart() {
//        DiscoveryManager discMan = mock(DiscoveryManager.class);
//        DiscoveryManager.setDummyDiscoveryManagerForTesting(discMan);
//
//        mHelper.start();
//        waitForMessagesToBeProcessed(SHORT_TIMEOUT);
//        mHelper.removePendingMessagesOnQueueForTesting();
//
//        verify(discMan).syncLocalAppliancesWithSsdpStackDelayed();
//        verify(discMan, never()).cancelSyncLocalAppliancesWithSsdpStack();
//
//        DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
//        DiscoveryManager.createSharedInstance(getInstrumentation().getTargetContext(), mock(ApplianceFactory.class));
//    }
//
//    @SuppressWarnings("unchecked")
//    public void testDiscoverySyncLocalOnStop() {
//        DiscoveryManager discMan = mock(DiscoveryManager.class);
//        DiscoveryManager.setDummyDiscoveryManagerForTesting(discMan);
//
//        mHelper.stop();
//        try {
//            Thread.sleep(STOPMESSAGE_TIMEOUT);
//        } catch (Exception ignored) {
//        }
//        mHelper.removePendingMessagesOnQueueForTesting();
//
//        verify(discMan, never()).syncLocalAppliancesWithSsdpStackDelayed();
//        verify(discMan).cancelSyncLocalAppliancesWithSsdpStack();
//
//        DiscoveryManager.setDummyDiscoveryManagerForTesting(null);
//        DiscoveryManager.createSharedInstance(getInstrumentation().getTargetContext(), mock(ApplianceFactory.class));
//    }

    // ***** STOP TESTS TO START STOP DISCOVERY WHEN METHODS ARE CALLED *****

    public void testOnlineDevicesNull() {
        when(mService.getAliveDeviceList()).thenReturn(null);
        ArrayList<String> onlineCppIds = mHelper.getOnlineDevicesCppId();

        Assert.assertNotNull(onlineCppIds);
        Assert.assertEquals(0, onlineCppIds.size());
    }

    public void testOnlineDevicesEmpty() {
        when(mService.getAliveDeviceList()).thenReturn(new HashSet<DeviceModel>());
        ArrayList<String> onlineCppIds = mHelper.getOnlineDevicesCppId();

        Assert.assertNotNull(onlineCppIds);
        Assert.assertEquals(0, onlineCppIds.size());
    }

    public void testOnlineDevicesOneDevice() {
        DeviceModel model1 = generateSsdpDeviceModel("udn1", "cppId_1");
        HashSet<DeviceModel> aliveDevices = new HashSet<DeviceModel>();
        aliveDevices.add(model1);
        when(mService.getAliveDeviceList()).thenReturn(aliveDevices);
        ArrayList<String> onlineCppIds = mHelper.getOnlineDevicesCppId();

        Assert.assertNotNull(onlineCppIds);
        Assert.assertEquals(1, onlineCppIds.size());
        Assert.assertEquals("cppId_1", onlineCppIds.get(0));
    }

    public void testOnlineDevicesTwoDevices() {
        DeviceModel model1 = generateSsdpDeviceModel("udn1", "cppId_1");
        DeviceModel model2 = generateSsdpDeviceModel("udn2", "cppId_2");
        LinkedHashSet<DeviceModel> aliveDevices = new LinkedHashSet<DeviceModel>();
        aliveDevices.add(model1);
        aliveDevices.add(model2);
        when(mService.getAliveDeviceList()).thenReturn(aliveDevices);
        ArrayList<String> onlineCppIds = mHelper.getOnlineDevicesCppId();

        Assert.assertNotNull(onlineCppIds);
        Assert.assertEquals(2, onlineCppIds.size());
        Assert.assertEquals("cppId_1", onlineCppIds.get(0));
        Assert.assertEquals("cppId_2", onlineCppIds.get(1));
    }

    private static class TestApplianceFactory implements ApplianceFactory<TestAppliance> {

        @Override
        public boolean canCreateApplianceForNode(NetworkNode networkNode) {
            return true;
        }

        @Override
        public TestAppliance createApplianceForNode(NetworkNode networkNode) {
            return new TestAppliance(networkNode);
        }

        @Override
        public Set<String> getSupportedDeviceTypes() {
            return null;
        }
    }

    private static class TestAppliance extends Appliance {

        TestAppliance(NetworkNode networkNode) {
            super(networkNode, new NullCommunicationStrategy());
        }

        @Override
        public String getDeviceType() {
            return null;
        }
    }
}
