package com.example.cdpp.bluelibreferenceapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cdpp.bluelibreferenceapp.DeviceListAdapter;
import com.example.cdpp.bluelibreferenceapp.R;
import com.example.cdpp.bluelibreferenceapp.ReferenceApplication;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;

import java.util.ArrayList;
import java.util.List;

public class AvailableFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "AvailableFragment";

    private static final long SCAN_TIMEOUT_MS = 10000L;

    private SHNCentral mShnCentral;
    private SHNDeviceScanner mDeviceScanner;

    private List<SHNDevice> mFoundDevices = new ArrayList<>();

    private DeviceListAdapter mDeviceListAdapter;
    private Handler mHandler = new Handler(Looper.myLooper());

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean mIsScanning;

    private final SHNDeviceScanner.SHNDeviceScannerListener mDeviceScannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
            Log.i(TAG, String.format("Device found: %s", shnDeviceFoundInfo.getDeviceName()));

            mFoundDevices.add(shnDeviceFoundInfo.getShnDevice());
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
        mDeviceListAdapter = new DeviceListAdapter(mFoundDevices);
        recyclerView.setAdapter(mDeviceListAdapter);
    }

    private void updateList() {
        if (mDeviceListAdapter == null) {
            return;
        }
        mDeviceListAdapter.notifyDataSetChanged();
    }

    private void scanForDevices() {
        if (mIsScanning) {
            return;
        }

        // Initialize device list
        mFoundDevices.clear();
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
