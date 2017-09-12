/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;

public class THSPreWelcomePresenter implements THSBasePresenter{
    THSPreWelcomeFragment mThsPreWelcomeFragment;

    THSPreWelcomePresenter(THSPreWelcomeFragment thsPreWelcomeScreen){
        mThsPreWelcomeFragment = thsPreWelcomeScreen;
    }
    @Override
    public void onEvent(int componentID) {
        if(componentID == R.id.ths_go_see_provider){
            THSWelcomeFragment thsWelcomeFragment = new THSWelcomeFragment();
            mThsPreWelcomeFragment.addFragment(thsWelcomeFragment,THSWelcomeFragment.TAG,null);
        }
    }
}
