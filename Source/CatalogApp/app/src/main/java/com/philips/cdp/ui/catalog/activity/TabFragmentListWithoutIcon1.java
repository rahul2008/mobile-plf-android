/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.CustomListView.ListViewWithoutIcons;
import com.philips.cdp.ui.catalog.R;

/**
 * <b></b> TabFragmentListWithoutIcon1 is class to demonstrate the use of List with icons with the help of an adapter ListViewWithoutIcons </b>
 * <p/>
 * <p/>
 * <b></b>Make Arrays of Section Headers and Data , and setAdapter() </b><br>
 */
public class TabFragmentListWithoutIcon1 extends Fragment {

    ListViewWithoutIcons adapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_without_icons, container, false);

        list = (ListView) view.findViewById(R.id.listwithouticon);

        adapter = new ListViewWithoutIcons(getActivity());
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("ListViewWithoutIcons")) {
                adapter.setSavedBundle(savedInstanceState.getBundle("ListViewWithoutIcons"));
            }
        }

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Intent intent = new Intent(getActivity(), com.philips.cdp.ui.catalog.activity.DummyActivityForListItemClick.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("ListViewWithoutIcons", adapter.getSavedBundle());
    }


}
