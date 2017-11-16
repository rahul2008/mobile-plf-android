/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.aboutscreen;

import com.philips.platform.baseapp.screens.termsandconditions.WebViewEnum;

/**
 * Created by philips on 25/07/17.
 */

public interface AboutScreenContract {
    interface View {
    }

    interface Action {
        void loadTermsAndPrivacy(WebViewEnum termsAndPrivacyEnum);
    }
}
