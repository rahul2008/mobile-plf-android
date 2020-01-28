/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.themesettings;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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