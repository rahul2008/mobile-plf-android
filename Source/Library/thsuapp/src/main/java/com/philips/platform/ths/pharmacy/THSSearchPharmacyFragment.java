package com.philips.platform.ths.pharmacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.SearchBox;

public class THSSearchPharmacyFragment extends THSBaseFragment implements SearchBox.ExpandListener, SearchBox.QuerySubmitListener,View.OnClickListener {

    public static String TAG = THSSearchPharmacyFragment.class.getSimpleName();
    private UIDNavigationIconToggler navIconToggler;
    private SearchBox searchBox;
    private EditText searchPharmacy;
    private ActionBarListener actionBarListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.ths_pharmacy_search_fragment,container,false);
        searchPharmacy = (EditText) view.findViewById(R.id.launch_your_pharmacy);
        searchPharmacy.setOnClickListener(this);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(R.string.search_pharmacy_fragment_name,true);
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        navIconToggler.hideNavigationIcon();
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

    @Override
    public void onSearchExpanded() {
        navIconToggler.hideNavigationIcon();
    }

    @Override
    public void onSearchCollapsed() {
        navIconToggler.hideNavigationIcon();
    }

    @Override
    public void onQuerySubmit(CharSequence charSequence) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.launch_your_pharmacy){
            final THSPharmacyListFragment fragment = new THSPharmacyListFragment();
            fragment.setFragmentLauncher(getFragmentLauncher());
            getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(), fragment,"Your Pharmace").addToBackStack(null).commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        navIconToggler.restoreNavigationIcon();
    }
}
