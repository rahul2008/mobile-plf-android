/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.appliance.reference.BleReferenceAppliance;
import com.philips.cdp2.demouapp.appliance.reference.ReferenceAppliance;
import com.philips.cdp2.demouapp.util.SelectorDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import static com.philips.cdp2.commlib.demouapp.R.string.cml_no_firmware_directory_found;
import static com.philips.cdp2.commlib.demouapp.R.string.cml_select_a_firmware_image;
import static com.philips.cdp2.demouapp.util.UiUtils.showIndefiniteMessage;

public class FirmwareUpgradeFragment extends Fragment {
    private static final String TAG = "FirmwareUpgradeFragment";

    private static final long DEFAULT_TIMEOUT_MILLIS = 30000L;

    private View rootView;
    private ProgressBar firmwareUploadProgressBar;
    private TextView firmwareSearchLocationTextView;
    private TextView firmwareImageNameTextView;
    private TextView stateTextView;
    private TextView versionTextView;
    private TextView statusTextView;
    private EditText timeoutEditText;

    private Button btnSelect;
    private Button btnUpload;
    private Button btnDeploy;
    private Button btnCancel;

    private ArrayAdapter<File> fwImageAdapter;

    private int selectedFirmwareIndex = ListView.INVALID_POSITION;

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

