
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * Refresh login session handler
 */
public interface RefreshLoginSessionHandler {

	/**
	 *{@code onRefreshLoginSessionSuccess} method to validate on refresh login session success
	 */
	void onRefreshLoginSessionSuccess();

	/**
	 * {@code onRefreshLoginSessionFailedWithError}method to validate on refresh login session failed with error
	 * @param error error
     */
	void onRefreshLoginSessionFailedWithError(int error);

	/**
	 *  method to validateon refresh login session in progress
	 * @param message message
     */
	void onRefreshLoginSessionInProgress(String message);

}
