package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.philips.cdp.ui.catalog.CustomListView.FavoritesAdapter;
import com.philips.cdp.ui.catalog.CustomListView.FavoritesAdapterAll;
import com.philips.cdp.ui.catalog.R;

import java.util.ArrayList;
import java.util.Set;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class Favorites extends Fragment{


    FavoritesAdapter adapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.favorites, container, false);
        list=(ListView)view.findViewById(R.id.fav_items);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter!=null)
            adapter.closeConnections();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            FavoritesActivity activity = (FavoritesActivity)getActivity();

            adapter=new FavoritesAdapter(getActivity());
            list.setAdapter(adapter);
        }
    }
}
