/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;

import com.americanwell.sdk.entity.provider.Provider;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;

public class THSWelcomeBackPresenter implements THSBasePresenter {
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
            bundle.putParcelable(THSConstants.THS_PROVIDER_INFO,thsProviderInfo);
            mThsWelcomeBackFragment.addFragment(new THSSymptomsFragment(),THSSymptomsFragment.TAG,bundle, true);
        }
    }
}
