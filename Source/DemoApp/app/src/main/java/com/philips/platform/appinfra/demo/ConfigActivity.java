package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.config.ConfigInterface;

public class ConfigActivity extends AppCompatActivity {

    ConfigInterface mConfigInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mConfigInterface = AppInfraApplication.gAppInfra.getConfigInterface();

        final EditText cocoKeyET = (EditText) findViewById(R.id.CocoKeyID);
        final EditText KeyET = (EditText) findViewById(R.id.keyID);
        final EditText valueET = (EditText) findViewById(R.id.valueID);

        Button btnFetchValue = (Button) findViewById(R.id.btn_fetchValue);
        assert btnFetchValue != null;
        btnFetchValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valueET.setText(null);
                String cocokey = cocoKeyET.getText().toString();
                String key = KeyET.getText().toString();
                if (null == cocokey || null == key || cocokey.isEmpty() || key.isEmpty()) {
                    Toast.makeText(ConfigActivity.this, "Please enter Coco name and key", Toast.LENGTH_SHORT).show();

                } else {
                    ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();
                    Object object = mConfigInterface.getPropertyForKey(cocoKeyET.getText().toString(), KeyET.getText().toString(), configError);
                    if (null != configError.getErrorCode()) {
                        Toast.makeText(ConfigActivity.this, configError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        valueET.setText(object.toString());
                    }
                /*Object o1 = deviceObject.getJSONObject("AI").get("RegistrationEnvironment");
                object = deviceObject.getJSONObject("AI").get("NL");
                Object o3 = deviceObject.getJSONObject("AI").get("EE");
*/
                }
            }
        });

        final TextView show = (TextView) findViewById(R.id.show_TV);
        Button btnFetchValueFromDevice = (Button) findViewById(R.id.btn_fetchValueFromDEvice);
        assert btnFetchValueFromDevice != null;
        btnFetchValueFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cocokey = cocoKeyET.getText().toString();
                String key = KeyET.getText().toString();
                String value = valueET.getText().toString();
                if (null == cocokey || null == key || cocokey.isEmpty() || key.isEmpty() || value == null || value.isEmpty()) {

                    Toast.makeText(ConfigActivity.this, "Please enter Coco name ,key and value", Toast.LENGTH_SHORT).show();

                } else {

                    ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();
                    boolean success = mConfigInterface.setPropertyForKey(cocoKeyET.getText().toString(), KeyET.getText().toString(), value, configError);
                    if (null != configError.getErrorCode()) {
                        Toast.makeText(ConfigActivity.this, configError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (success) {
                            Toast.makeText(ConfigActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ConfigActivity.this, "Fails", Toast.LENGTH_SHORT).show();
                        }
                        //  valueET.setText(i.toString());
                    }
                }
            }
        });
    }

}
