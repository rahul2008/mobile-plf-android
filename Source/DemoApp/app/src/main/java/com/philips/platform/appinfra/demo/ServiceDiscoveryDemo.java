package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 310238655 on 6/7/2016.
 */
public class ServiceDiscoveryDemo extends AppCompatActivity implements ServiceDiscoveryInterface.OnGetServiceLocaleListener, ServiceDiscoveryInterface.OnGetServiceUrlListener, ServiceDiscoveryInterface.OnGetHomeCountryListener, ServiceDiscoveryInterface.OnGetServiceUrlMapListener {

    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryInterface.OnGetServiceLocaleListener mOnGetServiceLocaleListener = null;
    ServiceDiscoveryInterface.OnGetServiceUrlListener mOnGetServiceUrlListener = null;
    ServiceDiscoveryInterface.OnGetHomeCountryListener mOnGetHomeCountryListener = null;
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener mOnGetServiceUrlMapListener = null;
    AppInfraInterface appInfra;

    TextView resultView;
    EditText idEditText;
    EditText idEditTextCountry;
    String editTextData;
    private Button getUrl;
    private Spinner requestTypeSpinner;

    String[] requestTypeOption = {"Get local by lang",
            "Get local by country",
            "Get url by lang",
            "Get url by country",
            "Get home country",
            "Get Url by country with multiple service id",
            "Get Url by language with multiple service id",
            "Get Url by country with replaced url",
            "Get Url by language with replaced url",
            "Get replaced Url by country with multiple service id",
            "Get replaced Url by Language with multiple service id"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInfra = AppInfraApplication.gAppInfra;
        mServiceDiscoveryInterface = appInfra.getServiceDiscovery();
        mOnGetServiceLocaleListener = this;
        mOnGetServiceUrlListener = this;
        mOnGetHomeCountryListener = this;
        mOnGetServiceUrlMapListener = this;

        setContentView(R.layout.service_discovery_demopage);
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

        setHomeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = idEditTextCountry.getText().toString();
                if (country.length() == 2) {
                    mServiceDiscoveryInterface.setHomeCountry(country.toUpperCase());
                }

            }
        });

        getUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get local by lang")) {
                    editTextData = idEditText.getText().toString();
                    mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(editTextData, mOnGetServiceLocaleListener);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get local by country")) {
                    editTextData = idEditText.getText().toString();
                    mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(editTextData, mOnGetServiceLocaleListener);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get url by lang")) {
                    editTextData = idEditText.getText().toString();
                    mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(editTextData, mOnGetServiceUrlListener);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get url by country")) {
                    editTextData = idEditText.getText().toString();
                    mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(editTextData, mOnGetServiceUrlListener);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get home country")) {
                    editTextData = idEditText.getText().toString();
                    mServiceDiscoveryInterface.getHomeCountry(mOnGetHomeCountryListener);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get Url by country with multiple service id")) {
                    String[] serviceIds = idEditText.getText().toString().split(",");
                    ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
                    mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, mOnGetServiceUrlMapListener);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get Url by language with multiple service id")) {
                    String[] serviceIds = idEditText.getText().toString().split(",");
                    ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
                    mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, mOnGetServiceUrlMapListener);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get Url by country with replaced url")) {
                    editTextData = idEditText.getText().toString();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("ctn", "HD9740");
                    parameters.put("sector", "B2C");
                    parameters.put("catalog", "shavers");
                    mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(editTextData, mOnGetServiceUrlListener, parameters);

                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get Url by language with replaced url")) {
                    editTextData = idEditText.getText().toString();
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("ctn", "HD9740");
                    parameters.put("sector", "B2C");
                    parameters.put("catalog", "shavers");
                    mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(editTextData, mOnGetServiceUrlListener, parameters);
                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get replaced Url by country with multiple service id")) {

                    String[] serviceIds = idEditText.getText().toString().split(",");
                    ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("ctn", "HD9740");
                    parameters.put("sector", "B2C");
                    parameters.put("catalog", "shavers");
                    mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, mOnGetServiceUrlMapListener, parameters);

                } else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Get replaced Url by Language with multiple service id")) {

                    String[] serviceIds = idEditText.getText().toString().split(",");
                    ArrayList<String> serviceId = new ArrayList<>(Arrays.asList(serviceIds));
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("ctn", "HD9740");
                    parameters.put("sector", "B2C");
                    parameters.put("catalog", "shavers");
                    mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId, mOnGetServiceUrlMapListener, parameters);

                }
//                else if (requestTypeSpinner.getSelectedItem().toString().trim().equalsIgnoreCase("Replace Url")) {
//                    Map<String, String> parameters = new HashMap<>();
//                    parameters.put("ctn", "HD9740");
//                    parameters.put("sector", "B2C");
//                    parameters.put("catalog", "shavers");
//
//                    url = new URL("https://acc.philips.com/prx/product/%sector%/ar_RW/%catalog%/products/%ctn%.assets");
//                    URL newURl = mServiceDiscoveryInterface.replacePlaceholders(url, parameters);
//
//
//                }
            }
        });
    }

    @Override
    public void onSuccess(String services) {
        Log.i("OnGetServicesListener", "" + services);
        resultView.setText(services);
    }

    @Override
    public void onError(ERRORVALUES error, String message) {
        Log.i("onError", "" + message);
        resultView.setText(message);
    }

    @Override
    public void onSuccess(URL url) {
        Log.i("Success", "" + url);
        try {
//            Map<String, String> parameters = new HashMap<>();
//            parameters.put("ctn", "HD9740");
//            parameters.put("sector", "B2C");
//            parameters.put("catalog", "shavers");
//
//            url = new URL("https://acc.philips.com/prx/product/%sector%/ar_RW/%catalog%/products/%ctn%.assets");
//            URL newURl = mServiceDiscoveryInterface.replacePlaceholders(url, parameters);
            resultView.setText("" + url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String countryCode, SOURCE source) {
        resultView.setText("Country Code : " + countryCode + " Source : " + source);
    }

    @Override
    public void onSuccess(Map urlMap) {
        ServiceDiscoveyService service = new ServiceDiscoveyService();

        String key = null;
        String locale = null;
        String configUrl = null;
        Iterator it = urlMap.entrySet().iterator();
        Map mMap = new HashMap<String, Map>();

        while (it.hasNext()) {

            Map dataMap = new HashMap<String, String>();
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            key = pair.getKey().toString();
            service = (ServiceDiscoveyService) pair.getValue();
            locale = service.getLocale();
            configUrl = service.getConfigUrls();

            dataMap.put(locale, configUrl);
            mMap.put(key, dataMap);
            it.remove(); // avoids a ConcurrentModificationException
        }

//        for (int i = 0; i < urlMap.size(); i++)
//        {
//            Log.i("SD", ""+urlMap.get(arryaLsit.get(i)).getConfigUrls());
//            Log.i("SD", ""+urlMap.get(i).getConfigUrls());
//        }
        resultView.setText(" URL Model   : " + mMap);

    }
}
