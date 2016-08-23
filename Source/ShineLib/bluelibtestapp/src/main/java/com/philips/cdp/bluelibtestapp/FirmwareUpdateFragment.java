/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.bluelibtestapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNCapabilityType;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNFirmwareInfo;
import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.SHNCapabilityFirmwareUpdate;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FirmwareUpdateFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = FirmwareUpdateFragment.class.getSimpleName();

    private SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate;
    private TextView textViewFirmwareVersion;
    private TextView textViewFirmwareState;
    private ListView listViewFiles;
    private File[] files;
    private File selectedFile;
    private ProgressBar progressBar;
    private TextView textViewUploadState;
    private TextView textViewResult;

    public static FirmwareUpdateFragment newInstance() {
        return new FirmwareUpdateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TestApplication application = (TestApplication) getActivity().getApplication();
        SHNDevice shnSelectedDevice = application.getSelectedDevice();

        shnCapabilityFirmwareUpdate = (SHNCapabilityFirmwareUpdate) shnSelectedDevice.getCapabilityForType(SHNCapabilityType.FirmwareUpdate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_firmware_update, container, false);

        Button start = (Button) fragmentView.findViewById(R.id.btnStartUpload);
        start.setOnClickListener(this);

        Button deploy = (Button) fragmentView.findViewById(R.id.btnDeploy);
        deploy.setOnClickListener(this);

        Button abort = (Button) fragmentView.findViewById(R.id.btnAbortUpload);
        abort.setOnClickListener(this);

        textViewFirmwareVersion = (TextView) fragmentView.findViewById(R.id.tvFirmwareVersion);
        textViewFirmwareState = (TextView) fragmentView.findViewById(R.id.tvFirmwareState);
        textViewUploadState = (TextView) fragmentView.findViewById(R.id.tvUploadState);
        textViewResult = (TextView) fragmentView.findViewById(R.id.tvUploadResult);
        listViewFiles = (ListView) fragmentView.findViewById(R.id.lvFiles);
        progressBar = (ProgressBar) fragmentView.findViewById(R.id.uploadProgress);

        listViewFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedFile = files[position];
            }
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

        textViewFirmwareVersion.setText(R.string.retrieving);
        shnCapabilityFirmwareUpdate.getUploadedFirmwareInfo(new SHNFirmwareInfoResultListener() {
            @Override
            public void onActionCompleted(SHNFirmwareInfo value, @NonNull SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    textViewFirmwareVersion.setText(value.getVersion());
                    textViewFirmwareState.setText(value.getState().name());
                } else {
                    textViewFirmwareVersion.setText(R.string.error);
                    textViewFirmwareState.setText(R.string.error);
                }
            }
        });

        updateFilesList();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnStartUpload:
                getFileAndStartUpload();
                break;
            case R.id.btnAbortUpload:
                shnCapabilityFirmwareUpdate.abortFirmwareUpload();
                break;
            case R.id.btnDeploy:
                shnCapabilityFirmwareUpdate.deployFirmware();
                break;
        }
    }

    private void getFileAndStartUpload() {
        if (selectedFile != null) {
            try {
                InputStream inputStream = new FileInputStream(selectedFile);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];

                try {
                    int size;
                    while ((size = inputStream.read(buffer, 0, buffer.length)) >= 0) {
                        outputStream.write(buffer, 0, size);
                    }
                    buffer = outputStream.toByteArray();
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                shnCapabilityFirmwareUpdate.setSHNCapabilityFirmwareUpdateListener(firmwareUpdateListener);
                shnCapabilityFirmwareUpdate.uploadFirmware(buffer);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateFilesList() {
        File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (dir != null) {
            Log.d(TAG, dir.getAbsolutePath());

            files = null;
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    files = dir.listFiles();
                }
            } else {
                dir.mkdirs();
            }

            List<String> list = new ArrayList<>();
            if (files != null) {
                for (final File file : files) {
                    list.add(file.getName());
                }
            }

            listViewFiles.setAdapter(new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, list));
        }
    }

    private SHNCapabilityFirmwareUpdate.SHNCapabilityFirmwareUpdateListener firmwareUpdateListener = new SHNCapabilityFirmwareUpdate.SHNCapabilityFirmwareUpdateListener() {

        @Override
        public void onStateChanged(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate) {
            SHNLogger.w(TAG, "onStateChanged ");
            textViewUploadState.setText((shnCapabilityFirmwareUpdate).getState().name().replace("SHNFirmwareUpdateState", ""));
        }

        @Override
        public void onProgressUpdate(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, float v) {
            SHNLogger.w(TAG, "onProgressUpdate " + v);
            progressBar.setProgress((int) (v * 100));
        }

        @Override
        public void onUploadFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult) {
            SHNLogger.w(TAG, "onUploadFailed " + shnResult);
            textViewResult.setText(String.format("%s%s", getResources().getString(R.string.upload_failed), shnResult));
        }

        @Override
        public void onUploadFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate) {
            SHNLogger.w(TAG, "onUploadFinished ");
            textViewResult.setText(R.string.finished);
        }

        @Override
        public void onDeployFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult) {
            SHNLogger.w(TAG, "onDeployFailed ");
            textViewResult.setText(String.format("%s%s", getResources().getString(R.string.deploy_failed), shnResult));
        }

        @Override
        public void onDeployFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult) {
            SHNLogger.w(TAG, "onDeployFinished: " + shnResult);
            textViewResult.setText(String.format("%s%s", getResources().getString(R.string.deploy_finished), shnResult));
        }
    };
}
