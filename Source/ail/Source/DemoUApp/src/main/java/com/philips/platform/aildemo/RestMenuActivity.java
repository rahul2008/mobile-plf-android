package com.philips.platform.aildemo;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.philips.platform.appinfra.demo.R;

/**
 * Created by 310238114 on 10/20/2016.
 */
public class RestMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_menu);
        Button secureStorageButton   = (Button) findViewById(R.id.restURL);
        secureStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RestMenuActivity.this,RestClientActivity.class);
                startActivity(intent);

            }
        });
        Button encryptDecryptButton   = (Button) findViewById(R.id.restServiceId);
        encryptDecryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RestMenuActivity.this, RestClientServiceIdActivity.class);
                startActivity(i);
            }
        });
    }

}