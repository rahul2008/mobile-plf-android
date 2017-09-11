/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
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
//import com.philips.platform.catalogapp.databinding.SidebarViewBinding;


public class SideBarFragment extends BaseFragment {

    private Context context;
    private FragmentSideBarBinding fragmentSideBarBinding;
    private TypedArray typedArray;
    private int sidebarBGColor;

    @Override
    public int getPageTitle() {
        return R.string.page_title_sidebar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();

        fragmentSideBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_bar, container, false);
        fragmentSideBarBinding.setFrag(this);

        ((MainActivity)getActivity()).getSideBar().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
        ((MainActivity)getActivity()).getSideBar().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.END);

        return fragmentSideBarBinding.getRoot();
    }

    public void showSideBar(){

        if(fragmentSideBarBinding.radiobuttonLeft.isChecked()){
            if(fragmentSideBarBinding.radiobuttonMapContent.isChecked()){
                ((MainActivity)getActivity()).showContentThemedLeftComponents();
            } else if(fragmentSideBarBinding.radiobuttonMapNavigation.isChecked()){
                ((MainActivity)getActivity()).showNavigationThemedLeftComponents();
            }
            ((MainActivity)getActivity()).getSideBar().openDrawer(GravityCompat.START);
        } else if(fragmentSideBarBinding.radiobuttonRight.isChecked()){
            if(fragmentSideBarBinding.radiobuttonMapContent.isChecked()){
                ((MainActivity)getActivity()).showContentThemedRightComponents();
            } else if(fragmentSideBarBinding.radiobuttonMapNavigation.isChecked()){
                ((MainActivity)getActivity()).showNavigationThemedRightComponents();
            }
            ((MainActivity)getActivity()).getSideBar().openDrawer(GravityCompat.END);
        }
    }

    private int getContentMappedBGColor(){
        typedArray = getActivity().getTheme().obtainStyledAttributes(new int[]{R.attr.uidContentPrimaryBackgroundColor});
        if (typedArray != null) {
            sidebarBGColor = typedArray.getColor(0, Color.WHITE);
            typedArray.recycle();
        }
        return sidebarBGColor;
    }

    private int getNavigationMappedBGColor(){
        typedArray = getActivity().getTheme().obtainStyledAttributes(new int[]{R.attr.uidNavigationPrimaryBackgroundColor});
        if (typedArray != null) {
            sidebarBGColor = typedArray.getColor(0, Color.WHITE);
            typedArray.recycle();
        }
        return sidebarBGColor;
    }

}
