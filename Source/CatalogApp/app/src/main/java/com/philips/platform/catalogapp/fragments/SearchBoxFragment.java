package com.philips.platform.catalogapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.MainActivity;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxBinding;

public class SearchBoxFragment extends BaseFragment {

    private FragmentSearchBoxBinding fragmentSearchBoxBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_searchbox;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSearchBoxBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_box, container, false);
        fragmentSearchBoxBinding.setFrag(this);
        fragmentSearchBoxBinding.searchBox.setMinimumHeight(((MainActivity)getActivity()).getSupportActionBar().getHeight());
        fragmentSearchBoxBinding.searchBox.mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentSearchBoxBinding.searchBox.setVisibility(View.GONE);
                ((MainActivity)getActivity()).getSupportActionBar().show();
            }
        });
        return fragmentSearchBoxBinding.getRoot();
    }

    @Override
    public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem menuItem = menu.findItem(R.id.menu_search_trigger);
        menu.findItem(R.id.menu_set_theme_settings).setVisible(false);
        menu.findItem(R.id.menu_theme_settings).setVisible(false);
        menuItem.setVisible(true);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                fragmentSearchBoxBinding.searchBox.setVisibility(View.VISIBLE);
                ((MainActivity)getActivity()).getSupportActionBar().hide();
                return true;
            }
        });

    }

    @Override
    public void onStop() {
        ((MainActivity)getActivity()).getSupportActionBar().show();
        super.onStop();
    }
}
