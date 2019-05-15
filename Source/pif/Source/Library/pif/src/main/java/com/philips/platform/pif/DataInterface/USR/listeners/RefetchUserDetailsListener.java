/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR.listeners;

import com.philips.platform.pif.DataInterface.USR.enums.Error;


/**
 * Callback to proposition for handling refetchUserDetails
 *
 * @since 1903
 */
public interface RefetchUserDetailsListener {

    void onRefetchSuccess();

    void onRefetchFailure(Error error);

}
