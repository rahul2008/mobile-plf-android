/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;


import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxSelectBinding;

public class SearchBoxSelectFragment extends BaseFragment {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String SEARCH_TYPE = "search_selection";

    @Override
    public int getPageTitle() {
        return R.string.page_title_searchbox;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentSearchBoxSelectBinding fragmentSearchBoxSelectBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_box_select, container, false);
        fragmentSearchBoxSelectBinding.setFragment(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = sharedPreferences.edit();
        return fragmentSearchBoxSelectBinding.getRoot();
    }

    public void onExpandableClicked() {
        editor.putBoolean(SEARCH_TYPE, true).apply();
        showFragment(new SearchBoxFragment());
    }

    public void onPersistentClicked() {
        editor.putBoolean(SEARCH_TYPE, false).apply();
        showFragment(new SearchBoxFragment());
    }
}
