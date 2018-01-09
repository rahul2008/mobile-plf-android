
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is used to update consumer interest
 */
public interface UpdateConsumerInterestHandler {

	/**
	 * {@code onUpdateConsumerInterestSuccess} method to on update consumer interest success
	 * @since 1.0.0
	 */
	public void onUpdateConsumerInterestSuccess();

	/**
	 * {@code onUpdateConsumerInterestFailedWithError}method to on update consumer interest failed with error
	 * @param error api error
	 * @since 1.0.0
     */
	public void onUpdateConsumerInterestFailedWithError( com.janrain.android.capture.CaptureApiError error);

}
