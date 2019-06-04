/*
 * Copyright (c) Koninklijke Philips N.V. 2019
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.pif.DataInterface.USR.listeners;

/**
 * It is a callback for proposition to register listener for logout,refresh session and refetch userdetails
 *
 * @since 1903
 */
public interface UserDataListener extends LogoutSessionListener, RefreshSessionListener, RefetchUserDetailsListener  {
}
