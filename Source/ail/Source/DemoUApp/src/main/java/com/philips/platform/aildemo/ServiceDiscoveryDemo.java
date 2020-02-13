package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 310238655 on 6/7/2016.
 */
public class ServiceDiscoveryDemo extends AppCompatActivity implements ServiceDiscoveryInterface.OnGetHomeCountryListener, ServiceDiscoveryInterface.OnGetServiceUrlMapListener {

    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryInterface.OnGetHomeCountryListener mOnGetHomeCountryListener = null;
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener mOnGetServiceUrlMapListener = null;
    AppInfraInterface appInfra;

    TextView resultView;
    EditText idEditText;
    EditText idEditTextCountry;
    String editTextData;

    String[] requestTypeOption = {
            "Get home country",
            "Get services by lang",
            "Get services by country",
            "Refresh",
            "Get home country Synchronous"
    };

    private Button getUrl;
    private Spinner requestTypeSpinner;
    private HashMap<String, String> parameters;
    private HomeCountryUpdateReceiver receiver;
    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInfra = AILDemouAppInterface.getInstance().getAppInfra();

        mServiceDiscoveryInterface = appInfra.getServiceDiscovery();
        mOnGetHomeCountryListener = this;
        mOnGetServiceUrlMapListener = this;

        setContentView(R.layout.service_discovery_demopage);
        SecureStorageInterface mSecureStorage = appInfra.getSecureStorage();

        String enc = "4324332423432432432435425435435346465464547657567.000343242342";

        try {
            plainByte = enc.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
        encryptedByte = mSecureStorage.encryptData(plainByte, sseStore);
        try {
            String encBytesString = new String(encryptedByte, "UTF-8");
            Log.e("Encrypted Data", encBytesString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] plainData = mSecureStorage.decryptData(encryptedByte, sseStore);
        String result = Arrays.equals(plainByte, plainData) ? "True" : "False";
        try {
            String decBytesString = new String(plainByte, "UTF-8");
            Log.e("Decrypted Data", decBytesString);
        } catch (UnsupportedEncodingException e) {
        }

        getUrl = (Button) findViewById(R.id.geturl);
        requestTypeSpinner = (Spinner) findViewById(R.id.requestspinner);
        ArrayAdapter<String> input_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, requestTypeOption);
        requestTypeSpinner.setAdapter(input_adapter);


        idEditText = (EditText) findViewById(R.id.serviceid_editText);
        Button setHomeCountry = (Button) findViewById(R.id.button2);
        idEditTextCountry = (EditText) findViewById(R.id.contry_edittext);

        editTextData = idEditText.getText().toString();

        resultView = (TextView) findViewById(R.id.textView2);


        receiver = new HomeCountryUpdateReceiver();
        mServiceDiscoveryInterface.registerOnHomeCountrySet(receiver);

        setHomeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = idEditTextCountry.getText().toString();
                if (country.length() == 2) {
                    mServiceDiscoveryInterface.setHomeCountry(country.toUpperCase());
                }

            }
        });

        JSONObject json = getMasterConfigFromApp();
        try {
            parameters = (HashMap<String, String>) jsonToMap(json);
        } catch (JSONException e) {
        }

        getUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get home country")) {
                    editTextData = idEditText.getText().toString();
                    mServiceDiscoveryInterface.getHomeCountry(mOnGetHomeCountryListener);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get services by lang")) {
                    String[] serviceIds = idEditText.getText().toString().split(",");
                    ArrayList<String> serviceIDList = new ArrayList<String>(Arrays.asList(serviceIds));
                    mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceIDList,mOnGetServiceUrlMapListener,null);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get services by country")) {
                    String[] serviceIds = idEditText.getText().toString().split(",");
                    ArrayList<String> serviceIDList = new ArrayList<String>(Arrays.asList(serviceIds));
                    mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceIDList,mOnGetServiceUrlMapListener,null);
                }else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Refresh")) {
                    mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
                        @Override
                        public void onSuccess() {
                            resultView.setText("SD REFRESH Success");
                            Log.i("SD REFRESH", "Success");
                        }
                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            resultView.setText("SD REFRESH Error:  " + message);
                            Log.i("SD REFRESH", "Error");
                        }
                    },false);

                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get home country Synchronous")) {
                    String homeCountry = mServiceDiscoveryInterface.getHomeCountry();
                    Toast.makeText(ServiceDiscoveryDemo.this, "Home country is " + homeCountry, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected JSONObject getMasterConfigFromApp() {
        JSONObject result = null;
        try {
            InputStream mInputStream = this.getAssets().open("SDReplacementValues.json");
            BufferedReader r = new BufferedReader(new InputStreamReader(mInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            result = new JSONObject(total.toString());
            appInfra.getLogging().log(LoggingInterface.LogLevel.VERBOSE, "Json",
                    result.toString());

        } catch (Exception e) {
            appInfra.getLogging().log(LoggingInterface.LogLevel.ERROR, "Service Discover exception",
                    Log.getStackTraceString(e));
        }
        return result;
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


    @Override
    public void onError(ERRORVALUES error, String message) {
        Log.i("onError", "" + message);
        resultView.setText(message);
    }

    @Override
    public void onSuccess(String countryCode, SOURCE source) {
        resultView.setText("Country Code : " + countryCode + " Source : " + source);
    }

    @Override
    public void onSuccess(Map urlMap) {
        ServiceDiscoveryService service = new ServiceDiscoveryService();

        String key = null;
        String locale = null;
        String configUrl = null;
        Iterator it = urlMap.entrySet().iterator();
        Map mMap = new HashMap<String, Map>();

        while (it.hasNext()) {
            Map dataMap = new HashMap<String, String>();
            Map.Entry pair = (Map.Entry) it.next();
            key = pair.getKey().toString();
            service = (ServiceDiscoveryService) pair.getValue();
            locale = service.getLocale();
            configUrl = service.getConfigUrls();

            if (configUrl != null) {
                dataMap.put(locale, configUrl);
            } else {
                dataMap.put(locale, service.getmError());
            }
            mMap.put(key, dataMap);
            it.remove(); // avoids a ConcurrentModificationException
        }
        resultView.setText(" URL Model   : " + mMap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mServiceDiscoveryInterface.unRegisterHomeCountrySet(receiver);
    }
}
