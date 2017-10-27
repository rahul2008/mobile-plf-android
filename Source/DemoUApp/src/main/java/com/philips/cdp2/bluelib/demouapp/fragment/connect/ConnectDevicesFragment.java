/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.fragment.connect;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.R;
import com.philips.cdp2.bluelib.demouapp.adapter.BaseDeviceAdapter;
import com.philips.cdp2.bluelib.demouapp.adapter.ConnectDeviceAdapter;
import com.philips.cdp2.bluelib.demouapp.fragment.device.DeviceFragment;
import com.philips.cdp2.bluelib.demouapp.util.DeviceScanner;
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
        View rootView = inflater.inflate(R.layout.bll_fragment_connect, container, false);

        // Setup swipe layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.bll_swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Setup nearby devices list
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.bll_nearbyDevices);
        setupRecyclerView(recyclerView);

        // Listen for scan events
        mDeviceScanner = new DeviceScanner(BluelibUapp.get().getDependencies().getShnCentral(), new Handler());
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
                BluelibUapp.get().setSelectedDevice(info.getShnDevice());
                BluelibUapp.get().nextFragment(new DeviceFragment());
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
