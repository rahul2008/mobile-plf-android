package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;

import java.util.ArrayList;
import java.util.List;

public class AppConfigurationActivity extends AppCompatActivity {

    AppConfigurationInterface mConfigInterface;
    private Spinner dataTypeSpinner;
    final String[] dataType = {"String", "Integer"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mConfigInterface = AppInfraApplication.gAppInfra.getConfigInterface();


        // setting language spinner
        dataTypeSpinner = (Spinner) findViewById(R.id.spinnerDataType);

        ArrayAdapter<String> mLanguage_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, dataType);
        dataTypeSpinner.setAdapter(mLanguage_adapter);

        dataTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // initializeDigitalCareLibrary();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final EditText getGroupKeyET = (EditText) findViewById(R.id.getCocoKeyID);
        final EditText getKeyET = (EditText) findViewById(R.id.getKeyID);
        final TextView showValue = (TextView) findViewById(R.id.getValue);
        Button btnGetValueFromDevice = (Button) findViewById(R.id.btn_getValueFromDEvice);

        assert btnGetValueFromDevice != null;
        btnGetValueFromDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showValue.setText(null);
                String cocokey = getGroupKeyET.getText().toString();
                String key = getKeyET.getText().toString();
                if (null == cocokey || null == key || cocokey.isEmpty() || key.isEmpty()) {
                    Toast.makeText(AppConfigurationActivity.this, "Please enter Coco name and key", Toast.LENGTH_SHORT).show();

                } else {
                    AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
                    Object object = null;
                    try {
                        object = mConfigInterface.getPropertyForKey(getKeyET.getText().toString(), getGroupKeyET.getText().toString(),  configError);
                    } catch (AppConfigurationInterface.InvalidArgumentException e) {
                        e.printStackTrace();
                    }
                    if (null != configError.getErrorCode()) {
                        Toast.makeText(AppConfigurationActivity.this, configError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (object != null) {
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
        Button btnSetValueToDevice = (Button) findViewById(R.id.btn_setValueToDEvice);
        assert btnSetValueToDevice != null;
        btnSetValueToDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object value = null;
                String cocokey = setGroupKeyET.getText().toString();
                String key = setKeyET.getText().toString();
                List<String> arrayListString = new ArrayList<String>();
                List<Integer> arrayListInteger = new ArrayList<Integer>();
                String enteredValue = setValueET.getText().toString().trim();

                if (null == cocokey || null == key || cocokey.isEmpty() || key.isEmpty() || enteredValue == null) {

                    Toast.makeText(AppConfigurationActivity.this, "Please enter Coco name ,key and value", Toast.LENGTH_SHORT).show();

                } else {
                    boolean isInputDataValid = true;
                    try {
                        if (dataTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("String")) {// if input data is String type

                            if (enteredValue.contains(",")) {
                                String[] tempArray = enteredValue.split(",");
                                for (int icount = 0; icount < tempArray.length; icount++) {
                                    arrayListString.add(tempArray[icount].trim());
                                }
                                if (arrayListString.size() > 1) {
                                    // if multiple  item in arrayList  then use Arraylist
                                    value = arrayListString;
                                } else {
                                    // if only one item in arrayList then do not use Arraylist
                                    value = arrayListString.get(0);
                                }
                            } else {
                                value = enteredValue;
                            }
                        } else {// if input data is Integer type


                            if (enteredValue.contains(",")) {
                                String[] tempArray = enteredValue.split(",");
                                for (int icount = 0; icount < tempArray.length; icount++) {
                                    arrayListInteger.add(new Integer(Integer.parseInt(tempArray[icount].trim())));
                                }
                                if (arrayListInteger.size() > 1) {
                                    // if multiple  item in arrayList then use Arraylist
                                    value = arrayListInteger;
                                } else {
                                    // if only one item in arrayList then do not use Arraylist
                                    value = arrayListInteger.get(0);
                                }
                            } else {
                                Integer singleInteger = new Integer(Integer.parseInt(enteredValue));
                                value = singleInteger;
                            }


                        }
                    } catch (Exception e) {
                        isInputDataValid = false; // if parsing String and Integer fails
                    }
                    if (isInputDataValid) {
                        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
                        boolean success = false;
                        try {
                            success = mConfigInterface.setPropertyForKey(setKeyET.getText().toString(), setGroupKeyET.getText().toString(),  value, configError);
                        } catch (AppConfigurationInterface.InvalidArgumentException e) {
                            e.printStackTrace();
                        }
                        if (null != configError.getErrorCode()) {
                            Toast.makeText(AppConfigurationActivity.this, configError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                        } else {
                            if (success) {
                                Toast.makeText(AppConfigurationActivity.this, "SUCCESS", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AppConfigurationActivity.this, "Fails", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Toast.makeText(AppConfigurationActivity.this, "Invalid Value entered", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
