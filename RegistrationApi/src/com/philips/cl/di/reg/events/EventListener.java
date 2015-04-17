package com.philips.cl.di.reg.events;

public interface EventListener {

	/**
	 * raise a event
	 * 
	 * @param event
	 */
	public void raiseEvent(String event);

	/**
	 * take action on this event
	 * 
	 * @param event
	 */
	public void onEventReceived(String event);

}
