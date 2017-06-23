package com.philips.platform.catalogapp.fragments;


import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.dataUtils.StateListAdapter;
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxBinding;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.Arrays;

public class SearchBoxFragment extends BaseFragment implements SearchBox.ExpandListener, SearchBox.QuerySubmitListener {

    private FragmentSearchBoxBinding fragmentSearchBoxBinding;
    private SearchBox searchBox;
    private String query;
    boolean searchBoxCollpased = true;
    private UIDNavigationIconToggler navIconToggler;
    private StateListAdapter stateAdapter;
    private SharedPreferences sharedPreferences;
    public static final String SEARCH_TYPE = "search_selection";
    private boolean isExpandableSearch;

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
            searchBoxCollpased = savedInstanceState.getBoolean("collapsed");
        }
        setListAdapter();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        isExpandableSearch = sharedPreferences.getBoolean(SEARCH_TYPE, true);
        if (!isExpandableSearch) {
            navIconToggler.hideNavigationIcon();
        }
        return fragmentSearchBoxBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.setGroupVisible(R.id.main_menus, false);
        inflater.inflate(R.menu.country_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setListAdapter() {
        stateAdapter = new StateListAdapter(getActivity(), Arrays.asList(STATES));
        fragmentSearchBoxBinding.countryList.setAdapter(stateAdapter);
        fragmentSearchBoxBinding.countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchBox.setSearchCollapsed(true);
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchBox = (SearchBox) menu.findItem(R.id.country_search).getActionView();
        searchBox.setExpandListener(this);
        searchBox.setQuerySubmitListener(this);
        searchBox.setSearchIconified(isExpandableSearch);
        searchBox.setSearchCollapsed(searchBoxCollpased);
        searchBox.setQuery(query);
        searchBox.setAdapter(stateAdapter);
        searchBox.setSearchBoxHint(R.string.search_box_hint);
        searchBox.setDecoySearchViewHint(R.string.search_box_hint);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("collapsed", searchBox.isSearchCollapsed());
        outState.putString("query", String.valueOf(searchBox.getQuery()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSearchExpanded() {
        if (isExpandableSearch) {
            navIconToggler.hideNavigationIcon();
        }
        fragmentSearchBoxBinding.countryList.setVisibility(View.VISIBLE);
        fragmentSearchBoxBinding.searchDescription.setVisibility(View.GONE);
    }

    @Override
    public void onSearchCollapsed() {
        if (isExpandableSearch) {
            navIconToggler.restoreNavigationIcon();
        }
        fragmentSearchBoxBinding.countryList.setVisibility(View.GONE);
        fragmentSearchBoxBinding.searchDescription.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQuerySubmit(CharSequence query) {

    }

    @Override
    public boolean handleBackPress() {
        if (!searchBox.isSearchCollapsed()) {
            searchBox.setSearchCollapsed(true);
            return true;
        }
        return super.handleBackPress();
    }

}