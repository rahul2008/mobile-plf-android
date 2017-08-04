/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional.mobile;

import android.view.View;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.ui.customviews.OnUpdateListener;
import com.philips.cdp.registration.ui.utils.RLog;

public class ForgotPasswordVerifyCodeFragmentController implements View.OnClickListener, OnUpdateListener, NetworStateListener, EventListener {

    private MobileForgotPasswordVerifyCodeFragment mVerifyCodeFragment;

    public ForgotPasswordVerifyCodeFragmentController(MobileForgotPasswordVerifyCodeFragment fragment) {
        mVerifyCodeFragment = fragment;
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();

        if (id == R.id.btn_reg_Verify) {
            RLog.d(RLog.ONCLICK, "Verify Account : Activiate Account");
            mVerifyCodeFragment.createSMSPasswordResetIntent();
        }
    }

    @Override
    public void onUpdate() {
        mVerifyCodeFragment.handleUI();
    }

    @Override
    public void onEventReceived(final String event) {
        mVerifyCodeFragment.handleUI();
    }

    @Override
    public void onNetWorkStateReceived(final boolean isOnline) {
        mVerifyCodeFragment.handleUI();
        mVerifyCodeFragment.networkUiState();
    }

}
