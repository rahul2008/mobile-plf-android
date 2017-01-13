/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.philips.cdp.dicommclient.appliance.DICommApplianceFactory;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.FirmwarePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.pluginreferenceboard.DeviceDefinitionInfoReferenceBoard;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp2.commlib.ble.context.BleTransportContext;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.appliance.ApplianceManager;
import com.philips.cdp2.commlib.core.exception.MissingPermissionException;
import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityFirmwareUpdateDiComm;
import com.philips.pins.shinelib.capabilities.SHNCapabilityBattery;
import com.philips.pins.shinelib.dicommsupport.DiCommChannel;
import com.philips.pins.shinelib.dicommsupport.DiCommPort;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.wrappers.SHNCapabilityFirmwareUpdateWrapper;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceApplianceFactory;
import com.philips.platform.appframework.connectivity.appliance.DeviceMeasurementPort;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Field;
import java.util.Map;

public class ConnectivityFragment extends AppFrameworkBaseFragment implements View.OnClickListener, ConnectivityContract.View {
    public static final String TAG = ConnectivityFragment.class.getSimpleName();
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 0x1;
    private EditText editText = null;
    private EditText momentValueEditText = null;
    private EditText deviceID = null;
    private String editTextValue;
    private String accessTokenValue;
    private TextView connectionState;
    private SHNCentral shnCentral;
    private SHNDevice shineDevice;
    private DicommDeviceMeasurementPort measPort = null;
    private DeviceDefinitionInfoReferenceBoard deviceDefinitionInfoReferenceBoard;
    private User user;
    private ProgressDialog dialog = null;
    private TextView dataCoreErrorText;
    private int measValue;
    private CommCentral commCentral;
    private DICommApplianceFactory applianceFactory;
    private BleReferenceAppliance bleReferenceAppliance;
    private TextView txtState;
    private TextView txtResult;
    private Runnable permissionCallback;

    /**
     * Presenter object for Connectivity
     */
    private ConnectivityPresenter connectivityPresenter;

    public ConnectivityFragment() {
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.app_connectivity_title);
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        connectivityPresenter = new ConnectivityPresenter(this, getActivity());
        View rootView = inflater.inflate(R.layout.af_connectivity_fragment, container, false);
        editText = (EditText) rootView.findViewById(R.id.measurement_value_editbox);
        deviceID = (EditText) rootView.findViewById(R.id.reference_device_id_editbox);
        momentValueEditText = (EditText) rootView.findViewById(R.id.moment_value_editbox);
        dataCoreErrorText = (TextView) rootView.findViewById(R.id.datacre_error_text);
        Button btnGetMoment = (Button) rootView.findViewById(R.id.get_momentumvalue_button);
        txtState = (TextView) rootView.findViewById(R.id.txtState);
        txtResult = (TextView) rootView.findViewById(R.id.txtResult);

