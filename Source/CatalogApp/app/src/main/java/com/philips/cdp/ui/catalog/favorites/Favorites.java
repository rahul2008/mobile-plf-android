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


public class Favorites extends Fragment{


    FavoritesAdapter adapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.favorites, container, false);
        list=(ListView)view.findViewById(R.id.fav_items);
        return view;
    }

    /**
     * All the DB connections should be closed on Destroy of the Fragment
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter!=null)
            adapter.closeConnections();
    }

    /**
     * When ever the DataSet changes, The changes should be visible to the User. Hence This API is used for setting the Adapter
     * @param isVisibleToUser
     */
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
