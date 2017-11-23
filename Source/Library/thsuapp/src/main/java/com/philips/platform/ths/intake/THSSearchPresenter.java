/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.pharmacy.THSGetPharmaciesCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProvidersListCallback;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_PHARMACY;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_SEARCH_MEDICATION;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PROVIDER;


class THSSearchPresenter implements THSBasePresenter, THSSDKValidatedCallback<THSMedication, SDKError>, THSProvidersListCallback, THSGetPharmaciesCallback {

    private THSBaseFragment uiBaseView;

    public THSSearchPresenter(THSBaseFragment uiBaseView) {
        this.uiBaseView = uiBaseView;
    }


    //////////////// start of call backs for search medicines//////////////
    @Override
    public void onValidationFailure(Map<String, ValidationReason> var1) {

        AmwellLog.i("onFetchMedication", "failure");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(THSMedication THSMedication, SDKError sdkError) {
        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            if (null != sdkError) {
                uiBaseView.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_SEARCH_MEDICATION, sdkError));
            } else if (null != THSMedication && null != THSMedication.getMedicationList() && !THSMedication.getMedicationList().isEmpty()) {
                AmwellLog.i("onFetchMedication", "success");
                // if user deletes string to less than 3 character before response comes then response should not be shown
                if (((THSSearchFragment) uiBaseView).searchBox.getSearchTextView().getText().length() > 2) {
                    ((THSSearchFragment) uiBaseView).searchedMedicines = THSMedication;
                    ((THSSearchFragment) uiBaseView).mTHSSearchListAdapter.setData(((THSSearchFragment) uiBaseView).searchedMedicines.getMedicationList());
                }
            } else {
                AmwellLog.i("onFetchMedication", "failure");
                //((THSSearchFragment) uiBaseView).showToast("No match found");
            }
        }
    }

    @Override
    public void onFailure(Throwable throwable) {

        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            AmwellLog.i("onFetchMedication", "server failed to fetch");
            uiBaseView.showError(uiBaseView.getString(R.string.ths_se_server_error_toast_message));
        }
    }


    //////////////// end of call backs for search medicines//////////////


    @Override
    public void onEvent(int componentID) {

    }

    void searchProviders(String provider, Practice practice) {
        try {
            THSManager.getInstance().getProviderList(uiBaseView.getFragmentActivity(), practice, provider, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    void searchMedication(String medication) {
        try {
            THSManager.getInstance().searchMedication(uiBaseView.getFragmentActivity(), medication, this);

        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    void searchPharmacy(String pharmacyZip) {
        try {
            THSManager.getInstance().getPharmacies(uiBaseView.getFragmentActivity(), THSManager.getInstance().getPTHConsumer(uiBaseView.getContext()), null, null, pharmacyZip, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onProvidersListReceived(List<THSProviderInfo> providerInfoList, SDKError sdkError) {
        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            if (null != providerInfoList && !providerInfoList.isEmpty()) {

                AmwellLog.i("onFetchMedication", "success");
                // if user deletes string to less than 3 character before response comes then response should not be shown
                if (((THSSearchFragment) uiBaseView).searchBox.getSearchTextView().getText().length() > 2) {
                    ((THSSearchFragment) uiBaseView).providerInfoList = providerInfoList;
                    ((THSSearchFragment) uiBaseView).mTHSSearchListAdapter.setData(((THSSearchFragment) uiBaseView).providerInfoList);
                }
            } else if (null != sdkError) {
                AmwellLog.i("onFetchMedication", "failure");
                uiBaseView.showError(THSSDKErrorFactory.getErrorType(ANALYTIC_FETCH_PROVIDER, sdkError));

            }
        }
    }

    @Override
    public void onProvidersListFetchError(Throwable throwable) {
        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            AmwellLog.i("onFetchProvider", "failure");
            uiBaseView.showError(uiBaseView.getString(R.string.ths_se_server_error_toast_message));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onPharmacyListReceived(List<Pharmacy> pharmacies, SDKError sdkError) {
        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            if (null != sdkError) {
                uiBaseView.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_PHARMACY, sdkError));
            } else {
                if (null != pharmacies && !pharmacies.isEmpty()) {
                    if (((THSSearchFragment) uiBaseView).searchBox.getSearchTextView().getText().length() > 2) {
                        ((THSSearchFragment) uiBaseView).pharmacyList = pharmacies;
                        ((THSSearchFragment) uiBaseView).mTHSSearchListAdapter.setData(((THSSearchFragment) uiBaseView).pharmacyList);
                        ((THSSearchFragment) uiBaseView).callPharmacyListFragment();
                    }
                }
            }
        }
    }
}
