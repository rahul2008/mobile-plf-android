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

import com.philips.cdp.ui.catalog.CustomListView.ListViewWithIcons;
import com.philips.cdp.ui.catalog.CustomListView.ListViewWithOptions;
import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TabFragmentListicon extends Fragment {

    ListViewWithIcons mAdapter;
    ListViewWithIcons savedAdapter;
    ListView list;
    public static boolean switch1=false;
    public static boolean switch2=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        View view =  inflater.inflate(R.layout.listview_icons, container, false);


        list=(ListView)view.findViewById(R.id.listicon);

        mAdapter = new ListViewWithIcons(getActivity());
        mAdapter.addSectionHeaderItem("Title Pallendia");
        mAdapter.addItem("Quisque ");
        mAdapter.addItem("Eget Odio ");
        mAdapter.addItem("Foscibus ");
        mAdapter.addItem("AC Lectus ");
        mAdapter.addItem("Pellentesque ");
        mAdapter.addSectionHeaderItem("Title Pallendia");
        mAdapter.addItem("Vestibullum ");
        mAdapter.addItem("Nulla Facilisi ");
        mAdapter.addItem("Tortor ");

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("ListviewWithIcons")) {
                mAdapter.setSavedBundle(savedInstanceState.getBundle("ListviewWithIcons"));
            }
        }

     /*   if(savedAdapter!=null)
        {
            list.setAdapter(savedAdapter);
        }
        else*/


        // setListAdapter(mAdapter);
        list.setAdapter(mAdapter);


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
    public void onDestroyView() {
        super.onDestroyView();

       // savedAdapter = mAdapte;
      //  savedFilelist = filelist;
       // fromBackStack = true;
      //  Log.e("onDestroyView", "onDestroyView");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("ListviewWithIcons",mAdapter.getSavedBundle());
    }


}
