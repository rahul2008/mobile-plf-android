
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is used to handle refresh user
 * @since 1.0.0
 */
public interface RefreshUserHandler {

	/**
	 *{@code onRefreshUserSuccess} method to on refresh user success
	 *
	 * @since 1.0.0
	 */
	public void onRefreshUserSuccess();

	/**
	 * {@code onRefreshUserFailed} method to on refresh user failed
	 * @param error  error code when refresh user fails
	 *
	 * @since 1.0.0
     */
	public void onRefreshUserFailed(int error);

}
