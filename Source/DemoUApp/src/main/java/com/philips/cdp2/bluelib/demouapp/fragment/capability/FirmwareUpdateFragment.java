/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.fragment.capability;

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

import com.philips.cdp2.bluelib.demouapp.BluelibUapp;
import com.philips.cdp2.bluelib.demouapp.R;
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

public class FirmwareUpdateFragment extends Fragment {

    private static final String TAG = FirmwareUpdateFragment.class.getSimpleName();

    private SHNDevice mDevice;
    private SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate;
    private TextView textViewFirmwareVersion;
    private TextView textViewFirmwareState;
    private ListView listViewFiles;
    private File[] files;
    private File selectedFile;
    private ProgressBar progressBar;
    private TextView textViewUploadState;
    private TextView textViewResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDevice = BluelibUapp.get().getSelectedDevice();
        shnCapabilityFirmwareUpdate = (SHNCapabilityFirmwareUpdate) mDevice.getCapabilityForType(SHNCapabilityType.FirmwareUpdate);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.bll_fragment_firmware_update, container, false);

        Button start = fragmentView.findViewById(R.id.btnStartUpload);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFileAndStartUpload();
            }
        });

        Button deploy = fragmentView.findViewById(R.id.btnDeploy);
        deploy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shnCapabilityFirmwareUpdate.deployFirmware();
            }
        });

        Button abort = fragmentView.findViewById(R.id.btnAbortUpload);
        abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shnCapabilityFirmwareUpdate.abortFirmwareUpload();
            }
        });

        textViewFirmwareVersion = fragmentView.findViewById(R.id.tvFirmwareVersion);
        textViewFirmwareState = fragmentView.findViewById(R.id.tvFirmwareState);
        textViewUploadState = fragmentView.findViewById(R.id.tvUploadState);
        textViewResult = fragmentView.findViewById(R.id.tvUploadResult);
        listViewFiles = fragmentView.findViewById(R.id.lvFiles);
        progressBar = fragmentView.findViewById(R.id.uploadProgress);

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

        textViewFirmwareVersion.setText(R.string.bll_retrieving);
        shnCapabilityFirmwareUpdate.getUploadedFirmwareInfo(new SHNFirmwareInfoResultListener() {
            @Override
            public void onActionCompleted(SHNFirmwareInfo value, @NonNull SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    textViewFirmwareVersion.setText(value.getVersion());
                    textViewFirmwareState.setText(value.getState().name());
                } else {
                    textViewFirmwareVersion.setText(R.string.bll_error);
                    textViewFirmwareState.setText(R.string.bll_error);
                }
            }
        });

        updateFilesList();
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
                    Log.e(TAG, "IOException", e);
                }

                shnCapabilityFirmwareUpdate.setSHNCapabilityFirmwareUpdateListener(firmwareUpdateListener);
                shnCapabilityFirmwareUpdate.uploadFirmware(buffer);
            } catch (FileNotFoundException e) {
                Log.e(TAG, "FileNotFoundException", e);
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
            if (textViewUploadState != null) {
                textViewUploadState.setText((shnCapabilityFirmwareUpdate).getState().name().replace("SHNFirmwareUpdateState", ""));
            }
        }

        @Override
        public void onProgressUpdate(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, float v) {
            SHNLogger.w(TAG, "onProgressUpdate " + v);
            if (progressBar != null) {
                progressBar.setProgress((int) (v * 100));
            }
        }

        @Override
        public void onUploadFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult) {
            SHNLogger.w(TAG, "onUploadFailed " + shnResult);
            if (textViewResult != null) {
                textViewResult.setText(String.format("%s%s", getResources().getString(R.string.bll_upload_failed), shnResult));
            }
        }

        @Override
        public void onUploadFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate) {
            SHNLogger.w(TAG, "onUploadFinished ");
            if (textViewResult != null) {
                textViewResult.setText(R.string.bll_finished);
            }
        }

        @Override
        public void onDeployFailed(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult) {
            SHNLogger.w(TAG, "onDeployFailed ");
            if (textViewResult != null) {
                textViewResult.setText(String.format("%s%s", getResources().getString(R.string.bll_deploy_failed), shnResult));
            }
        }

        @Override
        public void onDeployFinished(SHNCapabilityFirmwareUpdate shnCapabilityFirmwareUpdate, SHNResult shnResult) {
            SHNLogger.w(TAG, "onDeployFinished: " + shnResult);
            if (textViewResult != null) {
                textViewResult.setText(String.format("%s%s", getResources().getString(R.string.bll_deploy_finished), shnResult));
            }
        }
    };
}
