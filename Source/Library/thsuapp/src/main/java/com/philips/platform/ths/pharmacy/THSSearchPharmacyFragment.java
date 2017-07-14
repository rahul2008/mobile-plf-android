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
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.SearchBox;

public class THSSearchPharmacyFragment extends THSBaseFragment implements SearchBox.ExpandListener, SearchBox.QuerySubmitListener,View.OnClickListener {

    private UIDNavigationIconToggler navIconToggler;
    //private SearchBox searchBox;
    private Button button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.ths_pharmacy_search_fragment,container,false);
        button = (Button) view.findViewById(R.id.launch_your_pharmacy);
        button.setOnClickListener(this);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        navIconToggler.hideNavigationIcon();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.search_pharmacy_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
//        searchBox = (SearchBox) menu.findItem(R.id.search_pharmacy_menu).getActionView();
//        searchBox.setExpandListener(this);
//        searchBox.setQuerySubmitListener(this);
//        searchBox.setQuery(searchBox.getQuery());
//        searchBox.setSearchBoxHint("Search for pharmacy");
//        searchBox.setDecoySearchViewHint("Search for pharmacy");
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
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.launch_your_pharmacy){
            getActivity().getSupportFragmentManager().beginTransaction().replace(getContainerID(),new THSPharmacyListFragment(),"Your Pharmace").addToBackStack(null).commit();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        navIconToggler.restoreNavigationIcon();
    }
}
