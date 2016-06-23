
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * Update receive marketing email handler
 */
public interface UpdateReceiveMarketingEmailHandler {

	/**
	 * {@code onUpdateReceiveMarketingEmailSuccess}method to on update receive marketing email success
	 */
	public void onUpdateReceiveMarketingEmailSuccess();

	/**
	 * {@code onUpdateReceiveMarketingEmailFailedWithError}method toon update receive marketing email failed with error
	 * @param error error
     */
	public void onUpdateReceiveMarketingEmailFailedWithError(int error);

}
