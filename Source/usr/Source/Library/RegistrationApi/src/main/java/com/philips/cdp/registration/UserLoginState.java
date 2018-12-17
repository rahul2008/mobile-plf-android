package com.philips.cdp.registration;


/**
 * API UserLoginState is implemented to return the exact status of the user while logging in. The user log in states returned are the following:
 *
 * USER_NOT_LOGGED_IN: Informs the state of the user as not logged in
 * PENDING_VERIFICATION: Informs that the verification is pending for the user
 * PENDING_TERM_CONDITION: Informs that the terms and conditions acceptance is pending for the user.
 * PENDING_HSDP_LOGIN: Informs that HSDP login is pending for the user.
 * USER_LOGGED_IN: Informs that the user is successfully logged in.
 *
 * @since 1804.0
 *
 */
public enum UserLoginState {
    USER_NOT_LOGGED_IN,
    PENDING_VERIFICATION,
    PENDING_TERM_CONDITION,
    PENDING_HSDP_LOGIN,
    USER_LOGGED_IN;


}