        btnGetMoment.setOnClickListener(this);
        Button btnStartConnectivity = (Button) rootView.findViewById(R.id.start_connectivity_button);
        btnStartConnectivity.setOnClickListener(this);
        user = new User(getActivity().getApplicationContext());
        accessTokenValue = user.getHsdpAccessToken();
        connectionState = (TextView) rootView.findViewById(R.id.connectionState);
        setUpCommCentral();
//        setUpShnCentral();
        return rootView;
    }

    private void setUpShnCentral() {
        try {
            Handler mainHandler = new Handler(getActivity().getMainLooper());
            shnCentral = new SHNCentral.Builder(getActivity().getApplicationContext()).setHandler(mainHandler).create();
            deviceDefinitionInfoReferenceBoard = new DeviceDefinitionInfoReferenceBoard();
            shnCentral.registerDeviceDefinition(deviceDefinitionInfoReferenceBoard);
        } catch (SHNBluetoothHardwareUnavailableException e) {
            e.printStackTrace();
        }
    }

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
            bleReferenceAppliance = foundAppliance;
            Log.d("test", "Device found :" + foundAppliance.getName());
            Toast.makeText(getActivity(), "Device found name:" + foundAppliance.getName(), Toast.LENGTH_SHORT).show();
            setupAppliance(bleReferenceAppliance);

            // Perform request on port
           bleReferenceAppliance.getDeviceMeasurementPort().getPortProperties();
        }

        @Override
        public void onApplianceUpdated(@NonNull BleReferenceAppliance bleReferenceAppliance) {
            // NOOP
        }
    };

    private void updateStateAndResult(final String state, final String result) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                txtState.setText(state);
                txtResult.setText(result);
            }
        });
    }

    private SHNCapabilityBattery setListenerAndReturnSHNCapabilityBattery() {
        SHNCapabilityBattery shnCapabilityBattery = getSHNCapabilityBattery();
        if (shnCapabilityBattery != null) {
            shnCapabilityBattery.setSetSHNCapabilityBatteryListener(shnCapabilityBatteryListener);
        }
        return shnCapabilityBattery;
    }

    private SHNCapabilityBattery getSHNCapabilityBattery() {
        SHNDevice connectedDevice = shineDevice;
        if (connectedDevice != null && connectedDevice.getSupportedCapabilityTypes().contains(SHNCapabilityType.BATTERY)) {
            return ((SHNCapabilityBattery) connectedDevice.getCapabilityForType(SHNCapabilityType.BATTERY));
        }
        return null;
    }

    SHNCapabilityBattery.SHNCapabilityBatteryListener shnCapabilityBatteryListener = new SHNCapabilityBattery.SHNCapabilityBatteryListener() {
        @Override
        public void onBatteryLevelChanged(int level) {
        }
    };

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.start_connectivity_button:
//                startConnectivity();
                startDiscovery();
                break;
            case R.id.get_momentumvalue_button:
                editTextValue = editText.getText().toString();
                if (accessTokenValue == null || RegistrationConfiguration.getInstance().getHSDPInfo() == null || !ConnectivityUtils.isNetworkAvailable(getActivity())) {
                    dataCoreErrorText.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Datacore is not reachable", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    dataCoreErrorText.setVisibility(View.GONE);
                }
                processMoment(user, editTextValue);
                break;
            default:
        }
    }

    private void startDiscovery() {
        try {
            this.commCentral.startDiscovery();
            updateStateAndResult(getString(R.string.lblStateDiscovering), getString(R.string.lblResultNotApplicable));
        } catch (MissingPermissionException e) {
            updateStateAndResult(getString(R.string.lblStatePermissionError), getString(R.string.lblResultDiscoveryFailed));
            acquirePermission(new Runnable() {
                @Override
                public void run() {
                    startDiscovery();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new Handler().post(this.permissionCallback);
                } else {
                    updateStateAndResult(getString(R.string.lblStateError), getString(R.string.lblResultNotApplicable));
                }
            }
        }
    }

    private void acquirePermission(@NonNull Runnable permissionCallback) {
        this.permissionCallback = permissionCallback;

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    private void startConnectivity() {
        SHNDevice.SHNDeviceListener shineDeviceListener = new SHNDevice.SHNDeviceListener() {
            @Override
            public void onStateUpdated(SHNDevice shnDevice) {
                connectionState.setText(shnDevice.getState().toString());
                if (shnDevice.getState().toString() == "Connected") {
                    getMeasurementValue();
                }
            }

            @Override
            public void onFailedToConnect(SHNDevice shnDevice, SHNResult shnResult) {
                connectionState.setText(shnDevice.getState().toString() + shnResult);
            }

            @Override
            public void onReadRSSI(final int rssi) {

            }
        };

        shineDevice = shnCentral.createSHNDeviceForAddressAndDefinition(deviceID.getText().toString(), deviceDefinitionInfoReferenceBoard);
        shineDevice.registerSHNDeviceListener(shineDeviceListener);
        shineDevice.connect();
        connectionState.setText(shineDevice.getState().toString());
    }

    private void getMeasurementValue() {
        try {
            SHNCapabilityFirmwareUpdateWrapper shnCapabilityFirmwareUpdate = (SHNCapabilityFirmwareUpdateWrapper) shineDevice.getCapabilityForType(SHNCapabilityType.FIRMWARE_UPDATE);
            Field fui = SHNCapabilityFirmwareUpdateWrapper.class.getDeclaredField("wrappedCapability");
            fui.setAccessible(true);
            Field firmwarePort = CapabilityFirmwareUpdateDiComm.class.getDeclaredField("firmwareDiCommPort");
            firmwarePort.setAccessible(true);
            DiCommPort port = (DiCommPort) firmwarePort.get(fui.get(shnCapabilityFirmwareUpdate));
            Field channel = DiCommPort.class.getDeclaredField("diCommChannel");
            channel.setAccessible(true);
            DiCommChannel diCommChannel = (DiCommChannel) channel.get(port);
            measPort = new DicommDeviceMeasurementPort(new Handler());
            diCommChannel.addPort(measPort);
            if (measPort != null) {
                measPort.reloadProperties(new SHNMapResultListener<String, Object>() {
                    @Override
                    public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                        measValue = measPort.GetMeasurementValue();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                editText.setText(String.valueOf((measValue)));
                            }
                        });
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processMoment(final User user, String momentValue) {
        Log.i(TAG, "Moment value" + momentValue);
        showProgressDialog("Posting data in datacore, Please wait...");
        connectivityPresenter.postMoment(user, momentValue);
    }

    @Override
    public void updateUIOnPostMomentSuccess(final String momentId) {
        Log.i(TAG, "Moment Id" + momentId);
        showProgressDialog("Getting moment from datacore, Please wait...");
        connectivityPresenter.getMoment(user, momentId);
    }

    @Override
    public void updateUIOnPostMomentError(final VolleyError volleyError) {
        dismissProgressDialog();
        Log.d(TAG, "Error while setting moment value");
    }

    @Override
    public void updateUIOnGetMomentSuccess(final String momentValue) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                momentValueEditText.setText(momentValue);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void updateUIOnGetMomentError(final VolleyError volleyError) {
        dismissProgressDialog();
        Log.d(TAG, "Error while getting moment value");
    }

    public void showProgressDialog(String text) {
        dismissProgressDialog();
        dialog = ProgressDialog.show(getActivity(), "", text);
    }

    public void dismissProgressDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void setupAppliance(@NonNull BleReferenceAppliance appliance) {
        if (appliance == null) {
            throw new IllegalArgumentException("Cannot create bleReferenceAppliance for provided NetworkNode.");
        }

        // Setup firmware port
        appliance.getFirmwarePort().addPortListener(new DICommPortListener<FirmwarePort>() {
            @Override
            public void onPortUpdate(FirmwarePort firmwarePort) {
                updateStateAndResult(getString(R.string.lblStateDone), getString(R.string.lblResultGetPropsSuccess, firmwarePort.getPortProperties().getVersion()));
            }

            @Override
            public void onPortError(FirmwarePort diCommPort, Error error, String s) {
                updateStateAndResult(getString(R.string.lblStateDone), getString(R.string.lblResultGetPropsFailed, s));
            }
        });

        // Setup time port
        appliance.getDeviceMeasurementPort().addPortListener(new DICommPortListener<DeviceMeasurementPort>() {

            @Override
            public void onPortUpdate(DeviceMeasurementPort deviceMeasurementPort) {
                Toast.makeText(getActivity(), "Inside port update", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Port properties"+deviceMeasurementPort.getPortProperties(), Toast.LENGTH_SHORT).show();
                Log.d("test","Posrt properties"+deviceMeasurementPort.getPortProperties().measurementvalue);
//                DateTime dateTime = new DateTime(timePort.getPortProperties().);
//                DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_OUTPUT_FORMAT);
//
//                updateStateAndResult(getString(R.string.lblStateDone), dateTime.toString(dateTimeFormatter));
            }

            @Override
            public void onPortError(DeviceMeasurementPort deviceMeasurementPort, Error error, final String s) {
                updateStateAndResult(getString(R.string.lblStateDone), getString(R.string.lblResultPortError, s));
            }
        });
    }
}


