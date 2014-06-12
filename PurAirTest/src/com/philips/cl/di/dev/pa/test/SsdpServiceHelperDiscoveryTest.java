package com.philips.cl.di.dev.pa.test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import android.content.Context;
import android.os.Handler.Callback;
import android.test.InstrumentationTestCase;

import com.philips.cl.di.common.ssdp.lib.SsdpService;
import com.philips.cl.di.dev.pa.constant.AppConstants;
import com.philips.cl.di.dev.pa.cpp.CPPController;
import com.philips.cl.di.dev.pa.newpurifier.SsdpServiceHelper;
import com.philips.cl.di.dev.pa.purifier.SubscriptionHandler;

public class SsdpServiceHelperDiscoveryTest extends InstrumentationTestCase {

	private static final int SHORT_TIMEOUT = 200;
	private static final int STOPSSDP_TESTDELAY = 300;
	private static final int STOPMESSAGE_TIMEOUT = STOPSSDP_TESTDELAY + 100;
	private static final int SLEEP_STEP = 20;

	private SsdpServiceHelper mHelper;
	private SsdpService mService;

	@Override
	protected void setUp() throws Exception {
		// Necessary to get Mockito framework working
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());

		mService = mock(SsdpService.class);
		mHelper = new SsdpServiceHelper(mService, null);
		mHelper.setStopDelayForTesting(STOPSSDP_TESTDELAY);
		super.setUp();
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

	// ***** START TESTS TO START STOP DISCOVERY WHEN METHODS ARE CALLED *****
	public void testDiscoveryConstructor() {
		verify(mService, never()).startDeviceDiscovery(any(Callback.class));
		verify(mService, never()).stopDeviceDiscovery();
	}

	public void testDiscoveryOnStart() {
		mHelper.startDiscoveryAsync();
		waitForMessagesToBeProcessed(SHORT_TIMEOUT);
		mHelper.removePendingMessagesOnQueueForTesting();

		verify(mService).startDeviceDiscovery(any(Callback.class));
		verify(mService, never()).stopDeviceDiscovery();
	}

	public void testDiscoveryOnStartStart() {
		mHelper.startDiscoveryAsync();
		mHelper.startDiscoveryAsync();
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

	public void testDiscoveryOnStartProcessStop() {
		mHelper.startDiscoveryAsync();
		waitForMessagesToBeProcessed(SHORT_TIMEOUT);
		mHelper.stopDiscoveryAsync();
		mHelper.removePendingMessagesOnQueueForTesting();

		verify(mService).startDeviceDiscovery(any(Callback.class));
		verify(mService, never()).stopDeviceDiscovery();
	}

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

	// ***** STOP TESTS TO START STOP DISCOVERY WHEN METHODS ARE CALLED *****
}
