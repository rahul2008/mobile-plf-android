/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;
import com.philips.cl.di.common.ssdp.lib.SsdpService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;

public class SsdpServiceHelperThreadTest extends RobolectricTest {

	private static final int STOPSSDP_TESTDELAY = 300;
	private static final int STOPMESSAGE_TIMEOUT = STOPSSDP_TESTDELAY + 100;

	private SsdpServiceHelper mHelper;
	private SsdpService mService;

	@Before
	public void setUp() throws Exception {

		mHelper = new SsdpServiceHelper(null, null);
		mHelper.setStopDelayForTesting(STOPSSDP_TESTDELAY);
	}

	@After
	public void tearDown() throws Exception {
	}

	// ***** START TESTS TO START STOP THREAD WHEN METHODS ARE CALLED *****

	@Test
	public void testThreadConstructor() {
		assertFalse(mHelper.testIsThreadAlive());
	}

//	public void testThreadOnStart() {
//		mHelper.startDiscoveryAsync();
//		mHelper.removePendingMessagesOnQueueForTesting();
//		assertTrue(mHelper.testIsThreadAlive());
//	}
//
//	public void testThreadOnStartWait() {
//		mHelper.startDiscoveryAsync();
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//		assertTrue(mHelper.testIsThreadAlive());
//	}
//
//	public void testThreadOnStartStart() {
//		mHelper.startDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.startDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStartStop() {
//		mHelper.startDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStartStopStart() {
//		mHelper.startDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.startDiscoveryAsync();
//		Thread thread3 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId(), thread3.getId());
//	}
//
//	public void testThreadOnStartStopStartWait() {
//		mHelper.startDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.startDiscoveryAsync();
//		Thread thread3 = mHelper.getThreadForTesting();
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId(), thread3.getId());
//	}
//
//	public void testThreadOnStartStopWait() {
//		mHelper.startDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertFalse(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStartWaitStart() {
//		mHelper.startDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.startDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStop() {
//		mHelper.stopDiscoveryAsync();
//		mHelper.removePendingMessagesOnQueueForTesting();
//		assertTrue(mHelper.testIsThreadAlive());
//	}
//
//	public void testThreadOnStopWait() {
//		mHelper.stopDiscoveryAsync();
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//		assertFalse(mHelper.testIsThreadAlive());
//	}
//
//	public void testThreadOnStopStop() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStopStopWait() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertFalse(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStopWaitStop() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//
//		mHelper.stopDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertFalse(thread1.isAlive());
//		assertFalse(thread1.getId() == thread2.getId());
//	}
//
//	public void testThreadOnStopWaitStopWait() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//
//		mHelper.stopDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertFalse(mHelper.testIsThreadAlive());
//		assertFalse(thread2.isAlive());
//		assertFalse(thread1.isAlive());
//		assertFalse(thread1.getId() == thread2.getId());
//	}
//
//	public void testThreadOnStopStart() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.startDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStopStartWait() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.startDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStopWaitStart() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//
//		mHelper.startDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertFalse(thread1.isAlive());
//		assertFalse(thread1.getId() == thread2.getId());
//	}
//
//	public void testThreadOnStopWaitStartStop() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//
//		mHelper.startDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread3 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertFalse(thread1.isAlive());
//		assertFalse(thread1.getId() == thread2.getId());
//		assertEquals(thread2.getId(), thread3.getId());
//	}
//
//	public void testThreadOnStopStartStop() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.startDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread3 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(), thread2.getId(), thread3.getId());
//	}
//
//	public void testThreadOnStopStartStopWait() {
//		mHelper.stopDiscoveryAsync();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.startDiscoveryAsync();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.stopDiscoveryAsync();
//		Thread thread3 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertFalse(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(), thread2.getId(), thread3.getId());
//	}
// ***** STOP TESTS TO START STOP THREAD WHEN METHODS ARE CALLED *****

}
