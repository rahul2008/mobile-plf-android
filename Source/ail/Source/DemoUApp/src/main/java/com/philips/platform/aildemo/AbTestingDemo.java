package com.philips.platform.aildemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

/**
 * Created by 310243577 on 10/3/2016.
 */

public class AbTestingDemo extends Activity {

    private String[] valueTypes = {"App Update", "App Restart"};
    private ABTestClientInterface.UPDATETYPES valueType;
    private ABTestClientInterface abTestingInterface;
    private TextView value;
    private TextView cacheStatusValue;
    private TextView refreshStatus;
    private Spinner requestType;
    private EditText testName;
    private EditText defaultValue;

    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abtesting);

        value =  findViewById(R.id.value);
        Button btValue = findViewById(R.id.bttestValue);
        Button btCacheStatus = findViewById(R.id.btcachestatus);
        Button btRefresh = findViewById(R.id.btrefresh);
        cacheStatusValue =  findViewById(R.id.cachestatusValue);
        refreshStatus =  findViewById(R.id.refreshstatus);
        requestType =  findViewById(R.id.spinnerRequestType);
        testName =  findViewById(R.id.tesName);
        defaultValue =  findViewById(R.id.defaultValue);
        AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        abTestingInterface = AILDemouAppInterface.getInstance().getAppInfra().getAbTesting();
        abTestingInterface.enableDeveloperMode(true);
        defaultValue.setText("Experience K");

        ArrayAdapter<String> input_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, valueTypes);
        requestType.setAdapter(input_adapter);

        requestType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

	    btValue.setOnClickListener(v -> {
           if (requestType.getSelectedItem().toString().equalsIgnoreCase("App Update")) {
                valueType = ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE;
            } else if (requestType.getSelectedItem().toString().equalsIgnoreCase("App Restart")) {
                valueType = ABTestClientInterface.UPDATETYPES.EVERY_APP_START;
            }
            String test = abTestingInterface.getTestValue(testName.getText().toString(), defaultValue.getText().toString(),
                    valueType, null);
            value.setText("Experience - " + test);
        });

        btCacheStatus.setOnClickListener(v -> {
            if(abTestingInterface.getCacheStatus() != null) {
                String cacheStatus = abTestingInterface.getCacheStatus().toString();
                if(cacheStatus != null)
                    cacheStatusValue.setText(cacheStatus);
            }
        });

        btRefresh.setOnClickListener(v -> abTestingInterface.updateCache(new ABTestClientInterface.OnRefreshListener() {
            @Override
            public void onSuccess() {
                refreshStatus.setText("SUCCESS");
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                refreshStatus.setText(message);
            }
        }));

    }

}
