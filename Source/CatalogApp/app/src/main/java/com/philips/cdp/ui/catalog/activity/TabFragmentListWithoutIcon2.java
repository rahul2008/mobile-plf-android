package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.CustomListView.ListViewWithOptions;
import com.philips.cdp.ui.catalog.CustomListView.ListViewWithoutIcons;
import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TabFragmentListWithoutIcon2 extends Fragment {

    ListViewWithoutIcons adapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.listview_without_icons2, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        list=(ListView)getActivity().findViewById(R.id.listwithouticon2);

        adapter=new ListViewWithoutIcons(getActivity());

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
