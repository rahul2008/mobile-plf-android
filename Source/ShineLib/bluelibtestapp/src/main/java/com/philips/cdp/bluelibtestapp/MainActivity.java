/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.bluelibtestapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.pluginreferenceboard.DeviceDefinitionInfoReferenceBoard;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceAssociation;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNDeviceScanner;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinepluginmoonshinelib.SHNMoonshineDeviceDefinitionInfo;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<SHNDevice> associatedDevices;
    private SHNCentral shnCentral;
    private CustomAdapter<SHNDevice> arrayAdapter;
    public static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Handler mainHandler = new Handler();
            shnCentral = new SHNCentral.Builder(getApplicationContext()).setHandler(mainHandler).create();

            showVersions();

            SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = new DeviceDefinitionInfoReferenceBoard();
            shnCentral.registerDeviceDefinition(shnDeviceDefinitionInfo);

            SHNMoonshineDeviceDefinitionInfo shnMoonshineDeviceDefinitionInfo = new SHNMoonshineDeviceDefinitionInfo();
            shnCentral.registerDeviceDefinition(shnMoonshineDeviceDefinitionInfo);

            Button associateButton = (Button) findViewById(R.id.associateButton);
            if (associateButton != null) {
                associateButton.setOnClickListener(this);
            }

            associatedDevices = shnCentral.getShnDeviceAssociation().getAssociatedDevices();

            arrayAdapter = new CustomAdapter<>(associatedDevices, android.R.layout.simple_list_item_1, new CustomAdapter.ViewLoader<SHNDevice>() {
                private int[] viewIds = new int[]{android.R.id.text1};

                @Override
                public int[] getViewIds() {
                    return viewIds;
                }

                @Override
                public void setupView(SHNDevice item, Map<Integer, View> viewDescriptors) {
                    String deviceTypeName = item.getDeviceTypeName();
                    String address = item.getAddress();
                    ((TextView) viewDescriptors.get(android.R.id.text1)).setText(String.format("%s : [%s]", deviceTypeName, address));
                }
            }, this);

            ListView listView = (ListView) findViewById(R.id.foundDeices);
            if (listView != null) {
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(this);
            }
            registerForContextMenu(listView);

            acquirePermission();
        } catch (SHNBluetoothHardwareUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void acquirePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    ACCESS_COARSE_LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {
                if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        associatedDevices = shnCentral.getShnDeviceAssociation().getAssociatedDevices();

        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        TestApplication testApplication = (TestApplication) getApplication();
        testApplication.setShnCentral(shnCentral);

        Intent intent = new Intent(MainActivity.this, AssociateActivity.class);

        startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SHNDeviceScanner shnDeviceScanner = shnCentral.getShnDeviceScanner();
        shnDeviceScanner.stopScanning();

        SHNDevice shnDevice = associatedDevices.get(position);

        TestApplication testApplication = (TestApplication) getApplication();
        testApplication.setSelectedDevice(shnDevice);

        Intent intent = new Intent(MainActivity.this, TabbedDeviceDetailActivity.class);

        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo
            menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final SHNDeviceAssociation deviceAssociation = shnCentral.getShnDeviceAssociation();

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        SHNDevice associatedDeviceInfo = associatedDevices.get(info.position);
        deviceAssociation.removeAssociatedDevice(associatedDeviceInfo);

        associatedDevices = shnCentral.getShnDeviceAssociation().getAssociatedDevices();
        arrayAdapter.notifyDataSetChanged();

        return true;
    }

    private void showVersions() {
        String blueLibVersion = shnCentral.getVersion();

        TextView versionNameTv = (TextView) findViewById(R.id.versionNameTv);
        if (versionNameTv != null) {
            String versions = String.format("%s%s\n%s%s",
                    getString(R.string.lib_version), blueLibVersion,
                    getString(R.string.app_version), BuildConfig.VERSION_NAME);
            versionNameTv.setText(versions);
        }
    }
}
