/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.aildemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.securestoragev1.SecureStorageV1;


public class SecureStorageActivity extends AppCompatActivity  {
    SecureStorageInterface mSecureStorage=null;
    boolean isOldSSEnabled;

    TextView statusTextView;
    private AppInfraInterface appInfra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_storage);
        appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        isOldSSEnabled=getIntent().getBooleanExtra(Constants.IS_OLD_SS_ENABLED,false);
        if(isOldSSEnabled) {
            mSecureStorage = new SecureStorageV1(appInfra);
            Toast.makeText(this,"Old secure storage is enabled",Toast.LENGTH_SHORT).show();
        }else{
            mSecureStorage= appInfra.getSecureStorage();
        }

        final EditText userKey = (EditText) findViewById(R.id.Key_editText);
        final  EditText data = (EditText) findViewById(R.id.data_editText);

        final  TextView decryptedDataTextView = (TextView) findViewById(R.id.decripted_Output_textView);
        statusTextView = findViewById(R.id.status_desc_text_view);


        Button encryptButton = (Button) findViewById(R.id.encript_button);

        assert encryptButton != null;
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
                decryptedDataTextView.setText(null);
                appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG,"SecureStorageNFRTesting","before storeValueForKey::"+System.currentTimeMillis());
                boolean isSaved = mSecureStorage.storeValueForKey(userKey.getText().toString(), data.getText().toString(),sseStore);
                appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG,"SecureStorageNFRTesting","after storeValueForKey::"+System.currentTimeMillis());
                if(null!=sseStore.getErrorCode())
                {
                    statusTextView.setText(sseStore.getErrorCode().toString());
                    Toast.makeText(SecureStorageActivity.this,sseStore.getErrorCode().toString(),Toast.LENGTH_SHORT).show();
                }else{
                    statusTextView.setText("No Error");
                }
              

            }
        });

        Button decryptButton = (Button) findViewById(R.id.decypt_button);

        assert decryptButton != null;
        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
                appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG,"SecureStorageNFRTesting","before fetchValueForKey::"+System.currentTimeMillis());
                String decryptedData= mSecureStorage.fetchValueForKey(userKey.getText().toString(),sse);
                appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG,"SecureStorageNFRTesting","after fetchValueForKey::"+System.currentTimeMillis());
                if(null!=sse.getErrorCode())
                {
                    statusTextView.setText(sse.getErrorCode().toString());
                    Toast.makeText(SecureStorageActivity.this,sse.getErrorCode().toString(),Toast.LENGTH_SHORT).show();
                }else{
                    statusTextView.setText("No Error");
                    decryptedDataTextView.setText(decryptedData);

                }
                /*if(null==decryptedData){
                    Toast.makeText(SecureStorageActivity.this,"Key not found",Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteData_button);

        assert deleteButton != null;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               boolean result =  mSecureStorage.removeValueForKey(userKey.getText().toString());
                if(result) {
                    statusTextView.setText("Deletion successful");
                    data.setText(null);
                    userKey.setText(null);
                    decryptedDataTextView.setText(null);
                }else{
                    statusTextView.setText("Deletion failed");
                    Toast.makeText(SecureStorageActivity.this,"Deletion failed",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    public void onClickBulk(View view){
        Intent intent = new Intent(this,SecureStorageBulkActivity.class);
        startActivity(intent);
    }



}
