/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceApplianceFactory;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;

import static com.philips.platform.baseapp.screens.utility.Constants.DEVICE_DATAPARSING;

public class ConnectivityFragment extends AppFrameworkBaseFragment implements View.OnClickListener, ConnectivityContract.View {
    public static final String TAG = ConnectivityFragment.class.getSimpleName();
    private EditText editText = null;
    private EditText momentValueEditText = null;
    private ProgressDialog dialog = null;
    private CommCentral commCentral;
    private DICommApplianceFactory applianceFactory;
    private TextView connectionState;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private BLEScanDialogFragment bleScanDialogFragment;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1001;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1002;


    /**
     * Presenter object for Connectivity
     */
    private ConnectivityPresenter connectivityPresenter;

    public ConnectivityFragment() {
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_ConnectivityScreen_Menu_Title);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        connectivityPresenter = new ConnectivityPresenter(this, getActivity());
        View rootView = inflater.inflate(R.layout.af_connectivity_fragment, container, false);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ConnectivityUtils.hideSoftKeyboard(getActivity());
                return false;
            }
        });
        editText = (EditText) rootView.findViewById(R.id.measurement_value_editbox);
        momentValueEditText = (EditText) rootView.findViewById(R.id.moment_value_editbox);
        Button btnGetMoment = (Button) rootView.findViewById(R.id.get_momentumvalue_button);
        btnGetMoment.setOnClickListener(this);
        Button btnStartConnectivity = (Button) rootView.findViewById(R.id.start_connectivity_button);
        btnStartConnectivity.setOnClickListener(this);
        connectionState = (TextView) rootView.findViewById(R.id.connectionState);
        setUpCommCentral();
        return rootView;
    }

    /**
     * Setup comm central
     */
    private void setUpCommCentral() {
        // Setup CommCentral
        final BleTransportContext bleTransportContext = new BleTransportContext(getActivity());
        this.applianceFactory = new BleReferenceApplianceFactory(bleTransportContext);

        this.commCentral = new CommCentral(this.applianceFactory, bleTransportContext);
        this.commCentral.getApplianceManager().addApplianceListener(this.applianceListener);
    }

    private final ApplianceManager.ApplianceListener applianceListener = new ApplianceManager.ApplianceListener<BleReferenceAppliance>() {
        @Override
        public void onApplianceFound(@NonNull BleReferenceAppliance foundAppliance) {
            Log.d(TAG, "Device found :" + foundAppliance.getName());
            bleScanDialogFragment.addDevice(foundAppliance);
            Toast.makeText(getActivity(), "Device found name:" + foundAppliance.getName(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onApplianceUpdated(@NonNull BleReferenceAppliance bleReferenceAppliance) {
            // NOOP
        }

        @Override
        public void onApplianceLost(@NonNull BleReferenceAppliance bleReferenceAppliance) {
        }
    };


    @Override
    public void onClick(final View v) {
        ConnectivityUtils.hideSoftKeyboard(getActivity());
        switch (v.getId()) {
            case R.id.start_connectivity_button:
                if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    checkForAccessFineLocation();
                }
                break;
            case R.id.get_momentumvalue_button:
                connectivityPresenter.processMoment(editText.getText().toString());
                break;
            default:
        }
    }


    /**
     * Start scanning nearby devices using given strategy
     */
    private void startDiscovery() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    bleScanDialogFragment = new BLEScanDialogFragment();
                    bleScanDialogFragment.show(getActivity().getSupportFragmentManager(), "BleScanDialog");
                    bleScanDialogFragment.setBLEDialogListener(new BLEScanDialogFragment.BLEScanDialogListener() {
                        @Override
                        public void onDeviceSelected(BleReferenceAppliance bleRefAppliance) {
                            updateConnectionStateText(getString(R.string.RA_Connectivity_Connection_Status_Connected));
                            connectivityPresenter.setUpApplicance(bleRefAppliance);
                            bleRefAppliance.getDeviceMeasurementPort().getPortProperties();
                        }
                    });
                    commCentral.startDiscovery();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (commCentral != null) {
                                commCentral.stopDiscovery();
                                if (bleScanDialogFragment != null) {
                                    bleScanDialogFragment.hideProgressBar();
                                    if (bleScanDialogFragment.getDeviceCount() == 0) {
                                        bleScanDialogFragment.dismiss();
                                        Toast.makeText(getActivity(), R.string.no_device_found, Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                        }
                    }, 30000);
                    updateConnectionStateText(getString(R.string.RA_Connectivity_Connection_Status_Disconnected));
                } catch (MissingPermissionException e) {
                    Log.e(TAG, "Permission missing");
                }
            }
        }, 100);

    }


    @Override
    public void onProcessMomentError(String errorText) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        Toast.makeText(getActivity(), errorText, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProcessMomentSuccess(String momentValue) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if(momentValueEditText!=null) {
            momentValueEditText.setText(momentValue);
        }
    }

    @Override
    public void onProcessMomentProgress(String message) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = ProgressDialog.show(getActivity(), "", message);
    }


    @Override
    public void updateDeviceMeasurementValue(final String measurementvalue) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!TextUtils.isEmpty(measurementvalue)) {
                        editText.setText(measurementvalue);
                    } else {
                        editText.setText(Integer.toString(-1));
                    }
                } catch (Exception e) {
                    AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, DEVICE_DATAPARSING,
                            e.getMessage());              }
            }
        });

    }

    @Override
    public void onDeviceMeasurementError(final Error error, String s) {
        //TODO:Handle device measurement error properly
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), "Error while reading measurement from reference board" + error.getErrorMessage(), Toast.LENGTH_SHORT);
            }
        });

    }

    @Override
    public void updateConnectionStateText(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (connectionState != null) {
                    connectionState.setText(text);
                }
            }
        });

    }


    private void checkForAccessFineLocation() {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            checkForAccessCoarseLocation();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void checkForAccessCoarseLocation() {

        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            startDiscovery();
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
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
                    checkForAccessCoarseLocation();
                } else {
                    Toast.makeText(getActivity(), "Need permission", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay!
                    startDiscovery();
                } else {
                    // permission denied, boo!
                    Toast.makeText(getActivity(), "Need permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        ConnectivityUtils.hideSoftKeyboard(getActivity());
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                checkForAccessFineLocation();
            }
        }
    }
}


