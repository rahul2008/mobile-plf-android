package com.philips.platform.aildemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.securestoragev1.SecureStorageV1;

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

    private AppInfraInterface appInfra;

    private static final String IS_OLD_SS_ENABLED="is_ols_ss_enabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secure_storage_encrypt_decrypt);
        appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        ToggleButton toggleButton= (ToggleButton)findViewById(R.id.toggleButton);
        isOldSSEnabled=getIntent().getBooleanExtra(Constants.IS_OLD_SS_ENABLED,false);
        toggleButton.setChecked(isOldSSEnabled);
        if(isOldSSEnabled) {
            mSecureStorage = new SecureStorageV1(appInfra);
            Toast.makeText(this,"Old secure storage is enabled",Toast.LENGTH_SHORT).show();
        }else{
            mSecureStorage=appInfra.getSecureStorage();
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
                appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG,"SecureStorageNFRTesting","before encryptData::"+System.currentTimeMillis());
                encryptedByte = mSecureStorage.encryptData(plainByte, sseStore);
                appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG,"SecureStorageNFRTesting","after encryptData::"+System.currentTimeMillis());
                if (null != sseStore.getErrorCode()) {
                    Toast.makeText(SecureStorageEncryptDecryptActivity.this, sseStore.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                } else {
                    String encBytesString = new String(encryptedByte);
                    textViewEncrypt.setText(encBytesString);
                }


            }
        });



       toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
               Intent intent=new Intent(SecureStorageEncryptDecryptActivity.this,SecureStorageEncryptDecryptActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
               intent.putExtra(IS_OLD_SS_ENABLED,isChecked);
               startActivity(intent);
           }
       });

        Button decryptButton = (Button) findViewById(R.id.buttonDecrypt);
        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
                appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG,"SecureStorageNFRTesting","before decryptData::"+System.currentTimeMillis());
                byte[] plainData = mSecureStorage.decryptData(encryptedByte, sseStore);
                appInfra.getLogging().log(LoggingInterface.LogLevel.DEBUG,"SecureStorageNFRTesting","after decryptData::"+System.currentTimeMillis());
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        boolean isOldSSEnabled=intent.getBooleanExtra(IS_OLD_SS_ENABLED,false);
        if(isOldSSEnabled) {
            mSecureStorage = new SecureStorageV1(appInfra);
            Toast.makeText(this,"Old secure storage is enabled",Toast.LENGTH_SHORT).show();
        }else{
            mSecureStorage=appInfra.getSecureStorage();
        }

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
