package com.philips.cdp.appframework.userregistrationscreen;

import android.content.Intent;

import com.philips.cdp.appframework.homescreen.HomeActivity;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

/**
 * Exteding the RegistrationActivity to override the default backbutton press behaviour.
 */
public class UserRegistrationActivity extends RegistrationActivity {

    @Override
    public void onBackPressed() {
        if (!RegistrationLaunchHelper.isBackEventConsumedByRegistration(this)) {
            startActivity(new Intent(UserRegistrationActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
            super.onBackPressed();
        }

    }
}
