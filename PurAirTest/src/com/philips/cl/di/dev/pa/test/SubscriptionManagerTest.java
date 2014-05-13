package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionManager;

public class SubscriptionManagerTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
		SubscriptionManager.setDummySubscriptionManagerForTesting(null);
		super.setUp();
	}

	protected void tearDown() throws Exception {
		// Reset SubscriptionManager after tests
		SubscriptionManager.setDummySubscriptionManagerForTesting(null);
		super.tearDown();
	}
	
	public void testUDPEventReceivedNull() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		manager.onUDPEventReceived(null);

		assertFalse(listener.udpCallbackOccured);
	}
	
	public void testUDPEventReceivedEmptyString() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		manager.onUDPEventReceived("");

		assertFalse(listener.udpCallbackOccured);
	}
	
	public void testUDPEventReceivedNonDecryptableString() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		
		String expected = "dfjalsjdfl";
		manager.onUDPEventReceived(expected);

		assertTrue(listener.udpCallbackOccured);
	}
	
	public void testDCSEventReceivedNull() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		manager.onDCSEventReceived(null);

		assertFalse(listener.dcsCallbackOccured);
	}
	
	public void testDCSEventReceivedEmptyString() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		manager.onDCSEventReceived("");

		assertFalse(listener.dcsCallbackOccured);
	}
	
	public void testDCSEventReceivedRandomString() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		
		String expected = "dfjalsjdfl";
		manager.onDCSEventReceived(expected);

		assertTrue(listener.dcsCallbackOccured);
	}

	private class SubscriptionTestEventListener implements SubscriptionEventListener {
		public boolean udpCallbackOccured = false;
		public boolean dcsCallbackOccured = false;
		public String callbackString = null;
		@Override
		public void onLocalEventReceived(String data) {
			udpCallbackOccured = true;
			callbackString = data;
		}
		@Override
		public void onRemoteEventReceived(String data) {
			dcsCallbackOccured = true;
			callbackString = data;
		}
	}
	
}
