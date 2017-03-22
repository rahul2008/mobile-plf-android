/*
 * Copyright (c) 2016 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

import static com.philips.cdp2.commlib.example.ApplianceActivity.CPPID;

public class FirmwareUpgradeActivity extends AppCompatActivity {
    private static final String TAG = "FirmwareUpgradeActivity";

    private static final int DEFAULT_TIMEOUT_MILLIS = 30000;

    private BleReferenceAppliance bleReferenceAppliance;
    private ProgressBar firmwareUploadProgressBar;
    private ListView firmwareImagesListView;
    private TextView firmwareSearchLocationTextView;
    private TextView statusTextView;
    private ArrayAdapter<File> fwImageAdapter;
    private EditText timeoutEditText;

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
                case R.id.btnCancel:
                    cancelFirmware();
                    break;
                default:
                    Log.d(TAG, "Unknown view clicked");
                    break;
            }
        }
    };

    private FirmwarePortListener firmwarePortListener = new FirmwarePortListener() {
        private String TAG = "FirmwarePortListener";

        @Override
        public void onCheckingProgress(int progress) {
            Log.i(TAG, "onCheckingProgress(" + progress + ")");

            statusTextView.setText(R.string.checking_firmware);
            firmwareUploadProgressBar.setProgress(progress);
        }

        @Override
        public void onDownloadProgress(int progress) {
            Log.i(TAG, "onDownloadProgress(" + progress + ")");

            statusTextView.setText(R.string.uploading_firmware_image);
            firmwareUploadProgressBar.setProgress(progress);
        }

        @Override
        public void onDownloadFailed(final FirmwarePortException exception) {
            Log.i(TAG, "onDownloadFailed(" + exception.getMessage() + ")");

            statusTextView.setText(getString(R.string.uploading_firmware_failed) + exception.getMessage());
        }

        @Override
        public void onDownloadFinished() {
            Log.i(TAG, "onDownloadFinished()");
            statusTextView.setText(R.string.upload_firmware_finished);
        }

        @Override
        public void onFirmwareAvailable(String version) {
            Log.i(TAG, "onFirmwareAvailable(" + version + ")");
            statusTextView.setText(getString(R.string.new_firmware_available) + version);
        }

        @Override
        public void onDeployFailed(FirmwarePortException exception) {
            Log.i(TAG, "onDeployFailed(" + exception.getMessage() + ")");
            statusTextView.setText(getString(R.string.deploy_firmware_failed) + exception.getMessage());
        }

        @Override
        public void onDeployFinished() {
            Log.i(TAG, "onDeployFinished()");
            statusTextView.setText(R.string.firmware_deploy_finished);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmware_upgrade);

        bleReferenceAppliance = (BleReferenceAppliance) ((App) getApplication()).getCommCentral().getApplianceManager().findApplianceByCppId(getIntent().getExtras().getString(CPPID));

        firmwareSearchLocationTextView = (TextView) findViewById(R.id.tvFirmwareSearchLocation);
        statusTextView = (TextView) findViewById(R.id.tvStatus);
        timeoutEditText = (EditText) findViewById(R.id.timeoutEditText);

        if (bleReferenceAppliance == null) {
            finish();
        } else {
            getSupportActionBar().setTitle(bleReferenceAppliance.getNetworkNode().getName());

            findViewById(R.id.btnUploadFirmware).setOnClickListener(clickListener);
            findViewById(R.id.btnDeployFirmware).setOnClickListener(clickListener);
            findViewById(R.id.btnCancel).setOnClickListener(clickListener);
            firmwareUploadProgressBar = (ProgressBar) findViewById(R.id.progressUploadFirmware);
            firmwareUploadProgressBar.setProgress(0);
            firmwareImagesListView = (ListView) findViewById(R.id.lvFirmwareImages);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final File externalFilesDir = getExternalFilesDir(null);
        if (externalFilesDir != null) {
            firmwareSearchLocationTextView.setText(externalFilesDir.getAbsolutePath());
            final File[] files = externalFilesDir.listFiles(upgradeFilesFilter);
            if (files == null) {
                Toast.makeText(FirmwareUpgradeActivity.this, R.string.no_firmware_directory_found, Toast.LENGTH_SHORT).show();
            } else {
                fwImageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, files);
            }
            firmwareImagesListView.setAdapter(fwImageAdapter);
        }
        bleReferenceAppliance.getFirmwarePort().addFirmwarePortListener(firmwarePortListener);
        bleReferenceAppliance.enableCommunication();
    }

    @Override
    protected void onPause() {
        super.onPause();

        cancelFirmware();

        bleReferenceAppliance.getFirmwarePort().removeFirmwarePortListener(firmwarePortListener);
        bleReferenceAppliance.disableCommunication();
    }

    private void uploadSelectedFirmware() {
        final int selectedItemPosition = firmwareImagesListView.getCheckedItemPosition();
        if (selectedItemPosition == ListView.INVALID_POSITION) {
            Toast.makeText(FirmwareUpgradeActivity.this, R.string.select_a_firmware_image, Toast.LENGTH_SHORT).show();
        } else {
            File firmwareFile = fwImageAdapter.getItem(selectedItemPosition);
            final byte[] firmwareBytes = fileToBytes(firmwareFile);

            bleReferenceAppliance.getFirmwarePort().pushLocalFirmware(firmwareBytes, getTimeoutFromUi());
        }
    }

    private byte[] fileToBytes(File firmwareFile) {
        byte[] bytes = new byte[(int) firmwareFile.length()];
        try (InputStream inputStream = new FileInputStream(firmwareFile)) {
            int offset = 0;
            int numRead = 0;
            while ((numRead = inputStream.read(bytes, offset, bytes.length - offset)) > 0) {
                offset += numRead;
            }
        } catch (IOException e) {
            Log.e(TAG, "error accessing fw image", e);
        }
        return bytes;
    }

    private void deployFirmware() {
        bleReferenceAppliance.getFirmwarePort().deployFirmware(getTimeoutFromUi());
    }

    private void cancelFirmware() {
        bleReferenceAppliance.getFirmwarePort().cancel(getTimeoutFromUi());
    }

    private int getTimeoutFromUi() {
        int timeout = DEFAULT_TIMEOUT_MILLIS;

        String timeoutText = timeoutEditText.getText().toString();
        if (timeoutText.length() > 0) {
            timeout = Integer.parseInt(timeoutText) * 1000;
        }

        return timeout;
    }
}
