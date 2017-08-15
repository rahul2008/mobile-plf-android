/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import android.os.Bundle;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;

import static com.philips.platform.ths.utility.THSConstants.IS_LAUNCHED_FROM_COST_SUMMARY;

public class THSInsuranceConfirmationPresenter implements THSBasePresenter {
    private THSInsuranceConfirmationFragment mTHSInsuranceConfirmationFragment;


    public THSInsuranceConfirmationPresenter(THSInsuranceConfirmationFragment tHSInsuranceConfirmationFragment) {
        this.mTHSInsuranceConfirmationFragment = tHSInsuranceConfirmationFragment;
    }


    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.pth_insurance_confirmation_radio_option_yes) {
            THSInsuranceDetailFragment fragment = new THSInsuranceDetailFragment();
            Bundle bundle = null;
            if (mTHSInsuranceConfirmationFragment.isLaunchedFromCostSummary) {
                bundle = new Bundle();
                bundle.putBoolean(IS_LAUNCHED_FROM_COST_SUMMARY, true);
            }
            mTHSInsuranceConfirmationFragment.addFragment(fragment, THSInsuranceDetailFragment.TAG, bundle);

        } else if (componentID == R.id.pth_insurance_confirmation_radio_option_no) {
            if (mTHSInsuranceConfirmationFragment.isLaunchedFromCostSummary) {
                mTHSInsuranceConfirmationFragment.getActivity().getSupportFragmentManager().popBackStack(THSCostSummaryFragment.TAG, 0);
            } else {

                final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
                fragment.setFragmentLauncher(mTHSInsuranceConfirmationFragment.getFragmentLauncher());
                mTHSInsuranceConfirmationFragment.addFragment(fragment, THSCostSummaryFragment.TAG, null);
            }
        }

    }


}
