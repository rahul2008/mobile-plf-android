
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
 * It is used to handle traditional registration
 */
public interface TraditionalRegistrationHandler {

	/**
	 * {@code onRegisterSuccess}method to on register success
	 * @since 1.0.0
	 */
	public void onRegisterSuccess();

	/**
	 *{{@code onRegisterFailedWithFailure} method to on register failed with failure
	 * @param userRegistrationFailureInfo user registration failure info
	 * @since 1.0.0
     */
	public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo);
}
