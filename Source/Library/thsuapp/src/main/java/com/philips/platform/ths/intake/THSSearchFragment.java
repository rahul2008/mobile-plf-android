package com.philips.platform.ths.intake;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.utils.UIDNavigationIconToggler;
import com.philips.platform.uid.view.widget.SearchBox;

/**
 * Created by philips on 7/11/17.
 */

public class THSSearchFragment extends THSBaseFragment implements SearchBox.QuerySubmitListener,View.OnClickListener {
    public static final String TAG = THSSearchFragment.class.getSimpleName();
    private THSBasePresenter mPresenter;

    private ListView searchListView;
    private UIDNavigationIconToggler navIconToggler;
    private SearchBox searchBox;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_search_layout, container, false);

        searchListView = (ListView) view.findViewById(R.id.pth_search_listview);
        navIconToggler = new UIDNavigationIconToggler(getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // actionBarListener.
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }


    @Override
    public void onResume() {
        super.onResume();
        navIconToggler.restoreNavigationIcon();
    }

    @Override
    public void onStop() {
        super.onStop();
        navIconToggler.hideNavigationIcon();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchBox = (SearchBox) menu.findItem(R.id.search_pharmacy_menu).getActionView();
       // searchBox.setExpandListener(this);

        searchBox.setQuerySubmitListener(this);
        searchBox.setQuery(searchBox.getQuery());
        searchBox.setSearchBoxHint("Search for medication");
        searchBox.setDecoySearchViewHint("Search for medication");

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onQuerySubmit(CharSequence charSequence) {

    }
}
