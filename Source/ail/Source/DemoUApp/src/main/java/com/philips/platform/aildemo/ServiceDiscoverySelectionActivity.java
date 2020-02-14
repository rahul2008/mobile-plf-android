/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.aildemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import com.philips.platform.aildemo.servicediscovery.ServiceDiscoveryManagerCSV;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;


public class ServiceDiscoverySelectionActivity extends AppCompatActivity {

    private RadioButton radioSelectionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_storage_selection);

        radioSelectionButton= findViewById(R.id.radioButton);
    }

    public void onClickSubmit(View view) {
        if (radioSelectionButton.isChecked()) {
            ServiceDiscoveryManagerCSV sdmCSV = new ServiceDiscoveryManagerCSV();

            AppInfra.Builder builder = new AppInfra.Builder();
            final AppInfra mAppInfra = builder.setServiceDiscovery(sdmCSV).build(this);
            sdmCSV.init(mAppInfra);
            sdmCSV.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
                @Override
                public void onSuccess() {
                    AILDemouAppInterface.getInstance().setAppInfra(mAppInfra);
                    Intent intent = new Intent(ServiceDiscoverySelectionActivity.this, ServiceDiscoveryDemo.class);
                    startActivity(intent);
                }

                @Override
                public void onError(ERRORVALUES errorvalues, String s) {
                    Log.d(getClass().getSimpleName(), "Error Response from Service Discovery CSV :" + s);
                    Toast.makeText(ServiceDiscoverySelectionActivity.this, "Some thing went wrong while setting local service discovery", Toast.LENGTH_SHORT).show();

                }
            },false);

        } else {
            AppInfra.Builder builder = new AppInfra.Builder();
            final AppInfra mAppInfra = builder.build(getApplicationContext());
            AILDemouAppInterface.getInstance().setAppInfra(mAppInfra);
            Intent intent = new Intent(ServiceDiscoverySelectionActivity.this, ServiceDiscoveryDemo.class);
            startActivity(intent);
        }
    }
}
