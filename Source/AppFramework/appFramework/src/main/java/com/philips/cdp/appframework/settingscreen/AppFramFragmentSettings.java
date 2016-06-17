/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.appframework.settingscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.cdp.appframework.R;

public class AppFramFragmentSettings extends Fragment {

    private ListViewSettings adapter;
    private ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_settings, container, false);

        list = (ListView) view.findViewById(R.id.listwithouticon);

        adapter = new ListViewSettings(getActivity());
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("ListViewWithoutIcons")) {
                adapter.setSavedBundle(savedInstanceState.getBundle("ListViewWithoutIcons"));
            }
        }

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Toast.makeText(getActivity(), "settings clicked", Toast.LENGTH_LONG).show();
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
