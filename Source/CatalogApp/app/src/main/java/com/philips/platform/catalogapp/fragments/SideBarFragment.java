package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.DataHolder;
import com.philips.platform.catalogapp.DataHolderView;
import com.philips.platform.catalogapp.MainActivity;
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

        return fragmentSideBarBinding.getRoot();
    }

    public void showSideBar(){

        if(fragmentSideBarBinding.radiobuttonLeft.isChecked()){
            if(fragmentSideBarBinding.radiobuttonMapContent.isChecked()){
                ((MainActivity)getActivity()).setLeftSidebarBGColor(getContentMappedBGColor());
            } else if(fragmentSideBarBinding.radiobuttonMapNavigation.isChecked()){
                ((MainActivity)getActivity()).setLeftSidebarBGColor(getNavigationMappedBGColor());
            }
            ((MainActivity)getActivity()).getSideBar().openDrawer(GravityCompat.START);
        } else if(fragmentSideBarBinding.radiobuttonRight.isChecked()){
            if(fragmentSideBarBinding.radiobuttonMapContent.isChecked()){
                ((MainActivity)getActivity()).setRightSidebarBGColor(getContentMappedBGColor());
            } else if(fragmentSideBarBinding.radiobuttonMapNavigation.isChecked()){
                ((MainActivity)getActivity()).setRightSidebarBGColor(getNavigationMappedBGColor());
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
