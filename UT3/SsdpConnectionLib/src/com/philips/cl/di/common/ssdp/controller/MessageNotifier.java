/*******************************************************************************
 * File name: MessageNotifier.java
 * Creation date: 2011
 * Author: Maciej Gorski
 * Change log:
 ******************************************************************************/
package com.philips.cl.di.common.ssdp.controller;

import android.os.Handler;

/**
 * @author 310151556
 * @version $Revision: 1.0 $
 */
public interface MessageNotifier {

	/**
	 * Method addMessageHandler.
	 * 
	 * @param handler
	 *            Handler
	 */
	void addMessageHandler(Handler handler);

	/**
	 * Method removeMessageHandler.
	 * 
	 * @param handler
	 *            Handler
	 */
	void removeMessageHandler(Handler handler);
}
