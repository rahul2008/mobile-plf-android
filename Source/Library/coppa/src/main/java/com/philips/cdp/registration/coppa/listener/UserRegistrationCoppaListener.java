
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.listener;

public interface UserRegistrationCoppaListener {

    void onUserLogoutSuccess();

    void onUserLogoutFailure();

    void onUserLogoutSuccessWithInvalidAccessToken();

}
