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
public class AllItems extends Fragment{


    FavoritesAdapterAll adapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.all_items, container, false);
        list=(ListView)view.findViewById(R.id.all_items);
        final FavoritesActivity activity = (FavoritesActivity)getActivity();
        adapter=new FavoritesAdapterAll(activity);
        list.setAdapter(adapter);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        try {
            if (isVisibleToUser) {
                final FavoritesActivity activity = (FavoritesActivity) getActivity();
                adapter = new FavoritesAdapterAll(activity);
                list.setAdapter(adapter);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
