package com.philips.cl.di.common.ssdp.controller;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public class InternalMessage {

	private final List<Handler> handlersList;
	public Object obj;

	public int what;

	/**
	 * Constructor for InternalMessage.
	 */
	public InternalMessage() {
		what = -1;
		obj = null;
		handlersList = new ArrayList<Handler>();
	}

	// Check if message handler is already on the list. If YES, it means that
	// MessageController sent this message already.
	/**
	 * Method isHandlerRegistered.
	 * 
	 * @param pHandler
	 *            Handler
	 * @return boolean
	 */
	public boolean isHandlerRegistered(final Handler pHandler) {
		final int isRegistered = handlersList.indexOf(pHandler);
		if (isRegistered != -1) {
			// Log.d(LOG, String.format("ALLREADY REGISTERED handler %s", myHandler));
		}
		return (isRegistered == -1) ? false : true;
	}

	/**
	 * Method registerHandler.
	 * 
	 * @param pHandler
	 *            Handler
	 */
	public void registerHandler(final Handler pHandler) {
		if (handlersList.indexOf(pHandler) == -1) {
			handlersList.add(pHandler);
			// Log.d(LOG, String.format("registering handler %s", h));
		}
	}
}
