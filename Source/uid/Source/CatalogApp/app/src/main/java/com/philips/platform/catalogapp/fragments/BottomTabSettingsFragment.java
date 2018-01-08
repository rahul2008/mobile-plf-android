/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.NavigationController;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentBottomTabSettingsBinding;

public class BottomTabSettingsFragment extends BaseFragment {

    private FragmentBottomTabSettingsBinding fragmentBottomTabSettingsBinding;
    public static ObservableBoolean isIconOnlyEnabled = new ObservableBoolean(Boolean.TRUE);
    public static ObservableInt itemSelection = new ObservableInt();
    private NavigationController navigationController;

    @Override
    public int getPageTitle() {
        return R.string.page_title_bottom_tab_bar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBottomTabSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bottom_tab_settings, container, false);
        fragmentBottomTabSettingsBinding.setFrag(this);
        navigationController = ((MainActivity) getActivity()).getNavigationController();
        return fragmentBottomTabSettingsBinding.getRoot();
    }

    @Override
    public void onPause() {
        super.onPause();
        getCheckedRadioButtonItems();
    }

    public void setIconOnlyEnabled(boolean isIconOnlyEnabled){
        BottomTabSettingsFragment.isIconOnlyEnabled.set(isIconOnlyEnabled);
    }

    public void setItemSelection(int itemSelection){
        BottomTabSettingsFragment.itemSelection.set(itemSelection);
    }

    private void getCheckedRadioButtonItems() {
        int checkedRadioButtonId = fragmentBottomTabSettingsBinding.radioGroupItems.getCheckedRadioButtonId();
        switch (checkedRadioButtonId){
            case R.id.four_items:
                setItemSelection(1);
                break;
            case R.id.more_than_five_items:
                setItemSelection(2);
                break;
            default:
                setItemSelection(1);
                break;
        }
    }

    public void showBottomTabBar() {
        navigationController.switchFragment(new BottomTabBarFragment());
    }

}