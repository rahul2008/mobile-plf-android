/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_PHARMACY;


public class THSPharmacyListPresenter implements THSGetPharmaciesCallback, THSUpdatePharmacyCallback, THSBasePresenter {

    private Pharmacy pharmacy;
    private THSPharmacyListViewListener thsPharmacyListViewListener;

    public THSPharmacyListPresenter(THSPharmacyListViewListener thsPharmacyListViewListener) {
        this.thsPharmacyListViewListener = thsPharmacyListViewListener;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.switch_view_layout) {
            thsPharmacyListViewListener.switchView();
        }
        if (componentID == R.id.segment_control_view_one) {
            thsPharmacyListViewListener.showRetailView();
        }
        if (componentID == R.id.segment_control_view_two) {
            thsPharmacyListViewListener.showMailOrderView();

        }
        if (componentID == R.id.choose_pharmacy_button) {
            thsPharmacyListViewListener.setPreferredPharmacy();
        }
    }

    public void fetchPharmacyList(THSConsumerWrapper thsConsumerWrapper, String city, State state, String zipCode) {
        try {
            THSManager.getInstance().getPharmacies(thsPharmacyListViewListener.getFragmentActivity(), thsConsumerWrapper, city, state, zipCode, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void fetchPharmacyList(THSConsumerWrapper thsConsumerWrapper, float latitude, float longitude, int radius) {
        try {
            THSManager.getInstance().getPharmacies(thsPharmacyListViewListener.getFragmentActivity(), thsConsumerWrapper, latitude, longitude, radius, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> map) {
        if (null != thsPharmacyListViewListener && null != thsPharmacyListViewListener.getFragmentActivity()) {
            thsPharmacyListViewListener.hideProgressBar();
            thsPharmacyListViewListener.showErrorToast(thsPharmacyListViewListener.getFragmentActivity().getResources().getString(R.string.ths_pharmacy_fetch_validation_error));
        }

    }

    @Override
    public void onPharmacyListReceived(List<Pharmacy> pharmacies, SDKError sdkError) {
        if (null != thsPharmacyListViewListener && null != thsPharmacyListViewListener.getFragmentActivity()) {
            thsPharmacyListViewListener.hideProgressBar();
            if (null != sdkError) {
                thsPharmacyListViewListener.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_PHARMACY, sdkError));
            } else {
                if (null == pharmacies || pharmacies.size() == 0) {
                    thsPharmacyListViewListener.showErrorToast(thsPharmacyListViewListener.getFragmentActivity().getResources().getString(R.string.ths_pharmacy_fetch_error));
                }
                thsPharmacyListViewListener.updatePharmacyList(pharmacies);
                thsPharmacyListViewListener.updatePharmacyListView(pharmacies);
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        if (null != thsPharmacyListViewListener && null != thsPharmacyListViewListener.getFragmentActivity()) {
            thsPharmacyListViewListener.showErrorToast(thsPharmacyListViewListener.getFragmentActivity().getResources().getString(R.string.ths_se_server_error_toast_message));
            thsPharmacyListViewListener.hideProgressBar();
        }

    }

    public void updateConsumerPreferredPharmacy(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        try {
            THSManager.getInstance().updateConsumerPreferredPharmacy(thsPharmacyListViewListener.getFragmentActivity(), pharmacy, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateSuccess(SDKError sdkError) {
        if (null != thsPharmacyListViewListener && null != thsPharmacyListViewListener.getFragmentActivity()) {
            thsPharmacyListViewListener.hideProgressBar();
            thsPharmacyListViewListener.validateForMailOrder(pharmacy);
        }
    }

    @Override
    public void onUpdateFailure(Throwable throwable) {
        if (null != thsPharmacyListViewListener && null != thsPharmacyListViewListener.getFragmentActivity()) {
            thsPharmacyListViewListener.showErrorToast(thsPharmacyListViewListener.getFragmentActivity().getString(R.string.ths_se_server_error_toast_message));
            thsPharmacyListViewListener.hideProgressBar();
        }

    }
}
