package com.philips.platform.catalogapp.fragments;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSearchBoxBinding;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

public class SearchBoxFragment extends BaseFragment {

    private FragmentSearchBoxBinding fragmentSearchBoxBinding;
    private SearchBox searchBox;
    String query;
    boolean searchIconExpanded = true;
    private UIDNavigationIconToggler navIconToggler;

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
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        navIconToggler.hideNavigationIcon();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query");
            searchIconExpanded = savedInstanceState.getBoolean("iconified");
        }
        return fragmentSearchBoxBinding.getRoot();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.country_search, menu);
            searchBox = (SearchBox) menu.findItem(R.id.country_search).getActionView();
        searchBox.setSearchIconified(searchIconExpanded);
        searchBox.autoCompleteTextView.setText(query);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.country_search) {
            navIconToggler.hideNavigationIcon();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("iconified",searchBox.isSearchIconified());
        outState.putString("query", String.valueOf(searchBox.autoCompleteTextView.getText()));
        super.onSaveInstanceState(outState);
    }

}