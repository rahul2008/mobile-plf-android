/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.cdp.appframework.userregistrationscreen;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;


/**
 * Exteding the RegistrationActivity to override the default backbutton press behaviour.
 */
public class UserRegistrationActivity extends RegistrationActivity {

    @Override
    public void onBackPressed() {
        /*if (!RegistrationLaunchHelper.isBackEventConsumedByRegistration(this)) {
            UIState returnedState =  (UIState) UIStateManager.currentState.getNavigator().onPageLoad(this);

            if (ActivityMap.activityMap.get(userRegState) == UIConstants.UI_HAMBURGER_SCREEN) {
                UIStateManager.currentState = UIStateManager.getFromStateList(UIConstants.UI_HAMBURGER_STATE);
                startActivity(new Intent(this, HamburgerActivity.class));
            }
            super.onBackPressed();
        }*/

    }
}
