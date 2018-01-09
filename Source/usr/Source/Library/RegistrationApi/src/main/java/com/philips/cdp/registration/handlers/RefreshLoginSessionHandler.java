
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is used to handle Refresh login session
 */
public interface RefreshLoginSessionHandler {

	/**
	 *{@code onRefreshLoginSessionSuccess} method to validate on refresh login session success
	 * @since 1.0.0
	 */
	void onRefreshLoginSessionSuccess();

	/**
	 * {@code onRefreshLoginSessionFailedWithError}method to validate on refresh login session failed with error
	 * @param error - int error
	 * @since 1.0.0
     */
	void onRefreshLoginSessionFailedWithError(int error);

	/**
	 *  method to validateon refresh login session in progress
	 * @param message - String message
	 * @since 1.0.0
     */
	void onRefreshLoginSessionInProgress(String message);

}
