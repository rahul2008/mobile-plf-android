/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.securestoragev1.SecureStorageV1;

import java.security.Key;


public class SecureStoragePasswordActivity extends AppCompatActivity {
    SecureStorageInterface mSecureStorage = null;
    private boolean isOldSSEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secure_storage_password);
        AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        isOldSSEnabled=getIntent().getBooleanExtra(Constants.IS_OLD_SS_ENABLED,false);
        if(isOldSSEnabled) {
            mSecureStorage = new SecureStorageV1(appInfra);
            Toast.makeText(this,"Old secure storage is enabled",Toast.LENGTH_SHORT).show();
        }else{
            mSecureStorage=appInfra.getSecureStorage();
        }
        final EditText userKey = (EditText) findViewById(R.id.Key_editText);
        final TextView output_textView = (TextView) findViewById(R.id.output_textView);


        Button passWordButton = (Button) findViewById(R.id.create_password);

        assert passWordButton != null;
        passWordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any

                boolean isSaved = mSecureStorage.createKey(SecureStorageInterface.KeyTypes.AES, userKey.getText().toString(), sseStore);
                if (null != sseStore.getErrorCode()) {
                    Toast.makeText(SecureStoragePasswordActivity.this, sseStore.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    output_textView.setText("Create Key Successfully");
                }

            }
        });

        Button retrieveButton = (Button) findViewById(R.id.retrieve);

        assert retrieveButton != null;
        retrieveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any

                   Key decryptedData = mSecureStorage.getKey(userKey.getText().toString(), sse);
                  if(decryptedData!=null) {
                      String keyString = Base64.encodeToString(decryptedData.getEncoded(), Base64.DEFAULT);
                      if (null != sse.getErrorCode()) {
                          Toast.makeText(SecureStoragePasswordActivity.this, sse.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                      } else {
                          output_textView.setText(keyString);

                      }
                  }
            else {
                      output_textView.setText(null);
                Toast.makeText(SecureStoragePasswordActivity.this, "Key is not found", Toast.LENGTH_SHORT).show();
            }

            }
        });


        Button deleteButton = (Button) findViewById(R.id.delete_button);

        assert deleteButton != null;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError();

                boolean result = mSecureStorage.clearKey(userKey.getText().toString(), sse);
                if (result) {
                    output_textView.setText("Deleted key successfully");
                    userKey.setText(null);
                } else {
                    output_textView.setText(null);
                    Toast.makeText(SecureStoragePasswordActivity.this, "Deletion failed", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


}
