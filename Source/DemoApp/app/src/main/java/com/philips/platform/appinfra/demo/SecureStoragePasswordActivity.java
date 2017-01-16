/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;


public class SecureStoragePasswordActivity extends AppCompatActivity {
    SecureStorageInterface mSecureStorage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secure_storage_password);
        AppInfraInterface appInfra = AppInfraApplication.gAppInfra;
        mSecureStorage = appInfra.getSecureStorage();

        final EditText userKey = (EditText) findViewById(R.id.Key_editText);
        final EditText passWord = (EditText) findViewById(R.id.password_editText);
        final TextView output_textView = (TextView) findViewById(R.id.output_textView);


        Button passWordButton = (Button) findViewById(R.id.create_password);

        assert passWordButton != null;
        passWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any

                boolean isSaved = mSecureStorage.storeValueForKey(userKey.getText().toString(), passWord.getText().toString(), sseStore);
                if (null != sseStore.getErrorCode()) {
                    Toast.makeText(SecureStoragePasswordActivity.this, sseStore.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    output_textView.setText("PassWord Successfully");
                }


            }
        });

        Button retrieveButton = (Button) findViewById(R.id.retrieve);

        assert retrieveButton != null;
        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
                String decryptedData = mSecureStorage.fetchValueForKey(userKey.getText().toString(), sse);
                if (null != sse.getErrorCode()) {
                    Toast.makeText(SecureStoragePasswordActivity.this, sse.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    output_textView.setText(decryptedData);

                }
                /*if(null==decryptedData){
                    Toast.makeText(SecureStorageActivity.this,"Key not found",Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        Button deleteButton = (Button) findViewById(R.id.delete_button);

        assert deleteButton != null;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean result = mSecureStorage.removeValueForKey(userKey.getText().toString());
                if (result) {
                    passWord.setText(null);
                    userKey.setText(null);
                    output_textView.setText(null);
                } else {
                    Toast.makeText(SecureStoragePasswordActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}
