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
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.pharmacy.THSGetPharmaciesCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProvidersListCallback;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import java.util.List;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_PHARMACY;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_SEARCH_MEDICATION;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_SEARCH_PROVIDER;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SERVER_ERROR;


class THSSearchPresenter implements THSBasePresenter, THSSDKValidatedCallback<THSMedication, SDKError>, THSProvidersListCallback, THSGetPharmaciesCallback {

    private THSBaseFragment uiBaseView;

    public THSSearchPresenter(THSBaseFragment uiBaseView) {
        this.uiBaseView = uiBaseView;
    }


    //////////////// start of call backs for search medicines//////////////
    @Override
    public void onValidationFailure(Map<String, String> var1) {

        AmwellLog.i("onFetchMedication", "failure");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(THSMedication THSMedication, SDKError sdkError) {
        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            if (null != sdkError) {
                uiBaseView.showError(THSSDKErrorFactory.getErrorType(uiBaseView.getFragmentActivity(), ANALYTICS_SEARCH_MEDICATION, sdkError));
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

        }
    }

    void searchMedication(String medication) {
        try {
            THSManager.getInstance().searchMedication(uiBaseView.getFragmentActivity(), medication, this);

        } catch (AWSDKInstantiationException e) {

        }
    }

    void searchPharmacy(String pharmacyZip) {
        try {
            THSManager.getInstance().getPharmacies(uiBaseView.getFragmentActivity(), THSManager.getInstance().getPTHConsumer(uiBaseView.getContext()), null, null, pharmacyZip, this);
        } catch (AWSDKInstantiationException e) {

        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onProvidersListReceived(List<THSProviderInfo> providerInfoList, SDKError sdkError) {
        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            if (null != providerInfoList && !providerInfoList.isEmpty()) {

                AmwellLog.i("onSearchProv", "success");
                // if user deletes string to less than 3 character before response comes then response should not be shown
                if (((THSSearchFragment) uiBaseView).searchBox.getSearchTextView().getText().length() > 2) {
                    ((THSSearchFragment) uiBaseView).providerInfoList = providerInfoList;
                    ((THSSearchFragment) uiBaseView).mTHSSearchListAdapter.setData(((THSSearchFragment) uiBaseView).providerInfoList);
                }
            } else if (null != sdkError) {
                AmwellLog.i("onSearchProv", "onProvidersListReceived failure");
                uiBaseView.showError(THSSDKErrorFactory.getErrorType(uiBaseView.getFragmentActivity(), ANALYTICS_SEARCH_PROVIDER, sdkError));

            }
        }
    }

    @Override
    public void onProvidersListFetchError(Throwable throwable) {
        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            AmwellLog.i("onSearchProv", "onProvidersListFetchError failure");
            String errMessage= throwable!=null?throwable.getMessage():uiBaseView.getString(R.string.ths_se_server_error_toast_message);
            final String errorTag=THSTagUtils.createErrorTag(ANALYTICS_SEARCH_PROVIDER,errMessage);
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SERVER_ERROR,errorTag);
            uiBaseView.showError(uiBaseView.getString(R.string.ths_se_server_error_toast_message));
        }
    }



    @SuppressWarnings("unchecked")
    @Override
    public void onPharmacyListReceived(List<Pharmacy> pharmacies, SDKError sdkError) {
        if (null != uiBaseView && uiBaseView.isFragmentAttached()) {
            if (null != sdkError) {
                uiBaseView.showError(THSSDKErrorFactory.getErrorType(uiBaseView.getFragmentActivity(), ANALYTICS_PHARMACY, sdkError));
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
