package com.example.cdpp.bluelibexampleapp.fragments;

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

import com.example.cdpp.bluelibexampleapp.DeviceFoundInfoListAdapter;
import com.example.cdpp.bluelibexampleapp.R;
import com.example.cdpp.bluelibexampleapp.ReferenceApplication;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.List;

public class AvailableFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "AvailableFragment";

    private static final long SCAN_TIMEOUT_MS = 10000L;

    private SHNCentral mShnCentral;
    private SHNDeviceScanner mDeviceScanner;

    private List<SHNDeviceFoundInfo> mDeviceFoundInfoList = new ArrayList<>();

    private DeviceFoundInfoListAdapter mDeviceFoundInfoListAdapter;
    private Handler mHandler = new Handler(Looper.myLooper());

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsScanning;

    private final SHNDeviceScanner.SHNDeviceScannerListener mDeviceScannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
            SHNLogger.i(TAG, String.format("Device found: %s", shnDeviceFoundInfo.getDeviceName()));

            mDeviceFoundInfoList.add(shnDeviceFoundInfo);
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

    public AvailableFragment() {
    }

    public static AvailableFragment newInstance() {
        return new AvailableFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_available, container, false);

        // Setup swipe layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Setup device list
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.available_devices_list);
        setupRecyclerView(recyclerView);

        // Obtain reference to BlueLib instance
        mShnCentral = ReferenceApplication.get().getShnCentral();
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
        mDeviceFoundInfoListAdapter = new DeviceFoundInfoListAdapter(mDeviceFoundInfoList);
        recyclerView.setAdapter(mDeviceFoundInfoListAdapter);
    }

    private void updateList() {
        if (mDeviceFoundInfoListAdapter == null) {
            return;
        }
        mDeviceFoundInfoListAdapter.notifyDataSetChanged();
    }

    private void scanForDevices() {
        if (mIsScanning) {
            return;
        }

        // Initialize device list
        mDeviceFoundInfoList.clear();
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
