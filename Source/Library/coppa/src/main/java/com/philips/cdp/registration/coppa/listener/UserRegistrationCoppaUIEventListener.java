
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.listener;

import android.app.Activity;

public interface UserRegistrationCoppaUIEventListener {

    void onUserRegistrationComplete(Activity activity);

    void onPrivacyPolicyClick(Activity activity);

    void onTermsAndConditionClick(Activity activity);
}
