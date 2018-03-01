/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.platform.appframework.connectivity.BLEScanDialogFragment;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;

public abstract class AbstractConnectivityBaseFragment extends AbstractAppFrameworkBaseFragment {

    private final String TAG = AbstractConnectivityBaseFragment.class.getSimpleName();
    private Context context;

    protected BLEScanDialogFragment bleScanDialogFragment;

    protected BluetoothAdapter mBluetoothAdapter;

    public static final int REQUEST_ENABLE_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1001;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1002;


    protected static final int STOP_DISCOVERY_TIMEOUT = 30000;

    protected static final int START_DISCOVERY_TIME = 100;

    protected CommCentral mCommCentral;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    protected BluetoothAdapter getBluetoothAdapter() {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        return bluetoothManager.getAdapter();
    }

    /**
     * Setup comm central
     */
    protected CommCentral getCommCentral() {
        // Setup CommCentral
        RALog.i(TAG, "Setup CommCentral ");
        try {
            AppFrameworkApplication appContext = ((AppFrameworkApplication) context.getApplicationContext().getApplicationContext());
            mCommCentral = appContext.getCommCentralInstance();
            mCommCentral.getApplianceManager().addApplianceListener(this.applianceListener);
            RALog.i(TAG,"ConnectivityFragment getCommCentralInstance - " + mCommCentral);
        } catch (TransportUnavailableException e) {
            RALog.d(TAG, "Blutooth hardware unavailable");
        }
        return mCommCentral;
    }

    protected final ApplianceManager.ApplianceListener applianceListener = new ApplianceManager.ApplianceListener() {
        @Override
        public void onApplianceFound(@NonNull Appliance foundAppliance) {
            RALog.d(TAG, "Device found :" + foundAppliance.getName());
            if(foundAppliance instanceof RefAppBleReferenceAppliance) {
                bleScanDialogFragment.addDevice((RefAppBleReferenceAppliance) foundAppliance);
            } else {
                RALog.i(TAG, "Appliance is not a BleReferenceAppliance");
            }
        }

        @Override
        public void onApplianceUpdated(@NonNull Appliance bleReferenceAppliance) {
            // NOOP
        }

        @Override
        public void onApplianceLost(@NonNull Appliance bleReferenceAppliance) {
        }
    };

    protected void launchBluetoothActivity() {
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            checkForAccessFineLocation();
        }
    }

    protected void checkForAccessFineLocation() {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            startDiscovery();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                checkForAccessFineLocation();
            } else {
                Toast.makeText(context, getString(R.string.RA_DLS_enable_bluetooth), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    startDiscovery();
                } else {
                    Toast.makeText(context, "Need permission", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
        }
    }

    protected Runnable stopDiscoveryRunnable = new Runnable() {
        @Override
        public void run() {
            if (mCommCentral != null && isFragmentLive()) {
                mCommCentral.stopDiscovery();
                if (bleScanDialogFragment != null) {
                    bleScanDialogFragment.hideProgressBar();
                    if (bleScanDialogFragment.getDeviceCount() == 0) {
                        bleScanDialogFragment.dismiss();
                        Toast.makeText(context, R.string.RA_no_device_found, Toast.LENGTH_SHORT).show();

                    }

                }
            }
        }
    };

    protected void removeApplianceListener() {
        if (mCommCentral != null && mCommCentral.getApplianceManager() != null) {
            mCommCentral.getApplianceManager().removeApplianceListener(this.applianceListener);
        }
    }

    protected abstract boolean isFragmentLive();

    protected abstract void startDiscovery();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
