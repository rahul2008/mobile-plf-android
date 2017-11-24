/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.insurance.THSInsuranceConfirmationFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;
import java.util.regex.Pattern;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_SHIPPING_ADDRESS;

public class THSShippingAddressPresenter implements THSUpdateShippingAddressCallback {

    private THSBaseView thsBaseView;

    public THSShippingAddressPresenter(THSBaseView thsBaseView){
        this.thsBaseView = thsBaseView;
    }
    String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
    Pattern pattern = Pattern.compile(regex);

    public void updateShippingAddress(Address address){
        try {
            THSManager.getInstance().updatePreferredShippingAddress(thsBaseView.getFragmentActivity(),address,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddressValidationFailure(Map<String, ValidationReason> map) {
        ((THSBaseFragment)thsBaseView).doTagging(ANALYTICS_UPDATE_SHIPPING_ADDRESS,THSConstants.THS_GENERIC_USER_ERROR,false);
        ((THSBaseFragment)thsBaseView).showError(null);
    }

    public boolean validateZip(String zipCode){
        return pattern.matcher(zipCode).matches();

    }
    @Override
    public void onUpdateSuccess(Address address, SDKError sdkErro) {
        //TODO: check this immediately
        if(null!=thsBaseView && null!=thsBaseView.getFragmentActivity()) {
            Consumer consumer = THSManager.getInstance().getPTHConsumer(thsBaseView.getFragmentActivity()).getConsumer();
            if (consumer.getSubscription() != null && consumer.getSubscription().getHealthPlan() != null) {
                final THSCostSummaryFragment fragment = new THSCostSummaryFragment();
                thsBaseView.addFragment(fragment, THSCostSummaryFragment.TAG, null, true);
            } else {
                final THSInsuranceConfirmationFragment fragment = new THSInsuranceConfirmationFragment();
                thsBaseView.addFragment(fragment, THSInsuranceConfirmationFragment.TAG, null, true);
            }
            //((THSShippingAddressFragment) thsBaseView).showToast("Update Shipping address success");
        }
    }

    @Override
    public void onUpdateFailure(Throwable throwable) {
        if(null!=thsBaseView && null!=thsBaseView.getFragmentActivity()) {
            ((THSShippingAddressFragment) thsBaseView).showError(((THSShippingAddressFragment) thsBaseView).getString(R.string.ths_se_server_error_toast_message));
        }
    }
}
