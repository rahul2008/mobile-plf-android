/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.wrapper;

import android.support.v4.app.FragmentActivity;

import com.philips.platform.mya.MyaFragment;

public class MyaFragmentWrapper extends MyaFragment {
    public FragmentActivity fragmentActivity;

    @Override
    protected FragmentActivity overridableGetActivity() {
        return fragmentActivity;
    }
}
