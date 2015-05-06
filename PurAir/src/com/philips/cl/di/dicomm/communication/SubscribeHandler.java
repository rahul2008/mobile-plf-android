package com.philips.cl.di.dicomm.communication;

import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;

public abstract class SubscribeHandler {

	public abstract void enableSubscription(NetworkNode networkNode);
	public abstract void disableSubscription();

}
