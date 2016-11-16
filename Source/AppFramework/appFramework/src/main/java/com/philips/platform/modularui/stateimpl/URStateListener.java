/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import com.philips.platform.modularui.statecontroller.UIStateListener;

public interface URStateListener extends UIStateListener{

    void onLogoutSuccess();
    void onLogoutFailure();
}
