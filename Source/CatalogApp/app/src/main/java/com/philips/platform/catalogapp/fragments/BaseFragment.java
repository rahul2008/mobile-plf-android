/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.NavigationController;
import com.philips.platform.catalogapp.R;


public abstract class BaseFragment extends Fragment implements NavigationController.BackPressListener {

    public static final String KEY_HIDE_THEME_MENU = "hide_menu";
    private boolean hideThemeMenu;

    public abstract int getPageTitle();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateThemeMenuVisibilityFlag();
    }

    @Override
    public void onResume() {
        super.onResume();

        int pageTitle = getPageTitle();
        if(pageTitle >0) {
            (getActivity()).setTitle(pageTitle);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (hideThemeMenu) {
            menu.setGroupVisible(R.id.main_menus, false);
        }
    }

    protected void showFragment(final BaseFragment fragment) {
        ((MainActivity) getActivity()).getNavigationController().switchFragment(fragment);
    }

    private void updateThemeMenuVisibilityFlag() {
        Bundle arguments = getArguments();
        if(arguments != null) {
            hideThemeMenu = arguments.getBoolean(KEY_HIDE_THEME_MENU, false);
        }
    }

    @Override
    public boolean handleBackPress() {
        return false;
    }
}
