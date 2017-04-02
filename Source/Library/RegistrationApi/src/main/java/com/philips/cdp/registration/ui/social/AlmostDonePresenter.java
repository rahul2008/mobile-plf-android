package com.philips.cdp.registration.ui.social;


import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;

public class AlmostDonePresenter implements NetworStateListener,EventListener {

    private final AlmostDoneContract almostDoneContract;

    private boolean isNetworkAvailable;

    public AlmostDonePresenter(AlmostDoneContract almostDoneContract) {
        URInterface.getComponent().inject(this);
        this.almostDoneContract = almostDoneContract;
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        EventHelper.getInstance().registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
    }


   public void cleanUp(){
       RegistrationHelper.getInstance().unRegisterNetworkListener(this);
       EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
               this);
   }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        isNetworkAvailable = isOnline;
        RLog.i(RLog.NETWORK_STATE, "AlmostDone :onNetWorkStateReceived state :" + isOnline);
        almostDoneContract.updateUiStatus();
    }

    public boolean isNetworkAvailable(){
        return isNetworkAvailable;
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "AlmostDoneFragment :onEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            almostDoneContract.updateUiStatus();
        }
    }

    public void handleAcceptTermsAndReceiveMarketingOpt(){
        if (RegistrationConfiguration.getInstance().isTermsAndConditionsAcceptanceRequired()) {
            almostDoneContract.handleTermsAndCondition();
        }else{
            almostDoneContract.hideAcceptTermsView();
        }
        if(almostDoneContract.isTermsAndConditionAccepted()){
            almostDoneContract.updateTermsAndConditionView();
        }else if(almostDoneContract.isReceiveMarketingEmail()){
            almostDoneContract.updateReceiveMarktingView();
        }
    }
}
