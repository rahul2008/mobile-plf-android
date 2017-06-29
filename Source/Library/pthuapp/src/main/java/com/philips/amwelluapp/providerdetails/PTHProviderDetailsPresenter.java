package com.philips.amwelluapp.providerdetails;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.amwelluapp.utility.PTHManager;

public class PTHProviderDetailsPresenter implements PTHProviderDetailsCallback{

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
}
