package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AppConfigurationActivity extends AppCompatActivity {

    final String[] dataType = {"String", "Integer", "Map of <String,String>/<String,Integer>", "Delete"};
    AppConfigurationInterface mConfigInterface;
    private Spinner dataTypeSpinner;
    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        SecureStorageInterface mSecureStorage = appInfra.getSecureStorage();

        String enc = "4324332423432432432435425435435346465464547657567.000343242342";

        try {
            plainByte= enc.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
        encryptedByte=mSecureStorage.encryptData(plainByte,sseStore);
        try {
            String encBytesString = new String(encryptedByte, "UTF-8");
            Log.e("Encrypted Data",encBytesString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] plainData= mSecureStorage.decryptData(encryptedByte,sseStore);
        String  result = Arrays.equals(plainByte,plainData)?"True":"False";
        try {
            String decBytesString = new String(plainByte, "UTF-8");
            Log.e("Decrypted Data",decBytesString);
        } catch (UnsupportedEncodingException e) {
        }

        mConfigInterface = AILDemouAppInterface.getInstance().getAppInfra().getConfigInterface();


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
                if (cocokey.isEmpty() || key.isEmpty()) {
                    Toast.makeText(AppConfigurationActivity.this, "Please enter Coco name and key", Toast.LENGTH_SHORT).show();

                } else {
                    AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
                    Object object = null;
                    try {
                        object = mConfigInterface.getPropertyForKey(getKeyET.getText().toString(), getGroupKeyET.getText().toString(), configError);
                        if (object instanceof Map) {
                            int h = 10;
                        }
                        int y = 10;
                    } catch (IllegalArgumentException e) {
                        Log.e(getClass() + "", " Illegal argument exception ");
                    }
                    if (null != configError.getErrorCode() && AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.NoError != configError.getErrorCode()) {
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


        final EditText getGroupKeyETDef = (EditText) findViewById(R.id.getCocoKeyIDDef);
        final EditText getKeyETDef = (EditText) findViewById(R.id.getKeyIDDef);
        final TextView showValueDef = (TextView) findViewById(R.id.getValueDef);
        Button btnGetValueFromDeviceDef = (Button) findViewById(R.id.btn_getValueFromDEviceDef);

        assert btnGetValueFromDeviceDef != null;
        btnGetValueFromDeviceDef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showValueDef.setText(null);
                String cocokey = getGroupKeyETDef.getText().toString();
                String key = getKeyETDef.getText().toString();
                if (cocokey.isEmpty() || key.isEmpty()) {
                    Toast.makeText(AppConfigurationActivity.this, "Please enter Coco name and key", Toast.LENGTH_SHORT).show();

                } else {
                    AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
                    Object object = null;
                    try {
                        object = mConfigInterface.getDefaultPropertyForKey(getKeyETDef.getText().toString(), getGroupKeyETDef.getText().toString(), configError);
                    } catch (IllegalArgumentException e) {
                        Log.e(getClass() + "", " Illegal argument exception ");
                    }
                    if (null != configError.getErrorCode() && AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum.NoError != configError.getErrorCode()) {
                        Toast.makeText(AppConfigurationActivity.this, configError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        if (object != null) {
                            showValueDef.setText(object.toString());

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
                        } else if (dataTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Integer")) {// if input data is Integer type


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

                        } else if (dataTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Map of <String,String>/<String,Integer>")) {// if input data is Map<String,String>
                            JSONObject jObject = new JSONObject(enteredValue); // json

                            Map hmS = jsonToMap(jObject);
                            // hmS.put("Key1", "value1");
                            // hmS.put("Key2", "value2");
                            value = hmS;
                        }else if (dataTypeSpinner.getSelectedItem().toString().equalsIgnoreCase("Delete")) {// if input data is Null
                            value = null; // this value will be deleted
                        }
                    } catch (Exception e) {
                        isInputDataValid = false; // if parsing String and Integer fails
                    }
                    if (isInputDataValid) {
                        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();
                        boolean success = false;
                        try {
                            success = mConfigInterface.setPropertyForKey(setKeyET.getText().toString(), setGroupKeyET.getText().toString(), value, configError);
                        } catch (IllegalArgumentException e) {
                            Log.e(getClass() + "", " Illegal argument exception ");
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

        Button btnFetchCloudConfig = (Button) findViewById(R.id.btn_fetchCloudConfig);//
        final TextView cloudConfigResponse = (TextView) findViewById(R.id.cloudConfigResponse);
        btnFetchCloudConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cloudConfigResponse.setText(null);
                mConfigInterface.refreshCloudConfig(new AppConfigurationInterface.OnRefreshListener() {
                    @Override
                    public void onError(AppConfigurationInterface.AppConfigurationError.AppConfigErrorEnum error, String message) {
                        Log.v("refreshCloudConfig",message);
                    }

                    @Override
                    public void onSuccess(REFRESH_RESULT result) {
                        Log.v("refreshCloudConfig",result.toString());
                        cloudConfigResponse.setText(result.toString());
                    }
                });
            }
        });

        Button btResetConfig = (Button) findViewById(R.id.btn_resetConfig);
        btResetConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mConfigInterface.resetConfig();
                Toast.makeText(AppConfigurationActivity.this,"RESET CONFIG" ,Toast.LENGTH_LONG).show();
            }
        });

    }

    private Map jsonToMap(Object JSON) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();
        JSONObject jObject = new JSONObject(JSON.toString());
        Iterator<?> keys = jObject.keys();

        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = jObject.get(key);
            map.put(key, value);
        }
        return map;
    }
}
