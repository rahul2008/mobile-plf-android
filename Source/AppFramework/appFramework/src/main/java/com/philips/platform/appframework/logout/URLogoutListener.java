/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.logout;

public interface URLogoutListener {

    void onLogoutResultSuccess();

    void onLogoutResultFailure(int i, String errorMessage);

}
