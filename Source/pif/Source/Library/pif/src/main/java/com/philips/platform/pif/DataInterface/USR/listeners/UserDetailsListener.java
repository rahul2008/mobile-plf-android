/*
 * Copyright (c) Koninklijke Philips N.V. 2018
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR.listeners;

/**
 * Interface to handle all user data related listeners
 * @since 2018.1.0
 */
public interface UserDetailsListener {

    /**
     * {@code onRefetchSuccess} method is invoked on refetch user details Success
     * @since 2018.1.0
     */
    void onRefetchSuccess();
    /**
     * {@code onRefetchFailure} method is invoked on refetch user details Failure
     * @param error error code on failure
     * @since 2018.1.0
     */
    void onRefetchFailure(int error);
    /**
     * {@code onUpdateSuccess} method is invoked on update Success
     * @since 2018.1.0
     */
    void onUpdateSuccess();
    /**
     * {@code onUpdateFailure} method is invoked on update Failure
     * @param error error code on failure
     * @since 2018.1.0
     */
    void onUpdateFailure(int error);

}
