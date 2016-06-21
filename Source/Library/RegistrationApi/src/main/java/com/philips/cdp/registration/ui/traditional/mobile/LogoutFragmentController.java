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
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.ui.customviews.XCheckBox;
import com.philips.cdp.registration.ui.utils.RLog;

public class LogoutFragmentController implements View.OnClickListener, XCheckBox.OnCheckedChangeListener,LogoutHandler,NetworStateListener {

    private MobileLogoutFragment mMobileLogoutFragment;

    public LogoutFragmentController(MobileLogoutFragment fragment) {
        mMobileLogoutFragment = fragment;
    }

    @Override
    public void onClick(final View v) {
        int id = v.getId();

        if (id == R.id.btn_reg_sign_out) {
            RLog.d(RLog.ONCLICK, "Sign Out : Sign Out");
            onLogoutSuccess();
        }
    }

    @Override
    public void onCheckedChanged(final View view, final boolean checked) {

    }

    @Override
    public void onLogoutSuccess() {
        mMobileLogoutFragment.getLogout();

    }

    @Override
    public void onLogoutFailure(final int responseCode, final String message) {

    }

    @Override
    public void onNetWorkStateReceived(final boolean isOnline) {
        mMobileLogoutFragment.networkUiState();

    }
}