            if (viewId == R.id.cml_btnSelectFirmware) {
                selectFirmware();
            } else if (viewId == R.id.cml_btnUploadFirmware) {
                uploadSelectedFirmware();
            } else if (viewId == R.id.cml_btnDeployFirmware) {
                deployFirmware();
            } else if (viewId == R.id.cml_btnCancelFirmware) {
                cancelFirmware();
            } else {
                DICommLog.w(TAG, "Unknown view clicked.");
            }
        }
    };
    private final DICommPortListener<FirmwarePort> portListener = new DICommPortListener<FirmwarePort>() {
        @Override
        public void onPortUpdate(FirmwarePort port) {
            if (isAdded()) {
                FirmwarePortProperties properties = port.getPortProperties();
                if (properties == null) {
                    return;
                }
                stateTextView.setText(properties.getState().toString());
                versionTextView.setText(port.getPortProperties().getVersion());
            }
        }

        @Override
        public void onPortError(FirmwarePort port, Error error, String errorData) {
            if (isAdded()) {
                stateTextView.setText(getString(R.string.cml_n_a));
                statusTextView.setText(String.format(Locale.US, "Error: %s", error.getErrorMessage()));
            }
        }
    };
    private final FirmwarePortListener firmwarePortListener = new FirmwarePortListener() {
        private String TAG = "FirmwarePortListener";

        @Override
        public void onCheckingProgress(int progress) {
            Log.i(TAG, "onCheckingProgress(" + progress + ")");
            if (isAdded()) {
                updateButtons(false, false, true);
                statusTextView.setText(R.string.cml_checking_firmware);
                firmwareUploadProgressBar.setProgress(progress);
            }
        }

        @Override
        public void onDownloadProgress(int progress) {
            Log.i(TAG, "onDownloadProgress(" + progress + ")");

            if (isAdded()) {
                updateButtons(false, false, true);
                statusTextView.setText(getString(R.string.cml_uploading_firmware_image) + " (" + progress + "%)");
                firmwareUploadProgressBar.setProgress(progress);
            }
        }

        @Override
        public void onDownloadFailed(final FirmwarePortException exception) {
            Log.i(TAG, "onDownloadFailed(" + exception.getMessage() + ")");

            if (isAdded()) {
                updateButtons(true, false, false);
                statusTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.cml_uploading_firmware_failed), exception.getMessage()));
            }
        }

        @Override
        public void onDownloadFinished() {
            Log.i(TAG, "onDownloadFinished()");

            if (isAdded()) {
                updateButtons(false, true, true);
                statusTextView.setText(R.string.cml_upload_firmware_finished);
            }
        }

        @Override
        public void onFirmwareAvailable(String version) {
            Log.i(TAG, "onFirmwareAvailable(" + version + ")");

            if (isAdded()) {
                updateButtons(true, false, false);
                statusTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.cml_new_firmware_available), version));
            }
        }

        @Override
        public void onDeployFailed(FirmwarePortException exception) {
            Log.i(TAG, "onDeployFailed(" + exception.getMessage() + ")");

            if (isAdded()) {
                updateButtons(true, false, false);
                statusTextView.setText(String.format(Locale.US, "%s%s", getString(R.string.cml_deploy_firmware_failed), exception.getMessage()));
            }
        }

        @Override
        public void onDeployFinished() {
            Log.i(TAG, "onDeployFinished()");

            if (isAdded()) {
                updateButtons(true, false, false);
                statusTextView.setText(R.string.cml_firmware_deploy_finished);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cml_fragment_firmware_upgrade, container, false);

        firmwareSearchLocationTextView = (TextView) rootView.findViewById(R.id.cml_tvFirmwareSearchLocation);
        firmwareImageNameTextView = (TextView) rootView.findViewById(R.id.cml_tvFirmwareImageName);

        btnSelect = (Button) rootView.findViewById(R.id.cml_btnSelectFirmware);
        btnUpload = (Button) rootView.findViewById(R.id.cml_btnUploadFirmware);
        btnDeploy = (Button) rootView.findViewById(R.id.cml_btnDeployFirmware);
        btnCancel = (Button) rootView.findViewById(R.id.cml_btnCancelFirmware);

        stateTextView = (TextView) rootView.findViewById(R.id.cml_txtFirmwareState);
        versionTextView = (TextView) rootView.findViewById(R.id.cml_txtFirmwareVersion);
        statusTextView = (TextView) rootView.findViewById(R.id.cml_txtFirmwareStatusMsg);
        timeoutEditText = (EditText) rootView.findViewById(R.id.cml_timeoutEditText);

        btnSelect.setOnClickListener(clickListener);
        btnUpload.setOnClickListener(clickListener);
        btnDeploy.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
        updateButtons(true, false, false);

        firmwareUploadProgressBar = (ProgressBar) rootView.findViewById(R.id.cml_progressUploadFirmware);
        firmwareUploadProgressBar.setProgress(0);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        Appliance appliance = CurrentApplianceManager.getInstance().getCurrentAppliance();
        if (appliance == null || !(appliance instanceof ReferenceAppliance)) {
            getFragmentManager().popBackStack();
            return;
        }
        currentAppliance = (ReferenceAppliance) appliance;

        readFirmwareFiles();

        currentAppliance.getFirmwarePort().addPortListener(portListener);
        currentAppliance.getFirmwarePort().reloadProperties();

        currentAppliance.getFirmwarePort().addFirmwarePortListener(firmwarePortListener);

        checkUpgradeSupport();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (currentAppliance != null) {
            currentAppliance.getFirmwarePort().removePortListener(portListener);
            currentAppliance.getFirmwarePort().removeFirmwarePortListener(firmwarePortListener);
        }
    }

    private void checkUpgradeSupport() {
        final FirmwarePort firmwarePort = currentAppliance.getFirmwarePort();
        firmwarePort.addPortListener(new DICommPortListener<FirmwarePort>() {
            @Override
            public void onPortUpdate(FirmwarePort port) {
                port.removePortListener(this);

                boolean canUpgrade = firmwarePort.canUpgrade();
                if (isAdded()) {
                    ((TextView) getActivity().findViewById(R.id.cml_tvCanUpgrade)).setText(canUpgrade ? "Yes" : "No");
                }
            }

            @Override
            public void onPortError(FirmwarePort port, Error error, String errorData) {
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
            showIndefiniteMessage(getActivity(), rootView, getString(cml_no_firmware_directory_found));
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
        }
    }

    private void selectFirmware() {
        SelectorDialog dialog = SelectorDialog.newInstance(R.string.cml_choose_firmware_image, fwImageAdapter, new SelectorDialog.OnDialogSelectorListener() {
            @Override
            public void onSelectedOption(int index) {
                selectedFirmwareIndex = index;
                File firmwareImage = fwImageAdapter.getItem(selectedFirmwareIndex);
                if (firmwareImage != null) {
                    firmwareImageNameTextView.setText(firmwareImage.getName());
                }
            }
        });

        dialog.show(getActivity().getSupportFragmentManager(), null);
    }

    private void uploadSelectedFirmware() {
        if (selectedFirmwareIndex == ListView.INVALID_POSITION) {
            showIndefiniteMessage(getActivity(), rootView, getString(cml_select_a_firmware_image));
        } else {
            File firmwareFile = fwImageAdapter.getItem(selectedFirmwareIndex);
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
