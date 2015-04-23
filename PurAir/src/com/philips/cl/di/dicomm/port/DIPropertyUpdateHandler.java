package com.philips.cl.di.dicomm.port;

public abstract class DIPropertyUpdateHandler {

	private final boolean isSubscriptionsEnabled;
	private final boolean isResponsesEnabled;

	public DIPropertyUpdateHandler() {
		isSubscriptionsEnabled = true;
		isResponsesEnabled = true;
	}

	// Hide this method for subclasses
	void handlePropertyUpdateForPort(DICommPort<?> port, boolean isSubscription) {
		if ((isSubscriptionsEnabled && isSubscription) || (isResponsesEnabled && !isSubscription)) {
			handlePropertyUpdateForPort(port);
		}
	}

	public abstract void handlePropertyUpdateForPort(DICommPort<?> port);

}
