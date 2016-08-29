/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.cdp.bluelibtestapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNAssociationProcedure;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDevice;
import com.philips.pins.shinelib.SHNDeviceAssociation;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.List;
import java.util.Map;

public class AssociateActivity extends AppCompatActivity implements SHNDeviceAssociation.SHNDeviceAssociationListener {
    private static final String TAG = "AssociateActivity";

    private SHNCentral shnCentral;
    private SHNDeviceAssociation shnDeviceAssociation;
    private SHNDevice associatedSHNDevice;

    private Button btnStartAssociation;
    private ListView associationListView;
    private TextView tvAssociationState;

    private View successTextView;
    private View failureTextView;

    private View.OnClickListener startAssociationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            btnStartAssociation.setEnabled(false);

            SHNDeviceDefinitionInfo selectedSHNShnDeviceDefinitionInfo = shnCentral.getSHNDeviceDefinitions().getRegisteredDeviceDefinitions().get(associationListView.getCheckedItemPosition());
            SHNLogger.i(TAG, "Associating with: " + selectedSHNShnDeviceDefinitionInfo.getDeviceTypeName());

            shnDeviceAssociation.startAssociationForDeviceType(selectedSHNShnDeviceDefinitionInfo.getDeviceTypeName());
            associatedSHNDevice = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_associate);

        successTextView = findViewById(R.id.tvAssociationSucceeded);
        if (successTextView != null) {
            successTextView.setVisibility(View.GONE);
        }

        failureTextView = findViewById(R.id.tvAssociationFailed);
        if (failureTextView != null) {
            failureTextView.setVisibility(View.GONE);
        }

        TestApplication application = (TestApplication) getApplication();
        shnCentral = application.getShnCentral();

        shnDeviceAssociation = shnCentral.getShnDeviceAssociation();
        shnDeviceAssociation.setShnDeviceAssociationListener(this);

        setupAssociationListView();
        setupAssociationButton();
    }

    private void setupAssociationButton() {
        btnStartAssociation = (Button) findViewById(R.id.btnStartAssociation);

        if (btnStartAssociation != null) {
            btnStartAssociation.setOnClickListener(startAssociationListener);
        }
    }

    private void setupAssociationListView() {
        List<SHNDeviceDefinitionInfo> shnDeviceDefinitionInfos = shnCentral.getSHNDeviceDefinitions().getRegisteredDeviceDefinitions();
        CustomAdapter<SHNDeviceDefinitionInfo> adapter = new CustomAdapter<>(shnDeviceDefinitionInfos, android.R.layout.simple_list_item_1, new CustomAdapter.ViewLoader<SHNDeviceDefinitionInfo>() {
            private int[] viewIds = new int[]{android.R.id.text1};

            @Override
            public int[] getViewIds() {
                return viewIds;
            }

            @Override
            public void setupView(SHNDeviceDefinitionInfo item, Map<Integer, View> viewDescriptors) {
                ((TextView) viewDescriptors.get(android.R.id.text1)).setText(item.getDeviceTypeName());
            }
        }, this);
        associationListView = (ListView) findViewById(R.id.listView);
        if (associationListView != null) {
            associationListView.setAdapter(adapter);
            associationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    btnStartAssociation.setEnabled(true);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateAssociationState();
    }

    @Override
    public void onStop() {
        super.onStop();

        shnDeviceAssociation.stopAssociation();
        if (associatedSHNDevice != null) {
            associatedSHNDevice.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        shnDeviceAssociation.setShnDeviceAssociationListener(null);
        shnDeviceAssociation = null;
    }

    // implements SHNDeviceAssociation.SHNDeviceAssociationListener
    @Override
    public void onAssociationStarted(SHNAssociationProcedure shnDeviceAssociationProcedure) {
        SHNLogger.i(TAG, "onAssociationStarted");
        updateAssociationState();
    }

    @Override
    public void onAssociationStopped() {
        SHNLogger.i(TAG, "onAssociationStopped");
        updateAssociationState();
        btnStartAssociation.setEnabled(true);
    }

    @Override
    public void onAssociationSucceeded(SHNDevice shnDevice) {
        SHNLogger.i(TAG, "onAssociationSucceeded");
        associatedSHNDevice = shnDevice;
        updateAssociationState();
        successTextView.setVisibility(View.VISIBLE);
        failureTextView.setVisibility(View.GONE);
    }

    @Override
    public void onAssociationFailed(SHNResult shnError) {
        SHNLogger.i(TAG, "onAssociationFailed");

        if (associationListView.getCheckedItemPosition() != AdapterView.INVALID_POSITION && shnDeviceAssociation.getState() == SHNDeviceAssociation.State.Idle) {
            btnStartAssociation.setEnabled(true);
            successTextView.setVisibility(View.GONE);
            failureTextView.setVisibility(View.VISIBLE);
        } else {
            btnStartAssociation.setEnabled(false);
        }

        updateAssociationState();
    }

    @Override
    public void onAssociatedDevicesUpdated() {
    }

    private void updateAssociationState() {
        if (tvAssociationState == null) {
            tvAssociationState = (TextView) findViewById(R.id.tvAssociationState);
        }
        tvAssociationState.setText(shnDeviceAssociation.getState().toString());
    }
}
