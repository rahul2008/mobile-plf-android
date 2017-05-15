/*
 * Copyright (c) 2016, 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static com.philips.cdp2.commlib.example.ApplianceActivity.CPPID;

public class FirmwareUpgradeActivity extends AppCompatActivity {
    private static final String TAG = "FirmwareUpgradeActivity";

    private static final long DEFAULT_TIMEOUT_MILLIS = 30000L;

    private BleReferenceAppliance bleReferenceAppliance;
    private ProgressBar firmwareUploadProgressBar;
    private ListView firmwareImagesListView;
    private TextView firmwareSearchLocationTextView;
    private TextView stateTextView;
    private TextView versionTextView;
    private TextView statusTextView;
    private EditText timeoutEditText;
    private Button btnUpload;
    private Button btnDeploy;
    private Button btnCancel;
    private ArrayAdapter<File> fwImageAdapter;

    private FilenameFilter upgradeFilesFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".upg");
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnUploadFirmware:
                    uploadSelectedFirmware();
                    break;
                case R.id.btnDeployFirmware:
                    deployFirmware();
                    break;
                case R.id.btnCancelFirmware:
                    cancelFirmware();
                    break;
                default:
                    Log.d(TAG, "Unknown view clicked");
                    break;
            }
        }
    };

    private final DICommPortListener<FirmwarePort> portListener = new DICommPortListener<FirmwarePort>() {
        @Override
        public void onPortUpdate(FirmwarePort port) {
            stateTextView.setText(port.getPortProperties().getState().toString());
            versionTextView.setText(port.getPortProperties().getVersion());
        }

        @Override
        public void onPortError(FirmwarePort port, Error error, String errorData) {
            FirmwarePortProperties properties = port.getPortProperties();
            if (properties != null) {
                stateTextView.setText(port.getPortProperties().getState().toString());
            }
            statusTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.lblErrorMessage), error.getErrorMessage()));
        }
    };

    private final FirmwarePortListener firmwarePortListener = new FirmwarePortListener() {
        private String TAG = "FirmwarePortListener";

        @Override
        public void onCheckingProgress(int progress) {
            Log.i(TAG, "onCheckingProgress(" + progress + ")");

            updateButtons(false, false, true);
            statusTextView.setText(R.string.checking_firmware);
            firmwareUploadProgressBar.setProgress(progress);
        }

        @Override
        public void onDownloadProgress(int progress) {
            Log.i(TAG, "onDownloadProgress(" + progress + ")");

            updateButtons(false, false, true);
            statusTextView.setText(getString(R.string.uploading_firmware_image) + " (" + progress + "%)");
            firmwareUploadProgressBar.setProgress(progress);
        }

        @Override
        public void onDownloadFailed(final FirmwarePortException exception) {
            Log.i(TAG, "onDownloadFailed(" + exception.getMessage() + ")");

            updateButtons(true, false, false);
            statusTextView.setText(String.format("%s%s", getString(R.string.uploading_firmware_failed), exception.getMessage()));
        }

        @Override
        public void onDownloadFinished() {
            Log.i(TAG, "onDownloadFinished()");

            updateButtons(false, true, true);
            statusTextView.setText(R.string.upload_firmware_finished);
        }

        @Override
        public void onFirmwareAvailable(String version) {
            Log.i(TAG, "onFirmwareAvailable(" + version + ")");

            updateButtons(true, false, false);
            statusTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.new_firmware_available), version));
        }

        @Override
        public void onDeployFailed(FirmwarePortException exception) {
            Log.i(TAG, "onDeployFailed(" + exception.getMessage() + ")");

            updateButtons(true, false, false);
            statusTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.deploy_firmware_failed), exception.getMessage()));
        }

        @Override
        public void onDeployFinished() {
            Log.i(TAG, "onDeployFinished()");

            updateButtons(true, false, false);
            statusTextView.setText(R.string.firmware_deploy_finished);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmware_upgrade);

        firmwareSearchLocationTextView = (TextView) findViewById(R.id.tvFirmwareSearchLocation);

        btnUpload = (Button) findViewById(R.id.btnUploadFirmware);
        btnDeploy = (Button) findViewById(R.id.btnDeployFirmware);
        btnCancel = (Button) findViewById(R.id.btnCancelFirmware);

        stateTextView = (TextView) findViewById(R.id.txtFirmwareState);
        versionTextView = (TextView) findViewById(R.id.txtFirmwareVersion);
        statusTextView = (TextView) findViewById(R.id.txtFirmwareStatusMsg);
        timeoutEditText = (EditText) findViewById(R.id.timeoutEditText);

        bleReferenceAppliance = (BleReferenceAppliance) ((App) getApplication()).getCommCentral().getApplianceManager().findApplianceByCppId(getIntent().getExtras().getString(CPPID));

        if (bleReferenceAppliance == null) {
            finish();
        } else {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(bleReferenceAppliance.getNetworkNode().getName());
            }

            btnUpload.setOnClickListener(clickListener);
            btnDeploy.setOnClickListener(clickListener);
            btnCancel.setOnClickListener(clickListener);
            updateButtons(true, false, false);

            firmwareUploadProgressBar = (ProgressBar) findViewById(R.id.progressUploadFirmware);
            firmwareUploadProgressBar.setProgress(0);
            firmwareImagesListView = (ListView) findViewById(R.id.lvFirmwareImages);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        readFirmwareFiles();

        bleReferenceAppliance.getFirmwarePort().addPortListener(portListener);
        bleReferenceAppliance.getFirmwarePort().reloadProperties();

        bleReferenceAppliance.getFirmwarePort().addFirmwarePortListener(firmwarePortListener);
        bleReferenceAppliance.enableCommunication();

        checkUpgradeSupport();
    }

    private void checkUpgradeSupport() {
        final FirmwarePort firmwarePort = bleReferenceAppliance.getFirmwarePort();
        firmwarePort.addPortListener(new DICommPortListener() {
            @Override
            public void onPortUpdate(DICommPort port) {
                firmwarePort.removePortListener(this);
                boolean canUpgrade = firmwarePort.canUpgrade();
                ((TextView) findViewById(R.id.tvCanUpgrade)).setText(canUpgrade ? "Yes" : "No");
            }

            @Override
            public void onPortError(DICommPort port, Error error, String errorData) {
                port.removePortListener(this);
            }
        });
        firmwarePort.reloadProperties();
    }

    @Override
    protected void onPause() {
        super.onPause();

        cancelFirmware();

        bleReferenceAppliance.getFirmwarePort().removePortListener(portListener);
        bleReferenceAppliance.getFirmwarePort().removeFirmwarePortListener(firmwarePortListener);
        bleReferenceAppliance.disableCommunication();
    }

    private void updateButtons(boolean isUploadEnabled, boolean isDeployEnabled, boolean isCancelEnabled) {
        btnUpload.setEnabled(isUploadEnabled);
        btnDeploy.setEnabled(isDeployEnabled);
        btnCancel.setEnabled(isCancelEnabled);
    }

    private void readFirmwareFiles() {
        final File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir == null) {
            return;
        }
        firmwareSearchLocationTextView.setText(externalFilesDir.getAbsolutePath());
        final File[] files = externalFilesDir.listFiles(upgradeFilesFilter);

        if (files == null) {
            Toast.makeText(FirmwareUpgradeActivity.this, R.string.no_firmware_directory_found, Toast.LENGTH_SHORT).show();
        } else {
            fwImageAdapter = new ArrayAdapter<File>(this, android.R.layout.simple_spinner_dropdown_item, files) {
                @NonNull
                @Override
                public View getView(final int position, final View convertView, final @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    File file = getItem(position);

                    if (file != null) {
                        ((TextView) view.findViewById(android.R.id.text1)).setText(file.getName());
                    }

                    return view;
                }
            };
            firmwareImagesListView.setAdapter(fwImageAdapter);
        }
    }

    private void uploadSelectedFirmware() {
        final int selectedItemPosition = firmwareImagesListView.getCheckedItemPosition();

        if (selectedItemPosition == ListView.INVALID_POSITION) {
            Toast.makeText(FirmwareUpgradeActivity.this, R.string.select_a_firmware_image, Toast.LENGTH_SHORT).show();
        } else {
            File firmwareFile = fwImageAdapter.getItem(selectedItemPosition);
            final byte[] firmwareBytes = fileToBytes(firmwareFile);

            bleReferenceAppliance.getFirmwarePort().pushLocalFirmware(firmwareBytes, getTimeoutInMillisFromUi());
        }
    }

    private byte[] fileToBytes(File firmwareFile) {
        byte[] bytes = new byte[(int) firmwareFile.length()];
        try (InputStream inputStream = new FileInputStream(firmwareFile)) {
            int offset = 0;
            int numRead;
            while ((numRead = inputStream.read(bytes, offset, bytes.length - offset)) > 0) {
                offset += numRead;
            }
        } catch (IOException e) {
            Log.e(TAG, "error accessing fw image", e);
        }
        return bytes;
    }

    private void deployFirmware() {
        bleReferenceAppliance.getFirmwarePort().deployFirmware(getTimeoutInMillisFromUi());
    }

    private void cancelFirmware() {
        bleReferenceAppliance.getFirmwarePort().cancel(getTimeoutInMillisFromUi());
    }

    private long getTimeoutInMillisFromUi() {
        final String timeoutText = timeoutEditText.getText().toString();
        return TextUtils.isEmpty(timeoutText) ? DEFAULT_TIMEOUT_MILLIS : Long.parseLong(timeoutText);
    }
}
