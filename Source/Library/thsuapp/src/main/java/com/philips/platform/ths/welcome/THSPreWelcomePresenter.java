/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.faqs.THSFaqFragment;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSSharedPreferenceUtility;

import java.net.URL;

import static com.philips.platform.ths.utility.THSConstants.THS_IS_TERMS_AND_CONDITIONS_LAUNCHED;
import static com.philips.platform.ths.utility.THSConstants.THS_TERMS_AND_CONDITIONS;

public class THSPreWelcomePresenter implements THSBasePresenter{
    THSPreWelcomeFragment mThsPreWelcomeFragment;

    THSPreWelcomePresenter(THSPreWelcomeFragment thsPreWelcomeScreen){
        mThsPreWelcomeFragment = thsPreWelcomeScreen;
    }
    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_go_see_provider){
            mThsPreWelcomeFragment.popSelfBeforeTransition();
            if (THSManager.getInstance().isReturningUser()) {
                THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
                mThsPreWelcomeFragment.addFragment(thsWelcomeFragment, THSWelcomeFragment.TAG, null, false);
            }else {
                THSRegistrationFragment thsRegistrationFragment = new THSRegistrationFragment();
                mThsPreWelcomeFragment.addFragment(thsRegistrationFragment,THSRegistrationFragment.TAG,null, false);
            }
        }else if(componentID == R.id.ths_video_consults){
            THSFaqFragment thsFaqFragment = new THSFaqFragment();
            mThsPreWelcomeFragment.addFragment(thsFaqFragment,THSFaqFragment.TAG,null,false);
        }else if(componentID == R.id.ths_licence){
            getTermsAndConditions();
        }
    }



    public void getTermsAndConditions(){
        THSManager.getInstance().getAppInfra().getServiceDiscovery().getServiceUrlWithCountryPreference(THS_TERMS_AND_CONDITIONS, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                mThsPreWelcomeFragment.showError("Service discovery failed - >" + s);
                mThsPreWelcomeFragment.hideProgressBar();
            }

            @Override
            public void onSuccess(URL url) {
                mThsPreWelcomeFragment.showTermsAndConditions(url.toString());
            }
        });
    }
}
