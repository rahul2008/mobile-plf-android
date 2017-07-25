package com.philips.platform.ths.insurance;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;

/**
 * Created by philips on 7/10/17.
 */

public class THSInsuranceConfirmationPresenter implements THSBasePresenter {
    THSBaseFragment mTHSBaseFragment;


    public THSInsuranceConfirmationPresenter(THSInsuranceConfirmationFragment tHSInsuranceConfirmationFragment) {
        this.mTHSBaseFragment = tHSInsuranceConfirmationFragment;
    }


    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.pth_insurance_confirmation_radio_option_yes) {
            mTHSBaseFragment.addFragment(new THSInsuranceDetailFragment(), THSInsuranceDetailFragment.TAG,null);

        } else if (componentID == R.id.pth_insurance_confirmation_radio_option_no) {
            mTHSBaseFragment.addFragment(new THSCostSummaryFragment(), THSCostSummaryFragment.TAG,null);
        }

    }


}
