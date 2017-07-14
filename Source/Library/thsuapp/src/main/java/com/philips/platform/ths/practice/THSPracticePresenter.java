package com.philips.platform.ths.practice;

import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.providerslist.THSProvidersListFragment;
import com.philips.platform.ths.utility.THSManager;


public class THSPracticePresenter implements THSBasePresenter, THSPracticesListCallback {

    THSBaseView uiBaseView;



     THSPracticePresenter(THSPracticeFragment tHSPracticeFragment){
        this.uiBaseView = tHSPracticeFragment;

    }

    @Override
    public void onEvent(int componentID) {

    }

    protected void fetchPractices(){
        try {
            THSManager.getInstance().getPractices(uiBaseView.getFragmentActivity(),THSManager.getInstance().getPTHConsumer().getConsumer(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPracticesListReceived(THSPractice practices, SDKError sdkError) {
        ((THSPracticeFragment)uiBaseView).showPracticeList(practices);

    }

    @Override
    public void onPracticesListFetchError(Throwable throwable) {
    }

    void showProviderList(Practice practice){
        THSProvidersListFragment providerListFragment = new THSProvidersListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("practice",practice);


       // providerListFragment.setPracticeAndConsumer(practice,mConsumer);
        ((THSPracticeFragment)uiBaseView).addFragment(providerListFragment,THSProvidersListFragment.TAG,bundle);
       /* providerListFragment.setActionBarListener(getActionBarListener());

         getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), providerListFragment,"ProviderListFragment").addToBackStack(null).commit();
*/
    }
}
