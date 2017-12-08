
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

/**
 * Traditional regitration handler interface
 */
public interface TraditionalRegistrationHandler {

	/**
	 * {@code onRegisterSuccess}method to on register success
	 */
	public void onRegisterSuccess();

	/**
	 *{{@code onRegisterFailedWithFailure} method to on register failed with failure
	 * @param userRegistrationFailureInfo user registration failure info
     */
	public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo);
}
