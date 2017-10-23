/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.practice;

import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.providerslist.THSProvidersListFragment;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;


public class THSPracticePresenter implements THSBasePresenter, THSPracticesListCallback {

    private THSBaseView uiBaseView;



     THSPracticePresenter(THSPracticeFragment tHSPracticeFragment){
        this.uiBaseView = tHSPracticeFragment;

    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void fetchPractices(){
        try {
            THSManager.getInstance().getPractices(uiBaseView.getFragmentActivity(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPracticesListReceived(THSPracticeList practices, SDKError sdkError) {
        if(null!=uiBaseView && null!=uiBaseView.getFragmentActivity()) {
            ((THSPracticeFragment) uiBaseView).showPracticeList(practices);
        }

    }

    @Override
    public void onPracticesListFetchError(Throwable throwable) {
        if(null!=uiBaseView && null!=uiBaseView.getFragmentActivity()) {
            ((THSPracticeFragment) uiBaseView).showErrorToast();
        }
    }

    void showProviderList(Practice practice){
        THSProvidersListFragment providerListFragment = new THSProvidersListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.PRACTICE_FRAGMENT,practice);


       // providerListFragment.setPracticeAndConsumer(practice,mConsumer);
        providerListFragment.setFragmentLauncher(((THSBaseFragment)uiBaseView).getFragmentLauncher());
        ((THSPracticeFragment)uiBaseView).addFragment(providerListFragment,THSProvidersListFragment.TAG,bundle, false);
       /* providerListFragment.setActionBarListener(getActionBarListener());

         getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), providerListFragment,"ProviderListFragment").addToBackStack(null).commit();
*/
    }
}
