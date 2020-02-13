/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform;

import androidx.annotation.StringRes;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

public class TestActivity extends HamburgerActivity {

    @Override
    protected void initializeActivityContents() {

    }

    @Override
    public int getContainerId() {
        return R.id.frame_container;
    }


    @Override
    public void updateActionBarIcon(boolean b) {

    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    @Override
    public void updateSelectionIndex(int position) {

    }

    @Override
    protected void removeListeners() {

    }
}
