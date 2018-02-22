package com.philips.platform.aildemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.securestoragev2.SecureStorage2;

import java.util.Arrays;

/**
 * Created by 310238114 on 8/26/2016.
 */
public class SecureStorageEncryptDecryptActivity extends AppCompatActivity {
    SecureStorageInterface mSecureStorage=null;
    byte[] plainByte;
    byte[] encryptedByte;
    ScrollView encryptScrollView, decryptScrollView;
    private boolean isOldSSEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secure_storage_encrypt_decrypt);
        AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        isOldSSEnabled=getIntent().getBooleanExtra(Constants.IS_OLD_SS_ENABLED,false);
        if(isOldSSEnabled) {
            mSecureStorage = appInfra.getSecureStorage();
            Toast.makeText(this,"Old secure storage is enabled",Toast.LENGTH_SHORT).show();
        }else{
            mSecureStorage=new SecureStorage2((AppInfra) appInfra);
        }

        final TextView textViewEncrypt = (TextView)findViewById(R.id.textViewEncrypted);
        final TextView textViewDecrypted = (TextView)findViewById(R.id.textViewDecrpted);
        final TextView textViewDataMatched = (TextView)findViewById(R.id.textViewDataMatched);
        encryptScrollView = findViewById(R.id.SCROLLER_ID2);
        decryptScrollView = findViewById(R.id.SCROLLER_ID1);

        final EditText et = (EditText)findViewById(R.id.plainText);
        Button encryptButton   = (Button) findViewById(R.id.buttonEncrypt);
        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text= et.getText().toString();
                textViewDecrypted.setText(null);
                textViewDataMatched.setText(null);
                plainByte=null;
                if(text.isEmpty()){
                    Toast.makeText(SecureStorageEncryptDecryptActivity.this,"NullData",Toast.LENGTH_SHORT).show();
                    return;
                }
                plainByte= text.getBytes();
                SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
                encryptedByte = mSecureStorage.encryptData(plainByte, sseStore);
                if (null != sseStore.getErrorCode()) {
                    Toast.makeText(SecureStorageEncryptDecryptActivity.this, sseStore.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    String encBytesString = new String(encryptedByte);
                    textViewEncrypt.setText(encBytesString);
                }


            }
        });

        Button decryptButton = (Button) findViewById(R.id.buttonDecrypt);
        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
                byte[] plainData = mSecureStorage.decryptData(encryptedByte, sseStore);
                if (null != sseStore.getErrorCode()) {
                    Toast.makeText(SecureStorageEncryptDecryptActivity.this, sseStore.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    String result = Arrays.equals(plainByte, plainData) ? "True" : "False";
                    textViewDataMatched.setText(result);
                    String decBytesString = new String(plainByte);
                    textViewDecrypted.setText(decBytesString);
                }

            }
        });
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.buttonEncrypt_scroll_up) {
            encryptScrollView.fullScroll(ScrollView.FOCUS_UP);
        } else if (id == R.id.buttonEncrypt_scroll_bottom) {
            encryptScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        } else if (id == R.id.buttonDecrypt_scroll_up) {
            decryptScrollView.fullScroll(ScrollView.FOCUS_UP);
        } else if (id == R.id.buttonDecrypt_scroll_bottom) {
            decryptScrollView.fullScroll(ScrollView.FOCUS_DOWN);
        }
    }
}
