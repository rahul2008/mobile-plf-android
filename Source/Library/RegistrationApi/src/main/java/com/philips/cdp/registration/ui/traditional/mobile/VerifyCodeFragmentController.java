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
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.ui.customviews.onUpdateListener;
import com.philips.cdp.registration.ui.utils.RLog;

public class VerifyCodeFragmentController implements View.OnClickListener, onUpdateListener, NetworStateListener, EventListener {

    private MobileVerifyCodeFragment mVerifyCodeFragment;

    public VerifyCodeFragmentController(MobileVerifyCodeFragment fragment) {
        mVerifyCodeFragment = fragment;
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();

        if (id == R.id.btn_reg_Verify) {
            RLog.d(RLog.ONCLICK, "Verify Account : Activiate Account");
            mVerifyCodeFragment.verifyMobileNumberTask();
        }
    }

    @Override
    public void onUpadte() {
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
