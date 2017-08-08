/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.aboutscreen;

import com.philips.platform.baseapp.screens.termsandconditions.TermsAndPrivacyStateData;

/**
 * Created by philips on 25/07/17.
 */

public class AboutScreenContract {
    interface View {
    }

    interface Action {
        void loadTermsAndPrivacy(TermsAndPrivacyStateData.TermsAndPrivacyEnum termsAndPrivacyEnum);
    }
}
