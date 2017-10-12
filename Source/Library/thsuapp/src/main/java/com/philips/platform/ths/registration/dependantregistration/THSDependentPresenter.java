/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import android.os.Bundle;

import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.practice.THSPractice;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.registration.THSCheckConsumerExistsCallback;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.settings.THSScheduledVisitsFragment;
import com.philips.platform.ths.settings.THSVisitHistoryFragment;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

public class THSDependentPresenter implements THSBasePresenter {
    THSDependantHistoryFragment mThsDependantHistoryFragment;

    public THSDependentPresenter(THSDependantHistoryFragment thsDependantHistoryFragment) {
        mThsDependantHistoryFragment = thsDependantHistoryFragment;
    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void checkIfUserExists() {
        Bundle bundle = new Bundle();
        bundle.putInt(THSConstants.THS_LAUNCH_INPUT, mThsDependantHistoryFragment.mLaunchInput);

        if (THSManager.getInstance().getThsConsumer().getConsumer() != null && THSManager.getInstance().getThsConsumer().getConsumer().isEnrolled()) {

            switch (mThsDependantHistoryFragment.mLaunchInput) {
                case THSConstants.THS_SCHEDULED_VISITS:
                    mThsDependantHistoryFragment.addFragment(new THSScheduledVisitsFragment(), THSScheduledVisitsFragment.TAG, null, false);
                    break;
                case THSConstants.THS_VISITS_HISTORY:
                    mThsDependantHistoryFragment.addFragment(new THSVisitHistoryFragment(), THSScheduledVisitsFragment.TAG, null, false);
                    break;
                case THSConstants.THS_PRACTICES:
                    mThsDependantHistoryFragment.addFragment(new THSPracticeFragment(), THSPracticeFragment.TAG, null, false);
                    break;
            }
        } else {
            mThsDependantHistoryFragment.addFragment(new THSRegistrationFragment(), THSRegistrationFragment.TAG, bundle, false);
        }
    }
}
