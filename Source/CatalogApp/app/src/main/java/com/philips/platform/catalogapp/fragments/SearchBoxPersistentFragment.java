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
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxPersistentBinding;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.Arrays;


public class SearchBoxPersistentFragment extends BaseFragment {

    private FragmentSearchBoxPersistentBinding fragmentSearchBoxPersistentBinding;
    private SearchBox searchBox;
    String query;

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
        fragmentSearchBoxPersistentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_box_persistent, container, false);
        fragmentSearchBoxPersistentBinding.setFrag(this);

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query");
        }
        setListAdapter();
        return fragmentSearchBoxPersistentBinding.getRoot();
    }

    private void setListAdapter() {
        stateAdapter = new StateListAdapter(getActivity(), Arrays.asList(STATES));
        fragmentSearchBoxPersistentBinding.countryList.setAdapter(stateAdapter);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menu_set_theme_settings).setVisible(false);
        menu.findItem(R.id.menu_theme_settings).setVisible(false);
        menu.findItem(R.id.country_search).setVisible(false);
        searchBox = (SearchBox) menu.findItem(R.id.country_search).getActionView();
        searchBox.setQuery(query);
        searchBox.setAdapter(stateAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("iconified",searchBox.isSearchIconified());
        outState.putString("query", String.valueOf(searchBox.getQuery()));
        super.onSaveInstanceState(outState);
    }


}
