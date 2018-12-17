/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.userregistration;


import com.philips.platform.appframework.flowmanager.base.UIStateListener;

public interface URStateListener extends UIStateListener {

    void onLogoutSuccess();
    void onLogoutFailure();
}
