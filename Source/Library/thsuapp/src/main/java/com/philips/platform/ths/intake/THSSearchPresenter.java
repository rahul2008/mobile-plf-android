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
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.pharmacy.THSGetPharmaciesCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProvidersListCallback;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;


class THSSearchPresenter implements THSBasePresenter, THSSDKValidatedCallback<THSMedication, SDKError>, THSProvidersListCallback,THSGetPharmaciesCallback {

    private THSBaseView uiBaseView;


    THSSearchPresenter(THSBaseView uiBaseView) {
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

        if (null != THSMedication && null != THSMedication.getMedicationList() && !THSMedication.getMedicationList().isEmpty()) {

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

    @Override
    public void onFailure(Throwable throwable) {


        AmwellLog.i("onFetchMedication", "failure");
        ((THSMedicationFragment) uiBaseView).showToast("Search failure");
    }


    //////////////// end of call backs for search medicines//////////////


    @Override
    public void onEvent(int componentID) {

    }

    void searchProviders(String provider, Practice practice) {
        try {
            THSManager.getInstance().getProviderList(uiBaseView.getFragmentActivity(), THSManager.getInstance().getPTHConsumer().getConsumer(), practice, provider, this);
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

    void searchPharmacy(String pharmacyZip){
        try {
            THSManager.getInstance().getPharmacies(uiBaseView.getFragmentActivity(),THSManager.getInstance().getPTHConsumer(),null,null,pharmacyZip,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onProvidersListReceived(List<THSProviderInfo> providerInfoList, SDKError sdkError) {
        if (null != providerInfoList && !providerInfoList.isEmpty()) {

            AmwellLog.i("onFetchMedication", "success");
            // if user deletes string to less than 3 character before response comes then response should not be shown
            if (((THSSearchFragment) uiBaseView).searchBox.getSearchTextView().getText().length() > 2) {
                ((THSSearchFragment) uiBaseView).providerInfoList = providerInfoList;
                ((THSSearchFragment) uiBaseView).mTHSSearchListAdapter.setData(((THSSearchFragment) uiBaseView).providerInfoList);
            }
        } else {

            AmwellLog.i("onFetchMedication", "failure");
            //((THSSearchFragment) uiBaseView).showToast("No match found");
        }
    }

    @Override
    public void onProvidersListFetchError(Throwable throwable) {
        AmwellLog.i("onFetchProvider", "failure");
        ((THSBaseFragment) uiBaseView).showToast("Search failure");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onPharmacyListReceived(List<Pharmacy> pharmacies, SDKError sdkError) {
        if(null != pharmacies && !pharmacies.isEmpty()){
            if (((THSSearchFragment) uiBaseView).searchBox.getSearchTextView().getText().length() > 2) {
                ((THSSearchFragment) uiBaseView).pharmacyList = pharmacies;
                ((THSSearchFragment) uiBaseView).mTHSSearchListAdapter.setData(((THSSearchFragment) uiBaseView).pharmacyList);
                ((THSSearchFragment) uiBaseView).callPharmacyListFragment();
            }
        }
    }
}
