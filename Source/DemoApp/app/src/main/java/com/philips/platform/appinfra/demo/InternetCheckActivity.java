/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.security.Key;


public class InternetCheckActivity extends AppCompatActivity {
    RestInterface mRestInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_check);
        AppInfraInterface appInfra = AppInfraApplication.gAppInfra;
        mRestInterface = appInfra.getRestClient();

        final TextView output_textView = (TextView) findViewById(R.id.output_textView);


        Button networkInfo = (Button) findViewById(R.id.networkInfo);

        assert networkInfo != null;
        networkInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                output_textView.setText("Network Info or Connection Type : "+mRestInterface.getNetworkReachabilityStatus());
            }
        });

        Button isNetWork = (Button) findViewById(R.id.isNetWork);

        assert isNetWork != null;
        isNetWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                output_textView.setText("Is Network Reachable: " + mRestInterface.isInternetReachable());

            }
        });


    }
}
