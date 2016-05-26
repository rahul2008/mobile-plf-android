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

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;


public class SecureStorageActivity extends AppCompatActivity  {
    SecureStorageInterface mSecureStorage=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_storage);
      //  mSecureStorage = new SecureStorage(getApplicationContext());
        AppInfra appInfra = AppInfraApplication.gAppInfra;
        mSecureStorage = appInfra.getSecureStorage();

        final EditText userKey = (EditText) findViewById(R.id.Key_editText);
        final  EditText data = (EditText) findViewById(R.id.data_editText);
        final TextView encryptedDataTextView = (TextView) findViewById(R.id.encripted_Ouput_textView);
        final  TextView decryptedDataTextView = (TextView) findViewById(R.id.decripted_Output_textView);


        Button encryptButton = (Button) findViewById(R.id.encript_button);

        assert encryptButton != null;
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mSecureStorage.storeValueForKey(userKey.getText().toString(), data.getText().toString());
                if(null==SecureStorage.encryptedTextTemp){
                    Toast.makeText(SecureStorageActivity.this, "Key or Value incorrect", Toast.LENGTH_SHORT).show();;
                }
                encryptedDataTextView.setText(SecureStorage.encryptedTextTemp);

            }
        });

        Button decryptButton = (Button) findViewById(R.id.decypt_button);

        assert decryptButton != null;
        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String decryptedData= mSecureStorage.fetchValueForKey(userKey.getText().toString());
                if(null==decryptedData){
                    Toast.makeText(SecureStorageActivity.this,"Key not found",Toast.LENGTH_SHORT).show();;
                }
                decryptedDataTextView.setText(decryptedData);
                System.out.println("MySuccess2");
            }
        });

        Button deleteButton = (Button) findViewById(R.id.deleteData_button);

        assert deleteButton != null;
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSecureStorage.removeValueForKey(userKey.getText().toString());
                data.setText(null);
                encryptedDataTextView.setText(null);
                decryptedDataTextView.setText(null);
            }
        });



    }



}
