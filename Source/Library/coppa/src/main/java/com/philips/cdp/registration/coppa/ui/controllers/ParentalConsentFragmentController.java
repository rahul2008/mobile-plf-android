/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
package com.philips.cdp.registration.coppa.ui.controllers;

import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalConsentFragment;
import com.philips.cdp.registration.coppa.utils.AppCoppaTaggingConstants;

public class ParentalConsentFragmentController {

    private ParentalConsentFragment mParentalApprovalFragment;
    private CoppaExtension mCoppaExtension;

    public ParentalConsentFragmentController(final ParentalConsentFragment parentalConsentFragment) {
        this.mParentalApprovalFragment = parentalConsentFragment;
        mCoppaExtension = new CoppaExtension(mParentalApprovalFragment.getRegistrationFragment().getParentActivity().getApplicationContext());
    }

    public void addagreeConfirmation() {
        ConfirmationHandler confirmationHandler = new ConfirmationHandler(mCoppaExtension, mParentalApprovalFragment.getContext());
        confirmationHandler.agreeConfirmation(AppTagingConstants.SEND_DATA, AppCoppaTaggingConstants.SECOND_LEVEL_CONSENT, mParentalApprovalFragment);
    }

    public void disAgreeConfirmation() {
        ConfirmationHandler confirmationHandler = new ConfirmationHandler(mCoppaExtension, mParentalApprovalFragment.getContext());
        confirmationHandler.disAgreeConfirmation(mParentalApprovalFragment);
    }
}
