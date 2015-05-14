
package com.philips.cl.di.reg.events;

public interface EventListener {

	/**
	 * take action on this event
	 * 
	 * @param event
	 */
	public void onEventReceived(String event);

}
