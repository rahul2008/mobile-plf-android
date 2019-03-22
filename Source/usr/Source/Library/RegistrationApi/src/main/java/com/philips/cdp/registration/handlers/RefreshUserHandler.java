
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is a callback class to proposition for handling refresh User
 * @since 1.0.0
 */
public interface RefreshUserHandler {

	/**
	 *{@code onRefreshUserSuccess} method is invoked on refresh User success
	 *
	 * @since 1.0.0
	 */
	public void onRefreshUserSuccess();

	/**
	 * {@code onRefreshUserFailed} method is invoked on refresh user fails
	 * @param error  error code when refresh user fails
	 *
	 * @since 1.0.0
     */
	public void onRefreshUserFailed(int error);

	/**
	 * {@code onRefreshUserInProgress}method is invoked on refresh login session in progress
	 * @param message  progress message on refresh user in progress
	 * @since 1902
	 * ToDo : Need to update since version before merge to develop
	 */
	void onRefreshUserInProgress(String message);

	/**
	 * {@code onRefreshUserFailedAndLoggedout}method is invoked on user refresh Failure due to excess login at multiple devices and gets forced logged out on refresh session
	 * @since 1902
	 * TODO : Need to update version before merge to develop
	 */
	void onRefreshUserFailedAndLoggedout();

}
