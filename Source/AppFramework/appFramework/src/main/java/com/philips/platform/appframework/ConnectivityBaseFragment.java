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

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;
import com.philips.platform.appframework.connectivity.BLEScanDialogFragment;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceApplianceFactory;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.utility.RALog;

public abstract class ConnectivityBaseFragment extends AbstractAppFrameworkBaseFragment {

    private final String TAG = ConnectivityBaseFragment.class.getSimpleName();
    private Context context;


    protected BLEScanDialogFragment bleScanDialogFragment;

    protected BluetoothAdapter mBluetoothAdapter;

    public static final int REQUEST_ENABLE_BT = 1;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1001;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1002;

    protected CommCentral mCommCentral;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    protected BluetoothAdapter getBluetoothAdapter(){
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        return bluetoothManager.getAdapter();
    }

    /**
     * Setup comm central
     */
    protected CommCentral getCommCentral(ConnectivityDeviceType deviceType) {
        // Setup CommCentral
        RALog.i(TAG, "Setup CommCentral ");
        CommCentral commCentral=null;
        try {
            final BleTransportContext bleTransportContext = new BleTransportContext(getActivity());
            DICommApplianceFactory<BleReferenceAppliance> applianceFactory = new BleReferenceApplianceFactory(bleTransportContext, deviceType);

            commCentral = new CommCentral(applianceFactory, bleTransportContext);
            commCentral.getApplianceManager().addApplianceListener(this.applianceListener);
        } catch (TransportUnavailableException e) {
            RALog.d(TAG,"Blutooth hardware unavailable");
        }
        return commCentral;
    }

    protected final ApplianceManager.ApplianceListener<BleReferenceAppliance> applianceListener = new ApplianceManager.ApplianceListener<BleReferenceAppliance>() {
        @Override
        public void onApplianceFound(@NonNull BleReferenceAppliance foundAppliance) {
            RALog.d(TAG, "Device found :" + foundAppliance.getName());
            bleScanDialogFragment.addDevice(foundAppliance);
            Toast.makeText(context, "Device found name:" + foundAppliance.getName(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onApplianceUpdated(@NonNull BleReferenceAppliance bleReferenceAppliance) {
            // NOOP
        }

        @Override
        public void onApplianceLost(@NonNull BleReferenceAppliance bleReferenceAppliance) {
        }
    };

    protected void launchBlutoothActivity() {
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
            }else{
                Toast.makeText(context,"Please enable bluetooth",Toast.LENGTH_SHORT).show();
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

    protected abstract boolean isFragmentLive();

    protected abstract void startDiscovery();
}
