
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * It is used to update user record
 * @since 1.0.0
 */
public interface UpdateUserRecordHandler {

	/**
	 * {@code updateUserRecordLogin}method to update user record login
	 * @since 1.0.0
	 */
	public void updateUserRecordLogin();

	/**
	 * {@code updateUserRecordRegister} method to update user record register
	 * @since 1.0.0
	 */
	public void updateUserRecordRegister();
}
