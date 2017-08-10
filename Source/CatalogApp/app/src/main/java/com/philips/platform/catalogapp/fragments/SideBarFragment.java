package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.DataHolder;
import com.philips.platform.catalogapp.DataHolderView;
import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.NavigationController;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSideBarBinding;
//import com.philips.platform.catalogapp.databinding.SidebarViewBinding;
import com.philips.platform.uid.thememanager.ThemeUtils;
import com.philips.platform.uid.utils.UIDContextWrapper;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;
import com.philips.platform.uid.view.widget.SideBar;

/**
 * Created by Kunal on 31/07/17.
 */

public class SideBarFragment extends BaseFragment {

    @Override
    public int getPageTitle() {
        return R.string.page_title_sidebar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentSideBarBinding fragmentSideBarBinding;
        fragmentSideBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_side_bar, container, false);
        fragmentSideBarBinding.setFrag(this);
        return fragmentSideBarBinding.getRoot();
    }

    public void showSideBar(){
        ((MainActivity)getActivity()).getSideBar().openDrawer(GravityCompat.START);
    }
}
