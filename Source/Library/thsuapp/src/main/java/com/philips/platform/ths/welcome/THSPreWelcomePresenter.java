/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.utility.THSManager;

public class THSPreWelcomePresenter implements THSBasePresenter{
    THSPreWelcomeFragment mThsPreWelcomeFragment;

    THSPreWelcomePresenter(THSPreWelcomeFragment thsPreWelcomeScreen){
        mThsPreWelcomeFragment = thsPreWelcomeScreen;
    }
    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_go_see_provider){
            if (THSManager.getInstance().isReturningUser()) {
                THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
                mThsPreWelcomeFragment.addFragment(thsWelcomeFragment, THSWelcomeFragment.TAG, null);
            }else {
                THSRegistrationFragment thsRegistrationFragment = new THSRegistrationFragment();
                mThsPreWelcomeFragment.addFragment(thsRegistrationFragment,THSRegistrationFragment.TAG,null);
            }
        }
    }
}
