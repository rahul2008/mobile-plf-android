/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSideBarBinding;

public class SideBarFragment extends BaseFragment {

    private FragmentSideBarBinding fragmentSideBarBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_sidebar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentSideBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_bar, container, false);
        fragmentSideBarBinding.setFrag(this);

        ((MainActivity)getActivity()).getSideBarController().getSideBar().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
        ((MainActivity)getActivity()).getSideBarController().getSideBar().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);

        return fragmentSideBarBinding.getRoot();
    }

    public void showSideBar(){

        if(fragmentSideBarBinding.radiobuttonLeft.isChecked()){
            if(fragmentSideBarBinding.radiobuttonMapContent.isChecked()){
                ((MainActivity)getActivity()).getSideBarController().showContentThemedLeftComponents();
            } else if(fragmentSideBarBinding.radiobuttonMapNavigation.isChecked()){
                ((MainActivity)getActivity()).getSideBarController().showNavigationThemedLeftComponents();
            }
            ((MainActivity)getActivity()).getSideBarController().getSideBar().openDrawer(GravityCompat.START);
        } else if(fragmentSideBarBinding.radiobuttonRight.isChecked()){
            if(fragmentSideBarBinding.radiobuttonMapContent.isChecked()){
                ((MainActivity)getActivity()).getSideBarController().showContentThemedRightComponents();
            } else if(fragmentSideBarBinding.radiobuttonMapNavigation.isChecked()){
                ((MainActivity)getActivity()).getSideBarController().showNavigationThemedRightComponents();
            }
            ((MainActivity)getActivity()).getSideBarController().getSideBar().openDrawer(GravityCompat.END);
        }
    }
}
