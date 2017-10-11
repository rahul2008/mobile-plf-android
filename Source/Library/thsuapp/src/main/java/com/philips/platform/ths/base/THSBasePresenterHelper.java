/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.base;

import android.os.Bundle;

import com.americanwell.sdk.entity.practice.Practice;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.utility.THSConstants;

import java.util.Date;

public class THSBasePresenterHelper {

    public void launchAvailableProviderDetailFragment(THSBaseFragment thsBaseFragment,THSProviderEntity thsProviderInfo, Date date, Practice practice) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY,thsProviderInfo);
        bundle.putSerializable(THSConstants.THS_DATE,date);
        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,practice);
        THSAvailableProviderDetailFragment fragment = new THSAvailableProviderDetailFragment();
        fragment.setFragmentLauncher(thsBaseFragment.getFragmentLauncher());
        fragment.setTHSProviderEntity(thsProviderInfo);
        thsBaseFragment.addFragment(fragment, THSAvailableProviderDetailFragment.TAG,bundle, true);
    }
}
