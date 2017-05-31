package com.philips.platform.catalogapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxBinding;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

public class SearchBoxFragment extends BaseFragment implements SearchBox.ExpandListener{

    private FragmentSearchBoxBinding fragmentSearchBoxBinding;
    private SearchBox searchBox;
    String query;
    boolean searchIconExpanded = false;
    private UIDNavigationIconToggler navIconToggler;

    private static final String[] COUNTRIES = new String[]{
            "Belgium", "Brazil", "Belarus", "France", "Italy", "Germany", "Spain"
    };
    private ArrayAdapter<String> countryAdapter;

    @Override
    public int getPageTitle() {
        return R.string.page_title_searchbox;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSearchBoxBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_box, container, false);
        fragmentSearchBoxBinding.setFrag(this);
        navIconToggler = new UIDNavigationIconToggler(getActivity());

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query");
            searchIconExpanded = savedInstanceState.getBoolean("iconified");
        }
        setListAdapter();
        return fragmentSearchBoxBinding.getRoot();
    }

    private void setListAdapter() {
        countryAdapter = new ArrayAdapter<>(getActivity(), R.layout.uid_search_item_one_line ,COUNTRIES);
        fragmentSearchBoxBinding.countryList.setAdapter(countryAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.country_search, menu);
            searchBox = (SearchBox) menu.findItem(R.id.country_search).getActionView();
        searchBox.setExpandListener(this);
        searchBox.setSearchIconified(searchIconExpanded);
        searchBox.searchTextView.setText(query);
        searchBox.setAdapter(countryAdapter);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("iconified",searchBox.isSearchIconified());
        outState.putString("query", String.valueOf(searchBox.searchTextView.getText()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSearchExpanded() {
        navIconToggler.hideNavigationIcon();
    }

    @Override
    public void onSearchCollapsed() {
        navIconToggler.restoreNavigationIcon();
    }
}