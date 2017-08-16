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
//		mHelper.start();
//		mHelper.removePendingMessagesOnQueueForTesting();
//		assertTrue(mHelper.testIsThreadAlive());
//	}
//
//	public void testThreadOnStartWait() {
//		mHelper.start();
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//		assertTrue(mHelper.testIsThreadAlive());
//	}
//
//	public void testThreadOnStartStart() {
//		mHelper.start();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.start();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStartStop() {
//		mHelper.start();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stop();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStartStopStart() {
//		mHelper.start();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stop();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.start();
//		Thread thread3 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId(), thread3.getId());
//	}
//
//	public void testThreadOnStartStopStartWait() {
//		mHelper.start();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stop();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.start();
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
//		mHelper.start();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stop();
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
//		mHelper.start();
//		Thread thread1 = mHelper.getThreadForTesting();
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.start();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStop() {
//		mHelper.stop();
//		mHelper.removePendingMessagesOnQueueForTesting();
//		assertTrue(mHelper.testIsThreadAlive());
//	}
//
//	public void testThreadOnStopWait() {
//		mHelper.stop();
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//		mHelper.removePendingMessagesOnQueueForTesting();
//		assertFalse(mHelper.testIsThreadAlive());
//	}
//
//	public void testThreadOnStopStop() {
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stop();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStopStopWait() {
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.stop();
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
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//
//		mHelper.stop();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertFalse(thread1.isAlive());
//		assertFalse(thread1.getId() == thread2.getId());
//	}
//
//	public void testThreadOnStopWaitStopWait() {
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//
//		mHelper.stop();
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
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.start();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(),  thread2.getId());
//	}
//
//	public void testThreadOnStopStartWait() {
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.start();
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
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//
//		mHelper.start();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertFalse(thread1.isAlive());
//		assertFalse(thread1.getId() == thread2.getId());
//	}
//
//	public void testThreadOnStopWaitStartStop() {
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//
//		try {
//			Thread.sleep(STOPMESSAGE_TIMEOUT);
//		} catch(Exception e) {
//		}
//
//		mHelper.start();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.stop();
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
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.start();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.stop();
//		Thread thread3 = mHelper.getThreadForTesting();
//		mHelper.removePendingMessagesOnQueueForTesting();
//
//		assertTrue(mHelper.testIsThreadAlive());
//		assertEquals(thread1.getId(), thread2.getId(), thread3.getId());
//	}
//
//	public void testThreadOnStopStartStopWait() {
//		mHelper.stop();
//		Thread thread1 = mHelper.getThreadForTesting();
//		mHelper.start();
//		Thread thread2 = mHelper.getThreadForTesting();
//		mHelper.stop();
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
