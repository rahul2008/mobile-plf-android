/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.insurance.THSInsuranceConfirmationFragment;
import com.philips.platform.ths.utility.THSManager;

public class THSPharmacyAndShippingPresenter implements THSBasePresenter {

    private THSPharmacyShippingViewInterface thsBaseView;

    public THSPharmacyAndShippingPresenter(THSPharmacyShippingViewInterface thsBaseView) {
        this.thsBaseView = thsBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ps_edit_pharmacy) {
            thsBaseView.startSearchPharmacy();
        }
        if (componentID == R.id.ps_edit_consumer_shipping_address) {
            thsBaseView.startEditShippingAddress();
        }
        if (componentID == R.id.ths_ps_continue_button) {
            Consumer consumer = THSManager.getInstance().getPTHConsumer(thsBaseView.getFragmentActivity()).getConsumer();
            if (consumer.getSubscription() != null && consumer.getSubscription().getHealthPlan() != null) {
                final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
                 thsBaseView.addFragment(fragment, THSCostSummaryFragment.TAG, null, true);
            } else {
                final THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
                thsBaseView.addFragment(fragment, THSInsuranceConfirmationFragment.TAG, null, true);
            }
        }
    }
}
