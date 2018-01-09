
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is used to  update user details
 */
public interface UpdateUserDetailsHandler {

	/**
	 * {@code onUpdateSuccess}method to on update receive marketing email success
	 * @since 1.0.0
	 */
	public void onUpdateSuccess();

	/**
	 * {@code onUpdateFailedWithError}method toon update receive marketing email failed with error
	 * @param error - int error
	 * @since 1.0.0
     */
	public void onUpdateFailedWithError(int error);

}
