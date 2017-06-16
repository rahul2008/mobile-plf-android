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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.CommlibUapp;
import com.philips.cdp2.demouapp.appliance.reference.ReferenceAppliance;
import com.philips.cdp2.demouapp.port.time.TimePort;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Locale;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

public abstract class ReferenceApplianceFragment extends Fragment {

    private static final String TAG = "ReferenceApplianceFragment";

    private static final String PROPERTY_DATETIME = "datetime";
    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
    private int requestCount = 0;

    private TextView txtResult;
    private TextView txtProgress;
    private CompoundButton switchStayConnected;

    private CompoundButton switchLoopGet;

    private ReferenceAppliance currentAppliance;

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();

            if (viewId == R.id.btnGetTime) {
                currentAppliance.getTimePort().reloadProperties();
            } else if (viewId == R.id.btnSetTime) {
                DateTime dateTime = new DateTime(currentTimeMillis() + new Random().nextInt());
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                String timestamp = dateTime.toString(fmt);

                currentAppliance.getTimePort().putProperties(PROPERTY_DATETIME, timestamp);
            } else if (viewId == R.id.btnFirmwareUpgrade) {
                CurrentApplianceManager.getInstance().setCurrentAppliance(currentAppliance);

                CommlibUapp.instance.nextFragment(new FirmwareUpgradeFragment());
            } else {
                DICommLog.d(TAG, "Unknown view clicked");

            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener connectionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (currentAppliance == null) {
                return;
            }

            if (isChecked) {
                currentAppliance.enableCommunication();
            } else {
                currentAppliance.disableCommunication();
            }
        }
    };

    private final CompoundButton.OnCheckedChangeListener subscriptionCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (currentAppliance == null) {
                return;
            }

            if (isChecked) {
                currentAppliance.getTimePort().subscribe();
            } else {
                currentAppliance.getTimePort().unsubscribe();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_reference_appliance, container, false);

        rootview.findViewById(R.id.btnGetTime).setOnClickListener(buttonClickListener);
        rootview.findViewById(R.id.btnSetTime).setOnClickListener(buttonClickListener);
        rootview.findViewById(R.id.btnFirmwareUpgrade).setOnClickListener(buttonClickListener);

        txtResult = (TextView) rootview.findViewById(R.id.txtResult);
        txtProgress = (TextView) rootview.findViewById(R.id.txtProgress);

        switchStayConnected = ((CompoundButton) rootview.findViewById(R.id.switchStayConnected));
        switchLoopGet = (CompoundButton) rootview.findViewById(R.id.switchLoopGet);
        switchLoopGet.setOnCheckedChangeListener(loopGetCheckedChangeListener);

        ((CompoundButton) rootview.findViewById(R.id.switchStayConnected)).setOnCheckedChangeListener(connectionCheckedChangeListener);
        ((CompoundButton) rootview.findViewById(R.id.switchSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);

        currentAppliance = getCurrentAppliance();

        if (currentAppliance == null) {
            getFragmentManager().popBackStack();
        } else {
            final ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(currentAppliance.getNetworkNode().getName());
            }
            setupAppliance(currentAppliance);

            rootview.findViewById(R.id.btnGetTime).setEnabled(true);
            rootview.findViewById(R.id.btnSetTime).setEnabled(true);
        }

        return rootview;
    }

    protected abstract ReferenceAppliance getCurrentAppliance();

    private final CompoundButton.OnCheckedChangeListener loopGetCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked) {
                currentAppliance.getTimePort().reloadProperties();
            }
        }
    };

    private void setupAppliance(@NonNull ReferenceAppliance appliance) {
        boolean stayConnected = switchStayConnected.isChecked();

        if (stayConnected) {
            appliance.enableCommunication();
        } else {
            appliance.disableCommunication();
        }

        appliance.getTimePort().addPortListener(new DICommPortListener<TimePort>() {

            @Override
            public void onPortUpdate(TimePort timePort) {
                final String datetime = timePort.getPortProperties().datetime;
                if (datetime == null) {
                    return;
                }
                DateTime dt = new DateTime(datetime);
                String dateTimeString = DATETIME_FORMATTER.print(dt);

                updateResult(dateTimeString);

                if (switchLoopGet.isChecked()) {
                    timePort.reloadProperties();
                }
            }

            @Override
            public void onPortError(TimePort timePort, Error error, final String s) {
                DICommLog.e(TAG, "Time port error: " + error.getErrorMessage() + " (" + s + ")");

                if (isAdded()) {
                    updateResult(getString(R.string.lblResultPortError, s));
                }
            }
        });
    }

    private void updateResult(final String result) {
        requestCount++;

        if (isAdded()) {
            txtProgress.setText(String.format(Locale.US, "Count: %d", requestCount));
            txtResult.setText(String.format(Locale.US, "Last result: %s", result));
        }
    }
}
