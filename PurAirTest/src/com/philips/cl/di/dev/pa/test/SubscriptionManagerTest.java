package com.philips.cl.di.dev.pa.test;

import junit.framework.TestCase;

import com.philips.cl.di.dev.pa.purifier.SubscriptionEventListener;
import com.philips.cl.di.dev.pa.purifier.SubscriptionManager;

public class SubscriptionManagerTest extends TestCase {
	
	public void testUDPEventReceivedNull() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		manager.onUDPEventReceived(null);

		assertFalse(listener.callbackOccured);
	}
	
	public void testUDPEventReceivedEmptyString() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		manager.onUDPEventReceived("");

		assertFalse(listener.callbackOccured);
	}
	
	public void testUDPEventReceivedNonDecryptableString() {
		SubscriptionTestEventListener listener = new SubscriptionTestEventListener();
		SubscriptionManager manager = SubscriptionManager.getInstance();
		manager.setSubscriptionListener(listener);
		
		String expected = "dfjalsjdfl";
		manager.onUDPEventReceived(expected);

		assertFalse(listener.callbackOccured);
	}

	private class SubscriptionTestEventListener implements SubscriptionEventListener {
		public boolean callbackOccured = false;
		public String callbackString = null;
		@Override
		public void onSubscribeEventOccurred(String data) {
			callbackOccured = true;
			callbackString = data;
		}
	}
	
}
