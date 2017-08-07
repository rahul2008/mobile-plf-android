/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.insurance;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;

public class THSInsuranceConfirmationPresenter implements THSBasePresenter {
    private THSBaseFragment mTHSBaseFragment;


    public THSInsuranceConfirmationPresenter(THSInsuranceConfirmationFragment tHSInsuranceConfirmationFragment) {
        this.mTHSBaseFragment = tHSInsuranceConfirmationFragment;
    }


    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.pth_insurance_confirmation_radio_option_yes) {
            final THSInsuranceDetailFragment fragment = new THSInsuranceDetailFragment();
            fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            mTHSBaseFragment.addFragment(fragment, THSInsuranceDetailFragment.TAG, null);

        } else if (componentID == R.id.pth_insurance_confirmation_radio_option_no) {
            final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
            fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            mTHSBaseFragment.addFragment(fragment, THSCostSummaryFragment.TAG, null);
        }

    }


}
