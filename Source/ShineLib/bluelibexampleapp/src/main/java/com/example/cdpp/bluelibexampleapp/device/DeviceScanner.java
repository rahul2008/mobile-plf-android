/*
 * Copyright © 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.example.cdpp.bluelibexampleapp.device;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceFoundInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.HashSet;
import java.util.Set;

public class DeviceScanner {

    private static final String TAG = "DeviceScanner";

    public interface OnDeviceScanListener {
        void onDeviceScanStarted();

        void onDeviceFoundInfo(SHNDeviceFoundInfo deviceFoundInfo);

        void onDeviceScanFinished();
    }

    private ScanTask mScanTask;

    private boolean mIsScanning;
    private SHNDeviceScanner mDeviceScanner;
    private Set<OnDeviceScanListener> mListeners = new HashSet<>();

    public DeviceScanner(@NonNull SHNCentral shnCentral) {
        mDeviceScanner = shnCentral.getShnDeviceScanner();
    }

    public void startScan() {
        mScanTask = new ScanTask();
        mScanTask.execute();
    }

    public void stopScan() {
        if (mScanTask == null) {
            return;
        }
        mScanTask.cancel(true);
    }

    public void addOnScanListener(OnDeviceScanListener listener) {
        if (listener == null) {
            throw new NullPointerException("OnDeviceScanListener is null.");
        }
        mListeners.add(listener);
    }

    public void removeOnScanListener(OnDeviceScanListener listener) {
        mListeners.remove(listener);
    }

    private void notifyOnScanStarted() {
        for (OnDeviceScanListener listener : mListeners) {
            listener.onDeviceScanStarted();
        }
    }

    private void notifyOnDeviceFoundInfo(SHNDeviceFoundInfo deviceFoundInfo) {
        for (OnDeviceScanListener listener : mListeners) {
            listener.onDeviceFoundInfo(deviceFoundInfo);
        }
    }

    private void notifyOnScanFinished() {
        for (OnDeviceScanListener listener : mListeners) {
            listener.onDeviceScanFinished();
        }
    }

    private class ScanTask extends AsyncTask<Long, Void, Void> implements SHNDeviceScanner.SHNDeviceScannerListener {

        private static final long SCAN_TIMEOUT_MS = 10000L;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            notifyOnScanStarted();
        }

        @Override
        protected Void doInBackground(Long... params) {
            SHNLogger.d(TAG, "doInBackground");

            if (mIsScanning) {
                return null;
            }
            mIsScanning = true;

            // If a timeout value is provided use that instead of the default
            long scanTimeoutMs = params.length == 1 ? params[0] : SCAN_TIMEOUT_MS;

            mDeviceScanner.startScanning(this, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesNotAllowed, scanTimeoutMs);
            notifyOnScanStarted();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            notifyOnScanFinished();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            mDeviceScanner.stopScanning();
        }

        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDeviceFoundInfo shnDeviceFoundInfo) {
            SHNLogger.i(TAG, String.format("Device found: %s", shnDeviceFoundInfo.getDeviceName()));

            notifyOnDeviceFoundInfo(shnDeviceFoundInfo);
        }

        @Override
        public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
            mIsScanning = false;

            notifyOnScanFinished();
        }
    }
}
