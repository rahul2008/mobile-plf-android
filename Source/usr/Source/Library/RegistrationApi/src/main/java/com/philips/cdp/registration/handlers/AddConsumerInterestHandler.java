
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is used to handle consumer interest
 * @since 1.0.0
 */
public interface AddConsumerInterestHandler {

	/**
	 * {@codeonAddConsumerInterestSuccess } method to validate on add consumer interest success
	 * @since 1.0.0
	 */
	public void onAddConsumerInterestSuccess();

	/**
	 * {@code onAddConsumerInterestFailedWithError} method to validate on add consumer interest failed with error
	 * @param error  error code
	 * @since 1.0.0
     */
	public void onAddConsumerInterestFailedWithError(int error);

}
