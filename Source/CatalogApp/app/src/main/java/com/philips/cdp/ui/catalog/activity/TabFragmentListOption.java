/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.CustomListView.ListViewWithOptions;
import com.philips.cdp.ui.catalog.R;

public class TabFragmentListOption extends Fragment {

    ListViewWithOptions adapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.listview_options, container, false);

        list=(ListView)view.findViewById(R.id.listoption);


        adapter=new ListViewWithOptions(getActivity());
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

}
