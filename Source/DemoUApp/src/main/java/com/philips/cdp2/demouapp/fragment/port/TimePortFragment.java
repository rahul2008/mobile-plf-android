/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.fragment.port;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.appliance.CurrentApplianceManager;
import com.philips.cdp2.commlib.demouapp.R;
import com.philips.cdp2.demouapp.appliance.reference.ReferenceAppliance;
import com.philips.cdp2.demouapp.port.time.TimePort;
import com.philips.cdp2.demouapp.port.time.TimePortProperties;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Locale;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

public class TimePortFragment extends Fragment {

    private static final String TAG = "TimePortFragment";

    private static final String PROPERTY_DATETIME = "datetime";
    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
    private int requestCount = 0;

    private TextView txtResult;
    private TextView txtProgress;

    private CompoundButton switchLoopGet;

    private ReferenceAppliance currentAppliance;
    private DICommPortListener<TimePort> portListener = new DICommPortListener<TimePort>() {

        @Override
        public void onPortUpdate(TimePort timePort) {
            if (isAdded()) {
                TimePortProperties properties = timePort.getPortProperties();
                if (properties == null) {
                    return;
                }

                final String datetime = properties.datetime;
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
        }

        @Override
        public void onPortError(TimePort port, Error error, @Nullable String errorData) {
            DICommLog.e(TAG, String.format(Locale.US, "Time port error: [%s], data: [%s]", error.getErrorMessage(), errorData));

            if (isAdded()) {
                updateResult(getString(R.string.cml_lblResultPortError, error.getErrorMessage()));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.cml_fragment_timeport, container, false);

        rootview.findViewById(R.id.cml_btnGetTime).setOnClickListener(buttonClickListener);
        rootview.findViewById(R.id.cml_btnSetTime).setOnClickListener(buttonClickListener);

        txtResult = (TextView) rootview.findViewById(R.id.cml_txtResult);
        txtProgress = (TextView) rootview.findViewById(R.id.cml_txtProgress);

        switchLoopGet = (CompoundButton) rootview.findViewById(R.id.cml_switchLoopGet);
        switchLoopGet.setOnCheckedChangeListener(loopGetCheckedChangeListener);

        ((CompoundButton) rootview.findViewById(R.id.cml_switchSubscription)).setOnCheckedChangeListener(subscriptionCheckedChangeListener);

        rootview.findViewById(R.id.cml_btnGetTime).setEnabled(true);
        rootview.findViewById(R.id.cml_btnSetTime).setEnabled(true);

        return rootview;
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
        currentAppliance.getTimePort().addPortListener(portListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (currentAppliance != null) {
            currentAppliance.getTimePort().removePortListener(portListener);
        }
    }

    private final View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int viewId = view.getId();

            if (viewId == R.id.cml_btnGetTime) {
                currentAppliance.getTimePort().reloadProperties();
            } else if (viewId == R.id.cml_btnSetTime) {
                DateTime dateTime = new DateTime(currentTimeMillis() + new Random().nextInt());
                DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
                String timestamp = dateTime.toString(fmt);

                currentAppliance.getTimePort().putProperties(PROPERTY_DATETIME, timestamp);
            } else {
                DICommLog.d(TAG, "Unknown view clicked");
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

    private final CompoundButton.OnCheckedChangeListener loopGetCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
            if (isChecked) {
                currentAppliance.getTimePort().reloadProperties();
            }
        }
    };

    private void updateResult(final String result) {
        requestCount++;

        if (isAdded()) {
            txtProgress.setText(String.format(Locale.US, "Count: %d", requestCount));
            txtResult.setText(String.format(Locale.US, "Last result: %s", result));
        }
    }
}
