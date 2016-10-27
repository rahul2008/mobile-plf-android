/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.support.v4.app.Fragment;

import com.philips.platform.catalogapp.MainActivity;

public abstract class BaseFragment extends Fragment {

    public abstract int getPageTitle();

    public boolean showThemeSettingsIcon() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        (getActivity()).setTitle(getPageTitle());
        if (showThemeSettingsIcon()) {
            ((MainActivity) getActivity()).showThemeSettingsIcon();
        }
    }
}
