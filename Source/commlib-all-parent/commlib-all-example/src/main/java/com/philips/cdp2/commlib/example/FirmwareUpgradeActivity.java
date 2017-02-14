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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp2.commlib.example.appliance.BleReferenceAppliance;
import com.philips.commlib.core.appliance.Appliance;
import com.philips.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static com.philips.cdp2.commlib.example.ApplianceActivity.CPPID;

public class FirmwareUpgradeActivity extends AppCompatActivity {
    private static final String TAG = "FirmwareUpgradeActivity";
    private BleReferenceAppliance bleReferenceAppliance;
    private ProgressBar firmwareUploadProgressBar;
    private ListView firmwareImagesListView;
    private TextView txtFirmwareSearchLocation;
    private ArrayAdapter<File> fwImageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firmware_upgrade);

        final Set<? extends Appliance> availableAppliances = ((App) getApplication()).getCommCentral().getApplianceManager().getAvailableAppliances();
        for (Appliance appliance : availableAppliances) {
            if (appliance.getNetworkNode().getCppId().equals(getIntent().getExtras().getString(CPPID))) {
                bleReferenceAppliance = (BleReferenceAppliance) appliance;
            }
        }

        txtFirmwareSearchLocation = (TextView) findViewById(R.id.tvFirmwareSearchLocation);

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
            txtFirmwareSearchLocation.setText(externalFilesDir.getAbsolutePath());
            final File[] files = externalFilesDir.listFiles(upgradeFilesFilter);
            fwImageAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, files);
            firmwareImagesListView.setAdapter(fwImageAdapter);
        }

        bleReferenceAppliance.getFirmwarePort().addFirmwarePortListener(firmwarePortListener);
    }

    @Override
    protected void onPause() {
        super.onPause();

        bleReferenceAppliance.getFirmwarePort().removeFirmwarePortListener(firmwarePortListener);
    }

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

    private void uploadSelectedFirmware() {
        final int selectedItemPosition = firmwareImagesListView.getCheckedItemPosition();
        if (selectedItemPosition == ListView.INVALID_POSITION) {
            Toast.makeText(FirmwareUpgradeActivity.this, R.string.select_a_firmware_image, Toast.LENGTH_SHORT).show();
        } else {
            File firmwareFile = fwImageAdapter.getItem(selectedItemPosition);
            byte[] firmwareBytes = fileToBytes(firmwareFile);
            bleReferenceAppliance.getFirmwarePort().pushLocalFirmware(firmwareBytes);
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
        } catch (FileNotFoundException e) {
            Log.e(TAG, "fw image not found", e);
        } catch (IOException e) {
            Log.e(TAG, "error accessing fw image", e);
        }
        return bytes;
    }

    private void deployFirmware() {
        bleReferenceAppliance.getFirmwarePort().deployFirmware();
    }

    private void cancelFirmware() {
        bleReferenceAppliance.getFirmwarePort().cancel();
    }

    private FirmwarePortListener firmwarePortListener = new FirmwarePortListener() {
        private String TAG = "FirmwarePortListener";

        @Override
        public void onProgressUpdated(FirmwarePortProperties.FirmwarePortState state, int progress) {
            Log.i(TAG, "onProgressUpdated(" + state.toString() + ", " + progress + ")");
        }

        @Override
        public void onDownloadFailed(FirmwarePortException exception) {
            Log.i(TAG, "onDownloadFailed(" + exception.getMessage() + ")");
        }

        @Override
        public void onDownloadFinished() {
            Log.i(TAG, "onDownloadFinished()");
        }

        @Override
        public void onFirmwareAvailable(String version) {
            Log.i(TAG, "onFirmwareAvailable(" + version + ")");
        }

        @Override
        public void onDeployFailed(FirmwarePortException exception) {
            Log.i(TAG, "onDeployFailed(" + exception.getMessage() + ")");
        }

        @Override
        public void onDeployFinished() {
            Log.i(TAG, "onDeployFinished()");
        }
    };
}
