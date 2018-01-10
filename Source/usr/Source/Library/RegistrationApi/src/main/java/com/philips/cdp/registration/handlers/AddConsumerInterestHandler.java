
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is a call back class to proposition for adding consumer interest
 * @since 1.0.0
 */
public interface AddConsumerInterestHandler {

	/**
	 * {@codeonAddConsumerInterestSuccess } method is invoked when consumer interest is added successfully
	 * @since 1.0.0
	 */
	public void onAddConsumerInterestSuccess();

	/**
	 * {@code onAddConsumerInterestFailedWithError} method is invoked when adding consumer interest fails
	 * @param error  error code
	 * @since 1.0.0
     */
	public void onAddConsumerInterestFailedWithError(int error);

}
