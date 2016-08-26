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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by 310238114 on 8/26/2016.
 */
public class SecureStorageEncryptDecryptActivity extends AppCompatActivity {
    SecureStorageInterface mSecureStorage=null;
    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secure_storage_encrypt_decrypt);
        AppInfraInterface appInfra = AppInfraApplication.gAppInfra;
        mSecureStorage = appInfra.getSecureStorage();

        final TextView textViewEncrypt = (TextView)findViewById(R.id.textViewEncrypted);
        final TextView textViewDecrypted = (TextView)findViewById(R.id.textViewDecrpted);
        final TextView textViewDataMatched = (TextView)findViewById(R.id.textViewDataMatched);

        final EditText et = (EditText)findViewById(R.id.plainText);
        Button encryptButton   = (Button) findViewById(R.id.buttonEncrypt);
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text= et.getText().toString();
                textViewDecrypted.setText(null);
                textViewDataMatched.setText(null);
                plainByte=null;
                if(null==text || text.isEmpty()){
                    Toast.makeText(SecureStorageEncryptDecryptActivity.this,"NullData",Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    plainByte= text.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
                encryptedByte=mSecureStorage.encryptData(plainByte,sseStore);
                if(null!=sseStore.getErrorCode())
                {
                    Toast.makeText(SecureStorageEncryptDecryptActivity.this,sseStore.getErrorCode().toString(),Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        String encBytesString = new String(encryptedByte, "UTF-8");
                        textViewEncrypt.setText(encBytesString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }


            }
        });

        Button decryptButton   = (Button) findViewById(R.id.buttonDecrypt);
        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
                byte[] plainData= mSecureStorage.decryptData(encryptedByte,sseStore);
                if(null!=sseStore.getErrorCode())
                {
                    Toast.makeText(SecureStorageEncryptDecryptActivity.this,sseStore.getErrorCode().toString(),Toast.LENGTH_SHORT).show();
                }else{
                    String  result = Arrays.equals(plainByte,plainData)?"True":"False";
                    textViewDataMatched.setText(result);
                    try {
                        String decBytesString = new String(plainByte, "UTF-8");
                        textViewDecrypted.setText(decBytesString);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }
}
