/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
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
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.appliance.reference.ReferenceAppliance;
import com.philips.cdp2.demouapp.util.SelectorDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.philips.cdp2.commlib.demouapp.R.string.cml_no_firmware_directory_found;
import static com.philips.cdp2.demouapp.fragment.ApplianceFragmentFactory.APPLIANCE_KEY;
import static com.philips.cdp2.demouapp.util.UiUtils.showIndefiniteMessage;
import static com.philips.cdp2.demouapp.util.UiUtils.showMessage;
import static java.lang.System.currentTimeMillis;

public class FirmwareUpgradeFragment extends Fragment {
    private static final String TAG = "FirmwareUpgradeFragment";

    private static final long DEFAULT_TIMEOUT_MILLIS = 30000L;

    private FirmwarePort firmwarePort;

    private View rootView;
    private ProgressBar firmwareUploadProgressBar;
    private TextView firmwareSearchLocationTextView;
    private TextView firmwareImageNameTextView;
    private TextView stateTextView;
    private TextView versionTextView;
    private TextView statusTextView;
    private TextView canUpgradeTextView;
    private EditText timeoutEditText;
    private Button btnUpload;
    private Button btnDeploy;
    private Button btnCancel;

    private ArrayAdapter<File> fwImageAdapter;

    private int selectedFirmwareIndex = ListView.INVALID_POSITION;

    private long startTimeMillis;
    private int firmwareSizeInBytes;

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

            if (viewId == R.id.cml_btnCheckUpgrade) {
                firmwarePort.reloadProperties();
            } else if (viewId == R.id.cml_btnSelectFirmware) {
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
            updateTextViews(null);
        }

