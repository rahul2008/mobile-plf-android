package com.philips.cdp.dicommclientsample;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.philips.cdp.dicommclient.appliance.CurrentApplianceManager;
import com.philips.cdp.dicommclient.appliance.DICommAppliance;
import com.philips.cdp.dicommclient.appliance.DICommApplianceListener;
import com.philips.cdp.dicommclient.port.DICommPort;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DetailActivity extends Activity implements DICommApplianceListener, CompoundButton.OnCheckedChangeListener {

    private TextView purifierNameView;
    private Switch lightSwitch;
    private AirPurifier currentPurifier;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        currentPurifier = (AirPurifier) CurrentApplianceManager.getInstance().getCurrentAppliance();

        purifierNameView = (TextView) findViewById(R.id.tv_airpurifier_name);
        purifierNameView.setText(currentPurifier.getName());

        lightSwitch = (Switch) findViewById(R.id.sw_light);
        lightSwitch.setOnCheckedChangeListener(this);
        updateLightState(currentPurifier.getAirPort());
    }

    @Override
    protected void onResume() {
        super.onResume();
        CurrentApplianceManager.getInstance().addApplianceListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CurrentApplianceManager.getInstance().removeApplianceListener(this);
    }

    @Override
    public void onAppliancePortUpdate(final DICommAppliance appliance, final DICommPort<?> port) {
        if (port instanceof AirPort) {
            updateLightState((AirPort) port);
        }
    }

    @Override
    public void onAppliancePortError(final DICommAppliance appliance, final DICommPort<?> port, final com.philips.cdp.dicommclient.request.Error error) {

    }

    private void updateLightState(final AirPort port) {
        AirPortProperties properties = port.getPortProperties();
        if (properties != null) {
            lightSwitch.setChecked(properties.getLightOn());
        }
    }

    @Override
    public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
        currentPurifier.getAirPort().setLight(isChecked);
    }
}
