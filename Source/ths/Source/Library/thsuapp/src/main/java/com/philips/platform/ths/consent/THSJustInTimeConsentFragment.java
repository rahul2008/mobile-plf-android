package com.philips.platform.ths.consent;

import android.support.v4.app.Fragment;

import com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment;
import com.philips.platform.ths.intake.THSFollowUpFragment;
import com.philips.platform.uappframework.listener.BackEventListener;

/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

public class THSJustInTimeConsentFragment extends JustInTimeConsentFragment implements BackEventListener{
    /**
     * Check if UApp us handling back key event.
     *
     * @return true if uApp is handling back key event else return false.
     * @since 1.0.0
     */
    @Override
    public boolean handleBackEvent() {
        getActivity().getSupportFragmentManager().popBackStack(THSFollowUpFragment.TAG, 0);
        return true;
    }
}
