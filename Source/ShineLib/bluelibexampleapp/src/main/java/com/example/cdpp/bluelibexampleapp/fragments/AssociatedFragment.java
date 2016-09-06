package com.example.cdpp.bluelibexampleapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cdpp.bluelibexampleapp.R;
import com.example.cdpp.bluelibexampleapp.ReferenceApplication;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.List;

public class AssociatedFragment extends Fragment {

    private static final String TAG = "AssociatedFragment";

    private List<SHNDevice> mAssociatedDevices = new ArrayList<>();

    private SHNCentral mShnCentral;
    private DeviceArrayAdapter mAssociatedDevicesAdapter;

    public AssociatedFragment() {
    }

    public static AssociatedFragment newInstance() {
        return new AssociatedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_associated, container, false);

        // Obtain reference to BlueLib instance
        mShnCentral = ReferenceApplication.get().getShnCentral();

        // Associated devices list
        mAssociatedDevicesAdapter = new DeviceArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, mAssociatedDevices);

        ListView listView = (ListView) rootView.findViewById(R.id.associatedDevices);
        if (listView != null) {
            listView.setAdapter(mAssociatedDevicesAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    SHNLogger.d(TAG, "onItemClick, position: " + position);

                    SHNDevice device = mAssociatedDevices.get(position);
                    SHNLogger.d(TAG, "Associated device: " + device.getDeviceTypeName());

                    // TODO show detail Activity
                }
            });
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh list of associated devices
        mAssociatedDevices = mShnCentral.getShnDeviceAssociation().getAssociatedDevices();
        mAssociatedDevicesAdapter.notifyDataSetChanged();
    }

    private class DeviceArrayAdapter extends ArrayAdapter<SHNDevice> {

        public DeviceArrayAdapter(Context context, int resource, List<SHNDevice> objects) {
            super(context, resource, objects);
        }
    }
}
