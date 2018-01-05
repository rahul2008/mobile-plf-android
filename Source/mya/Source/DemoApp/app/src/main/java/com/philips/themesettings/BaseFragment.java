/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.themesettings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {

    public abstract int getPageTitle();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        (getActivity()).setTitle(getPageTitle());
    }

//    protected void showFragment(final BaseFragment fragment) {
//        ((MainActivity) getActivity()).getNavigationController().switchFragment(fragment);
//    }


//    @Override
//    public boolean handleBackPress() {
//        return false;
//    }
}