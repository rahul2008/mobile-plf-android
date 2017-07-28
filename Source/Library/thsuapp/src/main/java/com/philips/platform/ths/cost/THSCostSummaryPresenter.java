package com.philips.platform.ths.cost;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.payment.THSCreditCardDetailFragment;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

/**
 * Created by philips on 7/19/17.
 */

public class THSCostSummaryPresenter implements THSBasePresenter, THSSDKValidatedCallback<THSVisit, SDKError> {

    THSBaseFragment mTHSBaseFragment;

    public THSCostSummaryPresenter(THSCostSummaryFragment thsCostSummaryFragment) {
        mTHSBaseFragment = thsCostSummaryFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID== R.id.ths_cost_summary_continue_button){
            // calling payment detail
            final THSCreditCardDetailFragment fragment = new THSCreditCardDetailFragment();
            fragment.setFragmentLauncher(mTHSBaseFragment.getFragmentLauncher());
            ((THSCostSummaryFragment) mTHSBaseFragment).addFragment(fragment,THSCreditCardDetailFragment.TAG,null);
        }

    }


    void createVisit() {
        try {
            THSManager.getInstance().createVisit(mTHSBaseFragment.getFragmentActivity(), THSManager.getInstance().getPthVisitContext(), this);
        } catch (Exception e) {

        }

    }


    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {

    }

    @Override
    public void onResponse(THSVisit thsVisit, SDKError sdkError) {
        ((THSCostSummaryFragment) mTHSBaseFragment).hideProgressBar();
        if (null != thsVisit) {
            THSManager.getInstance().setTHSVisit(thsVisit);
           double costDouble= thsVisit.getVisit().getVisitCost().getExpectedConsumerCopayCost();
            String costString = String.valueOf(costDouble);
            String[] costStringArray = costString.split("\\.");// seperate the decimal value
            ((THSCostSummaryFragment) mTHSBaseFragment).costBigLabel.setText("$"+costStringArray[0]);
            if(!"0".equals(costStringArray[1])) { // if decimal part is zero then dont show
                ((THSCostSummaryFragment) mTHSBaseFragment).costSmallLabel.setText("."+costStringArray[1]);
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        ((THSCostSummaryFragment) mTHSBaseFragment).hideProgressBar();
    }
}
