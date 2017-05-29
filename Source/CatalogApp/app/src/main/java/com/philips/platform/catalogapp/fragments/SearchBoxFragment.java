package com.philips.platform.catalogapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxBinding;
import com.philips.platform.uid.view.widget.SearchBox;

public class SearchBoxFragment extends BaseFragment {

    private FragmentSearchBoxBinding fragmentSearchBoxBinding;
    private SearchBox searchBox;

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "Brazil", "Belarus", "France", "Italy", "Germany", "Spain"
    };

    @Override
    public int getPageTitle() {
        return R.string.page_title_searchbox;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSearchBoxBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_box, container, false);
        fragmentSearchBoxBinding.setFrag(this);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        return fragmentSearchBoxBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.country_search, menu);
        searchBox = (SearchBox) menu.findItem(R.id.country_search).getActionView();
        searchBox.setIconified(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
}