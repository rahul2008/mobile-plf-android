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


        final EditText getGroupKeyET = (EditText) findViewById(R.id.getCocoKeyID);
        final EditText getKeyET = (EditText) findViewById(R.id.getKeyID);
        final TextView showValue = (TextView) findViewById(R.id.getValue);
        Button btnGetValueToDevice = (Button) findViewById(R.id.btn_getValueFromDEvice);

        assert btnGetValueToDevice != null;
        btnGetValueToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showValue.setText(null);
                String cocokey = getGroupKeyET.getText().toString();
                String key = getKeyET.getText().toString();
                if (null == cocokey || null == key || cocokey.isEmpty() || key.isEmpty()) {
                    Toast.makeText(ConfigActivity.this, "Please enter Coco name and key", Toast.LENGTH_SHORT).show();

                } else {
                    ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();
                    Object object = mConfigInterface.getPropertyForKey(getGroupKeyET.getText().toString(), getKeyET.getText().toString(), configError);
                    if (null != configError.getErrorCode()) {
                        Toast.makeText(ConfigActivity.this, configError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        if(object != null){
                            showValue.setText(object.toString());

                        } else {

                        }
                    }

                }
            }
        });


        final EditText setGroupKeyET = (EditText) findViewById(R.id.setGroupKeyID);
        final EditText setKeyET = (EditText) findViewById(R.id.setKeyID);
        final EditText setValueET = (EditText) findViewById(R.id.setValueID);
        Button btnSetValueFromDevice = (Button) findViewById(R.id.btn_setValueToDEvice);
        assert btnSetValueFromDevice != null;
        btnSetValueFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cocokey = setGroupKeyET.getText().toString();
                String key = setKeyET.getText().toString();
                String value = setValueET.getText().toString();
                if (null == cocokey || null == key || cocokey.isEmpty() || key.isEmpty() || value == null || value.isEmpty()) {

                    Toast.makeText(ConfigActivity.this, "Please enter Coco name ,key and value", Toast.LENGTH_SHORT).show();

                } else {

                    ConfigInterface.ConfigError configError = new ConfigInterface.ConfigError();
                    boolean success = mConfigInterface.setPropertyForKey(setGroupKeyET.getText().toString(), setKeyET.getText().toString(), value, configError);
                    if (null != configError.getErrorCode()) {
                        Toast.makeText(ConfigActivity.this, configError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (success) {
                            Toast.makeText(ConfigActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ConfigActivity.this, "Fails", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

}
