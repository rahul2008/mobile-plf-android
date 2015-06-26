/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port;

import com.philips.cdp.dicommclient.request.Error;

public interface DICommPortListener {

	/**
	 * Called when the (locally) know state of a port has changed. This may happen because of two reasons:
	 * <ul>
	 * <li>
	 * As the result of performing an action on the port. (putProperties(), subscribe(), ...)
	 * </li>
	 * <li>
	 * When a subscription event is received for this port.
	 * </li>
	 * </ul>
	 * 
	 * <p><b>WARNING:</b> You cannot rely on this callback to be the response to an action you initiated on the port.
	 * The received callback might also be a subscription event or the result of another action initiated by another part of the code.</p>
	 * 
	 * @param port The port that received an update.
	 */
	public void onPortUpdate(DICommPort<?> port);

    public void onPortError(DICommPort<?> port, Error error, String errorData);

}
