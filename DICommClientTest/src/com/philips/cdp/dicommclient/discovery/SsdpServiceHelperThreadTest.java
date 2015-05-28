package com.philips.cdp.dicommclient.discovery;

import static org.mockito.Mockito.mock;

import com.philips.cdp.dicommclient.testutil.MockitoTestCase;
import com.philips.cl.di.common.ssdp.lib.SsdpService;

public class SsdpServiceHelperThreadTest extends MockitoTestCase {

	private static final int STOPSSDP_TESTDELAY = 300;
	private static final int STOPMESSAGE_TIMEOUT = STOPSSDP_TESTDELAY + 100;

	private SsdpServiceHelper mHelper;
	private SsdpService mService;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		mService = mock(SsdpService.class);
		mHelper = new SsdpServiceHelper(mService, null);
		mHelper.setStopDelayForTesting(STOPSSDP_TESTDELAY);
	}

// ***** START TESTS TO START STOP THREAD WHEN METHODS ARE CALLED *****
	public void testThreadConstructor() {
		assertFalse(mHelper.testIsThreadAlive());
	}

	public void testThreadOnStart() {
		mHelper.startDiscoveryAsync();
		mHelper.removePendingMessagesOnQueueForTesting();
		assertTrue(mHelper.testIsThreadAlive());
	}

	public void testThreadOnStartWait() {
		mHelper.startDiscoveryAsync();
		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.removePendingMessagesOnQueueForTesting();
		assertTrue(mHelper.testIsThreadAlive());
	}

	public void testThreadOnStartStart() {
		mHelper.startDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.startDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId());
	}

	public void testThreadOnStartStop() {
		mHelper.startDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId());
	}

	public void testThreadOnStartStopStart() {
		mHelper.startDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.startDiscoveryAsync();
		Thread thread3 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId(), thread3.getId());
	}

	public void testThreadOnStartStopStartWait() {
		mHelper.startDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.startDiscoveryAsync();
		Thread thread3 = mHelper.getThreadForTesting();
		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId(), thread3.getId());
	}

	public void testThreadOnStartStopWait() {
		mHelper.startDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.removePendingMessagesOnQueueForTesting();

		assertFalse(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId());
	}

	public void testThreadOnStartWaitStart() {
		mHelper.startDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.startDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId());
	}

	public void testThreadOnStop() {
		mHelper.stopDiscoveryAsync();
		mHelper.removePendingMessagesOnQueueForTesting();
		assertTrue(mHelper.testIsThreadAlive());
	}

	public void testThreadOnStopWait() {
		mHelper.stopDiscoveryAsync();
		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.removePendingMessagesOnQueueForTesting();
		assertFalse(mHelper.testIsThreadAlive());
	}

	public void testThreadOnStopStop() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId());
	}

	public void testThreadOnStopStopWait() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();

		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.removePendingMessagesOnQueueForTesting();

		assertFalse(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId());
	}

	public void testThreadOnStopWaitStop() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();

		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}

		mHelper.stopDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertFalse(thread1.isAlive());
		assertFalse(thread1.getId() == thread2.getId());
	}

	public void testThreadOnStopWaitStopWait() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();

		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}

		mHelper.stopDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();

		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.removePendingMessagesOnQueueForTesting();

		assertFalse(mHelper.testIsThreadAlive());
		assertFalse(thread2.isAlive());
		assertFalse(thread1.isAlive());
		assertFalse(thread1.getId() == thread2.getId());
	}

	public void testThreadOnStopStart() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.startDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId());
	}

	public void testThreadOnStopStartWait() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.startDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();

		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(),  thread2.getId());
	}

	public void testThreadOnStopWaitStart() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();

		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}

		mHelper.startDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertFalse(thread1.isAlive());
		assertFalse(thread1.getId() == thread2.getId());
	}

	public void testThreadOnStopWaitStartStop() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();

		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}

		mHelper.startDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread3 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertFalse(thread1.isAlive());
		assertFalse(thread1.getId() == thread2.getId());
		assertEquals(thread2.getId(), thread3.getId());
	}

	public void testThreadOnStopStartStop() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.startDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread3 = mHelper.getThreadForTesting();
		mHelper.removePendingMessagesOnQueueForTesting();

		assertTrue(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(), thread2.getId(), thread3.getId());
	}

	public void testThreadOnStopStartStopWait() {
		mHelper.stopDiscoveryAsync();
		Thread thread1 = mHelper.getThreadForTesting();
		mHelper.startDiscoveryAsync();
		Thread thread2 = mHelper.getThreadForTesting();
		mHelper.stopDiscoveryAsync();
		Thread thread3 = mHelper.getThreadForTesting();

		try {
			Thread.sleep(STOPMESSAGE_TIMEOUT);
		} catch(Exception e) {
		}
		mHelper.removePendingMessagesOnQueueForTesting();

		assertFalse(mHelper.testIsThreadAlive());
		assertEquals(thread1.getId(), thread2.getId(), thread3.getId());
	}
// ***** STOP TESTS TO START STOP THREAD WHEN METHODS ARE CALLED *****

}
