package com.philips.platform.ths.providerdetails;

import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.appointment.THSDatePickerFragment;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

public class THSProviderDetailsPresenter implements THSBasePresenter,THSProviderDetailsCallback {

    THSPRoviderDetailsViewInterface viewInterface;

    public THSProviderDetailsPresenter(THSPRoviderDetailsViewInterface viewInterface){
        this.viewInterface = viewInterface;
    }

    public void fetchProviderDetails(){
        try {
            if (viewInterface.getProviderInfo() != null)
                getPTHManager().getProviderDetails(viewInterface.getContext(), viewInterface.getConsumerInfo(), viewInterface.getProviderInfo(), this);
            else
                viewInterface.dismissRefreshLayout();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    protected THSManager getPTHManager() {
        return THSManager.getInstance();
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
            THSConsumer THSConsumer = new THSConsumer();
            THSConsumer.setConsumer(viewInterface.getConsumerInfo());

            THSProviderInfo THSProviderInfo = new THSProviderInfo();
            THSProviderInfo.setProviderInfo(viewInterface.getProviderInfo());

            Bundle bundle = new Bundle();
            bundle.putParcelable(THSConstants.THS_PROVIDER_INFO, THSProviderInfo);

            ((THSBaseView)viewInterface).addFragment(new THSSymptomsFragment(), THSSymptomsFragment.TAG,bundle);
        }else if(componentID == R.id.detailsButtonTwo){
            Bundle bundle = new Bundle();
            bundle.putParcelable(THSConstants.THS_PRACTICE_INFO ,viewInterface.getPracticeInfo());
            ((THSBaseView)viewInterface).addFragment(new THSDatePickerFragment(), THSDatePickerFragment.TAG,bundle);
        }
    }
}
