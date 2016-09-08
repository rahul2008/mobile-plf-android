/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.example.cdpp.bluelibexampleapp.connect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.cdpp.bluelibexampleapp.detail.DeviceDetailActivity;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.List;

public class ConnectDevicesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ConnectDevicesFragment";

    private static final long SCAN_TIMEOUT_MS = 10000L;

    private SHNCentral mShnCentral;
    private SHNDeviceScanner mDeviceScanner;

    private List<SHNDeviceFoundInfo> mNearbyDevices = new ArrayList<>();

    private ConnectDeviceAdapter mConnectDeviceAdapter;
    private Handler mHandler = new Handler(Looper.myLooper());

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsScanning;

    private final SHNDeviceScanner.SHNDeviceScannerListener mDeviceScannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
            SHNLogger.i(TAG, String.format("Device found: %s", shnDeviceFoundInfo.getDeviceName()));

            mNearbyDevices.add(shnDeviceFoundInfo);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    updateList();
                }
            });
        }

        @Override
        public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onScanStopped();
                }
            });
        }
    };

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

        // Obtain reference to BlueLib instance
        mShnCentral = BlueLibExampleApplication.get().getShnCentral();
        mDeviceScanner = mShnCentral.getShnDeviceScanner();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        scanForDevices();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mDeviceScanner.stopScanning();
    }

    @Override
    public void onRefresh() {
        scanForDevices();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (recyclerView == null) {
            throw new IllegalStateException("RecyclerView not initialized.");
        }
        mConnectDeviceAdapter = new ConnectDeviceAdapter(mNearbyDevices);
        mConnectDeviceAdapter.setOnItemClickListener(new BaseDeviceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View itemView) {
                final SHNDeviceFoundInfo info = mConnectDeviceAdapter.getItem(position);
                BlueLibExampleApplication.get().setSelectedDevice(info.getShnDevice());

                startActivity(new Intent(getActivity(), DeviceDetailActivity.class));
            }

            @Override
            public void onItemLongClick(int position, View itemView) {
                // Nothing to do
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

    private void scanForDevices() {
        if (mIsScanning) {
            return;
        }

        // Initialize device list
        mNearbyDevices.clear();
        updateList();

        // Scan for devices
        mDeviceScanner.startScanning(mDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, SCAN_TIMEOUT_MS);

        onScanStarted();
    }

    private void onScanStarted() {
        mIsScanning = true;
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private void onScanStopped() {
        mIsScanning = false;
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
