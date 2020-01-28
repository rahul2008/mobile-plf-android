/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class InternetCheckActivity extends AppCompatActivity {
    RestInterface mRestInterface = null;
    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_check);
        AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        mRestInterface = appInfra.getRestClient();
        String enc = "4324332423432432432435425435435346465464547657567.000343242342";

        try {
            plainByte= enc.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        SecureStorageInterface mSecureStorage = appInfra.getSecureStorage();

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
