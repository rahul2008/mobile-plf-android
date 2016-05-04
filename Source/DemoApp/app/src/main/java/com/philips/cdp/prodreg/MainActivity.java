package com.philips.cdp.prodreg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.philips.cdp.prodreg.register.ProdRegHelper;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String configurationType[] = {"Evaluation", "Testing", "Development", "Staging", "Production"};
    private String TAG = getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ProdRegHelper().init(this);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);

        final ArrayAdapter<String> configType = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, configurationType);
        configType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(configType);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                final String configuration = adapter.getItemAtPosition(position).toString();
                Log.d(TAG, "Before Configuration" + configuration);
                if (configuration.equalsIgnoreCase("Development")) {
                    RegistrationConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment("Development");
                } else if (configuration.equalsIgnoreCase("Testing")) {
                    RegistrationConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment("Testing");
                } else if (configuration.equalsIgnoreCase("Evaluation")) {
                    RegistrationConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment("Evaluation");
                } else if (configuration.equalsIgnoreCase("Staging")) {
                    RegistrationConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment("Staging");
                } else if (configuration.equalsIgnoreCase("Production")) {
                    RegistrationConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment("Production");
                }
                Log.d(TAG, "After Configuration" + configuration);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_user_registration:
                Log.d(TAG, "On Click : User Registration");
                RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(this);
                Util.navigateFromUserRegistration();
                break;
            case R.id.btn_product_registration:
                intent = new Intent(this, ProductActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register_list:
                intent = new Intent(this, RegisteredProductsList.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
