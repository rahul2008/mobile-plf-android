/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;

import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.practice.THSPractice;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
import com.philips.platform.ths.providerslist.THSOnDemandSpecialtyCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;

public class THSWelcomeBackPresenter implements THSBasePresenter, THSOnDemandSpecialtyCallback<List<THSOnDemandSpeciality>,THSSDKError> {
    THSWelcomeBackFragment mThsWelcomeBackFragment;

    public THSWelcomeBackPresenter(THSWelcomeBackFragment thsWelcomeBackFragment) {
        mThsWelcomeBackFragment = thsWelcomeBackFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_get_started){
            final Provider provider = mThsWelcomeBackFragment.getProvider();

            THSProviderInfo thsProviderInfo = new THSProviderInfo();
            thsProviderInfo.setTHSProviderInfo(provider);

            Bundle bundle = new Bundle();
            bundle.putParcelable(THSConstants.THS_PROVIDER,thsProviderInfo);
            mThsWelcomeBackFragment.addFragment(new THSSymptomsFragment(),THSSymptomsFragment.TAG,bundle);
        }
    }

    /*private void getOnDemandProvider(PracticeInfo practiceInfo) {
        try {
            THSManager.getInstance().getOnDemandSpecialities(mThsWelcomeBackFragment.getContext(),
                    practiceInfo, null, this);
        } catch (AWSDKInstantiationException e) {


        }
    }*/

    @Override
    public void onResponse(List<THSOnDemandSpeciality> onDemandSpecialties, THSSDKError sdkError) {
        if(onDemandSpecialties == null || onDemandSpecialties.size()==0){
            mThsWelcomeBackFragment.showToast("No OnDemandSpecialities available at present, please try after some time");
            mThsWelcomeBackFragment.hideProgressBar();
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_ON_DEMAND,onDemandSpecialties.get(0));
        final THSSymptomsFragment fragment = new THSSymptomsFragment();
        fragment.setFragmentLauncher(mThsWelcomeBackFragment.getFragmentLauncher());
        mThsWelcomeBackFragment.addFragment(fragment,THSSymptomsFragment.TAG,bundle);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
