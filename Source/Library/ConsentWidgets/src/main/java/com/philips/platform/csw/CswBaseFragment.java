/*
 * Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * * in whole or in part is prohibited without the prior written
 * * consent of the copyright holder.
 * /
 */

package com.philips.platform.csw;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class CswBaseFragment extends Fragment {

    protected int mLeftRightMarginPort;

    protected int mLeftRightMarginLand;

    public abstract int getTitleResourceId();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLeftRightMarginPort = (int) getResources().getDimension(com.philips.cdp.registration.R.dimen.reg_layout_margin_port);
        mLeftRightMarginLand = (int) getResources().getDimension(com.philips.cdp.registration.R.dimen.reg_layout_margin_land);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentTitle();
    }

    private void setCurrentTitle() {
        CswFragment fragment = overridableGetParentFragment();
        if (fragment != null) {
            if (fragment.getUpdateTitleListener() != null) {
                fragment.getUpdateTitleListener().updateActionBar(getTitleResourceId(), (fragment.getFragmentCount() > 1));
            }

            fragment.setResourceID(getTitleResourceId());
        }
    }

    protected CswFragment overridableGetParentFragment() {
        return (CswFragment) getParentFragment();
    }
}
