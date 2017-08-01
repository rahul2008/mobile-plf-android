package com.philips.platform.ths.providerdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProviderListViewInterface;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

import java.util.List;

public class THSSearchProviderFragment extends THSBaseFragment implements THSProviderListViewInterface,SearchBox.ExpandListener, SearchBox.QuerySubmitListener{

    private UIDNavigationIconToggler navIconToggler;
    private SearchBox searchBox;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        navIconToggler.hideNavigationIcon();
        View view = inflater.inflate(R.layout.ths_search_layout,container,false);
        return view;
    }


    @Override
    public void updateProviderAdapterList(List<THSProviderInfo> providerInfos) {

    }

    @Override
    public void onSearchExpanded() {

    }

    @Override
    public void onSearchCollapsed() {

    }

    @Override
    public void onQuerySubmit(CharSequence charSequence) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.ths_pharmacy_search_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchBox = (SearchBox) menu.findItem(R.id.ths_pharmacy_search).getActionView();
        searchBox.setExpandListener(this);
        searchBox.setQuerySubmitListener(this);
        searchBox.setQuery(searchBox.getQuery());
        searchBox.setSearchBoxHint("Search for pharmacy");
        searchBox.setDecoySearchViewHint("Search for pharmacy");
        searchBox.setExpandListener(this);
        searchBox.setQuerySubmitListener(this);
        searchBox.setSearchIconified(true);
        searchBox.setSearchCollapsed(true);
    }
}
