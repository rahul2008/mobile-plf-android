
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * Refresh user handler interface
 */
public interface RefreshUserHandler {

	/**
	 *{@code onRefreshUserSuccess} method to on refresh user success
	 */
	public void onRefreshUserSuccess();

	/**
	 * {@code onRefreshUserFailed} method to on refresh user failed
	 * @param error   error
     */
	public void onRefreshUserFailed(int error);

}
