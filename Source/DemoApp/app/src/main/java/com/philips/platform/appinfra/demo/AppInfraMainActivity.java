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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AppInfraMainActivity extends AppCompatActivity {


    ListView listView;
    String appInfraComponents[] = {"Secure Storage", "AIAppTagging", "Logging", "Prx", "LocalMatch", "AppIdentity", "LocalMain"};

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
    }


    private void launchAppInfraActivities(int position) {
        switch (position) {
            case 0 :
               /* Toast toast = Toast.makeText(getContext(), "Launch your activity here", Toast.LENGTH_SHORT);
                toast.show();*/
                Intent intent = new Intent(AppInfraMainActivity.this,SecureStorageActivity.class);
                startActivity(intent);
                break;
            case 1 :
                //                AIAppTagging.enableAppTagging(true);
//                //Mandatory to set
//                AIAppTagging.setTrackingIdentifier(ANALYTICS_APP_ID);
//                AIAppTagging.init(Locale.CHINA, getActivity(), "App Framwork demo app");
                Intent i = new Intent(AppInfraMainActivity.this, AIATDemoPage.class);
                startActivity(i);
                break;
            case 2 :
                Intent intentLoggingActivity = new Intent(AppInfraMainActivity.this, LoggingActivity.class);
                startActivity(intentLoggingActivity);

                break;
            case 3 :
                Intent intentPrxActivity = new Intent(AppInfraMainActivity.this,
                        PrxLauncherActivity.class);
                startActivity(intentPrxActivity);

                break;
            case 4 :
                Intent intentlocalMatchActivity = new Intent(AppInfraMainActivity.this,
                        LocalMatchActivity.class);
                startActivity(intentlocalMatchActivity);

                break;
            case 5 :
                Intent intentAppIdentityActivity = new Intent(AppInfraMainActivity.this,
                        AppIndentityDemoPage.class);
                startActivity(intentAppIdentityActivity);

                break;
            case 6 :
                Intent intentLocalMainActivity = new Intent(AppInfraMainActivity.this,
                        LocalMainDemoPage.class);
                startActivity(intentLocalMainActivity);

                break;

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
