
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

/**
 * Update user record handler interface
 */
public interface UpdateUserRecordHandler {

	/**
	 * {@code updateUserRecordLogin}method to update user record login
	 */
	public void updateUserRecordLogin();

	/**
	 * {@code updateUserRecordRegister} method to update user record register
	 */
	public void updateUserRecordRegister();
}
