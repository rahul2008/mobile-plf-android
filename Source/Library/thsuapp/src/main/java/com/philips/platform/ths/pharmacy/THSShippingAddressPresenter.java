/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.os.Bundle;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.insurance.THSInsuranceConfirmationFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

public class THSShippingAddressPresenter implements THSUpdateShippingAddressCallback {

    private THSBaseView thsBaseView;

    public THSShippingAddressPresenter(THSBaseView thsBaseView){
        this.thsBaseView = thsBaseView;
    }

    public void updateShippingAddress(Address address){
        try {
            THSManager.getInstance().updatePreferredShippingAddress(((THSShippingAddressFragment)thsBaseView).getConsumer(), address, this, thsBaseView.getFragmentActivity());
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddressValidationFailure(Map<String, ValidationReason> map) {
        ((THSBaseFragment)thsBaseView).showToast("Shipping Address validation failure");
    }

    @Override
    public void onUpdateSuccess(Address address, SDKError sdkErro) {
        //TODO: check this immediately
        Consumer consumer = THSManager.getInstance().getPTHConsumer().getConsumer();
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_CONSUMER,((THSShippingAddressFragment)thsBaseView).getConsumer());

        if (consumer.getSubscription() != null && consumer.getSubscription().getHealthPlan() != null) {
            final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
            thsBaseView.addFragment(fragment, THSCostSummaryFragment.TAG, bundle, true);
        } else {
            final THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
            thsBaseView.addFragment(fragment, THSInsuranceConfirmationFragment.TAG, bundle, true);
        }
        //((THSShippingAddressFragment) thsBaseView).showToast("Update Shipping address success");
    }

    @Override
    public void onUpdateFailure(Throwable throwable) {
        ((THSShippingAddressFragment) thsBaseView).showToast("Update Shipping address Failed");
    }
}
