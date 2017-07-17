package com.philips.platform.aildemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

/**
 * Created by 310238114 on 8/26/2016.
 */
public class SecureStorageMenuActivity extends AppCompatActivity {

	TextView deviceCapable;
	TextView deviceLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secure_storage_menu);
		AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
		SecureStorageInterface mSecureStorage = appInfra.getSecureStorage();

		deviceCapable = (TextView) findViewById(R.id.devicecapableStatus);
		deviceCapable.setText(mSecureStorage.getDeviceCapability());

		deviceLock = (TextView) findViewById(R.id.devicelockStatus);
		deviceLock.setText(Boolean.toString(mSecureStorage.deviceHasPasscode()));

		Button secureStorageButton = (Button) findViewById(R.id.secureStorage);
		secureStorageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SecureStorageMenuActivity.this, SecureStorageActivity.class);
				startActivity(intent);

			}
		});
		Button encryptDecryptButton = (Button) findViewById(R.id.EncryptDecrypt);
		encryptDecryptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(SecureStorageMenuActivity.this, SecureStorageEncryptDecryptActivity.class);
				startActivity(i);
			}
		});
		Button passWordCreationButton = (Button) findViewById(R.id.createPaaasword);
		passWordCreationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SecureStorageMenuActivity.this, SecureStoragePasswordActivity.class);
				startActivity(intent);

			}
		});
	}

}
