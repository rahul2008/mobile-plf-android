/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.appframework.settingscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.philips.cdp.appframework.AppFrameworkBaseFragment;
import com.philips.cdp.appframework.R;

/**
 * SettingsFragment is the Base Class of all existing fragments.
 *
 * @author: ritesh.jha@philips.com
 * @since: June 17, 2016
 */
public class SettingsFragment extends AppFrameworkBaseFragment {

    private ListViewSettings mAdapter = null;
    private ListView mList = null;
    private static String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.settings_screen_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listview_settings, container, false);

        mList = (ListView) view.findViewById(R.id.listwithouticon);

        mAdapter = new ListViewSettings(getActivity());
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("ListViewWithoutIcons")) {
                mAdapter.setSavedBundle(savedInstanceState.getBundle("ListViewWithoutIcons"));
            }
        }

        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        outState.putBundle("ListViewWithoutIcons", mAdapter.getSavedBundle());
    }
}
