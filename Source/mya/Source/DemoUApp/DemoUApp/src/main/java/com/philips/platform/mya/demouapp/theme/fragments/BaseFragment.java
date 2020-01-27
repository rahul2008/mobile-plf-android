/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.mya.demouapp.theme.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philips.platform.mya.demouapp.theme.themesettings.NavigationController;


public abstract class BaseFragment extends Fragment implements NavigationController.BackPressListener {

    public abstract int getPageTitle();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        //(getActivity()).setTitle(getPageTitle());
    }


    @Override
    public boolean handleBackPress() {
        return false;
    }
}