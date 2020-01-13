package com.philips.platform.pimdemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.uid.view.widget.Button;
import com.pim.demouapp.HomeCountryUpdateReceiver;
import com.pim.demouapp.PIMDemoUAppActivity;
import com.pim.demouapp.PIMDemoUAppDependencies;
import com.pim.demouapp.PIMDemoUAppInterface;
import com.pim.demouapp.PIMDemoUAppSettings;

import java.util.ArrayList;
import java.util.List;

public class PimDemoActivity extends UIDActivity {

    private PIMDemoUAppInterface uAppInterface;
    private Spinner spinnerCountrySelection;
    private AppCompatSpinner selectLibreary;
    private HomeCountryUpdateReceiver receiver;
    private ServiceDiscoveryInterface mServiceDiscoveryInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pim_demo);
        uAppInterface = new PIMDemoUAppInterface();
        Button launchUApp = findViewById(R.id.launch);
        launchUApp.setOnClickListener(v -> {
            Intent intent = new Intent(PimDemoActivity.this, PIMDemoUAppActivity.class);
            intent.putExtra("SelectedLib", selectLibreary.getSelectedItem().toString());
            startActivity(intent);
//            uAppInterface.init(new PIMDemoUAppDependencies((PimDemoApplication) getApplication()).getAppInfra(), new PIMDemoUAppSettings(this));
        });

        PimDemoApplication pimDemoApplication = (PimDemoApplication) getApplicationContext();
//        AppInfraInterface appInfraInterface = pimDemoApplication.getAppInfra();

        selectLibreary = findViewById(R.id.selectLibrary);
        List<String> libraryList = new ArrayList<>();
        libraryList.add("PIM");
        libraryList.add("USR");
        ArrayAdapter libraryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, libraryList);
        selectLibreary.setAdapter(libraryAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServiceDiscoveryInterface.unRegisterHomeCountrySet(receiver);
    }
}
