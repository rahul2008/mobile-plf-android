package com.philips.platform.ths.onboarding;

import android.util.Log;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.uappclasses.THSCompletionProtocol;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.net.URL;

import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;
import static com.philips.platform.ths.utility.THSConstants.THS_TERMS_AND_CONDITIONS;

/**
 * Created by philips on 10/25/17.
 */

public class OnBoardingPresenter implements THSBasePresenter {

    private final OnBoardingFragment onBoardingFragment;


    public OnBoardingPresenter(OnBoardingFragment onBoardingFragment) {
        this.onBoardingFragment = onBoardingFragment;
    }

    @Override
    public void onEvent(int componentID) {

        if(componentID==R.id.btn_continue){
            if(THSManager.getInstance().getOnBoradingABFlow().equalsIgnoreCase(THSConstants.THS_ONBOARDING_ABFLOW1) ){
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "agreesToTermsAndConditions");
            }else {
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "agreesToTermsAndConditions");
            }
            onBoardingFragment.popSelfBeforeTransition();
            if (THSManager.getInstance().isReturningUser()) {
                THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
                onBoardingFragment.addFragment(thsWelcomeFragment, THSWelcomeFragment.TAG, null, false);
            }else {
                THSRegistrationFragment thsRegistrationFragment = new THSRegistrationFragment();
                onBoardingFragment.addFragment(thsRegistrationFragment,THSRegistrationFragment.TAG,null, false);
            }
        }else if(componentID == R.id.breif_2){
            getTermsAndConditions();
        }
    }

    public void getTermsAndConditions(){
        if(THSManager.getInstance().getAppInfra() == null || THSManager.getInstance().getAppInfra().getServiceDiscovery() == null){
            Log.e(AmwellLog.LOG,"App Infra instance is null");
            onBoardingFragment.exitFromAmWell(THSCompletionProtocol.THSExitType.Other);
        }else {
            THSManager.getInstance().getAppInfra().getServiceDiscovery().getServiceUrlWithCountryPreference(THS_TERMS_AND_CONDITIONS, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

                @Override
                public void onError(ERRORVALUES errorvalues, String s) {
                    onBoardingFragment.showError("Service discovery failed - >" + s);
                    onBoardingFragment.hideProgressBar();
                }

                @Override
                public void onSuccess(URL url) {
                    onBoardingFragment.showTermsAndConditions(url.toString());
                }
            });
        }
    }
}
