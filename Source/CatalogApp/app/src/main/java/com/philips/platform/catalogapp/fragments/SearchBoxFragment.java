package com.philips.platform.catalogapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.dataUtils.StateListAdapter;
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxBinding;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.Arrays;

public class SearchBoxFragment extends BaseFragment implements SearchBox.ExpandListener, SearchBox.QuerySubmitListener{

    private FragmentSearchBoxBinding fragmentSearchBoxBinding;
    private SearchBox searchBox;
    String query;
    boolean searchIconExpanded = true;
    private UIDNavigationIconToggler navIconToggler;

    private static final String[] STATES = new String[]{
            "Alabama",
            "Alaska",
            "Arizona",
            "Arkansas",
            "California",
            "Colorado",
            "Connecticut",
            "Delaware",
            "Florida",
            "Georgia",
            "Hawaii",
            "Idaho",
            "Illinois",
            "Indiana",
            "Iowa",
            "Kansas",
            "Kentucky",
            "Louisiana",
            "Maine",
            "Maryland",
            "Massachusetts",
            "Michigan",
            "Minnesota",
            "Mississippi",
            "Missouri",
            "Montana",
            "Nebraska",
            "Nevada",
            "New Hampshire",
            "New Jersey",
            "New Mexico",
            "New York",
            "North Carolina",
            "North Dakota",
            "Ohio",
            "Oklahoma",
            "Oregon",
            "Pennsylvania",
            "Rhode Island",
            "South Carolina",
            "South Dakota",
            "Tennessee",
            "Texas",
            "Utah",
            "Vermont",
            "Virginia",
            "Washington",
            "West Virginia",
            "Wisconsin",
            "Wyoming"
    };
    private StateListAdapter stateAdapter;

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
        stateAdapter = new StateListAdapter(getActivity(), Arrays.asList(STATES));
        fragmentSearchBoxBinding.countryList.setAdapter(stateAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_set_theme_settings).setVisible(false);
        menu.findItem(R.id.menu_theme_settings).setVisible(false);
        menu.findItem(R.id.country_search).setVisible(true);
        searchBox = (SearchBox) menu.findItem(R.id.country_search).getActionView();
        searchBox.setExpandListener(this);
        searchBox.setQuerySubmitListener(this);
        searchBox.setSearchIconified(searchIconExpanded);
        searchBox.setQuery(query);
        searchBox.setAdapter(stateAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("iconified",searchBox.isSearchIconified());
        outState.putString("query", String.valueOf(searchBox.getQuery()));
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

    @Override
    public void onQuerySubmit(CharSequence query) {

    }

}