/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import com.philips.cdp.registration.events.EventListener;

public class VerifyCodeFragmentController implements EventListener {

    private MobileVerifyCodeFragment mVerifyCodeFragment;

    public VerifyCodeFragmentController(MobileVerifyCodeFragment fragment) {
        mVerifyCodeFragment = fragment;
    }

    @Override
    public void onEventReceived(final String event) {
        mVerifyCodeFragment.handleUI();
    }

}
