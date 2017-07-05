package com.philips.amwelluapp.providerdetails;

import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBasePresenter;
import com.philips.amwelluapp.base.PTHBaseView;
import com.philips.amwelluapp.intake.PTHSymptomsFragment;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.utility.PTHConstants;
import com.philips.amwelluapp.utility.PTHManager;

public class PTHProviderDetailsPresenter implements PTHBasePresenter,PTHProviderDetailsCallback{

    PTHPRoviderDetailsViewInterface viewInterface;

    public PTHProviderDetailsPresenter(PTHPRoviderDetailsViewInterface viewInterface){
        this.viewInterface = viewInterface;
    }

    public void fetchProviderDetails(){
        try {
            PTHManager.getInstance().getProviderDetails(viewInterface.getContext(), viewInterface.getConsumerInfo(), viewInterface.getProviderInfo(),this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
        viewInterface.updateView(provider);
    }

    @Override
    public void onProviderDetailsFetchError(Throwable throwable) {

    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.detailsButtonOne) {
            PTHConsumer pthConsumer = new PTHConsumer();
            pthConsumer.setConsumer(viewInterface.getConsumerInfo());

            PTHProviderInfo pthProviderInfo = new PTHProviderInfo();
            pthProviderInfo.setProviderInfo(viewInterface.getProviderInfo());

            Bundle bundle = new Bundle();
            bundle.putParcelable(PTHConstants.THS_CONSUMER,pthConsumer);
            bundle.putParcelable(PTHConstants.THS_PROVIDER_INFO,pthProviderInfo);

            ((PTHBaseView)viewInterface).addFragment(new PTHSymptomsFragment(),PTHSymptomsFragment.TAG,bundle);
        }
    }
}
