/*
 * Copyright (c) Koninklijke Philips N.V. 2019
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR.listeners;

/**
 *
 * Callback to proposition for refresh session
 *
 * @since 1903
 */
import com.philips.platform.pif.DataInterface.USR.enums.Error;

public interface RefreshSessionListener {
    void refreshSessionSuccess();

    void refreshSessionFailed(Error error);

    void forcedLogout();
}
