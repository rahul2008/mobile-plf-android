package com.philips.platform.appinfra.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by 310238114 on 8/26/2016.
 */
public class SecureStorageMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.secure_storage_menu);
        Button secureStorageButton   = (Button) findViewById(R.id.secureStorage);
        secureStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecureStorageMenuActivity.this,SecureStorageActivity.class);
                startActivity(intent);

            }
        });
        Button encryptDecryptButton   = (Button) findViewById(R.id.EncryptDecrypt);
        encryptDecryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SecureStorageMenuActivity.this, SecureStorageEncryptDecryptActivity.class);
                startActivity(i);
            }
        });
    }

}
