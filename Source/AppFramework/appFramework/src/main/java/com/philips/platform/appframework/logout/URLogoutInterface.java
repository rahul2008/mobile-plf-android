/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.logout;

import android.content.Context;

import com.philips.cdp.registration.User;

public interface URLogoutInterface {

    void performLogout(Context context, User user, boolean isDSPollingEnabled, boolean isAutoLogoutEnabled);

    void setUrLogoutListener(URLogoutListener urLogoutListener);

    void removeListener();
}
