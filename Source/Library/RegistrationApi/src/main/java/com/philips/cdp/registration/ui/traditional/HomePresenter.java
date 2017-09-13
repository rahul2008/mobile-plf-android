package com.philips.cdp.registration.ui.traditional;


import com.philips.cdp.registration.events.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;

import javax.inject.*;

public class HomePresenter implements NetworkStateListener {


    @Inject
    NetworkUtility networkUtility;

    private HomeContract homeContract;

    public HomePresenter(HomeContract homeContract) {
       URInterface.getComponent().inject(this);
        this.homeContract = homeContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
    }

    public void cleanUp() {
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if(isOnline){
            homeContract.enableControlsOnNetworkConnectionArraival();
        }else {
            homeContract.disableControlsOnNetworkConnectionGone();
        }
    }



    public void updateHomeControls() {
        if (networkUtility.isNetworkAvailable()) {
            homeContract.enableControlsOnNetworkConnectionArraival();
        } else {
            homeContract.disableControlsOnNetworkConnectionGone();
        }
    }
}
