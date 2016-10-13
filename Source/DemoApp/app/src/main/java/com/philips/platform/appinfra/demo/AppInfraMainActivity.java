/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class AppInfraMainActivity extends AppCompatActivity {


    ListView listView;
    String appInfraComponents[] = {"Secure Storage", "AppTagging", "Logging", "Prx","AppIdentity",
            "Internationalization", "ServiceDiscovery", "TimeSync", "Config", "Rest Client" , " A/B Testing"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_infra_main);

        listView = (ListView) findViewById(R.id.listViewAppInfraComponents);
        listView.setAdapter(new AppInfraListAdapter());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchAppInfraActivities(position);
            }
        });

        final ArrayList arryaLsit = new ArrayList();
        arryaLsit.add("appinfra.testing.service");
//        arryaLsit.add("userreg.janrain.cdn");
//        arryaLsit.add("userreg.landing.emailverif");
//        arryaLsit.add("userreg.landing.resetpass");

        AppInfraApplication.gAppInfra.getServiceDiscovery().getServicesWithCountryPreference(arryaLsit, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                for (int i = 0; i < urlMap.size(); i++)
                {
                    Log.i("SDTest", ""+urlMap.get(arryaLsit.get(i)).getConfigUrls());
                }
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("SD", ""+message);
            }
        });
        AppInfraApplication.gAppInfra.getServiceDiscovery().getServicesWithLanguagePreference(arryaLsit, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                for (int i = 0; i < urlMap.size(); i++)
                {
                    Log.i("SDTest", ""+urlMap.get(arryaLsit.get(i)).getConfigUrls());
                }
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("SD", ""+message);
            }
        });

        AppInfraApplication.gAppInfra.getServiceDiscovery().getServicesWithCountryPreference(arryaLsit, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                for (int i = 0; i < urlMap.size(); i++)
                {
                    Log.i("SD", ""+urlMap.get(arryaLsit.get(i)).getConfigUrls());
                }
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("SD", ""+message);
            }
        });
        AppInfraApplication.gAppInfra.getServiceDiscovery().getServicesWithLanguagePreference(arryaLsit, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveyService> urlMap) {
                for (int i = 0; i < urlMap.size(); i++)
                {
                    Log.i("SD", ""+urlMap.get(arryaLsit.get(i)).getConfigUrls());
                }
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("SD", ""+message);
            }
        });

        AppInfraApplication.gAppInfra.getServiceDiscovery().getServiceUrlWithCountryPreference("userreg.janrain.api", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                Log.i("SD", ""+url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("SD", ""+message);
            }
        });
        AppInfraApplication.gAppInfra.getServiceDiscovery().getServiceUrlWithCountryPreference("userreg.janrain.cdn", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                Log.i("SD", ""+url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("SD", ""+message);
            }
        });

    }


    private void launchAppInfraActivities(int position) {
        switch (position) {
            case 0:
               /* Toast toast = Toast.makeText(getContext(), "Launch your activity here", Toast.LENGTH_SHORT);
                toast.show();*/
                Intent intent = new Intent(AppInfraMainActivity.this, SecureStorageMenuActivity.class);
                startActivity(intent);
                break;
            case 1:
                //                AppTagging.enableAppTagging(true);
//                //Mandatory to set
//                AppTagging.setTrackingIdentifier(ANALYTICS_APP_ID);
//                AppTagging.init(Locale.CHINA, getActivity(), "App Framwork demo app");
                Intent i = new Intent(AppInfraMainActivity.this, AIATDemoPage.class);
                startActivity(i);
                break;
            case 2:
                Intent intentLoggingActivity = new Intent(AppInfraMainActivity.this, LoggingActivity.class);
                startActivity(intentLoggingActivity);

                break;
            case 3:
                Intent intentPrxActivity = new Intent(AppInfraMainActivity.this,
                        PrxLauncherActivity.class);
                startActivity(intentPrxActivity);

                break;

            case 4:
                Intent intentAppIdentityActivity = new Intent(AppInfraMainActivity.this,
                        AppIndentityDemoPage.class);
                startActivity(intentAppIdentityActivity);

                break;
            case 5:
                Intent intentLocalMainActivity = new Intent(AppInfraMainActivity.this,
                        InternationalizationDemoPage.class);
                startActivity(intentLocalMainActivity);

                break;
            case 6:
                Intent intentServiceDiscoveryActivity = new Intent(AppInfraMainActivity.this,
                        ServiceDiscoveryDemo.class);
                startActivity(intentServiceDiscoveryActivity);

                break;
            case 7:
                Intent intentTimeSyncActivity = new Intent(AppInfraMainActivity.this,
                        TimeSyncDemo.class);
                startActivity(intentTimeSyncActivity);

                break;
            case 8:

                Intent configActivity = new Intent(AppInfraMainActivity.this,
                        AppConfigurationActivity.class);
                startActivity(configActivity);
                break;

            case 9:

                Intent restClientActivity = new Intent(AppInfraMainActivity.this,
                        RestClientActivity.class);
                startActivity(restClientActivity);
                break;

            case 10 :
                Intent abTesting = new Intent(AppInfraMainActivity.this , AbTestingDemo.class );
                startActivity(abTesting);
        }
    }

    class AppInfraListAdapter extends BaseAdapter {
        ViewHolder viewHolder;

        @Override
        public int getCount() {
            return appInfraComponents.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.app_infra_list_row, null);
                viewHolder = new ViewHolder();
                viewHolder.testModeLabel = (TextView) convertView.findViewById(R.id.AppInfraListRowTextLabel);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.testModeLabel.setText(appInfraComponents[position]);
            return convertView;
        }

        class ViewHolder {
            TextView testModeLabel;
        }
    }
}
