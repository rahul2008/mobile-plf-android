package com.example.cdpp.bluelibexampleapp.device;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleApplication;
import com.example.cdpp.bluelibexampleapp.R;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;

import java.util.ArrayList;
import java.util.List;

public class AssociatedDevicesFragment extends Fragment {

    private SHNCentral mShnCentral;
    private List<SHNDevice> mAssociatedDevices = new ArrayList<>();
    private AssociatedDeviceAdapter mAssociatedDevicesAdapter;

    public AssociatedDevicesFragment() {
    }

    public static AssociatedDevicesFragment newInstance() {
        return new AssociatedDevicesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_associated, container, false);

        // Obtain reference to BlueLib instance
        mShnCentral = BlueLibExampleApplication.get().getShnCentral();

        // Setup associated device list
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.associatedDevices);
        setupRecyclerView(recyclerView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Refresh list of associated devices
        mAssociatedDevices = mShnCentral.getShnDeviceAssociation().getAssociatedDevices();
        mAssociatedDevicesAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (recyclerView == null) {
            throw new IllegalStateException("RecyclerView not initialized.");
        }
        mAssociatedDevicesAdapter = new AssociatedDeviceAdapter(mAssociatedDevices);
        recyclerView.setAdapter(mAssociatedDevicesAdapter);
    }
}
