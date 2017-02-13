package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.dataUtils.GridDataHelper;
import com.philips.platform.catalogapp.databinding.FragmentGridviewSettingsBinding;


public class GridViewSettingsFragment extends BaseFragment {


    public ObservableBoolean isSecondaryActionEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean isDarkBackroungEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean isEnlargedGutterEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean isGridDisableEnabled = new ObservableBoolean(Boolean.TRUE);

    private GridDataHelper gridDataHelper;
    private FragmentGridviewSettingsBinding gridviewSettingsBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_gridview_settings;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        gridviewSettingsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_gridview_settings,container,false);
        gridviewSettingsBinding.setFrag(this);

        gridDataHelper = new GridDataHelper(getContext());
//        initSavedSettings();

        return gridviewSettingsBinding.getRoot();
    }

//    private void initSavedSettings(){
//        setSecondaryActionEnabled(gridDataHelper.isSecondaryActionEnabled());
//        setDarkBackgroundEnabled(gridDataHelper.isDarkBackgroundEnabled());
//        setEnlargedGutterEnabled(gridDataHelper.isEnlargedGutterEnabled());
//        setGridDisableEnabled(gridDataHelper.isSetDisableStateEnabled());
//    }
}
