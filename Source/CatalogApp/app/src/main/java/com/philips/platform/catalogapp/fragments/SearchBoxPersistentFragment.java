package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.dataUtils.StateListAdapter;
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxPersistentBinding;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.Arrays;


public class SearchBoxPersistentFragment extends BaseFragment implements SearchBox.ExpandListener, SearchBox.QuerySubmitListener{

    private FragmentSearchBoxPersistentBinding fragmentSearchBoxPersistentBinding;
    private SearchBox searchBox;
    String query;
    private StateListAdapter stateAdapter;
    boolean searchBoxExpanded = true;
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

    @Override
    public int getPageTitle() {
        return R.string.page_title_searchbox_persistent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSearchBoxPersistentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_box_persistent, container, false);
        fragmentSearchBoxPersistentBinding.setFrag(this);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        navIconToggler.hideNavigationIcon();
        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query");
            searchBoxExpanded = savedInstanceState.getBoolean("expanded");
        }
        setListAdapter();
        return fragmentSearchBoxPersistentBinding.getRoot();
    }

    private void setListAdapter() {
        stateAdapter = new StateListAdapter(getActivity(), Arrays.asList(STATES));
        fragmentSearchBoxPersistentBinding.countryList.setAdapter(stateAdapter);
        fragmentSearchBoxPersistentBinding.countryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchBox.setSearchCollapsed(true);
            }
        });
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
        searchBox.setSearchIconified(false);
        searchBox.setSearchCollapsed(searchBoxExpanded);
        searchBox.setQuery(query);
        searchBox.setAdapter(stateAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("expanded",searchBox.isSearchCollapsed());
        outState.putString("query", String.valueOf(searchBox.getQuery()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onSearchExpanded() {
        fragmentSearchBoxPersistentBinding.countryList.setVisibility(View.VISIBLE);
        fragmentSearchBoxPersistentBinding.searchDescription.setVisibility(View.GONE);
    }

    @Override
    public void onSearchCollapsed() {
        fragmentSearchBoxPersistentBinding.countryList.setVisibility(View.GONE);
        fragmentSearchBoxPersistentBinding.searchDescription.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQuerySubmit(CharSequence query) {

    }

    @Override
    public boolean handleBackPress() {
        if(!searchBox.isSearchCollapsed()){
            searchBox.setSearchCollapsed(true);
            return true;
        }
        return super.handleBackPress();
    }

}
