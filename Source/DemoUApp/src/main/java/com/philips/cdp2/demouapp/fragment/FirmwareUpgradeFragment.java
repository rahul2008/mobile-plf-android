/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.appliance.reference.ReferenceAppliance;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class FirmwareUpgradeFragment extends Fragment {
    private static final String TAG = "FirmwareUpgradeFragment";

    private static final long DEFAULT_TIMEOUT_MILLIS = 30000L;

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

    private ReferenceAppliance currentAppliance;

    private FilenameFilter upgradeFilesFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.toLowerCase().endsWith(".upg");
        }
    };

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            int viewId = view.getId();

            if (viewId == R.id.btnUploadFirmware) {
                uploadSelectedFirmware();
            } else if (viewId == R.id.btnDeployFirmware) {
                deployFirmware();
            } else if (viewId == R.id.btnCancelFirmware) {
                cancelFirmware();
            } else {
                DICommLog.w(TAG, "Unknown view clicked.");
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
            stateTextView.setText(port.getPortProperties().getState().toString());
            statusTextView.setText(String.format(Locale.US, "Error: %s", error.getErrorMessage()));
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
            statusTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.uploading_firmware_failed), exception.getMessage()));
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_firmware_upgrade, container, false);

        firmwareSearchLocationTextView = (TextView) rootview.findViewById(R.id.tvFirmwareSearchLocation);

        btnUpload = (Button) rootview.findViewById(R.id.btnUploadFirmware);
        btnDeploy = (Button) rootview.findViewById(R.id.btnDeployFirmware);
        btnCancel = (Button) rootview.findViewById(R.id.btnCancelFirmware);

        stateTextView = (TextView) rootview.findViewById(R.id.txtFirmwareState);
        versionTextView = (TextView) rootview.findViewById(R.id.txtFirmwareVersion);
        statusTextView = (TextView) rootview.findViewById(R.id.txtFirmwareStatusMsg);
        timeoutEditText = (EditText) rootview.findViewById(R.id.timeoutEditText);

        currentAppliance = (ReferenceAppliance) CurrentApplianceManager.getInstance().getCurrentAppliance();

        if (currentAppliance == null) {
            getFragmentManager().popBackStack();
        } else {
            final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(currentAppliance.getNetworkNode().getName());
            }

            btnUpload.setOnClickListener(clickListener);
            btnDeploy.setOnClickListener(clickListener);
            btnCancel.setOnClickListener(clickListener);
            updateButtons(true, false, false);

            firmwareUploadProgressBar = (ProgressBar) rootview.findViewById(R.id.progressUploadFirmware);
            firmwareUploadProgressBar.setProgress(0);
            firmwareImagesListView = (ListView) rootview.findViewById(R.id.lvFirmwareImages);
        }

        return rootview;
    }


    @Override
    public void onResume() {
        super.onResume();

        readFirmwareFiles();

        currentAppliance.getFirmwarePort().addPortListener(portListener);
        currentAppliance.getFirmwarePort().reloadProperties();

        currentAppliance.getFirmwarePort().addFirmwarePortListener(firmwarePortListener);
        currentAppliance.enableCommunication();

        checkUpgradeSupport();
    }

    private void checkUpgradeSupport() {
        final FirmwarePort firmwarePort = currentAppliance.getFirmwarePort();
        firmwarePort.addPortListener(new DICommPortListener() {
            @Override
            public void onPortUpdate(DICommPort port) {
                firmwarePort.removePortListener(this);
                boolean canUpgrade = firmwarePort.canUpgrade();
                if (isAdded()) {
                    ((TextView) getActivity().findViewById(R.id.tvCanUpgrade)).setText(canUpgrade ? "Yes" : "No");
                }
            }

            @Override
            public void onPortError(DICommPort port, Error error, String errorData) {
                port.removePortListener(this);
            }
        });
        firmwarePort.reloadProperties();
    }

    @Override
    public void onPause() {
        super.onPause();

        cancelFirmware();

        currentAppliance.getFirmwarePort().removePortListener(portListener);
        currentAppliance.getFirmwarePort().removeFirmwarePortListener(firmwarePortListener);
        currentAppliance.disableCommunication();
    }

    private void updateButtons(boolean isUploadEnabled, boolean isDeployEnabled, boolean isCancelEnabled) {
        btnUpload.setEnabled(isUploadEnabled);
        btnDeploy.setEnabled(isDeployEnabled);
        btnCancel.setEnabled(isCancelEnabled);
    }

    private void readFirmwareFiles() {
        final File externalFilesDir = getActivity().getExternalFilesDir(null);
        if (externalFilesDir == null) {
            return;
        }
        firmwareSearchLocationTextView.setText(externalFilesDir.getAbsolutePath());
        final File[] files = externalFilesDir.listFiles(upgradeFilesFilter);

        if (files == null) {
            Toast.makeText(getActivity(), R.string.no_firmware_directory_found, Toast.LENGTH_SHORT).show();
        } else {
            fwImageAdapter = new ArrayAdapter<File>(getActivity(), android.R.layout.simple_spinner_dropdown_item, files) {
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
            Toast.makeText(getActivity(), R.string.select_a_firmware_image, Toast.LENGTH_SHORT).show();
        } else {
            File firmwareFile = fwImageAdapter.getItem(selectedItemPosition);
            final byte[] firmwareBytes = fileToBytes(firmwareFile);

            currentAppliance.getFirmwarePort().pushLocalFirmware(firmwareBytes, getTimeoutInMillisFromUi());
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
        currentAppliance.getFirmwarePort().deployFirmware(getTimeoutInMillisFromUi());
    }

    private void cancelFirmware() {
        currentAppliance.getFirmwarePort().cancel(getTimeoutInMillisFromUi());
    }

    private long getTimeoutInMillisFromUi() {
        final String timeoutText = timeoutEditText.getText().toString();
        return TextUtils.isEmpty(timeoutText) ? DEFAULT_TIMEOUT_MILLIS : Long.parseLong(timeoutText);
    }
}
