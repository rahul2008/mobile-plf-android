/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.example.cdpp.bluelibexampleapp.connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cdpp.bluelibexampleapp.BlueLibExampleApplication;
import com.example.cdpp.bluelibexampleapp.R;
import com.example.cdpp.bluelibexampleapp.device.BaseDeviceAdapter;
import com.example.cdpp.bluelibexampleapp.device.DeviceDetailActivity;
import com.example.cdpp.bluelibexampleapp.device.DeviceScanner;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;

import java.util.ArrayList;
import java.util.List;

public class ConnectDevicesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, DeviceScanner.OnDeviceScanListener {

    private ConnectDeviceAdapter mConnectDeviceAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private DeviceScanner mDeviceScanner;
    private List<SHNDeviceFoundInfo> mDeviceFoundInfos = new ArrayList<>();

    public ConnectDevicesFragment() {
    }

    public static ConnectDevicesFragment newInstance() {
        return new ConnectDevicesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_connect, container, false);

        // Setup swipe layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Setup nearby devices list
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.nearbyDevices);
        setupRecyclerView(recyclerView);

        // Listen for scan events
        mDeviceScanner = BlueLibExampleApplication.get().getScanner();
        mDeviceScanner.addOnScanListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mDeviceFoundInfos.isEmpty()) {
            mDeviceScanner.startScan();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDeviceScanner.stopScan();
        mDeviceScanner.removeOnScanListener(this);
    }

    @Override
    public void onRefresh() {
        mDeviceScanner.startScan();
    }

    @Override
    public void onDeviceScanStarted() {
        mDeviceFoundInfos.clear();

        updateList();
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onDeviceFoundInfo(SHNDeviceFoundInfo deviceFoundInfo) {
        mDeviceFoundInfos.add(deviceFoundInfo);

        updateList();
    }

    @Override
    public void onDeviceScanFinished() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mConnectDeviceAdapter = new ConnectDeviceAdapter(mDeviceFoundInfos);
        mConnectDeviceAdapter.setOnItemClickListener(new BaseDeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                final SHNDeviceFoundInfo info = mConnectDeviceAdapter.getItem(position);
                BlueLibExampleApplication.get().setSelectedDevice(info.getShnDevice());

                startActivity(new Intent(getActivity(), DeviceDetailActivity.class));
            }

            @Override
            public void onItemLongClick(int position, View itemView) {
                // Nothing to do.
            }
        });
        recyclerView.setAdapter(mConnectDeviceAdapter);
    }

    private void updateList() {
        if (mConnectDeviceAdapter == null) {
            return;
        }
        mConnectDeviceAdapter.notifyDataSetChanged();
    }
}
