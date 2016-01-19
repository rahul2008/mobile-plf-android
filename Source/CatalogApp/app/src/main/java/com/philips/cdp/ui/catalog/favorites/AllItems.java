/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.favorites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.FavoritesActivity;

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
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter!=null)
            adapter.closeConnections();
    }

    /**
     * Whenever the DataSet changes the change should be visible to the User - Hence this API
     * @param isVisibleToUser
     */
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

        }
    }

}