        @Override
        public void onPortError(FirmwarePort port, Error error, String errorData) {
            updateTextViews(getString( R.string.cml_lblResultPortError, error.getErrorMessage()));
        }
    };

    private final FirmwarePortListener firmwarePortListener = new FirmwarePortListener() {
        private String TAG = "FirmwarePortListener";

        @Override
        public void onCheckingProgress(int progress) {
            Log.i(TAG, "onCheckingProgress(" + progress + ")");

            updateButtons(false, false, true);
            updateTextViews(getString(R.string.cml_checking_firmware));

            firmwareUploadProgressBar.setProgress(progress);
        }

        @Override
        public void onDownloadProgress(int progress) {
            Log.i(TAG, "onDownloadProgress(" + progress + ")");

            updateButtons(false, false, true);
            int throughput = calculateThroughputInBytesPerSecond(progress);
            updateTextViews(String.format(Locale.US, "%s: %d%% (%d B/s)", getString(R.string.cml_uploading_firmware_image), progress, throughput));

            firmwareUploadProgressBar.setProgress(progress);
        }

        @Override
        public void onDownloadFailed(final FirmwarePortException exception) {
            Log.i(TAG, "onDownloadFailed(" + exception.getMessage() + ")");

            updateButtons(true, false, false);
            updateTextViews(String.format(Locale.US, "%s %s", getString(R.string.cml_uploading_firmware_failed), exception.getMessage()));
        }

        @Override
        public void onDownloadFinished() {
            Log.i(TAG, "onDownloadFinished()");

            updateButtons(false, true, true);
            updateTextViews(getString(R.string.cml_upload_firmware_finished));
        }

        @Override
        public void onFirmwareAvailable(String version) {
            Log.i(TAG, "onFirmwareAvailable(" + version + ")");

            updateButtons(true, false, false);
            updateTextViews(String.format(Locale.US, "%s %s", getString(R.string.cml_new_firmware_available), version));
        }

        @Override
        public void onDeployFailed(FirmwarePortException exception) {
            Log.i(TAG, "onDeployFailed(" + exception.getMessage() + ")");

            updateButtons(true, false, false);
            updateTextViews(String.format(Locale.US, "%s %s", getString(R.string.cml_deploy_firmware_failed), exception.getMessage()));
        }

        @Override
        public void onDeployFinished() {
            Log.i(TAG, "onDeployFinished()");

            updateButtons(true, false, false);
            updateTextViews(getString(R.string.cml_firmware_deploy_finished));
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.cml_fragment_firmware_upgrade, container, false);

        final String cppId = getArguments().getString(APPLIANCE_KEY);
        Appliance appliance = CommlibUapp.get().getDependencies().getCommCentral().getApplianceManager().findApplianceByCppId(cppId);
        if (appliance != null && appliance instanceof ReferenceAppliance) {
            firmwarePort = appliance.getFirmwarePort();
        }

        firmwareSearchLocationTextView = rootView.findViewById(R.id.cml_tvFirmwareSearchLocation);
        firmwareImageNameTextView = rootView.findViewById(R.id.cml_tvFirmwareImageName);

        Button btnRefreshProperties = rootView.findViewById(R.id.cml_btnCheckUpgrade);
        Button btnSelect = rootView.findViewById(R.id.cml_btnSelectFirmware);
        btnUpload = rootView.findViewById(R.id.cml_btnUploadFirmware);
        btnDeploy = rootView.findViewById(R.id.cml_btnDeployFirmware);
        btnCancel = rootView.findViewById(R.id.cml_btnCancelFirmware);

        stateTextView = rootView.findViewById(R.id.cml_txtFirmwareState);
        versionTextView = rootView.findViewById(R.id.cml_txtFirmwareVersion);
        statusTextView = rootView.findViewById(R.id.cml_txtFirmwareStatusMsg);
        timeoutEditText = rootView.findViewById(R.id.cml_timeoutEditText);
        canUpgradeTextView = rootView.findViewById(R.id.cml_tvCanUpgrade);

        btnRefreshProperties.setOnClickListener(clickListener);
        btnSelect.setOnClickListener(clickListener);
        btnUpload.setOnClickListener(clickListener);
        btnDeploy.setOnClickListener(clickListener);
        btnCancel.setOnClickListener(clickListener);
        updateButtons(true, false, false);

        firmwareUploadProgressBar = rootView.findViewById(R.id.cml_progressUploadFirmware);
        firmwareUploadProgressBar.setProgress(0);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (firmwarePort == null) {
            getFragmentManager().popBackStack();
            return;
        }
        readFirmwareFiles();

        firmwarePort.addPortListener(portListener);
        firmwarePort.addFirmwarePortListener(firmwarePortListener);
    }

    @Override
    public void onPause() {
        super.onPause();

        cancelFirmware();

        firmwarePort.removePortListener(portListener);
        firmwarePort.removeFirmwarePortListener(firmwarePortListener);
    }

    private void updateButtons(boolean isUploadEnabled, boolean isDeployEnabled, boolean isCancelEnabled) {
        btnUpload.setEnabled(isUploadEnabled);
        btnDeploy.setEnabled(isDeployEnabled);
        btnCancel.setEnabled(isCancelEnabled);
    }

    private void updateTextViews(final @Nullable String statusMessage) {
        canUpgradeTextView.setText(firmwarePort.canUpgrade() ? getString(R.string.cml_yes) : getString(R.string.cml_no));
        FirmwarePortProperties portProperties = firmwarePort.getPortProperties();
        if (portProperties != null) {
            stateTextView.setText(portProperties.getState().toString());
            versionTextView.setText(portProperties.getVersion());
        }

        if (statusMessage != null) {
            statusTextView.setText(statusMessage);
        }
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

            if (fwImageAdapter.getCount() > 0) {
                selectFirmwareImage(0);
            }
        }
    }

    private void selectFirmware() {
        SelectorDialog dialog = SelectorDialog.newInstance(R.string.cml_choose_firmware_image, fwImageAdapter, new SelectorDialog.OnDialogSelectorListener() {
            @Override
            public void onSelectedOption(int index) {
                selectFirmwareImage(index);
            }
        });

        dialog.show(getActivity().getSupportFragmentManager(), null);
    }

    private void selectFirmwareImage(int index) {
        selectedFirmwareIndex = index;
        File firmwareImage = fwImageAdapter.getItem(selectedFirmwareIndex);
        if (firmwareImage != null) {
            firmwareImageNameTextView.setText(firmwareImage.getName());
        }
    }

    private void uploadSelectedFirmware() {
        if (selectedFirmwareIndex == ListView.INVALID_POSITION) {
            showIndefiniteMessage(getActivity(), rootView, getString(R.string.cml_select_a_firmware_image));
        } else {
            File firmwareFile = fwImageAdapter.getItem(selectedFirmwareIndex);
            final byte[] firmwareBytes;

            if (firmwareFile == null) {
                showMessage(getActivity(), rootView, getString(R.string.cml_select_a_firmware_image));
            } else {
                firmwareBytes = fileToBytes(firmwareFile);

                firmwareSizeInBytes = firmwareBytes.length;
                startTimeMillis = currentTimeMillis();

                try {
                    firmwarePort.pushLocalFirmware(firmwareBytes, getTimeoutInMillisFromUi());
                } catch (IllegalStateException ex) {
                    showMessage(getActivity(), rootView, getString(R.string.cml_firmware_update_already_in_progress));
                }
            }
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
        firmwarePort.deployFirmware(getTimeoutInMillisFromUi());
    }

    private void cancelFirmware() {
        firmwarePort.cancel(getTimeoutInMillisFromUi());
    }

    private long getTimeoutInMillisFromUi() {
        final String timeoutText = timeoutEditText.getText().toString();
        return TextUtils.isEmpty(timeoutText) ? DEFAULT_TIMEOUT_MILLIS : Long.parseLong(timeoutText);
    }

    private int calculateThroughputInBytesPerSecond(int progressPercentage) {
        if (startTimeMillis <= 0) {
            return 0;
        }
        final long timeDiffInMillis = currentTimeMillis() - startTimeMillis;
        final int progressInBytes = firmwareSizeInBytes * progressPercentage / 100;
        final int throughputInBytesPerSecond = Math.round(1000 * progressInBytes / timeDiffInMillis);

        // Comma-separated line, easy for use in Excel
        DICommLog.v(TAG, String.format(Locale.US, "%d,%d,%d", TimeUnit.MILLISECONDS.toSeconds(timeDiffInMillis), progressInBytes, throughputInBytesPerSecond));

        return throughputInBytesPerSecond;
    }
}
