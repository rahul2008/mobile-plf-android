package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.pharmacy.THSConsumerShippingAddressCallback;
import com.philips.platform.ths.pharmacy.THSPreferredPharmacyCallback;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;

/**
 * Created by philips on 7/4/17.
 */

public class THSFollowUpPresenter implements THSBasePresenter, THSUpdateConsumerCallback<THSConsumer, THSSDKPasswordError>
        , THSPreferredPharmacyCallback, THSConsumerShippingAddressCallback {
    THSBaseView uiBaseView;
    Pharmacy pharmacy;

    public THSFollowUpPresenter(THSFollowUpFragment tHSFollowUpFragment) {
        this.uiBaseView = tHSFollowUpFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.pth_intake_follow_up_continue_button) {

            if (null != ((THSFollowUpFragment) uiBaseView).mPhoneNumberEditText.getText() && !((THSFollowUpFragment) uiBaseView).mPhoneNumberEditText.getText().toString().isEmpty()) {
                acceptLegalText();
                updateConsumer(((THSFollowUpFragment) uiBaseView).mPhoneNumberEditText.getText().toString().trim());
            }
            // uiBaseView.addFragment(new THSInsuranceConfirmationFragment(), THSInsuranceConfirmationFragment.TAG, null);
        } else if (componentID == R.id.pth_intake_follow_up_i_agree_link_text) {

            final THSNoppFragment fragment = new THSNoppFragment();
            fragment.setFragmentLauncher(((THSBaseFragment)uiBaseView).getFragmentLauncher());
            uiBaseView.addFragment(fragment, THSNoppFragment.TAG, null);
        }else {
            ((THSBaseFragment)uiBaseView).showToast("Please Enter a valid Phone Number");
        }
    }

    private void acceptLegalText() {
        List<LegalText> legalTextList = THSManager.getInstance().getPthVisitContext().getLegalTexts();
        for (LegalText legalText : legalTextList) {
            legalText.setAccepted(true);
        }
    }


    protected void updateConsumer(String updatedPhoner) {


        try {
            THSManager.getInstance().updateConsumer(uiBaseView.getFragmentActivity(), updatedPhoner, this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    public void fetchConsumerPreferredPharmacy(THSConsumer thsConsumer) {
        try {
            THSManager.getInstance().getConsumerPreferredPharmacy(uiBaseView.getFragmentActivity(), thsConsumer, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void getConsumerShippingAddress(THSConsumer thsConsumer) {
        try {
            THSManager.getInstance().getConsumerShippingAddress(uiBaseView.getFragmentActivity(), thsConsumer, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateConsumerValidationFailure(Map<String, ValidationReason> var1) {

    }

    @Override
    public void onUpdateConsumerResponse(THSConsumer thsConsumer, THSSDKPasswordError sdkPasswordError) {
        //update signleton THSManager THSConsumer member
        THSManager.getInstance().setPTHConsumer(thsConsumer);
        fetchConsumerPreferredPharmacy(thsConsumer);
    }

    @Override
    public void onUpdateConsumerFailure(Throwable var1) {

    }

    @Override
    public void onPharmacyReceived(Pharmacy pharmacy, SDKError sdkError) {
        if (null != pharmacy) {
            this.pharmacy = pharmacy;
            getConsumerShippingAddress(THSManager.getInstance().getPTHConsumer());
            //((THSFollowUpFragment) uiBaseView).displaySearchPharmacy();
        } else {
            ((THSFollowUpFragment) uiBaseView).displaySearchPharmacy();
        }
    }

    @Override
    public void onSuccessfulFetch(Address address, SDKError sdkError) {
        if (null != address) {
            ((THSFollowUpFragment) uiBaseView).displayPharmacyAndShippingPreferenceFragment(pharmacy, address);
        } else {
            ((THSFollowUpFragment) uiBaseView).displaySearchPharmacy();
        }
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
