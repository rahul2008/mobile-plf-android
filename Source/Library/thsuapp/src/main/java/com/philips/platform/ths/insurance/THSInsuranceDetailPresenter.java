package com.philips.platform.ths.insurance;

import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;

/**
 * Created by philips on 7/11/17.
 */

public class THSInsuranceDetailPresenter implements THSBasePresenter {
    THSBaseFragment mTHSBaseFragment;


    public THSInsuranceDetailPresenter(THSInsuranceDetailFragment tHSInsuranceDetailFraagment) {
        this.mTHSBaseFragment = tHSInsuranceDetailFraagment;

    }


    public THSHealthPlan fetchHealthPlanList() {
        THSHealthPlan tHSHealthPlan = new THSHealthPlan();
        List<HealthPlan> healthPlanList = THSManager.getInstance().getHealthPlans(mTHSBaseFragment.getFragmentActivity());
        tHSHealthPlan.setHealthPlanList(healthPlanList);
        return tHSHealthPlan;
    }

    @Override
    public void onEvent(int componentID) {

    }
}
